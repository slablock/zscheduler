package com.github.slablock.zscheduler.server.pub

import akka.actor.testkit.typed.scaladsl.ScalaTestWithActorTestKit
import akka.actor.typed.ActorRef
import com.github.slablock.zscheduler.server.actor.protos.brokerActor.BrokerStatus
import com.github.slablock.zscheduler.server.actor.{BrokerActor, DemoActor}
import com.github.slablock.zscheduler.server.actor.protos.clientActor.{ClientMsg, ClusterInfo}
import com.github.slablock.zscheduler.server.actor.protos.demoActorProtocol.{Read, Resp}
import org.scalatest.wordspec.AnyWordSpecLike



class DeviceSpec extends ScalaTestWithActorTestKit with AnyWordSpecLike {

  "Device actor" must {
    "demo" in {
      val probe = createTestProbe[Resp]()
      val deviceActor = spawn(DemoActor())

      deviceActor ! Read(id = "42", probe.ref.asInstanceOf[ActorRef[Any]])
      val response = probe.receiveMessage()
      response.data should ===("42")
    }
    "broker" in {
      val prob = createTestProbe[ClusterInfo]()
      val broker = spawn(BrokerActor())

      broker ! BrokerStatus(prob.ref)
      val rep = prob.receiveMessage()
      rep.data should === ("hello")
    }
  }
}
