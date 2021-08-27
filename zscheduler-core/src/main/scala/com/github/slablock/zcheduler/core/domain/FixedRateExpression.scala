package com.github.slablock.zcheduler.core.domain
import org.joda.time.DateTime

class FixedRateExpression(expression: String) extends ScheduleExpression(expression) {

  override def getTimeBefore(dateTime: DateTime): Option[DateTime] = {
    Option.empty
  }

  override def getTimeAfter(dateTime: DateTime): Option[DateTime] = {
    Option.empty
  }

  override def isValid: Boolean = {
    false
  }

}
