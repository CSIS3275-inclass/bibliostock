package com.example.bibliostock.model;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FormatRepository extends JpaRepository<Format, Long> {
	List<Format> findByName(String name);
	Optional<Format> findById(long ID);
}
