package com.github.slablock.zscheduler.server.pub

import akka.actor.testkit.typed.scaladsl.ScalaTestWithActorTestKit
import com.github.slablock.zscheduler.server.actor.BrokerActor
import com.github.slablock.zscheduler.server.actor.ClientActor.ClientCommand
import com.github.slablock.zscheduler.server.actor.protos.brokerActor.BrokerStatus
import org.scalatest.wordspec.AnyWordSpecLike



class DeviceSpec extends ScalaTestWithActorTestKit with AnyWordSpecLike {

  "Device actor" must {
    "broker" in {
      val prob = createTestProbe[ClientCommand]()
      val broker = spawn(BrokerActor())

      broker ! BrokerStatus(prob.ref)
      val rep = prob.receiveMessage()
    }
  }
}
