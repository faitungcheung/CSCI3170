import java.io.*;
import java.sql.*;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.*;

public class Administrator {

	 private boolean displayAdminMenu(Connection conn){
        String[] options = {"Create all tables", 
			"Delete all tables", 
			"Load from datafile", 
			"Show number of records in each table", 
			"Return to the main menu"};
        int choice = LibraryInquirySystem.displayMenu("Operations for administrator menu", options);
        switch(choice){
            case 1:
                createTables(conn);
                break;
            case 2:
                dropTables(conn);
                break;
            case 3:
                loadData(conn);
                break;
            case 4:
                countRecords(conn);
                break;
            case 5:
                return false;
        }
        return true;
    }

    private void createTables(Connection conn){
        System.out.print("Processing...");
        try{
            Statement stmt = conn.createStatement();
			//create table category
            String table1 = "CREATE TABLE Category ("+
				"c_id INT NOT NULL, "+
				"max_books INT NOT NULL, "+
				"loan_period INT NOT NULL, "+
				"PRIMARY KEY(c_id))";

			//create table user
            String table2 = "CREATE TABLE User (u_id VARCHAR(10) NOT NULL, "+
				"u_name VARCHAR(25) NOT NULL, "+
				"address VARCHAR(100) NOT NULL, "+
				"c_id INT NOT NULL, "+
				"PRIMARY KEY(u_id))";

			//create table book
            String table3 = "CREATE TABLE Book (call_num VARCHAR(8) NOT NULL, "+
				"title VARCHAR(30) NOT NULL, "+
				"publish_date DATE NOT NULL, "+
				"PRIMARY KEY(call_num))";

			//create table copy
            String table4 = "CREATE TABLE Copy (call_num VARCHAR(8) NOT NULL, "+
				"copy_num INT NOT NULL, "+
				"PRIMARY KEY(call_num, copynum), "+
				"FOREIGN KEY (call_num) REFERENCES Book(call_num))";

			//create table checkout_record
            String table5 = "CREATE TABLE Checkout_record (u_id VARCHAR(10) NOT NULL, "+
				"call_num VARCHAR(8) NOT NULL, "+
				"copy_num INT NOT NULL, "+
				"checkout_date DATE NOT NULL, "+
				"return_date DATE, "+
				"PRIMARY KEY(u_id, call_num, "+
				"copy_num, checkout_date), "+
				"FOREIGN KEY (u_id) REFERENCES Libuser(u_id), "+
				"FOREIGN KEY (call_num, copy_num) REFERENCES Copy(call_num, copy_num))";

			//create table author
            String table6 = "CREATE TABLE Author (a_name VARCHAR(25) NOT NULL, "+
				"call_num VARCHAR(8) NOT NULL, "+
				"PRIMARY KEY(a_name, call_num), "+
				"FOREIGN KEY (call_num) REFERENCES Book(call_num))";

            System.out.println(table1);
            stmt.executeUpdate(table2);
            stmt.executeUpdate(table3);
            stmt.executeUpdate(table4);
            stmt.executeUpdate(table5);
            stmt.executeUpdate(table6);
            stmt.close();
        }catch(Exception e){
            System.out.println("\n[Error]: Failed to create tables, Please try again!");
            return;
        }
        System.out.println("Got it! Database is initialized!");
    }

    private void dropTables(Connection conn){
        System.out.print("Processing...");
        try {
            Statement stmt = conn.createStatement();

			//drop all tables
            stmt.executeUpdate("drop table Author");
			stmt.executeUpdate("drop table Checkout_record");
            stmt.executeUpdate("drop table Copy");
            stmt.executeUpdate("drop table Book");
			stmt.executeUpdate("drop table User");
            stmt.executeUpdate("drop table Category");
        }catch(Exception e){
            System.out.println("\n[Error]: Failed to drop tables, Please try again!");
            return;
        }
        System.out.println("Done! Database is removed!");
    }

    private List<String[]> getStrData(String folder, String fileName) {
        BufferedReader in = readFile(folder, fileName);
        if(in == null){
            return null;
        }
        List<String[]> data = new ArrayList<String[]>();
        try{
            String line = in.readLine();
            while(line != null) {
                if (line.length() > 0) {
                    System.out.println(line.split("\t"));
                    data.add(line.split("\t"));
                }
                line = in.readLine();
            }
        }catch(Exception e){
            System.out.println("Invalid format: '"+folder+"/"+fileName+"'");
            return null;
        }
        return data;
    }

