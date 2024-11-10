import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Scanner;

public class contacts {
    public static void main(String args[]) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return;
        }

        try (Connection con = DriverManager.getConnection(
                     "jdbc:mysql://localhost:3306/contact", "root", "Tharun@2005")) {
            handleContacts(con);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void handleContacts(Connection con) throws SQLException {
        Scanner sc = new Scanner(System.in);
        while(true)
        {
        System.out.println("Enter the option:\n1. Create contact\n2. Delete contact\n3.show all contacts\n4.edit contact");
        int ch = sc.nextInt();

        if (ch == 1) {
            createContact(con, sc);
        } else if (ch == 2) {
            deleteContact(con, sc);
        } 
        else if(ch==3) {
        	showallcontacts(con,sc);
        }
        else if(ch==4) {
        	editcontact(con,sc);
        }
        else {
            System.out.println("Invalid option");
        }
    }
    }
    private static void editcontact(Connection con, Scanner sc) {
    	String name1=null,name=null;
    	long number=6;
    	sc.nextLine();
    	Statement stmt = null;
		try {
			stmt = con.createStatement();
			ResultSet rs=stmt.executeQuery("select *from contacts");
			int i=0;
		while(rs.next()) {
			i++;
		}
		  if(i<1) {
			  System.out.println("there are no contacts to edit");
		  }
		  else if(i>0) {
			  while(rs.next()) {
				  System.out.println("enter contact name to edit");
			       name1=sc.nextLine();
				  if(rs.getString(1)!=name1) {
					  System.out.println("there is no such contact name to edit");
				  }
				  else if(rs.getString(1)==name1) {
					  System.out.println("enter new contact name to edit");
						 name=sc.nextLine();
						System.out.println("enter new number to edit");
						 number=sc.nextLong();
					  
				  }
			  }
		  }
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
		String query="update contacts set name=?,number=? where name=?";
		PreparedStatement ps=con.prepareStatement(query);
		ps.setString(1, name);
		ps.setLong(2, number);
		ps.setString(3, name1);
		int rowsAffected =ps.executeUpdate();
		if(rowsAffected>0) {
			System.out.println("saved changes successfully");
		}
		else {
			System.out.println("failed to edit");
		}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private static void showallcontacts(Connection con, Scanner sc) {
    	Statement stmt = null;
		try {
			stmt = con.createStatement();
			ResultSet rs=stmt.executeQuery("select *from contacts");
			int i=0;
		while(rs.next()) {
			System.out.println("-------------------------------");
			System.out.println("name: "+rs.getString(1));
			System.out.println("number: "+rs.getLong(2));
			System.out.println("------------------------------");
			i++;
		}
		System.out.println("total "+i+" contact(s) present");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

	private static void createContact(Connection con, Scanner sc) throws SQLException {
        String query = "INSERT INTO contacts(name, number,creationdate,creationtime) VALUES (?, ?, ?, ?)";

        System.out.println("Enter the contact name:");
        sc.nextLine(); // consume the newline character
        String name = sc.nextLine();

        System.out.println("Enter the mobile number:");
        long number = sc.nextLong();

        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, name);
            ps.setLong(2, number);
            LocalDate date=LocalDate.now();
            String s=String2(date);
            ps.setString(3,s);
            LocalTime time=LocalTime.now();
            String t=String1(time);
            ps.setString(4,t);
            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Contact added successfully");
            } else {
                System.out.println("Failed to add contact");
            }
        }
    }

	private static String String1(LocalTime time) {
		return time.toString();
	}

	

	private static String String2(LocalDate date) {
		return date.toString();
	}

	private static void deleteContact(Connection con, Scanner sc) throws SQLException {
        String query = "DELETE FROM contacts WHERE name = ?";

        System.out.println("Enter the contact name to delete:");
        sc.nextLine(); // consume the newline character
        String name = sc.nextLine();

        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, name);
            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Contact deleted successfully");
            } else {
                System.out.println("Failed to delete the contact");
            }
        }
    }
}
