package com.bezshtanko.university_admission.transfer;

import com.bezshtanko.university_admission.model.subject.Subject;
import com.bezshtanko.university_admission.model.subject.Type;
import lombok.*;

import java.util.Locale;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class SubjectDTO {

    private Long id;
    private String name;
    private Type type;

    public SubjectDTO(Subject subject, Locale locale) {
        this.id = subject.getId();
        if (locale.getLanguage().equals(new Locale("en").getLanguage())) {
            this.name = subject.getNameEn();
        } else {
            this.name = subject.getNameUa();
        }
        this.type = subject.getType();
    }

}
