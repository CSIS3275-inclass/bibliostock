package com.example.bibliostock.model;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ManagerRepository extends JpaRepository<Manager, Long> {
	List<Manager> findByUsername(String username);
	List<Manager> findByEmail (String email);
	List<Manager> findByisAdmin(Boolean isAdmin);
	Optional<Manager> findById(long id);
}
