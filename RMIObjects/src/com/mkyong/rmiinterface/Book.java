package com.mkyong.rmiinterface;

import java.io.Serializable;

public class Book implements Serializable{
	private static final long serialVersionUID = 1190476516911661470L;
	private String title;
	private String isbn;
	private double cost;
	
	public Book(String isbn){
		this.isbn = isbn;
	}
	
	public Book(String title, String isbn, double cost){
		this.title = title;
		this.isbn = isbn;
		this.cost = cost;
	}

	public String getTitle() {
		return title;
	}

	public String getIsbn() {
		return isbn;
	}

	public double getCost() {
		return cost;
	}
	
	public String toString(){
		return "> " + this.title + " ($" + this.cost + ")";
	}
}
