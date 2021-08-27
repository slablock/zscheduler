package com.github.slablock.zscheduler.server.service.executionFlow

import com.github.slablock.zscheduler.dao.Tables.ExecutionFlowRow
import com.github.slablock.zscheduler.server.domain.TaskType
import com.github.slablock.zscheduler.server.service.flow.FlowService
import com.google.inject.Inject
import org.joda.time.DateTime

import java.sql.Timestamp
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}

class ExecutionFlowService @Inject()(executionFlowStorage: ExecutionFlowStorage, flowService: FlowService)  {

  def add(flowId: Long, scheduleTime: Long, dataTime: Long, taskType: TaskType)(implicit executionContext: ExecutionContext): Future[Long] = {
    flowService.queryFlow(flowId).transform({
      case(None, _, _) => throw new IllegalArgumentException("flow not found")
      case(Some(flow), _, _) => {
        val time = new Timestamp(DateTime.now().getMillis)
        val row: ExecutionFlowRow = ExecutionFlowRow(0, 0, flow.projectId, flow.flowId,
          new Timestamp(scheduleTime), new Timestamp(dataTime), taskType.getValue, 0, 0, "", None, None, time, time)
        Await.result(executionFlowStorage.save(row), Duration.Inf)
      }
    } , f=>f)
  }

  def getByFlowIdAndScheduleTime(flowId: Long, scheduleTime: Long, taskType: TaskType): Future[Option[ExecutionFlowRow]] = {
    executionFlowStorage.queryByFlowAndScheduleTime(flowId, scheduleTime, taskType)
  }
}
