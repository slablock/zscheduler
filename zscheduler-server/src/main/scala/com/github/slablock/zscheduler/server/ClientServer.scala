package com.github.slablock.zscheduler.server

import akka.actor.typed.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives.{logRequestResult, pathPrefix}
import com.github.slablock.zcheduler.core.lifecycle.{LifecycleStart, LifecycleStop}
import com.github.slablock.zscheduler.server.common.ClusterRole
import com.typesafe.config.ConfigFactory
import org.slf4j.LoggerFactory
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.github.slablock.zscheduler.server.ClientProtocol.{IpInfo, Protocols}
import com.github.slablock.zscheduler.server.actor.ClientActor
import com.github.slablock.zscheduler.server.actor.protos.clientActor.{ClientMsg, ClusterInfo, Query}

import scala.concurrent.{ExecutionContextExecutor, Future}

class ClientServer extends Protocols {

  private val LOGGER = LoggerFactory.getLogger(classOf[ClientServer])

  @LifecycleStart
  def start(): Unit = {
    val port = 2552
    val portConf = "akka.remote.artery.canonical.port=" + port
    val portBindConf = "akka.remote.artery.bind.port=" + port

    val config = ConfigFactory.parseString(portConf)
      .withFallback(ConfigFactory.parseString(portBindConf))
      .withFallback(ConfigFactory.load())
      .withFallback(ConfigFactory.parseString(s"akka.cluster.roles = [${ClusterRole.CLIENT}]"))


//    val config = ConfigFactory.load().withFallback(ConfigFactory.parseString(s"akka.cluster.roles = [${ClusterRole.CLIENT}]"))

    val systemName = config.getString("zs.system")
    implicit val system: ActorSystem[ClientMsg] = ActorSystem[ClientMsg](ClientActor(), systemName, config)
    implicit def executor: ExecutionContextExecutor = system.executionContext

    val routes: Route = {
      logRequestResult("akka-http-microservice") {
        pathPrefix("ip") {
          (get & path(Segment)) { ip =>
            system ! Query("1")
            complete(IpInfo("hello"))
          }
        }
      }
    }

    Http().newServerAt(config.getString("http.interface"), config.getInt("http.port")).bindFlow(routes)
    LOGGER.info("Client start successfully!!!")
  }

  @LifecycleStop
  def stop(): Unit = {
    LOGGER.info("Client stopped!!!")
  }

}
