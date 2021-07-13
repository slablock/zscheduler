#!/usr/bin/env bash
BIN_PATH=$(cd $(dirname $0);pwd)
ZS_HOME=`dirname "$BIN_PATH"`
ZS_DAO_HOME=${ZS_HOME}/zscheduler-dao
MYBATIS_CONFIG_FILE=${ZS_DAO_HOME}/src/main/resources/mybatis_generator.xml

cd ${ZS_DAO_HOME}
rm ${ZS_DAO_HOME}/src/main/resources/com/github/slablock/zscheduler/dao/mapper/generate/*.xml

#mvn org.mybatis.generator:mybatis-generator-maven-plugin:generate

java -cp ${BIN_PATH}/mybatis-generator-core-1.4.0.jar:${BIN_PATH}/mysql-connector-java-6.0.6.jar  org.mybatis.generator.api.ShellRunner -configfile ${MYBATIS_CONFIG_FILE} -overwrite -verbose
