/*
str1 = """CREATE TABLE IF NOT EXISTS user_category(
	ucid UNSIGNED INT(1) NOT NULL AUTO_INCREMENT,
	max UNSIGNED INT(2) NOT NULL,
	period UNSIGNED INT(2) NOT NULL,
	PRIMARY KEY(ucid)
);"""
#print(str1)
lst = str1.split("\n")
for i in lst:
	print('psql += "'+i.strip()+'";')
*/

/*
Compile your class(es):
C:\mywork> javac *.java
Create a manifest file and your jar file:
C:\mywork> echo Main-Class: Craps >manifest.txt

C:\mywork> jar cf Craps.jar manifest.txt *.class
or
C:\mywork> jar cf Craps.jar Craps *.class

Test your jar:

C:\mywork> Craps.jar
or
C:\mywork> java -jar Craps.jar

java –cp .:mysql-connector-java-5.1.47(1).jar 3170proj

*/

// package exercise;
/*
import javax.swing.JFrame;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.concurrent.TimeUnit;
*/

import java.sql.*;
// do not import com.mysql.jdbc.*

import java.util.Scanner;
import java.util.regex.Pattern;
import java.nio.file.Path;
import java.nio.file.Paths;

//import java.io.BufferedReader;
//import java.io.FileNotFoundException;
import java.io.FileReader;
//import java.io.IOException;
import java.text.SimpleDateFormat;
import java.sql.Date;


public class LibraryInquirySystem {
	
	/*
	@Override
	public void actionPerformed(ActionEvent eventObject)
	{
		JButton target = (JButton) (eventObject.getSource());
		if (target.getForeground() == Color.GREEN)
			target.setForeground(Color.BLUE);
		else if (target.getForeground() == Color.BLUE) 
		{
			target.setForeground(null);
			shake();
		}
		else
			target.setForeground(Color.GREEN);
	}
	*/

	public static Connection con = null;
	public static Scanner read = new Scanner(System.in);
	
	public static void main(String[] args) { 
		/*
		// reference: https://www.geeksforgeeks.org/ways-to-read-input-from-console-in-java/
		if (args.length > 0) {
			System.out.println(
			"The command line arguments are:");

			// iterating the args array and printing
			// the command line arguments
			for (String val : args)
			System.out.println(val);
		}
		else
			System.out.println("No command line " + "arguments found.");
		*/
		
		String dbAddress = "jdbc:mysql://projgw.cse.cuhk.edu.hk:2633/db11";
		String dbUsername = "Group11";
		String dbPassword = "3170gp11";
		
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			// Class.forName("com.mysql.jdbc.Driver").newInstance()
			con = DriverManager.getConnection(dbAddress, dbUsername, dbPassword);
			// DriverManager.getConnection("jdbc:mysql://localhost/test?" +"user=minty&password=greatsqldb");
			mainMenu();
		} catch (ClassNotFoundException e){
			System.out.println("[Error]: Java MySQL DB Driver not found!!");
			System.exit(0);
		} catch (SQLException e){
			System.out.println(e);
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("SQLState: " + e.getSQLState());
			System.out.println("VendorError: " + e.getErrorCode());
		}
		
