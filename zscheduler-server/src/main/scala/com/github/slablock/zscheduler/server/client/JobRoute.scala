package com.github.slablock.zscheduler.server.client

import akka.actor.typed.scaladsl.AskPattern.{Askable, schedulerFromActorSystem}
import akka.actor.typed.{ActorRef, ActorSystem}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.util.Timeout
import com.github.slablock.zscheduler.server.actor.BrokerActor.BrokerCommand
import com.github.slablock.zscheduler.server.actor.protos.brokerActor.{FlowSubmitRequest, FlowUpdateRequest, JobSubmitRequest, JobUpdateRequest, ProjectQueryRequest}
import com.github.slablock.zscheduler.server.actor.protos.clientActor.{FlowSubmitResp, FlowUpdateResp, JobSubmitResp, ProjectQueryResp}
import com.github.slablock.zscheduler.server.client.ClientProtocol._
import com.github.slablock.zscheduler.server.client.ClientRoutes.{errHandler, toDependencyMsg, toScheduleMsg}
import de.heikoseeberger.akkahttpcirce.ErrorAccumulatingCirceSupport
import io.circe.generic.auto._

import scala.concurrent.duration._
import scala.util.{Failure, Success}

class JobRoute(broker: ActorRef[BrokerCommand])(implicit system: ActorSystem[_]) extends ErrorAccumulatingCirceSupport {

  implicit val timeout: Timeout = 3.seconds

  lazy val route: Route = pathPrefix("job") {
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

}
