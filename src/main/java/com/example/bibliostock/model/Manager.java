package com.example.bibliostock.model;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
@DiscriminatorValue("1") //isManager in User Table
public class Manager extends User{
	
	@Column(name = "isAdmin")
	private Boolean isAdmin;
	
	@OneToMany(mappedBy="manager")
	private Set<BookStock> bookStock = new HashSet<>();
	
	
	
	
	public Manager() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Manager(String username, String password, String email, Boolean isAdmin) {
		super(username, password, email);
		this.isAdmin=isAdmin;
	}

	public Boolean getIsAdmin() {
		return isAdmin;
	}

	public void setIsAdmin(Boolean isAdmin) {
		this.isAdmin = isAdmin;
	}
	
	public Set<BookStock> getBookStock() {
		return bookStock;
	}

	public void setBookStock(Set<BookStock> bookStock) {
		this.bookStock = bookStock;
	}
	
	
}
