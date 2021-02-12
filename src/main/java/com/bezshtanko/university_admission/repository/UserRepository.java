package com.bezshtanko.university_admission.repository;

import com.bezshtanko.university_admission.model.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
public interface UserRepository extends PagingAndSortingRepository<User, Long> {

    Optional<User> findByEmail(String email);
    Page<User> findAll(Pageable pageable);

    @Modifying
    @Transactional
    @Query(value = "UPDATE user SET status = 'BLOCKED' WHERE id = :id",
            nativeQuery = true)
    void blockUser(Long id);

    @Modifying
    @Transactional
    @Query(value = "UPDATE user SET status = 'ACTIVE' WHERE id = :id",
            nativeQuery = true)
    void unblockUser(Long id);

    @Modifying
    @Transactional
    @Query(value = "UPDATE user " +
            "SET status = 'ENROLLED_STATE_FUNDED' " +
            "WHERE id in(" +
            "SELECT t.u_id from (" +
            "SELECT user.id as u_id, sum(mark) AS total FROM enrollment " +
            "JOIN marks ON enrollment.id = marks.enrollment_id " +
            "JOIN user ON enrollment.user_id = user.id " +
            "WHERE enrollment.faculty_id = :facultyId " +
            "AND user.status = 'ACTIVE' " +
            "AND enrollment.status = 'FINALIZED' " +
            "GROUP BY enrollment.id " +
            "ORDER BY total DESC " +
            "LIMIT :quantity) t)",
            nativeQuery = true)
    void setEnrolledStateFunded(Long facultyId, Integer quantity);

    @Modifying
    @Transactional
    @Query(value = "UPDATE user " +
            "SET status = 'ENROLLED_CONTRACT' " +
            "WHERE id in(" +
            "SELECT t.u_id from (" +
            "SELECT user.id as u_id, sum(mark) AS total FROM enrollment " +
            "JOIN marks ON enrollment.id = marks.enrollment_id " +
            "JOIN user ON enrollment.user_id = user.id " +
            "WHERE enrollment.faculty_id = :facultyId " +
            "AND user.status = 'ACTIVE' " +
            "AND enrollment.status = 'FINALIZED' " +
            "GROUP BY enrollment.id " +
            "ORDER BY total DESC " +
            "LIMIT :quantity) t)",
            nativeQuery = true)
    void setEnrolledContract(Long facultyId, Integer quantity);

}
