package com.bezshtanko.university_admission.model.faculty;

import com.bezshtanko.university_admission.model.enrollment.Enrollment;
import com.bezshtanko.university_admission.model.subject.Subject;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
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
    private Status status;

    @Column(name = "description_en", nullable = false, columnDefinition = "TEXT")
    private String descriptionEn;

    @Column(name = "description_ua", nullable = false, columnDefinition = "TEXT")
    private String descriptionUa;

    @Column(name = "state_funded_places", nullable = false)
    @Min(0)
    private Integer stateFundedPlaces;

    @Column(name = "contract_places", nullable = false)
    @Min(0)
    private Integer contractPlaces;

    @Transient
    private Integer totalPlaces;

    @OneToMany(targetEntity = Subject.class, fetch = FetchType.EAGER)
    private Set<Subject> subjects;

    @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "faculty_id")
    private Set<Enrollment> enrollments;

    @PostLoad
    private void countTotalPlaces() {
        totalPlaces = stateFundedPlaces + contractPlaces;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Faculty faculty = (Faculty) o;
        return Objects.equals(nameUa, faculty.nameUa);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nameUa);
    }

}
