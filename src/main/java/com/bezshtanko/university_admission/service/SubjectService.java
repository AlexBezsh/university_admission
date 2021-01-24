package com.bezshtanko.university_admission.service;

import com.bezshtanko.university_admission.model.subject.Subject;
import com.bezshtanko.university_admission.repository.SubjectRepository;
import com.bezshtanko.university_admission.transfer.SubjectDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SubjectService {

    private SubjectRepository subjectRepository;

    @Autowired
    public SubjectService(SubjectRepository subjectRepository) {
        this.subjectRepository = subjectRepository;
    }

    public List<Subject> getAll() {
        return subjectRepository.findAll();
                /*.stream()
                .map(SubjectDTO::new)
                .collect(Collectors.toList());*/
    }


}
