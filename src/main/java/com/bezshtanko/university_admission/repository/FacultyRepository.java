package com.bezshtanko.university_admission.repository;

import com.bezshtanko.university_admission.model.faculty.Faculty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface FacultyRepository extends PagingAndSortingRepository<Faculty, Long> {

    @Query(value = "SELECT DISTINCT f FROM Faculty f JOIN f.subjects")
    Page<Faculty> findAll(Pageable pageable);

    @Modifying
    @Transactional
    @Query(value = "UPDATE faculty SET name_ua = :nameUa, name_en = :nameEn, description_ua = :descriptionUa, description_en = :descriptionEn, state_funded_places = :stateFundedPlaces, contract_places = :contractPlaces WHERE id = :id",
            nativeQuery = true)
    void updateFaculty(String nameUa,
                       String nameEn,
                       String descriptionUa,
                       String descriptionEn,
                       Integer stateFundedPlaces,
                       Integer contractPlaces,
                       Long id);

    @Modifying
    @Transactional
    @Query(value = "UPDATE faculty SET status = 'CLOSED' WHERE id = :id",
            nativeQuery = true)
    void setClosed(Long id);
}
