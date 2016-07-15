package com.javacodegeeks.mockito;

import static org.mockito.Mockito.*;

import org.mockito.InOrder;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class MockitoVerifyExample {
	private Customer customer;
	private AccountManager accountManager;
	private Account account;
	private long withdrawlAmount2000 = 2000L;
	
	@BeforeMethod
	public void setupMock() {
		customer = new Customer();
		accountManager = mock(AccountManager.class);
		customer.setAccountManager(accountManager);
		account = mock(Account.class);
		when(accountManager.findAccount(customer)).thenReturn(account);		
	}
	
	@Test(expectedExceptions=NotEnoughFundsException.class)
	public void withdrawButNotEnoughFunds() throws NotEnoughFundsException {
		long balanceAmount200 = 200L;
		
		p("Train getBalance(account) to return " + balanceAmount200);
		when(accountManager.getBalance(account)).thenReturn(balanceAmount200);
		
		printBalance(balanceAmount200);
		try {
			p("Customer.withdraw(" + withdrawlAmount2000 + ") should fail with NotEnoughFundsException");
			customer.withdraw(withdrawlAmount2000);			
		} catch (NotEnoughFundsException e) {
			p("NotEnoughFundsException is thrown"); 
			
			verify(accountManager).findAccount(customer);
			p("Verified findAccount(customer) is called");			
			
			verify(accountManager, times(0)).withdraw(account, withdrawlAmount2000);
			p("Verified withdraw(account, " + withdrawlAmount2000 + ") is not called");
			
			throw e;
		}
	}
	
	@Test
	public void withdraw() throws NotEnoughFundsException {		
		long balanceAmount3000 = 3000L;
		
		p("Train getBalance(account) to return " + balanceAmount3000);
		when(accountManager.getBalance(account)).thenReturn(balanceAmount3000);
		
		printBalance(balanceAmount3000);
		
		p("Customer.withdraw(" + withdrawlAmount2000 + ")");
		customer.withdraw(withdrawlAmount2000);
		
		verify(accountManager).withdraw(account, withdrawlAmount2000);
		p("Verified withdraw(account, " + withdrawlAmount2000 + ") is Called");
		
		verify(accountManager, times(2)).getBalance(account);
		p("Verified getBalance(account) is called twice");
		
		verify(accountManager).withdraw(account, withdrawlAmount2000);
		p("Verified withdraw(account, " +  withdrawlAmount2000 + ") is called just once");
		
		verify(accountManager, atLeastOnce()).findAccount(customer);
		p("Verified findAccount(account) is called atleast once");
	}
	
	@Test
	public void withdrawAndVerifyOrder() throws NotEnoughFundsException {	
		long balanceAmount3000 = 3000L;
		
		p("Train getBalance(account) to return " + balanceAmount3000);
		when(accountManager.getBalance(account)).thenReturn(balanceAmount3000);
		
		printBalance(balanceAmount3000);
		
		p("Customer.withdraw(" + withdrawlAmount2000 + ")");
		customer.withdraw(withdrawlAmount2000);
		
		p("Verify order of method calls");
		InOrder order = inOrder(accountManager);
		
		order.verify(accountManager).findAccount(customer);
		p("Verified findAccount(account) is called");
		
		order.verify(accountManager).getBalance(account);
		p("Verified getBalance(account) is called");
		
		order.verify(accountManager).withdraw(account, withdrawlAmount2000);
		p("Verified withdraw(account, " +  withdrawlAmount2000 + ") is called");
		
		order.verify(accountManager).getBalance(account);
		p("Verified getBalance(account) is called one more time after withdrawl");
		
		verifyNoMoreInteractions(accountManager);
		p("verified no more calls are executed on the mock object");
	}
	
	private static void p(String text) {
		System.out.println(text);
	}
	
	private void printBalance(long balance) {
		p("Balance is " + balance + " and withdrawl amount " + withdrawlAmount2000);	
	}
}