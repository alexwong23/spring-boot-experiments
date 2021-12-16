package com.example.experiments.controller;

import com.example.experiments.application.Application;
import com.example.experiments.error.exceptions.UserNotFoundException;
import com.example.experiments.model.Account.User;
import com.example.experiments.repository.UserRepository;
import com.example.experiments.service.UserService;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.hamcrest.beans.HasPropertyWithValue.hasProperty;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest(classes={Application.class})
@AutoConfigureMockMvc
//@ExtendWith(SpringExtension.class) // extends behaviour of test classes/methods - integrates Spring TestContext Framework into JUnit5's Jupiter programming model
//@WebMvcTest(UserController.class) // scans only the UserController rather than entire application - have to provide mock of its dependencies
//@ComponentScan(basePackages = "com.example.experiments.controller.api") // tells Spring to manage annotated components in scanned packages
public class UserControllerTest {

    // NOTE: MockMVC Integration testing guide -> https://howtodoinjava.com/spring-boot2/testing/spring-boot-mockmvc-example/

    @Autowired          // injects with repositories wired - don't need WebApplicationContext
    private MockMvc mvc; // main entry point for server-side Spring MVC testing

    @MockBean       // NOTE: add/replace mock objects in Spring Application Context
    private UserRepository userRepository;
    @MockBean
    private UserService userService;
    @InjectMocks    // NOTE: Create and inject the mock object (@Mock) into it
    private UserController userController;

    private List<User> users = new ArrayList<>();
    private User userOne, userTwo, userThree, userFour;
    private static Logger log = LoggerFactory.getLogger(UserControllerTest.class);

    private final String USERCONTROLLER_PATH = "/user";
    private final String USERAPICONTROLLER_PATH = "/api/v1/user";

    @BeforeEach
    public void Setup() {
        userOne = new User("MoSalahhh",
                "PASSW0RD2s",
                "jamessoh@gmail.com",
                "James",
                "Soh",
                LocalDate.of(2000, Month.JANUARY, 5));
        userTwo = new User(
                "julius99",
                "gloryOfRome3",
                "juliuscaesar@gmail.com",
                "Julius",
                "Caesar",
                LocalDate.of(1994, Month.SEPTEMBER, 15));
        userThree = new User(
                "alexwong23",
                "someRando3",
                "alexwong23@gmail.com",
                "Alex",
                "Wong",
                null);
        userFour = new User(
                "newUser",
                "passwordheR3",
                "newuser@gmail.com",
                "New",
                "User",
                LocalDate.of(2000, Month.JANUARY, 1));
        users = new ArrayList<>(List.of(userOne, userTwo, userThree));
    }

    @Test
    public void TestUserController_FindAllUsers_ShouldPass() throws Exception {
        Mockito.when(userService.findAllUsers()).thenReturn(users);
        this.mvc.perform(get(USERCONTROLLER_PATH))
                .andExpect(model().attribute("users", users))
                .andExpect(status().isOk());
        Mockito.verify(userService).findAllUsers();
    }

    @Test
    public void TestUserController_FindUserById_ShouldPass() throws Exception {
        Long userId = 2L;
        Mockito.when(userService.findUserById(userId)).thenReturn(userTwo);
        this.mvc.perform(get(USERCONTROLLER_PATH + "/" + userId))
                .andExpect(model().attribute("updateUser", userTwo))
                .andExpect(status().isOk());
        Mockito.verify(userService).findUserById(userId);
    }

    @Test
    public void TestUserController_CreateNewUser_ShouldPass() throws Exception {
        Long userId = 4L;
        Mockito.when(userService.addOneUser(any(User.class))).thenReturn(userFour);
        userFour.setId(userId); // manually set userId

        this.mvc.perform(post(USERCONTROLLER_PATH)
                        .param("username", userFour.getUsername())
                        .param("password", userFour.getPassword())
                        .param("email", userFour.getEmail())
                        .param("firstName", userFour.getFirstName())
                        .param("lastName", userFour.getLastName())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(USERCONTROLLER_PATH + "/" + userFour.getId()))
                .andDo(print());

        assertTrue(userFour.getId() == userId);
        Mockito.verify(userService).addOneUser(any(User.class));
    }

