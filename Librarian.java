import java.io.*;
import java.sql.*;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.*;

public class Librarian{

	private boolean displayLibrarianMenu(){
		String[] options = {"Book Borrowing", "Book Returning", "Return to the main menu"};
        int choice = LibraryInquirySystem.displayMenu("Operations for librarian menu", options);
        switch(choice){
            case 1:
                borrowBook();
                break;
            case 2:
                returnBook();
                break;
	    case 3:
		getUnreturnedBook();
            case 4:
                return false;
        }
        return true;
    }

    private void borrowBook(){
        BufferedReader in = new BufferedReader (new InputStreamReader(System.in));
        String user = getStrInput(in, "Enter the User ID: ");
        String callnum = LibraryInquirySystem.getStrInput(in, "Enter the Call Number: ");

		//constraint the number of copys
        int copynum = LibraryInquirySystem.getIntInput(in, "Enter the Copy Number: ", new int[]{1, 10});

        try{
			//check whether the user exist
			if(user == null){
				return;
			}
			PreparedStatement pstmt = LibraryInquirySystem.conn.prepareStatement("SELECT COUNT(*) FROM User where u_id = ?");
            pstmt.setString(1, user);
            ResultSet rs1 = pstmt.executeQuery();
            rs1.next();
            if(rs1.getInt(1) == 0){
                System.out.println("[Error]: No such user");
                return null;
            }
            pstmt.close();

			// check if the book copy exist
            pstmt = LibraryInquirySystem.conn.prepareStatement("SELECT COUNT(*) FROM Copy WHERE call_num = ? AND copy_num = ?");
            pstmt.setString(1, callnum);
            pstmt.setInt(2, copynum);
            ResultSet rs1 = pstmt.executeQuery();
            rs1.next();
            if(rs1.getInt(1) == 0){
                pstmt.close();
                System.out.println("[Error] There is no such book copy!");
                return;
            }
            pstmt.close();

			//check whether the book is checked out
            pstmt = LibraryInquirySystem.conn.prepareStatement("SELECT COUNT(*) FROM Checkout_record WHERE call_num = ? AND copy_num = ? AND return_date IS NULL");
            pstmt.setString(1, callnum);
            pstmt.setInt(2, copynum);
            ResultSet rs2 = pstmt.executeQuery();
            rs2.next();
            if(rs2.getInt(1) > 0){
                pstmt.close();
                System.out.println("[Error] Book copy is checked out now");
                return;
            }
            pstmt.close();

			//check whether reached the max_books
            pstmt = LibraryInquirySystem.conn.prepareStatement("SELECT max_books FROM Category C, User L WHERE C.c_id = L.cid AND L.u_id = ?");
            pstmt.setString(1, user);
            ResultSet rs3 = pstmt.executeQuery();
            rs3.next();
            int max = rs3.getInt(1);
            pstmt.close();
            pstmt = conn.prepareStatement("SELECT COUNT(*) FROM Checkout_record WHERE u_id = ? AND return_date IS NULL");
            pstmt.setString(1, user);
            ResultSet rs4 = pstmt.executeQuery();
            rs4.next();
            if(rs4.getInt(1) >= max){
                pstmt.close();
                System.out.println("[Error] User has reached the checkout limit");
                return;
            }
            pstmt.close();

            Date today = new java.sql.Date(new java.util.Date().getTime());
            pstmt = LibraryInquirySystem.conn.prepareStatement("INSERT INTO Checkout_record VALUES (?, ?, ?, ?, NULL)");
            pstmt.setString(1, user);
            pstmt.setString(2, callnum);
            pstmt.setInt(3, copynum);
            pstmt.setDate(4, today);
            pstmt.executeUpdate();
            pstmt.close();

            System.out.println("Book borrowing performed successfully!!!");
        }catch(Exception e){
            System.out.println("[Error] Unable to perform operation");
        }
    }

