package com.springboot.backend.felipe.usersanswers.answers_backend.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.persistence.Id;

@Entity
@Table(name = "brand_pc")
public class BrandPC {
    @Id
    private Long id;

    @NotBlank
    private String name;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}