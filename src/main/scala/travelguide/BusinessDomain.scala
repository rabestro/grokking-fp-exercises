package travelguide

object BusinessDomain:
  opaque type LocationId = String

  object LocationId:
    def apply(value: String): LocationId = value

    extension (a: LocationId) def value: String = a

  case class Location(id: LocationId, name: String, population: Int)

  case class Attraction(name: String, description: Option[String], location: Location)

  enum PopCultureSubject:
    case Artist(name: String, followers: Int)
    case Movie(name: String, boxOffice: Int)

  case class TravelGuide(attraction: Attraction, subjects: List[PopCultureSubject])

