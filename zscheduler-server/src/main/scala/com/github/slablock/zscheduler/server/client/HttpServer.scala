package com.github.slablock.zscheduler.server.client

import akka.actor.typed.{ActorRef, ActorSystem}
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.github.slablock.zscheduler.server.actor.protos.clientActor.{ClientMsg, Query}
import com.github.slablock.zscheduler.server.client.ClientConf.config
import com.github.slablock.zscheduler.server.client.ClientProtocol.{IpInfo, Job}
import de.heikoseeberger.akkahttpcirce.ErrorAccumulatingCirceSupport
import io.circe.generic.auto._

import scala.concurrent.ExecutionContextExecutor


class HttpServer private (val port: Int, val interface: String, val actorRef: ActorRef[ClientMsg],
                          implicit val system: ActorSystem[Nothing]) extends ErrorAccumulatingCirceSupport {

  implicit val executor: ExecutionContextExecutor = system.executionContext

  Http().newServerAt(interface, port).bind(route())

  def route(): Route = {
    pathPrefix("ip") {
      (get & path(Segment)) { ip =>
        actorRef ! Query("1")
        complete(IpInfo("hello"))
      }
    } ~ pathPrefix("job") {
      post {
        entity(as[Job]) { job =>
          complete(job)
        }
      }
    }
  }

}


object HttpServer {

  def apply(actorRef: ActorRef[ClientMsg], system: ActorSystem[Nothing]): HttpServer = {
    val port = config.getInt(ClientConf.PORT)
    val interface = config.getString(ClientConf.INTERFACE)
    new HttpServer(port, interface, actorRef, system)
  }
}