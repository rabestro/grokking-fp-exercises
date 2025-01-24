package travelguide.dao

import cats.effect.IO
import travelguide.BusinessDomain.PopCultureSubject.{Artist, Movie}
import travelguide.BusinessDomain.{Attraction, LocationId}

trait DataAccess:

  def findAttractions(name: String, ordering: AttractionOrdering, limit: Int): IO[List[Attraction]]

  def findArtistsFromLocation(locationId: LocationId, limit: Int): IO[List[Artist]]

  def findMoviesAboutLocation(locationId: LocationId, limit: Int): IO[List[Movie]]

