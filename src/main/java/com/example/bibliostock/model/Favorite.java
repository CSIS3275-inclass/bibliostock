package com.example.bibliostock.model;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="Favorite")
public class Favorite {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long ID;
	
	//A Customer only has one favorite
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name="customerID", nullable=false)
	@JsonIgnore
	private Customer customer;
	
	//One favorite has multiple books
	//A book can be in multiple favorites
	@ManyToMany(mappedBy="favorites", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	//@JsonIgnore
	@JsonProperty("books")
	private Set<Book> books = new HashSet<>();
	
	public long getID() {
		return ID;
	}


	public Favorite() {
		
	}
	
	public Favorite(Customer customer) {
		this.customer = customer;
	}


	public Favorite(Customer customer, Set<Book> books) {
		this.customer = customer;
		this.books = books;
	}


	public Customer getCustomer() {
		return customer;
	}


	public void setCustomer(Customer customer) {
		this.customer = customer;
	}


	public Set<Book> getBooks() {
		return books;
	}


	public void setBooks(Set<Book> books) {
		this.books = books;
	}
	
	public void addBook(Book book) {
		this.books.add(book);
		Set<Favorite> favorites = book.getFavorites();
		favorites.add(this);
		book.setFavorites(favorites);
	}
	
}