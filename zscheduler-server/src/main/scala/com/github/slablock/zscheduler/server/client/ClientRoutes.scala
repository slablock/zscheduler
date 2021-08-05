package com.github.slablock.zscheduler.server.client

import akka.actor.typed.{ActorRef, ActorSystem}
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{Route, StandardRoute}
import akka.util.Timeout
import com.github.slablock.zscheduler.server.actor.BrokerActor.BrokerCommand
import com.github.slablock.zscheduler.server.actor.protos.brokerActor.{BrokerStatus, DependencyExpressionMsg, FlowSubmitRequest, FlowUpdateRequest, JobSubmitRequest, JobUpdateRequest, ProjectQueryRequest, ProjectSubmitRequest, ProjectUpdateRequest, ScheduleExpressionMsg}
import com.github.slablock.zscheduler.server.actor.protos.clientActor.{ClusterInfo, FlowSubmitResp, FlowUpdateResp, JobSubmitResp, ProjectQueryResp, ProjectSubmitResp, ProjectUpdateResp}
import com.github.slablock.zscheduler.server.client.ClientProtocol.{CODE_SUCCESS, DependencyExpression, FlowSubmit, FlowSubmitResult, FlowUpdate, JobSubmit, JobSubmitResult, JobUpdate, ProjectSubmit, ProjectUpdate, ProjectWriteResult, ScheduleExpression, errorResult}
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


  lazy val projectRoutes: Route = pathPrefix("project") {
    path("create") {
      post {
        entity(as[ProjectSubmit]) { project =>
          onComplete(broker.ask(ref => ProjectSubmitRequest(project.projectName, project.user, ref))) {
            case Success(ProjectSubmitResp(projectId)) => complete(ProjectWriteResult(CODE_SUCCESS, projectId))
            case Failure(ex) => errHandler(ex)
          }
        }
      }
    } ~
    path("modify") {
      post {
        entity(as[ProjectUpdate]) { project =>
          onComplete(broker.ask(ref => ProjectUpdateRequest(project.projectId, project.projectName,
            project.user, project.updateUser, ref))) {
            case Success(ProjectUpdateResp(projectId)) => complete(ProjectWriteResult(CODE_SUCCESS, projectId))
            case Failure(ex) => errHandler(ex)
          }
        }
      }
    } ~
    path("query") {
      pathEndOrSingleSlash {
        get {
          parameters(Symbol("projectId").as[Long]) { projectId =>
            onComplete(broker.ask(ref => ProjectQueryRequest(projectId, ref))) {
              case Success(ProjectQueryResp(success, data, msg)) =>
                complete(CODE_SUCCESS, data)
              case Failure(ex) => errHandler(ex)
            }
          }
        }
      }
    }
  }


  lazy val flowRoutes: Route = pathPrefix("flow") {
    path("create") {
      post {
        entity(as[FlowSubmit]) { flow =>
          onComplete(broker.ask(ref => FlowSubmitRequest(flow.projectId, flow.flowName,
            flow.user, toDependencyMsg(flow.dependencies), toScheduleMsg(flow.schedules), ref))) {
            case Success(FlowSubmitResp(flowId)) => complete(FlowSubmitResult(CODE_SUCCESS, flowId))
            case Failure(ex) => errHandler(ex)
          }
        }
      }
    } ~
    path("modify") {
      post {
        entity(as[FlowUpdate]) { flow =>
          onComplete(broker.ask(ref => FlowUpdateRequest(flow.flowId, flow.projectId, flow.flowName,
            flow.user, flow.updateUser,
            toDependencyMsg(flow.dependencies), toScheduleMsg(flow.schedules), ref))) {
            case Success(FlowUpdateResp(flowId)) => complete(FlowSubmitResult(CODE_SUCCESS, flowId))
            case Failure(ex) => errHandler(ex)
          }
        }
      }
    } ~
      path("query") {
        pathEndOrSingleSlash {
          get {
            parameters(Symbol("flowId").as[Long]) { flowId =>
              onComplete(broker.ask(ref => ProjectQueryRequest(flowId, ref))) {
                case Success(ProjectQueryResp(success, data, msg)) =>
                  complete(CODE_SUCCESS, data)
                case Failure(ex) => errHandler(ex)
              }
            }
          }
        }
      }
  }


  lazy val jobRoutes: Route = pathPrefix("job") {
    path("create") {
      post {
        entity(as[JobSubmit]) { job =>
          onComplete(broker.ask(ref => JobSubmitRequest(job.projectId, job.flowId, job.jobName, job.jobType, job.contentType,
            job.content, job.params, job.priority, job.user, toDependencyMsg(job.dependencies), ref))) {
            case Success(JobSubmitResp(jobId)) => complete(JobSubmitResult(CODE_SUCCESS, jobId))
            case Failure(ex) => errHandler(ex)
          }
        }
      }
    } ~
    path("modify") {
      post {
        entity(as[JobUpdate]) { job =>
          onComplete(broker.ask(ref => JobUpdateRequest(job.jobId, job.projectId, job.flowId, job.jobName, job.jobType, job.contentType,
            job.content, job.params, job.priority,
            job.user, job.updateUser, toDependencyMsg(job.dependencies), ref))) {
            case Success(JobSubmitResp(jobId)) => complete(JobSubmitResult(CODE_SUCCESS, jobId))
            case Failure(ex) => errHandler(ex)
          }
        }
      }
    }
  }


  def errHandler(ex: Throwable) : StandardRoute = {
    LOGGER.info("", ex)
    ex match {
      case _: TimeoutException =>
        complete(StatusCodes.RequestTimeout -> errorResult(ex.getLocalizedMessage))
      case _ =>
        complete(StatusCodes.ServerError -> errorResult(ex.getLocalizedMessage))
    }
  }


  def toDependencyMsg(dependencies: Seq[DependencyExpression]): Seq[DependencyExpressionMsg] = {
    dependencies.map(d=>DependencyExpressionMsg(d.preProjectId, d.preFlowId, d.preJobId,
      d.rangeExpression, d.offsetExpression))
  }

  def toScheduleMsg(schedules: Seq[ScheduleExpression]): Seq[ScheduleExpressionMsg] = {
    schedules.map(s=>ScheduleExpressionMsg(s.scheduleType, s.expression))
  }

}
