package com.springboot.backend.felipe.usersanswers.answers_backend.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.springboot.backend.felipe.usersanswers.answers_backend.entities.User;

public interface UserRepository extends CrudRepository<User, Long> {
        Page<User> findAll(Pageable pageable);

        Optional<User> findByUsername(String name);

        @Modifying
        @Query("UPDATE User u SET u.failed_attempts = :failedAttempts WHERE u.id = :id")
        void updateFailedAttempts(@Param("failedAttempts") int failedAttempts, @Param("id") Long id);

        @Modifying
        @Query("UPDATE User u SET u.locked_account = :locked WHERE u.id = :id")
        void updateLockedAccount(@Param("locked") boolean locked, @Param("id") Long id);

}
