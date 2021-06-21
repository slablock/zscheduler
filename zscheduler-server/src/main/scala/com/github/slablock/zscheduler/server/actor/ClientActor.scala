package com.github.slablock.zscheduler.server.actor

import akka.actor.typed.receptionist.Receptionist
import akka.actor.typed.scaladsl.AskPattern.Askable
import akka.actor.typed.{ActorSystem, Behavior, Scheduler}
import akka.actor.typed.scaladsl.{AbstractBehavior, ActorContext, Behaviors}
import akka.http.scaladsl.server.Directives.entity
import akka.util.Timeout
import com.github.slablock.zscheduler.server.actor.protos.brokerActor.BrokerStatus
import com.github.slablock.zscheduler.server.actor.protos.clientActor.{ClientMsg, ClusterInfo, Query}

import java.util.concurrent.TimeUnit
import akka.http.scaladsl.Http
import io.circe.generic.auto._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.github.slablock.zscheduler.server.client.ClientConf
import com.github.slablock.zscheduler.server.client.ClientConf.config
import com.github.slablock.zscheduler.server.client.ClientProtocol.{IpInfo, JobSubmit}
import de.heikoseeberger.akkahttpcirce.ErrorAccumulatingCirceSupport

import scala.concurrent.ExecutionContextExecutor


class ClientActor(context: ActorContext[ClientMsg]) extends AbstractBehavior[ClientMsg](context) with ErrorAccumulatingCirceSupport {

  implicit val system: ActorSystem[Nothing] = context.system
  implicit val timeout: Timeout = Timeout(10, TimeUnit.SECONDS)
  implicit val scheduler: Scheduler = context.system.scheduler
  implicit val executionContext: ExecutionContextExecutor = context.system.executionContext

  Http().newServerAt(config.getString(ClientConf.INTERFACE), config.getInt(ClientConf.PORT)).bind(route())

  override def onMessage(msg: ClientMsg): Behavior[ClientMsg] = {
    msg match {
      case msg: Query =>
        context.log.info("client receive msg: {}", msg.id)
        context.system.receptionist.ask[Receptionist.Listing](Receptionist.Find(BrokerActor.serviceKey)).map {
          listing =>
            if (listing.isForKey(BrokerActor.serviceKey)) {
              listing.serviceInstances(BrokerActor.serviceKey).head ! BrokerStatus(context.self)
            }
        }
        Behaviors.same
      case msg: ClusterInfo =>
        context.log.info("ClusterInfo {}", msg.data)
        Behaviors.same
    }
  }

  def route(): Route = {
    pathPrefix("ip") {
      (get & path(Segment)) { ip =>
        context.self ! Query("1")
        complete(IpInfo("hello"))
      }
    } ~ pathPrefix("job") {
      post {
        entity(as[JobSubmit]) { job =>
          complete(job)
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
