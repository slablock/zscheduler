
package com.github.slablock.zscheduler.server.guice;

import com.typesafe.config.Config;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.mybatis.guice.MyBatisModule;

public class ZSMyBatisModule extends MyBatisModule {

    private final Config config;

    public ZSMyBatisModule(Config config) {
        this.config = config;
    }

    @Override
    protected void initialize() {
        environmentId("development");
        addMapperClasses("com.github.slablock.zscheduler.dao");
        bindDataSourceProvider(new DataSourceProvider(config));
        bindTransactionFactoryType(JdbcTransactionFactory.class);
    }
}
