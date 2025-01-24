package travelguide

import cats.effect.IO
import cats.effect.unsafe.implicits.global

object AppRunner:
  /**
   * Helper function that runs the given IO[A], times its execution, prints it, and returns it
   */
  def unsafeRunTimedIO[A](io: IO[A]): A =
    val start = System.currentTimeMillis()
    val result = io.unsafeRunSync()
    val end = System.currentTimeMillis()
    println(s"$result (took ${end - start}ms)")
    result
