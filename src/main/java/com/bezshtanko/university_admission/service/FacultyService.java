package com.bezshtanko.university_admission.service;

import com.bezshtanko.university_admission.exception.FacultyNotExistException;
import com.bezshtanko.university_admission.exception.UserNotExistException;
import com.bezshtanko.university_admission.model.enrollment.Enrollment;
import com.bezshtanko.university_admission.model.faculty.Faculty;
import com.bezshtanko.university_admission.model.faculty.FacultyStatus;
import com.bezshtanko.university_admission.model.user.User;
import com.bezshtanko.university_admission.repository.EnrollmentRepository;
import com.bezshtanko.university_admission.repository.FacultyRepository;
import com.bezshtanko.university_admission.repository.UserRepository;
import com.bezshtanko.university_admission.transfer.FacultyDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FacultyService {

    private static final Comparator<Enrollment> ACTIVE_FACULTY_ENROLLMENTS_COMPARATOR = Comparator
            .comparing((Enrollment e) -> e.getUser().getStatus())
            .thenComparing(Enrollment::getStatus)
            .thenComparing(Comparator.comparing(Enrollment::getMarksSum).reversed());

    private static final Comparator<Enrollment> CLOSED_FACULTY_ENROLLMENTS_COMPARATOR = Comparator
            .comparing((Enrollment e) -> e.getUser().getStatus()).reversed()
            .thenComparing(Comparator.comparing(Enrollment::getMarksSum).reversed());

    private final FacultyRepository facultyRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final UserRepository userRepository;

    @Autowired
    public FacultyService(FacultyRepository facultyRepository,
                          EnrollmentRepository enrollmentRepository,
                          UserRepository userRepository) {
        this.facultyRepository = facultyRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.userRepository = userRepository;
    }

    public void save(Faculty faculty) {
        facultyRepository.save(faculty);
    }

    public Faculty findById(Long id) {
        return facultyRepository.findById(id)
                .orElseThrow(FacultyNotExistException::new);
    }

    public Page<FacultyDTO> findAllForUser(Pageable pageable, User user) {
        return convertFacultiesToDTO(facultyRepository.findAll(pageable), user);
    }

    public FacultyDTO getWithEnrollmentsForUser(Long id, User user) {
        Faculty faculty = facultyRepository.findById(id).orElseThrow(FacultyNotExistException::new);

        List<Enrollment> enrollments;
        if (faculty.getStatus() == FacultyStatus.ACTIVE) {
            enrollments = enrollmentRepository.findRelevantByFacultyId(id);
        } else {
            enrollments = enrollmentRepository.findFinalizedByFacultyId(id);
        }

        setEnrollments(faculty, enrollments);
        return convertFacultyToDTO(faculty, user);
    }

    public void update(Faculty faculty) {
        facultyRepository.updateFaculty(faculty);
    }

    @Transactional
    public void finalizeFaculty(Long id) {
        Faculty faculty = facultyRepository.findById(id).orElseThrow(FacultyNotExistException::new);
        facultyRepository.setClosed(id);
        enrollmentRepository.setFinalized(id, faculty.getTotalPlaces());
        userRepository.setEnrolledStateFunded(id, faculty.getStateFundedPlaces());
        userRepository.setEnrolledContract(id, faculty.getContractPlaces());
    }

    public void deleteById(Long id) {
        facultyRepository.deleteById(id);
    }

    private void setEnrollments(Faculty faculty, List<Enrollment> enrollments) {
        faculty.setEnrollments(enrollments
                .stream()
                .peek(e -> e.setFaculty(faculty))
                .sorted(faculty.getStatus() == FacultyStatus.ACTIVE
                        ? ACTIVE_FACULTY_ENROLLMENTS_COMPARATOR
                        : CLOSED_FACULTY_ENROLLMENTS_COMPARATOR)
                .collect(Collectors.toList()));
    }

    private FacultyDTO convertFacultyToDTO(Faculty faculty, User user) {
        FacultyDTO result = new FacultyDTO(faculty);
        if (result.getStatus() == FacultyStatus.CLOSED) {
            result.setRegistrationAllowed(false);
        } else {
            result.setRegistrationAllowed(!getUserFacultiesNames(user).contains(result.getNameEn()));
        }
        return result;
    }

    private Page<FacultyDTO> convertFacultiesToDTO(Page<Faculty> page, User user) {
        List<String> userFacultiesNames = getUserFacultiesNames(user);
        Page<FacultyDTO> result = page.map(FacultyDTO::new);
        result.forEach(f -> f.setRegistrationAllowed(f.getStatus() != FacultyStatus.CLOSED && !userFacultiesNames.contains(f.getNameEn())));
        return result;
    }

    private List<String> getUserFacultiesNames(User user) {
        User applicant = userRepository.findByEmail(user.getEmail()).orElseThrow(UserNotExistException::new);
        return enrollmentRepository
                .findAllByUserId(applicant.getId())
                .stream()
                .map(e -> e.getFaculty().getNameEn())
                .collect(Collectors.toList());
    }

}
