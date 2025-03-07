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

    Page<User> findAll(Pageable pageable);

    Optional<User> findById(@NonNull Long id);

    User save(User user);

    Optional<User> update(UserRequest user, Long id);

    void deleteById(Long id);

    List<Survey> findAllSurvey();

    Page<Survey> findSurveysByUserId(@NonNull Long userId, Pageable pageable);

    Survey createSurvey(Long userId, Survey survey);

    Optional<Survey> updateSurveyByUserId(Long userId, Long surveyId, Survey updatedSurvey);

    void deleteSurveyByUserId(Long userId, Long surveyId);

    void increaseFailedAttempts(@NonNull Long id);

    void resetFailedAttempts(@NonNull Long id);

    void unlockAccount(@NonNull Long id);

    boolean isAccountLocked(@NonNull Long id);

}
