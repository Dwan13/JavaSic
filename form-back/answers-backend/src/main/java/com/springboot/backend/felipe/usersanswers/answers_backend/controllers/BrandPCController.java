package com.springboot.backend.felipe.usersanswers.answers_backend.controllers;

import com.springboot.backend.felipe.usersanswers.answers_backend.entities.BrandPC;
import com.springboot.backend.felipe.usersanswers.answers_backend.services.BrandPCService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/brands")
@CrossOrigin(origins = "http://localhost:4200")
public class BrandPCController {

    @Autowired
    private BrandPCService brandPCService;

    @GetMapping
    public List<BrandPC> getAllBrands() {
        return brandPCService.findAll();
    }
}
