package com.moneymoney.account.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.moneymoney.account.SavingsAccount;
import com.moneymoney.account.util.DBUtil;
import com.moneymoney.exception.AccountNotFoundException;

@Repository
public class SavingsAccountDAOImpl implements SavingsAccountDAO {

	public SavingsAccount createNewAccount(SavingsAccount account) throws ClassNotFoundException, SQLException {

		Connection connection = DBUtil.getConnection();

		PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO ACCOUNT VALUES(?,?,?,?,?,?)");

		preparedStatement.setInt(1, account.getBankAccount().getAccountNumber());

		preparedStatement.setString(2, account.getBankAccount().getAccountHolderName());

		preparedStatement.setDouble(3, account.getBankAccount().getAccountBalance());

		preparedStatement.setBoolean(4, account.isSalary());

		preparedStatement.setObject(5, null);

		preparedStatement.setString(6, "SA");

		preparedStatement.executeUpdate();

		preparedStatement.close();

		DBUtil.commit();

		return account;

	}

	public List<SavingsAccount> getAllSavingsAccount() throws ClassNotFoundException, SQLException {

		List<SavingsAccount> savingsAccounts = new ArrayList<>();

		Connection connection = DBUtil.getConnection();

		Statement statement = connection.createStatement();

		ResultSet resultSet = statement.executeQuery("SELECT * FROM ACCOUNT");

		while (resultSet.next()) {// Check if row(s) is present in table

			int accountNumber = resultSet.getInt(1);

			String accountHolderName = resultSet.getString("accountHolderName");

			double accountBalance = resultSet.getDouble(3);

			boolean salary = resultSet.getBoolean("salaried");

			SavingsAccount savingsAccount = new SavingsAccount(accountNumber, accountHolderName, accountBalance,
					salary);

			savingsAccounts.add(savingsAccount);

		}

		DBUtil.commit();

		return savingsAccounts;

	}

	@Override

	public void updateBalance(int accountNumber, double currentBalance) throws ClassNotFoundException, SQLException {

		Connection connection = DBUtil.getConnection();

		connection.setAutoCommit(false);

		PreparedStatement preparedStatement = connection.prepareStatement

		("UPDATE ACCOUNT SET accountBalance? where accountNumber=?");

		preparedStatement.setDouble(1, currentBalance);

		preparedStatement.setInt(2, accountNumber);

		preparedStatement.executeUpdate();

	}

	@Override

	public SavingsAccount getAccountById(int accountNumber)
			throws ClassNotFoundException, SQLException, AccountNotFoundException {

		Connection connection = DBUtil.getConnection();

		PreparedStatement preparedStatement = connection.prepareStatement

		("SELECT * FROM account where accountNumber=?");

		preparedStatement.setInt(1, accountNumber);

		ResultSet resultSet = preparedStatement.executeQuery();

		SavingsAccount savingsAccount = null;

		if (resultSet.next()) {

			String accountHolderName = resultSet.getString("accountHolderName");

			double accountBalance = resultSet.getDouble(3);

			boolean salary = resultSet.getBoolean("salaried");

			savingsAccount = new SavingsAccount(accountNumber, accountHolderName, accountBalance, salary);

			DBUtil.commit();

			return savingsAccount;

		}

		throw new AccountNotFoundException("Account with account number " + accountNumber + " does not exist.");

	}

	@Override

	public SavingsAccount deleteAccount(int accountNumber)
			throws ClassNotFoundException, SQLException, AccountNotFoundException {

		if (getAccountById(accountNumber) != null) {

			Connection connection = DBUtil.getConnection();

			PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM account WHERE accountNumber=?");

			preparedStatement.setInt(1, accountNumber);

			preparedStatement.execute();

			DBUtil.commit();

		}

		return null;

	}

	@Override

	public double checkBalance(int accountNumber)
			throws AccountNotFoundException, ClassNotFoundException, SQLException {

		if (getAccountById(accountNumber) != null) {

			Connection connection = DBUtil.getConnection();

			PreparedStatement preparedStatement = connection.prepareStatement

			("SELECT * FROM account where accountNumber=?");

			preparedStatement.setInt(1, accountNumber);

			ResultSet resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {

				double accountBalance = resultSet.getDouble(3);

				DBUtil.commit();

				return accountBalance;

			}

		}

		throw new AccountNotFoundException("Account with account number " + accountNumber + " does not exist.");

	}

	@Override

