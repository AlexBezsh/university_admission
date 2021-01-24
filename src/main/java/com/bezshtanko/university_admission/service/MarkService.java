package com.bezshtanko.university_admission.service;

import com.bezshtanko.university_admission.model.mark.Mark;
import com.bezshtanko.university_admission.repository.MarkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MarkService {

    private final MarkRepository markRepository;

    @Autowired
    public MarkService(MarkRepository markRepository) {
        this.markRepository = markRepository;
    }

    public void saveAll(List<Mark> marks) {
        markRepository.saveAll(marks);
    }

}
