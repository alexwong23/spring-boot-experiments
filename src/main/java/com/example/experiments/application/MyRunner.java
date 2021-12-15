package com.example.experiments.application;

import com.example.experiments.model.Account.Account;
import com.example.experiments.model.Account.User;
import com.example.experiments.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

// CommandLineRunner guide -> https://zetcode.com/springboot/commandlinerunner/

@Component // autodetected and registered
public class MyRunner implements CommandLineRunner { // NOTE: CommandLineRunner interface indicates which beans to run

    @Autowired // inject repository bean into repository field
    private UserRepository userRepository;

    private static Logger log = LoggerFactory.getLogger(MyRunner.class);

    @Override
    public void run(String... args) throws Exception { // NOTE: method executes after application starts
        userRepository.deleteAll();

        ApplicationContext appContext = new ClassPathXmlApplicationContext("file:src/main/resources/beans/accounts.xml");
        Account userBean = appContext.getBean("user", Account.class);
        User user = new User(
                "MoSalahhh",
                "PaSSW0eeRD4",
                "jamessoh@gmail.com",
                "James",
                "Soh",
                LocalDate.of(2000, Month.JANUARY, 5));
        User user2 = new User(
                "julius922",
                "gloryOfRomsde3",
                "juliuscaesar@gmail.com",
                "Julius",
                "Caesar",
                LocalDate.of(1994, Month.SEPTEMBER, 15));
        userRepository.saveAll(List.of(user, user2, (User) userBean));
    }
}
