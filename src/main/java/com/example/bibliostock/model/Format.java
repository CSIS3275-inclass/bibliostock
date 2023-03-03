package com.example.bibliostock.model;

import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name="Format")
public class Format {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Column(name="name")
	private String name;
	
	//One format can be in multiple Books 
	@OneToMany(mappedBy="format")
	private Set<BookStock> bookStocks; 

	public Format(String name) {
		this.name = name;
	}
	
	

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<BookStock> getbookStocks() {
		return bookStocks;
	}

	public void setbookStocks(Set<BookStock> bookStocks) {
		this.bookStocks = bookStocks;
	}
	
	
	
}
