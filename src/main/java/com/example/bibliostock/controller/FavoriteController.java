package com.example.bibliostock.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.bibliostock.model.AuthorRepository;
import com.example.bibliostock.model.Book;
import com.example.bibliostock.model.BookCart;
import com.example.bibliostock.model.BookCartRepository;
import com.example.bibliostock.model.BookRepository;
import com.example.bibliostock.model.BookStock;
import com.example.bibliostock.model.BookStockRepository;
import com.example.bibliostock.model.Cart;
import com.example.bibliostock.model.Favorite;
import com.example.bibliostock.model.FavoriteRepository;
import com.example.bibliostock.model.FormatRepository;
import com.example.bibliostock.model.GenreRepository;
import com.example.bibliostock.model.ManagerRepository;
import com.example.bibliostock.request.BookFormatRequest;
import com.example.bibliostock.response.MessageResponse;

import com.example.bibliostock.model.Customer;
import com.example.bibliostock.model.CustomerRepository;



@CrossOrigin(origins = "http://localhost:8081") // used in vue.js
@RestController
@RequestMapping("/api")
public class FavoriteController {
	
	@Autowired
	JdbcTemplate jdbcTemplate; 
	
	@Autowired
	FavoriteRepository favRepo;
	
	@Autowired
	CustomerRepository customerRepo;
	

	@Autowired
	BookRepository bookRepo;
	@Autowired
	FormatRepository formatRepo;
	@Autowired
	BookCartRepository bookCartRepo;
	@Autowired
	BookStockRepository bookStockRepo;
	@Autowired
	ManagerRepository managerRepo;
	
	
	//Get all the favorite
		@GetMapping("/favorites")
		public ResponseEntity<?> getFavourites() {
			try{
				List<Favorite> favorites = new ArrayList<Favorite>();
				favRepo.findAll().forEach(favorites::add);
				return new ResponseEntity<>(favorites, HttpStatus.OK);
			} catch (Exception e) {
				MessageResponse exception = new MessageResponse(e.toString());
				return new ResponseEntity<>(exception,HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		
		
		//get the items of a favorite with FavoriteID 
				@GetMapping("/favorites/{ID}/{bookid}")
				public ResponseEntity<?> getFavoritesItems(@PathVariable("ID") long ID, @PathVariable("bookid") long bookid) {
					try {
						
						Optional<Customer> customer =customerRepo.findByID(ID);
						if(customerRepo.findByID(ID).isEmpty()) { 
							return new ResponseEntity<>(HttpStatus.NOT_FOUND);
						}
						else { 
							 
							
							Favorite theFav = customer.get().getFavorite();
							
							List<Book> book = bookRepo.findByFavoritesAndID(theFav, bookid);
							
							if(book.isEmpty()) {
								MessageResponse noCart= new MessageResponse("The book not found");
								return new ResponseEntity<>(noCart,HttpStatus.NOT_FOUND);
							} else {
								
								return new ResponseEntity<>(book.get(0),HttpStatus.OK);
								
							}
														
						}
						
					} catch (Exception e) {
						MessageResponse exception = new MessageResponse(e.toString());
						return new ResponseEntity<>(exception,HttpStatus.INTERNAL_SERVER_ERROR);
					}
				}
				
				
				//Delete Cart item
				@DeleteMapping("/favorite/{ID}/{bookid}")
				public ResponseEntity<?> deleteCartItem(@PathVariable("ID") long ID, @PathVariable("bookid") long bookid) {
					try {
						Optional<Customer> customer =customerRepo.findByID(ID);
				
						if(customer.isEmpty()) {
							MessageResponse noCart= new MessageResponse("The customer donot exist.");
							return new ResponseEntity<>(noCart,HttpStatus.NOT_FOUND);
						} else {
								
						Favorite theFav = customer.get().getFavorite();
						
						String deleteBook = "DELETE FROM FAVORITE_BOOK WHERE BOOKID = ? AND FAVORITEID = ?;";
						jdbcTemplate.update(deleteBook, bookid, theFav.getID());
						
						
						//if(book.isEmpty())
						//{
						//	MessageResponse noBook= new MessageResponse("The book is not found inside Favorite");
						//	return new ResponseEntity<>(noBook,HttpStatus.NOT_FOUND);
						//}
						
											
						//Set<Book> books = new HashSet<>();
						
						//books.addAll(theFav.getBooks());
						
						//books.remove(book.get(0));
						
						//theFav.setBooks(books);
						
						//favRepo.save(theFav);				
						
					
						return new ResponseEntity<>(new MessageResponse("deleted!"),HttpStatus.NO_CONTENT);
//						return new ResponseEntity<>(theCart,HttpStatus.NO_CONTENT);
						
					}
					}catch (Exception e) {
						MessageResponse exception = new MessageResponse(e.toString());
						return new ResponseEntity<>(exception,HttpStatus.INTERNAL_SERVER_ERROR);
					}
				}
					
				
				
				
	
}
	

	
