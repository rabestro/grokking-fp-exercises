package chapter03

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class firstTwoSpec extends AnyFunSuite with Matchers {

  test("firstTwo should return an empty list when the input list is empty") {
    Solution.firstTwo(List()) shouldBe List()
  }

  test("firstTwo should return the same list when the input list has less than two elements") {
    Solution.firstTwo(List("Alice")) shouldBe List("Alice")
  }

  test("firstTwo should return the first two elements when the input list has two or more elements") {
    Solution.firstTwo(List("Alice", "Bob")) shouldBe List("Alice", "Bob")
    Solution.firstTwo(List("Alice", "Bob", "Charlie")) shouldBe List("Alice", "Bob")
    Solution.firstTwo(List("Alice", "Bob", "Charlie", "Diana")) shouldBe List("Alice", "Bob")
  }

}