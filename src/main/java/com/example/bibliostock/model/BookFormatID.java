package com.example.bibliostock.model;

import java.io.Serializable;
import java.util.Set;

import jakarta.persistence.Embeddable;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

@Embeddable //composite key for bookstock table
public class BookFormatID implements Serializable{
	
	private long bookID;
	
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

