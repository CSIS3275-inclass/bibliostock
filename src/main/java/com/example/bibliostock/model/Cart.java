package com.example.bibliostock.model;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="Cart")
public class Cart {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Column(name="total")
	private int total = 0;
	
	@Column(name="quantity")
	private int quantity = 0;
	
	@OneToOne
	@JoinColumn(name="customerID")
	private Customer customer;
	
	@ManyToMany
	@JoinTable(
			name="BookCart",
			joinColumns = @JoinColumn(name="cartID"),
			inverseJoinColumns = {
					@JoinColumn(name="bookID"),
					@JoinColumn(name="formatID")})
	private Set<BookStock> bookItems = new HashSet<>();
	
	public Cart() {
		
	}

	public Cart(Customer customer) {
		this.customer = customer;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Set<BookStock> getBookItems() {
		return bookItems;
	}

	public void setBookItems(Set<BookStock> bookItems) {
		this.bookItems = bookItems;
	}
	
	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public void addBook(BookStock bookStock) {
		bookItems.add(bookStock);
		quantity++;
		Set<Cart> carts = bookStock.getCarts();
		carts.add(this);
		bookStock.setCarts(carts);
		
	}
	
	
	
	
	
}
