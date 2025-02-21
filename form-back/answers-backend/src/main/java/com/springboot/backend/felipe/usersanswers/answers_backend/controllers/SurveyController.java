package com.springboot.backend.felipe.usersanswers.answers_backend.controllers;

import com.springboot.backend.felipe.usersanswers.answers_backend.entities.Survey;
import com.springboot.backend.felipe.usersanswers.answers_backend.models.SurveyRequest;
import com.springboot.backend.felipe.usersanswers.answers_backend.services.SurveyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/surveys")
public class SurveyController {

    @Autowired
    private SurveyService surveyService;

    @GetMapping
    public List<Survey> list() {
        return surveyService.findAll();
    }
    @GetMapping("/page/{page}")
    public Page<Survey> listPageable(@PathVariable Integer page) {
        Pageable pageable = PageRequest.of(page, 4);
        return surveyService.findAll(pageable);
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> show(@PathVariable Long id) {
        Optional<Survey> surveyOptional = surveyService.findById(id);
        if (surveyOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(surveyOptional.orElseThrow());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Collections.singletonMap("error", "el registro no se encontro por el id:" + id));
    }

    @PostMapping
    public ResponseEntity<?> createSurvey(@Valid @RequestBody Survey survey, BindingResult result) {
        if (result.hasErrors()) {
            return validation(result);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(surveyService.save(survey));
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@Valid @RequestBody SurveyRequest survey, BindingResult result, @PathVariable Long id) {

        if (result.hasErrors()) {
            return validation(result);
        }
        
        Optional<Survey> surveyOptional = surveyService.update(survey, id);

        if (surveyOptional.isPresent()) {
            return ResponseEntity.ok(surveyOptional.orElseThrow());
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        Optional<Survey> surveyOptional = surveyService.findById(id);
        if (surveyOptional.isPresent()) {
            surveyService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    private ResponseEntity<?> validation(BindingResult result) {
        Map<String, String> errors = new HashMap<>();
        result.getFieldErrors().forEach(error -> {
            errors.put(error.getField(), "El campo " + error.getField() + " " + error.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errors);
    }
}
