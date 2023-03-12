package com.example.bibliostock.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.hibernate.Hibernate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.bibliostock.request.BookFormatRequest;
import com.example.bibliostock.request.BookStockRequest;
import com.example.bibliostock.response.MessageResponse;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name="BookStock") //Manages inventoried books (a book of a specific format in the inventory)
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id") //serialize relationship-related fields without causing infinite loop
public class BookStock {

	
	@EmbeddedId
	private BookFormatID ID; //Composite key: bookID and formatID
	
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name="formatID", 
		referencedColumnName = "ID",
		insertable=false, updatable=false) 	//FormatID can no longer be updated when it's added - IT has to be removed to set a different one 
	@MapsId("formatID") //what's in BookFormatID embedded class
	@JsonIgnore
	@JsonProperty("format")
	private Format format;

	
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name="bookID", 
		referencedColumnName = "ID",
		insertable=false, updatable=false) 	//BookID can no longer be updated when it's added - IT has to be removed to set a different one
	@MapsId("bookID") //what's in BookFormatID embedded class
	@JsonIgnore
	@JsonProperty("book")
	private Book book;
	
	//an inventory item can be added by a manager
	//a manager can add multiple inventory items
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name="managerID")
//	@JsonIgnore
	@JsonProperty("manager")
	private Manager manager;
	
	@Column(name="purchasePrice")
	private double purchasePrice;

	@Column(name="sellingPrice")
	private double sellingPrice;
	
	@Column(name="quantityInStock")
	private int quantityInStock;
	
	@Column(name="quantitySold")
	private int quantitySold;
	
	//set to true when Book is discontinued
	@Column(name="isRemoved")
	private Boolean isRemoved;
	
	@Column(name="addedDate")
	@Temporal(TemporalType.DATE)
	private LocalDate addedDate;

	@Column(name="updatedDate")
	@Temporal(TemporalType.DATE)
	private LocalDate updatedDate;
	
	@Column(name="removedDate")
	@Temporal(TemporalType.DATE)
	private LocalDate removedDate;
	
	//a cart can have an instance of inventory item 
	//an inventory item can be in multiple carts
	//CascadeType.ALL so any changes made to the inventory item affects the related cart items
	@OneToMany(mappedBy="bookStock",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JsonIgnore
	private Set<BookCart> booksInCart = new HashSet<>();
	
	
	public BookStock() {
		
	}
	public BookStock(Book book, Format format, double purchasePrice, double sellingPrice, int quantityInStock, Manager manager) {
		
		this.ID=new BookFormatID(book.getID(),format.getId());
		this.purchasePrice = purchasePrice;
		this.sellingPrice = sellingPrice;
		this.quantityInStock = quantityInStock;
		this.quantitySold = 0;
		
		this.manager = manager; 
		//update bookStocks added by manager
		Set<BookStock> bookStock = manager.getBookStock();
		bookStock.add(this);
		manager.setBookStock(bookStock);
		
		this.book=book;
		//update bookStocks in book
		Set<BookStock> bookBookStock = book.getBookStocks();
		bookBookStock.add(this);
		book.setBookStocks(bookBookStock);
		
		this.format=format;
		//update bookStocks in format
		Set<BookStock> formatBookStock = format.getBookStocks();
		formatBookStock.add(this);
		format.setBookStocks(formatBookStock);
		
		addedDate=LocalDate.now();	
		updatedDate=LocalDate.now();
		isRemoved = false;
	}
	
	public void setID(BookFormatID id) {
		this.ID=id;
	}
	public BookFormatID getID() {
		return ID;
	}
	public Format getFormat() {
		return format;
	}


	public Book getBook() {
		return book;
	}

	public Manager getManager() {
		return manager;
	}

	public void setManager(Manager manager) {
		this.manager = manager;
	}
	
	public double getPurchasePrice() {
		return purchasePrice;
	}

	public void setPurchasePrice(double purchasePrice) {
		this.purchasePrice = purchasePrice;
	}

	public double getSellingPrice() {
		return sellingPrice;
	}

	public void setSellingPrice(double sellingPrice) {
		this.sellingPrice = sellingPrice;
	}

	public int getQuantityInStock() {
		return quantityInStock;
	}

	public void setQuantityInStock(int quantityInStock) {
		this.quantityInStock = quantityInStock;
	}

	public int getQuantitySold() {
		return quantitySold;
	}

	public void setQuantitySold(int quantitySold) {
		this.quantitySold = quantitySold;
	}

	public LocalDate getRemovedDate() {
		return removedDate;
	}

	public void setRemovedDate(LocalDate removedDate) {
		this.removedDate = removedDate;
	}
	
	public Boolean getIsRemoved() {
		return isRemoved;
	}

	public void setIsRemoved(Boolean isRemoved) {
		this.isRemoved = isRemoved;
	}

	public LocalDate getAddedDate() {
		return addedDate;
	}

	public void setAddedDate(LocalDate addedDate) {
		this.addedDate = addedDate;
	}
	public LocalDate getUpdatedDate() {
		return updatedDate;
	}
	public void setUpdatedDate(LocalDate updatedDate) {
		this.updatedDate = updatedDate;
	}
	public Set<BookCart> getBooksInCart() {
		return booksInCart;
	}
	public void setBooksInCart(Set<BookCart> booksInCart) {
		this.booksInCart = booksInCart;
	}
	

	
	
}