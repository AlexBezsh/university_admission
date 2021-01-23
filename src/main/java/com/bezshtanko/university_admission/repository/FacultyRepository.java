package com.bezshtanko.university_admission.repository;

import com.bezshtanko.university_admission.model.faculty.Faculty;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FacultyRepository extends PagingAndSortingRepository<Faculty, Long> {

}
