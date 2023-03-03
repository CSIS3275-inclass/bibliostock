package com.example.bibliostock;

import java.time.LocalDate;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.example.bibliostock.model.Book;
import com.example.bibliostock.model.BookRepository;

@SpringBootApplication
public class BibliostockApplication {

	public static void main(String[] args) {
		SpringApplication.run(BibliostockApplication.class, args);
	}
	
	@Bean
	ApplicationRunner init(BookRepository bookRepo) {
		return (arg) -> {
			Book book1 = new Book("349673424-8", "Möbius", "Donec diam neque, vestibulum eget", LocalDate.of(2022,11,24));
			bookRepo.save(book1);
		};
	}
}
