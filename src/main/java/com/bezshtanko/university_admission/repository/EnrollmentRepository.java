package com.bezshtanko.university_admission.repository;

import com.bezshtanko.university_admission.model.enrollment.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    @Modifying
    @Transactional
    @Query(value = "UPDATE enrollment SET status = 'APPROVED' WHERE id = :id",
            nativeQuery = true)
    void setApproved(Long id);

    @Modifying
    @Transactional
    @Query(value = "UPDATE enrollment " +
            "SET status = 'FINALIZED' " +
            "WHERE id in(" +
            "SELECT t.e_id from (" +
            "SELECT enrollment.id as e_id, sum(mark) AS total FROM enrollment " +
            "JOIN marks ON enrollment.id = marks.enrollment_id " +
            "JOIN user ON enrollment.user_id = user.id " +
            "WHERE enrollment.faculty_id = :facultyId " +
            "AND user.status = 'ACTIVE' " +
            "AND enrollment.status = 'APPROVED' " +
            "GROUP BY enrollment.id " +
            "ORDER BY total DESC " +
            "LIMIT :quantity) t)",
            nativeQuery = true)
    void setFinalized(Long facultyId, Integer quantity);

    @Modifying
    @Transactional
    @Query(value = "UPDATE faculty SET name_ua = :nameUa, name_en = :nameEn, description_ua = :descriptionUa, " +
            "description_en = :descriptionEn, state_funded_places = :stateFundedPlaces, contract_places = :contractPlaces " +
            "WHERE id = :id",
            nativeQuery = true)
    void updateFaculty(String nameUa,
                       String nameEn,
                       String descriptionUa,
                       String descriptionEn,
                       Integer stateFundedPlaces,
                       Integer contractPlaces,
                       Long id);

    @Query("SELECT DISTINCT e FROM Enrollment e LEFT JOIN e.faculty LEFT JOIN e.marks " +
            "WHERE e.user.id = :id")
    List<Enrollment> findAllByUserId(Long id);

    @Query("SELECT DISTINCT e FROM Enrollment e LEFT JOIN e.marks " +
            "WHERE e.faculty.id = :id AND e.status = 'FINALIZED'")
    List<Enrollment> findFinalizedByFacultyId(Long id);

    @Query("SELECT DISTINCT e FROM Enrollment e LEFT JOIN e.marks " +
            "WHERE e.faculty.id = :id AND (e.user.status = 'ACTIVE' OR e.user.status = 'BLOCKED')")
    List<Enrollment> findRelevantByFacultyId(Long id);

}
