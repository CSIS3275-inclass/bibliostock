package com.example.bibliostock.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.transaction.Transactional;

@Entity
@Table(name="Cart")
public class Cart { //Already initialized when customer is initiallized
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long ID;
	
	@Column(name="total")
	private double total;
	
	@Column(name="quantity")
	private int quantity;
	
	//One Customer only has one cart
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name="customerID")
	@JsonIgnore
	private Customer customer;
	
	//One Cart has many books
	//same books are in many carts
	@OneToMany(mappedBy="cart",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JsonIgnore
	@JsonProperty("addedItems")
	private Set<BookCart> addedItems = new HashSet<>();
	
	public Cart() {
		
	}

	public Cart(Customer customer) {
		this.customer = customer;
//		customer.setCart(this);
		total=0;
		quantity=0;
	
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	
	public Set<BookCart> getAddedItems() {
		return addedItems;
	}

	public void setAddedItems(Set<BookCart> addedItems) {
		this.addedItems = addedItems;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	public void addBook(BookCartRepository bookCartRepo,BookStock bookStock) {
		List<BookCart> existingItems = bookCartRepo.findByCartAndBookStock(this, bookStock);
		Set<BookCart> carts = bookStock.getBooksInCart();
		
		if(existingItems.isEmpty()) {
			BookCart newItem = new BookCart(this,bookStock);
			addedItems.add(newItem);
			carts.add(newItem);
			bookCartRepo.save(newItem);
		}
		else {
			BookCart existingItem = existingItems.get(0);
			existingItem.setUnitQuantity(existingItem.getUnitQuantity()+1);
		}
		quantity++;
//		bookStock.setBooksInCart(carts);
		
	}
	
	
	
	
	
}
