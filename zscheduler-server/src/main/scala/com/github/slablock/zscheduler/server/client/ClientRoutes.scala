package com.github.slablock.zscheduler.server.client

import akka.actor.typed.{ActorRef, ActorSystem}
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.util.Timeout
import com.github.slablock.zscheduler.server.actor.BrokerActor.BrokerCommand
import com.github.slablock.zscheduler.server.actor.protos.brokerActor.{BrokerStatus, DependencyExpression, FlowSubmitRequest, JobSubmitRequest, ProjectSubmitRequest, ScheduleExpression}
import com.github.slablock.zscheduler.server.actor.protos.clientActor.{ClusterInfo, JobSubmitResp, ProjectSubmitResp}
import com.github.slablock.zscheduler.server.client.ClientProtocol.{FlowSubmit, JobSubmit, ProjectSubmit, errorResult, successResult}
import de.heikoseeberger.akkahttpcirce.ErrorAccumulatingCirceSupport
import io.circe.generic.auto._
import org.slf4j.{Logger, LoggerFactory}

import scala.concurrent.TimeoutException
import scala.concurrent.duration._
import scala.util.{Failure, Success}

class ClientRoutes(broker: ActorRef[BrokerCommand])(implicit system: ActorSystem[_]) extends ErrorAccumulatingCirceSupport {
  import akka.actor.typed.scaladsl.AskPattern.schedulerFromActorSystem
  import akka.actor.typed.scaladsl.AskPattern.Askable

  implicit val timeout: Timeout = 3.seconds
  private val LOGGER: Logger = LoggerFactory.getLogger(classOf[ClientRoutes])

  lazy val routes: Route =
    pathPrefix("system") {
      path("status") {
      get {
        onSuccess(broker.ask(ref => BrokerStatus(ref))) {
          case ClusterInfo(data) => complete(ClusterInfo(data))
        }
      }
      }
    } ~ pathPrefix("v1") {
      jobRoutes ~ projectRoutes ~ flowRoutes
    }

  lazy val jobRoutes: Route = pathPrefix("job") {
    post {
      entity(as[JobSubmit]) { job =>
        onComplete(broker.ask(ref => JobSubmitRequest(0, job.jobName, job.jobType, job.contentType,
          job.content, "", job.priority, job.user, Seq(), ref))) {
          case Success(JobSubmitResp(jobId)) => complete(successResult(jobId))
          case Failure(ex) => {
            LOGGER.info("", ex)
            ex match {
              case _: TimeoutException =>
                complete(StatusCodes.RequestTimeout -> errorResult(ex.getMessage))
              case _ =>
                complete(StatusCodes.ServerError -> errorResult(ex.getMessage))
            }
          }
        }
      }
    }
  }

  lazy val projectRoutes: Route = pathPrefix("project") {
    post {
      entity(as[ProjectSubmit]) { project =>
        onComplete(broker.ask(ref => ProjectSubmitRequest(project.projectName, project.user, ref))) {
          case Success(ProjectSubmitResp(projectId)) => complete(successResult(projectId))
          case Failure(ex) => {
            LOGGER.info("", ex)
            ex match {
              case _: TimeoutException =>
                complete(StatusCodes.RequestTimeout -> errorResult(ex.getMessage))
              case _ =>
                complete(StatusCodes.ServerError -> errorResult(ex.getMessage))
            }
          }
        }
      }
    }
  }

  lazy val flowRoutes: Route = pathPrefix("flow") {
    post {
      entity(as[FlowSubmit]) { flow =>

        val dependencies = flow.dependencies
          .map(d=>DependencyExpression(d.preProjectId, d.preFlowId, d.preJobId, d.rangeExpression, d.offsetExpression))

        val schedules = flow.schedules.map(s=>ScheduleExpression(s.scheduleType, s.expression))

        onComplete(broker.ask(ref => FlowSubmitRequest(flow.projectId, flow.flowName,
          flow.user, dependencies, schedules, ref))) {
          case Success(JobSubmitResp(jobId)) => complete(successResult(jobId))
          case Failure(ex) => {
            LOGGER.info("", ex)
            ex match {
              case _: TimeoutException =>
                complete(StatusCodes.RequestTimeout -> errorResult(ex.getMessage))
              case _ =>
                complete(StatusCodes.ServerError -> errorResult(ex.getMessage))
            }
          }
        }
      }
    }
  }
}
