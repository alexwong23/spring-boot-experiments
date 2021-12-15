package com.example.experiments.model.Account;

import com.example.experiments.SpringBootApplication;
import com.example.experiments.model.Item.ConsumableItem;
import com.example.experiments.model.Item.DecorItem;
import com.example.experiments.model.Item.Item;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = SpringBootApplication.class)
public class ApplicationBeanTest {

    private static ConfigurableApplicationContext context;

    // TODO: what happens if i mock this?
    private Account userBean, adminBean;
    private Item consumableItemBean, decorItemBean;

    private static Logger log = LoggerFactory.getLogger(ApplicationBeanTest.class);
    private final String ACCOUNTS_XMLFILEPATH = "file:src/main/resources/beans/accounts.xml";
    private final String ITEMS_XMLFILEPATH = "file:src/main/resources/beans/items.xml";

    @BeforeAll
    public static void Initialisation() {
//        SpringBootApplication.main(new String[] { });
        context = SpringApplication.run(SpringBootApplication.class, new String[] { });
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
                1.50);
        decorItemBean = new DecorItem(
                "Sofa",
                899.99);
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
        assertEquals(defaultItem.getClass(), DecorItem.class);  // default Item type is DECOR
        assertTrue(defaultItem.getName() == null &&
                            defaultItem.getPrice() == null &&
                            defaultItem.getCategory().equals("Decor"));
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

    @Test
    public void TestGetBean_ExtraFields_ShouldFail() {
        ApplicationContext appContext = new ClassPathXmlApplicationContext(ACCOUNTS_XMLFILEPATH);

        Account badAdmin = appContext.getBean("badAdmin", Account.class);
        assertEquals(badAdmin.getClass(), Admin.class);
        assertEquals(adminBean.getClass(), Admin.class);
        assertFalse(badAdmin.equals(adminBean));    // NOTE: not equals cause badAdmin has extra property defined in XML file
    }

    @Test
    public void TestGetBean_Items_ShouldPass() {
        ApplicationContext appContext = new ClassPathXmlApplicationContext(ITEMS_XMLFILEPATH);

        Item consumableItem = appContext.getBean("consumableItem", Item.class);
        assertEquals(consumableItem.getClass(), ConsumableItem.class);
        assertEquals(consumableItemBean.getClass(), ConsumableItem.class);
        assertTrue(consumableItem.equals(consumableItemBean));

        Item decorItem = appContext.getBean("decorItem", Item.class);
        assertEquals(decorItem.getClass(), DecorItem.class);
        assertEquals(decorItemBean.getClass(), DecorItem.class);
        assertTrue(decorItem.equals(decorItemBean));
    }

}