	public boolean updateAccountType(SavingsAccount account) throws SQLException, ClassNotFoundException {

		Connection connection = DBUtil.getConnection();

		PreparedStatement preparedStatement = connection.prepareStatement

		("UPDATE ACCOUNT SET accountHolderName=?,salaried=? where accountNumber=?");

		preparedStatement.setString(1, account.getBankAccount().getAccountHolderName());

		preparedStatement.setBoolean(2, account.isSalary());

		preparedStatement.setInt(3, account.getBankAccount().getAccountNumber());

		int count = preparedStatement.executeUpdate();

		boolean result = false;

		if (count != 0) {

			result = true;

		}

		DBUtil.commit();

		return result;

	}

	@Override

	public List<SavingsAccount> sortByAccountHolderName() throws SQLException, ClassNotFoundException {

		Connection connection = DBUtil.getConnection();

		List<SavingsAccount> savingsList = new ArrayList<SavingsAccount>();

		Statement statement = connection.createStatement();

		ResultSet resultSet = statement.executeQuery("SELECT * FROM account ORDER BY accountHolderName");

		while (resultSet.next()) {

			int accountNumber = resultSet.getInt(1);

			String accountHolderName = resultSet.getString("accountHolderName");

			double accountBalance = resultSet.getDouble(3);

			boolean salary = resultSet.getBoolean("salaried");

			SavingsAccount savingsAccount = new SavingsAccount(accountNumber, accountHolderName, accountBalance,
					salary);

			savingsList.add(savingsAccount);

		}

		DBUtil.commit();

		return savingsList;

	}

	@Override

	public List<SavingsAccount> sortByBalanceRange(int minimumBalance, int maximumBalance)
			throws SQLException, ClassNotFoundException {

		Connection connection = DBUtil.getConnection();

		List<SavingsAccount> savingsList = new ArrayList<SavingsAccount>();

		PreparedStatement preparedStatementQuery = connection
				.prepareStatement("SELECT * FROM account WHERE accountBalance BETWEEN ? and ? ORDER BY accountBalance");

		preparedStatementQuery.setDouble(1, minimumBalance);

		preparedStatementQuery.setDouble(2, maximumBalance);

		ResultSet resultset = preparedStatementQuery.executeQuery();

		while (resultset.next()) {

			int accountNumber = resultset.getInt(1);

			String accountHolderName = resultset.getString("accountHolderName");

			double accountBalance = resultset.getDouble(3);

			boolean salary = resultset.getBoolean("salaried");

			SavingsAccount savingsAccount = new SavingsAccount(accountNumber, accountHolderName, accountBalance,
					salary);

			savingsList.add(savingsAccount);

		}

		DBUtil.commit();

		return savingsList;

	}

	@Override

	public List<SavingsAccount> sortByAccountHolderNameDescending() throws SQLException, ClassNotFoundException {

		Connection connection = DBUtil.getConnection();

		List<SavingsAccount> savingsList = new ArrayList<SavingsAccount>();

		Statement statement = connection.createStatement();

		ResultSet resultSet = statement.executeQuery("SELECT * FROM account ORDER BY accountHolderName DESC");

		while (resultSet.next()) {

			int accountNumber = resultSet.getInt(1);

			String accountHolderName = resultSet.getString("accountHolderName");

			double accountBalance = resultSet.getDouble(3);

			boolean salary = resultSet.getBoolean("salaried");

			SavingsAccount savingsAccount = new SavingsAccount(accountNumber, accountHolderName, accountBalance,
					salary);

			savingsList.add(savingsAccount);

		}

		DBUtil.commit();

		return savingsList;

	}

	@Override

	public List<SavingsAccount> sortByBalanceRangeDescending(

			int minimumBalanceDescending, int maximumBalanceDescending) throws ClassNotFoundException, SQLException {

		Connection connection = DBUtil.getConnection();

		List<SavingsAccount> savingsList = new ArrayList<SavingsAccount>();

		PreparedStatement preparedStatementQuery = connection
				.prepareStatement("SELECT * FROM account WHERE accountBalance BETWEEN ? and ? ORDER BY accountBalance DESC");

		preparedStatementQuery.setDouble(1, minimumBalanceDescending);

		preparedStatementQuery.setDouble(2, maximumBalanceDescending);

		ResultSet resultset = preparedStatementQuery.executeQuery();

		while (resultset.next()) {

			int accountNumber = resultset.getInt(1);

			String accountHolderName = resultset.getString("accountHolderName");

			double accountBalance = resultset.getDouble(3);

			boolean salary = resultset.getBoolean("salaried");

			SavingsAccount savingsAccount = new SavingsAccount(accountNumber, accountHolderName, accountBalance,
					salary);

			savingsList.add(savingsAccount);

		}

		DBUtil.commit();

		return savingsList;

	}

	@Override

