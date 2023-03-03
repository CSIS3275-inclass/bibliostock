package com.example.bibliostock.model;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;


@Entity
@DiscriminatorValue("0") //isManager in User Table
public class Customer extends User{
	@Column(name="defaultAddress")
	private String defaultAddress;
	
	//One Customer only has one favorite
	@OneToOne(mappedBy="customer")
	private Favorite favorite;
	
	
	
	public Customer() {
		super();
	}

	public Customer(String username, String password, String email, String defaultAddress) {
		super(username, password, email);
		this.defaultAddress=defaultAddress;
	}

	public String getDefaultAddress() {
		return defaultAddress;
	}

	public void setDefaultAddress(String defaultAddress) {
		this.defaultAddress = defaultAddress;
	}

	public Favorite getFavorite() {
		return favorite;
	}

	public void setFavorite(Favorite favorite) {
		this.favorite = favorite;
	}
	
	public void addFavoriteBook(Book book) {
		this.favorite.addBook(book);
	}
}
