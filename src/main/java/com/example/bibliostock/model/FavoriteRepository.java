package com.example.bibliostock.model;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
	Optional<Favorite> findByCustomer(Customer customer);
	Optional<Favorite> findByID(long ID);
	
	
}
