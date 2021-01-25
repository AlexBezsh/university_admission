package com.bezshtanko.university_admission.controller;

import com.bezshtanko.university_admission.model.user.UserRole;
import com.bezshtanko.university_admission.model.user.UserStatus;
import com.bezshtanko.university_admission.model.user.User;
import com.bezshtanko.university_admission.exception.DBException;
import com.bezshtanko.university_admission.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;

@Slf4j
@Controller
public class AuthenticationController {

    private final UserService userService;

    @Autowired
    public AuthenticationController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value="/")
    public String home() {
        return "home";
    }

    @GetMapping(value="/login")
    public String loginPage(Model model) {
        if (isAuthenticated()) {
            return "redirect:/";
        }
        model.addAttribute("user", new User());
        return "login";
    }

    @PostMapping(value = "/login")
    public String login(@ModelAttribute("user") User user) {
        try {
            userService.login(user);
        } catch (DBException e) {
            return "redirect:/login?error";
        }
        return "redirect:/faculties";
    }

    @GetMapping("/register")
    public String regForm(Model model){
        if (isAuthenticated()) {
            return "redirect:/";
        }
        model.addAttribute("user", new User());
        return "reg_form";
    }

    @PostMapping(value = "/register")
    public String register(@Valid @ModelAttribute("user") User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "reg_form";
        }
        user.setRoles(new HashSet<>());
        user.getRoles().add(UserRole.ENTRANT);
        user.setStatus(UserStatus.ACTIVE);
        userService.saveNewUser(user);
        return "redirect:/login";
    }

    private boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || AnonymousAuthenticationToken.class.
                isAssignableFrom(authentication.getClass())) {
            return false;
        }
        return authentication.isAuthenticated();
    }
}
