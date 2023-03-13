package com.example.bibliostock.model;

import java.util.List;
import java.util.Optional;


import org.springframework.data.jpa.repository.JpaRepository;

public interface BookStockRepository extends JpaRepository<BookStock, BookFormatID> {
	Optional<BookStock> findByIDBookIDAndFormatID(long bookId,long formatID);
	Optional<BookStock> findByBookIDAndFormatID(long bookId,long formatID);
	Optional<BookStock> findByBookAndFormat(Book book,Format format);
	List<BookStock> findByBookID(long bookID);
	List<BookStock> findByFormatID(long bookID);
	List<BookStock> findByManagerID(long managerID);
}
