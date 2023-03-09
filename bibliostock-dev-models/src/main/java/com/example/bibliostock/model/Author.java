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
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name="authors")
public class Author {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long ID;
	
	private String name;
	
	private String statement;
	
	//an author can have multiple books
	//a book can be written by multiple authors
	@ManyToMany
	@JoinTable(
			name="AuthorBook",
			joinColumns = @JoinColumn(name="authorID"),
			inverseJoinColumns = @JoinColumn(name="bookID")
			)
	@JsonIgnore
	private Set<Book> books = new HashSet<>();
	
	public Author() {
		
	}
	public Author(String name, String statement) {
		this.name = name;
		this.statement = statement;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStatement() {
		return statement;
	}

	public void setStatement(String statement) {
		this.statement = statement;
	}
	
	
	
	public Set<Book> getBooks() {
		return books;
	}
	public void setBooks(Set<Book> books) {
		this.books = books;
	}
	
	public void addBook(Book book)
	{
		this.books.add(book);
		Set<Author> authors = book.getAuthors();
		authors.add(this);
		book.setAuthors(authors);
	}
}
