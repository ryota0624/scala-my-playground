package ddd

import scala.concurrent.{ExecutionContext, Future}

trait ApplicationContext {
  def applyEc[S](factory: ExecutionContext => S): S
  def runFuture[R](body: ExecutionContext => Future[R]): Future[R]
}

case class ApplicationContextImpl(ec: ExecutionContext) extends ApplicationContext {
  def applyEc[S](factory: ExecutionContext => S): S = factory(ec)
  def runFuture[R](body: ExecutionContext => Future[R]): Future[R] = body(ec)

}
