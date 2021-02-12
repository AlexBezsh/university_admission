package com.bezshtanko.university_admission.model.enrollment;

import com.bezshtanko.university_admission.model.faculty.Faculty;
import com.bezshtanko.university_admission.model.mark.Mark;
import com.bezshtanko.university_admission.model.user.User;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString(exclude = {"marks"})
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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "enrollment")
    private List<Mark> marks;

    @Transient
    private BigDecimal marksSum;

    @PostLoad
    private void countMarksSum() {
        marksSum = marks.stream().map(Mark::getMark).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

}
