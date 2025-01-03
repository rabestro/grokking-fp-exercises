package chapter03

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class lastTwoSpec extends AnyFunSuite with Matchers {

  test("lastTwo should return an empty list when the input list is empty") {
    Solution.lastTwo(List()) shouldBe List()
  }

  test("lastTwo should return the same list when the input list has fewer than two elements") {
    Solution.lastTwo(List("Alice")) shouldBe List("Alice")
  }

  test("lastTwo should return the last two elements when the input list has two or more elements") {
    Solution.lastTwo(List("Alice", "Bob")) shouldBe List("Alice", "Bob")
    Solution.lastTwo(List("Alice", "Bob", "Charlie")) shouldBe List("Bob", "Charlie")
    Solution.lastTwo(List("Alice", "Bob", "Charlie", "Diana")) shouldBe List("Charlie", "Diana")
  }
}