package com.example.bibliostock.controller;

import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.bibliostock.model.BookCart;
import com.example.bibliostock.model.BookCartRepository;
import com.example.bibliostock.model.BookRepository;
import com.example.bibliostock.model.BookStock;
import com.example.bibliostock.model.BookStockRepository;
import com.example.bibliostock.model.Cart;
import com.example.bibliostock.model.CartRepository;
import com.example.bibliostock.model.FormatRepository;
import com.example.bibliostock.model.ManagerRepository;
import com.example.bibliostock.request.BookFormatRequest;
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
			}
			
		} catch (Exception e) {
			MessageResponse exception = new MessageResponse(e.toString());
			return new ResponseEntity<>(exception,HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	//Add an item to a Cart with cart ID, and (bookID + formatID) for inventory item
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
			}
		} catch (Exception e) {
			MessageResponse exception = new MessageResponse(e.toString());
			return new ResponseEntity<>(exception,HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
// Update a cart item quantity
	@PutMapping("/cart/{ID}")
	public ResponseEntity<?> updateCartItem(@PathVariable("ID") long ID,
			@RequestParam("quantity") int quantity,
			@RequestBody BookFormatRequest bookFormatRequest) {
		try {
			Optional<Cart> cart =cartRepo.findByID(ID);
			if(cart.isEmpty()) {
				MessageResponse noCart= new MessageResponse("The cart does not exist.");
				return new ResponseEntity<>(noCart,HttpStatus.NOT_FOUND);
			}
			else {
				Optional<BookCart> referencedCartItem = bookCartRepo.findByBookStockBookIDAndBookStockFormatIDAndCartID(bookFormatRequest.getBookID(),bookFormatRequest.getFormatID(),ID);
				if(referencedCartItem.isEmpty()) {
					MessageResponse noBookStock = new MessageResponse("The cart item to update is not in the cart.");
					return new ResponseEntity<>(noBookStock,HttpStatus.NOT_FOUND);
				}
				
				BookStock referencedInventoryItem =bookStockRepo.findByBookIDAndFormatID(bookFormatRequest.getBookID(), bookFormatRequest.getFormatID()).get();
				BookCart cartItem = referencedCartItem.get();
				Cart theCart = cart.get();
				
				//quantity in stock for the cart item
				final int MAX_QUANTITY = referencedInventoryItem.getQuantityInStock();
			
				if(quantity<1||quantity>MAX_QUANTITY)
				{
					MessageResponse invalidQuantity= new MessageResponse("The quantity set is invalid, there are only "+ MAX_QUANTITY+" of this item available in stock.");
					return new ResponseEntity<>(invalidQuantity,HttpStatus.CONFLICT);
				}
				else {
					int currentQuantity = cartItem.getUnitQuantity();
					//Update cart item unit quantity 
					cartItem.setUnitQuantity(quantity);
					
					//update entire cart's total quantity and price
					int quantityUpdate = quantity - currentQuantity;
					double priceUpdate = quantityUpdate * referencedInventoryItem.getSellingPrice();
					theCart.setQuantity(theCart.getQuantity()+quantityUpdate);
					theCart.setTotal(theCart.getTotal()+ priceUpdate);
					
					bookCartRepo.save(cartItem);
					cartRepo.save(theCart);
					
					return new ResponseEntity<>(theCart, HttpStatus.OK);
				}
			}
			
		} catch (Exception e) {
			MessageResponse exception = new MessageResponse(e.toString());
			return new ResponseEntity<>(exception,HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
//Delete Cart item
	@DeleteMapping("/cart/{ID}")
	public ResponseEntity<?> deleteCartItem(@PathVariable("ID") long ID,
			@RequestBody BookFormatRequest bookFormatRequest) {
		try {
			Optional<Cart> cart =cartRepo.findByID(ID);
			Optional<BookCart> referencedCartItem = bookCartRepo.findByBookStockBookIDAndBookStockFormatIDAndCartID(bookFormatRequest.getBookID(),bookFormatRequest.getFormatID(),ID);
			if(cart.isEmpty()) {
				MessageResponse noCart= new MessageResponse("The cart does not exist.");
				return new ResponseEntity<>(noCart,HttpStatus.NOT_FOUND);
			}
			else if(referencedCartItem.isEmpty()) {
					MessageResponse noBookStock = new MessageResponse("The cart item to delete is not in the cart.");
					return new ResponseEntity<>(noBookStock,HttpStatus.NOT_FOUND);
			}
			
			BookStock referencedInventoryItem =bookStockRepo.findByBookIDAndFormatID(bookFormatRequest.getBookID(), bookFormatRequest.getFormatID()).get();
			BookCart cartItem = referencedCartItem.get();
			Cart theCart = cart.get();
			
			//update cart total quantity and price
			theCart.setQuantity(theCart.getQuantity()-cartItem.getUnitQuantity());
			double itemTotalPrice = referencedInventoryItem.getSellingPrice() * cartItem.getUnitQuantity();
			theCart.setTotal(theCart.getTotal()-itemTotalPrice);
			
			bookCartRepo.delete(referencedCartItem.get());
			cartRepo.save(theCart);
		
			return new ResponseEntity<>(new MessageResponse("deleted!"),HttpStatus.NO_CONTENT);
			
		} catch (Exception e) {
			MessageResponse exception = new MessageResponse(e.toString());
			return new ResponseEntity<>(exception,HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
