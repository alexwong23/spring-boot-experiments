package com.example.experiments.service;

import com.example.experiments.error.exceptions.UserNotFoundException;
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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

public class UserServiceTest {

    @Mock // NOTE: Creates mock object to be injected
    private UserRepository userRepository;
    @InjectMocks // NOTE: Create and inject the mock object (@Mock) into it
    private UserService userService;

    private List<User> users = new ArrayList<>();
    private User userOne, userTwo, userThree, userFour;
    private static Logger log = LoggerFactory.getLogger(UserServiceTest.class);

    @BeforeEach
    public void Initialise() {
        // NOTE: tells Mockito to initialise @Mock fields in this test class instance
        MockitoAnnotations.openMocks(this);
        userOne = new User(
                "MoSalahhh",
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
    public void TestGetAllUsers_ShouldPass() {
        // NOTE: add behaviour of findAll()
        Mockito.when(userRepository.findAll()).thenReturn(users);

        List<User> userList = userService.findAllUsers();
        assertTrue(userList.equals(users));

        // NOTE: verify call to service was made AND with same arguments
        Mockito.verify(userRepository).findAll();
    }

    @Test
    public void TestGetUserById_ShouldPass() {
        Long userId = 3L;
        Mockito.when(userRepository.findById(userId)).thenReturn(java.util.Optional.of(userThree));

        User found = userService.findUserById(userId);
        assertTrue(found.equals(userThree));

        Mockito.verify(userRepository).findById(userId);
    }

    @Test
    public void TestGetUserById_ShouldFail() {
        Long userId = 4L;
        Mockito.when(userRepository.findById(userId)).thenThrow(new UserNotFoundException(userId));

        Exception exception = assertThrows(UserNotFoundException.class, () -> {
            userService.findUserById(userId);
        });
        assertEquals(exception.getMessage(), "User id not found: " + userId);

        Mockito.verify(userRepository).findById(userId);
    }

    @Test
    public void TestAddOneUser_ShouldPass() {
        Mockito.when(userRepository.findByEmail(userFour.getEmail())).thenReturn(java.util.Optional.empty());
        Mockito.when(userRepository.save(any(User.class))).thenReturn(userFour);

        User newUser = userService.addOneUser(userFour);
        assertTrue(newUser.equals(userFour));

        Mockito.verify(userRepository).findByEmail(userFour.getEmail());
        Mockito.verify(userRepository).save(any(User.class));
    }

    @Test
    public void TestAddOneUser_ShouldFail() {
        Mockito.when(userRepository.findByEmail(userOne.getEmail())).thenThrow(new IllegalStateException("Email has been taken"));

        Exception exception = assertThrows(IllegalStateException.class, () -> {
            userService.addOneUser(userOne);
        });
        assertEquals(exception.getMessage(), "Email has been taken");

        Mockito.verify(userRepository).findByEmail(userOne.getEmail());
    }

    @Test
    public void TestUpdateUserById_ShouldPass() {
        User newDetails = new User(
                "MoSalahhh",
                "PASSW_RD",
                "jamesbond@gmail.com",
                "",
                "Bond",
                LocalDate.of(2000, Month.JANUARY, 5));
        Mockito.when(userRepository.findById(userOne.getId())).thenReturn(java.util.Optional.of(userOne));
        Mockito.when(userRepository.findByEmail(newDetails.getEmail())).thenReturn(java.util.Optional.empty());
        userOne.setEmail(newDetails.getEmail());
        userOne.setLastName(newDetails.getLastName());
        Mockito.when(userRepository.save(any(User.class))).thenReturn(userOne);

        userService.updateUserById(userOne.getId(), newDetails);
        User found = userService.findUserById(userOne.getId());
        assertTrue(found.equals(userOne));

        // NOTE: Mock findById() twice
        Mockito.verify(userRepository, Mockito.times(2)).findById(userOne.getId());
        Mockito.verify(userRepository).findByEmail(newDetails.getEmail());
        Mockito.verify(userRepository).save(any(User.class));
    }

    // TODO: TestUpdateUserById Fail - complete form validation first

    @Test
    public void TestDeleteUserById_ShouldPass() {
        Mockito.when(userRepository.existsById(userOne.getId())).thenReturn(true);
        Mockito.doNothing().when(userRepository).deleteById(userOne.getId());

        userService.deleteUserById(userOne.getId());

        Mockito.verify(userRepository).existsById(userOne.getId());
        Mockito.verify(userRepository).deleteById(userOne.getId());
    }

    @Test
    public void TestDeleteUserById_ShouldFail() {
        Mockito.when(userRepository.existsById(userFour.getId())).thenReturn(false);

        Exception exception = assertThrows(UserNotFoundException.class, () -> {
            userService.deleteUserById(userFour.getId());
        });
        assertEquals(exception.getMessage(), "User id not found: " + userFour.getId());

        Mockito.verify(userRepository).existsById(userFour.getId());
    }
}
