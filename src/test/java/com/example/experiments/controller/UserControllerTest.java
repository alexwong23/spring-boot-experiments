package com.example.experiments.controller;

import com.example.experiments.model.Account.User;
import com.example.experiments.repository.UserRepository;
import com.example.experiments.service.UserService;
import com.example.experiments.service.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.IOException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
public class UserControllerTest extends AbstractTest {

    @Autowired
    private UserService userService;
    @InjectMocks
    private UserController userController;
    private static Logger log = LoggerFactory.getLogger(UserControllerTest.class);

    @BeforeEach
    public void Initialise() {
        // NOTE: tells Mockito to initialise @Mock fields in this test class instance
        MockitoAnnotations.openMocks(this);
        super.SetUp();
        log.info("mvc " + String.valueOf(mvc));
    }

    @Test
    public void getAllUsers() throws Exception {
        String uri = "/user";
        log.info("mvc " + String.valueOf(mvc));
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        User[] userList = super.mapFromJson(content, User[].class);
        assertTrue(userList.length > 0);
    }
}
