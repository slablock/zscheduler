package com.github.slablock.zscheduler.server.service

import com.github.slablock.zscheduler.server.broker.BrokerConf.config


trait ZSDbComponent {
 import slick.jdbc.JdbcProfile
 val profile: JdbcProfile

 import profile.api._
 val db: Database
}


class ZSMySqlDbComponent extends ZSDbComponent {
 import slick.jdbc.MySQLProfile.api._

 val profile = slick.jdbc.MySQLProfile
 val db = Database.forConfig("mysql", config)
}