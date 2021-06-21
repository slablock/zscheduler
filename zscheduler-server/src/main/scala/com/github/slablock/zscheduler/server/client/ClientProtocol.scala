package com.github.slablock.zscheduler.server.client

import com.github.slablock.zscheduler.server.client.entity.Job
import de.heikoseeberger.akkahttpcirce.ErrorAccumulatingCirceSupport
import io.circe.{Decoder, Encoder}

object ClientProtocol {

  case class IpInfo(query: String)


  trait Protocols extends ErrorAccumulatingCirceSupport {

    import io.circe.generic.semiauto._

    implicit val ipInfoDecoder: Decoder[IpInfo] = deriveDecoder
    implicit val ipInfoEncoder: Encoder[IpInfo] = deriveEncoder
    implicit val jobDecoder: Decoder[Job] = deriveDecoder
    implicit val jobEncoder: Encoder[Job] = deriveEncoder
  }

}
