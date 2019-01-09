package com.moneymoney.account.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.moneymoney.account.SavingsAccount;

public class SavingsAccountDetailsMapper implements RowMapper<SavingsAccount> {

	@Override
	public SavingsAccount mapRow(ResultSet rs, int rowNum) throws SQLException {

		int accountNumber = rs.getInt(1);
		String accountHolderName = rs.getString("accountHolderName");
		double accountBalance = rs.getDouble(3);
		boolean salary = rs.getBoolean("salaried");
		SavingsAccount savingsAccount;
		return new SavingsAccount(accountNumber, accountHolderName, accountBalance, salary);
	}

}
