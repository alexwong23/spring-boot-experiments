package com.example.experiments.configurations;

import com.example.experiments.model.Account.Account;
import com.example.experiments.model.Account.Admin;
import com.example.experiments.model.Account.User;
import com.example.experiments.model.Item.ConsumableItem;
import com.example.experiments.model.Item.DecorItem;
import com.example.experiments.model.Item.Item;
import com.example.experiments.model.Item.Manufacturer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

// NOTE: Bean and Configuration introduced to get rid of XML bean definition
//  Bean returns a customizable instance of spring bean
//  Component defines a class that may be later instantiated by Spring IoC engine when needed
//      one-to-one mapping between annotated class and the bean
@Configuration
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

    @Bean
    @ConditionalOnProperty(value = "item.default.type", havingValue = "consumable", matchIfMissing = false)
    public Item consumableItemService() {
        return new ConsumableItem();
    }

    @Bean
    @ConditionalOnProperty(value = "item.default.type", havingValue = "decor", matchIfMissing = true) // NOTE: default type
    public Item decorItemService() {
        return new DecorItem();
    }

//    @Bean
//    public Manufacturer manufacturerService() {
//        return new Manufacturer();
//    }
}
