package com.bezshtanko.university_admission.transfer;

import com.bezshtanko.university_admission.model.subject.Subject;
import com.bezshtanko.university_admission.model.subject.SubjectType;
import lombok.*;
import org.springframework.context.i18n.LocaleContextHolder;

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
    private SubjectType type;

    public SubjectDTO(Subject subject) {
        this.id = subject.getId();
        if (LocaleContextHolder.getLocale().getLanguage().equals(new Locale("en").getLanguage())) {
            this.name = subject.getNameEn();
        } else {
            this.name = subject.getNameUa();
        }
        this.type = subject.getType();
    }

}
