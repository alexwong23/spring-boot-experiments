package com.example.experiments.model.Item;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
@Scope(value= ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ListedItem {

    @Autowired
    private Item item;

    private static Logger log = LoggerFactory.getLogger(ListedItem.class);

    public ListedItem() {
        log.info("ListedItem Bean Instantiated!");
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }
}
