package chapter02

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class TipCalculatorSpec extends AnyFunSuite with Matchers {

  test("tipPercentage should return 0 for an empty list of names") {
    TipCalculator.tipPercentage(List()) shouldBe 0
  }

  test("tipPercentage should return 10 for a list with 1 to 5 names") {
    TipCalculator.tipPercentage(List("Alice")) shouldBe 10
    TipCalculator.tipPercentage(List("Alice", "Bob")) shouldBe 10
    TipCalculator.tipPercentage(List("Alice", "Bob", "Charlie", "Diana", "Edward")) shouldBe 10
  }

  test("tipPercentage should return 20 for a list with more than 5 names") {
    TipCalculator.tipPercentage(List("Alice", "Bob", "Charlie", "Diana", "Edward", "Frank")) shouldBe 20
    TipCalculator.tipPercentage(List("Name1", "Name2", "Name3", "Name4", "Name5", "Name6", "Name7")) shouldBe 20
  }
}
