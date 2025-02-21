package com.springboot.backend.felipe.usersanswers.answers_backend.services;

import com.springboot.backend.felipe.usersanswers.answers_backend.entities.BrandPC;
import com.springboot.backend.felipe.usersanswers.answers_backend.repositories.BrandPCRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BrandPCService {

    @Autowired
    private BrandPCRepository brandPCRepository;

    public List<BrandPC> findAll() {
        return brandPCRepository.findAll();
    }
}
