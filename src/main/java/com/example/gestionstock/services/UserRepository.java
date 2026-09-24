package com.example.gestionstock.services;

import com.example.gestionstock.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findUserByEmail(String email);

    boolean existsByEmail(String email);

    User getUsersByEmail(String email);

    User getUserByEmail(String email);

    Optional<User> findByEmail(String email);
}