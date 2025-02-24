package com.springboot.backend.felipe.usersanswers.answers_backend.services;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;

import com.springboot.backend.felipe.usersanswers.answers_backend.entities.Survey;
import com.springboot.backend.felipe.usersanswers.answers_backend.entities.User;
import com.springboot.backend.felipe.usersanswers.answers_backend.models.UserRequest;

public interface UserService {
    List<User> findAll();
    
    List<Survey> findAllSurvey();
    
    Page<Survey> findSurveysByUserId(Long userId, Pageable pageable);

    Page<User> findAll(Pageable pageable);

    Optional<User> findById(@NonNull Long id);

    User save(User user);

    Optional<User> update(UserRequest user, Long id);

    void deleteById(Long id);

    Optional<Survey> updateSurveyByUserId(Long userId, Long surveyId, Survey updatedSurvey);

    void deleteSurveyByUserId(Long userId, Long surveyId);

}
