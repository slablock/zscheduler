
package com.github.slablock.zscheduler.server.guice;

import com.google.inject.Provider;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigValue;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.util.Map.Entry;

public class DataSourceProvider implements Provider<DataSource> {

    private final HikariDataSource dataSource;
    private static final String PREFIX = "zs.dataSource.";

    public DataSourceProvider(Config config) {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDataSourceClassName(config.getString(PREFIX + "dataSourceClassName"));
        for (Entry<String, ConfigValue> entry : config.entrySet()) {
            String key = entry.getKey();
            if (key.startsWith(PREFIX)) {
                hikariConfig.addDataSourceProperty(key.replace(PREFIX, ""), entry.getValue().unwrapped());
            }
        }
        dataSource = new HikariDataSource(hikariConfig);
    }

    @Override
    public DataSource get() {
        return dataSource;
    }

}
