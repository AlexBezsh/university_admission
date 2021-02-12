package com.bezshtanko.university_admission.model.subject;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Entity
@Table(name = "subject",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"type", "name_ua"}),
                @UniqueConstraint(columnNames = {"type", "name_en"})})
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "type", columnDefinition = "ENUM('SCHOOL', 'EXAM') NOT NULL")
    @Enumerated(EnumType.STRING)
    private SubjectType type;

    @Column(name = "name_en", nullable = false)
    @Size(min = 2, max = 100)
    private String nameEn;

    @Column(name = "name_ua", nullable = false)
    @Size(min = 2, max = 100)
    private String nameUa;

}
