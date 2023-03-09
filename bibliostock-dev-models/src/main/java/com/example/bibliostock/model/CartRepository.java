package com.example.bibliostock.model;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
	Optional<Cart> findByID(long ID);
	Optional<Cart> findByCustomer(Customer customer);
}
