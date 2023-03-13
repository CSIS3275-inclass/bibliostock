package com.example.bibliostock.request;

public class BookFormatRequest {
	private long bookID;
	private long formatID;
		
	public BookFormatRequest(long bookID, long formatID) {
		this.bookID = bookID;
		this.formatID = formatID;
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