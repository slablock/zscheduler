package com.github.slablock.zcheduler.core.domain
import cron4s.Cron
import org.joda.time.DateTime
import cron4s._
import cron4s.lib.joda._

class CronExpression(expression: String) extends ScheduleExpression(expression) {

  val cron: Either[Error, CronExpr] = Cron.parse(expression)

  val zeroDate: DateTime = new DateTime(1970, 1, 1, 0, 0, 0)

  override def getTimeBefore(dateTime: DateTime): Option[DateTime] = {
    cron match {
      case Left(_)   => Option.empty
      case Right(expr) => expr.prev(dateTime)
    }
  }

  override def getTimeAfter(dateTime: DateTime): Option[DateTime] = {
    cron match {
      case Left(_)   => Option.empty
      case Right(expr) => expr.next(dateTime)
    }
  }

  override def isValid: Boolean = {
    try  {
      getTimeAfter(zeroDate).nonEmpty
    } catch {
      case _: Exception =>
        false
    }
  }

}
