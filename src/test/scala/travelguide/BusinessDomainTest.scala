package travelguide

import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import travelguide.BusinessDomain.*
import travelguide.BusinessDomain.PopCultureSubject.Movie

class BusinessDomainTest extends AnyFunSuite with ScalaCheckPropertyChecks {

  /** STEP 1: testing by providing examples
   */
  test("score of a guide with a description, 0 artists, and 2 popular movies should be 65") {
    val guide = TravelGuide(
      Attraction(
        "Yellowstone National Park",
        Some("first national park in the world"),
        Location(LocationId("Q1214"), "Wyoming", 586107)
      ),
      List(Movie("The Hateful Eight", 155760117), Movie("Heaven's Gate", 3484331))
    )

    // 30 (description) + 0 (0 artists) + 20 (2 movies) + 15 (159 million box office)
    assert(guideScore(guide) == 65)
  }

  // Practicing testing by example
  test("score of a guide with no description, 0 artists, and 0 movies should be 0") {
    val guide = TravelGuide(
      Attraction(
        "Yellowstone National Park",
        None,
        Location(LocationId("Q1214"), "Wyoming", 586107)
      ),
      List.empty
    )

    // 0 (description) + 0 (0 artists) + 0 (0 movies)
    assert(guideScore(guide) == 0)
  }

  test("score of a guide with no description, 0 artists, and 2 movies with no box office earnings should be 20") {
    val guide = TravelGuide(
      Attraction(
        "Yellowstone National Park",
        None,
        Location(LocationId("Q1214"), "Wyoming", 586107)
      ),
      List(Movie("The Hateful Eight", 0), Movie("Heaven's Gate", 0))
    )

    // 0 (description) + 0 (0 artists) + 20 (2 movies) + 0 (0 million box office)
    assert(guideScore(guide) == 20)
  }

  /** STEP 2: testing by providing properties
   */
  test("guide score should not depend on its attraction's name and description strings") {
    forAll((name: String, description: String) => {
      val guide = TravelGuide(
        Attraction(
          name, // introduce: empty strings and shorter/longer sizes with different characters
          Some(description),
          Location(LocationId("Q1214"), "Wyoming", 586107)
        ),
        List(Movie("The Hateful Eight", 155760117), Movie("Heaven's Gate", 3484331))
      )

      // 30 (description) + 0 (0 artists) + 20 (2 movies) + 15 (159 million box office)
      assert(guideScore(guide) == 65)
    })
  }

}
