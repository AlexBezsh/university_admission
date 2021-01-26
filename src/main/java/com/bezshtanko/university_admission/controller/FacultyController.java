package com.bezshtanko.university_admission.controller;

import com.bezshtanko.university_admission.model.enrollment.Enrollment;
import com.bezshtanko.university_admission.model.enrollment.EnrollmentStatus;
import com.bezshtanko.university_admission.model.faculty.Faculty;
import com.bezshtanko.university_admission.model.faculty.FacultyStatus;
import com.bezshtanko.university_admission.model.mark.Mark;
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

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Controller
public class FacultyController {

    private final FacultyService facultyService;
    private final SubjectService subjectService;
    private final EnrollmentService enrollmentService;
    private final UserService userService;
    private final MarkService markService;

    @Autowired
    public FacultyController(FacultyService facultyService,
                             SubjectService subjectService,
                             EnrollmentService enrollmentService,
                             UserService userService,
                             MarkService markService) {
        this.facultyService = facultyService;
        this.subjectService = subjectService;
        this.enrollmentService = enrollmentService;
        this.userService = userService;
        this.markService = markService;
    }

    @GetMapping(value = "/faculties")
    public String getFaculties(Model model,
                               @PageableDefault(value = 3, sort = {"stateFundedPlaces"}, direction = Sort.Direction.DESC) Pageable pageable,
                               @AuthenticationPrincipal User user) {
        if (user.getStatus() == UserStatus.ENROLLED) {
            return "congratulation";
        }

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

    @GetMapping(value = "/faculty/{facultyId}")
    public String showFaculty(Model model, @PathVariable Long facultyId) {
        model.addAttribute("faculty", facultyService.getFacultyWithEnrollments(facultyId));
        return "faculty";
    }

    @GetMapping(value = "/faculty/{facultyId}/edit")
    public String editFaculty(Model model, @PathVariable Long facultyId) {
        model.addAttribute("faculty", facultyService.getFaculty(facultyId));
        return "edit_faculty_form";
    }

    @PostMapping(value = "/faculty/{facultyId}/edit")
    public String saveEditedFaculty(@PathVariable Long facultyId, @Valid @ModelAttribute("faculty") Faculty faculty, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "edit_faculty_form";
        }
        faculty.setId(facultyId);
        facultyService.updateFaculty(faculty);
        return "redirect:/faculties";
    }

    @GetMapping(value = "/faculty/{facultyId}/enroll")
    public String enrollForm(Model model, @PathVariable Long facultyId) {
        Enrollment enrollment = new Enrollment();
        Faculty faculty = facultyService.getFaculty(facultyId);
        enrollment.setFaculty(faculty);
        enrollment.setMarks(faculty.getSubjects().stream()
                .map(s -> new Mark(null, enrollment, s, new BigDecimal("0")))
                .collect(Collectors.toList()));
        model.addAttribute("enrollment", enrollment);
        return "enrollment_form";
    }

    @Transactional
    @PostMapping(value = "/faculty/{facultyId}/enroll")
    public String enroll(@ModelAttribute Enrollment enrollment, @AuthenticationPrincipal User user) {
        enrollment.setStatus(EnrollmentStatus.NEW);
        enrollment.setUser(userService.getUserByEmail(user.getEmail()));
        enrollmentService.saveNewEnrollment(enrollment);
        enrollment.getMarks().forEach(m -> m.setEnrollment(enrollment));
        markService.saveAll(enrollment.getMarks());
        return "redirect:/faculties";
    }

    @GetMapping(value = "/faculty/{facultyId}/finalize")
    public String createFinalList(Model model, @PathVariable Long facultyId) {
        Faculty faculty = facultyService.getFaculty(facultyId);
        if (faculty.getStatus() == FacultyStatus.CLOSED) {
            return "redirect:/faculty/" + facultyId;
        }

        List<Enrollment> enrollments = faculty.getEnrollments()
                .stream()
                .filter(e -> (e.getStatus() == EnrollmentStatus.APPROVED) && (e.getUser().getStatus() == UserStatus.ACTIVE))
                .sorted(Comparator.comparing(Enrollment::getMarksSum).reversed())
                .limit(faculty.getTotalPlaces())
                .collect(Collectors.toList());
        normalizeFinalList(faculty, enrollments);
        model.addAttribute("enrollments", enrollments);
        return "final_list";
    }

    @Transactional
    void normalizeFinalList(Faculty faculty, List<Enrollment> enrollments) {
        facultyService.setClosed(faculty);
        userService.setEnrolled(enrollments
                .stream()
                .map(Enrollment::getUser)
                .collect(Collectors.toList()));
        enrollmentService.setFinalized(enrollments);
    }

    @GetMapping(value = "/faculty/{facultyId}/delete")
    public String deleteFaculty(@PathVariable Long facultyId) {
        facultyService.deleteFaculty(facultyId);
        return "redirect:/faculties";
    }

}
