package com.example.experiments.service;

import com.example.experiments.model.Account.Admin;
import com.example.experiments.model.Account.User;
import com.example.experiments.repository.UserRepository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserServiceTest {

    @Mock // Creates mock implementation of class
    private UserRepository userRepository;
    @InjectMocks // Like @Mock, but additionally also injects dependent mocks (@Mock) into it
    private UserService userService;

    private User userOne, userTwo, userThree;
    private static Logger log = LoggerFactory.getLogger(UserServiceTest.class);

    @BeforeEach
    public void Initialise() {
        MockitoAnnotations.openMocks(this);
        List<User> list = new ArrayList<User>();
        userOne = new User(
                "MoSalahhh",
                "PASSW_RD",
                "jamessoh@gmail.com",
                "James",
                "Soh",
                LocalDate.of(2000, Month.JANUARY, 5));
        userTwo = new User(
                "julius99",
                "gloryOfRome",
                "juliuscaesar@gmail.com",
                "Julius",
                "Caesar",
                LocalDate.of(1994, Month.SEPTEMBER, 15));
        userThree = new User(
                "alexwong23",
                "someRando",
                "alexwong23@gmail.com",
                "Alex",
                "Wong",
                null);
    }

    @AfterEach
    public void flush() {
        // TODO: why do we need to flush?
        userRepository.flush();
    }

    @Test
    public void TestGetAllUsers_ShouldPass() {
        List<User> users = new ArrayList<>(List.of(userOne, userTwo, userThree));
        Mockito.when(userRepository.findAll()).thenReturn(users);
        List<User> userList = userService.findAllUsers();
        assertTrue(userList.equals(List.of(userOne, userTwo, userThree)));
        Mockito.verify(userRepository).findAll();
    }

    @Test
    public void TestGetUserById_ShouldPass() {
//        assertTrue(userList.equals(List.of(userOne, userTwo, userThree)));
        Mockito.when(userRepository.findById(1L)).thenReturn(java.util.Optional.of(new User(
                "alexwong23",
                "someRando",
                "alexwong23@gmail.com",
                "Alex",
                "Wong",
                null)));
        log.info("Repository users: " + userRepository.findAll());
        log.info("Service users: " + userService.findAllUsers());
        User found = userService.findUserById(1L);
        assertTrue(found.equals(userThree));
    }
}
