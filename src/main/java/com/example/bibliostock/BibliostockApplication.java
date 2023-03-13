package com.example.bibliostock;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.Hibernate;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.example.bibliostock.model.Author;
import com.example.bibliostock.model.AuthorRepository;
import com.example.bibliostock.model.Book;
import com.example.bibliostock.model.BookCart;
import com.example.bibliostock.model.BookCartRepository;
import com.example.bibliostock.model.BookRepository;
import com.example.bibliostock.model.BookStock;
import com.example.bibliostock.model.BookStockRepository;
import com.example.bibliostock.model.Cart;
import com.example.bibliostock.model.CartRepository;
import com.example.bibliostock.model.Customer;
import com.example.bibliostock.model.CustomerRepository;
import com.example.bibliostock.model.Favorite;
import com.example.bibliostock.model.FavoriteRepository;
import com.example.bibliostock.model.Format;
import com.example.bibliostock.model.FormatRepository;
import com.example.bibliostock.model.Genre;
import com.example.bibliostock.model.GenreRepository;
import com.example.bibliostock.model.Manager;
import com.example.bibliostock.model.ManagerRepository;
import com.example.bibliostock.model.Serie;
import com.example.bibliostock.model.SerieRepository;
import com.example.bibliostock.request.BookStockRequest;

@SpringBootApplication
public class BibliostockApplication {

	public static void main(String[] args) {
		SpringApplication.run(BibliostockApplication.class, args);
	}
	
