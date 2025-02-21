package com.springboot.backend.felipe.usersanswers.answers_backend.repositories;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import com.springboot.backend.felipe.usersanswers.answers_backend.entities.Survey;

public interface SurveyRepository extends CrudRepository<Survey, Long> {
    Page<Survey> findAll(Pageable pageable);
}
