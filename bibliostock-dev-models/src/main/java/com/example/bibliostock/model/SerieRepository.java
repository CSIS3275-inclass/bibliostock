package com.example.bibliostock.model;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SerieRepository extends JpaRepository<Serie, Long> {
	List<Serie> findByName(String name);
	Optional<Serie> findByID(long ID);
	Optional<Serie> findByBooks(Book book);
}
