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
import com.example.bibliostock.model.BookRepository;
import com.example.bibliostock.model.Genre;
import com.example.bibliostock.model.GenreRepository;




@CrossOrigin(origins = "http://localhost:8081") // used in vue.js
@RestController
@RequestMapping("/api")
public class HomepageController {
	
	@Autowired
	BookRepository bookRepo;
	
	@Autowired
	AuthorRepository authorRepo;
	
	@Autowired
	GenreRepository genreRepo;
	
	
	
	//To display all Books on the homepage
	@GetMapping("/homepage")
	public ResponseEntity<List<Book>> getAllBooks() {
		try {
			
			List<Book> books = new ArrayList<Book>();
			
			bookRepo.findAll().forEach(books::add);
			
			if (books.isEmpty()) {
				return new ResponseEntity(HttpStatus.NO_CONTENT);
			}

			return new ResponseEntity(books, HttpStatus.OK);

		}catch (Exception e) {
			return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	//to display a specified book with a given id
	@GetMapping("/homepage/{id}")
	public ResponseEntity<Book> getBookById(
			@PathVariable Long id){
		try {
			Optional<Book> books = bookRepo.findById(id);
			if(books.isPresent()) {
				return new ResponseEntity<>(books.get(),HttpStatus.OK);
			}
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			
		}catch(Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	

	// To display all Authors from homepage
	@GetMapping("/homepage/authors")
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
	
	
	//Display authors with name
	@GetMapping("/homepage/authors/{authorName}")
	public ResponseEntity<Set<Book>> getAuthorByName(@PathVariable String authorName){
		
		try {
			Set<Book> books = new HashSet<>();
			if(authorName != null) {
				List<Book> booksbyAuthor = bookRepo.findByAuthorName(authorName);
				books.addAll(booksbyAuthor);
			}else {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(books,HttpStatus.OK);
		}catch(Exception e) {
			return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
		}
		
	}
	
	
	//Display all the genres
	@GetMapping("/homepage/genres")
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
	
	//Search by genre
	@GetMapping("/homepage/genresearch")
	public ResponseEntity<Set<Book>> searchBooksByGenre(@RequestParam(required = false) String genreName,@RequestParam(required = false) String bookTitle){
		
		try {
			Set<Book> books = new HashSet<>();
		if (genreName != null && bookTitle != null) {
			List<Book> booksByTitle = bookRepo.findByTitleContainingIgnoreCaseAndGenresNameContainingIgnoreCase(bookTitle,genreName);
			books.addAll(booksByTitle);
			
		}else if(genreName != null) {
			List<Book> booksbyGenre = bookRepo.findByGenreName(genreName);
			books.addAll(booksbyGenre);
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
	
	

	

}
