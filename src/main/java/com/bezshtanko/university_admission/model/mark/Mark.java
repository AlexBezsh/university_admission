package com.bezshtanko.university_admission.model.mark;

import com.bezshtanko.university_admission.model.enrollment.Enrollment;
import com.bezshtanko.university_admission.model.subject.Subject;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Entity
@Table(name = "mark",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"enrollment_id", "subject_id"})})
public class Mark {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @ManyToOne(targetEntity = Enrollment.class, fetch = FetchType.LAZY)
    private Enrollment enrollment;

    @NotNull
    @ManyToOne(targetEntity = Subject.class, fetch = FetchType.LAZY)
    private Subject subject;

    @Column(name = "mark")
    @Min(0)
    private Integer mark;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Mark mark = (Mark) o;
        return Objects.equals(enrollment, mark.enrollment) && Objects.equals(subject, mark.subject);
    }

    @Override
    public int hashCode() {
        return Objects.hash(enrollment, subject);
    }
}
