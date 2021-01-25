package com.bezshtanko.university_admission.repository;


import com.bezshtanko.university_admission.model.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
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
    void blockUser(@Param("id") Long id);

}
