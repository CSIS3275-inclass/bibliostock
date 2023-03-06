package com.example.bibliostock.model;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.MapsId;

@Embeddable //composite key for bookstock table
public class BookFormatID implements Serializable{
	private static final long serialVersionUID = 1L;
	 @Column(name = "bookID",
			 insertable=false, updatable=false)
//	@MapsId("bookID") //what's in BookFormatID embedded class
	private long bookID;
	
	 @Column(name = "formatID",
			 insertable=false, updatable=false)
//	@MapsId("formatID") //what's in BookFormatID embedded class
	private long formatID;
	
	
	public BookFormatID() {
		
	}
	public BookFormatID(long bookId, long formatId) {
		this.bookID = bookId;
		this.formatID = formatId;
	}
	public long getBookID() {
		return bookID;
	}
	public void setBookID(long bookID) {
		this.bookID = bookID;
	}
	public long getFormatID() {
		return formatID;
	}
	public void setFormatID(long formatID) {
		this.formatID = formatID;
	}

	
}
