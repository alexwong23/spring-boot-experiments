package com.example.experiments.controller.api;

import com.example.experiments.model.Account.User;
import com.example.experiments.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/user/")
public class UserApiController {

    private final UserService userService;

    @Autowired
    public UserApiController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getUsers() {
        return userService.getUsers();
    }

    @GetMapping(path = "{userId}")
    public User getUser(@PathVariable("userId") Long userId) {
        return userService.getUser(userId);
    }

    // TODO: gotta revamp
//    @PostMapping
//    public void createNewUser(@RequestBody User user) {
//        userService.addUser(user);
//    }
//
//    @PutMapping(path = "{userId}")
//    public void updateUser(@PathVariable("userId") Long userId,
//                           @RequestParam(required = false) String name,
//                           @RequestParam(required = false) String email) {
//        userService.updateUser(userId, name, email);
//    }

    @DeleteMapping(path = "{userId}")
    public void deleteUser(@PathVariable("userId") Long userId) {
        userService.deleteUser(userId);
    }
}