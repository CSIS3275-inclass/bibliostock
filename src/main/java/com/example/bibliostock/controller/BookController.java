package com.example.bibliostock.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.bibliostock.model.Author;
import com.example.bibliostock.model.AuthorRepository;
import com.example.bibliostock.model.Book;
import com.example.bibliostock.model.BookCart;
import com.example.bibliostock.model.BookRepository;
import com.example.bibliostock.model.BookStockRepository;
import com.example.bibliostock.model.Format;
import com.example.bibliostock.model.FormatRepository;
import com.example.bibliostock.model.Genre;
import com.example.bibliostock.model.GenreRepository;
import com.example.bibliostock.response.MessageResponse;


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
	
	@Autowired
	FormatRepository formatRepo;
	
	@Autowired
	GenreRepository genreRepo;
	
	//get Book by ID
	@GetMapping("/book/{ID}")
	public ResponseEntity<?> getBookByID(@PathVariable("ID") long ID) {
		try {
			Optional<Book> book = bookRepo.findByID(ID);
			if(book.isEmpty()) { //If cartID is invalid
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			else { 
				return new ResponseEntity<>(book.get(),HttpStatus.OK);
			}
			
		} catch (Exception e) {
			MessageResponse exception = new MessageResponse(e.toString());
			return new ResponseEntity<>(exception,HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	//get format by Id 
	@GetMapping("/book/format/{ID}")
	public ResponseEntity<?> getFormatByID(@PathVariable("ID") long ID) {
		try {
			Optional<Format> format = formatRepo.findById(ID);
			if(format.isEmpty()) { //If cartID is invalid
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			else { 
				return new ResponseEntity<>(format.get(),HttpStatus.OK);
			}
			
		} catch (Exception e) {
			MessageResponse exception = new MessageResponse(e.toString());
			return new ResponseEntity<>(exception,HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	
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

	
	// To display all Authors
	@GetMapping("/books/authors")
	public ResponseEntity<List<Author>> getAllAuthors() {
		try {
			List<Author> authors = new ArrayList<Author>();

			authorRepo.findAll().forEach(authors::add);
					

			if (authors.isEmpty()) {
				return new ResponseEntity(HttpStatus.NO_CONTENT);
			}

			return new ResponseEntity(authors, HttpStatus.OK);

		}catch (Exception e) {
			return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	//Display all the genres
	@GetMapping("/books/genres")
	public ResponseEntity<List<Genre>> getAllGenre() {
		try {
			List<Genre> genres = new ArrayList<Genre>();

			genreRepo.findAll().forEach(genres::add);
					

			if (genres.isEmpty()) {
				return new ResponseEntity(HttpStatus.NO_CONTENT);
			}

			return new ResponseEntity(genres, HttpStatus.OK);

		}catch (Exception e) {
			return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	

	//Filter books by review
	@GetMapping("/book-review")
	public ResponseEntity<List<Book>> bookStock(@RequestParam(required = true) Double averageReview){
	    List<Book> bookReview = bookRepo.findByAverageReviewGreaterThanEqual(averageReview);
	    System.out.print(bookReview);
	    return new ResponseEntity<>(bookReview,HttpStatus.OK);
	}
	
}