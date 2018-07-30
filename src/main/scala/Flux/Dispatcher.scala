package Flux

import logger.Logger

trait Action

abstract class Dispatcher[A <: Action] {
  case class RemoveDispatcher[Ac] private(index: Int)
  private var handlers: Map[Int, A => Unit] = Map.empty
  def register(action: A => Unit): RemoveDispatcher[A] = {
    val index = handlers.size + 1
    handlers = handlers + (index -> action)
    RemoveDispatcher(index)
  }

  def dispatch(a: A): Unit = {
    handlers.foreach {
      case (_, handler) => handler(a)
    }
  }

  def remove(removeDispatcher: RemoveDispatcher[A]): Unit = {
    handlers = handlers - removeDispatcher.index
  }
}


object Sample {
  def sample(body: () => Unit) = {
    println("sample start")

    body()

    println("sample end")
  }
  def main(args: Array[String]): Unit = { sample { () =>
    var store: Map[Int, String] = Map.empty
    sealed trait SampleAction extends Action

    val logger = new Logger(println)

    final case class Add(key: Int, value: String) extends SampleAction
    final case class Remove(key: Int) extends SampleAction

    final class SampleDispatcher extends Dispatcher[SampleAction] {
      override def dispatch(a: SampleAction): Unit = logger.loggingWithTime { () => super.dispatch(a) }
    }

    val dispatcher = new SampleDispatcher

    dispatcher.register {
      case Add(key, value) => store = store + (key -> value)
      case Remove(key) => store = store - key
    }

    dispatcher.dispatch(Add(10, "Hello!"))
    dispatcher.dispatch(Add(11, "Hello2!"))
    dispatcher.dispatch(Remove(11))

    assert(store == Map(10 -> "Hello!"))
    }
  }
}