// Practicing nested flatMaps

case class Point(x: Int, y: Int)

List(1).flatMap(x =>
  List(-2, 7).map(y =>
    Point(x, y)
  )
)


