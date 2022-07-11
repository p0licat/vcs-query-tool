package org.ibm.config;

import javax.sql.DataSource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(entityManagerFactoryRef = "entityManagerFactory", basePackages={"org.ibm.applicationuser.repository.ApplicationUserRepository"})
public class ApplicationUserDbConfig {
	@Primary
	@Bean(name="datasource")
	@ConfigurationProperties(prefix="datasource")
	public DataSource dataSource() {
		return DataSourceBuilder.create().build();
	}
}
