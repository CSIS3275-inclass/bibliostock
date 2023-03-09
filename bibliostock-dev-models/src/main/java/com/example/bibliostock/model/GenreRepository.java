package com.example.bibliostock.model;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface GenreRepository extends JpaRepository<Genre, Long> {
	List<Genre> findByName(String name);
	Optional<Genre> findById(long ID);
}