    private void loadData(Connection conn){
        System.out.print("Type in the Source Data Folder Path: ");
        BufferedReader in = new BufferedReader (new InputStreamReader(System.in));
        try{
            String folder = in.readLine();
            System.out.print("Processing...");
            List<String[]> category= getStrData(folder, "category.txt");
            List<String[]> user= getStrData(folder, "user.txt");
            List<String[]> book= getStrData(folder, "book.txt");
            List<String[]> checkOut= getStrData(folder, "checkout.txt");
            
            if(category == null || user == null || book == null || checkOut == null){
                System.out.println("\n[Error]: Wrong path or Missing file");
                return;
            }

			//insert into table category
            PreparedStatement pstmt, pstmt2, pstmt3;
            pstmt = conn.prepareStatement("INSERT INTO Category VALUES (?, ?, ?)");
            for(String[] line : category){
                pstmt.setInt(1, Integer.parseInt(line[0]));
                pstmt.setInt(2, Integer.parseInt(line[1]));
                pstmt.setInt(3, Integer.parseInt(line[2]));
                pstmt.executeUpdate();
            }
            pstmt.close();

			//insert into user table
            pstmt = conn.prepareStatement("INSERT INTO User VALUES (?, ?, ?, ?)");
            for(String[] line : user){
                pstmt.setString(1, line[0]);
                pstmt.setString(2, line[1]);
                pstmt.setString(3, line[2]);
                pstmt.setInt(4, Integer.parseInt(line[3]));
                pstmt.executeUpdate();
            }
            pstmt.close();

			//insert into book, copy, author tables
            SimpleDateFormat Data = new SimpleDateFormat("dd/MM/yyyy");
            pstmt = conn.prepareStatement("INSERT INTO Book VALUES (?, ?, ?)");
            pstmt2 = conn.prepareStatement("INSERT INTO Copy VALUES (?, ?)");
            pstmt3 = conn.prepareStatement("INSERT INTO Author VALUES (?, ?)");
            for(String[] line : book){
                pstmt.setString(1, line[0]);
                pstmt.setString(2, line[2]);
                pstmt.setDate(3, new java.sql.Date(Data.parse(line[4]).getTime()));
                pstmt.executeUpdate();
                int copies = Integer.parseInt(line[1]);
                for(int j=0; j<copies; j++) {
                    pstmt2.setString(1, line[0]);
                    pstmt2.setInt(2, j + 1);
                    pstmt2.executeUpdate();
                }
                String[] authors = line[3].split(",");
                for(String name : authors){
                    pstmt3.setString(1, name);
                    pstmt3.setString(2, line[0]);
                    pstmt3.executeUpdate();
                }
            }
            pstmt.close();
            pstmt2.close();
            pstmt3.close();

			//insert into checkout_record
            pstmt = conn.prepareStatement("INSERT INTO Checkout_record" VALUES (?, ?, ?, ?, ?)");
            for(String[] line : checkOut){
                if(!line[4].equals("null")){
                    pstmt.setDate(5, new java.sql.Date(df.parse(line[4]).getTime()));
                }else{
                    pstmt.setDate(5, null);
                }
                pstmt.setString(1, line[2]);
                pstmt.setString(2, line[0]);
                pstmt.setInt(3, Integer.parseInt(line[1]));
                pstmt.setDate(4, new java.sql.Date(df.parse(line[3]).getTime()));
                pstmt.executeUpdate();
            }
            pstmt.close();
        }catch(Exception e){
            System.out.println("\n[Error]: Invalid format");
            return;
        }
        System.out.println("Done! Data is inputted to the database!");
    }

    private void countRecords(Connection conn){
        String[] tables = {"Author", "Book", "Checkout_record", "Category", "Copy", "User"};
        Statement stmt = conn.createStatement();
        for(String table : tables){
			try{
				//countRecords
				ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM "+table);
                rs.next();
                int count = rs.getInt(1);
                System.out.println(table+": "+count);
            }
			catch(Exception e){
				System.out.println("[Error]: Failed to retrieve "+ table);
			}
		}
	}
}
