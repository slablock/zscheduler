package com.github.slablock.zscheduler.server.service.project

import com.github.slablock.zscheduler.dao.Tables
import com.github.slablock.zscheduler.dao.Tables.{ProjectRow, project}
import com.github.slablock.zscheduler.server.service.ZSDbComponent
import com.google.inject.Inject
import slick.jdbc.TransactionIsolation

import scala.concurrent.{ExecutionContext, Future}

trait ProjectStorage {
  def queryProject(projectId: Long): Future[Option[ProjectRow]]
  def saveProject(projectRow: ProjectRow)(implicit executionContext: ExecutionContext): Future[Long]
  def updateProject(projectRow: ProjectRow)(implicit executionContext: ExecutionContext): Future[Int]
}

private[service] class JdbcProjectStorage @Inject()(val dbComponent: ZSDbComponent) extends ProjectStorage {
  import dbComponent._
  import profile.api._

  override def queryProject(projectId: Long): Future[Option[Tables.ProjectRow]] = {
    db.run(project.filter(d=>d.projectId === projectId).result.headOption)
  }

  override def saveProject(projectRow: Tables.ProjectRow)(implicit executionContext: ExecutionContext): Future[Long] = {
    val q = project returning project.map(_.projectId) += projectRow
    db.run(q.withTransactionIsolation(TransactionIsolation.ReadCommitted).transactionally)
  }

  override def updateProject(projectRow: ProjectRow)(implicit executionContext: ExecutionContext): Future[Int] = {
    val q = project.filter(d=>d.projectId === projectRow.projectId)
      .map(d=>(d.projectName, d.user, d.updateUser, d.updateTime))
      .update((projectRow.projectName, projectRow.user, projectRow.updateUser, projectRow.updateTime))
    db.run(q.withTransactionIsolation(TransactionIsolation.ReadCommitted).transactionally)
  }

}
