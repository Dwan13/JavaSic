package com.springboot.backend.felipe.usersanswers.answers_backend.services;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class CustomUserDetails extends User {

    private final Long id; // Agregamos el ID del usuario

    public CustomUserDetails(Long id, String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, true, true, true, true, authorities);
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
