package com.example.bibliostock.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.bibliostock.model.Book;
import com.example.bibliostock.model.BookCart;
import com.example.bibliostock.model.BookCartRepository;
import com.example.bibliostock.model.BookFormatID;
import com.example.bibliostock.model.BookRepository;
import com.example.bibliostock.model.BookStock;
import com.example.bibliostock.model.BookStockRepository;
import com.example.bibliostock.model.Cart;
import com.example.bibliostock.model.CartRepository;
import com.example.bibliostock.model.CustomerRepository;
import com.example.bibliostock.model.Format;
import com.example.bibliostock.model.FormatRepository;
import com.example.bibliostock.model.Manager;
import com.example.bibliostock.model.ManagerRepository;
import com.example.bibliostock.request.BookFormatRequest;
import com.example.bibliostock.request.BookStockRequest;
import com.example.bibliostock.response.MessageResponse;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
public class CartController {
	@Autowired
	JdbcTemplate jdbcTemplate; //for running manual queries
	
	@Autowired
	CartRepository cartRepo;
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
	
//	@Autowired
//	CustomerRepository customerRepo;
	
	//Get all the carts - could be used from manager's side
	@GetMapping("/carts")
	public ResponseEntity<?> getCarts() {
		try {
			List<Cart> carts = new ArrayList<Cart>();
			cartRepo.findAll().forEach(carts::add);
			return new ResponseEntity<>(carts,HttpStatus.OK);
		} catch (Exception e) {
			MessageResponse exception = new MessageResponse(e.toString());
			return new ResponseEntity<>(exception,HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	//get the items of a card with cartID 
		@GetMapping("/cart/{ID}")
		public ResponseEntity<?> getCartItems(@PathVariable("ID") long ID) {
			try {
				if(cartRepo.findByID(ID).isEmpty()) { //If cartID is invalid
					return new ResponseEntity<>(HttpStatus.NOT_FOUND);
				}
				else { 
					
					List<BookCart> bookCarts = new ArrayList<BookCart>();
					bookCartRepo.findByCartID(ID).forEach(bookCarts::add);

					if(bookCarts.isEmpty()) { //If Cart is empty
						return new ResponseEntity<>(HttpStatus.NO_CONTENT);
					}
					
					return new ResponseEntity<>(cartRepo.findByID(ID).get(),HttpStatus.OK);
//					return new ResponseEntity<>(bookCarts,HttpStatus.OK);
				}
				
			} catch (Exception e) {
				MessageResponse exception = new MessageResponse(e.toString());
				return new ResponseEntity<>(exception,HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		
		@PostMapping("/cart/{ID}")
		public ResponseEntity<?> addCartItem(@PathVariable("ID") long ID, @RequestBody BookFormatRequest bookFormatRequest) {
			try {
				Optional<Cart> cart =cartRepo.findByID(ID);
				Optional<BookStock> referencedBookStock = bookStockRepo.findByIDBookIDAndFormatID(bookFormatRequest.getBookID(),bookFormatRequest.getFormatID());
				
				if(cart.isEmpty()) {
					MessageResponse noCart= new MessageResponse("The cart does not exist.");
					return new ResponseEntity<>(noCart,HttpStatus.NOT_FOUND);
				}
				if(referencedBookStock.isEmpty()) {
					MessageResponse noBookStock = new MessageResponse("The added item is not in the inventory.");
					return new ResponseEntity<>(noBookStock,HttpStatus.NOT_FOUND);
				}
				else {
					
					Cart theCart = cart.get();
					BookStock inventoryItem = referencedBookStock.get();
					BookCart cartItem = new BookCart();
					
					if(inventoryItem.getQuantityInStock()==0) {
						MessageResponse noBookStock = new MessageResponse("The added item out of stock.");
						return new ResponseEntity<>(noBookStock,HttpStatus.NOT_FOUND);
					}
					
					//To check if the item is already in cart
					List<BookCart> addedItems = bookCartRepo.findByCartAndBookStock(theCart,inventoryItem);
					
				
					if(addedItems.isEmpty()) { //Not in cart yet
						cartItem = bookCartRepo.save(new BookCart(theCart,inventoryItem));
						
						//Update Cart's added items
						Set<BookCart> cartItems = theCart.getAddedItems();
						cartItems.add(cartItem);
						theCart.setAddedItems(cartItems);
						
						//update inventory item reference
						Set<BookCart> bookstockCarts = inventoryItem.getBooksInCart();
						bookstockCarts.add(cartItem);
						inventoryItem.setBooksInCart(bookstockCarts);
					}
					else { //already in cart
						cartItem = addedItems.get(0);
						cartItem.setUnitQuantity(cartItem.getUnitQuantity()+1); //just increment unit quantity
					}
					
					//Update cart's total quantity and price items 
					theCart.setQuantity(theCart.getQuantity()+1);
					theCart.setTotal(theCart.getTotal() + inventoryItem.getSellingPrice());
					cartRepo.save(theCart);
					
				
					return new ResponseEntity<>(theCart,HttpStatus.CREATED);
//					return new ResponseEntity<>(cartItem,HttpStatus.CREATED);
				}
			} catch (Exception e) {
				System.out.println(e);
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
	
}