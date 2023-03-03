package com.example.bibliostock.model;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name="BookStock")
public class BookStock {
	@EmbeddedId
	private BookFormatID ID;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name="bookID",
				nullable=false,
				referencedColumnName = "bookID")
	@MapsId("bookID") //what's in BookFormatID embedded class
	private Book book;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name="formatID", 
				nullable=false,
				referencedColumnName = "formatID")
	@MapsId("formatID") //what's in BookFormatID embedded class
	private Format format;
	
	@Column(name="purchasePrice")
	private float purchasePrice;

	@Column(name="sellingPrice")
	private float sellingPrice;
	
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
	
	@Column(name="removedDate")
	@Temporal(TemporalType.DATE)
	private LocalDate removedDate;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name="managerID")
	private Manager manager;
	
	@ManyToMany(mappedBy="bookItems")
	private Set<Cart> carts = new HashSet<>();
	
	
	
	public BookStock() {
		
	}
	
	
	public BookStock(Book book, Format format, float purchasePrice, float sellingPrice, int quantityInStock, int quantitySold, Manager manager) {
		
		this.ID=new BookFormatID(book.getID(),format.getId());
		this.purchasePrice = purchasePrice;
		this.sellingPrice = sellingPrice;
		this.quantityInStock = quantityInStock;
		this.quantitySold = quantitySold;
		
		this.manager = manager;
		Set<BookStock> bookStock = manager.getBookStock();
		bookStock.add(this);
		manager.setBookStock(bookStock);
	}
	
	public Book getBook() {
	return book;
}

	public void setBook(Book book) {
		this.book = book;
		Set<BookStock> bookStocks = book.getBookStocks();
		bookStocks.add(this);
		book.setBookStocks(bookStocks);
	}
	
	public Format getFormat() {
	return format;
	}

	public void setFormat(Format format) {
		this.format = format;
		Set<BookStock> bookStocks= format.getbookStocks();
		bookStocks.add(this);
		format.setbookStocks(bookStocks);
	}
	
	public float getPurchasePrice() {
		return purchasePrice;
	}

	public void setPurchasePrice(float purchasePrice) {
		this.purchasePrice = purchasePrice;
	}

	public float getSellingPrice() {
		return sellingPrice;
	}

	public void setSellingPrice(float sellingPrice) {
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

	public Manager getManager() {
		return manager;
	}

	public void setManager(Manager manager) {
		this.manager = manager;
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


	public Set<Cart> getCarts() {
		return carts;
	}


	public void setCarts(Set<Cart> carts) {
		this.carts = carts;
	}
	
	
	
	
}

