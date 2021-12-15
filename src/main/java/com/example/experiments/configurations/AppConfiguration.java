package com.example.experiments.configurations;

import com.example.experiments.model.Account.Account;
import com.example.experiments.model.Account.Admin;
import com.example.experiments.model.Account.User;
import com.example.experiments.model.Item.ConsumableItem;
import com.example.experiments.model.Item.DecorItem;
import com.example.experiments.model.Item.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfiguration {

    public static Logger log = LoggerFactory.getLogger(Admin.class);

    @Bean
    @ConditionalOnProperty(value = "account.service.mode", havingValue = "admin", matchIfMissing = false)
    public Account admin() {
        return new Admin();
    }

    @Bean
    @ConditionalOnProperty(value = "account.service.mode", havingValue = "user", matchIfMissing = true) // NOTE: default type
    public Account user() {
        return new User();
    }

    @Bean
    @ConditionalOnProperty(value = "item.service.mode", havingValue = "consumable", matchIfMissing = false)
    public Item consumableItemService() {
        return new ConsumableItem();
    }

    @Bean
    @ConditionalOnProperty(value = "item.service.mode", havingValue = "decor", matchIfMissing = true) // NOTE: default type
    public Item decorItemService() {
        return new DecorItem();
    }
}
