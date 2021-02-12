package com.bezshtanko.university_admission.controller;

import com.bezshtanko.university_admission.model.faculty.Faculty;
import com.bezshtanko.university_admission.model.faculty.FacultyStatus;
import com.bezshtanko.university_admission.model.user.User;
import com.bezshtanko.university_admission.model.user.UserStatus;
import com.bezshtanko.university_admission.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    private final EnrollmentService enrollmentService;

    @Autowired
    public FacultyController(FacultyService facultyService,
                             SubjectService subjectService,
                             EnrollmentService enrollmentService) {
        this.facultyService = facultyService;
        this.subjectService = subjectService;
        this.enrollmentService = enrollmentService;
    }

    @GetMapping(value = "/faculties")
    public String getFaculties(Model model,
                               @PageableDefault(value = 3, sort = {"stateFundedPlaces"}, direction = Sort.Direction.DESC) Pageable pageable,
                               @AuthenticationPrincipal User user) {
        log.info("Loading faculties page for user with email '{}'", user.getEmail());

        if (user.getStatus() == UserStatus.ENROLLED_CONTRACT || user.getStatus() == UserStatus.ENROLLED_STATE_FUNDED) {
            log.info("User is enrolled. Redirecting to congratulation page");
            return "redirect:/congratulation";
        }

        model.addAttribute("faculties", facultyService.findAllForUser(pageable, user));
        return "faculties";
    }

    @GetMapping(value = "/faculty/new")
    public String createFacultyForm(Model model) {
        log.info("Loading new faculty form");

        Faculty faculty = new Faculty();
        faculty.setSubjects(subjectService.getAll());
        model.addAttribute("faculty", faculty);

        return "admin/create_faculty_form";
    }

    @PostMapping(value = "/faculty/new")
    public String createFaculty(@Valid @ModelAttribute("faculty") Faculty faculty,
                                BindingResult bindingResult,
                                Model model) {
        log.info("Saving new faculty: {}", faculty);

        if (bindingResult.hasErrors()) {
            log.error("Faculty has invalid data");
            faculty.setSubjects(subjectService.getAll());
            model.addAttribute("faculty", faculty);
            return "admin/create_faculty_form";
        }

        faculty.setStatus(FacultyStatus.ACTIVE);
        facultyService.save(faculty);
        return "redirect:/faculties";
    }

    @GetMapping(value = "/faculty/{facultyId}")
    public String showFaculty(Model model,
                              @PathVariable Long facultyId,
                              @AuthenticationPrincipal User user) {
        log.info("Loading faculty page for faculty with id '{}'", facultyId);

        model.addAttribute("faculty", facultyService.getWithEnrollmentsForUser(facultyId, user));
        return "faculty";
    }

    @GetMapping(value = "/faculty/{facultyId}/edit")
    public String editFaculty(Model model, @PathVariable Long facultyId) {
        log.info("Loading faculty editing form");

        model.addAttribute("faculty", facultyService.findById(facultyId));
        return "admin/edit_faculty_form";
    }

    @PostMapping(value = "/faculty/{facultyId}/edit")
    public String saveEditedFaculty(@PathVariable Long facultyId,
                                    @Valid @ModelAttribute("faculty") Faculty faculty,
                                    BindingResult bindingResult) {
        log.info("Saving updated faculty: {}", faculty);

        if (bindingResult.hasErrors()) {
            log.error("Updated faculty contains invalid data");
            return "admin/edit_faculty_form";
        }

        faculty.setId(facultyId);
        facultyService.update(faculty);
        return "redirect:/faculties";
    }

    @GetMapping(value = "faculty/{facultyId}/enrollment/{enrollmentId}/approve")
    public String approveEnrollment(@PathVariable Long facultyId, @PathVariable Long enrollmentId) {
        log.info("Setting 'approved' status to enrollment with id '{}'", enrollmentId);

        enrollmentService.setApproved(enrollmentId);
        return "redirect:/faculty/" + facultyId;
    }

    @GetMapping(value = "/faculty/{facultyId}/finalize")
    public String createFinalList(Model model, @PathVariable Long facultyId) {
        log.info("Finalization of faculty with id '{}' started", facultyId);

        facultyService.finalizeFaculty(facultyId);
        return "redirect:/faculty/" + facultyId;
    }

    @GetMapping(value = "/faculty/{facultyId}/delete")
    public String deleteFaculty(@PathVariable Long facultyId) {
        log.info("Deleting faculty with id '{}'", facultyId);

        facultyService.deleteById(facultyId);
        return "redirect:/faculties";
    }

}
