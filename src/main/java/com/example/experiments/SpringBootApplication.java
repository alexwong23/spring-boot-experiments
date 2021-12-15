package com.example.experiments;

import com.example.experiments.model.Account.Account;
import com.example.experiments.model.Account.Admin;
import com.example.experiments.model.Item.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.support.ClassPathXmlApplicationContext;

// NOTE: it will fire up a servlet container and serve up our service
// TODO: difference between autoconfigure.SpringBootApplication and normal
@org.springframework.boot.autoconfigure.SpringBootApplication
public class SpringBootApplication {

	public static Logger log = LoggerFactory.getLogger(Admin.class);

	// command to run ./gradlew bootRun
	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(SpringBootApplication.class, args);
		log.info("-------------------Start of Program-------------------");

		try {
			Account defaultAccount = context.getBean(Account.class); // NOTE: default account type is User
			log.info("default: " + String.valueOf(defaultAccount));
		} catch(Exception e) {
			log.info("An error occurred in the application: " + e.getMessage());
		}

		log.info("-------------------End of Program-------------------");
	}
}
