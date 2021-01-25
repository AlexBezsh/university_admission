package com.bezshtanko.university_admission.service;

import com.bezshtanko.university_admission.model.subject.Subject;
import com.bezshtanko.university_admission.repository.SubjectRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class SubjectService {

    private final SubjectRepository subjectRepository;

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
