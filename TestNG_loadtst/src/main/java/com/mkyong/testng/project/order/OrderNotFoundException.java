package com.mkyong.testng.project.order;

public class OrderNotFoundException extends Exception{

	private static final long serialVersionUID = 1L;
	
	public OrderNotFoundException(){}
	
	public OrderNotFoundException(String message){
		super(message);
	}
	
}