package org.ibm.springjpa;

import java.util.List;

import javax.sql.DataSource;

import org.ibm.applicationuser.repository.ApplicationUserRepository;
import org.ibm.model.applicationuser.ApplicationUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@PropertySources({
	@PropertySource({"classpath:application.properties"})
})
@RestController
public class SpringjpaApplication {

	@Autowired
	private ApplicationUserRepository userRepository;
	
	@GetMapping
	public List<ApplicationUser> getUsers() {
		return this.userRepository.findAll();
	}
	
	public static void main(String[] args) {
		SpringApplication.run(SpringjpaApplication.class, args);
	}

	/*
	 * @Bean public DataSource dataSource() { DriverManagerDataSource dataSource =
	 * new DriverManagerDataSource(); dataSource.setDriverClassName(""); }
	 */
}
