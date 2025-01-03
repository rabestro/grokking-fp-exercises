package chapter02

object TipCalculator {
  def tipPercentage (names: List[String]): Int =
    if names.isEmpty then 0
    else if names.size > 5 then 20 else 10
}

