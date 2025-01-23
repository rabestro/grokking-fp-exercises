import sbt.*

object Dependencies {
  lazy val scalaTest = "org.scalatest" %% "scalatest" % "3.2.18"
  lazy val catsEffect = "org.typelevel" %% "cats-effect" % "3.5.7"
  lazy val stream = "co.fs2" %% "fs2-core" % "3.11.0"
}
