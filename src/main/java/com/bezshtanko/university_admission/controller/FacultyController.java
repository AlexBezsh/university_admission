package com.bezshtanko.university_admission.controller;

import com.bezshtanko.university_admission.model.faculty.Faculty;
import com.bezshtanko.university_admission.model.faculty.FacultyStatus;
import com.bezshtanko.university_admission.service.FacultyService;
import com.bezshtanko.university_admission.service.SubjectService;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Slf4j
@Controller
public class FacultyController {

    private final FacultyService facultyService;
    private final SubjectService subjectService;

    @Autowired
    public FacultyController(FacultyService facultyService, SubjectService subjectService) {
        this.facultyService = facultyService;
        this.subjectService = subjectService;
    }

    @GetMapping(value = "/faculties")
    public String getFaculties(Model model, @PageableDefault(sort = {"stateFundedPlaces"}, direction = Sort.Direction.DESC) Pageable pageable) {
        model.addAttribute("faculties", facultyService.getFaculties(pageable));
        return "faculties";
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
        faculty.setStatus(FacultyStatus.ACTIVE);
        facultyService.saveNewFaculty(faculty);
        return "redirect:/faculties";
    }

    @GetMapping(value="/faculty/{facultyId}")
    public String showFaculty(@PathVariable Long facultyId) {
        //TODO
        return "faculty";
    }

    @GetMapping(value ="/faculty/{facultyId}/edit")
    public String editFaculty(@PathVariable Long facultyId) {
        //TODO
        return "edit_faculty_form";
    }

    @GetMapping(value ="/faculty/{facultyId}/enroll")
    public String enroll(@PathVariable Long facultyId) {
        //TODO
        return "enrollment_form";
    }

    @GetMapping(value="/faculty/{facultyId}/delete")
    public String deleteFaculty(@PathVariable Long id) {
        facultyService.deleteFaculty(id);
        return "redirect:/faculties";
    }



}
