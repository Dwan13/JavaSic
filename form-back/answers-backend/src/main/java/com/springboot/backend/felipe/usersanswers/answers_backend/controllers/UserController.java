package com.springboot.backend.felipe.usersanswers.answers_backend.controllers;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.springboot.backend.felipe.usersanswers.answers_backend.entities.Survey;
import com.springboot.backend.felipe.usersanswers.answers_backend.entities.User;
import com.springboot.backend.felipe.usersanswers.answers_backend.models.UserRequest;
import com.springboot.backend.felipe.usersanswers.answers_backend.services.UserService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PutMapping;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@CrossOrigin(origins = { "http://localhost:4200" })
@RestController
@RequestMapping("/api/users")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService service;

    @GetMapping
    public List<User> listUser() {
        log.info("Entrando al método listUser()");
        return service.findAll();
    }

    @GetMapping("/page/{page}")
    public Page<User> listPageable(@PathVariable Integer page) {
        log.info("Entrando al método listPageable()");

        Pageable pageable = PageRequest.of(page, 4);
        return service.findAll(pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> show(@PathVariable Long id) {
        Optional<User> userOptional = service.findById(id);
        if (userOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(userOptional.orElseThrow());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Collections.singletonMap("error", "el usuario no se encontro por el id:" + id));
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody User user, BindingResult result) {
        if (result.hasErrors()) {
            return validation(result);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@Valid @RequestBody UserRequest user, BindingResult result, @PathVariable Long id) {

        if (result.hasErrors()) {
            return validation(result);
        }

        Optional<User> userOptional = service.update(user, id);

        if (userOptional.isPresent()) {
            return ResponseEntity.ok(userOptional.orElseThrow());
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        Optional<User> userOptional = service.findById(id);
        if (userOptional.isPresent()) {
            service.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/surveys")
    public List<Survey> listSurvey() {
        List<Survey> surveys = service.findAllSurvey();
        for (Survey survey : surveys) {
            Hibernate.initialize(survey.getUser()); // Forzar la carga de la relación LAZY
        }
        return surveys;
    }

    // Endpoint para obtener surveys por userId
    @GetMapping("/{id}/surveys")
    public ResponseEntity<Page<Survey>> getSurveysByUserId(
            @PathVariable Long id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "4") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Survey> surveys = service.findSurveysByUserId(id, pageable);

        return ResponseEntity.ok(surveys);
    }

    @PostMapping("/{id}/surveys")
    public ResponseEntity<?> createSurvey(
            @PathVariable Long id,
            @Valid @RequestBody Survey survey,
            BindingResult result) {

        // Validar los datos de la survey
        if (result.hasErrors()) {
            return validation(result);
        }

        try {
            // Crear la survey asociada al usuario
            Survey createdSurvey = service.createSurvey(id, survey);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdSurvey);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("error", e.getMessage()));
        }
    }

    // Endpoint para actualizar una survey por userId
    @PutMapping("/{id}/surveys/{surveyId}")
    public ResponseEntity<?> updateSurveyByUserId(
            @PathVariable Long id,
            @PathVariable Long surveyId,
            @RequestBody Survey updatedSurvey) {

        Optional<Survey> surveyOptional = service.updateSurveyByUserId(id, surveyId, updatedSurvey);

        if (surveyOptional.isPresent()) {
            return ResponseEntity.ok(surveyOptional.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Collections.singletonMap("error", "Survey no encontrada o no pertenece al usuario"));
    }

    // Endpoint para eliminar una survey por userId
    @DeleteMapping("/{id}/surveys/{surveyId}")
    public ResponseEntity<?> deleteSurveyByUserId(
            @PathVariable Long id,
            @PathVariable Long surveyId) {

        try {
            service.deleteSurveyByUserId(id, surveyId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("error", e.getMessage()));
        }
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(@AuthenticationPrincipal User userDetails) {
        Map<String, Object> response = new HashMap<>();
        response.put("id", userDetails.getId());
        response.put("username", userDetails.getUsername());
        response.put("isAdmin", userDetails.isAdmin());
        return ResponseEntity.ok(response);
    }

    private ResponseEntity<?> validation(BindingResult result) {
        Map<String, String> errors = new HashMap<>();
        result.getFieldErrors().forEach(error -> {
            errors.put(error.getField(), "El campo " + error.getField() + " " + error.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errors);
    }
}
