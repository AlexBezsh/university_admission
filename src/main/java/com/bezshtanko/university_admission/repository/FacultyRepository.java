package com.bezshtanko.university_admission.repository;

import com.bezshtanko.university_admission.model.faculty.Faculty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface FacultyRepository extends PagingAndSortingRepository<Faculty, Long> {

    @Query(value = "SELECT DISTINCT f FROM Faculty f JOIN f.subjects")
    Page<Faculty> findAll(Pageable pageable);

    @Modifying
    @Transactional
    @Query(value = "UPDATE faculty SET name_ua = :#{#faculty.nameUa}, name_en = :#{#faculty.nameEn}, description_ua = :#{#faculty.descriptionUa}, " +
            "description_en = :#{#faculty.descriptionEn}, state_funded_places = :#{#faculty.stateFundedPlaces}, contract_places = :#{#faculty.contractPlaces} " +
            "WHERE id = :#{#faculty.id}",
            nativeQuery = true)
    void updateFaculty(@Param("faculty") Faculty faculty);

    @Modifying
    @Transactional
    @Query(value = "UPDATE faculty SET status = 'CLOSED' WHERE id = :id",
            nativeQuery = true)
    void setClosed(Long id);
}
