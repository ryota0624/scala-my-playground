package ddd

import scala.concurrent.{ExecutionContext, Future}

trait Repository[E <: Entity] {
  implicit val ex: ExecutionContext

  def findBy(id: Id[E]): Future[Option[E]]
  def findAll(): Future[Seq[E]]
  def store(e: E): Unit
}
