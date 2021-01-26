package com.bezshtanko.university_admission.transfer;

import com.bezshtanko.university_admission.model.enrollment.Enrollment;
import com.bezshtanko.university_admission.model.faculty.Faculty;
import com.bezshtanko.university_admission.model.faculty.FacultyStatus;
import lombok.*;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class FacultyDTO {

    private Long id;
    private String name;
    private FacultyStatus status;
    private String description;
    private Integer stateFundedPlaces;
    private Integer contractPlaces;
    private Integer totalPlaces;
    private List<SubjectDTO> subjects;
    private List<Enrollment> enrollments;

    public FacultyDTO(Faculty faculty, boolean includeEnrollments) {
        this.id = faculty.getId();
        if (LocaleContextHolder.getLocale().getLanguage().equals(new Locale("en").getLanguage())) {
            this.name = faculty.getNameEn();
            this.description = faculty.getDescriptionEn();
        } else {
            this.name = faculty.getNameUa();
            this.description = faculty.getDescriptionUa();
        }
        this.status = faculty.getStatus();
        this.stateFundedPlaces = faculty.getStateFundedPlaces();
        this.contractPlaces = faculty.getContractPlaces();
        this.totalPlaces = faculty.getTotalPlaces();
        this.subjects = faculty
                .getSubjects()
                .stream()
                .map(SubjectDTO::new)
                .sorted(Comparator.comparing(SubjectDTO::getName))
                .collect(Collectors.toList());
        if (includeEnrollments) { //TODO this sorting is not working - refactor - move to service layer
            Comparator<Enrollment> comparator;
            if (status == FacultyStatus.ACTIVE) {
                comparator = Comparator.comparing(Enrollment::getStatus);
            } else {
                comparator = Comparator.comparing(Enrollment::getStatus).reversed();
            }
            comparator.thenComparing(Comparator.comparing(Enrollment::getMarksSum));

            this.enrollments = faculty
                    .getEnrollments()
                    .stream()
                    .sorted(comparator)
                    .collect(Collectors.toList());
        }
    }

}