package com.uniovi.entities;

import java.util.Date;
import javax.persistence.*;

@Entity
public class Offer {
	@Id
	@GeneratedValue
	private Long id;
	private String title;
	private String description;
	private int date;
	private Double price;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	
	public Offer(Long id, String title, String description, int date, Double price) {
		super();
		this.id = id;
		this.title = title;
		this.description = description;
		this.date = date;
		this.price = price;
	}

	public Offer(String title, String description, int date, Double price, User user) {
		super();
		this.title = title;
		this.description = description;
		this.date = date;
		this.price = price;
		this.user = user;
	}

	public Offer() {
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public int getDate() {
		return date;
	}

	public void setDate(int date) {
		this.date = date;
	}
	
	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	
	
}
