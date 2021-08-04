package com.github.slablock.zscheduler.dao
// AUTO-GENERATED Slick data model
/** Stand-alone Slick data model for immediate use */
object Tables extends {
  val profile = slick.jdbc.MySQLProfile
} with Tables

/** Slick data model trait for extension, choice of backend or usage in the cake pattern. (Make sure to initialize this late.) */
trait Tables {
  val profile: slick.jdbc.JdbcProfile
  import profile.api._
  import slick.model.ForeignKeyAction
  // NOTE: GetResult mappers for plain SQL are only generated for tables where Slick knows how to map the types of all columns.
  import slick.jdbc.{GetResult => GR}

  /** DDL for all tables. Call .create to execute. */
  lazy val schema: profile.SchemaDescription = Array(executionFlow.schema, flow.schema, flowDependency.schema, flowSchedule.schema, job.schema, jobDependency.schema, project.schema, task.schema).reduceLeft(_ ++ _)
  @deprecated("Use .schema instead of .ddl", "3.0")
  def ddl = schema

  /** Entity class storing rows of table executionFlow
   *  @param execId Database column execId SqlType(BIGINT UNSIGNED), AutoInc, PrimaryKey
   *  @param attemptId Database column attemptId SqlType(INT UNSIGNED), Default(0)
   *  @param projectId Database column projectId SqlType(BIGINT UNSIGNED), Default(0)
   *  @param flowId Database column flowId SqlType(BIGINT UNSIGNED), Default(0)
   *  @param scheduleTime Database column scheduleTime SqlType(DATETIME)
   *  @param dataTime Database column dataTime SqlType(DATETIME)
   *  @param progress Database column progress SqlType(FLOAT), Default(0.0)
   *  @param status Database column status SqlType(INT UNSIGNED), Default(0)
   *  @param executeUser Database column executeUser SqlType(VARCHAR), Length(32,true), Default()
   *  @param executeStartTime Database column executeStartTime SqlType(DATETIME), Default(None)
   *  @param executeEndTime Database column executeEndTime SqlType(DATETIME), Default(None)
   *  @param createTime Database column createTime SqlType(DATETIME)
   *  @param updateTime Database column updateTime SqlType(DATETIME) */
  case class ExecutionFlowRow(execId: Long, attemptId: Long = 0L, projectId: Long = 0L, flowId: Long = 0L, scheduleTime: java.sql.Timestamp, dataTime: java.sql.Timestamp, progress: Float = 0.0F, status: Long = 0L, executeUser: String = "", executeStartTime: Option[java.sql.Timestamp] = None, executeEndTime: Option[java.sql.Timestamp] = None, createTime: java.sql.Timestamp, updateTime: java.sql.Timestamp)
  /** GetResult implicit for fetching ExecutionFlowRow objects using plain SQL queries */
  implicit def GetResultExecutionFlowRow(implicit e0: GR[Long], e1: GR[java.sql.Timestamp], e2: GR[Float], e3: GR[String], e4: GR[Option[java.sql.Timestamp]]): GR[ExecutionFlowRow] = GR{
    prs => import prs._
    ExecutionFlowRow.tupled((<<[Long], <<[Long], <<[Long], <<[Long], <<[java.sql.Timestamp], <<[java.sql.Timestamp], <<[Float], <<[Long], <<[String], <<?[java.sql.Timestamp], <<?[java.sql.Timestamp], <<[java.sql.Timestamp], <<[java.sql.Timestamp]))
  }
  /** Table description of table execution_flow. Objects of this class serve as prototypes for rows in queries. */
  class ExecutionFlow(_tableTag: Tag) extends profile.api.Table[ExecutionFlowRow](_tableTag, Some("zscheduler"), "execution_flow") {
    def * = (execId, attemptId, projectId, flowId, scheduleTime, dataTime, progress, status, executeUser, executeStartTime, executeEndTime, createTime, updateTime) <> (ExecutionFlowRow.tupled, ExecutionFlowRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(execId), Rep.Some(attemptId), Rep.Some(projectId), Rep.Some(flowId), Rep.Some(scheduleTime), Rep.Some(dataTime), Rep.Some(progress), Rep.Some(status), Rep.Some(executeUser), executeStartTime, executeEndTime, Rep.Some(createTime), Rep.Some(updateTime))).shaped.<>({r=>import r._; _1.map(_=> ExecutionFlowRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get, _7.get, _8.get, _9.get, _10, _11, _12.get, _13.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column execId SqlType(BIGINT UNSIGNED), AutoInc, PrimaryKey */
    val execId: Rep[Long] = column[Long]("execId", O.AutoInc, O.PrimaryKey)
    /** Database column attemptId SqlType(INT UNSIGNED), Default(0) */
    val attemptId: Rep[Long] = column[Long]("attemptId", O.Default(0L))
    /** Database column projectId SqlType(BIGINT UNSIGNED), Default(0) */
    val projectId: Rep[Long] = column[Long]("projectId", O.Default(0L))
    /** Database column flowId SqlType(BIGINT UNSIGNED), Default(0) */
    val flowId: Rep[Long] = column[Long]("flowId", O.Default(0L))
    /** Database column scheduleTime SqlType(DATETIME) */
    val scheduleTime: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("scheduleTime")
    /** Database column dataTime SqlType(DATETIME) */
    val dataTime: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("dataTime")
    /** Database column progress SqlType(FLOAT), Default(0.0) */
    val progress: Rep[Float] = column[Float]("progress", O.Default(0.0F))
    /** Database column status SqlType(INT UNSIGNED), Default(0) */
    val status: Rep[Long] = column[Long]("status", O.Default(0L))
    /** Database column executeUser SqlType(VARCHAR), Length(32,true), Default() */
    val executeUser: Rep[String] = column[String]("executeUser", O.Length(32,varying=true), O.Default(""))
    /** Database column executeStartTime SqlType(DATETIME), Default(None) */
    val executeStartTime: Rep[Option[java.sql.Timestamp]] = column[Option[java.sql.Timestamp]]("executeStartTime", O.Default(None))
    /** Database column executeEndTime SqlType(DATETIME), Default(None) */
    val executeEndTime: Rep[Option[java.sql.Timestamp]] = column[Option[java.sql.Timestamp]]("executeEndTime", O.Default(None))
    /** Database column createTime SqlType(DATETIME) */
    val createTime: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("createTime")
    /** Database column updateTime SqlType(DATETIME) */
    val updateTime: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("updateTime")
  }
  /** Collection-like TableQuery object for table executionFlow */
  lazy val executionFlow = new TableQuery(tag => new ExecutionFlow(tag))

  /** Entity class storing rows of table flow
   *  @param flowId Database column flowId SqlType(BIGINT UNSIGNED), AutoInc, PrimaryKey
   *  @param projectId Database column projectId SqlType(BIGINT UNSIGNED), Default(0)
   *  @param flowName Database column flowName SqlType(VARCHAR), Length(100,true), Default()
   *  @param user Database column user SqlType(VARCHAR), Length(32,true), Default()
   *  @param updateUser Database column updateUser SqlType(VARCHAR), Length(32,true), Default()
   *  @param createTime Database column createTime SqlType(DATETIME)
   *  @param updateTime Database column updateTime SqlType(DATETIME) */
  case class FlowRow(flowId: Long, projectId: Long = 0L, flowName: String = "", user: String = "", updateUser: String = "", createTime: java.sql.Timestamp, updateTime: java.sql.Timestamp)
  /** GetResult implicit for fetching FlowRow objects using plain SQL queries */
  implicit def GetResultFlowRow(implicit e0: GR[Long], e1: GR[String], e2: GR[java.sql.Timestamp]): GR[FlowRow] = GR{
    prs => import prs._
    FlowRow.tupled((<<[Long], <<[Long], <<[String], <<[String], <<[String], <<[java.sql.Timestamp], <<[java.sql.Timestamp]))
  }
  /** Table description of table flow. Objects of this class serve as prototypes for rows in queries. */
  class Flow(_tableTag: Tag) extends profile.api.Table[FlowRow](_tableTag, Some("zscheduler"), "flow") {
    def * = (flowId, projectId, flowName, user, updateUser, createTime, updateTime) <> (FlowRow.tupled, FlowRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(flowId), Rep.Some(projectId), Rep.Some(flowName), Rep.Some(user), Rep.Some(updateUser), Rep.Some(createTime), Rep.Some(updateTime))).shaped.<>({r=>import r._; _1.map(_=> FlowRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get, _7.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column flowId SqlType(BIGINT UNSIGNED), AutoInc, PrimaryKey */
    val flowId: Rep[Long] = column[Long]("flowId", O.AutoInc, O.PrimaryKey)
    /** Database column projectId SqlType(BIGINT UNSIGNED), Default(0) */
    val projectId: Rep[Long] = column[Long]("projectId", O.Default(0L))
    /** Database column flowName SqlType(VARCHAR), Length(100,true), Default() */
    val flowName: Rep[String] = column[String]("flowName", O.Length(100,varying=true), O.Default(""))
    /** Database column user SqlType(VARCHAR), Length(32,true), Default() */
    val user: Rep[String] = column[String]("user", O.Length(32,varying=true), O.Default(""))
    /** Database column updateUser SqlType(VARCHAR), Length(32,true), Default() */
    val updateUser: Rep[String] = column[String]("updateUser", O.Length(32,varying=true), O.Default(""))
    /** Database column createTime SqlType(DATETIME) */
    val createTime: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("createTime")
    /** Database column updateTime SqlType(DATETIME) */
    val updateTime: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("updateTime")
  }
  /** Collection-like TableQuery object for table flow */
  lazy val flow = new TableQuery(tag => new Flow(tag))

  /** Entity class storing rows of table flowDependency
   *  @param id Database column id SqlType(BIGINT UNSIGNED), AutoInc, PrimaryKey
   *  @param projectId Database column projectId SqlType(BIGINT UNSIGNED), Default(0)
   *  @param flowId Database column flowId SqlType(BIGINT UNSIGNED), Default(0)
   *  @param preProjectId Database column preProjectId SqlType(BIGINT UNSIGNED), Default(0)
   *  @param preFlowId Database column preFlowId SqlType(BIGINT UNSIGNED), Default(0)
   *  @param preJobId Database column preJobId SqlType(BIGINT), Default(0)
   *  @param rangeExpression Database column rangeExpression SqlType(VARCHAR), Length(1024,true), Default()
   *  @param offsetExpression Database column offsetExpression SqlType(VARCHAR), Length(1024,true), Default()
   *  @param createTime Database column createTime SqlType(DATETIME)
   *  @param updateTime Database column updateTime SqlType(DATETIME) */
  case class FlowDependencyRow(id: Long, projectId: Long = 0L, flowId: Long = 0L, preProjectId: Long = 0L, preFlowId: Long = 0L, preJobId: Long = 0L, rangeExpression: String = "", offsetExpression: String = "", createTime: java.sql.Timestamp, updateTime: java.sql.Timestamp)
  /** GetResult implicit for fetching FlowDependencyRow objects using plain SQL queries */
  implicit def GetResultFlowDependencyRow(implicit e0: GR[Long], e1: GR[String], e2: GR[java.sql.Timestamp]): GR[FlowDependencyRow] = GR{
    prs => import prs._
    FlowDependencyRow.tupled((<<[Long], <<[Long], <<[Long], <<[Long], <<[Long], <<[Long], <<[String], <<[String], <<[java.sql.Timestamp], <<[java.sql.Timestamp]))
  }
  /** Table description of table flow_dependency. Objects of this class serve as prototypes for rows in queries. */
  class FlowDependency(_tableTag: Tag) extends profile.api.Table[FlowDependencyRow](_tableTag, Some("zscheduler"), "flow_dependency") {
    def * = (id, projectId, flowId, preProjectId, preFlowId, preJobId, rangeExpression, offsetExpression, createTime, updateTime) <> (FlowDependencyRow.tupled, FlowDependencyRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(id), Rep.Some(projectId), Rep.Some(flowId), Rep.Some(preProjectId), Rep.Some(preFlowId), Rep.Some(preJobId), Rep.Some(rangeExpression), Rep.Some(offsetExpression), Rep.Some(createTime), Rep.Some(updateTime))).shaped.<>({r=>import r._; _1.map(_=> FlowDependencyRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get, _7.get, _8.get, _9.get, _10.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(BIGINT UNSIGNED), AutoInc, PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)
    /** Database column projectId SqlType(BIGINT UNSIGNED), Default(0) */
    val projectId: Rep[Long] = column[Long]("projectId", O.Default(0L))
    /** Database column flowId SqlType(BIGINT UNSIGNED), Default(0) */
    val flowId: Rep[Long] = column[Long]("flowId", O.Default(0L))
    /** Database column preProjectId SqlType(BIGINT UNSIGNED), Default(0) */
    val preProjectId: Rep[Long] = column[Long]("preProjectId", O.Default(0L))
    /** Database column preFlowId SqlType(BIGINT UNSIGNED), Default(0) */
    val preFlowId: Rep[Long] = column[Long]("preFlowId", O.Default(0L))
    /** Database column preJobId SqlType(BIGINT), Default(0) */
    val preJobId: Rep[Long] = column[Long]("preJobId", O.Default(0L))
    /** Database column rangeExpression SqlType(VARCHAR), Length(1024,true), Default() */
    val rangeExpression: Rep[String] = column[String]("rangeExpression", O.Length(1024,varying=true), O.Default(""))
    /** Database column offsetExpression SqlType(VARCHAR), Length(1024,true), Default() */
    val offsetExpression: Rep[String] = column[String]("offsetExpression", O.Length(1024,varying=true), O.Default(""))
    /** Database column createTime SqlType(DATETIME) */
    val createTime: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("createTime")
    /** Database column updateTime SqlType(DATETIME) */
    val updateTime: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("updateTime")
  }
  /** Collection-like TableQuery object for table flowDependency */
  lazy val flowDependency = new TableQuery(tag => new FlowDependency(tag))

  /** Entity class storing rows of table flowSchedule
   *  @param id Database column id SqlType(BIGINT), AutoInc, PrimaryKey
   *  @param projectId Database column projectId SqlType(BIGINT), Default(0)
   *  @param flowId Database column flowId SqlType(BIGINT), Default(0)
   *  @param scheduleType Database column scheduleType SqlType(INT), Default(0)
   *  @param expression Database column expression SqlType(VARCHAR), Length(200,true), Default()
   *  @param createTime Database column createTime SqlType(DATETIME)
   *  @param updateTime Database column updateTime SqlType(DATETIME) */
  case class FlowScheduleRow(id: Long, projectId: Long = 0L, flowId: Long = 0L, scheduleType: Int = 0, expression: String = "", createTime: java.sql.Timestamp, updateTime: java.sql.Timestamp)
  /** GetResult implicit for fetching FlowScheduleRow objects using plain SQL queries */
  implicit def GetResultFlowScheduleRow(implicit e0: GR[Long], e1: GR[Int], e2: GR[String], e3: GR[java.sql.Timestamp]): GR[FlowScheduleRow] = GR{
    prs => import prs._
    FlowScheduleRow.tupled((<<[Long], <<[Long], <<[Long], <<[Int], <<[String], <<[java.sql.Timestamp], <<[java.sql.Timestamp]))
  }
  /** Table description of table flow_schedule. Objects of this class serve as prototypes for rows in queries. */
  class FlowSchedule(_tableTag: Tag) extends profile.api.Table[FlowScheduleRow](_tableTag, Some("zscheduler"), "flow_schedule") {
    def * = (id, projectId, flowId, scheduleType, expression, createTime, updateTime) <> (FlowScheduleRow.tupled, FlowScheduleRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(id), Rep.Some(projectId), Rep.Some(flowId), Rep.Some(scheduleType), Rep.Some(expression), Rep.Some(createTime), Rep.Some(updateTime))).shaped.<>({r=>import r._; _1.map(_=> FlowScheduleRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get, _7.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(BIGINT), AutoInc, PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)
    /** Database column projectId SqlType(BIGINT), Default(0) */
    val projectId: Rep[Long] = column[Long]("projectId", O.Default(0L))
    /** Database column flowId SqlType(BIGINT), Default(0) */
    val flowId: Rep[Long] = column[Long]("flowId", O.Default(0L))
    /** Database column scheduleType SqlType(INT), Default(0) */
    val scheduleType: Rep[Int] = column[Int]("scheduleType", O.Default(0))
    /** Database column expression SqlType(VARCHAR), Length(200,true), Default() */
    val expression: Rep[String] = column[String]("expression", O.Length(200,varying=true), O.Default(""))
    /** Database column createTime SqlType(DATETIME) */
    val createTime: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("createTime")
    /** Database column updateTime SqlType(DATETIME) */
    val updateTime: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("updateTime")
  }
  /** Collection-like TableQuery object for table flowSchedule */
  lazy val flowSchedule = new TableQuery(tag => new FlowSchedule(tag))

  /** Entity class storing rows of table job
   *  @param jobId Database column jobId SqlType(BIGINT UNSIGNED), AutoInc, PrimaryKey
   *  @param projectId Database column projectId SqlType(BIGINT UNSIGNED), Default(0)
   *  @param flowId Database column flowId SqlType(BIGINT UNSIGNED), Default(0)
   *  @param jobName Database column jobName SqlType(VARCHAR), Length(100,true), Default()
   *  @param jobType Database column jobType SqlType(INT), Default(0)
   *  @param contentType Database column contentType SqlType(INT), Default(0)
   *  @param content Database column content SqlType(MEDIUMTEXT), Length(16777215,true)
   *  @param params Database column params SqlType(VARCHAR), Length(2048,true), Default()
   *  @param priority Database column priority SqlType(INT), Default(0)
   *  @param user Database column user SqlType(VARCHAR), Length(32,true), Default()
   *  @param updateUser Database column updateUser SqlType(VARCHAR), Length(32,true), Default()
   *  @param createTime Database column createTime SqlType(DATETIME)
   *  @param updateTime Database column updateTime SqlType(DATETIME) */
  case class JobRow(jobId: Long, projectId: Long = 0L, flowId: Long = 0L, jobName: String = "", jobType: Int = 0, contentType: Int = 0, content: String, params: String = "", priority: Int = 0, user: String = "", updateUser: String = "", createTime: java.sql.Timestamp, updateTime: java.sql.Timestamp)
  /** GetResult implicit for fetching JobRow objects using plain SQL queries */
  implicit def GetResultJobRow(implicit e0: GR[Long], e1: GR[String], e2: GR[Int], e3: GR[java.sql.Timestamp]): GR[JobRow] = GR{
    prs => import prs._
    JobRow.tupled((<<[Long], <<[Long], <<[Long], <<[String], <<[Int], <<[Int], <<[String], <<[String], <<[Int], <<[String], <<[String], <<[java.sql.Timestamp], <<[java.sql.Timestamp]))
  }
  /** Table description of table job. Objects of this class serve as prototypes for rows in queries. */
  class Job(_tableTag: Tag) extends profile.api.Table[JobRow](_tableTag, Some("zscheduler"), "job") {
    def * = (jobId, projectId, flowId, jobName, jobType, contentType, content, params, priority, user, updateUser, createTime, updateTime) <> (JobRow.tupled, JobRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(jobId), Rep.Some(projectId), Rep.Some(flowId), Rep.Some(jobName), Rep.Some(jobType), Rep.Some(contentType), Rep.Some(content), Rep.Some(params), Rep.Some(priority), Rep.Some(user), Rep.Some(updateUser), Rep.Some(createTime), Rep.Some(updateTime))).shaped.<>({r=>import r._; _1.map(_=> JobRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get, _7.get, _8.get, _9.get, _10.get, _11.get, _12.get, _13.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column jobId SqlType(BIGINT UNSIGNED), AutoInc, PrimaryKey */
    val jobId: Rep[Long] = column[Long]("jobId", O.AutoInc, O.PrimaryKey)
    /** Database column projectId SqlType(BIGINT UNSIGNED), Default(0) */
    val projectId: Rep[Long] = column[Long]("projectId", O.Default(0L))
    /** Database column flowId SqlType(BIGINT UNSIGNED), Default(0) */
    val flowId: Rep[Long] = column[Long]("flowId", O.Default(0L))
    /** Database column jobName SqlType(VARCHAR), Length(100,true), Default() */
    val jobName: Rep[String] = column[String]("jobName", O.Length(100,varying=true), O.Default(""))
    /** Database column jobType SqlType(INT), Default(0) */
    val jobType: Rep[Int] = column[Int]("jobType", O.Default(0))
    /** Database column contentType SqlType(INT), Default(0) */
    val contentType: Rep[Int] = column[Int]("contentType", O.Default(0))
    /** Database column content SqlType(MEDIUMTEXT), Length(16777215,true) */
    val content: Rep[String] = column[String]("content", O.Length(16777215,varying=true))
    /** Database column params SqlType(VARCHAR), Length(2048,true), Default() */
    val params: Rep[String] = column[String]("params", O.Length(2048,varying=true), O.Default(""))
    /** Database column priority SqlType(INT), Default(0) */
    val priority: Rep[Int] = column[Int]("priority", O.Default(0))
    /** Database column user SqlType(VARCHAR), Length(32,true), Default() */
    val user: Rep[String] = column[String]("user", O.Length(32,varying=true), O.Default(""))
    /** Database column updateUser SqlType(VARCHAR), Length(32,true), Default() */
    val updateUser: Rep[String] = column[String]("updateUser", O.Length(32,varying=true), O.Default(""))
    /** Database column createTime SqlType(DATETIME) */
    val createTime: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("createTime")
    /** Database column updateTime SqlType(DATETIME) */
    val updateTime: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("updateTime")

    /** Index over (projectId) (database name project) */
    val index1 = index("project", projectId)
  }
  /** Collection-like TableQuery object for table job */
  lazy val job = new TableQuery(tag => new Job(tag))

  /** Entity class storing rows of table jobDependency
   *  @param id Database column id SqlType(BIGINT UNSIGNED), AutoInc, PrimaryKey
   *  @param projectId Database column projectId SqlType(BIGINT UNSIGNED), Default(0)
   *  @param flowId Database column flowId SqlType(BIGINT UNSIGNED), Default(0)
   *  @param jobId Database column jobId SqlType(BIGINT UNSIGNED), Default(0)
   *  @param preProjectId Database column preProjectId SqlType(BIGINT UNSIGNED), Default(0)
   *  @param preFlowId Database column preFlowId SqlType(BIGINT UNSIGNED), Default(0)
   *  @param preJobId Database column preJobId SqlType(BIGINT), Default(0)
   *  @param rangeExpression Database column rangeExpression SqlType(VARCHAR), Length(1024,true), Default()
   *  @param offsetExpression Database column offsetExpression SqlType(VARCHAR), Length(1024,true), Default()
   *  @param createTime Database column createTime SqlType(DATETIME)
   *  @param updateTime Database column updateTime SqlType(DATETIME) */
  case class JobDependencyRow(id: Long, projectId: Long = 0L, flowId: Long = 0L, jobId: Long = 0L, preProjectId: Long = 0L, preFlowId: Long = 0L, preJobId: Long = 0L, rangeExpression: String = "", offsetExpression: String = "", createTime: java.sql.Timestamp, updateTime: java.sql.Timestamp)
  /** GetResult implicit for fetching JobDependencyRow objects using plain SQL queries */
  implicit def GetResultJobDependencyRow(implicit e0: GR[Long], e1: GR[String], e2: GR[java.sql.Timestamp]): GR[JobDependencyRow] = GR{
    prs => import prs._
    JobDependencyRow.tupled((<<[Long], <<[Long], <<[Long], <<[Long], <<[Long], <<[Long], <<[Long], <<[String], <<[String], <<[java.sql.Timestamp], <<[java.sql.Timestamp]))
  }
  /** Table description of table job_dependency. Objects of this class serve as prototypes for rows in queries. */
  class JobDependency(_tableTag: Tag) extends profile.api.Table[JobDependencyRow](_tableTag, Some("zscheduler"), "job_dependency") {
    def * = (id, projectId, flowId, jobId, preProjectId, preFlowId, preJobId, rangeExpression, offsetExpression, createTime, updateTime) <> (JobDependencyRow.tupled, JobDependencyRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(id), Rep.Some(projectId), Rep.Some(flowId), Rep.Some(jobId), Rep.Some(preProjectId), Rep.Some(preFlowId), Rep.Some(preJobId), Rep.Some(rangeExpression), Rep.Some(offsetExpression), Rep.Some(createTime), Rep.Some(updateTime))).shaped.<>({r=>import r._; _1.map(_=> JobDependencyRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get, _7.get, _8.get, _9.get, _10.get, _11.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(BIGINT UNSIGNED), AutoInc, PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)
    /** Database column projectId SqlType(BIGINT UNSIGNED), Default(0) */
    val projectId: Rep[Long] = column[Long]("projectId", O.Default(0L))
    /** Database column flowId SqlType(BIGINT UNSIGNED), Default(0) */
    val flowId: Rep[Long] = column[Long]("flowId", O.Default(0L))
    /** Database column jobId SqlType(BIGINT UNSIGNED), Default(0) */
    val jobId: Rep[Long] = column[Long]("jobId", O.Default(0L))
    /** Database column preProjectId SqlType(BIGINT UNSIGNED), Default(0) */
    val preProjectId: Rep[Long] = column[Long]("preProjectId", O.Default(0L))
    /** Database column preFlowId SqlType(BIGINT UNSIGNED), Default(0) */
    val preFlowId: Rep[Long] = column[Long]("preFlowId", O.Default(0L))
    /** Database column preJobId SqlType(BIGINT), Default(0) */
    val preJobId: Rep[Long] = column[Long]("preJobId", O.Default(0L))
    /** Database column rangeExpression SqlType(VARCHAR), Length(1024,true), Default() */
    val rangeExpression: Rep[String] = column[String]("rangeExpression", O.Length(1024,varying=true), O.Default(""))
    /** Database column offsetExpression SqlType(VARCHAR), Length(1024,true), Default() */
    val offsetExpression: Rep[String] = column[String]("offsetExpression", O.Length(1024,varying=true), O.Default(""))
    /** Database column createTime SqlType(DATETIME) */
    val createTime: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("createTime")
    /** Database column updateTime SqlType(DATETIME) */
    val updateTime: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("updateTime")
  }
  /** Collection-like TableQuery object for table jobDependency */
  lazy val jobDependency = new TableQuery(tag => new JobDependency(tag))

  /** Entity class storing rows of table project
   *  @param projectId Database column projectId SqlType(BIGINT UNSIGNED), AutoInc, PrimaryKey
   *  @param projectName Database column projectName SqlType(VARCHAR), Length(100,true), Default()
   *  @param user Database column user SqlType(VARCHAR), Length(32,true), Default()
   *  @param updateUser Database column updateUser SqlType(VARCHAR), Length(32,true), Default()
   *  @param createTime Database column createTime SqlType(DATETIME)
   *  @param updateTime Database column updateTime SqlType(DATETIME) */
  case class ProjectRow(projectId: Long, projectName: String = "", user: String = "", updateUser: String = "", createTime: java.sql.Timestamp, updateTime: java.sql.Timestamp)
  /** GetResult implicit for fetching ProjectRow objects using plain SQL queries */
  implicit def GetResultProjectRow(implicit e0: GR[Long], e1: GR[String], e2: GR[java.sql.Timestamp]): GR[ProjectRow] = GR{
    prs => import prs._
    ProjectRow.tupled((<<[Long], <<[String], <<[String], <<[String], <<[java.sql.Timestamp], <<[java.sql.Timestamp]))
  }
  /** Table description of table project. Objects of this class serve as prototypes for rows in queries. */
  class Project(_tableTag: Tag) extends profile.api.Table[ProjectRow](_tableTag, Some("zscheduler"), "project") {
    def * = (projectId, projectName, user, updateUser, createTime, updateTime) <> (ProjectRow.tupled, ProjectRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(projectId), Rep.Some(projectName), Rep.Some(user), Rep.Some(updateUser), Rep.Some(createTime), Rep.Some(updateTime))).shaped.<>({r=>import r._; _1.map(_=> ProjectRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column projectId SqlType(BIGINT UNSIGNED), AutoInc, PrimaryKey */
    val projectId: Rep[Long] = column[Long]("projectId", O.AutoInc, O.PrimaryKey)
    /** Database column projectName SqlType(VARCHAR), Length(100,true), Default() */
    val projectName: Rep[String] = column[String]("projectName", O.Length(100,varying=true), O.Default(""))
    /** Database column user SqlType(VARCHAR), Length(32,true), Default() */
    val user: Rep[String] = column[String]("user", O.Length(32,varying=true), O.Default(""))
    /** Database column updateUser SqlType(VARCHAR), Length(32,true), Default() */
    val updateUser: Rep[String] = column[String]("updateUser", O.Length(32,varying=true), O.Default(""))
    /** Database column createTime SqlType(DATETIME) */
    val createTime: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("createTime")
    /** Database column updateTime SqlType(DATETIME) */
    val updateTime: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("updateTime")
  }
  /** Collection-like TableQuery object for table project */
  lazy val project = new TableQuery(tag => new Project(tag))

  /** Entity class storing rows of table task
   *  @param taskId Database column taskId SqlType(BIGINT UNSIGNED), AutoInc, PrimaryKey
   *  @param attemptId Database column attemptId SqlType(INT UNSIGNED), Default(0)
   *  @param projectId Database column projectId SqlType(BIGINT UNSIGNED), Default(0)
   *  @param flowId Database column flowId SqlType(BIGINT UNSIGNED), Default(0)
   *  @param jobId Database column jobId SqlType(BIGINT UNSIGNED), Default(0)
   *  @param content Database column content SqlType(MEDIUMTEXT), Length(16777215,true)
   *  @param params Database column params SqlType(VARCHAR), Length(2048,true), Default()
   *  @param scheduleTime Database column scheduleTime SqlType(DATETIME)
   *  @param dataTime Database column dataTime SqlType(DATETIME)
   *  @param progress Database column progress SqlType(FLOAT), Default(0.0)
   *  @param taskType Database column taskType SqlType(INT UNSIGNED), Default(0)
   *  @param status Database column status SqlType(INT UNSIGNED), Default(0)
   *  @param finishReason Database column finishReason SqlType(VARCHAR), Length(1024,true), Default()
   *  @param workerId Database column workerId SqlType(INT), Default(0)
   *  @param executeUser Database column executeUser SqlType(VARCHAR), Length(32,true), Default()
   *  @param executeStartTime Database column executeStartTime SqlType(DATETIME), Default(None)
   *  @param executeEndTime Database column executeEndTime SqlType(DATETIME), Default(None)
   *  @param createTime Database column createTime SqlType(DATETIME)
   *  @param updateTime Database column updateTime SqlType(DATETIME) */
  case class TaskRow(taskId: Long, attemptId: Long = 0L, projectId: Long = 0L, flowId: Long = 0L, jobId: Long = 0L, content: String, params: String = "", scheduleTime: java.sql.Timestamp, dataTime: java.sql.Timestamp, progress: Float = 0.0F, taskType: Long = 0L, status: Long = 0L, finishReason: String = "", workerId: Int = 0, executeUser: String = "", executeStartTime: Option[java.sql.Timestamp] = None, executeEndTime: Option[java.sql.Timestamp] = None, createTime: java.sql.Timestamp, updateTime: java.sql.Timestamp)
  /** GetResult implicit for fetching TaskRow objects using plain SQL queries */
  implicit def GetResultTaskRow(implicit e0: GR[Long], e1: GR[String], e2: GR[java.sql.Timestamp], e3: GR[Float], e4: GR[Int], e5: GR[Option[java.sql.Timestamp]]): GR[TaskRow] = GR{
    prs => import prs._
    TaskRow.tupled((<<[Long], <<[Long], <<[Long], <<[Long], <<[Long], <<[String], <<[String], <<[java.sql.Timestamp], <<[java.sql.Timestamp], <<[Float], <<[Long], <<[Long], <<[String], <<[Int], <<[String], <<?[java.sql.Timestamp], <<?[java.sql.Timestamp], <<[java.sql.Timestamp], <<[java.sql.Timestamp]))
  }
  /** Table description of table task. Objects of this class serve as prototypes for rows in queries. */
  class Task(_tableTag: Tag) extends profile.api.Table[TaskRow](_tableTag, Some("zscheduler"), "task") {
    def * = (taskId, attemptId, projectId, flowId, jobId, content, params, scheduleTime, dataTime, progress, taskType, status, finishReason, workerId, executeUser, executeStartTime, executeEndTime, createTime, updateTime) <> (TaskRow.tupled, TaskRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(taskId), Rep.Some(attemptId), Rep.Some(projectId), Rep.Some(flowId), Rep.Some(jobId), Rep.Some(content), Rep.Some(params), Rep.Some(scheduleTime), Rep.Some(dataTime), Rep.Some(progress), Rep.Some(taskType), Rep.Some(status), Rep.Some(finishReason), Rep.Some(workerId), Rep.Some(executeUser), executeStartTime, executeEndTime, Rep.Some(createTime), Rep.Some(updateTime))).shaped.<>({r=>import r._; _1.map(_=> TaskRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get, _7.get, _8.get, _9.get, _10.get, _11.get, _12.get, _13.get, _14.get, _15.get, _16, _17, _18.get, _19.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column taskId SqlType(BIGINT UNSIGNED), AutoInc, PrimaryKey */
    val taskId: Rep[Long] = column[Long]("taskId", O.AutoInc, O.PrimaryKey)
    /** Database column attemptId SqlType(INT UNSIGNED), Default(0) */
    val attemptId: Rep[Long] = column[Long]("attemptId", O.Default(0L))
    /** Database column projectId SqlType(BIGINT UNSIGNED), Default(0) */
    val projectId: Rep[Long] = column[Long]("projectId", O.Default(0L))
    /** Database column flowId SqlType(BIGINT UNSIGNED), Default(0) */
    val flowId: Rep[Long] = column[Long]("flowId", O.Default(0L))
    /** Database column jobId SqlType(BIGINT UNSIGNED), Default(0) */
    val jobId: Rep[Long] = column[Long]("jobId", O.Default(0L))
    /** Database column content SqlType(MEDIUMTEXT), Length(16777215,true) */
    val content: Rep[String] = column[String]("content", O.Length(16777215,varying=true))
    /** Database column params SqlType(VARCHAR), Length(2048,true), Default() */
    val params: Rep[String] = column[String]("params", O.Length(2048,varying=true), O.Default(""))
    /** Database column scheduleTime SqlType(DATETIME) */
    val scheduleTime: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("scheduleTime")
    /** Database column dataTime SqlType(DATETIME) */
    val dataTime: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("dataTime")
    /** Database column progress SqlType(FLOAT), Default(0.0) */
    val progress: Rep[Float] = column[Float]("progress", O.Default(0.0F))
    /** Database column taskType SqlType(INT UNSIGNED), Default(0) */
    val taskType: Rep[Long] = column[Long]("taskType", O.Default(0L))
    /** Database column status SqlType(INT UNSIGNED), Default(0) */
    val status: Rep[Long] = column[Long]("status", O.Default(0L))
    /** Database column finishReason SqlType(VARCHAR), Length(1024,true), Default() */
    val finishReason: Rep[String] = column[String]("finishReason", O.Length(1024,varying=true), O.Default(""))
    /** Database column workerId SqlType(INT), Default(0) */
    val workerId: Rep[Int] = column[Int]("workerId", O.Default(0))
    /** Database column executeUser SqlType(VARCHAR), Length(32,true), Default() */
    val executeUser: Rep[String] = column[String]("executeUser", O.Length(32,varying=true), O.Default(""))
    /** Database column executeStartTime SqlType(DATETIME), Default(None) */
    val executeStartTime: Rep[Option[java.sql.Timestamp]] = column[Option[java.sql.Timestamp]]("executeStartTime", O.Default(None))
    /** Database column executeEndTime SqlType(DATETIME), Default(None) */
    val executeEndTime: Rep[Option[java.sql.Timestamp]] = column[Option[java.sql.Timestamp]]("executeEndTime", O.Default(None))
    /** Database column createTime SqlType(DATETIME) */
    val createTime: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("createTime")
    /** Database column updateTime SqlType(DATETIME) */
    val updateTime: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("updateTime")
  }
  /** Collection-like TableQuery object for table task */
  lazy val task = new TableQuery(tag => new Task(tag))
}
