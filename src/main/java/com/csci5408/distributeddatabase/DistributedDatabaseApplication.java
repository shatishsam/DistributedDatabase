package com.csci5408.distributeddatabase;

import com.csci5408.distributeddatabase.analytics.AnalyticsUtil;
import com.csci5408.distributeddatabase.dataexport.DataExport;
import com.csci5408.distributeddatabase.queryexecutor.*;
import com.csci5408.distributeddatabase.reverseengineering.ReverseEngineering;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.*;
import com.csci5408.distributeddatabase.user.Login;
import com.csci5408.distributeddatabase.user.RegUser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Scanner;

@SpringBootApplication
public class DistributedDatabaseApplication {
	public static void main(String[] args) throws Exception {

		SpringApplication.run(DistributedDatabaseApplication.class);

		Scanner sc = new Scanner(System.in);
		System.out.println("========================================");
		System.out.println("DPG23 Distributed database");
		boolean isLogged = false;
		BufferedReader reader = new BufferedReader(
				new InputStreamReader(System.in));
		while (true) {
			System.out.println("Select appropriate option from below list");
			System.out.println("1. User Registration");
			System.out.println("2. User Login");
			System.out.println("3. Exit system");
			System.out.println("========================================");
			System.out.println("\nEnter your choice :");
			sc = new Scanner(System.in);

			final String uinput = sc.nextLine();
			switch (uinput) {
				case "1":
					RegUser registration = new RegUser();
					registration.registerUser();
					break;
				case "2":
					Login login = new Login();
					isLogged = login.loginuser(sc);
					break;
				case "3":
					System.exit(0);
					break;
				default:
					System.out.println("Enter valid input");
			}


			while (isLogged) {
					System.out.println("Please select one from below:::::::::::::");
					System.out.println("1.Write Normal Queries" + "\n" +
							"2.Export" + "\n" + "3.Data Model" + "\n" + "4.Analytics" + "\n" + "5.Write Transaction Queries" + "\n" + "6.Exit");
					int operation = sc.nextInt();
					if (operation == 1) {
						System.out.print("Please enter the query to execute:::::::");
						String sql = reader.readLine();
						QueryExecutor queryExecutor = new QueryExecutor(sql);
						String queryResponse = queryExecutor.executeQuery();
						System.out.println("response for the query " + sql + " is " + queryResponse);
					}
					if(operation==2)
					{
						System.out.print("Please enter database name to export:::::::");
						String databaseName = reader.readLine();
						DataExport dataExport = new DataExport();
						String response = dataExport.exportSQLDump(databaseName);
						System.out.println("response for export is "+response);
					}
					if(operation==3)
					{
						System.out.print("Please enter database name to reverse engineer:::::::");
						String databaseName = reader.readLine();
						ReverseEngineering reverseEngineering = new ReverseEngineering();
						String response = reverseEngineering.reverseEngineering(databaseName);
						System.out.println("response for reverse engineering is "+response);
					}
					if(operation==4)
					{
						System.out.println("printing analytics data");
						AnalyticsUtil analyticsUtil = new AnalyticsUtil();
						System.out.println(analyticsUtil.getAllAnalyticsData());
					}
					if (operation == 5)
					{
						try {
							System.out.print("Please enter the transaction query to execute:::::::" + "\n");
							String sql = reader.readLine();
							QueryExecutor queryExecutor = new QueryExecutor(sql);
							queryExecutor.executeTransaction(sql);
						} catch (Exception exception) {
							exception.printStackTrace();
							System.err.println("Cannot commit transaction: " + exception.getMessage());
						}
					}
					if (operation == 6) {
						isLogged = false;
					}
				}
			}
		}
	}


