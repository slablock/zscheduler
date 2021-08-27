package com.github.slablock.zscheduler.server.service.executionFlow

import com.github.slablock.zscheduler.dao.Tables._
import com.github.slablock.zscheduler.server.domain.TaskType
import com.github.slablock.zscheduler.server.service.ZSDbComponent
import com.google.inject.Inject
import slick.jdbc.TransactionIsolation

import java.sql.Timestamp
import scala.concurrent.{ExecutionContext, Future}

trait ExecutionFlowStorage {

  def save(executionFlowRow: ExecutionFlowRow)(implicit executionContext: ExecutionContext) : Future[Long]

  def query(execId: Long): Future[Option[ExecutionFlowRow]]

  def queryByFlowAndScheduleTime(flowId: Long, scheduleTime: Long, taskType: TaskType): Future[Option[ExecutionFlowRow]]
}


private[service] class JdbcExecutionFlowStorage @Inject()(val dbComponent: ZSDbComponent) extends ExecutionFlowStorage {

  import dbComponent._
  import profile.api._

  override def save(executionFlowRow: ExecutionFlowRow)(implicit executionContext: ExecutionContext): Future[Long] = {
    val q: DBIO[Long] = executionFlow returning executionFlow.map(_.execId) += executionFlowRow
    db.run(q.withTransactionIsolation(TransactionIsolation.ReadCommitted).transactionally)
  }

  override def query(execId: Long): Future[Option[ExecutionFlowRow]] = {
    val q: DBIO[Option[ExecutionFlowRow]] = executionFlow.filter(f => f.execId === execId).result.headOption
    db.run(q)
  }

  override def queryByFlowAndScheduleTime(flowId: Long, scheduleTime: Long, taskType: TaskType): Future[Option[ExecutionFlowRow]] = {
    val dateTime = new Timestamp(scheduleTime)
    val q: DBIO[Option[ExecutionFlowRow]] = executionFlow
      .filter(f => (f.flowId === flowId) && (f.scheduleTime === dateTime) && (f.taskType === taskType.getValue))
      .result.headOption
    db.run(q)
  }

}
