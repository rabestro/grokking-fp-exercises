package travelguide

import cats.effect.IO
import travelguide.BusinessDomain.PopCultureSubject.{Artist, Movie}
import travelguide.BusinessDomain.{Attraction, LocationId}

object DataAccess:
  enum AttractionOrdering:
    case ByName
    case ByLocationPopulation


  trait DataAccess:
    def findAttractions(name: String, ordering: AttractionOrdering, limit: Int): IO[List[Attraction]]

    def findArtistsFromLocation(locationId: LocationId, limit: Int): IO[List[Artist]]

    def findMoviesAboutLocation(locationId: LocationId, limit: Int): IO[List[Movie]]

