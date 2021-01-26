package com.bezshtanko.university_admission.service;

import com.bezshtanko.university_admission.model.enrollment.Enrollment;
import com.bezshtanko.university_admission.repository.EnrollmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;

    @Autowired
    public EnrollmentService(EnrollmentRepository enrollmentRepository) {
        this.enrollmentRepository = enrollmentRepository;
    }

    public Enrollment saveNewEnrollment(Enrollment enrollment) {
        return enrollmentRepository.save(enrollment);
    }

    public void setApproved(Long enrollmentId) {
        enrollmentRepository.setApproved(enrollmentId);
    }

    public void setFinalized(List<Enrollment> enrollments) {
        enrollmentRepository.setFinalized(enrollments
                .stream()
                .map(Enrollment::getId)
                .collect(Collectors.toList()));
    }

    public List<Enrollment> getAllEnrollments(Long facultyId) {
        return enrollmentRepository.findAllByFacultyId(facultyId);
    }

}
