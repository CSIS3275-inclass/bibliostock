package com.example.bibliostock.model;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
	List<User> findByUsername(String username);
	List<User> findByEmail (String email);
	Optional<User> findById(Long id);
}
