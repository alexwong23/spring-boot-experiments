package com.example.experiments;

import com.example.experiments.application.Application;
import com.example.experiments.model.Account.Account;
import com.example.experiments.model.Account.Admin;
import com.example.experiments.model.Account.User;
import com.example.experiments.model.Item.ConsumableItem;
import com.example.experiments.model.Item.DecorItem;
import com.example.experiments.model.Item.Item;
import com.example.experiments.model.Item.Manufacturer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class)
@Configuration
@ComponentScan(basePackages = "com.example.experiments.*")  	// tells Spring to manage annotated components
@EntityScan("com.example.experiments.model.*") 					// tells Spring to scan for entity classes
public class ApplicationBeanTest {

    private static ConfigurableApplicationContext context;

    // TODO: what happens if i autowire this?
    private Account userBean, adminBean;
    private Item consumableItemBean, decorItemBean;

    private static Logger log = LoggerFactory.getLogger(ApplicationBeanTest.class);
    private final String ACCOUNTS_XMLFILEPATH = "file:src/main/resources/beans/accounts.xml";
    private final String ITEMS_XMLFILEPATH = "file:src/main/resources/beans/items.xml";

    @BeforeAll
    public static void Initialisation() {
//        SpringBootApplication.main(new String[] { });
        context = SpringApplication.run(Application.class, new String[] { });
    }

    @BeforeEach
    public void SetUp() {
        userBean = new User(
                "user2",
                "somessRand0",
                "user2@gmail.com",
                "Alex",
                "Wong");
        adminBean = new Admin(
                "admin1",
                "Supersafe22");
        consumableItemBean = new ConsumableItem(
                "Water",
                1.50,
                new Manufacturer("StarBucks", "United States"));
        decorItemBean = new DecorItem(
                "Sofa",
                899.99,
                new Manufacturer("Ikea", "Sweden"));
    }

    @Test
    public void TestGetBean_DefaultTypes_ShouldPass() {
        // NOTE: AppConfiguration property matchIfMissing = true
        Account defaultAccount = context.getBean(Account.class);
        assertEquals(defaultAccount.getClass(), User.class);    // default Account type is USER
        assertTrue(defaultAccount.getId() == null &&
                            defaultAccount.getUsername() == null &&
                            defaultAccount.getPassword() == null);

        Item defaultItem = context.getBean(Item.class);
        assertEquals(defaultItem.getClass(), ConsumableItem.class);  // NOTE: default Item type declared in application.properties
        assertTrue(defaultItem.getName() == null &&
                            defaultItem.getPrice() == null &&
                            defaultItem.getCategory().equals("Consumable"));
    }

    @Test
    public void TestGetBean_Accounts_ShouldPass() {
        // NOTE: Create and configure beans from XML File
        ApplicationContext appContext = new ClassPathXmlApplicationContext(ACCOUNTS_XMLFILEPATH);

        Account user = appContext.getBean("user", Account.class);   // NOTE: retrieve configured instance
        assertEquals(user.getClass(), User.class);
        assertEquals(userBean.getClass(), User.class);
        assertTrue(user.equals(userBean));

        Account admin = appContext.getBean("admin", Account.class); // NOTE: retrieve configured instance
        assertEquals(admin.getClass(), Admin.class);
        assertEquals(adminBean.getClass(), Admin.class);
        assertTrue(admin.equals(adminBean));
    }

    @Test
    public void TestGetBean_InvalidId_ShouldFail() {
        ApplicationContext appContext = new ClassPathXmlApplicationContext(ACCOUNTS_XMLFILEPATH);
        Exception exception = assertThrows(NoSuchBeanDefinitionException.class, () -> {
            Account badId = appContext.getBean("badId", Account.class); // NOTE: Invalid Bean Id
        });
        assertEquals(exception.getMessage(), "No bean named 'badId' available");
    }


    // TODO:
    //  User class default scope Singleton: only one instance created!
    //  if User class has scope prototype: more than one instance created!
    @Test
    public void TestGetBean_Singleton_ShouldPass() {
        ApplicationContext appContext = new ClassPathXmlApplicationContext(ACCOUNTS_XMLFILEPATH);

        Account user = appContext.getBean("user", Account.class);
        Account user2 = appContext.getBean("user", Account.class);

        log.info("user: " + user);
        log.info("user2: " + user2);
    }

    @Test
    public void TestGetBean_ExtraFields_ShouldFail() {
        ApplicationContext appContext = new ClassPathXmlApplicationContext(ACCOUNTS_XMLFILEPATH);

        Account badAdmin = appContext.getBean("badAdmin", Account.class);
        assertEquals(badAdmin.getClass(), Admin.class);
        assertEquals(adminBean.getClass(), Admin.class);
        assertFalse(badAdmin.equals(adminBean));    // NOTE: not equals cause badAdmin has extra property defined in XML file
    }

    @Test
    public void TestGetBean_AutowiredItems_ShouldPass() {
        ApplicationContext appContext = new ClassPathXmlApplicationContext(ITEMS_XMLFILEPATH);

        Item consumableItem = appContext.getBean("consumableItem", Item.class); // NOTE: Constructor Injection - items.xml file
        assertEquals(consumableItem.getClass(), ConsumableItem.class);
        assertEquals(consumableItemBean.getClass(), ConsumableItem.class);
        assertTrue(consumableItem.equals(consumableItemBean));
        assertEquals(((ConsumableItem) consumableItem).getManufacturer().toString(), "Manufacturer{name='StarBucks', country='United States'}");

        Item decorItem = appContext.getBean("decorItem", Item.class);
        assertEquals(decorItem.getClass(), DecorItem.class);
        assertEquals(decorItemBean.getClass(), DecorItem.class);
        assertTrue(decorItem.equals(decorItemBean));
        assertEquals(((DecorItem) decorItem).getManufacturer().toString(), "Manufacturer{name='Ikea', country='Sweden'}");

        Item decorItem2 = appContext.getBean("decorItem", Item.class);

        log.info("decorItem: " + decorItem);
        log.info("decorItem2: " + decorItem2);
    }

}
