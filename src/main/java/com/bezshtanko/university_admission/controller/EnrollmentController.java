package com.bezshtanko.university_admission.controller;

import com.bezshtanko.university_admission.model.faculty.Faculty;
import com.bezshtanko.university_admission.service.EnrollmentService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class EnrollmentController {

    private EnrollmentService enrollmentService;

    public EnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    @GetMapping(value = "faculty/{facultyId}/enrollment/{enrollmentId}/approve") //TODO security
    public String approveEnrollment(@PathVariable Long facultyId, @PathVariable Long enrollmentId) {
        enrollmentService.setApproved(enrollmentId);
        return "redirect:/faculty/" + facultyId;
    }


}
