package com.example.bibliostock.model;
import java.time.LocalDate;
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
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "books")
public class Book {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long ID;
	
	@Column(name = "ISBN")
	private String ISBN;
	
	@Column(name = "title")
	private String title;
	
	@Column(name = "synopsis")
	private String synopsis;
	
	//updated every time a score is added to the book
	@Column(name = "averageReview")
	private float averageReview;
	
	@Column(name = "publicationDate")
	@Temporal(TemporalType.DATE)
	private LocalDate publicationDate;
	
	//an author can write multiple books
	//a book can be written by multiple authors
	@ManyToMany(mappedBy="books")
	@JsonIgnore 
	@JsonProperty("authors")
	private Set<Author> authors= new HashSet<>();

	//a book can have multiple genres
	//a genre can have multiple books
	@ManyToMany(mappedBy="books")
	@JsonIgnore
	@JsonProperty("genres")
	private Set<Genre> genres= new HashSet<>();
	
	//a favorite can have multiple books
	//a book can be in multiple favorites
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinTable(
			name="FavoriteBook",
			joinColumns = @JoinColumn(name="bookID"),
			inverseJoinColumns= @JoinColumn(name="favoriteID"))
	@JsonIgnore
	private Set<Favorite> favorites = new HashSet<>();
	
	//A book only has one serie
	//A serie can have multiple books
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = true)
	@JoinColumn(name = "serieID",nullable = true)
	private Serie serie;
	
	@OneToMany(mappedBy="book",cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
	@JsonIgnore
	private Set<BookStock> bookStocks = new HashSet<>();

	
	public Book() {
		
	}

	public Book(String iSBN, String title, String synopsis, LocalDate publication) {
		super();
		ISBN = iSBN;
		this.title = title;
		this.synopsis = synopsis;
		this.publicationDate=publication;
	}
	
	public void info() { //just for testing will remove later
		System.out.println(this.ID+" "+this.ISBN+" "+this.title);
	}
	
	
	public long getID() {
		return ID;
	}

	public String getISBN() {
		return ISBN;
	}

	public void setISBN(String iSBN) {
		ISBN = iSBN;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSynopsis() {
		return synopsis;
	}

	public void setSynopsis(String synopsis) {
		this.synopsis = synopsis;
	}

	public float getAverageReview() {
		return averageReview;
	}

	public void setAverageReview(float averageReview) {
		this.averageReview = averageReview;
	}
	public LocalDate getPublicationDate() {
		return publicationDate;
	}
	
	public void setPublicationDate(LocalDate publicationDate) {
		this.publicationDate = publicationDate;
	}
	
	public Set<Author> getAuthors() {
		return authors;
	}
	
	public void setAuthors(Set<Author> authors) {
		this.authors = authors;
	}
	
	public void addAuthor(Author author) {
		author.addBook(this);
		
	}

	public Set<Genre> getGenres() {
		return genres;
	}

	public void setGenres(Set<Genre> genres) {
		this.genres = genres;
	}
	
	public void addGenre(Genre genre) {
		genre.addBook(this);
	}
	
	public Set<Favorite> getFavorites() {
		return favorites;
	}

	public void setFavorites(Set<Favorite> favorites) {
		this.favorites = favorites;
	}
	
	public Serie getSerie() {
		return serie;
	}
	
	public void setSerie(Serie serie) {
		this.serie = serie;
	}

	public Set<BookStock> getBookStocks() {
		return bookStocks;
	}

	public void setBookStocks(Set<BookStock> bookStocks) {
		this.bookStocks = bookStocks;
	}
	
	
}
