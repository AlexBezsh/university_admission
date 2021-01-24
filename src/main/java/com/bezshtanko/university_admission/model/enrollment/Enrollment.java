package com.bezshtanko.university_admission.model.enrollment;

import com.bezshtanko.university_admission.model.faculty.Faculty;
import com.bezshtanko.university_admission.model.mark.Mark;
import com.bezshtanko.university_admission.model.user.User;
import lombok.*;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Entity
@Table(name = "enrollment",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "faculty_id"})})
public class Enrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "faculty_id", nullable = false, updatable = false)
    private Faculty faculty;

    @Column(name = "status", columnDefinition = "ENUM('NEW', 'APPROVED', 'FINALIZED') NOT NULL DEFAULT 'NEW'")
    @Enumerated(EnumType.STRING)
    private EnrollmentStatus status;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "mark")
    private Set<Mark> marks;

    @Transient
    private Integer marksSum;

    @PostLoad
    private void countMarksSum() {
        marksSum = marks.stream().mapToInt(Mark::getMark).sum();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Enrollment that = (Enrollment) o;
        return Objects.equals(user, that.user) && Objects.equals(faculty, that.faculty);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, faculty);
    }
}
