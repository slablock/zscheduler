package com.github.slablock.zscheduler.server.service.flow

import com.github.slablock.zscheduler.dao.Tables.{FlowDependencyRow, FlowRow, FlowScheduleRow, flow, flowDependency, flowSchedule}
import com.github.slablock.zscheduler.server.domain.OperationType
import com.github.slablock.zscheduler.server.service.ZSDbComponent
import com.google.inject.Inject
import slick.jdbc.TransactionIsolation

import scala.concurrent.{ExecutionContext, Future}

trait FlowStorage {

  def queryFlow(flowId: Long): Future[Option[FlowRow]]

  def saveFlow(flowRow: FlowRow, flowDependencyRows: Seq[FlowDependencyRow],
               flowScheduleRows: Seq[FlowScheduleRow])(implicit executionContext: ExecutionContext): Future[Long]

  def updateFlow(flowRow: FlowRow, flowDependencyRows: Map[Int, Seq[FlowDependencyRow]],
                 flowScheduleRows: Map[Int, Seq[FlowScheduleRow]])(implicit executionContext: ExecutionContext): Future[Int]
}

private[service] class JdbcFlowStorage @Inject()(val dbComponent: ZSDbComponent) extends FlowStorage {

  import dbComponent._
  import profile.api._

  override def queryFlow(flowId: Long): Future[Option[FlowRow]] = {
    db.run(flow.filter(d => d.flowId === flowId).result.headOption)
  }

  override def saveFlow(flowRow: FlowRow, flowDependencyRows: Seq[FlowDependencyRow],
                        flowScheduleRows: Seq[FlowScheduleRow])(implicit executionContext: ExecutionContext): Future[Long] = {
    val flowQ = flow returning flow.map(_.flowId) += flowRow

    def addDependencies(flowId: Long): DBIO[Option[Int]] = {
      flowDependency ++= flowDependencyRows.map(fd => FlowDependencyRow(fd.id, fd.projectId,
        flowId, fd.preProjectId, fd.preFlowId, fd.preJobId, fd.rangeExpression, fd.offsetExpression, fd.createTime, fd.updateTime))
    }

    def addSchedules(flowId: Long): DBIO[Option[Int]] = {
      flowSchedule ++= flowScheduleRows.map(fs => FlowScheduleRow(fs.id, fs.projectId, flowId,
        fs.scheduleType, fs.expression, fs.createTime, fs.updateTime))
    }

    val q: DBIO[Long] = flowQ.flatMap(flowId => {
      addDependencies(flowId).andThen(addSchedules(flowId)).andThen(DBIO.successful(flowId))
    })

    db.run(q.withTransactionIsolation(TransactionIsolation.ReadCommitted).transactionally)
  }

  override def updateFlow(flowRow: FlowRow, flowDependencyRows: Map[Int, Seq[FlowDependencyRow]],
                          flowScheduleRows: Map[Int, Seq[FlowScheduleRow]])(implicit executionContext: ExecutionContext): Future[Int] = {

    val flowQ: DBIO[Int] = flow.filter(d => d.flowId === flowRow.flowId)
      .map(d => (d.flowName, d.user, d.updateUser, d.updateTime))
      .update((flowRow.flowName, flowRow.user, flowRow.updateUser, flowRow.updateTime))

    val dependencyUpdate: DBIO[Int] = DBIO.sequence(flowDependencyRows.get(OperationType.EDIT.getValue).map(_.map(fd => {
      flowDependency
        .filter(d => d.id === fd.id)
        .map(d => (d.preProjectId, d.flowId, d.preProjectId, d.preFlowId, d.preJobId, d.rangeExpression,
          d.offsetExpression, d.updateTime))
        .update((fd.projectId, fd.flowId, fd.preProjectId, fd.preFlowId, fd.preJobId, fd.rangeExpression,
          fd.offsetExpression, fd.updateTime))
    })).getOrElse(Seq())).collect(s => s.sum)

    val dependencyAdd: DBIO[Int] = (flowDependency ++= flowDependencyRows.getOrElse(OperationType.ADD.getValue, Seq()))
      .collect(r => r.sum)

    val toDeleteDependencies = flowDependencyRows.get(OperationType.DELETE.getValue).map(_.map(fd => fd.id)).getOrElse(Seq())

    val dependencyDelete: DBIO[Int] = flowDependency.filter(d => d.id.inSet(toDeleteDependencies)).delete


    val scheduleUpdate: DBIO[Int] = DBIO.sequence(flowScheduleRows.get(OperationType.EDIT.getValue).map(_.map(fs => {
      flowSchedule.filter(s => s.id === fs.id)
        .map(s => (s.projectId, s.flowId, s.scheduleType, s.expression, s.updateTime))
        .update((fs.projectId, fs.flowId, fs.scheduleType, fs.expression, fs.updateTime))
    })).getOrElse(Seq())).collect(s => s.sum)

    val scheduleAdd: DBIO[Int] = (flowSchedule ++= flowScheduleRows.getOrElse(OperationType.ADD.getValue, Seq()))
      .collect(r => r.sum)

    val toDeleteSchedules = flowScheduleRows.get(OperationType.DELETE.getValue).map(_.map(fs => fs.id)).getOrElse(Seq())

    val scheduleDelete: DBIO[Int] = flowSchedule.filter(d => d.id.inSet(toDeleteSchedules)).delete


    val q: DBIO[Int] = DBIO.sequence(Vector(flowQ, dependencyAdd, dependencyDelete, dependencyUpdate, scheduleAdd,
      scheduleDelete, scheduleUpdate)).collect(s => s.sum)
    db.run(q.withTransactionIsolation(TransactionIsolation.ReadCommitted).transactionally)
  }

}
