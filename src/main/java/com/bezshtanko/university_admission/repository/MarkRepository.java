package com.bezshtanko.university_admission.repository;

import com.bezshtanko.university_admission.model.mark.Mark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MarkRepository extends JpaRepository<Mark, Long> {
}
