package com.example.bibliostock.model;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "book_Format")
public class Format {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long ID;

	@Column(name = "name")
	private String name;

	//track all inventory items for a format
	@OneToMany(mappedBy = "format", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
	@JsonIgnore
	private Set<BookStock> bookStocks = new HashSet<>();

	public Format() {

	}

	public Format(String name) {
		this.name = name;
	}

	public long getId() {
		return ID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<BookStock> getBookStocks() {
		return bookStocks;
	}

	public void setBookStocks(Set<BookStock> bookStocks) {
		this.bookStocks = bookStocks;
	}
	
	
}
