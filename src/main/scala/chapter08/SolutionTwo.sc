// Practicing failure recovery in IO values (Page 296)

import cats.effect.IO
import chapter08.ch08_CardGame.castTheDie
import chapter08.ch08_CardGame.drawAPointCard

/*
Your task is to create three different IO values that describe the following
programs:
  1. Cast the die, and if it fails to produce a result, return 0.
  2. Draw a card, and if it fails, cast the die.
  3. Cast the die, and if it fails—retry once. If it fails again, return 0.
  4. Cast the die, and draw a card, using a fallback of 0 for each of them.
  Return the sum of both.
  5. Draw a card, and cast the die twice. Return the sum of all three or 0
  if any of them fails.
 */

// 1
IO.delay(castTheDie()).orElse(IO.pure(0))

// 2
IO.delay(drawAPointCard()).orElse(IO.delay(castTheDie()))

// 3
IO.delay(castTheDie())
  .orElse(IO.delay(castTheDie()))
  .orElse(IO.pure(0))

// 4
for {
  die <- IO.delay(castTheDie()).orElse(IO.pure(0))
  card <- IO.delay(drawAPointCard()).orElse(IO.pure(0))
} yield die + card

// 5
// Draw a card, and cast the die twice. Return the sum of all three ...
val partOne = for {
  card <- IO.delay(drawAPointCard())
  die1 <- IO.delay(castTheDie())
  die2 <- IO.delay(castTheDie())
} yield card + die1 + die2

// ... or 0 if any of them fails.
val partTwo = partOne.orElse(IO.pure(0))