	public List<SavingsAccount> getAccountByName(String accountHolderName)
			throws SQLException, AccountNotFoundException, ClassNotFoundException {

		Connection connection = DBUtil.getConnection();

		PreparedStatement preparedStatement = connection.prepareStatement

		("SELECT * FROM account where accountHolderName=?");

		preparedStatement.setString(1, accountHolderName);

		ResultSet resultSet = preparedStatement.executeQuery();

		SavingsAccount savingsAccount = null;

		if (resultSet.next()) {

			int accountId = resultSet.getInt("accountNumber");

			double accountBalance = resultSet.getDouble(3);

			boolean salary = resultSet.getBoolean("salaried");

			savingsAccount = new SavingsAccount(accountId, accountHolderName, accountBalance, salary);

			DBUtil.commit();

			return (List<SavingsAccount>) savingsAccount;

		}

		throw new AccountNotFoundException("Account with account HolderName " + accountHolderName + " does not exist.");

	}

	@Override

	public List<SavingsAccount> getAllBelowBalance(int balanceNumber) throws SQLException, ClassNotFoundException {

		List<SavingsAccount> savingsAccounts = new ArrayList<>();

		Connection connection = DBUtil.getConnection();

		PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM ACCOUNT WHERE accountBalance<=?");

		preparedStatement.setDouble(1, balanceNumber);

		ResultSet resultSet = preparedStatement.executeQuery();

		while (resultSet.next()) {// Check if row(s) is present in table

			int accountNumber = resultSet.getInt(1);

			String accountHolderName = resultSet.getString("accountHolderName");

			double accountBalance = resultSet.getDouble(3);

			boolean salary = resultSet.getBoolean("salaried");

			SavingsAccount savingsAccount = new SavingsAccount(accountNumber, accountHolderName, accountBalance,
					salary);

			savingsAccounts.add(savingsAccount);

		}

		DBUtil.commit();

		return savingsAccounts;

	}

	@Override

	public List<SavingsAccount> getAllAboveBalance(int balanceNumber) throws SQLException, ClassNotFoundException {

		List<SavingsAccount> savingsAccounts = new ArrayList<>();

		Connection connection = DBUtil.getConnection();

		PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM ACCOUNT WHERE account_bal>=?");

		preparedStatement.setDouble(1, balanceNumber);

		ResultSet resultSet = preparedStatement.executeQuery();

		while (resultSet.next()) {

			int accountNumber = resultSet.getInt(1);

			String accountHolderName = resultSet.getString("accountHolderName");

			double accountBalance = resultSet.getDouble(3);

			boolean salary = resultSet.getBoolean("salaried");

			SavingsAccount savingsAccount = new SavingsAccount(accountNumber, accountHolderName, accountBalance,
					salary);

			savingsAccounts.add(savingsAccount);

		}

		DBUtil.commit();

		return savingsAccounts;

	}

	@Override

	public List<SavingsAccount> sortByAccountBalance() throws ClassNotFoundException, SQLException {

		Connection connection = DBUtil.getConnection();

		List<SavingsAccount> savingsList = new ArrayList<SavingsAccount>();

		Statement statement = connection.createStatement();

		ResultSet resultSet = statement.executeQuery("SELECT * FROM account ORDER BY account_bal");

		while (resultSet.next()) {

			int accountNumber = resultSet.getInt(1);

			String accountHolderName = resultSet.getString("accountHolderName");

			double accountBalance = resultSet.getDouble(3);

			boolean salary = resultSet.getBoolean("salary");

			SavingsAccount savingsAccount = new SavingsAccount(accountNumber, accountHolderName, accountBalance,
					salary);

			savingsList.add(savingsAccount);

		}

		DBUtil.commit();

		return savingsList;

	}

	@Override

	public List<SavingsAccount> sortByAccountBalanceDescending() throws ClassNotFoundException, SQLException {

		Connection connection = DBUtil.getConnection();

		List<SavingsAccount> savingsList = new ArrayList<SavingsAccount>();

		Statement statement = connection.createStatement();

		ResultSet resultSet = statement.executeQuery("SELECT * FROM account ORDER BY account_bal DESC");

		while (resultSet.next()) {

			int accountNumber = resultSet.getInt(1);

			String accountHolderName = resultSet.getString("accountHolderName");

			double accountBalance = resultSet.getDouble(3);

			boolean salary = resultSet.getBoolean("salary");

			SavingsAccount savingsAccount = new SavingsAccount(accountNumber, accountHolderName, accountBalance,
					salary);

			savingsList.add(savingsAccount);

		}

		DBUtil.commit();

		return savingsList;

	}

}