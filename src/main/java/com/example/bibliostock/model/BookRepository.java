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
	Optional<Book> findByISBN(String isbn);
	List<Book> findByPublicationDate(LocalDate publication);
	Optional<Book> findById(long id);
	Optional<Book> findByBookStocksBookIDAndBookStocksFormatID(long bookID, long formatID);
	List<Book> findByFavoritesAndID(Favorite favorite, long id);
	List<Book> findByFavorites(Favorite favorite);
	
	@Query("SELECT b FROM Book b JOIN b.genres a WHERE LOWER(a.name) LIKE LOWER(CONCAT('%',:genreName,'%'))")
	List<Book> findByGenreName(@Param("genreName") String genreName);
	
	List<Book> findByTitleContainingIgnoreCaseAndGenresNameContainingIgnoreCase(String title, String genre);
	
	@Query("SELECT b FROM Book b JOIN b.authors a WHERE LOWER(a.name) LIKE LOWER(CONCAT('%',:authorName,'%'))")
	List<Book> findByAuthorName(@Param("authorName") String authorName);
	
	List<Book> findByAverageReviewGreaterThanEqual(Double averageReview);
	
}
	
