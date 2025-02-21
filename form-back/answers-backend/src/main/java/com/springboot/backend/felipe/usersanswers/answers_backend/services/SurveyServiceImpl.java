package com.springboot.backend.felipe.usersanswers.answers_backend.services;

import com.springboot.backend.felipe.usersanswers.answers_backend.entities.BrandPC;
import com.springboot.backend.felipe.usersanswers.answers_backend.entities.Survey;
import com.springboot.backend.felipe.usersanswers.answers_backend.models.SurveyRequest;
import com.springboot.backend.felipe.usersanswers.answers_backend.repositories.BrandPCRepository;
import com.springboot.backend.felipe.usersanswers.answers_backend.repositories.SurveyRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class SurveyServiceImpl implements SurveyService {

    private SurveyRepository surveyRepository;
    private BrandPCRepository brandPCRepository;

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    @Transactional(readOnly = true)
    public List<Survey> findAll() {
        return (List) surveyRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Survey> findAll(Pageable pageable) {
        return this.surveyRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Survey> findById(@NonNull Long id) {
        return surveyRepository.findById(id);
    }

    @Override
    @Transactional
    public Survey save(Survey survey) {
        return surveyRepository.save(survey);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        surveyRepository.deleteById(id);
    }

    public Optional<Survey> update(SurveyRequest updatedSurvey, Long id) {
        return surveyRepository.findById(id).map(existingSurvey -> {
            existingSurvey.setEmail(updatedSurvey.getEmail());
            existingSurvey.setDocument_number(updatedSurvey.getDocument_number());
            existingSurvey.setComments(updatedSurvey.getComments());
            existingSurvey.setResponse_date(updatedSurvey.getResponse_date());

            // Verificar si la marca existe antes de asignarla
            Optional<BrandPC> brandOptional = brandPCRepository.findById(updatedSurvey.getBrandId());
            brandOptional.ifPresent(existingSurvey::setBrand);

            return surveyRepository.save(existingSurvey);
        });
    }

   

   

    

    
}
