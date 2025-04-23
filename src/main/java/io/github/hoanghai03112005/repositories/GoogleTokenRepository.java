package io.github.hoanghai03112005.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import io.github.hoanghai03112005.models.GoogleToken;

public interface GoogleTokenRepository extends JpaRepository<GoogleToken, Long>{
    Optional<GoogleToken> findTopByOrderByIdDesc();
}
