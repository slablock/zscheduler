package com.github.slablock.zscheduler.server.actor

import akka.actor.typed.{ActorRef, ActorSystem, Behavior, PostStop, Scheduler}
import akka.actor.typed.scaladsl.{AbstractBehavior, ActorContext, Behaviors, Routers}
import com.github.slablock.zscheduler.server.actor.protos.brokerActor.BrokerStatus
import com.github.slablock.zscheduler.server.actor.protos.clientActor.{ClusterInfo, JobSubmitResp, Query}

import akka.http.scaladsl.Http
import akka.http.scaladsl.Http.ServerBinding
import com.github.slablock.zscheduler.server.actor.BrokerActor.BrokerCommand
import com.github.slablock.zscheduler.server.actor.ClientActor.{ClientCommand, StartFailed, Started, Stop}
import com.github.slablock.zscheduler.server.client.{ClientConf, ClientRoutes}
import com.github.slablock.zscheduler.server.client.ClientConf.config

import scala.concurrent.Future
import scala.util.{Failure, Success}


class ClientActor(context: ActorContext[ClientCommand]) extends AbstractBehavior[ClientCommand](context) {

  implicit val system: ActorSystem[Nothing] = context.system
  val broker: ActorRef[BrokerCommand] = context.spawn(Routers.group(BrokerActor.serviceKey).withRoundRobinRouting(), "broker-group")

  val serverBinding: Future[Http.ServerBinding] =
    Http().newServerAt(config.getString(ClientConf.INTERFACE), config.getInt(ClientConf.PORT))
      .bind((new ClientRoutes(broker)).routes)

  context.pipeToSelf(serverBinding) {
    case Success(binding) => Started(binding)
    case Failure(ex)      => StartFailed(ex)
  }

  override def onMessage(msg: ClientCommand): Behavior[ClientCommand] = {
    starting(wasStopped = false)
  }

  def running(binding: ServerBinding): Behavior[ClientCommand] =
    Behaviors.receiveMessagePartial[ClientCommand] {
      case Stop =>
        context.log.info(
          "Stopping server http://{}:{}/",
          binding.localAddress.getHostString,
          binding.localAddress.getPort)
        Behaviors.stopped
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
    }.receiveSignal {
      case (_, PostStop) =>
        binding.unbind()
        Behaviors.same
    }

  def starting(wasStopped: Boolean): Behaviors.Receive[ClientCommand] =
    Behaviors.receiveMessage[ClientCommand] {
      case StartFailed(cause) =>
        throw new RuntimeException("Server failed to start", cause)
      case Started(binding) =>
        context.log.info(
          "Server online at http://{}:{}/",
          binding.localAddress.getHostString,
          binding.localAddress.getPort)
        if (wasStopped) context.self ! Stop
        running(binding)
      case Stop =>
        // we got a stop message but haven't completed starting yet,
        // we cannot stop until starting has completed
        starting(wasStopped = true)
    }
}

object ClientActor {

  trait ClientCommand
  private final case class StartFailed(cause: Throwable) extends ClientCommand
  private final case class Started(binding: ServerBinding) extends ClientCommand
  case object Stop extends ClientCommand

  def apply(): Behavior[ClientCommand] = Behaviors.setup[ClientCommand](context => {
    new ClientActor(context)
  })

}
