package com.github.slablock.zscheduler.server.client

import akka.actor.typed.scaladsl.AskPattern.{Askable, schedulerFromActorSystem}
import akka.actor.typed.{ActorRef, ActorSystem}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.util.Timeout
import com.github.slablock.zscheduler.server.actor.BrokerActor.BrokerCommand
import com.github.slablock.zscheduler.server.actor.protos.brokerActor.{FlowSubmitRequest, FlowUpdateRequest, ProjectQueryRequest, ProjectSubmitRequest, ProjectUpdateRequest}
import com.github.slablock.zscheduler.server.actor.protos.clientActor.{FlowSubmitResp, FlowUpdateResp, ProjectQueryResp, ProjectSubmitResp, ProjectUpdateResp}
import com.github.slablock.zscheduler.server.client.ClientProtocol._
import com.github.slablock.zscheduler.server.client.ClientRoutes.{errHandler, toDependencyMsg, toScheduleMsg}
import de.heikoseeberger.akkahttpcirce.ErrorAccumulatingCirceSupport
import io.circe.generic.auto._

import scala.concurrent.duration._
import scala.util.{Failure, Success}

class FlowRoute(broker: ActorRef[BrokerCommand])(implicit system: ActorSystem[_]) extends ErrorAccumulatingCirceSupport {

  implicit val timeout: Timeout = 3.seconds

  lazy val route: Route = pathPrefix("flow") {
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

}
