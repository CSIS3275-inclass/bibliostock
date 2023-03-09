package com.example.bibliostock.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
	List<Favorite> findByCustomer(Customer customer);
}
