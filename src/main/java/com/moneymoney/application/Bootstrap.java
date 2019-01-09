package com.moneymoney.application;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.moneymoney.account.ui.AccountCUI;

public class Bootstrap {
	/*static {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection connection = DriverManager.getConnection
					("jdbc:mysql://localhost:3306/bankapp_db", "root", "root");
			//PreparedStatement preparedStatement = 
					//connection.prepareStatement("DELETE FROM ACCOUNT");
			//preparedStatement.execute();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
*/
	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext("context.xml");
		AccountCUI accountCUI = context.getBean(AccountCUI.class);
		accountCUI.start();
	}

}
