package com.example.bibliostock.model;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookRepository extends JpaRepository<Book, Long> {
	List<Book> findByTitle(String title);
	List<Book> findByTitleContainingIgnoreCase(String title);
	List<Book> findByISBN(String isbn);
	List<Book> findByPublicationDate(LocalDate publication);
	Optional<Book> findById(long id);
	Optional<Book> findByBookStocksBookIDAndBookStocksFormatID(long bookID, long formatID);
	
	@Query("SELECT b FROM Book b JOIN b.authors a WHERE LOWER(a.name) LIKE LOWER(CONCAT('%',:authorName,'%'))")
	List<Book> findByAuthorName(@Param("authorName") String authorName);
	
	
	
	
}
	
