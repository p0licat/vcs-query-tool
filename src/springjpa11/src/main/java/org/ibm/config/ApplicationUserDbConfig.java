package org.ibm.config;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.sql.DataSource;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EntityScan("org.ibm.*")
@EnableJpaRepositories(entityManagerFactoryRef = "entityManagerFactory", basePackages={"org.ibm.*", "org.ibm.jpaservice.contentsgatherer"})
public class ApplicationUserDbConfig {

	Logger logger = Logger.getLogger(getClass().getName());
	
	@Primary
	@Bean(name="datasource")
	@ConfigurationProperties(prefix="spring.datasource")
	public DataSource dataSource() {
		logger.info("Building data source");
		return DataSourceBuilder.create().build();
	}

	@Primary
	@Bean(name="entityManagerFactory")
	public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean(EntityManagerFactoryBuilder builder, @Qualifier("datasource") DataSource dataSource) {
		Map<String, Object> properties = new HashMap<>();
		//properties.put("spring.jpa.properties.hibernate.hbm2ddl.auto", "create");
		logger.info("entitymanagerfactory");
		return builder.dataSource(dataSource).properties(properties).packages("org.ibm.model.applicationuser").persistenceUnit("ApplicationUser").build();
	}

	@Bean(name="transactionManager")
	public PlatformTransactionManager transactionManager(@Qualifier("entityManagerFactory") EntityManagerFactory entityManagerFactory) {
		logger.info("Config 2");
		return new JpaTransactionManager(entityManagerFactory);
	}
	
}
