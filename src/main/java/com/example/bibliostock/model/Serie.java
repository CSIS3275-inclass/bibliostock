package com.example.bibliostock.model;

import java.util.HashSet;
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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "Serie")
public class Serie {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long ID;
	
	@Column(name = "name")
	private String name;
	
	//A book only has one serie
	//A serie can have multiple books
	@OneToMany(mappedBy = "serie", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JsonIgnore
	@JsonProperty
	private Set<Book> books = new HashSet<>();
	
	
	public Serie() {
		
	}

	public Serie(String name) {
		super();
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public void addBook(Book book) {
		this.books.add(book);
		book.setSerie(this);
	}
	
}
