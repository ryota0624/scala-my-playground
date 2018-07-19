package sample

import ddd.{ApplicationContext, Entity, Id, Repository}

import scala.concurrent.{ExecutionContext, Future}

case class Todo(
                 id: Id[Todo],
                 title: String,
                 text: String
                ) extends Entity


trait TodoRepository extends Repository[Todo]

trait UseTodoRepository {
  def todoRepository(implicit appCtx: ApplicationContext): TodoRepository
}

class TodoRepositoryRDBImpl(implicit val ex: ExecutionContext) extends TodoRepository {

  override def findBy(id: Id[Todo]): Future[Option[Todo]] = Future {
    None
  }

  override def findAll(): Future[Seq[Todo]] = Future {
    Nil
  }

  override def store(e: Todo): Unit = Future {
    ()
  }
}

trait MixInTodoRepositoryRDBImpl extends UseTodoRepository {
  def todoRepository(implicit appCtx: ApplicationContext): TodoRepository = appCtx.applyEc {
    implicit ec: ExecutionContext => new TodoRepositoryRDBImpl
  }
}