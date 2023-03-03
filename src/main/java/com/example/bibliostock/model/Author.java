package com.example.bibliostock.model;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name="Author")
public class Author {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long ID;
	
	private String name;
	
	private String statement;
	
	//multiple authors can have multiple books
	//multiple books can be written by an author
	@ManyToMany(mappedBy = "authors", cascade = CascadeType.ALL, fetch=FetchType.LAZY)
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
	
	public void addBook(Book book)
	{
		this.books.add(book);
		Set<Author> authors = book.getAuthors();
		authors.add(this);
		book.setAuthors(authors);
	}
}
