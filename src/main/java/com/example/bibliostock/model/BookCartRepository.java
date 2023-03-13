package com.example.bibliostock.model;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BookCartRepository extends JpaRepository<BookCart, Long> {
	Optional<BookCart> findByID(long ID);
	Optional<BookCart> findByBookStockBookIDAndBookStockFormatIDAndCartID(long bookID,long formatID, long cartID);
	List<BookCart> findByCartAndBookStock(Cart cart, BookStock bookStock);
	List<BookCart> findByCart(Cart cart);
	List<BookCart> findByCartID(long ID);
}
