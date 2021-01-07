package com.kim.config;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.*;

import javax.sql.DataSource;

@Configuration
@ComponentScan("com.kim")
@Import(JdbcConfiguration.class)
@PropertySource("classpath:jdbcConf.properties")
public class SpringConfiguration {

}
