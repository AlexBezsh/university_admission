package com.bezshtanko.university_admission.controller;

import com.bezshtanko.university_admission.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping(value = "/admin")
public class AdminController {

    private final UserService userService;

    @Autowired
    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/users") //TODO delete there is no need for admin to obtain list of all existing users
    public String getUsers(Model model, @PageableDefault(sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        log.info("getting all users");
        model.addAttribute("users", userService.getUsers(pageable));
        log.info("page with users was created");
        return "users";
    }

    @GetMapping(value = "/faculty/{facultyName}")
    public String getFaculty(Model model, @PageableDefault(sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        model.addAttribute("users", userService.getUsers(pageable));
        return "users";
    }

}
