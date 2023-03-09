package com.example.bibliostock.model;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
	@Autowired
	List<Customer> findByUsername(String username);
	List<Customer> findByEmail (String email);
	Optional<Customer> findByID(long id);
}
