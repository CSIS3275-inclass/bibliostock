package com.example.bibliostock.request;

import com.example.bibliostock.model.BookStock;

public class BookStockRequest {
	private BookFormatRequest bookFormatRequest;
	private BookStock bookStock;
	
	public BookStockRequest(BookFormatRequest bookFormatRequest, BookStock bookStock) {
		this.bookFormatRequest = bookFormatRequest;
		this.bookStock = bookStock;
	}
	public BookFormatRequest getBookFormatRequest() {
		return bookFormatRequest;
	}
	public void setBookFormatRequest(BookFormatRequest bookFormatRequest) {
		this.bookFormatRequest = bookFormatRequest;
	}
	public BookStock getBookStock() {
		return bookStock;
	}
	public void setBookStock(BookStock bookStock) {
		this.bookStock = bookStock;
	}
	
}