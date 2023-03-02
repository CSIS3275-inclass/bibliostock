package com.example.bibliostock.model;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
	
	@Column(name = "averageReview")
	private float averageReview;
	
	@Column(name = "publicationDate")
	@Temporal(TemporalType.DATE)
	private Date publicationDate;
	
	//multiple books can belong to the same serie
	@ManyToOne(mappedBy = "book", cascade = CascadeType.ALL, fetch = FetchType.LAZY) 
	private Set<serie> serie = new HashSet<>();
}
