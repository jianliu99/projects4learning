package com.mkyong.rmiclient;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.List;
import java.util.NoSuchElementException;

import javax.swing.JOptionPane;

import com.mkyong.rmiinterface.Book;
import com.mkyong.rmiinterface.RMIInterface;

public class Customer {
	private static RMIInterface look_up;
	

	public static void main(String[] args) throws MalformedURLException, RemoteException, NotBoundException {
		look_up = (RMIInterface) Naming.lookup("//localhost/MyBookstore");
		
		boolean findmore;
		do{
			String[] options = {"Show All", "Find a book", "Exit"};
			int choice = JOptionPane.showOptionDialog(null, "Choose an action", "Option dialog", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
			
			switch(choice){
				case 0:
					List<Book> list = look_up.allBooks();
					StringBuilder message = new StringBuilder();
					list.forEach(x -> {message.append(x.toString() + "\n");});
					JOptionPane.showMessageDialog(null, new String(message));
					break;
				case 1:
					String isbn = JOptionPane.showInputDialog("Type the isbn of the book you want to find.");
					try{
						Book response = look_up.findBook(new Book(isbn));
						JOptionPane.showMessageDialog(null, "Title: " + response.getTitle() + "\n" + "Cost: $" + response.getCost(), response.getIsbn(), JOptionPane.INFORMATION_MESSAGE);
					}catch(NoSuchElementException ex){
						JOptionPane.showMessageDialog(null, "Not found");
					}
					break;
				default:
					System.exit(0);
					break;
			}
			findmore = (JOptionPane.showConfirmDialog(null, "Do you want to exit?", "Exit", JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION);
		}while(findmore);
	}
}
