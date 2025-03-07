package com.springboot.backend.felipe.usersanswers.answers_backend.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.backend.felipe.usersanswers.answers_backend.entities.Role;
import com.springboot.backend.felipe.usersanswers.answers_backend.entities.Survey;
import com.springboot.backend.felipe.usersanswers.answers_backend.entities.User;
import com.springboot.backend.felipe.usersanswers.answers_backend.models.IUser;
import com.springboot.backend.felipe.usersanswers.answers_backend.models.UserRequest;
import com.springboot.backend.felipe.usersanswers.answers_backend.repositories.RoleRepository;
import com.springboot.backend.felipe.usersanswers.answers_backend.repositories.SurveyRepository;
import com.springboot.backend.felipe.usersanswers.answers_backend.repositories.UserRepository;

@Service
public class UserServiceImpl implements UserService {

    private static final int MAX_FAILED_ATTEMPTS = 3; // 🔹 Intentos permitidos antes del bloqueo

    @Autowired
    private UserRepository repository;

    private RoleRepository roleRepository;
    private SurveyRepository surveyRepository;

    private PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository repository, PasswordEncoder passwordEncoder,
            RoleRepository roleRepository, SurveyRepository surveyRepository) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.surveyRepository = surveyRepository;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    @Transactional(readOnly = true)
    public List<User> findAll() {
        return (List) this.repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<User> findAll(Pageable pageable) {
        return this.repository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<User> findById(@NonNull Long id) {
        return repository.findById(id);
    }

    @Transactional
    @Override
    public User save(User user) {
        user.setRoles(getRoles(user));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return repository.save(user);
    }

    @Override
    @Transactional
    public Optional<User> update(UserRequest user, Long id) {
        Optional<User> userOptional = repository.findById(id);

        if (userOptional.isPresent()) {
            User userDb = userOptional.get();
            userDb.setEmail(user.getEmail());
            userDb.setLastname(user.getLastname());
            userDb.setName(user.getName());
            userDb.setUsername(user.getUsername());

            userDb.setRoles(getRoles(user));
            return Optional.of(repository.save(userDb));
        }
        return Optional.empty();
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    private List<Role> getRoles(IUser user) {
        List<Role> roles = new ArrayList<>();
        Optional<Role> optionalRoleUser = roleRepository.findByName("ROLE_USER");
        optionalRoleUser.ifPresent(roles::add);

        if (user.isAdmin()) {
            Optional<Role> optionalRoleAdmin = roleRepository.findByName("ROLE_ADMIN");
            optionalRoleAdmin.ifPresent(roles::add);
        }
        return roles;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Survey> findAllSurvey() {
        return (List) surveyRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Survey> findSurveysByUserId(Long userId, Pageable pageable) {
        return surveyRepository.findByUserId(userId, pageable);
    }

    @Override
    @Transactional
    public Survey createSurvey(Long userId, Survey survey) {
        // Buscar al usuario por su ID
        Optional<User> userOptional = repository.findById(userId);

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            // Asociar la survey al usuario
            survey.setUser(user);

            // Guardar la survey en la base de datos
            return surveyRepository.save(survey);
        } else {
            throw new RuntimeException("Usuario no encontrado con ID: " + userId);
        }
    }

    @Override
    @Transactional
    public Optional<Survey> updateSurveyByUserId(Long userId, Long surveyId, Survey updatedSurvey) {
        // Buscar la survey por id y userId
        Optional<Survey> surveyOptional = surveyRepository.findByIdAndUserId(surveyId, userId);

        if (surveyOptional.isPresent()) {
            Survey survey = surveyOptional.get();
            // Actualizar los campos de la survey
            survey.setEmail(updatedSurvey.getEmail());
            survey.setDocument_number(updatedSurvey.getDocument_number());
            survey.setComments(updatedSurvey.getComments());
            survey.setResponse_date(updatedSurvey.getResponse_date());
            survey.setBrand(updatedSurvey.getBrand());

            // Guardar la survey actualizada
            return Optional.of(surveyRepository.save(survey));
        }
        return Optional.empty();
    }

    @Override
    @Transactional
    public void deleteSurveyByUserId(Long userId, Long surveyId) {
        // Buscar la survey por id y userId
        Optional<Survey> surveyOptional = surveyRepository.findByIdAndUserId(surveyId, userId);

        if (surveyOptional.isPresent()) {
            // Eliminar la survey si existe y pertenece al usuario
            surveyRepository.deleteById(surveyId);
        } else {
            throw new RuntimeException("Survey no encontrada o no pertenece al usuario");
        }
    }

    @Override
    @Transactional
    public void increaseFailedAttempts(Long userId) {
        Optional<User> userOptional = repository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            int attempts = user.getFailed_attempts() != null ? user.getFailed_attempts() : 0;
            attempts++;

            if (attempts >= MAX_FAILED_ATTEMPTS) {
                user.setLocked_account(true);
            }

            user.setFailed_attempts(attempts);
            repository.save(user);
        }
    }

    @Override
    @Transactional
    public void resetFailedAttempts(Long userId) {
        Optional<User> userOptional = repository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setFailed_attempts(0);
            repository.save(user);
        }
    }

    @Override
    @Transactional
    public void unlockAccount(Long userId) {
        Optional<User> userOptional = repository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setFailed_attempts(0);
            user.setLocked_account(false);
            repository.save(user);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isAccountLocked(Long userId) {
        Optional<User> userOptional = repository.findById(userId);
        return userOptional.map(User::getLocked_account).orElse(false);
    }

}
