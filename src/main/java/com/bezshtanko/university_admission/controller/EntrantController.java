package com.bezshtanko.university_admission.controller;

import com.bezshtanko.university_admission.model.enrollment.Enrollment;
import com.bezshtanko.university_admission.model.faculty.Faculty;
import com.bezshtanko.university_admission.model.mark.Mark;
import com.bezshtanko.university_admission.model.user.User;
import com.bezshtanko.university_admission.service.EnrollmentService;
import com.bezshtanko.university_admission.service.FacultyService;
import com.bezshtanko.university_admission.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.math.BigDecimal;
import java.util.stream.Collectors;

@Slf4j
@Controller
public class EntrantController {

    private final UserService userService;
    private final FacultyService facultyService;
    private final EnrollmentService enrollmentService;

    @Autowired
    public EntrantController(UserService userService, FacultyService facultyService, EnrollmentService enrollmentService) {
        this.userService = userService;
        this.facultyService = facultyService;
        this.enrollmentService = enrollmentService;
    }

    @GetMapping(value = "/profile")
    public String profile(Model model, @AuthenticationPrincipal User user) {
        log.info("Loading profile page for user with email '{}'", user.getEmail());

        model.addAttribute("user", userService.findByEmail(user.getEmail()));
        return "user_profile";
    }

    @GetMapping(value = "/entrant/{userId}")
    public String showProfile(Model model, @PathVariable Long userId) {
        log.info("Loading entrant profile page for user with id '{}'", userId);

        model.addAttribute("user", userService.findById(userId));
        return "user_profile";
    }

    @GetMapping(value = "/entrant/{userId}/block")
    public String blockUser(@PathVariable Long userId) {
        log.info("Blocking user with id '{}'", userId);

        userService.blockUser(userId);
        return "redirect:/entrant/" + userId;
    }

    @GetMapping(value = "/entrant/{userId}/unblock")
    public String unblockUser(@PathVariable Long userId) {
        log.info("Unblocking user with id '{}'", userId);

        userService.unblockUser(userId);
        return "redirect:/entrant/" + userId;
    }

    @GetMapping(value = "/entrant/enroll/{facultyId}")
    public String enrollForm(Model model, @PathVariable Long facultyId) {
        log.info("Loading enrollment form");

        Faculty faculty = facultyService.findById(facultyId);
        Enrollment enrollment = new Enrollment();
        enrollment.setFaculty(faculty);
        enrollment.setMarks(faculty.getSubjects().stream()
                .map(s -> new Mark(null, enrollment, s, new BigDecimal("0")))
                .collect(Collectors.toList()));

        model.addAttribute("enrollment", enrollment);
        return "entrant/enrollment_form";
    }

    @PostMapping(value = "/entrant/enroll/{facultyId}")
    public String enroll(@ModelAttribute Enrollment enrollment,
                         @AuthenticationPrincipal User user,
                         @PathVariable Long facultyId) {
        log.info("User with email '{}' registered in faculty with id {}", user.getEmail(), facultyId);

        enrollmentService.saveNewEnrollment(enrollment, userService.findByEmail(user.getEmail()));

        return "redirect:/faculty/" + facultyId;
    }

    @GetMapping("/congratulation")
    public String congratulation(Model model, @AuthenticationPrincipal User user) {
        log.info("Loading congratulation page");

        model.addAttribute("status", user.getStatus());
        return "entrant/congratulation";
    }

}
