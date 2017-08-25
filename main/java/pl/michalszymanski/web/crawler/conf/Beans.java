/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.michalszymanski.web.crawler.conf;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;
import javax.validation.Validation;
import javax.validation.Validator;
import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Qualifier;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.SpringConstraintValidatorFactory;

/**
 *
 * @author Michał Szymański, kontakt: michal.szymanski.aajar@gmail.com
 */
@Configuration
@EnableScheduling
public class Beans {

    @Bean
    public Validator validator() {
        return new LocalValidatorFactoryBean();
    }

    @Bean
    public NamedParameterJdbcTemplate template() {
        return new NamedParameterJdbcTemplate(ds());
    }

    @Bean
    public DataSource ds() {
        BasicDataSource ds = new BasicDataSource();
        Properties p = jdbcProperties();
        ds.setDriverClassName(p.getProperty("jdbc.driver"));
        ds.setUsername(p.getProperty("jdbc.username"));
        ds.setPassword(p.getProperty("jdbc.password"));
        ds.setUrl(p.getProperty("jdbc.url"));

        ds.setTestOnBorrow(true);
        ds.setValidationQuery("SELECT 1");

        return ds;
    }

    @Bean
    @Qualifier("mail")
    public Properties mailProperties() {
        InputStream is = this.getClass().getClassLoader().getResourceAsStream("mail.properties");
        Properties p = new Properties();
        try {
            p.load(is);
        } catch (IOException ex) {
            Logger.getLogger(Beans.class.getName()).log(Level.SEVERE, null, ex);
        }
        return p;
    }

    @Bean
    @Qualifier("jdbc")
    public Properties jdbcProperties() {
        InputStream is = this.getClass().getClassLoader().getResourceAsStream("jdbc.properties");
        Properties p = new Properties();
        try {
            p.load(is);
        } catch (IOException ex) {
            Logger.getLogger(Beans.class.getName()).log(Level.SEVERE, null, ex);
        }
        return p;
    }

    @Bean
    @Qualifier("specialObjectMapper")
    public ObjectMapper mapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(mapper.getVisibilityChecker().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
        return mapper;
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        return new DataSourceTransactionManager(ds());
    }
}
