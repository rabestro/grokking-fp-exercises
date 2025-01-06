package chapter03

object Solution {
  def firstTwo(list: List[String]): List[String] =
    list.slice(0, 2)

  def lastTwo(list: List[String]): List[String] =
    list.slice(list.size - 2, list.size)

  def movedFirstTwoToTheEnd(list: List[String]): List[String] =
    val (firstTwo, withoutFirstTwo) = list splitAt 2
    withoutFirstTwo ::: firstTwo

  def insertBeforeLast(list: List[String], element: String): List[String] =
    if list.isEmpty then 
      List(element)
    else 
      val withoutLast = list.slice(0, list.size - 1)
      withoutLast.appended(element).appended(list.last)
}
