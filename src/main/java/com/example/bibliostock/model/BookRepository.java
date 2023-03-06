package com.example.bibliostock.model;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
	List<Book> findByTitle(String title);
	List<Book> findByISBN(String isbn);
	List<Book> findByPublicationDate(LocalDate publication);
	Optional<Book> findById(long id);
	Optional<Book> findByBookStocksBookIDAndBookStocksFormatID(long bookID, long formatID);
	
}
	
