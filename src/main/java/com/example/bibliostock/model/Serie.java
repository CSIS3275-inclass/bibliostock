package com.example.bibliostock.model;

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
@Table(name = "Serie")
public class Serie {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long ID;
	
	@Column(name = "serie")
	private String serie;
}
