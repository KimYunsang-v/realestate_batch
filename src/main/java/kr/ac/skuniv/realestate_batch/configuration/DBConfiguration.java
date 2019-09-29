package kr.ac.skuniv.realestate_batch.configuration;


import com.mysql.cj.jdbc.MysqlXADataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;

@Configuration
public class DBConfiguration {

    @Autowired
    private Environment env;

    /**
     * Siphon data source.
     *
     * @return the data source
     */
    @Bean(name = "mainDataSource")
    @Primary
    public DataSource mainDataSource() {

        final String user = this.env.getProperty("spring.datasource.username");
        final String password = this.env.getProperty("spring.datasource.password");
        final String url = this.env.getProperty("spring.datasource.jdbc-url");

        return this.getMysqlXADataSource(url, user, password);
    }

    private MysqlXADataSource getMysqlXADataSource(final String url, final String user, final String password) {

        final MysqlXADataSource mysql = new MysqlXADataSource();
        mysql.setUser(user);
        mysql.setPassword(password);
        mysql.setUrl(url);

        return mysql;
    }
}
