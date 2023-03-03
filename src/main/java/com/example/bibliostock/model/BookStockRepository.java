package com.example.bibliostock.model;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BookStockRepository extends JpaRepository<BookStock, BookFormatID> {
	Optional<BookStock> findByBookFormat(BookFormatID id);
	
}
