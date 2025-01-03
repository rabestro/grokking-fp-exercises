package chapter03

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class insertBeforeLastSpec extends AnyFunSuite with Matchers {

  test("insertBeforeLast should return a list with the element as the only element if the input list is empty") {
    Solution.insertBeforeLast(List(), "NewElement") shouldBe List("NewElement")
  }

  test("insertBeforeLast should insert the element before the last element in a list with one element") {
    Solution.insertBeforeLast(List("Alice"), "NewElement") shouldBe List("NewElement", "Alice")
  }

  test("insertBeforeLast should correctly insert the element before the last element in a list with multiple elements") {
    Solution.insertBeforeLast(List("Alice", "Bob", "Charlie"), "NewElement") shouldBe List("Alice", "Bob", "NewElement", "Charlie")
    Solution.insertBeforeLast(List("A", "B", "C", "D"), "X") shouldBe List("A", "B", "C", "X", "D")
  }
}