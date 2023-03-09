package com.example.bibliostock.model;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name="managers")
@DiscriminatorValue("1") //isManager in User Table
public class Manager extends User{
	//Not all manager are admin
	@Column(name = "isAdmin")
	private Boolean isAdmin;
	
	//an inventory item can be added by a manager
	//a manager can add multiple inventory items
	@OneToMany(mappedBy="manager",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JsonIgnore
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
