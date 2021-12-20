package com.example.experiments.application;

import com.example.experiments.model.Item.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = Application.class)
@ComponentScan(basePackages = "com.example.experiments.*")  	// tells Spring to manage annotated components
@EntityScan("com.example.experiments.model.*") 					// tells Spring to scan for entity classes
public class ScopeTest {

    private static ConfigurableApplicationContext context;

    private static Logger log = LoggerFactory.getLogger(ScopeTest.class);
    private final String ACCOUNTS_XMLFILEPATH = "file:src/main/resources/beans/accounts.xml";
    private final String ITEMS_XMLFILEPATH = "file:src/main/resources/beans/items.xml";

    @BeforeAll
    public static void Initialisation() {
        context = SpringApplication.run(Application.class, new String[] { });
    }

    @BeforeEach
    public void SetUp() { }

    // NOTE :
    //  Class default scope Singleton: only one instance created!
    //  if class has scope prototype: more than one instance created!
    @Test
    public void TestScope_DefaultSingleton_ShouldPass() {
        ApplicationContext appContext = new ClassPathXmlApplicationContext(ITEMS_XMLFILEPATH);
        // NOTE : Consumable Bean only initialised ONCE
        Item consumableItem = appContext.getBean("consumableItem", Item.class);
        Item consumableItem2 = appContext.getBean("consumableItem", Item.class);
        assertEquals(consumableItem.getClass(), ConsumableItem.class);
        assertEquals(((ConsumableItem) consumableItem).getManufacturer().toString(), "Manufacturer{name='StarBucks', country='United States'}");
        assertTrue(consumableItem.equals(consumableItem2));
    }

    // NOTE : Prototype scope defined in AppConfiguration and XML file
    @Test
    public void TestScope_Prototype_ShouldPass() {
        ApplicationContext appContext = new ClassPathXmlApplicationContext(ITEMS_XMLFILEPATH);
        // NOTE : Decor Bean initialised TWICE
        log.info("-------Decor Bean Initialisation-------");
        Item decorItem = appContext.getBean("decorItem", Item.class);
        Item decorItem2 = appContext.getBean("decorItem", Item.class);
        assertEquals(decorItem.getClass(), DecorItem.class);
        assertEquals(((DecorItem) decorItem).getManufacturer().toString(), "Manufacturer{name='Ikea', country='Sweden'}");
        assertTrue(decorItem.equals(decorItem2));


        // NOTE : Listed Bean (autowires items) initialised TWICE
        log.info("-------Listed Bean Initialisation-------");
        ListedItem listedItem = appContext.getBean("listedItem", ListedItem.class);
        Item decorListed = listedItem.getItem();
        ListedItem listedItem2 = appContext.getBean("listedItem", ListedItem.class);
        Item decorListed2 = listedItem2.getItem();
        assertEquals(decorListed.getClass(), DecorItem.class);
        assertEquals(((DecorItem) decorListed).getManufacturer().toString(), "Manufacturer{name='Ikea', country='Sweden'}");
        assertTrue(decorListed.equals(decorListed2));
    }
}