    private void returnBook(){
        BufferedReader in = new BufferedReader (new InputStreamReader(System.in));
        String user = LibraryInquirySystem.getStrInput(in, "Enter the User ID: ");
		if(user == null){
            return;
        }
        String callnum = LibraryInquirySystem.getStrInput(in, "Enter the Call Number: ");
        int copynum = LibraryInquirySystem.getIntInput(in, "Enter the Copy Number: ", new int[]{1, 10});
        try{
			//check whether the user exists
			PreparedStatement pstmt = conn.prepareStatement("SELECT COUNT(*) FROM User where u_id = ?");
            pstmt.setString(1, user);
            ResultSet rs = pstmt.executeQuery();
            rs.next();
            if(rs.getInt(1) == 0){
                System.out.println("[Error]: No such user");
                return null;
            }
            pstmt.close();

			//check whether the borrowed record exist
            PreparedStatement pstmt = LibraryInquirySystem.conn.prepareStatement("SELECT checkout_date FROM Checkout_record WHERE u_id = ? AND call_num = ? AND copy_num = ? AND return_date IS NULL");
            pstmt.setString(1, user);
            pstmt.setString(2, callnum);
            pstmt.setInt(3, copynum);
            ResultSet rs = pstmt.executeQuery();
            if(!rs.next()){
                System.out.println("[Error] An matching borrow record is not found.");
                return;
            }
            Date checkOut = rs.getDate(1);
            pstmt.close();
            Date today = new java.sql.Date(new java.util.Date().getTime());

			//update
            pstmt = LibraryInquirySystem.conn.prepareStatement("UPDATE Checkout_record SET return_date = ? WHERE u_id = ? AND call_num = ? AND copy_num = ? AND checkout_date = ?");
            pstmt.setDate(1, today);
            pstmt.setString(2, user);
            pstmt.setString(3, callnum);
            pstmt.setInt(4, copynum);
            pstmt.setDate(5, checkOut);
            pstmt.executeUpdate();
            pstmt.close();
            System.out.println("Book returning performed successfully!!!");
        }catch(Exception e){
            System.out.println("[Error] Unable to perform operation");
        }
    }

    private boolean displayDirectorMenu(){
        String[] options = {"List all un-returned book copies which are checked-out within a period", "Return to the main menu"};
        int choice = LibraryInquirySystem.displayMenu("Operations for library director menu", options);
        switch(choice){
            case 1:
                listUnReturned();
                break;
            case 2:
                return false;
        }
        return true;
    }

    private void listUnReturned(){
        BufferedReader in = new BufferedReader (new InputStreamReader(System.in));
        Date startDate = getDateInput(in, "Type in the starting date [dd/mm/yyyy]: ");
        Date endDate = getDateInput(in, "Type in the ending date [dd/mm/yyyy]: ");

        SimpleDateFormat date = new SimpleDateFormat("dd/MM/yyyy");
        try{
            PreparedStatement pstmt = LibraryInquirySystem.conn.prepareStatement("SELECT * FROM Checkout_record WHERE checkout_date >= ? AND checkout_date <= ? AND return_date IS NULL ORDER BY checkout_date DESC");
            pstmt.setDate(1, startDate);
            pstmt.setDate(2, endDate);
            ResultSet rs = pstmt.executeQuery();
            System.out.println("List of UnReturned Book:\n|LibUID|CallNum|CopyNum|Checkout|");
            while(rs.next()){
				String res = "|"+rs.getString(1)+"|"+rs.getString(2)+"|"+rs.getString(3)+"|"+date.format(rs.getDate(4))+"|";
                System.out.println(res);
            }
            pstmt.close();
            System.out.println("End of Query");
        }catch(Exception e){
            System.out.println("[Error]: Unable to make query");
        }
    }

    private Date getDateInput(BufferedReader in, String question){
        Date result = null;
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        while(result == null){
            System.out.print(question);
            try {
                String input = in.readLine();
                result = new java.sql.Date(df.parse(input).getTime());
            }catch(Exception e){
                System.out.println("Invalid input, please try again");
            }
        }
        return result;
    }

}
