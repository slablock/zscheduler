package com.github.slablock.zcheduler.core.domain

import org.joda.time.DateTime

abstract class ScheduleExpression(expression: String) extends Expression {

  def getExpression: String = expression

  def getTimeBefore(dateTime: DateTime): Option[DateTime]

  def getTimeAfter(dateTime: DateTime): Option[DateTime]

}
