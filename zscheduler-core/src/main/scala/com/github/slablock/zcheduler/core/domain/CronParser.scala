package com.github.slablock.zcheduler.core.domain

import cron4s.{Cron, CronExpr, Error}

import java.time.LocalDateTime
import java.time._
import cron4s._
import cron4s.lib.javatime._

object CronParser extends App {

  val parsed : Either[Error, CronExpr]  = Cron("10-35 2,4,6 * ? * *")

  val cron = Cron.unsafeParse("10-35 2,4,6 * ? * *")

  val now = LocalDateTime.of(2016, 12, 1, 0, 4, 34)

  cron.allOf(now)
  // res0: Boolean = false
  cron.anyOf(now)
  // res1: Boolean = true

  var tmp = cron.next(now)

  System.out.println(tmp)

  tmp = cron.step(now, 2)

  System.out.println(tmp)

}
