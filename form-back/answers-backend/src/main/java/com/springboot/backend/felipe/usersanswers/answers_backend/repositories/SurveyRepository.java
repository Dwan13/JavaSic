package com.springboot.backend.felipe.usersanswers.answers_backend.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.springboot.backend.felipe.usersanswers.answers_backend.entities.Survey;
@Repository
public interface SurveyRepository extends CrudRepository<Survey, Long> {
    Page<Survey> findAll(Pageable pageable);

    Page<Survey> findByUserId(Long userId, Pageable pageable);

    Optional<Survey> findByIdAndUserId(Long id, Long userId);
}
