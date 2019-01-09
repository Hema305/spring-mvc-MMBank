package com.moneymoney.account.service;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.moneymoney.account.SavingsAccount;
import com.moneymoney.account.dao.SavingsAccountDAO;
import com.moneymoney.account.factory.AccountFactory;
import com.moneymoney.account.util.DBUtil;
import com.moneymoney.exception.AccountNotFoundException;
import com.moneymoney.exception.InsufficientFundsException;
import com.moneymoney.exception.InvalidInputException;


@Service
public class SavingsAccountServiceImpl implements SavingsAccountService {

	private AccountFactory factory;
	@Autowired
	private SavingsAccountDAO savingsAccountDAO;

	public SavingsAccountServiceImpl(SavingsAccountDAO savingsAccountDAO) {
		factory = AccountFactory.getInstance();
		this.savingsAccountDAO= savingsAccountDAO;
	}

	public SavingsAccount createNewAccount(String accountHolderName, double accountBalance, boolean salary)
			throws ClassNotFoundException, SQLException {
		SavingsAccount account = factory.createNewSavingsAccount(accountHolderName, accountBalance, salary);
		savingsAccountDAO.createNewAccount(account);
		return null;
	}

	public List<SavingsAccount> getAllSavingsAccount() throws ClassNotFoundException, SQLException {
		return savingsAccountDAO.getAllSavingsAccount();
	}

	public void deposit(SavingsAccount account, double amount) throws ClassNotFoundException, SQLException {
		if (amount > 0) {
			double currentBalance = account.getBankAccount().getAccountBalance();
			currentBalance += amount;
			savingsAccountDAO.updateBalance(account.getBankAccount().getAccountNumber(), currentBalance);
		}else {
			throw new InvalidInputException("Invalid Input Amount!");
		}
	}
	public void withdraw(SavingsAccount account, double amount) throws ClassNotFoundException, SQLException {
		double currentBalance = account.getBankAccount().getAccountBalance();
		if (amount > 0 && currentBalance >= amount) {
			currentBalance -= amount;
			savingsAccountDAO.updateBalance(account.getBankAccount().getAccountNumber(), currentBalance);
		} else {
			throw new InsufficientFundsException("Invalid Input or Insufficient Funds!");
		}
	}
	@Transactional(rollbackForClassName={"Throwable"})
	public void fundTransfer(SavingsAccount sender, SavingsAccount receiver, double amount)
			throws ClassNotFoundException, SQLException {
		try {
			withdraw(sender, amount);
			deposit(receiver, amount);
			DBUtil.commit();
		} catch (InvalidInputException | InsufficientFundsException e) {
			e.printStackTrace();
			DBUtil.rollback();
		} catch(Exception e) {
			e.printStackTrace();
			DBUtil.rollback();
		}
	}

	public boolean updateAccount(SavingsAccount account) throws ClassNotFoundException, SQLException {
		return savingsAccountDAO.updateAccountType(account);
	}


	public SavingsAccount deleteAccount(int accountNumber) throws ClassNotFoundException, SQLException, AccountNotFoundException {
		return  savingsAccountDAO.deleteAccount(accountNumber);
	}
	
	public SavingsAccount getAccountById(int accountNumber) throws ClassNotFoundException, SQLException, AccountNotFoundException {
		return savingsAccountDAO.getAccountById(accountNumber);
	}

	public double checkBalance(int accountNumber) throws ClassNotFoundException, SQLException, AccountNotFoundException {
		return savingsAccountDAO.checkBalance(accountNumber);
	}

	public List<SavingsAccount> sortByAccountHolderName() throws ClassNotFoundException, SQLException {
		return savingsAccountDAO.sortByAccountHolderName();
	}

	public List<SavingsAccount> sortByBalanceRange(int minimumBalance,
			int maximumBalance) throws ClassNotFoundException, SQLException {
		
		return savingsAccountDAO.sortByBalanceRange(minimumBalance,maximumBalance);
	}

	public List<SavingsAccount> sortByAccountHolderNameDescending() throws SQLException, ClassNotFoundException {
		return savingsAccountDAO.sortByAccountHolderNameDescending();
	}

	public List<SavingsAccount> sortByBalanceRangeDescending(
			int minimumBalanceDescending, int maximumBalanceDescending) throws ClassNotFoundException, SQLException {
		
		return savingsAccountDAO.sortByBalanceRangeDescending(minimumBalanceDescending,maximumBalanceDescending);
	}

	public List<SavingsAccount> getAccountByName(String accountHolderName) throws ClassNotFoundException, SQLException, AccountNotFoundException {
		return savingsAccountDAO.getAccountByName(accountHolderName);
	}

	public List<SavingsAccount> getAllBelowBalance(int balanceNumber) throws ClassNotFoundException, SQLException {
		
		return savingsAccountDAO.getAllBelowBalance(balanceNumber);
	}

	public List<SavingsAccount> getAllAboveBalance(int balanceNumber) throws ClassNotFoundException, SQLException {
		
		return savingsAccountDAO.getAllAboveBalance(balanceNumber);
	}

	public List<SavingsAccount> sortByAccountBalance() throws ClassNotFoundException, SQLException {
		return savingsAccountDAO.sortByAccountBalance();
	}

	public List<SavingsAccount> sortByAccountBalanceDescending() throws ClassNotFoundException, SQLException {
		return savingsAccountDAO.sortByAccountBalanceDescending();
	}
}