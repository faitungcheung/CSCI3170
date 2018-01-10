import java.io.*;
import java.sql.*;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.*;

public class LibraryUser {
private String getStrInput(String problem)
{
    System.out.print(problem);
    BufferedReader s=new BufferedReader(new InputStreamReader(System.in));
    String r="";
    while(r.length()==0)
    {
        try
        {
            r=s.readLine();
        }
        catch(Exception e)
        {
            System.out.println("Invalid input, please try again");   
        }
    }
    return r;
}

private int getIntInput(String problem,int options_num)
{
    System.out.print(problem);
    BufferedReader b=new BufferedReader (new InputStreamReader(System.in));
    int choice = -1;
    while(choice == -1)
    {
        try
        {
            String result=b.readLine();
            choice=Integer.parseInt(result);
            if(choice<1 || choice>options_num)
            {
                System.out.println("Invalid input, please try again");
                choice = -1;  
            }
        }
        catch(Exception e)
        {
            System.out.println("Invalid input, please try again");
        }
    return choice;
    }
}

 private void displayMenu(String name, String[] options)
    {
        System.out.println("\n-----"+name+"-----");
        System.out.println("What kinds of operation would you like to perform?");
        int i=0;
        while(i<options.length)
        {
            int j=i+1;
            System.out.println(j+". "+options[i]);
            i++;
        }
    }

private boolean displayUserMenu()
{
    //display the menu
    String[] options = {"Search for Books", "Show checkout records of a user", "Return to the main menu"};
    displayMenu("Operations for library user menu", options);

    //read the user input
    int choice=getIntInput("Enter Your Choice: ",options.length);
        
    //go to different stream
    switch(choice)
    {
        case 1:
            searchBooks();
            break;
        case 2:
            showLoanRecord();
            break;
        case 3:
            return false;
    }
    return true;
}

private void searchBooks()
{
    //display the options
    System.out.println("Choose the search criterion:");
    System.out.println("1. call number");
    System.out.println("2. title");
    System.out.println("3. author");
        
    //get user's choice and the search keyword
    int choice = getIntInput("Choose the search criterion: ", 3);
    String keyword = getStrInput("Type in the Search Keyword:");
        
    //according to the choice, execute different query
    try
    {
        String Query = "";
        String Word = keyword;
        switch(choice)
        {
            case 1:
                Query = "SELECT DISTINCT call_num FROM Book WHERE call_num = ? ORDER BY call_num ASC";
                break;
            case 2:
                Query = "SELECT DISTINCT call_num FROM Book WHERE title LIKE ? ORDER BY call_num ASC";
                Word = "%"+keyword+"%";
                break;
            case 3:
                Query = "SELECT DISTINCT call_num FROM Author WHERE a_name LIKE ? ORDER BY call_num ASC";
                Word = "%"+keyword+"%";
                break;
        }

        //execute the query to find all the callnum that satisfy the condition
        PreparedStatement pstmt = conn.prepareStatement(Query);
        pstmt.setString(1, Word);
        ResultSet result = pstmt.executeQuery();
        List<String> books = new ArrayList<String>();//use the list<string> to store the output
        while(result.next())
        {
            books.add(result.getString(1));
        }
        pstmt.close();
        if(books.size() == 0)
        {
            System.out.println("[Error]: No match book is found");
            return;
        }

        //output the format
        System.out.println("|Call Number|Title|Author|Available No. of Copies|");
            
        //start to find the other attributes according to the call_num
        Statement stmt = conn.createStatement();
        for(String target_num : books)
        {
            //get the title
            ResultSet rs1 = stmt.executeQuery("SELECT title FROM Book WHERE call_num = '"+target_num+"'");
            rs1.next();
            String title = rs1.getString(1);

            //get the authors
            ResultSet rs2 = stmt.executeQuery("SELECT aname FROM Authorship WHERE call_num = '"+target_num+"'");
            String authors="";
            while(rs2.next())
                authors += ", "+rs2.getString(1);
                
            //get the total copy number
            ResultSet rs3 = stmt.executeQuery("SELECT COUNT(*) FROM Copy WHERE call_num = '"+target_num+"'");
            rs3.next();
            int total_copy = rs3.getInt(1);
                
            //get the checkout_copy number
            ResultSet rs4 = stmt.executeQuery("SELECT COUNT(*) FROM  Checkout_record WHERE call_num = '"+callnum+"' AND return_date IS NULL");
            rs4.next();
            int checkedOut_copy = rs4.getInt(1);
                
            System.out.println("|"+target_num+"|"+title+"|"+authors+"|"+(total_copy-checkedOut_copy)+"|");
        }
        stmt.close();
        System.out.println("End of Query");
    }
    catch(Exception e)
    {
        System.out.println("[Error]: Unable to execute query");
    }
}

private void showLoanRecord()
{
    //read the user's id
    String userid = getStrInput("Enter the User ID: ");
        
    if(userid == null)
        return;
        
        
    try
    {
        System.out.println("Loan Record:");
        System.out.println("|CallNum|CopyNum|Title|Author|Check-out|Returned?|");
            
        PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM Checkout_record where u_id = ?");
        pstmt.setString(1, userid);
        ResultSet rs1 = pstmt.executeQuery();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        Statement stmt = conn.createStatement();
        while(rs1.next())
        {
            //get call_num            
            String callnum = rs1.getString(2);

            //get the copy_num
            int copynum = rs1.getInt(3);
            
            //get title    
            ResultSet rs2 = stmt.executeQuery("SELECT title FROM Book WHERE call_num = '"+callnum+"'");
            rs2.next();
            String title = rs2.getString(1);
            
            //get author name    
            ResultSet rs3 = stmt.executeQuery("SELECT a_name FROM Author WHERE call_num = '"+callnum+"'");
            String authors="";
            while(rs3.next())
                authors += ", "+rs3.getString(1);
            
            //get checkout_date    
            Date checkOut = rs1.getDate(4);

            //judge whether the book is returned
            boolean returned = (rs1.getDate(5)!=null);
            
            String record = "|"+callnum+"|"+copynum+"|"+title+"|"+authors+"|"+df.format(checkOut)+"|";
            if(returned)
            {
                record += "Yes|";
            }
            else
            {
                record += "No|";
            }
            System.out.println(record);
        }
        stmt.close();
        pstmt.close();
        System.out.println("End of Query");
    }
    catch(Exception e)
    {
        System.out.println("[Error]: Unable to execute query");
    }
}
}
