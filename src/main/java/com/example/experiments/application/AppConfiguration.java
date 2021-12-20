package com.example.experiments.application;

import com.example.experiments.model.Account.Account;
import com.example.experiments.model.Account.Admin;
import com.example.experiments.model.Account.User;
import com.example.experiments.model.Item.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.*;

// TODO: bean vs configuration
//  Bean and Configuration introduced to get rid of XML bean definition
//  Bean returns a customizable instance of spring bean
//  Component defines a class that may be later instantiated by Spring IoC engine when needed
//      one-to-one mapping between annotated class and the bean
@Configuration
@ComponentScan("com.example.experiments.*") // tells Spring to manage annotated components
public class AppConfiguration {

    public static Logger log = LoggerFactory.getLogger(Admin.class);

    @Bean
    @ConditionalOnProperty(value = "account.default.type", havingValue = "admin", matchIfMissing = false)
    public Account admin() {
        return new Admin();
    }

    @Bean
    @ConditionalOnProperty(value = "account.default.type", havingValue = "user", matchIfMissing = true) // NOTE: default type
    public Account user() {
        return new User();
    }

    @Bean(name = "consumableItem")
    @Qualifier(value = "consumableItem")
    @ConditionalOnProperty(value = "item.default.type", havingValue = "consumable", matchIfMissing = false)
    public Item consumableItem() {
        return new ConsumableItem();
    }


    @Bean(name = "decorItem")
    @Qualifier(value = "decorItem")
    @Primary
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    @ConditionalOnProperty(value = "item.default.type", havingValue = "decor", matchIfMissing = true) // NOTE: default type
    public Item decorItem() {
        return new DecorItem();
    }

}
