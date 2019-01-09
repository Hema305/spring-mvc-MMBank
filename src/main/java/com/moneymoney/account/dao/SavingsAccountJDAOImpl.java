package com.moneymoney.account.dao;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.moneymoney.account.SavingsAccount;
import com.moneymoney.account.mapper.SavingsAccountDetailsMapper;
import com.moneymoney.exception.AccountNotFoundException;

@Repository
@Primary
public class SavingsAccountJDAOImpl implements SavingsAccountDAO {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public SavingsAccount createNewAccount(SavingsAccount account) throws ClassNotFoundException, SQLException {
		jdbcTemplate.update("INSERT INTO ACCOUNT VALUES(?,?,?,?,?,?)",
				new Object[] { account.getBankAccount().getAccountNumber(),
						account.getBankAccount().getAccountHolderName(), account.getBankAccount().getAccountBalance(),
						account.isSalary(), null, "SA" });
		return account;
	}

	public List<SavingsAccount> getAllSavingsAccount() throws ClassNotFoundException, SQLException {

		return jdbcTemplate.query("SELECT * FROM ACCOUNT", new SavingsAccountDetailsMapper());
	}

	public void updateBalance(int accountNumber, double currentBalance) throws ClassNotFoundException, SQLException {

		jdbcTemplate.update("UPDATE ACCOUNT SET accountBalance=? where accountNumber=?",
				new Object[] { currentBalance, accountNumber });

	}

	public SavingsAccount getAccountById(int accountNumber)
			throws ClassNotFoundException, SQLException, AccountNotFoundException {

		return jdbcTemplate.queryForObject("SELECT * FROM account where accountNumber=?", new Object[] { accountNumber },
				new SavingsAccountDetailsMapper());

	}

	public SavingsAccount deleteAccount(int accountNumber)
			throws ClassNotFoundException, SQLException, AccountNotFoundException {

		jdbcTemplate.update("DELETE FROM account WHERE accountNumber=?", new Object[] { accountNumber });
		return null;
	}

	public double checkBalance(int accountNumber)
			throws AccountNotFoundException, ClassNotFoundException, SQLException {

		return jdbcTemplate.queryForObject("SELECT accountBalance FROM account where accountNumber=?",
				new Object[] { accountNumber }, Double.class);
	}

	public boolean updateAccountType(SavingsAccount account) throws SQLException, ClassNotFoundException {

		jdbcTemplate.update("UPDATE ACCOUNT SET accountHolderName=?,salaried=? where accountNumber=?",
				new Object[] { account.getBankAccount().getAccountHolderName(), account.isSalary(),
						account.getBankAccount().getAccountNumber() });

		return false;
	}

	public List<SavingsAccount> sortByAccountHolderName() throws SQLException, ClassNotFoundException {

		return jdbcTemplate.query("SELECT * FROM account ORDER BY accountHolderName", new SavingsAccountDetailsMapper());
	}

	public List<SavingsAccount> sortByBalanceRange(int minimumBalance, int maximumBalance)
			throws SQLException, ClassNotFoundException {

		return jdbcTemplate.query("SELECT * FROM account WHERE accountBalance BETWEEN ? and ? ORDER BY accountBalance",
				new Object[] { minimumBalance, maximumBalance }, new SavingsAccountDetailsMapper());
	}

	public List<SavingsAccount> sortByAccountHolderNameDescending() throws SQLException, ClassNotFoundException {

		return jdbcTemplate.query("SELECT * FROM account ORDER BY accountHolderName DESC", new SavingsAccountDetailsMapper());
	}

	public List<SavingsAccount> sortByBalanceRangeDescending(int minimumBalanceDescending, int maximumBalanceDescending)
			throws ClassNotFoundException, SQLException {

		return jdbcTemplate.query("SELECT * FROM account WHERE accountBalance BETWEEN ? and ? ORDER BY accountBalance DESC",
				new Object[] { minimumBalanceDescending, maximumBalanceDescending }, new SavingsAccountDetailsMapper());
	}

	public List<SavingsAccount> getAccountByName(String accountHolderName)
			throws SQLException, AccountNotFoundException, ClassNotFoundException {

		return jdbcTemplate.query("SELECT * FROM account where accountHolderName=?", new Object[] { accountHolderName },
				new SavingsAccountDetailsMapper());
	}

	public List<SavingsAccount> getAllBelowBalance(int balanceNumber) throws SQLException, ClassNotFoundException {

		return jdbcTemplate.query("SELECT * FROM ACCOUNT WHERE accountBalance<=?", new Object[] { balanceNumber },
				new SavingsAccountDetailsMapper());
	}

	public List<SavingsAccount> getAllAboveBalance(int balanceNumber) throws SQLException, ClassNotFoundException {

		return jdbcTemplate.query("SELECT * FROM ACCOUNT WHERE accountBalance>=?", new Object[] { balanceNumber },
				new SavingsAccountDetailsMapper());
	}

	public List<SavingsAccount> sortByAccountBalance() throws ClassNotFoundException, SQLException {

		return jdbcTemplate.query("SELECT * FROM account ORDER BY accountBalance", new SavingsAccountDetailsMapper());
	}

	public List<SavingsAccount> sortByAccountBalanceDescending() throws ClassNotFoundException, SQLException {

		return jdbcTemplate.query("SELECT * FROM account ORDER BY accountBalance DESC", new SavingsAccountDetailsMapper());
	}
}