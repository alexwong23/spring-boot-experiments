package com.example.experiments.application;

import com.example.experiments.model.Account.Account;
import com.example.experiments.model.Account.Admin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

// NOTE: it will fire up a servlet container and serve up our service
@SpringBootApplication // convenient alternative to @Configuration + @EnableAutoConfiguration + @ComponentScan with their default attributes
@EnableJpaRepositories("com.example.experiments.repository")	// tells Spring to enable JPA repositories
@ComponentScan(basePackages = "com.example.experiments.*")  	// tells Spring to manage annotated components
@EntityScan("com.example.experiments.model.*") 					// tells Spring to scan for entity classes
public class Application {

	public static Logger log = LoggerFactory.getLogger(Admin.class);

	// command to run ./gradlew bootRun
	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);
		log.info("-------------------Start of PSVM-------------------");

		try {
			Account defaultAccount = context.getBean(Account.class);
//			log.info("default: " + defaultAccount);
		} catch(Exception e) {
			log.info("An error occurred in the application: " + e.getMessage());
		}

		log.info("-------------------End of PSVM-------------------");
	}
}
