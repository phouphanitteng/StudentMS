package kh.com.acleda.student.config;

import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Objects;
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "kh.com.acleda.student.repository.pgsql")
@RequiredArgsConstructor
public class PgSQLConfig {

    private final Environment env;

    @Bean(name = "pgSqlDataSource")
    @ConfigurationProperties(prefix = "pgsql.datasource")
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl(env.getProperty("postgresql.datasource.url"));
        dataSource.setUsername(env.getProperty("postgresql.datasource.username"));
        dataSource.setPassword(env.getProperty("postgresql.datasource.password"));
        dataSource.setDriverClassName(Objects.requireNonNull(env.getProperty("postgresql.datasource.driver-class-name")));
        return dataSource;
    }

    @Bean(name = "pgSqlEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("pgSqlDataSource") DataSource dataSource
    ) {
        return builder.dataSource(dataSource)
                .packages("kh.com.acleda.student.entity.pgsql")
                .persistenceUnit("mysql")
                .build();
    }

    @Bean(name = "pgSqlTransactionManager")
    public PlatformTransactionManager transactionManager(
            @Qualifier("pgSqlEntityManagerFactory") EntityManagerFactory entityManagerFactory
    ) {
        return new JpaTransactionManager((entityManagerFactory));
    }
}