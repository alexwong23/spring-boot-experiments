package com.example.experiments.controller;

import com.example.experiments.model.Account.User;
import com.example.experiments.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
        model.addAttribute("users", userService.findAllUsers());
        return "user";
    }

    @GetMapping("/new")
    public String newUser(Model model) {
        // User argument so template can associate form attributes
        model.addAttribute("newUser", new User());
        return "user-new";
    }

    @GetMapping(path = "{userId}")
    public String getUser(@PathVariable("userId") Long userId, Model model) {
        model.addAttribute("updateUser", userService.findUserById(userId));
        return "user-edit";
    }

    @PostMapping
    public String createNewUser(@Valid @ModelAttribute(value="newUser") User newUser, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) return "user-new";
        newUser.setDob(LocalDate.now()); // TODO: DOB value should be provided in form
        userService.addOneUser(newUser);
        return "redirect:/user";
    }

    @PutMapping(path = "{userId}")
    public String updateUser(@PathVariable("userId") Long userId,
                             @Valid @ModelAttribute(value="updateUser") User updateUser,
                             BindingResult bindingResult) {
        if(bindingResult.hasErrors()) return "user-edit";
        updateUser.setDob(LocalDate.now()); // TODO: DOB value should be provided in form
        userService.updateUserById(userId, updateUser);
        return "redirect:/user/" + userId;
    }

    @DeleteMapping(path = "{userId}")
    public String deleteUser(@PathVariable("userId") Long userId) {
        userService.deleteUserById(userId);
        return "redirect:/user";
    }
}
