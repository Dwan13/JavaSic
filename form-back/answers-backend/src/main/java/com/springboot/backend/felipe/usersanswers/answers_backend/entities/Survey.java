package com.springboot.backend.felipe.usersanswers.answers_backend.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import static jakarta.persistence.GenerationType.*;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Id;

@Entity
@Table(name = "surveys")
public class Survey {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @NotEmpty
    @Email
    private String email;

    @NotBlank
    private Integer document_number;

    @NotEmpty
    private String comments;

    @NotBlank
    private String response_date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false) // Especificamos la clave foránea
    @JsonBackReference
    private User user;

    @ManyToOne
    @JoinColumn(name = "brand_id", nullable = false)
    private BrandPC brand;

    // Getters y Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getResponse_date() {
        return response_date;
    }

    public void setResponse_date(String response_date) {
        this.response_date = response_date;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public BrandPC getBrand() {
        return brand;
    }

    public void setBrand(BrandPC brand) {
        this.brand = brand;
    }

    public Integer getDocument_number() {
        return document_number;
    }

    public void setDocument_number(Integer document_number) {
        this.document_number = document_number;
    }
}
