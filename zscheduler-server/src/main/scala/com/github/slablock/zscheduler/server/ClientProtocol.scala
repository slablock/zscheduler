package com.github.slablock.zscheduler.server

import de.heikoseeberger.akkahttpcirce.ErrorAccumulatingCirceSupport
import io.circe.{Decoder, Encoder}

object ClientProtocol {

  case class IpInfo(query: String)


  trait Protocols extends ErrorAccumulatingCirceSupport {
    import io.circe.generic.semiauto._
    implicit val ipInfoDecoder: Decoder[IpInfo] = deriveDecoder
    implicit val ipInfoEncoder: Encoder[IpInfo] = deriveEncoder
  }

}
