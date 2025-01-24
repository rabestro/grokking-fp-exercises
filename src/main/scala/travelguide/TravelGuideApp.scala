package travelguide

import cats.effect.unsafe.implicits.global
import cats.effect.{IO, Ref, Resource}
import org.apache.jena.query.{QueryExecution, QueryFactory, QuerySolution}
import org.apache.jena.rdfconnection.{RDFConnection, RDFConnectionRemote}
import travelguide.AppRunner.runWithTiming
import travelguide.WikidataDataAccess.getSparqlDataAccess

import scala.jdk.javaapi.CollectionConverters.asScala

object TravelGuideApp {

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

  def main(args: Array[String]): Unit = {
    runCachedVersion
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

  // introduce Resource
  def execQuery(connection: RDFConnection)(query: String): IO[List[QuerySolution]] = {
    val executionResource: Resource[IO, QueryExecution] = Resource.make(createExecution(connection, query))(
      closeExecution
    ) // or Resource.fromAutoCloseable(createExecution)

    executionResource.use(execution => IO.blocking(asScala(execution.execSelect()).toList))
  }

  // PROBLEM: we are repeating the same queries, but the results don't change that often.

  /** STEP 7: handle resource leaks (query execution and connection)
   */
  def createExecution(connection: RDFConnection, query: String): IO[QueryExecution] = IO.blocking(
    connection.query(QueryFactory.create(query))
  )

  def closeExecution(execution: QueryExecution): IO[Unit] = IO.blocking(
    execution.close()
  )

  trait DataAccess extends dao.DataAccess
}
