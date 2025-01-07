// Practicing nested flatMaps (page 147)

case class Point(x: Int, y: Int)

List(1).flatMap(x =>
  List(-2, 7).map(y =>
    Point(x, y)
  )
)

// Practicing for comprehensions (page 164)

for {
  x <- List(1, 2, 3)
  y <- Set(1)
} yield x * y

for {
  x <- Set(1, 2, 3)
  y <- List(1)
} yield x * y

for {
  x <- List(1, 2, 3)
  y <- Set(1)
  z <- Set(0)
} yield x * y * z
