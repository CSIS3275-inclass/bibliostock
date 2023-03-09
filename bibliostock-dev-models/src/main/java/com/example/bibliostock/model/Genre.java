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
import jakarta.persistence.JoinTable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name="genres")
public class Genre {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long ID;
	
	@Column(name="name")
	private String name;
	
	@Column(name = "description")
	private String description;
	
	//multiple books can have same genre
	//One book can have multiple genres
	@ManyToMany
	@JoinTable(
			name="GenreBook",
			joinColumns=@JoinColumn(name="genreID"),
			inverseJoinColumns=@JoinColumn(name="bookID")
			)
	@JsonIgnore
	private Set<Book> books = new HashSet<>();

	public Genre() {
		
	}
	
	public Genre(String name) {
		this.name=name;
	}
	public Genre(String name, String description) {
		this.name = name;
		this.description = description;
	}
	
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public void addBook(Book book)
	{
		this.books.add(book);
		Set<Genre> genres= book.getGenres();
		genres.add(this);
		book.setGenres(genres);
	}
}
