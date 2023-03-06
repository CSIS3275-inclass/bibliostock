package com.example.bibliostock.model;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="customers")
@DiscriminatorValue("0") //isManager in users Table
public class Customer extends User{
	@Column(name="defaultAddress")
	private String defaultAddress;
	
	//A Customer only has one favorite
	@OneToOne(mappedBy="customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JsonProperty("favorite")
	private Favorite favorite;
	
	//One Customer only has one cart
	@OneToOne(mappedBy="customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JsonProperty("cart")
	private Cart cart;
	

	public Customer() {
		super();
	}

	public Customer(String username, String password, String email, String defaultAddress) {
		super(username, password, email);
		this.defaultAddress=defaultAddress;
		this.cart=new Cart(this);
	}

	public String getDefaultAddress() {
		return defaultAddress;
	}

	public void setDefaultAddress(String defaultAddress) {
		this.defaultAddress = defaultAddress;
	}
	
	public void addToFavorite(Book book) {
		if(favorite == null) {
			favorite = new Favorite(this);
		}
		
		Set<Book> favorites = this.favorite.getBooks();
		favorites.add(book);
		favorite.addBook(book);
	}
	public Cart getCart() {
		return cart;
	}
	
	public void setCart(Cart cart) {
		this.cart = cart;
	}

}
