package com.bezshtanko.university_admission.repository;

import com.bezshtanko.university_admission.model.enrollment.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    @Modifying
    @Transactional
    @Query(value = "UPDATE enrollment SET status = 'APPROVED' WHERE id = :id",
            nativeQuery = true)
    void setApproved(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query(value = "UPDATE faculty SET name_ua = :nameUa, name_en = :nameEn, description_ua = :descriptionUa, description_en = :descriptionEn, state_funded_places = :stateFundedPlaces, contract_places = :contractPlaces WHERE id = :id",
            nativeQuery = true)
    void updateFaculty(@Param("nameUa") String nameUa,
                       @Param("nameEn") String nameEn,
                       @Param("descriptionUa") String descriptionUa,
                       @Param("descriptionEn") String descriptionEn,
                       @Param("stateFundedPlaces") Integer stateFundedPlaces,
                       @Param("contractPlaces") Integer contractPlaces,
                       @Param("id") Long id);

    List<Enrollment> findAllByFacultyId(Long facultyId);

}
