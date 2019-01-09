package com.moneymoney.account.factory;

import org.springframework.stereotype.Component;

import com.moneymoney.account.SavingsAccount;


public final class AccountFactory {
	
	private static AccountFactory factory = new AccountFactory();

	private AccountFactory() {
		
	}
	
	public static AccountFactory getInstance() {
		return factory;
	}

	public SavingsAccount createNewSavingsAccount(String accountHolderName, double accountBalance, boolean salary) {
		return new SavingsAccount(accountHolderName, accountBalance, salary);
	}
}