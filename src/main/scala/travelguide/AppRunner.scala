package travelguide

import cats.effect.IO
import cats.effect.unsafe.implicits.global

/**
 * Utility object for running and timing IO effects.
 */
object AppRunner:

  /**
   * Executes an IO effect while measuring and printing the execution time.
   *
   * @param effect The IO effect to be executed and timed.
   * @return The result of the executed IO effect.
   */
  def runWithTiming[A](effect: IO[A]): A =
    def timeExecution[B](block: => B): (B, Long) =
      val start = System.currentTimeMillis()
      val result = block
      val elapsed = System.currentTimeMillis() - start
      (result, elapsed)

    val (result, duration) = timeExecution(effect.unsafeRunSync())
    println(s"$result (took ${duration}ms)")
    result
