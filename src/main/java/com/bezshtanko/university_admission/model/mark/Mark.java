package com.bezshtanko.university_admission.model.mark;

import com.bezshtanko.university_admission.model.enrollment.Enrollment;
import com.bezshtanko.university_admission.model.subject.Subject;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Entity
@Table(name = "marks",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"enrollment_id", "subject_id"})})
public class Mark {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enrollment_id", nullable = false)
    private Enrollment enrollment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;

    @Column(name = "mark")
    @Min(0)
    private BigDecimal mark;

}
