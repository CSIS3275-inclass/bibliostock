package com.example.bibliostock.model;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

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
@Table(name = "Book")
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
	
	//multiple books can belong to the same serie
	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	@JoinColumn(name = "serieID",nullable = true)
	private Serie serie;
	
	//multiple authors can write the same book
	//multiple books can be written by an author
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable (
			name="AuthorBook",
			joinColumns = @JoinColumn(name="bookID"),
			inverseJoinColumns = @JoinColumn(name="authorID"))
	private Set<Author> authors = new HashSet<>();
	
	//multiple books can have same genre
	//One book can have multiple genres
	@ManyToMany(fetch=FetchType.LAZY)
	@JoinTable(
			name="GenreBook",
			joinColumns =  @JoinColumn(name="bookID"),
			inverseJoinColumns = @JoinColumn(name="genreID")
			)
	private Set<Genre> genres = new HashSet<>();
	
	//One favorite has multiple books
	//Same books can be in multiple favorites
	@ManyToMany(mappedBy="books", cascade = CascadeType.ALL, fetch=FetchType.LAZY)
	private Set<Favorite> favorites = new HashSet<>();
	
	@OneToMany(mappedBy="book", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
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
	
	public Serie getSerie() {
		return serie;
	}
	
	public void setSerie(Serie serie) {
		this.serie = serie;
	}
	
	
	public Set<Author> getAuthors() {
		return authors;
	}
	
	public void setAuthors(Set<Author> authors) {
		this.authors = authors;
	}
	
	public void addAuthors(Author author) {
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

	public Set<BookStock> getBookStocks() {
		return bookStocks;
	}

	public void setBookStocks(Set<BookStock> bookStocks) {
		this.bookStocks = bookStocks;
	}
	
	
	
}
