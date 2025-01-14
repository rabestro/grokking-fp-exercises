// Practicing newtypes (Page 240)

object model {
  opaque type Genre = String

  object Genre:
    def apply(value: String): Genre = value

    extension (a: Genre) def name: String = a

  opaque type Location = String

  object Location:
    def apply(value: String): Location = value

    extension (a: Location) def name: String = a

  opaque type YearsActiveStart = Int

  object YearsActiveStart:
    def apply(value: Int): YearsActiveStart = value

    extension (a: YearsActiveStart) def value: Int = a

  opaque type YearsActiveEnd = Int

  object YearsActiveEnd:
    def apply(value: Int): YearsActiveEnd = value

    extension (a: YearsActiveEnd) def value: Int = a

  // Practicing pattern matching (Page 258)

  // Given

  enum MusicGenre {
    case HeavyMetal
    case Pop
    case HardRock
  }

  enum YearsActive:
    case StillActive(since: Int)
    case ActiveBetween(start: Int, end: Int)

  case class Artist(name: String, genre: MusicGenre, origin: Location, yearsActive: YearsActive)

  // Your task is to implement a function that gets an artist and the current
  // year and returns the number of years this artist was active.

  // Solution

  def activeLength(artist: Artist, currentYear: Int): Int =
    artist.yearsActive match
      case YearsActive.StillActive(since) => currentYear - since
      case YearsActive.ActiveBetween(start, end) => end - start

}

import model.*
import model.MusicGenre.*
import model.YearsActive.*

activeLength(
  Artist("Metallica", HeavyMetal, Location("U.S."), StillActive(1981)),
  2025)

activeLength(
  Artist("Led Zeppelin", HardRock, Location("England"), ActiveBetween(1968, 1980)),
  2022
)

activeLength(
  Artist("Bee Gees", Pop, Location("England"), ActiveBetween(1958, 2003)),
  2022
)