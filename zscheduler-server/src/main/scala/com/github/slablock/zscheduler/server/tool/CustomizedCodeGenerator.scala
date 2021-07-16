package com.github.slablock.zscheduler.server.tool

import slick.jdbc.MySQLProfile

import scala.concurrent._
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
import slick.jdbc.MySQLProfile.api._

/**
 * This customizes the Slick code generator. We only do simple name mappings.
 * For a more advanced example see https://github.com/cvogt/slick-presentation/tree/scala-exchange-2013
 */
object CustomizedCodeGenerator {

  val url = "jdbc:mysql://localhost:3306/zscheduler?useUnicode=true&characterEncoding=UTF-8&useSSL=false"
  val jdbcDriver = "com.mysql.cj.jdbc.Driver"
  val profile = "slick.jdbc.MySQLProfile"
  val folder = "zscheduler-dao/src/main/scala"
  val pkg = "com.github.slablock.zscheduler.dao"
  val user = "root"
  val password = "root"

  def main(args: Array[String]): Unit = {
    Await.ready(
      codegen.map(_.writeToFile(
        profile,
        folder,
        pkg
      )),
      20.seconds
    )
  }


  val db = Database.forURL(url, user, password, driver = jdbcDriver)

  // filter out desired tables
  val codegen = db.run {
    MySQLProfile.defaultTables.flatMap(MySQLProfile.createModelBuilder(_, false).buildModel)
  }.map { model =>
    new slick.codegen.SourceCodeGenerator(model) {
      // customize Scala entity name (case class, etc.)
      override def entityName = dbTableName => dbTableName match {
        case _ => super.entityName(dbTableName)
      }
      // customize Scala table name (table class, table values, ...)
      override def tableName = dbTableName => dbTableName match {
        case _ => super.tableName(dbTableName)
      }

      // override generator responsible for tables
      override def Table = new Table(_) {
        table =>
        // customize table value (TableQuery) name (uses tableName as a basis)
        override def TableValue = new TableValue {
          override def rawName = super.rawName.uncapitalize
        }

        // override generator responsible for columns
        override def Column = new Column(_) {
          // customize Scala column names
          override def rawName = (table.model.name.table, this.model.name) match {
            case _ => this.model.name
          }
        }
      }
    }
  }
}
