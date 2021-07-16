
CREATE TABLE `project` (
    `projectId` bigint(11) unsigned NOT NULL AUTO_INCREMENT,
    `projectName` varchar(100) NOT NULL DEFAULT '' COMMENT '项目名称',
    `user` varchar(32) NOT NULL DEFAULT '' COMMENT '项目所属人',
    `updateUser` varchar(32) not null default '' comment '更新人',
    `createTime` datetime NOT NULL COMMENT '创建时间',
    `updateTime` datetime NOT NULL COMMENT '最后更新时间',
    PRIMARY KEY (`projectId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='项目表';


CREATE TABLE `flow` (
    `flowId` bigint(11) unsigned NOT NULL AUTO_INCREMENT,
    `projectId` bigint(11) unsigned NOT NULL DEFAULT '0' COMMENT '项目ID',
    `flowName` varchar(100) NOT NULL DEFAULT '' COMMENT '流名称',
    `user` varchar(32) NOT NULL DEFAULT '' COMMENT '所属人',
    `updateUser` varchar(32) not null default '' comment '更新人',
    `createTime` datetime NOT NULL COMMENT '创建时间',
    `updateTime` datetime NOT NULL COMMENT '最后更新时间',
    PRIMARY KEY (`flowId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='工作流表';



CREATE TABLE `job` (
    `jobId` bigint(11) unsigned NOT NULL AUTO_INCREMENT,
    `projectId` bigint(11) unsigned NOT NULL DEFAULT '0' COMMENT '项目ID',
    `jobName` varchar(100) NOT NULL DEFAULT '' COMMENT '任务名称',
    `jobType` int(3) NOT NULL DEFAULT '0' COMMENT '任务类型',
    `contentType` int(3) not null default '0' comment '任务内容类型',
    `content` mediumtext not null comment '任务内容',
    `params` varchar(2048) not null default '' comment '任务参数',
    `priority` int(3) not null default '0' comment '任务优先级',
    `user` varchar(32) not null default '' comment '任务所属人',
    `updateUser` varchar(32) not null default '' comment '更新人',
    `createTime` datetime NOT NULL COMMENT '创建时间',
    `updateTime` datetime NOT NULL COMMENT '最后更新时间',
    PRIMARY KEY (`jobId`),
    key `project` (`projectId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='任务表';


create table `flow_dependency` (
    `id` bigint(11) unsigned not null AUTO_INCREMENT,
    `projectId` bigint(11) unsigned not null default '0' comment '项目ID',
    `flowId` bigint(11) unsigned not null default '0' comment '流ID',
    `preProjectId` bigint(11) unsigned not null default '0' comment '前置项目ID',
    `preFlowId` bigint(11) unsigned not null default '0' comment '前置流ID',
    `preJobId` bigint(11) not null default '0' comment '前置任务ID，如果为-1标识依赖流',
    `rangeExpression` varchar(1024) not null default '' comment 'range',
    `offsetExpression` varchar(1024) not null default '' comment 'offset',
    `createTime` datetime NOT NULL COMMENT '创建时间',
    `updateTime` datetime NOT NULL COMMENT '最后更新时间',
PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='流依赖表';


create table `job_dependency` (
    `id` bigint(11) unsigned not null AUTO_INCREMENT,
    `projectId` bigint(11) unsigned not null default '0' comment '项目ID',
    `flowId` bigint(11) unsigned not null default '0' comment '流ID',
    `jobId` bigint(11) unsigned not null default '0' comment '任务ID',
    `preProjectId` bigint(11) unsigned not null default '0' comment '前置项目ID',
    `preFlowId` bigint(11) unsigned not null default '0' comment '前置流ID',
    `preJobId` bigint(11) not null default '0' comment '前置任务ID，如果为-1标识依赖流',
    `rangeExpression` varchar(1024) not null default '' comment 'range',
    `offsetExpression` varchar(1024) not null default '' comment 'offset',
    `createTime` datetime NOT NULL COMMENT '创建时间',
    `updateTime` datetime NOT NULL COMMENT '最后更新时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='任务依赖表';


create table `flow_schedule` (
    `id` bigint(11) not null AUTO_INCREMENT,
    `projectId` bigint(11) not null default '0' comment '项目ID',
    `flowId` bigint(11) not null default '0' comment '流ID',
    `scheduleType` int(3) not null default '0' comment '调度类型',
    `expression` varchar(200) not null default '' comment '调度表达式',
    `createTime` datetime NOT NULL COMMENT '创建时间',
    `updateTime` datetime NOT NULL COMMENT '最后更新时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='流调度表';


CREATE TABLE `task` (
    `taskId` bigint(11) unsigned NOT NULL AUTO_INCREMENT COMMENT 'taskId',
    `attemptId` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '最后的尝试ID',
    `projectId` bigint(11) unsigned not null default '0' comment '项目ID',
    `flowId` bigint(11) unsigned not null default '0' comment '流ID',
    `jobId` bigint(11) unsigned NOT NULL DEFAULT '0' COMMENT '所属JobID',
    `content` mediumtext NOT NULL COMMENT '任务内容',
    `params` varchar(2048) NOT NULL DEFAULT '' COMMENT '任务参数',
    `scheduleTime` datetime NOT NULL COMMENT '调度时间',
    `dataTime` datetime NOT NULL COMMENT '数据时间',
    `progress` float NOT NULL DEFAULT '0' COMMENT '执行进度',
    `taskType` int(3) unsigned NOT NULL DEFAULT '0' COMMENT 'task类型：1:调度的task; 2:手动重跑的task; 3:一次性任务',
    `status` int(3) unsigned NOT NULL DEFAULT '0' COMMENT '执行状态： 1:waiting；2:ready；3:running；4:success；5:failed；6:killed；7:paused; 99:removed',
    `finishReason` varchar(1024) NOT NULL DEFAULT '' COMMENT 'task结束的原因',
    `workerId` int(11) NOT NULL DEFAULT '0' COMMENT 'workerId',
    `executeUser` varchar(32) NOT NULL DEFAULT '' COMMENT '执行用户',
    `executeStartTime` datetime DEFAULT NULL COMMENT '执行开始时间',
    `executeEndTime` datetime DEFAULT NULL COMMENT '执行结束时间',
    `createTime` datetime NOT NULL COMMENT '创建时间',
    `updateTime` datetime NOT NULL COMMENT '最后更新时间',
    PRIMARY KEY (`taskId`),
    KEY `index_taskId` (`taskId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='task表';


CREATE TABLE `execution_flow` (
    `execId` bigint(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '执行ID',
    `attemptId` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '最后的尝试ID',
    `projectId` bigint(11) unsigned not null default '0' comment '项目ID',
    `flowId` bigint(11) unsigned not null default '0' comment '工作流ID',
    `scheduleTime` datetime NOT NULL COMMENT '调度时间',
    `dataTime` datetime NOT NULL COMMENT '数据时间',
    `progress` float NOT NULL DEFAULT '0' COMMENT '执行进度',
    `status` int(3) unsigned NOT NULL DEFAULT '0' COMMENT '执行状态： 1:waiting；2:ready；3:running；4:success；5:failed；6:killed；7:paused; 99:removed',
    `executeUser` varchar(32) NOT NULL DEFAULT '' COMMENT '执行用户',
    `executeStartTime` datetime DEFAULT NULL COMMENT '执行开始时间',
    `executeEndTime` datetime DEFAULT NULL COMMENT '执行结束时间',
    `createTime` datetime NOT NULL COMMENT '创建时间',
    `updateTime` datetime NOT NULL COMMENT '最后更新时间',
    PRIMARY KEY (`execId`),
    KEY `index_execId` (`execId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='流实例表';

