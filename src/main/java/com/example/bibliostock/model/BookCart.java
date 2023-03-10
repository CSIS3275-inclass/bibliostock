package com.example.bibliostock.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="BooKCart")
public class BookCart {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long ID;
	
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name="CartID")
	@JsonIgnore
	@JsonProperty("cart")
	private Cart cart;
	
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name="bookID")
	@JoinColumn(name="formatID")
	@JsonIgnore
	@JsonProperty("bookStock")
	private BookStock bookStock;
	
	@Column(name="unitQuantity")
	private int unitQuantity;
	
	public BookCart() {
		
	}
	public BookCart(Cart cart, BookStock bookStock) {
		super();
		this.cart = cart;
		this.bookStock = bookStock;
		unitQuantity=1;
	}
	public Cart getCart() {
		return cart;
	}
	public BookStock getBookStock() {
		return bookStock;
	}
	public int getUnitQuantity() {
		return unitQuantity;
	}
	public void setUnitQuantity(int unitQuantity) {
		this.unitQuantity = unitQuantity;
	}
	
	
	
	
}
