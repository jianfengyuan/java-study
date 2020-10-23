package config;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import javax.sql.DataSource;

@Configurable
@ComponentScan("config")
public class SpringConfiguration {
    @Bean(name = "runner")
    public QueryRunner createQueryRunner(DataSource dataSource) {
        return new QueryRunner();
    }

    @Bean()
    public DataSource createDatasource() {
        ComboPooledDataSource ds = new ComboPooledDataSource();
        ds.setDriverClass();

    }
}
