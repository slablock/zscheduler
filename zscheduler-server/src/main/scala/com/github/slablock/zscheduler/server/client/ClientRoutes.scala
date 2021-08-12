package com.github.slablock.zscheduler.server.client

import akka.actor.typed.{ActorRef, ActorSystem}
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{Route, StandardRoute}
import akka.util.Timeout
import com.github.slablock.zscheduler.server.actor.BrokerActor.BrokerCommand
import com.github.slablock.zscheduler.server.actor.protos.brokerActor.{BrokerStatus, DependencyExpressionMsg, FlowSubmitRequest, FlowUpdateRequest, JobSubmitRequest, JobUpdateRequest, ProjectQueryRequest, ProjectSubmitRequest, ProjectUpdateRequest, ScheduleExpressionMsg}
import com.github.slablock.zscheduler.server.actor.protos.clientActor.{ClusterInfo, FlowSubmitResp, FlowUpdateResp, JobSubmitResp, ProjectQueryResp, ProjectSubmitResp, ProjectUpdateResp}
import com.github.slablock.zscheduler.server.client.ClientProtocol.{CODE_FAIL, CODE_SUCCESS, DependencyExpression, FlowSubmit, FlowSubmitResult, FlowUpdate, JobSubmit, JobSubmitResult, JobUpdate, ProjectSubmit, ProjectUpdate, ProjectWriteResult, ScheduleExpression, errorResult}
import de.heikoseeberger.akkahttpcirce.ErrorAccumulatingCirceSupport
import io.circe.generic.extras.Configuration
import io.circe.generic.extras.auto._
import akka.actor.typed.scaladsl.AskPattern.schedulerFromActorSystem
import akka.actor.typed.scaladsl.AskPattern.Askable
import org.slf4j.{Logger, LoggerFactory}

import scala.concurrent.TimeoutException
import scala.concurrent.duration._

class ClientRoutes(broker: ActorRef[BrokerCommand])(implicit system: ActorSystem[_]) extends ErrorAccumulatingCirceSupport {

  import ClientRoutes._

  private val projectRoute = new ProjectRoute(broker)
  private val flowRoute = new FlowRoute(broker)
  private val jobRoute = new JobRoute(broker)

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
      projectRoute.route ~
        flowRoute.route ~
        jobRoute.route
    }

}

object ClientRoutes extends ErrorAccumulatingCirceSupport {

  implicit val customConfig: Configuration = Configuration.default.withDefaults
  implicit val timeout: Timeout = 3.seconds

  private val LOGGER: Logger = LoggerFactory.getLogger(classOf[ClientRoutes])

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
    dependencies.map(d=>DependencyExpressionMsg(d.id, d.opType, d.preProjectId, d.preFlowId, d.preJobId,
      d.rangeExpression, d.offsetExpression))
  }

  def toScheduleMsg(schedules: Seq[ScheduleExpression]): Seq[ScheduleExpressionMsg] = {
    schedules.map(s=>ScheduleExpressionMsg(s.id, s.opType, s.scheduleType, s.expression))
  }

}