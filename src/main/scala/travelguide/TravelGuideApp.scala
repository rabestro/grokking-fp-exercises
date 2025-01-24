package travelguide

import cats.effect.unsafe.implicits.global
import cats.effect.{IO, Ref, Resource}
import cats.implicits.*
import org.apache.jena.query.{QueryExecution, QueryFactory, QuerySolution}
import org.apache.jena.rdfconnection.{RDFConnection, RDFConnectionRemote}
import travelguide.AppRunner.runWithTiming
import travelguide.BusinessDomain.PopCultureSubject.{Artist, Movie}
import travelguide.BusinessDomain.{Attraction, LocationId, TravelGuide}
import travelguide.WikidataDataAccess.getSparqlDataAccess

import scala.concurrent.duration.*
import scala.jdk.javaapi.CollectionConverters.asScala

object TravelGuideApp {

  /** STEP 2
   * DATA ACCESS (just an interface providing pure functions, implementation completely separated)
   */
  enum AttractionOrdering {
    case ByName
    case ByLocationPopulation
  }
  import AttractionOrdering.*

  trait DataAccess {
    def findAttractions(name: String, ordering: AttractionOrdering, limit: Int): IO[List[Attraction]]

    def findArtistsFromLocation(locationId: LocationId, limit: Int): IO[List[Artist]]

    def findMoviesAboutLocation(locationId: LocationId, limit: Int): IO[List[Movie]]
  }

  /** STEP 6: searching for the best travel guide
   * requirements:
   * - 30 points for a description
   * - 10 points for each artist and movie (max 40pts)
   * - 1 point for each 100_000 followers (all artists combined, max 15pts)
   * - 1 point for each 10_000_000 dollars in box-office totals (all movies combined, max 15pts)
   */
  def guideScore(guide: TravelGuide): Int = {
    val descriptionScore = guide.attraction.description.map(_ => 30).getOrElse(0)
    val quantityScore = Math.min(40, guide.subjects.size * 10)

    val totalFollowers = guide.subjects
      .map {
        case Artist(_, followers) => followers
        case _ => 0
      }
      .sum
    val totalBoxOffice = guide.subjects
      .map {
        case Movie(_, boxOffice) => boxOffice
        case _ => 0
      }
      .sum

    val followersScore = Math.min(15, totalFollowers / 100_000)
    val boxOfficeScore = Math.min(15, totalBoxOffice / 10_000_000)
    descriptionScore + quantityScore + followersScore + boxOfficeScore
  }

  // PROBLEM: it may not work, because we are leaking closable resources (in this case query executions)

  /** STEP 7: handle resource leaks (query execution and connection)
   */
  def createExecution(connection: RDFConnection, query: String): IO[QueryExecution] = IO.blocking(
    connection.query(QueryFactory.create(query))
  )

  def closeExecution(execution: QueryExecution): IO[Unit] = IO.blocking(
    execution.close()
  )

  // introduce Resource
  def execQuery(connection: RDFConnection)(query: String): IO[List[QuerySolution]] = {
    val executionResource: Resource[IO, QueryExecution] = Resource.make(createExecution(connection, query))(
      closeExecution
    ) // or Resource.fromAutoCloseable(createExecution)

    executionResource.use(execution => IO.blocking(asScala(execution.execSelect()).toList))
  }

  val connectionResource: Resource[IO, RDFConnection] = Resource.make(
    IO.blocking(
      RDFConnectionRemote.create
        .destination("https://query.wikidata.org/")
        .queryEndpoint("sparql")
        .build
    )
  )(connection => IO.blocking(connection.close()))

  val dataAccessResource: Resource[IO, DataAccess] =
    connectionResource.map(connection => getSparqlDataAccess(execQuery(connection)))

  // PROBLEM: we are repeating the same queries, but the results don't change that often.

  /** STEP 9: make it faster
   * we don't have to execute queries, we can cache them locally
   */
  def cachedExecQuery(connection: RDFConnection, cache: Ref[IO, Map[String, List[QuerySolution]]])(
    query: String
  ): IO[List[QuerySolution]] = {
    for {
      cachedQueries <- cache.get
      solutions <- cachedQueries.get(query) match {
        case Some(cachedSolutions) => IO.pure(cachedSolutions)
        case None => for {
          realSolutions <- execQuery(connection)(query)
          _ <- cache.update(_.updated(query, realSolutions))
        } yield realSolutions
      }
    } yield solutions
  }

  private def runCachedVersion = {
    // Coffee Break
    // before:
    connectionResource.use(connection => {
      val dataAccess = getSparqlDataAccess(execQuery(connection))
      for {
        result1 <- AppVersion3.travelGuide(dataAccess, "Yellowstone")
        result2 <- AppVersion3.travelGuide(dataAccess, "Yellowstone")
        result3 <- AppVersion3.travelGuide(dataAccess, "Yellowstone")
      } yield result1.toList.appendedAll(result2).appendedAll(result3)
    }).unsafeRunSync()

    // after:
    runWithTiming(
      connectionResource.use(connection =>
        for {
          cache <- Ref.of[IO, Map[String, List[QuerySolution]]](Map.empty)
          dataAccess = getSparqlDataAccess(cachedExecQuery(connection, cache)) // <- here is the main change
          result1 <- AppVersion3.travelGuide(dataAccess, "Yellowstone")
          result2 <- AppVersion3.travelGuide(dataAccess, "Yellowstone")
          result3 <- AppVersion3.travelGuide(dataAccess, "Yellowstone")
        } yield result1.toList.appendedAll(result2).appendedAll(result3)
      )
    ) // the second and third execution will take a lot less time because all queries are cached!
  }

  /** BONUS: make it resilient
   * let's fail fast if requests take too long (timeout 30s)
   */
  private def runCachedVersionWithTimeouts = {
    AppRunner.runWithTiming(
      connectionResource.use(connection =>
        for {
          cache <- Ref.of[IO, Map[String, List[QuerySolution]]](Map.empty)
          cachedSparql =
            getSparqlDataAccess(
              cachedExecQuery(connection, cache).map(_.timeout(30.seconds)) // note that we map over a functions result.
            ) // each query will fail after 30 seconds, releasing all the resources!
          result1 <- AppVersion3.travelGuide(cachedSparql, "Yellowstone")
          result2 <- AppVersion3.travelGuide(cachedSparql, "Yellowstone")
          result3 <- AppVersion3.travelGuide(cachedSparql, "Yellowstone")
        } yield result1.toList.appendedAll(result2).appendedAll(result3)
      )
    ) // the second and third execution will take a lot less time because all queries are cached!
  }

  def main(args: Array[String]): Unit = {
    runCachedVersion
  }
}
