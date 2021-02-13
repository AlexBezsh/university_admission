package com.bezshtanko.university_admission.service;

import com.bezshtanko.university_admission.model.enrollment.Enrollment;
import com.bezshtanko.university_admission.model.enrollment.EnrollmentStatus;
import com.bezshtanko.university_admission.model.user.User;
import com.bezshtanko.university_admission.repository.EnrollmentRepository;
import com.bezshtanko.university_admission.repository.MarkRepository;
import com.bezshtanko.university_admission.transfer.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final MarkRepository markRepository;

    @Autowired
    public EnrollmentService(EnrollmentRepository enrollmentRepository, MarkRepository markRepository) {
        this.enrollmentRepository = enrollmentRepository;
        this.markRepository = markRepository;
    }

    @Transactional
    public void saveNewEnrollment(Enrollment enrollment, UserDTO user) {
        enrollment.setUser(User.builder().id(user.getId()).build());
        enrollment.setStatus(EnrollmentStatus.NEW);
        enrollment.getMarks().forEach(m -> m.setEnrollment(enrollment));

        enrollmentRepository.save(enrollment);
        markRepository.saveAll(enrollment.getMarks());
    }

    public List<Enrollment> findAllByUserId(Long id) {
        return enrollmentRepository.findAllByUserId(id);
    }

    public void setApproved(Long id) {
        enrollmentRepository.setApproved(id);
    }

}
