package com.moneymoney.account.dao;

import java.sql.SQLException;
import java.util.List;

import com.moneymoney.account.SavingsAccount;
import com.moneymoney.exception.AccountNotFoundException;

public interface SavingsAccountDAO {
	
	SavingsAccount createNewAccount(SavingsAccount account) throws ClassNotFoundException, SQLException;
	
	SavingsAccount getAccountById(int accountNumber) throws ClassNotFoundException, SQLException, AccountNotFoundException;
	
	SavingsAccount deleteAccount(int accountNumber) throws ClassNotFoundException, SQLException, AccountNotFoundException;
	
	List<SavingsAccount> getAllSavingsAccount() throws ClassNotFoundException, SQLException;
	
	void updateBalance(int accountNumber, double currentBalance) throws ClassNotFoundException, SQLException;
	
	double checkBalance(int accountNumber) throws AccountNotFoundException, ClassNotFoundException, SQLException;
	
	boolean updateAccountType(SavingsAccount account) throws SQLException, ClassNotFoundException;
	
	List<SavingsAccount> sortByAccountHolderName() throws SQLException, ClassNotFoundException;
	
	List<SavingsAccount> sortByBalanceRange(int minimumBalance, int maximumBalance) throws SQLException, ClassNotFoundException;
	
	List<SavingsAccount> sortByAccountHolderNameDescending() throws SQLException, ClassNotFoundException;
	
	List<SavingsAccount> sortByBalanceRangeDescending(int minimumBalanceDescending, int maximumBalanceDescending) throws ClassNotFoundException, SQLException;
	
	List<SavingsAccount> getAccountByName(String accountHolderName) throws SQLException, AccountNotFoundException, ClassNotFoundException;
	
	List<SavingsAccount> getAllBelowBalance(int balanceNumber) throws SQLException, ClassNotFoundException;
	
	List<SavingsAccount> getAllAboveBalance(int balanceNumber)throws SQLException, ClassNotFoundException;

	List<SavingsAccount> sortByAccountBalance() throws ClassNotFoundException, SQLException;

	List<SavingsAccount> sortByAccountBalanceDescending() throws ClassNotFoundException, SQLException;
}