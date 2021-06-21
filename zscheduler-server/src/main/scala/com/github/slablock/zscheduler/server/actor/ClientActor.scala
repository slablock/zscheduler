package com.github.slablock.zscheduler.server.actor

import akka.actor.typed.scaladsl.AskPattern.Askable
import akka.actor.typed.{ActorRef, ActorSystem, Behavior, Scheduler}
import akka.actor.typed.scaladsl.{AbstractBehavior, ActorContext, Behaviors, Routers}
import akka.http.scaladsl.server.Directives.entity
import akka.util.Timeout
import com.github.slablock.zscheduler.server.actor.protos.brokerActor.{BrokerMsg, BrokerStatus, JobSubmitRequest}
import com.github.slablock.zscheduler.server.actor.protos.clientActor.{ClientMsg, ClusterInfo, JobSubmitResp, Query}

import java.util.concurrent.{TimeUnit, TimeoutException}
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.StatusCodes
import io.circe.generic.auto._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.github.slablock.zscheduler.server.client.ClientConf
import com.github.slablock.zscheduler.server.client.ClientConf.config
import com.github.slablock.zscheduler.server.client.ClientProtocol.{ErrorResult, IpInfo, JobSubmit, JobSubmitted}
import de.heikoseeberger.akkahttpcirce.ErrorAccumulatingCirceSupport
import org.slf4j.LoggerFactory

import scala.concurrent.{ExecutionContextExecutor, Future}
import scala.util.{Failure, Success}


class ClientActor(context: ActorContext[ClientMsg]) extends AbstractBehavior[ClientMsg](context) with ErrorAccumulatingCirceSupport {

  private val logger = LoggerFactory.getLogger(classOf[ClientActor])
  implicit val system: ActorSystem[Nothing] = context.system
  implicit val timeout: Timeout = Timeout(10, TimeUnit.SECONDS)
  implicit val scheduler: Scheduler = context.system.scheduler
  implicit val executionContext: ExecutionContextExecutor = context.system.executionContext
  val broker: ActorRef[BrokerMsg] = context.spawn(Routers.group(BrokerActor.serviceKey).withRoundRobinRouting(), "broker-group")

  Http().newServerAt(config.getString(ClientConf.INTERFACE), config.getInt(ClientConf.PORT)).bind(route())

  override def onMessage(msg: ClientMsg): Behavior[ClientMsg] = {
    msg match {
      case msg: Query =>
        context.log.info("client receive msg: {}", msg.id)
        broker ! BrokerStatus(context.self)
        Behaviors.same
      case msg: ClusterInfo =>
        context.log.info("ClusterInfo {}", msg.data)
        Behaviors.same
      case JobSubmitResp(jobId) => {
        Behaviors.same
      }
    }
  }

  def route(): Route = pathPrefix("ip") {
    (get & path(Segment)) { ip =>
      context.self ! Query("1")
      complete(IpInfo("hello"))
    }
  } ~ pathPrefix("job") {
    post {
      entity(as[JobSubmit]) { job =>
        onComplete(broker.ask(ref => JobSubmitRequest(job.name, job.jobType, job.content, job.user, ref))) {
          case Success(JobSubmitResp(jobId)) => complete(JobSubmitted(jobId))
          case Failure(ex) => {
            logger.info("", ex)
            ex match {
              case TimeoutException =>
                complete(StatusCodes.RequestTimeout -> ErrorResult(ex.getMessage))
              case _ =>
                complete(StatusCodes.ServerError -> ErrorResult(ex.getMessage))
            }
          }
        }
      }
    }
  }

}

object ClientActor {

  def apply(): Behavior[ClientMsg] = Behaviors.setup[ClientMsg](context => {
    context.spawn(ClusterListenerActor(), "lister")
    new ClientActor(context)
  })

}
