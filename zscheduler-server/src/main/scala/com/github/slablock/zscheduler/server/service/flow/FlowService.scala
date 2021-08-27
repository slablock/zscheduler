package com.github.slablock.zscheduler.server.service.flow

import com.github.slablock.zcheduler.core.domain.{CronExpression, FixedDelayExpression, FixedRateExpression, ISO8601Expression, ScheduleExpression}
import com.github.slablock.zscheduler.dao.Tables.{FlowDependencyRow, FlowRow, FlowScheduleRow}
import com.github.slablock.zscheduler.server.actor.protos.brokerActor.{FlowSubmitRequest, FlowUpdateRequest}
import com.google.inject.Inject
import org.joda.time.DateTime

import java.sql.Timestamp
import scala.concurrent.{ExecutionContext, Future}
import com.github.slablock.zcheduler.core.domain.ScheduleExpressionType
import com.github.slablock.zscheduler.server.domain.FlowEntry


class FlowService @Inject()(flowStorage: FlowStorage) {

  def queryFlow(flowId: Long)(implicit executionContext: ExecutionContext)
  : Future[(Option[FlowRow], Seq[FlowDependencyRow], Seq[FlowScheduleRow])] = flowStorage.queryFlow(flowId)

  def saveFlow(req: FlowSubmitRequest)(implicit executionContext: ExecutionContext): Future[Long] = {
    val flowId = 0
    val time = new Timestamp(DateTime.now().getMillis)
    val flowRow = FlowRow(flowId, req.projectId, req.flowName, req.user, req.user, time, time)
    val flowDependencies = req.dependencies.map(d =>
      FlowDependencyRow(d.id, req.projectId, flowId, d.preProjectId, d.preFlowId, d.preJobId, d.rangeExpression,
        d.offsetExpression, time, time))

    val schedules = req.schedules.map(s => FlowScheduleRow(s.id, req.projectId, flowId, s.scheduleType, s.expression, time, time))
    flowStorage.saveFlow(flowRow, flowDependencies, schedules)
  }

  def modifyFlow(req: FlowUpdateRequest)(implicit executionContext: ExecutionContext): Future[Int] = {
    val time = new Timestamp(DateTime.now().getMillis)
    val flowRow = FlowRow(req.flowId, req.projectId, req.flowName, req.user, req.user, time, time)

    val flowDependencies: Map[Int, Seq[FlowDependencyRow]] = req.dependencies.groupMap(_.opType)(d =>
      FlowDependencyRow(d.id, req.projectId, req.flowId, d.preProjectId, d.preFlowId, d.preJobId, d.rangeExpression,
        d.offsetExpression, time, time))

    val schedules: Map[Int, Seq[FlowScheduleRow]] = req.schedules.groupMap(_.opType)(s =>
      FlowScheduleRow(s.id, req.projectId, req.flowId, s.scheduleType, s.expression, time, time))

    flowStorage.updateFlow(flowRow, flowDependencies, schedules)
  }

  def getFlowEntry(flowId: Long)(implicit executionContext: ExecutionContext): Future[Option[FlowEntry]] = {
    queryFlow(flowId).transform({
      case (Some(flow), _, schedules) => {
        val scheduleMap = Map(schedules
          .map(s => (s.id, getScheduleExpression(ScheduleExpressionType.parseValue(s.scheduleType), s.expression))): _*)
        Option(new FlowEntry(flow, scheduleMap))
      }
      case (None, _, _) => Option.empty
    }, {f => f})
  }

  private def getScheduleExpression(expressionType: ScheduleExpressionType, expression: String): ScheduleExpression = {
    expressionType match {
      case ScheduleExpressionType.CRON => new CronExpression(expression)
      case ScheduleExpressionType.FIXED_RATE => new FixedRateExpression(expression)
      case ScheduleExpressionType.FIXED_DELAY => new FixedDelayExpression(expression)
      case ScheduleExpressionType.ISO8601 => new ISO8601Expression(expression)
    }
  }

}