	private void loadData(
			CustomerRepository customerRepo,
			ManagerRepository managerRepo,
			BookRepository bookRepo,
			AuthorRepository authorRepo,
			GenreRepository genreRepo,
			SerieRepository serieRepo,
			FormatRepository formatRepo,
			BookStockRepository bookStockRepo,
			CartRepository cartRepo,
			BookCartRepository bookCartRepo
			) {
		
		
		ArrayList<Book> books = new ArrayList<Book>();
		ArrayList<Author> authors = new ArrayList<Author>();
		ArrayList<Manager> managers = new ArrayList<Manager>();
		ArrayList<Customer> customers = new ArrayList<Customer>();
		ArrayList<Genre> genres = new ArrayList<Genre>();
		ArrayList<Serie> series = new ArrayList<Serie>();
		ArrayList<Format> formats = new ArrayList<Format>();
		ArrayList<BookStock> inventoryItems= new ArrayList<BookStock>();

		
		//adding authors
		authors.add(new Author("Seumas Butland","Integer ac leo. Pellentesque ultrices mattis odio. Donec vitae nisi."));
		authors.add(new Author("Nickolaus Baert","Sed ante. Vivamus tortor. Duis mattis egestas metus."));
		authors.add(new Author("Alexandra Cotgrave","Sed ante. Vivamus tortor. Duis mattis egestas metus."));
		authors.add(new Author("Pearce Cocci","Phasellus in felis. Donec semper sapien a libero. Nam dui."));
		authors.add(new Author("Selby Smouten","In hac habitasse platea dictumst. Etiam faucibus cursus urna. Ut tellus."));
		authors.add(new Author("Isaac Geipel","Praesent blandit. Nam nulla. Integer pede justo, lacinia eget, tincidunt eget, tempus vel, pede."));
		
		//adding genres
		genres.add(new Genre("Drama|War","mesh revolutionary platforms"));
		genres.add(new Genre("Western","expedite virtual metrics"));
		genres.add(new Genre("Documentary","extend real-time niches"));
		
		//adding series
		series.add(new Serie("Top Hat"));
		series.add(new Serie("Movie Movie"));
		
		//adding books
		books.add(new Book("911786354-6",5.0,"Escape from Fort Bravo","exploit innovative convergence",LocalDate.of(2022,9,24)));
		books.add(new Book("727904546-4",3.2,"My Father's Glory (La gloire de mon père)","transform sticky supply-chains",LocalDate.of(2022,7,25)));
		books.add(new Book("743903283-7",4.4,"Amandla! A Revolution in Four Part Harmony","deploy B2C partnerships",LocalDate.of(2022,8,24)));
		books.add(new Book("809867284-0",4.0,"At the Death House Door","scale cross-media e-business",LocalDate.of(2022,12,12)));
		books.add(new Book("172553343-X",3.2,"All the Young Men","target back-end paradigms",LocalDate.of(2022,9,24)));
		
		//add author,genre to books
		books.get(0).addAuthor(authors.get(0));
		books.get(0).addAuthor(authors.get(1));
		books.get(0).addGenre(genres.get(0));
		books.get(0).addGenre(genres.get(1));
		books.get(0).addGenre(genres.get(2));
		
		books.get(1).addAuthor(authors.get(2));
		books.get(1).addGenre(genres.get(1));
		books.get(1).addGenre(genres.get(0));
		
		books.get(2).addAuthor(authors.get(3));
		books.get(2).addGenre(genres.get(0));
		books.get(2).addGenre(genres.get(1));
		
		books.get(3).addAuthor(authors.get(4));
		books.get(3).addGenre(genres.get(1));
		books.get(4).addAuthor(authors.get(5));
		books.get(4).addGenre(genres.get(0));

		//add books to series
		series.get(0).addBook(books.get(0));
		series.get(0).addBook(books.get(1));
		series.get(1).addBook(books.get(2));
		
		//add formats
		formats.add(new Format("Audio"));
		formats.add(new Format("Paper"));
		formats.add(new Format("Ebook"));
		
		//adding managers
		managers.add(new Manager("eheatherington0","BrO83Zlp","khintze0@purevolume.com",false));
		managers.add(new Manager("gflattman3","sgOfIRYj","rmertsching3@bizjournals.com",true));
		
		//adding customers
		customers.add(new Customer("kgreatex0","Ztad8yku","ealyoshin0@epa.gov","Suite 88"));
		customers.add(new Customer("dwinsborrow5","PZMdLry","lwooding5@webs.com","Room 298"));
		customers.add(new Customer("bbaystona","bdD0cO","ebrosia@army.mil","Suite 71"));
		customers.add(new Customer("llifseyd","XY12bhRN","jbeavond@omniture.com","Room 608"));
		
		bookRepo.saveAll(books);
		authorRepo.saveAll(authors);
		genreRepo.saveAll(genres);
		serieRepo.saveAll(series);
		formatRepo.saveAll(formats);
		customerRepo.saveAll(customers);
		managerRepo.saveAll(managers);
		
		//add inventory items - ONLY when book,format, and other needed entities are saved
		inventoryItems.add(new BookStock(books.get(0),formats.get(0),44.97,66.03,677,managers.get(0)));
		bookStockRepo.saveAll(inventoryItems);
		
		bookRepo.findAll().forEach(System.out::println);
		books.get(1).info();
		managerRepo.findAll().forEach(System.out::println);
		authorRepo.findAll().forEach(System.out::println);
		customerRepo.findAll().forEach(System.out::println);
		genreRepo.findAll().forEach(System.out::println);
		serieRepo.findAll().forEach(System.out::println);
		formatRepo.findAll().forEach(System.out::println);
		bookStockRepo.findAll().forEach(System.out::println);
		cartRepo.findAll().forEach(System.out::println);
	}
	
	@Bean
	ApplicationRunner init(
			CustomerRepository customerRepo,
			ManagerRepository managerRepo,
			BookRepository bookRepo,
			AuthorRepository authorRepo,
			GenreRepository genreRepo,
			FavoriteRepository favoriteRepo,
			SerieRepository serieRepo,
			FormatRepository formatRepo,
			BookStockRepository bookStockRepo,
			CartRepository cartRepo,
			BookCartRepository bookCartRepo) {
		return (arg) -> {
			loadData(customerRepo, managerRepo, bookRepo, authorRepo, genreRepo, serieRepo, formatRepo, bookStockRepo, cartRepo, bookCartRepo);
			
		};
	}

}
