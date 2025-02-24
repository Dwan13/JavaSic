package com.springboot.backend.felipe.usersanswers.answers_backend.models;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public class SurveyRequest {

    @NotBlank
    private Integer document_number;

    @NotEmpty
    private String comments;
    @NotBlank
    private String response_date;

    @NotEmpty
    @Email
    private String email;

    private Long brandId;
    private Long userId;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getDocument_number() {
        return document_number;
    }

    public void setDocument_number(Integer document_number) {
        this.document_number = document_number;
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

    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

}
