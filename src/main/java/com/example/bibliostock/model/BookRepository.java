package com.example.bibliostock.model;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
	List<Book> findByTitle(String title);
	List<Book> findByISBN(String isbn);
	List<Book> findByAuthorId(Author author);
	List<Book> findBySerie(Serie serie);
	List<Book> findByGenre(Genre genre);
	List<Book> findByPublicationDate(LocalDate publication);
	Optional<Book> findById(Long id);
	
}