		// reference: https://www.geeksforgeeks.org/ways-to-read-input-from-console-in-java/
		// Enter data using BufferReader
		//BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
 
		
		// implement function for each user, and for each user, implement function for each menu, 
		// use while loops
	}
	
	public static void mainMenu() {
		int globalChoice = 4;
		
		do {
			System.out.println("Welcome to Library Inquiry System!");
			System.out.println();
			System.out.println();
			
			System.out.println("-----Main menu-----");
			System.out.println("What kinds of operations would you like to perform?");
			System.out.println("1. Operations for Administrator");
			System.out.println("2. Operations for Library User");
			System.out.println("3. Operations for Librarian");
			System.out.println("4. Exit this program");
			
			while (true) {
				try {
					System.out.print("Enter Your Choice: ");
					String s = read.nextLine();
					globalChoice = Integer.parseInt(s);
					if (1 < globalChoice or globalChoice > 4) {
						throw new Exception();
					}
					break;
				} catch (Exception e) {
					System.out.println("[Error]: Invalid operation number.");
				}
			}
			
			switch (globalChoice) {
				case 1:
					adminMenu();
					break;
				case 2:
					userMenu();
					break;
				case 3:
					librarianMenu();
					break;
				default:
					return;
			}
		} while (globalChoice != 4);
	}
	
	public static void adminMenu() {
		int localChoice = 5;
		
		do {
			// System.out.println("Welcome to Library Inquiry System!");
			System.out.println();
			System.out.println();
			
			System.out.println("-----Operations for administrator menu-----");
			System.out.println("What kind of operation would you like to perform?");
			System.out.println("1. Create all tables");
			System.out.println("2. Delete all tables");
			System.out.println("3. Load from datafile");
			System.out.println("4. Show number of records in each table");
			System.out.println("5. Return to the main menu");
			
			while (true) {
				try {
					System.out.print("Enter Your Choice: ");
					String s = read.nextLine();
					localChoice = Integer.parseInt(s);
					if (1 < localChoice or localChoice > 5) {
						throw new Exception();
					}
					break;
				} catch (Exception e) {
					System.out.println("[Error]: Invalid operation number.");
				}
			}
			
			switch (localChoice) {
				case 1:
					adminMenu1Create();
					break;
				case 2:
					adminMenu2Delete();
					break;
				case 3:
					adminMenu3Load();
					break;
				case 4:
					adminMenu4Count();
					break;
				default:
					return;
			}
		} while (localChoice != 5);
	}
	
	public static void adminMenu1Create() {
		System.out.print("Processing...")
		try {
			// user_category
			String psql = "";
			psql += "CREATE TABLE IF NOT EXISTS user_category( ";
			psql += "ucid UNSIGNED INT(1) NOT NULL AUTO_INCREMENT, ";
			psql += "max UNSIGNED INT(2) NOT NULL, ";
			psql += "period UNSIGNED INT(2) NOT NULL, ";
			psql += "PRIMARY KEY(ucid) ";
			psql += "); ";
			PreparedStatement pstmt = conn.prepareStatement(psql);
			pstmt.executeUpdate();
			
			
			// libuser
			String psql = "";
			psql += "CREATE TABLE IF NOT EXISTS libuser( ";
			psql += "libuid CHAR(10) NOT NULL, ";
			psql += "name VARCHAR(25) NOT NULL, ";
			psql += "age UNSIGNED INT(3) NOT NULL, ";
			psql += "address VARCHAR(100) NOT NULL, ";
			psql += "ucid UNSIGNED INT(1) NOT NULL, ";
			psql += "PRIMARY KEY(libuid), ";
			psql += "FOREIGN KEY(ucid) REFERENCES user_category(ucid) ON DELETE NO ACTION ";
			psql += "); ";
			PreparedStatement pstmt = conn.prepareStatement(psql);
			pstmt.executeUpdate();
			
			
			// book_category
			String psql = "";
			psql += "CREATE TABLE IF NOT EXISTS book_category( ";
			psql += "bcid UNSIGNED INT(1) NOT NULL AUTO_INCREMENT, ";
			psql += "bcname VARCHAR(30) NOT NULL, ";
			psql += "PRIMARY KEY(bcid) ";
			psql += "); ";
			PreparedStatement pstmt = conn.prepareStatement(psql);
			pstmt.executeUpdate();
			
			
			// book
			String psql = "";
			psql += "CREATE TABLE IF NOT EXISTS book( ";
			psql += "callnum CHAR(8) NOT NULL, ";
			psql += "title VARCHAR(30) NOT NULL, ";
			psql += "publish CHAR(10) NOT NULL, ";
			psql += "rating UNSIGNED FLOAT, ";
			psql += "tborrowed UNSIGNED INT(2) NOT NULL DEFAULT 0, ";
			psql += "bcid UNSIGNED INT(1) NOT NULL, ";
			psql += "PRIMARY KEY(callnum), ";
			psql += "FOREIGN KEY(bcid) REFERENCES book_category(bcid) ON DELETE NO ACTION ";
			psql += "); ";
			PreparedStatement pstmt = conn.prepareStatement(psql);
			pstmt.executeUpdate();
			
			
			// copy
			String psql = "";
			psql += "CREATE TABLE IF NOT EXISTS copy( ";
			psql += "callnum CHAR(8) NOT NULL, ";
			psql += "copynum UNSIGNED INT(1) NOT NULL, ";
			psql += "PRIMARY KEY(callnum, copynum), ";
			psql += "FOREIGN KEY(callnum) REFERENCES book(callnum) ON DELETE NO ACTION ";
			psql += "); ";
			PreparedStatement pstmt = conn.prepareStatement(psql);
			pstmt.executeUpdate();
			
			
			// borrow
			String psql = "";
			psql += "CREATE TABLE IF NOT EXISTS borrow( ";
			psql += "libuid CHAR(10) NOT NULL, ";
			psql += "callnum CHAR(8) NOT NULL, ";
			psql += "copynum INT(1) NOT NULL, ";
			psql += "checkout CHAR(10) NOT NULL, ";
			psql += "return CHAR(10), ";
			psql += "PRIMARY KEY (libuid, callnum, copynum, checkout), ";
			psql += "FOREIGN KEY (libuid) REFERENCES libuser, ";
			psql += "FOREIGN KEY (callnum, copynum) REFERENCES copy(callnum, copynum) ON DELETE NO ACTION ";
			psql += "); ";
			PreparedStatement pstmt = conn.prepareStatement(psql);
			pstmt.executeUpdate();
			
			
			// authorship
			String psql = "";
			psql += "CREATE TABLE IF NOT EXISTS authorship( ";
			psql += "aname VARCHAR(25) NOT NULL, ";
			psql += "callnum CHAR(8) NOT NULL, ";
			psql += "PRIMARY KEY (aname, callnum), ";
			psql += "FOREIGN KEY (callnum) REFERENCES book(callnum) ";
			psql += "); ";
			PreparedStatement pstmt = conn.prepareStatement(psql);
			pstmt.executeUpdate();
			
			System.out.println("Done. Database is initialized.");
			
		} catch (Exception e) {
			// reference https://www.geeksforgeeks.org/3-different-ways-print-exception-messages-java/
			e.printStackTrace();
			System.out.println(e);
		}
	}
	public static void adminMenu2Delete() {
		System.out.print("Processing...");
		try {
			String psql = "DROP TABLE IF EXISTS authorship;";
			PreparedStatement pstmt = conn.prepareStatement(psql);
			pstmt.executeUpdate();
			
			String psql = "DROP TABLE IF EXISTS borrow;";
			PreparedStatement pstmt = conn.prepareStatement(psql);
			pstmt.executeUpdate();
			
			String psql = "DROP TABLE IF EXISTS copy;";
			PreparedStatement pstmt = conn.prepareStatement(psql);
			pstmt.executeUpdate();
			
			String psql = "DROP TABLE IF EXISTS book;";
			PreparedStatement pstmt = conn.prepareStatement(psql);
			pstmt.executeUpdate();
			
			String psql = "DROP TABLE IF EXISTS book_category;";
			PreparedStatement pstmt = conn.prepareStatement(psql);
			pstmt.executeUpdate();
			
			String psql = "DROP TABLE IF EXISTS libuser;";
			PreparedStatement pstmt = conn.prepareStatement(psql);
			pstmt.executeUpdate();
			
			String psql = "DROP TABLE IF EXISTS user_category;";
			PreparedStatement pstmt = conn.prepareStatement(psql);
			pstmt.executeUpdate();
			
			System.out.println("Done. Database is removed.");
			
		} catch (Exception e) {
			// reference https://www.geeksforgeeks.org/3-different-ways-print-exception-messages-java/
			e.printStackTrace();
			System.out.println(e);
		}
	}
	public static void adminMenu3Load() {
		while (true) {
			try {
				System.out.print("Type in the Source Data Folder Path: ");
				//Scanner read = new Scanner(System.in);
				String s = read.nextLine();
				System.out.print("Processing...");
				File[] files = new File(s).listFiles( new CheckFileType() {
					@override
					public boolean FileCheck(File s) {
						return s.getName().endsWith("txt");
					}
				});
				if(files == null) {
					System.out.println("\n[ERROR] Not Found.");
				} else {
				for (File file: files){	//foreach loop
					List <List<String>> list = getFileString(file)//create 2d array
					switch(file.getName()){
						case "user_category.txt": {
							try {
								String psq1 = "";
								psq1 += "INSERT INTO user_category (ucid, max, period) ";
								for (List<String> record: list) {
									if(record.size() != 3) {
										System.out.println("\n[ERROR] Invalid Record!");
									} else {
										try {
											psq1 += "VALUES (" + record.get(0) + ", " + record.get(1) + ", " + record.get(2) + "); ";
											PreparedStatement pstmt = conn.prepareStatement(psql);
											pstmt.executeUpdate();
											pstmt.close();
										} catch (Exception e) {
											System.out.println("\n[ERROR] Invalid Record.")
											System.out.println(e.getMessage());
										}
									}
								}
							} catch (SQLException e) {
								System.out.println(e.getMessage());
							}
						} 
						break;
						
						case "user.txt": {
							try {
								String psq1 = "";
								psq1 += "INSERT INTO libuser (libuid, name, age, address, ucid) ";
								for (List<String> record: list) {
									if(record.size() != 5) {
										System.out.println("\n[ERROR] Invalid Record!");
									} else {
										try {
											psq1 += "VALUES (\"" + record.get(0) + "\" , \"" + record.get(1) + "\" , " + record.get(2) +", \"" + record.get(3) + "\" , " + record.get(4) + "); ";
											PreparedStatement pstmt = conn.prepareStatement(psql);
											pstmt.executeUpdate();
											pstmt.close();
										} catch (Exception e) {
											System.out.println("\n[ERROR] Invalid Record.")
											System.out.println(e.getMessage());
										}
									}
								}
							} catch (SQLException e) {
								System.out.println(e.getMessage());
							}
						} 
						break;
						
						case "book_category.txt": {
							try {
								String psq1 = "";
								psq1 += "INSERT INTO book_category (bcid, bcname) ";
								for (List<String> record: list) {
									if(record.size() != 2) {
										System.out.println("\n[ERROR] Invalid Record!");
									} else {
										try {
											psq1 += "VALUES (" + record.get(0) + ", \"" + record.get(1) + "\"); ";
											PreparedStatement pstmt = conn.prepareStatement(psql);
											pstmt.executeUpdate();
											pstmt.close();
										} catch (Exception e) {
											System.out.println("\n[ERROR] Invalid Record.")
											System.out.println(e.getMessage());
										}
									}
								}
							} catch (SQLException e) {
								System.out.println(e.getMessage());
							}
						} 
						break;
						
						case "book.txt": {
							try {
								String psq1 = "";
								String psq2 = "";
								String psq3 = "";
								psq1 += "INSERT INTO book (callnum, title, publish, rating, tborrowed, bcid) ";
								psq2 += "INSERT INTO copy (copynum) ";
								psq3 += "INSERT INTO authorship (aname) ";
								for (List<String> record: list) {
									if(record.size() != 8) {
										System.out.println("\n[ERROR] Invalid Record!");
									} else {
										try {
											//rating can be null or double \t , still will store null in table
											psq1 += "VALUES (\"" + record.get(0) + "\" , \""  + record.get(2)  + "\" , \"" + record.get(4) + "\" , " + record.get(5)+ ", " + record.get(6) + ", " + record.get(7) + "); ";
											psq2 += "VALUES (" + record.get(1)+ "); ";
											psq3 += "VALUES (\"" + record.get(3)+ "\"); ";
											PreparedStatement pstmt = conn.prepareStatement(psql);
											pstmt.executeUpdate();
											pstmt.close();
											pstmt = conn.prepareStatement(psq2);
											pstmt.executeUpdate();
											pstmt.close();
											pstmt = conn.prepareStatement(psq3);
											pstmt.executeUpdate();
											pstmt.close();
										} catch (Exception e) {
											System.out.println("\n[ERROR] Invalid Record.")
											System.out.println(e.getMessage());
										}
									}
								}
							} catch (SQLException e) {
								System.out.println(e.getMessage());
							}
						} 
						break;
						
						case "check_out.txt": {
							try {
								String psq1 = "";
								psq1 += "INSERT INTO book(callnum) ";
								String psq2 = "";
								psq2 += "INSERT INTO copy(copynum) ";
								String psq3 = "";
								psq3 += "INSERT INTO user(libuid) ";
								String psq4 = "";
								psq4 += "INSERT INTO borrow(check_out, `return`) "
								
								for (List<String> record: list) {
									if(record.size() != 5) {
										System.out.println("\n[ERROR] Invalid Record!");
									} else {
										try {
											psq1 += "VALUES (\"" + record.get(0) + "\"); ";
											psq2 += "VALUES (" + record.get(1) + "); ";
											psq3 += "VALUES (\"" + record.get(2) + "\"); ";
											psq4 += "VALUES (\"" + record.get(3) + "\", \"" +record.get(4) + "\"); ");
											PreparedStatement pstmt = conn.prepareStatement(psql);
											pstmt.executeUpdate();
											pstmt.close();
											PreparedStatement pstmt = conn.prepareStatement(psq2);
											pstmt.executeUpdate();
											pstmt.close();
											PreparedStatement pstmt = conn.prepareStatement(psq3);
											pstmt.executeUpdate();
											pstmt.close();
											PreparedStatement pstmt = conn.prepareStatement(psq4);
											pstmt.executeUpdate();
											pstmt.close();
										} catch (Exception e) {
											System.out.println("\n[ERROR] Invalid Record.")
											System.out.println(e.getMessage());
										}
									}
								}
							} catch (SQLException e) {
								System.out.println(e.getMessage());
							}
						} 
						break;
						
						// default:
						
					}
				}
			}
				/*globalChoice = Integer.parseInt(s);
				if (1 < globalChoice or globalChoice > 4) {
					throw new Exception();
				}
				break;*/
			System.out.println("Done. Data is inputted to the database.");
			} catch (Exception e) {
				System.out.println("[Error]: Invalid operation number.");
			}
		}
	}

	public static List <List<String>> getFileString(File file) {
		try {
			Path path = file.toPath();
			List<String> line = Files.readAllLines(path, Charset.forName("US-ASCII"));
			Lis<List<String>> Result = new ArrayList<> (line.size());
			for (String l: line) {
				Result.add(new ArrayList<>(Arrays.asList(l.split("\t"))));
			}
			return Result;
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			return null;
		}
	}

	public static void adminMenu4Count() {
		System.out.println("Number of records in each table:");
		try {

			String psql = "SELECT COUNT(*) FROM user_category;";
			PreparedStatement pstmt = conn.prepareStatement(psql);
			ResultSet resultSet = pstmt.executeQuery();
			// reference https://www.javatpoint.com/PreparedStatement-interface
			if(!(resultSet.next())){
				System.out.println("[Error]: The user_category table is not found. The user_category table has not been created yet.");
			} else {
				System.out.println("user_category: "+resultSet.getInt(1).toString());
			}
			
			String psql = "SELECT COUNT(*) FROM libusr;";
			PreparedStatement pstmt = conn.prepareStatement(psql);
			ResultSet resultSet = pstmt.executeQuery();
			if(!(resultSet.next())){
				System.out.println("[Error]: The libusr table is not found. The libusr table has not been created yet.");
			} else {
				System.out.println("libusr: "+resultSet.getInt(1).toString());
			}
			
			String psql = "SELECT COUNT(*) FROM book_category;";
			PreparedStatement pstmt = conn.prepareStatement(psql);
			ResultSet resultSet = pstmt.executeQuery();
			if(!(resultSet.next())){
				System.out.println("[Error]: The book_category table is not found. The book_category table has not been created yet.");
			} else {
				System.out.println("book_category: "+resultSet.getInt(1).toString());
			}
			
			String psql = "SELECT COUNT(*) FROM book;";
			PreparedStatement pstmt = conn.prepareStatement(psql);
			ResultSet resultSet = pstmt.executeQuery();
			if(!(resultSet.next())){
				System.out.println("[Error]: The book table is not found. The book table has not been created yet.");
			} else {
				System.out.println("book: "+resultSet.getInt(1).toString());
			}
			
			String psql = "SELECT COUNT(*) FROM copy;";
			PreparedStatement pstmt = conn.prepareStatement(psql);
			ResultSet resultSet = pstmt.executeQuery();
			if(!(resultSet.next())){
				System.out.println("[Error]: The copy table is not found. The copy table has not been created yet.");
			} else {
				System.out.println("copy: "+resultSet.getInt(1).toString());
			}
			
			String psql = "SELECT COUNT(*) FROM borrow;";
			PreparedStatement pstmt = conn.prepareStatement(psql);
			ResultSet resultSet = pstmt.executeQuery();
			if(!(resultSet.next())){
				System.out.println("[Error]: The borrow table is not found. The borrow table has not been created yet.");
			} else {
				System.out.println("borrow: "+resultSet.getInt(1).toString());
			}
			
			String psql = "SELECT COUNT(*) FROM authorship;";
			PreparedStatement pstmt = conn.prepareStatement(psql);
			ResultSet resultSet = pstmt.executeQuery();
			if(!(resultSet.next())){
				System.out.println("[Error]: The authorship table is not found. The authorship table has not been created yet.");
			} else {
				System.out.println("authorship: "+resultSet.getInt(1).toString());
			}
		} catch (Exception e) {
			// reference https://www.geeksforgeeks.org/3-different-ways-print-exception-messages-java/
			e.printStackTrace();
						System.out.println(e);
		}

	}
	
	public static void userMenu() {
		int localChoice = 3;
		
		do {
			// System.out.println("Welcome to Library Inquiry System!");
			System.out.println();
			System.out.println();
			
			System.out.println("-----Operations for library user menu-----");
			System.out.println("What kind of operation would you like to perform?");
			System.out.println("1. Search for Books");
			System.out.println("2. Show loan record of a user");
			System.out.println("3. Return to the main menu");
			
			while (true) {
				try {
					System.out.print("Enter Your Choice: ");
					String s = read.nextLine();
					localChoice = Integer.parseInt(s);
					if (1 < localChoice or localChoice > 3) {
						throw new Exception();
					}
					break;
				} catch (Exception e) {
					System.out.println("[Error]: Invalid operation number.");
				}
			}
			
			switch (localChoice) {
				case 1:
					userMenu1Search();
					break;
				case 2:
					userMenu2Show();
					break;
				default:
					return;
			}
		} while (localChoice != 3);
	}
	
	public static void userMenu1Search() {
		System.out.println();
		System.out.println();

		System.out.println("Choose the Search criterion:");
		System.out.println("1. call number");
		System.out.println("2. title");
		System.out.println("3. author");
		int searchChoice = 1;
		while (true) {
			try {
				System.out.print("Choose the search criterion: ");
				String s = read.nextLine();
				searchChoice = Integer.parseInt(s);
				if (1 < searchChoice or searchChoice > 3) {
					throw new Exception();
				}
				break;
			} catch (Exception e) {
				System.out.println("[Error]: Invalid search criterion.");
			}
		}
		
		System.out.print("Type in the Search Keyword:");
		String keyword = read.nextLine();
		String psql = "";
		
		switch (searchChoice) {
			case 1:
				//5211
				String psql += "5211";
				break;
				
			case 2:
				//5212
				String psql += "5212";
				break;
				
			default:
				//5213
				String psql += "5213";
		}
		PreparedStatement pstmt = conn.prepareStatement(psql);
		ResultSet resultSet = pstmt.executeQuery();
		// WIP print the resultSet
		System.out.println("\nEnd of Query");
	}

	public static void userMenu2Show() {
		System.out.println();
		System.out.println();
		
		System.out.print("Enter The User ID: ");
		String userID = read.nextLine();
		String psql = "";
		// 522
		String psql += "522";
		PreparedStatement pstmt = conn.prepareStatement(psql);
		ResultSet resultSet = pstmt.executeQuery();
		if(!(resultSet.next())){
			System.out.println("No record.");
			return;
		}
		while(resultSet.next()){
			// System.out.println("have record.");
		}
		System.out.println("\nEnd of Query");
	}
	
	public static void librarianMenu() {
		int localChoice = 4;
		
		do {
			// System.out.println("Welcome to Library Inquiry System!");
			System.out.println();
			System.out.println();
			
			System.out.println("-----Operations for librarian menu-----");
			System.out.println("What kind of operation would you like to perform?");
			System.out.println("1. Book Borrowing");
			System.out.println("2. Book Returning");
			System.out.println("3. List all un-returned book copies which are checked-out within a period");
			System.out.println("4. Return to the main menu");
			
			while (true) {
				try {
					System.out.print("Enter Your Choice: ");
					String s = read.nextLine();
					localChoice = Integer.parseInt(s);
					if (1 < localChoice or localChoice > 4) {
						throw new Exception();
					}
					break;
				} catch (Exception e) {
					System.out.println("[Error]: Invalid operation number.");
				}
			}
			
			switch (localChoice) {
				case 1:
					librarianMenu1Borrow();
					break;
				case 2:
					librarianMenu2Return();
					break;
				case 3:
					librarianMenu3List();
					break;
				default:
					return;
			}
		} while (localChoice != 4);
	}
	

	public static void librarianMenu1Borrow() {
		System.out.println();
		System.out.println();
		
		System.out.print("Enter The User ID: ");
		String userID = read.nextLine();
		System.out.print("Enter The Call Number: ");
		String callNum = read.nextLine();
		System.out.print("Enter The Copy Number: ");
		int copyNum = read.nextLine();
		
		try{
			if(userID == null) //check if userid input exists
			{
				return;	
			}
			String psq1 = "";
			psq1 += "SELECT COUNT(*) ";
			psq1 += "FROM borrow ";
			psq1 += "WHERE uid = ? ";
			PreparedStatement pstmt = conn.prepareStatement(psq1);
			pstmt.setString(1, userID); //refer 1 is the first question mark
			ResultSet resultSet = pstmt.executeQuery(); //return whether the user is in the list
			resultSet.next();
			if(resultSet.getInt(1) == 0){
                System.out.println("[Error]: Invalid User ID!");
				return null;
			}
			pstmt.close();
			
			String psq2 = "";
			psq2 += "SELECT COUNT(*) ";
			psq2 += "FROM borrow ";
			psq2 += "WHERE callnum = ? AND copynum = ? AND return IS NULL ";
			PreparedStatement pstmt = conn.prepareStatement(psq2);
			pstmt.setString(1, callNum);
			pstmt.setInt(2, copyNum);
			ResultSet resultSet2 = pstmt.executeQuery();
			resultSet2.next();
			
			if(resultSet2.getInt(1) > 0){
				//when the callnum is larger than 0 means the book is checkout
				pstmt.close();
				System.out.println("[Error]: The Book is Checked Out!");
				return;
			}
			pstmt.close();
			
			//check user's borrow quota
			String psq3 = "";
			psq3 += "SELECT max ";
			psq3 += "FROM user_category C, user U ";
			psq3 += "WHERE C.ucid = U.ucid AND U.libuid = ? ";
			PreparedStatement pstmt = conn.prepareStatement(psq3);
			pstmt.setString(1, userID);
			ResultSet resultSet3 = pstmt.executeQuery();
			resultSet3.next();
			int max = resultSet3.getInt(1);
			pstmt.close();
			
			//check the number of borrowed book of the user
			String psq4 = "";
			psq4 += "SELECT COUNT(*) ";
			psq4 += "FROM check_out ";
			psq4 += "WHERE libuid = ? AND return is NULL ";
			PreparedStatement pstmt = conn.prepareStatement(psq4);
			pstmt.setString(1, userID);
			ResultSet resultSet4 = pstmt.executeQuery();
			resultSet4.next();
			if(resultSet4.getInt(1) >= max)
			{
				pstmt.close();
				System.out.println("[Error]: Unavaliable to Borrow Book!");
				return;
			}
			pstmt.close();
			
			//After the checking, the user can borrow book
			Date today = new java.sql.Date(new java.util.Date().getTime());
			String psq5 = "";
			psq5 += "INSERT INTO borrow ";
			psq5 += "VALUES (?, ?, ?, ?, NULL) ";
			PreparedStatement pstmt = conn.prepareStatement(psq5);
			pstmt.setString(1, userID);
			pstmt.setString(2, callNum);
			pstmt.setInt(3, copyNum);
			pstmt.setDate(4, today);// Should be Date?
			pstmt.executeUpdate();
			pstmt.close();
			
			System.out.print("Book borrowing performed successfully.");
		}
		catch(Exception e)
		{
		System.out.println("[Error]: Unexpected Error!"); //need to modified later
		}
		
		//String psql = "";
		// 531 part 1
		//String psql += "531 part 1";
		//PreparedStatement pstmt = conn.prepareStatement(psql);
		//ResultSet resultSet = pstmt.executeQuery();// return whether the book can be borrowed or not
		
		// System.out.print("Book borrowing performed successfully.");
	}
	
	
	public static float updateRating(float prate, int tborrowed,float nrate){
		float arate;
		arate = (prate*tborrowed+nrate)/(tborrowed+1);
		return arate;
	}

	public static void librarianMenu2Return() {
		System.out.println();
		System.out.println();
		
		System.out.print("Enter The User ID: ");
		String userID = read.nextLine();
		System.out.print("Enter The Call Number: ");
		String callNum = read.nextLine();
		System.out.print("Enter The Copy Number: "); 
		int copyNum = read.nextLine();
		System.out.print("Enter Your Rating of the Book: ");
		float rating = read.nextLine();

		try{
			if(userID == null) //check if userid input exists
			{
				return;	
			}
			String psq1 = "";
			psq1 += "SELECT COUNT(*) ";
			psq1 += "FROM borrow ";
			psq1 += "WHERE uid = ? ";
			PreparedStatement pstmt = conn.prepareStatement(psq1);
			pstmt.setString(1, userID); //refer 1 is the first question mark
			ResultSet resultSet = pstmt.executeQuery(); //return whether the user is in the list
			resultSet.next();
			if(resultSet.getInt(1) == 0){
                System.out.println("[Error]: Invalid User ID!");
				return null;
			}
			pstmt.close();
			
			// check whether the user borrowed the book or not
			String psq2 ="";
			psq2 += "SELECT checkout ";
			psq2 += "FROM borrow ";
			psq2 += "WHERE libuid = ? AND callnum = ? AND copynum = ? AND return is NULL ";
			PreparedStatement pstmt = conn.prepareStatement(psq2);
			pstmt.setString(1, userID);
			pstmt.setString(2, callNum);
			pstmt.setInt(3, copyNum);
			ResultSet resultSet2 = pstmt.executeQuery(); 
			if(!resultSet2.next()) // borrow record not found (no borrow)
			{
				System.out.println("[Error]: Not Found!");
				return;
			}
			
			Date check_out_date = resultSet2.getDate(1);
			pstmt.close();
			
			Date today = new java.sql.Date(new java.util.Date().getTime());
			
			//return book
			String psq3 = "";
			psq3 += "UPDATE borrow ";
			psq3 += "SET `return` = ? ";
			psq3 += "WHERE libuid = ? AND callnum = ? AND copynum = ? AND check_out = ? ";
			PreparedStatement pstmt = conn.prepareStatement(psq3);
			pstmt.setDate(1, today);
			pstmt.setString(2, userID);
			pstmt.setString(3, callNum);
			pstmt.setInt(4, copyNum);
			pstmt.setDate(5, check_out_date);
			pstmt.executeUpdate();
			pstmt.close();
			System.out.println("Book returning performed successfully!!!");
			
			
			// get the rating and tborrowed
			float rate;
			String psq4 = "";
			psq4 += "SELECT rating, tborrowed ";
			psq4 += "FROM book ";
			psq4 += "WHERE libuid = ? AND callnum = ? AND copynum = ? ";
			ResultSet resultSet3 = pstmt.executeQuery();
			resultSet3.next();
			rate = updateRating(resultSet3.getInt(1), resultSet3.getInt(2), rating);
			int borrowed_times = resulSet3.getInt(2) + 1;
			pstmt.close();
			
			//update the rating of the book
			String psq5 = "";
			psq5 += "UPDATE borrow ";
			psq5 += "SET rating = ? ";
			psq5 += "WHERE libuid = ? AND callnum = ? AND copynum = ?";
			PreparedStatement pstmt = conn.prepareStatement(psq5);
			pstmt.setFloat(1, rate);
			pstmt.setString(2, userID);
			pstmt.setString(3, callNum);
			pstmt.setInt(4, copyNum);
			pstmt.executeUpdate();
			pstmt.close();
			
			
			//update tborrowed 
			String psq6 = "";
			psq6 += "UPDATE borrow ";
			psq6 += "SET tborrowed = ? ";
			psq6 += "WHERE libuid = ? AND callnum = ? AND copynum = ?";
			PreparedStatement pstmt = conn.prepareStatement(psq6);
			pstmt.setInt(1, borrowed_times);
			pstmt.setString(2, userID);
			pstmt.setString(3, callNum);
			pstmt.setInt(4, copyNum);
			pstmt.executeUpdate();
			pstmt.close();
			
			// select both rating and tborrowed 
			// use result.getInt() to get the variable
			
		}
		catch(Exception e)
		{
			System.out.println("[Error]: Unexpected Error!"); 
		}
		
		//String psql = "";
		// 532 part 1
		//String psql += "532 part 1";
		//PreparedStatement pstmt = conn.prepareStatement(psql);
		//ResultSet resultSet = pstmt.executeQuery();
		
		// System.out.print("Book returning performed successfully.");
	}
	
	
	
	public static void librarianMenu3List() {
		System.out.println();
		System.out.println();
		
		System.out.print("Type in the starting date [dd/mm/yyyy]: ");
		String startDate = read.nextLine();
		System.out.print("Type in the ending date [dd/mm/yyyy]:");
		String endDate = read.nextLine();
		
		SimpleDateFormat date = new SimpleDateFormat("dd/MM/yyyy");
		try{
			String psq1 = "";
			psq1 += "SELECT * ";
			psq1 += "FROM borrow ";
			psq1 += "WHERE check_out >= ? AND check_out <= ? AND `return` is NULL ";
			psq1 += "ORDER BY check_out DESC ";
			PreparedStatement pstmt = conn.prepareStatement(psq1);
			pstmt.setDate(1, startDate);
			pstmt.setDate(2, endDate);
			ResultSet resultSet1 = pstmt.executeQuery(); //return whether the user is in the list
			System.out.println("List of UnReturned Book:\n|LibUID|CallNum|CopyNum|Checkout|"); 
			while(resultSet1.next())
			{
				String result = "|" + resultSet1.getString(1) + "|" + resultSet1.getString(2) + "|" + resultSet1.getString(3) + "|" + date.format(resultSet1.getDate(4));
				System.out.println(result);
			}
			pstmt.close();
			System.out.println("\nEnd of Query");
		}
		catch(Exception e)
		{
			System.out.println("\n[ERROR]: Unable to list!");
		}
		
		// if else System.out.println("List of UnReturned Book:"); 
		//String psql = "";
		// 533
		//String psql += "533";
		//PreparedStatement pstmt = conn.prepareStatement(psql);
		//ResultSet resultSet = pstmt.executeQuery();
		
		//System.out.println("\nEnd of Query");
	}
}
