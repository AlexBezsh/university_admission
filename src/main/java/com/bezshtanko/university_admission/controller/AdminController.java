package com.bezshtanko.university_admission.controller;

import com.bezshtanko.university_admission.model.faculty.Faculty;
import com.bezshtanko.university_admission.model.faculty.FacultyStatus;
import com.bezshtanko.university_admission.service.FacultyService;
import com.bezshtanko.university_admission.service.SubjectService;
import com.bezshtanko.university_admission.transfer.SubjectsDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Slf4j
@Controller
public class AdminController {

    private final FacultyService facultyService;
    private final SubjectService subjectService;

    @Autowired
    public AdminController(FacultyService facultyService, SubjectService subjectService) {
        this.facultyService = facultyService;
        this.subjectService = subjectService;
    }

    @GetMapping(value = "/faculty/{facultyName}")
    public String getFaculty(Model model, @PageableDefault(sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        //model.addAttribute("users", userService.getUsers(pageable));
        return "users";
    }

    @GetMapping(value = "/faculty/new")
    public String createFacultyForm(Model model) {
        Faculty faculty = new Faculty();
        faculty.setSubjects(subjectService.getAll());
        model.addAttribute("faculty", faculty);
        return "create_faculty_form";
    }

    @PostMapping(value = "/faculty/new")
    public String createFaculty(@Valid @ModelAttribute("faculty") Faculty faculty, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "create_faculty_form";
        }
        log.info("{}", faculty);
        //checkFaculty(faculty);
        faculty.setStatus(FacultyStatus.ACTIVE);
        facultyService.saveNewFaculty(faculty);
        return "redirect:/faculties";
    }

    private void checkFaculty(Faculty faculty) {

    }

}