    @Test
    public void TestUserController_UpdateUser_ShouldPass() throws Exception {
        Long userId = 2L;
        Mockito.when(userService.updateUserById(userId, userTwo)).thenReturn(userTwo);  // returns userTwo with id NULL

        this.mvc.perform(put(USERCONTROLLER_PATH + "/" + userId)
                        .param("username", userTwo.getUsername())
                        .param("password", userTwo.getPassword())
                        .param("email", userTwo.getEmail())
                        .param("firstName", userTwo.getFirstName())
                        .param("lastName", userTwo.getLastName())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(USERCONTROLLER_PATH + "/" + userId));

        assertTrue(userTwo.getId() == null);
        Mockito.verify(userService).updateUserById(userId, userTwo);
    }

    @Test
    public void TestUserController_DeleteUser_ShouldPass() throws Exception {
        Long userId = 2L;
        Mockito.doNothing().when(userService).deleteUserById(userId); // doNothing cause method returns void

        this.mvc.perform(delete(USERCONTROLLER_PATH + "/" + userId)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(USERCONTROLLER_PATH));

        Mockito.verify(userService).deleteUserById(userId);
    }

    @Test
    public void TestUserController_DeleteUser_ShouldFail() throws Exception {
        Long userId = 4L;
        doThrow(new UserNotFoundException(userId)).when(userService).deleteUserById(userId);

        this.mvc.perform(delete(USERCONTROLLER_PATH + "/" + userId)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(view().name("error"))
                .andExpect(model().attribute("errors", hasProperty("status", Matchers.equalTo(404))))
                .andExpect(model().attribute("errors",
                        hasProperty("message", Matchers.equalTo("User id not found: " + userId))));

        Mockito.verify(userService).deleteUserById(userId);
    }

    @Test
    public void TestUserApiController_CreateNewUser_ShouldPass() throws Exception {
        Mockito.when(userService.addOneUser(userFour)).thenReturn(userFour);

        this.mvc.perform(post(USERAPICONTROLLER_PATH)
                    .param("username", userFour.getUsername())
                    .param("password", userFour.getPassword())
                    .param("email", userFour.getEmail())
                    .param("firstName", userFour.getFirstName())
                    .param("lastName", userFour.getLastName())
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value(userFour.getUsername()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.password").value(userFour.getPassword()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(userFour.getEmail()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value(userFour.getFirstName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value(userFour.getLastName()));

        Mockito.verify(userService).addOneUser(userFour);
    }

    @Test
    public void TestUserApiController_CreateNewUser_ShouldFail() throws Exception {
        Mockito.when(userService.addOneUser(userFour)).thenThrow(new IllegalStateException("Email has been taken"));

        this.mvc.perform(post(USERAPICONTROLLER_PATH)
                        .param("username", userFour.getUsername())
                        .param("password", userFour.getPassword())
                        .param("email", userFour.getEmail())
                        .param("firstName", userFour.getFirstName())
                        .param("lastName", userFour.getLastName())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isInternalServerError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Email has been taken"));

        Mockito.verify(userService).addOneUser(userFour);
    }

    @Test
    public void TestUserApiController_UpdateUser_ShouldPass() throws Exception {
        Long userId = 2L;
        Mockito.when(userService.updateUserById(userId, userTwo)).thenReturn(userTwo);  // returns userTwo with id NULL

        this.mvc.perform(put(USERAPICONTROLLER_PATH + "/" + userId)
                        .param("username", userTwo.getUsername())
                        .param("password", userTwo.getPassword())
                        .param("email", userTwo.getEmail())
                        .param("firstName", userTwo.getFirstName())
                        .param("lastName", userTwo.getLastName())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value(userTwo.getUsername()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.password").value(userTwo.getPassword()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(userTwo.getEmail()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value(userTwo.getFirstName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value(userTwo.getLastName()));

        assertTrue(userTwo.getId() == null);
        Mockito.verify(userService).updateUserById(userId, userTwo);
    }

    @Test
    public void TestUserApiController_DeleteUser_ShouldPass() throws Exception {
        Long userId = 2L;
        Mockito.doNothing().when(userService).deleteUserById(userId);

        this.mvc.perform(delete(USERAPICONTROLLER_PATH + "/" + userId)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isAccepted());

        Mockito.verify(userService).deleteUserById(userId);
    }

}
