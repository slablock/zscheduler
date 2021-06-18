package com.github.slablock.zscheduler.server.pub

import akka.actor.testkit.typed.scaladsl.ScalaTestWithActorTestKit
import com.github.slablock.zscheduler.server.actor.BrokerActor
import com.github.slablock.zscheduler.server.actor.protos.brokerActor.BrokerStatus
import com.github.slablock.zscheduler.server.actor.protos.clientActor.{ClientMsg, ClusterInfo}
import org.scalatest.wordspec.AnyWordSpecLike



class DeviceSpec extends ScalaTestWithActorTestKit with AnyWordSpecLike {

  "Device actor" must {
    "broker" in {
      val prob = createTestProbe[ClientMsg]()
      val broker = spawn(BrokerActor())

      broker ! BrokerStatus(prob.ref)
      val rep = prob.receiveMessage()
    }
  }
}
