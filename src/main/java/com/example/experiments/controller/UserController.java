package com.example.experiments.controller;

import com.example.experiments.model.Account.User;
import com.example.experiments.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;

// RestController - handles HTTP Requests
@Controller
@RequestMapping(path = "user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String getAllUsers(Model model) {
        model.addAttribute("users", userService.getUsers());
        return "user";
    }

    @GetMapping("/new")
    public String newUser() {
        return "user-new";
    }

    @GetMapping(path = "{userId}")
    public String getUser(@PathVariable("userId") Long userId, Model model) {
        model.addAttribute("user", userService.getUser(userId));
        return "user-edit";
    }

    @PostMapping
    public String createNewUser(
            @RequestParam(required = true) String username,
            @RequestParam(required = true) String password,
            @RequestParam(required = true) String email,
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName) {
        User newUser = new User(username, password, email, firstName, lastName, LocalDate.now());
        long userId = userService.addUser(newUser);
        return "redirect:/user/" + String.valueOf(userId);
    }

    @PutMapping(path = "{userId}")
    public String updateUser(@PathVariable("userId") Long userId,
                           @RequestParam(required = true) String username,
                           @RequestParam(required = true) String password,
                           @RequestParam(required = true) String email,
                           @RequestParam(required = false) String firstName,
                           @RequestParam(required = false) String lastName) {
        User newUserDetails = new User(username, password, email, firstName, lastName, LocalDate.now());
        userService.updateUser(userId, newUserDetails);
        return "redirect:/user/" + userId;
    }

    @DeleteMapping(path = "{userId}")
    public String deleteUser(@PathVariable("userId") Long userId) {
        userService.deleteUser(userId);
        return "redirect:/user";
    }
}
