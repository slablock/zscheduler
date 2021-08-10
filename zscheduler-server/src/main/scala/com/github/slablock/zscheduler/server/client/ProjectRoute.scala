package com.github.slablock.zscheduler.server.client


import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.util.Timeout
import com.github.slablock.zscheduler.server.actor.protos.brokerActor.{ProjectQueryRequest, ProjectSubmitRequest, ProjectUpdateRequest}
import com.github.slablock.zscheduler.server.actor.protos.clientActor.{ ProjectQueryResp, ProjectSubmitResp, ProjectUpdateResp}
import com.github.slablock.zscheduler.server.client.ClientProtocol.{CODE_FAIL, CODE_SUCCESS,ProjectSubmit, ProjectUpdate, ProjectWriteResult}
import io.circe.generic.auto._

import scala.concurrent.duration._
import scala.util.{Failure, Success}
import akka.actor.typed.scaladsl.AskPattern.schedulerFromActorSystem
import akka.actor.typed.scaladsl.AskPattern.Askable
import akka.actor.typed.{ActorRef, ActorSystem}
import com.github.slablock.zscheduler.server.actor.BrokerActor.BrokerCommand
import com.github.slablock.zscheduler.server.client.ClientRoutes.errHandler
import de.heikoseeberger.akkahttpcirce.ErrorAccumulatingCirceSupport

class ProjectRoute(broker: ActorRef[BrokerCommand])(implicit system: ActorSystem[_]) extends ErrorAccumulatingCirceSupport {

  implicit val timeout: Timeout = 3.seconds

  lazy val route: Route = pathPrefix("project") {
    path("create") {
      post {
        entity(as[ProjectSubmit]) { project =>
          onComplete(broker.ask(ref => ProjectSubmitRequest(project.projectName, project.user, ref))) {
            case Success(ProjectSubmitResp(success, projectId, msg)) =>
              if (success) {
                complete(ProjectWriteResult(CODE_SUCCESS, msg, projectId))
              } else {
                complete(ProjectWriteResult(CODE_FAIL, msg, projectId))
              }
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
              case Success(ProjectUpdateResp(success, projectId, msg)) =>
                if (success) {
                  complete(ProjectWriteResult(CODE_SUCCESS, msg, projectId))
                } else {
                  complete(ProjectWriteResult(CODE_FAIL, msg, projectId))
                }
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
}
