package com.bezshtanko.university_admission.controller;

import com.bezshtanko.university_admission.exception.AuthenticationException;
import com.bezshtanko.university_admission.exception.UserNotExistException;
import com.bezshtanko.university_admission.model.user.UserRole;
import com.bezshtanko.university_admission.model.user.UserStatus;
import com.bezshtanko.university_admission.model.user.User;
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

    private static final String REDIRECT_TO_FACULTIES = "redirect:/faculties";

    private final UserService userService;

    @Autowired
    public AuthenticationController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value="/")
    public String home() {
        log.info("Loading home page");

        if (isAuthenticated()) {
            log.error("Access denied. User is authenticated");
            return REDIRECT_TO_FACULTIES;
        }

        return "home";
    }

    @GetMapping(value="/login")
    public String loginPage(Model model) {
        log.info("Loading login page");

        if (isAuthenticated()) {
            log.error("Access denied. User is authenticated");
            return REDIRECT_TO_FACULTIES;
        }

        model.addAttribute("user", new User());
        return "login";
    }

    @PostMapping(value = "/login")
    public String login(@ModelAttribute("user") User user) {
        log.info("User with email '{}' is logging in", user.getEmail());

        try {
            userService.login(user);
        } catch (AuthenticationException e) {
            log.error("Access denied. Bad credentials");
            return "redirect:/login?error";
        }

        return REDIRECT_TO_FACULTIES;
    }

    @GetMapping("/register")
    public String regForm(Model model){
        log.info("Loading registration form");

        if (isAuthenticated()) {
            log.error("Access denied. User is authenticated");
            return REDIRECT_TO_FACULTIES;
        }

        model.addAttribute("user", new User());
        return "reg_form";
    }

    @PostMapping(value = "/register")
    public String register(@Valid @ModelAttribute("user") User user, BindingResult bindingResult) {
        log.info("New user registration started");

        if (bindingResult.hasErrors()) {
            log.error("Registration canceled. User entered invalid data.");
            return "reg_form";
        }

        try {
            userService.findByEmail(user.getEmail());
            return "redirect:/login?userAlreadyExist";
        } catch (UserNotExistException ignored) {
            //if user not exist, registration allowed
        }

        user.setRoles(new HashSet<>());
        user.getRoles().add(UserRole.ENTRANT);
        user.setStatus(UserStatus.ACTIVE);
        userService.save(user);
        log.info("New user has been successfully saved");

        return "redirect:/login?registrationSuccess";
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
