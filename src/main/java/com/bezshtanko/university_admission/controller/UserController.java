package com.bezshtanko.university_admission.controller;

import com.bezshtanko.university_admission.model.user.User;
import com.bezshtanko.university_admission.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/profile")
    public String profile(Model model, @AuthenticationPrincipal User user) {
        model.addAttribute("user", userService.getUserByEmail(user.getEmail()));
        return "profile";
    }

    @GetMapping(value = "/user/{userId}")
    public String showProfile(Model model, @PathVariable Long userId) {
        model.addAttribute("user", userService.getUserById(userId));
        return "profile";
    }

    @GetMapping(value = "/user/{userId}/block")
    public String blockUser(@PathVariable Long userId) {
        userService.blockUser(userId);
        return "redirect:/faculties";
    }
    
}
