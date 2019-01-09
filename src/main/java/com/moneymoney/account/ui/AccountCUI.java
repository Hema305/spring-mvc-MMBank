package com.moneymoney.account.ui;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.moneymoney.account.SavingsAccount;
import com.moneymoney.account.service.SavingsAccountService;
import com.moneymoney.account.util.DBUtil;
import com.moneymoney.exception.AccountNotFoundException;

@Component
public class AccountCUI {
	private static Scanner scanner = new Scanner(System.in);
	@Autowired
	private SavingsAccountService savingsAccountService;

	public void start() {

		do {
			System.out.println("****** Welcome to Money Money Bank********");
			System.out.println("1. Open New Savings Account");
			System.out.println("2. Update Account");
			System.out.println("3. Close Account");
			System.out.println("4. Search Account");
			System.out.println("5. Withdraw");
			System.out.println("6. Deposit");
			System.out.println("7. FundTransfer");
			System.out.println("8. Check Current Balance");
			System.out.println("9. Get All Savings Account Details");
			System.out.println("10. Sort Accounts");
			System.out.println("11. Exit");
			System.out.println("Make your choice: ");

			int choice = scanner.nextInt();

			performOperation(choice);

		} while (true);
	}

	private void performOperation(int choice) {
		SavingsAccount savingsAccount = null;
		switch (choice) {
		case 1:
			acceptInput("SA");
			break;
		case 2:
			System.out.println("Enter your account number to update : ");
			int accountId = scanner.nextInt();
			try {
				savingsAccount = savingsAccountService.getAccountById(accountId);
			} catch (ClassNotFoundException | SQLException | AccountNotFoundException e1) {
				e1.printStackTrace();
			}
			System.out.println("To Update Your Name Enter " + "1");
			System.out.println("To Update Your Salary Type Enter " + "2");
			System.out.println("To Update Your Name and Salary Type Enter " + "3");
			int select = scanner.nextInt();
			selectOptionsToUpdate(select, savingsAccount);
			break;
		case 3:
			closeAccount();
			break;
		case 4:
			searchAccount();
			break;
		case 5:
			withdraw();
			break;
		case 6:
			deposit();
			break;
		case 7:
			fundTransfer();
			break;
		case 8:
			checkBalance();
			break;
		case 9:
			showAllAccounts();
			break;
		case 10:
			sortAccounts();
			break;
		case 11:
			try {
				DBUtil.closeConnection();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			System.exit(0);
			break;
		default:
			System.err.println("Invalid Choice!");
			break;
		}

	}

	private void acceptInput(String type) {
		if (type.equalsIgnoreCase("SA")) {
			System.out.println("Enter your Full Name: ");
			String accountHolderName = scanner.nextLine();
			accountHolderName = scanner.nextLine();
			System.out.println("Enter Initial Balance(type na for Zero Balance): ");
			String accountBalanceStr = scanner.next();
			double accountBalance = 0.0;
			if (!accountBalanceStr.equalsIgnoreCase("na")) {
				accountBalance = Double.parseDouble(accountBalanceStr);
			}
			System.out.println("Salaried?(y/n): ");
			boolean salary = scanner.next().equalsIgnoreCase("n") ? false : true;
			createSavingsAccount(accountHolderName, accountBalance, salary);
		}
	}

	private void createSavingsAccount(String accountHolderName, double accountBalance, boolean salary) {
		try {
			savingsAccountService.createNewAccount(accountHolderName, accountBalance, salary);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void selectOptionsToUpdate(int select, SavingsAccount savingsAccount2) {

		switch (select) {

		case 1:
			System.out.println("Enter new name change your name : ");
			String changeName = scanner.nextLine();
			changeName = scanner.nextLine();
			savingsAccount2.getBankAccount().setAccountHolderName(changeName);
			try {
			
				savingsAccountService.updateAccount(savingsAccount2);
					System.out.println("Name Changed for " + savingsAccount2.getBankAccount().getAccountNumber()
							+ " to " + changeName);
				

			} catch (ClassNotFoundException | SQLException e) {

				e.printStackTrace();
			}

			break;

		case 2:

			System.out.println("If your account type is salaried enter (n) to change your account type as unsalaried");
			System.out.println("If your account type is savings enter (y)to change your account type as salaried");
			boolean changeSalaryType = scanner.next().equalsIgnoreCase("n") ? false : true;
			savingsAccount2.setSalary(changeSalaryType);

			try {
				savingsAccountService.updateAccount(savingsAccount2);
				System.out.println("Salary type of " + savingsAccount2.getBankAccount().getAccountNumber()
						+ "Changed to " + changeSalaryType);
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}

			break;

		case 3:
			System.out.println("Enter new name change your name : ");
			String changename = scanner.nextLine();
			changename = scanner.nextLine();
			savingsAccount2.getBankAccount().setAccountHolderName(changename);
			System.out.println("If your account type is salaried enter (n) to change your account type as unsalaried");
			System.out.println("If your account type is savings enter (y)to change your account type as salaried");
			boolean changeTypeOfSalary = scanner.next().equalsIgnoreCase("n") ? false : true;
			savingsAccount2.setSalary(changeTypeOfSalary);

			try {
				savingsAccountService.updateAccount(savingsAccount2);
					System.out.println("Name and Salary type for " + savingsAccount2.getBankAccount().getAccountNumber()
							+ " Changed to " + changename + " and " + changeTypeOfSalary);
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}
			break;

		default:
			System.err.println("Invalid Choice!");
			break;
		}
	}

	private void closeAccount() {
		System.out.println("Enter Account Number to be closed : ");
		int deleteAccountNumber = scanner.nextInt();
		try {
			savingsAccountService.deleteAccount(deleteAccountNumber);
		} catch (AccountNotFoundException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void searchAccount() {
		do {
			System.out.println("+++Ways of Searching+++");
			System.out.println("1.Search By Account Id");
			System.out.println("2.Search By Account Holder Name");
			System.out.println("3.Enter amount to return accounts less than that amount");
			System.out.println("4.Enter amount to return accounts greater than that amount");
			System.out.println("5.Redirect to start menu");
			int search = scanner.nextInt();

			switch (search) {
			case 1:
				System.out.println("Enter account number to search");
				int accountNumber = scanner.nextInt();
				try {
					SavingsAccount savingsAccount = savingsAccountService.getAccountById(accountNumber);
					System.out.println(savingsAccount);
				} catch (ClassNotFoundException | SQLException | AccountNotFoundException e) {
					e.printStackTrace();
				}
				break;
			case 2:
				System.out.println("Enter Account Holder  to search");
				String accountHolderName = scanner.nextLine();
				accountHolderName = scanner.nextLine();
				try {
					List<SavingsAccount> savingsAccount = savingsAccountService.getAccountByName(accountHolderName);
					System.out.println(savingsAccount);
				} catch (ClassNotFoundException | SQLException | AccountNotFoundException e) {
					e.printStackTrace();
				}
				break;
			case 3:
				System.out.println("Enter balance to get accounts with balance less than or equal to given balance");
				int balanceNumber = scanner.nextInt();
				try {
					List<SavingsAccount> savingsListLess = savingsAccountService.getAllBelowBalance(balanceNumber);
					for (SavingsAccount savings : savingsListLess) {
						System.out.println(savings);
					}
				} catch (ClassNotFoundException | SQLException e) {
					e.printStackTrace();
				}
				break;
			case 4:
				System.out.println("Enter balance to get accounts with balance greater than or equal to given balance");
				int aboveBalanceNumber = scanner.nextInt();
				try {
					List<SavingsAccount> savingsListAbove = savingsAccountService
							.getAllAboveBalance(aboveBalanceNumber);
					for (SavingsAccount savings : savingsListAbove) {
						System.out.println(savings);
					}
				} catch (ClassNotFoundException | SQLException e) {

					e.printStackTrace();
				}
				break;
			case 5:
				start();
				break;
			default:
				System.err.println("Invalid Choice!");
				break;
			}
		} while (true);
	}

	private void withdraw() {
		System.out.println("Enter Account Number: ");
		int accountNumber = scanner.nextInt();
		System.out.println("Enter Amount: ");
		double amount = scanner.nextDouble();
		SavingsAccount savingsAccount = null;
		try {
			savingsAccount = savingsAccountService.getAccountById(accountNumber);
			savingsAccountService.withdraw(savingsAccount, amount);
			DBUtil.commit();
		} catch (ClassNotFoundException | SQLException | AccountNotFoundException e) {
			try {
				DBUtil.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		} catch (Exception e) {
			try {
				DBUtil.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
	}

	private void deposit() {
		System.out.println("Enter Account Number: ");
		int accountNumber = scanner.nextInt();
		System.out.println("Enter Amount: ");
		double amount = scanner.nextDouble();
		SavingsAccount savingsAccount = null;
		try {
			savingsAccount = savingsAccountService.getAccountById(accountNumber);
			savingsAccountService.deposit(savingsAccount, amount);
			DBUtil.commit();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			try {
				DBUtil.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		} catch (Exception e) {
			try {
				DBUtil.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
	}

	private void fundTransfer() {
		System.out.println("Enter Account Sender's Number: ");
		int senderAccountNumber = scanner.nextInt();
		System.out.println("Enter Account Receiver's Number: ");
		int receiverAccountNumber = scanner.nextInt();
		System.out.println("Enter Amount: ");
		double amount = scanner.nextDouble();
		try {
			SavingsAccount senderSavingsAccount = savingsAccountService.getAccountById(senderAccountNumber);
			SavingsAccount receiverSavingsAccount = savingsAccountService.getAccountById(receiverAccountNumber);
			savingsAccountService.fundTransfer(senderSavingsAccount, receiverSavingsAccount, amount);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void checkBalance() {
		System.out.println("Enter Account Number: ");
		int accountNumber = scanner.nextInt();
		try {
			double balance = savingsAccountService.checkBalance(accountNumber);
			System.out.println("Available balance : " + balance + " rs");
		} catch (ClassNotFoundException | SQLException | AccountNotFoundException e) {
			e.printStackTrace();
		}

	}

	private void showAllAccounts() {
		List<SavingsAccount> savingsAccounts;
		try {
			savingsAccounts = savingsAccountService.getAllSavingsAccount();
			for (SavingsAccount savingsAccount : savingsAccounts) {
				System.out.println(savingsAccount);
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}

	}

	private void sortAccounts() {
		do {
			System.out.println("+++Ways of Sorting+++");
			System.out.println("1.Sort By Account Holder Name");
			System.out.println("2.Enter account balance range to sort in ascending order of the balance");
			System.out.println("3.Sort By Account Holder Name in descending order");
			System.out.println("4.Enter account balance range to sort in descending order of the balance");
			System.out.println("5.Sort By Account Balance");
			System.out.println("6.Sort By Account Balance in descending order");
			System.out.println("7.Redirect to start menu");
			int choose = scanner.nextInt();
			List<SavingsAccount> savingsAccountsList = null;

			switch (choose) {
			case 1:
				try {
					savingsAccountsList = savingsAccountService.sortByAccountHolderName();
					for (SavingsAccount savings : savingsAccountsList) {
						System.out.println(savings);
					}
				} catch (ClassNotFoundException | SQLException e) {
					e.printStackTrace();
				}
				break;
			case 2:
				System.out.println("Enter minimun range");
				int minimumBalance = scanner.nextInt();
				System.out.println("Enter maximum range");
				int maximumBalance = scanner.nextInt();
				try {
					savingsAccountsList = savingsAccountService.sortByBalanceRange(minimumBalance, maximumBalance);
					for (SavingsAccount savingsAccount : savingsAccountsList) {
						System.out.println(savingsAccount);
					}
				} catch (ClassNotFoundException | SQLException e) {
					e.printStackTrace();
				}
				break;
			case 3:
				try {
					savingsAccountsList = savingsAccountService.sortByAccountHolderNameDescending();
					for (SavingsAccount savings : savingsAccountsList) {
						System.out.println(savings);
					}
				} catch (ClassNotFoundException | SQLException e) {
					e.printStackTrace();
				}
				break;
			case 4:
				System.out.println("Enter minimun range");
				int minimumBalanceDescending = scanner.nextInt();
				System.out.println("Enter maximum range");
				int maximumBalanceDescending = scanner.nextInt();
				try {
					savingsAccountsList = savingsAccountService.sortByBalanceRangeDescending(minimumBalanceDescending,
							maximumBalanceDescending);
					for (SavingsAccount savingsAccount : savingsAccountsList) {
						System.out.println(savingsAccount);
					}
				} catch (ClassNotFoundException | SQLException e) {
					e.printStackTrace();
				}

				break;
			case 5:
				try {
					savingsAccountsList = savingsAccountService.sortByAccountBalance();
					for (SavingsAccount savings : savingsAccountsList) {
						System.out.println(savings);
					}
				} catch (ClassNotFoundException | SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				break;
			case 6:
				try {
					savingsAccountsList = savingsAccountService.sortByAccountBalanceDescending();
					for (SavingsAccount savings : savingsAccountsList) {
						System.out.println(savings);
					}
				} catch (ClassNotFoundException | SQLException e) {
					e.printStackTrace();
				}

				break;
			case 7:
				start();
				break;
			default:
				System.err.println("Invalid Choice!");
				break;
			}
		} while (true);

	}
}