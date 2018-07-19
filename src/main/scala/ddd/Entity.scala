package ddd


trait Entity {
  val id: Id[_]
}

class Id[E](val value: String) extends AnyVal {}
