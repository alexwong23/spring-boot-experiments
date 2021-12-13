package com.example.experiments.service;

import com.example.experiments.error.exceptions.UserNotFoundException;
import com.example.experiments.model.Account.User;
import com.example.experiments.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.List;

public interface UserService {

    // find all users
    public List<User> findAllUsers();

    // find one user by id
    public User findUserById(Long userId);

    // add new user
    public User addOneUser(User user);

    // update existing user
    @Transactional
    public void updateUserById(Long userId, User newUserDetails);

    // delete existing user
    public void deleteUserById(Long userId);
}
