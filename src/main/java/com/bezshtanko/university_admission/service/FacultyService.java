package com.bezshtanko.university_admission.service;

import com.bezshtanko.university_admission.model.faculty.Faculty;
import com.bezshtanko.university_admission.repository.FacultyRepository;
import com.bezshtanko.university_admission.transfer.FacultyDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class FacultyService {

    private FacultyRepository facultyRepository;

    @Autowired
    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    public Page<FacultyDTO> getFaculties(Pageable pageable) {
        return facultyRepository
                .findAll(pageable)
                .map(f -> new FacultyDTO(f, false));
    }

    public Faculty saveNewFaculty(Faculty faculty) {
        return facultyRepository.save(faculty);
    }

}
