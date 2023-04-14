package com.example.bibliostock.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.bibliostock.model.BookCart;
import com.example.bibliostock.model.BookCartRepository;
import com.example.bibliostock.model.BookRepository;
import com.example.bibliostock.model.BookStock;
import com.example.bibliostock.model.BookStockRepository;
import com.example.bibliostock.model.Cart;
import com.example.bibliostock.model.CartRepository;
import com.example.bibliostock.model.FormatRepository;
import com.example.bibliostock.model.Manager;
import com.example.bibliostock.model.ManagerRepository;
import com.example.bibliostock.request.BookFormatRequest;
import com.example.bibliostock.request.BookStockRequest;
import com.example.bibliostock.response.MessageResponse;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
public class BookStockController {
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
	
	//Get all items in stock
	@GetMapping("/stocks")
	public ResponseEntity<?> getBooksInStock() {
		try {
			List<BookStock> items = new ArrayList<BookStock>();
			bookStockRepo.findAll().forEach(items::add);
			return new ResponseEntity<>(items,HttpStatus.OK);
		} catch (Exception e) {
			MessageResponse exception = new MessageResponse(e.toString());
			return new ResponseEntity<>(exception,HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	//Get bookstock by book id
	@GetMapping("/stocks/book/{ID}")
	public ResponseEntity<?> getBooksInStockById(@PathVariable("ID") long ID) {
		try {
			List<BookStock> bookstocks =bookStockRepo.findByBookID(ID);

			if(bookstocks.isEmpty()) {
				MessageResponse noBookStock= new MessageResponse("The book is not in stock");
				return new ResponseEntity<>(noBookStock,HttpStatus.NOT_FOUND);
			}
			else {
				return new ResponseEntity<>(bookstocks,HttpStatus.OK);
			}
		} catch (Exception e) {
			MessageResponse exception = new MessageResponse(e.toString());
			return new ResponseEntity<>(exception,HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	//Add a new inventory item 
	@PostMapping("/stock")
	public ResponseEntity<?> addBookToStock(@RequestBody BookStockRequest bookStockRequest) {
		try {
			BookFormatRequest bookFormatRequest = bookStockRequest.getBookFormatRequest();
			BookStock bookStock = bookStockRequest.getBookStock();
			
			
			Optional<BookStock> referencedBookStock = bookStockRepo.findByBookIDAndFormatID(bookFormatRequest.getBookID(),bookFormatRequest.getFormatID());
			
			Hibernate.initialize(bookStock.getManager()); //since manager is fetched lazily in bookStock, getManager() needs to be initialized manually to not return null
			List<Manager> manager = managerRepo.findByUsername(bookStock.getManager().getUsername());
			
			if(manager.isEmpty()) {
				MessageResponse invalidManager= new MessageResponse("The manager's username is invalid");
				return new ResponseEntity<>(invalidManager,HttpStatus.NOT_FOUND);
				
			}

			else if(bookRepo.findById(bookFormatRequest.getBookID()).isEmpty()) {
				MessageResponse invalidBook= new MessageResponse("The bookID is invalid");
				return new ResponseEntity<>(invalidBook,HttpStatus.NOT_FOUND);
				
			}
			else if(formatRepo.findById(bookFormatRequest.getFormatID()).isEmpty()) {
				MessageResponse invalidFormat= new MessageResponse("The formatID is invalid");
				return new ResponseEntity<>(invalidFormat,HttpStatus.NOT_FOUND);
				
			}
			else if(referencedBookStock.isPresent()) {
				MessageResponse alreadyAdded= new MessageResponse("The added item is already in the inventory. Did you mean update?");
				return new ResponseEntity<>(alreadyAdded,HttpStatus.NOT_FOUND);
			}
			else {
						
				String insertBookStock = "INSERT INTO BOOK_STOCK (BOOKID,FORMATID,ADDED_DATE,IS_REMOVED,PURCHASE_PRICE,QUANTITY_IN_STOCK,QUANTITY_SOLD,SELLING_PRICE,UPDATED_DATE,MANAGERID) "
													+ "VALUES (?,?,?,?,?,?,?,?,?,?);";
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
				
				jdbcTemplate.update(insertBookStock, bookFormatRequest.getBookID()
													, bookFormatRequest.getFormatID()
													,LocalDate.now().format(formatter)
													,false
													,bookStock.getPurchasePrice()
													,bookStock.getQuantityInStock()
													,0
													,bookStock.getSellingPrice()
													,LocalDate.now().format(formatter)
													,manager.get(0).getID()
						
						);
				
				
				return new ResponseEntity<> (bookStockRepo.findByBookIDAndFormatID(bookFormatRequest.getBookID(), bookFormatRequest.getFormatID())
											,HttpStatus.CREATED);
			}
		} catch (Exception e) {
			MessageResponse exception = new MessageResponse(e.toString());
			return new ResponseEntity<>(exception,HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
