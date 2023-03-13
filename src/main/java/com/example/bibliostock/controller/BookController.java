package com.example.bibliostock.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.bibliostock.model.AuthorRepository;
import com.example.bibliostock.model.Book;
import com.example.bibliostock.model.BookRepository;
import com.example.bibliostock.model.BookStockRepository;


@CrossOrigin(origins="http://localhost:8081")
@RestController
@RequestMapping("/api")
public class BookController {
	
	@Autowired
	BookRepository bookRepo;
	
	@Autowired
	AuthorRepository authorRepo;
	
	@Autowired
	BookStockRepository bookStockRepo;
	
	// Search books by author name and or title
	@GetMapping("/search")
	public ResponseEntity<Set<Book>> searchBooks(@RequestParam(required = false) String authorName,@RequestParam(required = false) String bookTitle){
		
		try {
			Set<Book> books = new HashSet<>();
		if (authorName != null && bookTitle != null) {
			List<Book> booksbyAuthor = bookRepo.findByAuthorName(authorName);
			List<Book> booksByTitle = bookRepo.findByTitleContainingIgnoreCase(bookTitle);
			booksbyAuthor.addAll(booksByTitle);
			books.addAll(booksbyAuthor);
			
		}else if(authorName != null) {
			List<Book> booksbyAuthor = bookRepo.findByAuthorName(authorName);
			books.addAll(booksbyAuthor);
		}else if(bookTitle !=null) {
			List<Book> booksByTitle = bookRepo.findByTitleContainingIgnoreCase(bookTitle);
			books.addAll(booksByTitle);
		}else {
			books.addAll(bookRepo.findAll());
		}
		return new ResponseEntity<>(books,HttpStatus.OK);
		}catch(Exception e) {
			return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
		}
		
	}
	
	
	//Just Testing - Get all books
	@GetMapping("/book")
	public ResponseEntity<List<Book>> allBooks(){
		List<Book> books = bookRepo.findAll();
		return new ResponseEntity<>(books,HttpStatus.OK);
	}
	

	//Filter books by review
	@GetMapping("/book-review")
	public ResponseEntity<List<Book>> bookStock(@RequestParam(required = true) Double averageReview){
	    List<Book> bookReview = bookRepo.findByAverageReviewGreaterThanEqual(averageReview);
	    System.out.print(bookReview);
	    return new ResponseEntity<>(bookReview,HttpStatus.OK);
	}

	
}