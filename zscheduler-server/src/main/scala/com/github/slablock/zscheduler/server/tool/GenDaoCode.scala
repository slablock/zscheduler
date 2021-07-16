package com.github.slablock.zscheduler.server.tool

import slick.jdbc.JdbcProfile

object GenDaoCode extends App {

  val profile: JdbcProfile = slick.jdbc.MySQLProfile
  val jdbcUrl = "jdbc:mysql://localhost:3306/zscheduler?useUnicode=true&characterEncoding=UTF-8&useSSL=false"

  slick.codegen.SourceCodeGenerator.main(Array("slick.jdbc.MySQLProfile",
    "com.mysql.cj.jdbc.Driver",
    jdbcUrl, "zscheduler-dao/src/main/scala",
    "com.github.slablock.zscheduler.dao",
    "root",
    "root", "true"))
}
