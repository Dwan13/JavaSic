package com.springboot.backend.felipe.usersanswers.answers_backend.services;

import com.springboot.backend.felipe.usersanswers.answers_backend.entities.Survey;
import com.springboot.backend.felipe.usersanswers.answers_backend.models.SurveyRequest;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;

public interface SurveyService {

    List<Survey> findAll();

    Page<Survey> findAll(Pageable pageable);

    Optional<Survey> findById(@NonNull Long id);

    Survey save(Survey survey);

    Optional<Survey> update(SurveyRequest survey, Long id);

    void deleteById(Long id);


}
