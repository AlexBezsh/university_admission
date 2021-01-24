package com.bezshtanko.university_admission.transfer;

import com.bezshtanko.university_admission.model.subject.Subject;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class SubjectsDTO {

    private List<SubjectDTO> subjects;

}
