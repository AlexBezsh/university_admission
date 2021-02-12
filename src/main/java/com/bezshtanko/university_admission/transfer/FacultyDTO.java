package com.bezshtanko.university_admission.transfer;

import com.bezshtanko.university_admission.model.enrollment.Enrollment;
import com.bezshtanko.university_admission.model.faculty.Faculty;
import com.bezshtanko.university_admission.model.faculty.FacultyStatus;
import com.bezshtanko.university_admission.model.subject.Subject;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class FacultyDTO {

    private Long id;
    private String nameEn;
    private String nameUa;
    private FacultyStatus status;
    private String descriptionEn;
    private String descriptionUa;
    private Integer stateFundedPlaces;
    private Integer contractPlaces;
    private Integer totalPlaces;
    private List<Subject> subjects;
    private List<Enrollment> enrollments;
    private boolean registrationAllowed;

    public FacultyDTO(Faculty faculty) {
        this.id = faculty.getId();
        this.nameEn = faculty.getNameEn();
        this.nameUa = faculty.getNameUa();
        this.status = faculty.getStatus();
        this.descriptionEn = faculty.getDescriptionEn();
        this.descriptionUa = faculty.getDescriptionUa();
        this.stateFundedPlaces = faculty.getStateFundedPlaces();
        this.contractPlaces = faculty.getContractPlaces();
        this.totalPlaces = faculty.getTotalPlaces();
        this.subjects = faculty.getSubjects();
        this.enrollments = faculty.getEnrollments();
    }

}
