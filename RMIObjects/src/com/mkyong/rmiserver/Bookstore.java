package com.mkyong.rmiserver;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import com.mkyong.rmiinterface.Book;
import com.mkyong.rmiinterface.RMIInterface;

public class Bookstore extends UnicastRemoteObject implements RMIInterface{
	private static final long serialVersionUID = 1L;
	private List<Book> bookList;

	protected Bookstore(List<Book> list) throws RemoteException {
		super();
		this.bookList = list;
	}
	
	//The client sends a Book object with the isbn information on it (note: it could be a string with the isbn too)
	//With this method the server searches in the List bookList for any book that has that isbn and returns the whole object
	@Override
	public Book findBook(Book book) throws RemoteException {
		Predicate<Book> predicate = x-> x.getIsbn().equals(book.getIsbn());
		return bookList.stream().filter(predicate).findFirst().get();
		
	}
	
	@Override
	public List<Book> allBooks() throws RemoteException {
		return bookList;
	}
	
	private static List<Book> initializeList(){
		List<Book> list = new ArrayList<>();
		list.add(new Book("Head First Java, 2nd Edition", "978-0596009205", 31.41));
		list.add(new Book("Java In A Nutshell", "978-0596007737", 10.90));
		list.add(new Book("Java: The Complete Reference", "978-0071808552", 40.18));
		list.add(new Book("Head First Servlets and JSP", "978-0596516680", 35.41));
		list.add(new Book("Java Puzzlers: Traps, Pitfalls, and Corner Cases", "978-0321336781", 39.99));
		return list;
	}
	
	public static void main(String[] args){
        try {
            Naming.rebind("//localhost/MyBookstore", new Bookstore(initializeList()));
            System.err.println("Server ready");
        } catch (Exception e) {
            System.err.println("Server exception: " + e.getMessage());
        }
    }
}
