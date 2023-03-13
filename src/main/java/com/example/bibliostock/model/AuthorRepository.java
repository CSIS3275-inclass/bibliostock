package com.example.bibliostock.model;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Author, Long> {
	Optional<Author> findByName(String name);
	Optional<Author> findById(long aid); 
}
