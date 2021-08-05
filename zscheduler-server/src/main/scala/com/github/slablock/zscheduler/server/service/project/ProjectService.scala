package com.github.slablock.zscheduler.server.service.project

import com.github.slablock.zscheduler.dao.Tables.ProjectRow
import com.google.inject.Inject

import scala.concurrent.{ExecutionContext, Future}

class ProjectService @Inject()(projectStorage: ProjectStorage){

  def queryProject(projectId: Long): Future[Option[ProjectRow]] = projectStorage.queryProject(projectId)

  def addProject(project: ProjectRow)(implicit executionContext: ExecutionContext): Future[Long] =
    projectStorage.saveProject(project)

  def updateProject(projectRow: ProjectRow)(implicit executionContext: ExecutionContext): Future[Int] =
    projectStorage.updateProject(projectRow)

}