package com.github.slablock.zscheduler.server.service.plan

import com.github.slablock.zscheduler.server.service.flow.FlowService
import com.google.inject.Inject
import org.joda.time.DateTime

import scala.concurrent.{ExecutionContext, Future}

class PlanService @Inject()(flowService: FlowService) {

  def getScheduleTimeAfter(flowId: Long, dateTime: DateTime)(implicit executionContext: ExecutionContext): Future[Option[DateTime]] = {
    flowService.getFlowEntry(flowId).transform(s => s.map(ss => ss.schedules.flatMap(_._2.getTimeAfter(dateTime)).min), f => f)
  }
}