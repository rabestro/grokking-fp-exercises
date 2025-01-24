package travelguide

import cats.effect.IO
import cats.implicits.*
import travelguide.BusinessDomain.TravelGuide
import travelguide.TravelGuideApp.{DataAccess, dataAccessResource, guideScore}
import travelguide.dao.AttractionOrdering.ByLocationPopulation

object AppVersion3:
  // Coffee Break: making it concurrent
  def travelGuide(data: DataAccess, attractionName: String): IO[Option[TravelGuide]] = {
    for {
      attractions <- data.findAttractions(attractionName, ByLocationPopulation, 3)
      guides <- attractions
        .map(attraction =>
          List(
            data.findArtistsFromLocation(attraction.location.id, 2),
            data.findMoviesAboutLocation(attraction.location.id, 2)
          ).parSequence.map(_.flatten).map(popCultureSubjects =>
            TravelGuide(attraction, popCultureSubjects)
          )
        )
        .parSequence
    } yield guides.sortBy(guideScore).reverse.headOption
  }

  def main(args: Array[String]): Unit =
    AppRunner.runWithTiming(
      dataAccessResource.use(
        dataAccess => travelGuide(dataAccess, "Yellowstone")
      )
    )
