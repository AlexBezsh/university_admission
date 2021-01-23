package com.bezshtanko.university_admission.controller;

import com.bezshtanko.university_admission.service.FacultyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    private FacultyService facultyService;

    @Autowired
    public PageController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    @GetMapping(value = "/faculties")
    public String getFaculties(Model model, @PageableDefault(sort = {"stateFundedPlaces"}, direction = Sort.Direction.DESC) Pageable pageable) {
        model.addAttribute("faculties", facultyService.getFaculties(pageable));
        return "faculties";
    }

}
