package com.github.slablock.zscheduler.server.service.project

import com.github.slablock.zscheduler.dao.Tables
import com.github.slablock.zscheduler.dao.Tables.{ProjectRow, project}
import com.github.slablock.zscheduler.server.service.ZSDbComponent
import com.google.inject.Inject
import slick.jdbc.TransactionIsolation

import scala.concurrent.{ExecutionContext, Future}

trait ProjectStorage {
  def queryProject(projectId: Long): Future[Option[ProjectRow]]
  def saveProject(project: ProjectRow)(implicit executionContext: ExecutionContext): Future[ProjectRow]
}

private[service] class JdbcProjectStorage @Inject()(val dbComponent: ZSDbComponent) extends ProjectStorage {
  import dbComponent._
  import profile.api._

  override def queryProject(projectId: Long): Future[Option[Tables.ProjectRow]] = {
    db.run(project.filter(d=>d.projectId === projectId).result.headOption)
  }

  private val insertQuery = project returning project.map(_.projectId) into ((item, projectId) => item.copy(projectId = projectId))

  override def saveProject(project: Tables.ProjectRow)(implicit executionContext: ExecutionContext): Future[Tables.ProjectRow] = {
    val action = insertQuery += project
    db.run(action.withTransactionIsolation(TransactionIsolation.ReadCommitted).transactionally)
  }

}
