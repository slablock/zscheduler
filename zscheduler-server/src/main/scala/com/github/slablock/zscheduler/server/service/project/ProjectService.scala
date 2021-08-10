package com.github.slablock.zscheduler.server.service.project

import com.github.slablock.zscheduler.dao.Tables.ProjectRow
import com.github.slablock.zscheduler.server.actor.protos.brokerActor.{ProjectSubmitRequest, ProjectUpdateRequest}
import com.google.inject.Inject
import org.joda.time.DateTime

import java.sql.Timestamp
import scala.concurrent.{ExecutionContext, Future}

class ProjectService @Inject()(projectStorage: ProjectStorage) {

  def queryProject(projectId: Long): Future[Option[ProjectRow]] = projectStorage.queryProject(projectId)

  def addProject(req: ProjectSubmitRequest)(implicit executionContext: ExecutionContext): Future[Long] = {
    val time = new Timestamp(DateTime.now().getMillis)
    projectStorage.saveProject(ProjectRow(0, req.projectName, req.user, req.user, time, time))
  }

  def updateProject(req: ProjectUpdateRequest)(implicit executionContext: ExecutionContext): Future[Int] = {
    val time = new Timestamp(DateTime.now().getMillis)
    projectStorage.updateProject(ProjectRow(req.projectId, req.projectName, req.user, req.updateUser, time, time))
  }

}