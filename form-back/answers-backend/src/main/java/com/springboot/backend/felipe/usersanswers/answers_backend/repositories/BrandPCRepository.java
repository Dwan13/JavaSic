package com.springboot.backend.felipe.usersanswers.answers_backend.repositories;

import com.springboot.backend.felipe.usersanswers.answers_backend.entities.BrandPC;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BrandPCRepository extends JpaRepository<BrandPC, Long> {
}
