package com.javacodegeeks.mockito;

public class Customer {
	private AccountManager accountManager;

	public long withdraw(long amount) throws NotEnoughFundsException {
		Account account = accountManager.findAccount(this);
		long balance = accountManager.getBalance(account);
		if (balance < amount) {
			throw new NotEnoughFundsException();
		}
		accountManager.withdraw(account, amount);
		return accountManager.getBalance(account);
	}

	public void setAccountManager(AccountManager accountManager) {
		this.accountManager = accountManager;
	}
}
