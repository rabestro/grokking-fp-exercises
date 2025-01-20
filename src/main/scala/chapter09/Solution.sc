import cats.effect.IO
import cats.effect.unsafe.implicits.global
import chapter08.ch08_CardGame.castTheDie
import chapter08.ch08_CastingDieImpure.NoFailures.castTheDieImpure
import fs2.{Pure, Stream}

// Practicing stream operations (Page 353)

// Given
def castTheDie(): IO[Int] = IO.delay(castTheDieImpure())
val infiniteDieCasts: Stream[IO, Int] = Stream.eval(castTheDie()).repeat

/*
Your task is to create the following IO values. Each of them should internally use
numbers (and side effects) produced by infiniteDieCasts and do the following:
  1. Filter odd numbers, and return the first three such casts.
  2. Return the first five die casts, but make sure all 6 values are doubled (so
    a [1, 2, 3, 6, 4] becomes [1, 2, 3, 12, 4]).
  3. Return the sum of the first three casts.
  4. Cast the die until there is a 5 and then cast it two more times, returning
  three last results back (a 5 and two more).
  5. Make sure the die is cast 100 times, and values are discarded.
  6. Return the first three casts unchanged and the next three casts tripled (six
  in total).
*/

def isOdd: Int => Boolean = _ % 2 == 1

infiniteDieCasts
  .filter(isOdd)
  .take(3)
  .compile.toList

infiniteDieCasts
  .take(5)
  .map(x => if x == 6 then 12 else x)
  .compile.toList

infiniteDieCasts
  .take(3)
  .compile.toList
  .map(_.sum)

infiniteDieCasts
  .filter(_ == 5).take(1)
  .append(infiniteDieCasts.take(2))
  .compile.toList

infiniteDieCasts
  .take(100)
  .compile.drain

infiniteDieCasts
  .take(3)
  .append(infiniteDieCasts.take(3).map(_ * 3))
  .compile.toList

infiniteDieCasts
  .scan(0)((sixesInRow, current) => if current == 6 then sixesInRow + 1 else 0)
  .filter(_ == 2)
  .take(1)
  .compile.toList
