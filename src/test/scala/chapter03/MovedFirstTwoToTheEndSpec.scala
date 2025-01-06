package chapter03

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class MovedFirstTwoToTheEndSpec extends AnyFunSuite with Matchers {

  test("movedFirstTwoToTheEnd should return an empty list when the input list is empty") {
    Solution.movedFirstTwoToTheEnd(List()) shouldBe List()
  }

  test("movedFirstTwoToTheEnd should return the same list when the input list has fewer than two elements") {
    Solution.movedFirstTwoToTheEnd(List("Alice")) shouldBe List("Alice")
  }

  test("movedFirstTwoToTheEnd should move the first two elements to the end for a list with exactly two elements") {
    Solution.movedFirstTwoToTheEnd(List("Alice", "Bob")) shouldBe List("Alice", "Bob")
  }

  test("movedFirstTwoToTheEnd should move the first two elements to the end for a list with more than two elements") {
    Solution.movedFirstTwoToTheEnd(List("Alice", "Bob", "Charlie")) shouldBe List("Charlie", "Alice", "Bob")
    Solution.movedFirstTwoToTheEnd(List("Alice", "Bob", "Charlie", "Diana")) shouldBe List("Charlie", "Diana", "Alice", "Bob")
  }
}