package com.bezshtanko.university_admission.model.faculty;

import com.bezshtanko.university_admission.model.enrollment.Enrollment;
import com.bezshtanko.university_admission.model.subject.Subject;
import lombok.*;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString(exclude = {"enrollments"})
@Entity
@Table(name = "faculty",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"name_en"}),
                @UniqueConstraint(columnNames = {"name_ua"})})
public class Faculty {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name_en", nullable = false)
    @Size(min = 2, max = 250)
    private String nameEn;

    @Column(name = "name_ua", nullable = false)
    @Size(min = 2, max = 250)
    private String nameUa;

    @Column(name = "status", columnDefinition = "ENUM('ACTIVE', 'CLOSED') NOT NULL DEFAULT 'ACTIVE'")
    @Enumerated(EnumType.STRING)
    private FacultyStatus status;

    @Column(name = "description_en", nullable = false, columnDefinition = "TEXT")
    @Size(min = 10)
    private String descriptionEn;

    @Column(name = "description_ua", nullable = false, columnDefinition = "TEXT")
    @Size(min = 10)
    private String descriptionUa;

    @Column(name = "state_funded_places", nullable = false)
    @Min(0)
    private Integer stateFundedPlaces;

    @Column(name = "contract_places", nullable = false)
    @Min(0)
    private Integer contractPlaces;

    @Formula("state_funded_places + contract_places")
    private Integer totalPlaces;

    @OneToMany(targetEntity = Subject.class, fetch = FetchType.LAZY)
    @Size(min = 1)
    private List<Subject> subjects;

    @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "faculty_id")
    private List<Enrollment> enrollments;

}
