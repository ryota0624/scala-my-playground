package logger

import java.time.{LocalDateTime, ZoneOffset}

import scala.util.{Failure, Success, Try}


class Logger(write: String => Unit) {
  implicit class SeqOpt[E](self: Seq[E]) {
    def getOpt(index: Int): Option[E] = {
      Try[E] {
        self(index)
      } match {
        case Success(value) => Some(value)
        case Failure(_) => None
      }
    }
  }
  def loggingWithTime[R](body: => R): R = {
    val startTime = LocalDateTime.now()
    val result = body
    val endTime = LocalDateTime.now()
    val spendTime = spendMilliSecTime(start = startTime, end = endTime)
    write(logMessage(startTime, endTime, spendTime))
    result
  }

  protected def logMessage(startDateTime: LocalDateTime, endDateTime: LocalDateTime, spendTime: Long): String = {
    Thread.currentThread.getStackTrace.toSeq.getOpt(3).map { stackTraceElement =>
      val (className, fileName, methodName) = (stackTraceElement.getClassName, stackTraceElement.getFileName, stackTraceElement.getMethodName)
      s"""${fileName}: ${className}.${methodName} - ${startDateTime} ~ ${endDateTime} - ${spendTime} milliSec"""
    } getOrElse "NoStackTrace"
  }

  protected def spendMilliSecTime(start: LocalDateTime, end: LocalDateTime): Long = {
    val startMilliSec = start.toInstant(ZoneOffset.ofTotalSeconds(0)).toEpochMilli
    val endMilliSec = end.toInstant(ZoneOffset.ofTotalSeconds(0)).toEpochMilli

    endMilliSec - startMilliSec
  }
}
