package com.bezshtanko.university_admission.transfer;

import com.bezshtanko.university_admission.model.enrollment.Enrollment;
import com.bezshtanko.university_admission.model.faculty.Faculty;
import com.bezshtanko.university_admission.model.faculty.Status;
import com.bezshtanko.university_admission.model.subject.Subject;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class FacultyDTO {

    private String name;
    private Status status;
    private String description;
    private Integer stateFundedPlaces;
    private Integer contractPlaces;
    private Set<SubjectDTO> subjects;
    private Set<Enrollment> enrollments;
    private Integer totalPlaces;

    public FacultyDTO(Faculty faculty, Locale locale) {
        if (locale.getLanguage().equals(new Locale("en").getLanguage())) {
            this.name = faculty.getNameEn();
            this.description = faculty.getDescriptionEn();
        } else {
            this.name = faculty.getNameUa();
            this.description = faculty.getDescriptionUa();
        }
        this.status = faculty.getStatus();
        this.stateFundedPlaces = faculty.getStateFundedPlaces();
        this.contractPlaces = faculty.getContractPlaces();
        this.subjects = faculty
                .getSubjects()
                .stream()
                .map(s -> new SubjectDTO(s, locale))
                .collect(Collectors.toSet());
        this.totalPlaces = faculty.getTotalPlaces();
        this.enrollments = faculty.getEnrollments(); //TODO
    }

}