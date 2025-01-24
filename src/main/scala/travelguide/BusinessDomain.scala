package travelguide

object BusinessDomain:
  opaque type LocationId = String

  case class Location(id: LocationId, name: String, population: Int)

  case class Attraction(name: String, description: Option[String], location: Location)

  enum PopCultureSubject:
    case Artist(name: String, followers: Int)
    case Movie(name: String, boxOffice: Int)

  case class TravelGuide(attraction: Attraction, subjects: List[PopCultureSubject])

  object LocationId:
    def apply(value: String): LocationId = value

    extension (a: LocationId) def value: String = a

  import travelguide.BusinessDomain.PopCultureSubject.{Artist, Movie}

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
