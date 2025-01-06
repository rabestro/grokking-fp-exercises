package chapter03

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class SolutionSpec extends AnyWordSpec with Matchers {
  import Solution._

  "firstTwo" should {

    "returns an empty list when the input list is empty" in {
      firstTwo(Nil) shouldBe Nil
    }

    "returns the same list when the input list has less than two elements" in {
      firstTwo(List("Alice")) shouldBe List("Alice")
    }

    "returns the first two elements when the input list has two or more elements" in {
      firstTwo(List("Alice", "Bob")) shouldBe List("Alice", "Bob")
      firstTwo(List("Alice", "Bob", "Charlie")) shouldBe List("Alice", "Bob")
      firstTwo(List("Alice", "Bob", "Charlie", "Diana")) shouldBe List("Alice", "Bob")
    }
  }

  "lastTwo" should {

    "return an empty list when the input list is empty" in {
      lastTwo(Nil) shouldBe Nil
    }

    "return the same list when the input list has fewer than two elements" in {
      lastTwo(List("Alice")) shouldBe List("Alice")
    }

    "return the last two elements when the input list has two or more elements" in {
      lastTwo(List("Alice", "Bob")) shouldBe List("Alice", "Bob")
      lastTwo(List("Alice", "Bob", "Charlie")) shouldBe List(
        "Bob",
        "Charlie"
      )
      lastTwo(List("Alice", "Bob", "Charlie", "Diana")) shouldBe List(
        "Charlie",
        "Diana"
      )
    }
  }

  "insertBeforeLast" should {
    "return a list with the element as the only element if the input list is empty" in {
      insertBeforeLast(List(), "NewElement") shouldBe List("NewElement")
    }

    "insert the element before the last element in a list with one element" in {
      insertBeforeLast(List("Alice"), "NewElement") shouldBe List("NewElement", "Alice")
    }

    "correctly insert the element before the last element in a list with multiple elements" in {
      insertBeforeLast(List("Alice", "Bob", "Charlie"), "NewElement") shouldBe List("Alice", "Bob", "NewElement", "Charlie")
      insertBeforeLast(List("A", "B", "C", "D"), "X") shouldBe List("A", "B", "C", "X", "D")
    }
  }

}
