package settings;


import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class CustomerSelect
 */
@WebServlet("/SelectCustomer")
public class SelectCustomer extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SelectCustomer() {
        super();
        
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String jdbc_driver= "com.mysql.jdbc.Driver";  
		String url = "jdbc:mysql://mysql2.cs.stonybrook.edu:3306/blanunez";

   		String user = "blanunez";
   		String pass = "109162285";

   		java.sql.Connection conn = null;
	   	java.sql.Statement stmt = null;
	 
  		try {
  			Class.forName(jdbc_driver).newInstance();
			conn = java.sql.DriverManager.getConnection(url, user, pass);
			
			stmt = conn.createStatement();
			
			String id = request.getParameter("id");
			java.sql.ResultSet result = stmt.executeQuery("SELECT * FROM Client_ C WHERE C.ID = " + id + ";");
			result.next();
			
			String firstName = result.getString("FirstName");
			String lastName = result.getString("LastName");
			String email = result.getString("Email");
			String phone = result.getString("Telephone");
			String address = result.getString("Address");
			String city = result.getString("City");
			String state = result.getString("State");
			String zipCode = result.getString("Zipcode");
			
			RequestDispatcher rd = null;
			
			if(request.getParameter("managertrigger") == null)
				rd = request.getRequestDispatcher("employeeoptions.jsp");
			else
				rd = request.getRequestDispatcher("managereditcustomer.jsp");
			
			request.setAttribute("id",id);
			request.setAttribute("firstname",firstName);
			request.setAttribute("lastname",lastName);
			request.setAttribute("email",email);
			request.setAttribute("phonenumber",phone);
			request.setAttribute("address",address);
			request.setAttribute("city",city);
			request.setAttribute("state",state);
			request.setAttribute("zipcode",zipCode);
			
			
			
			rd.forward(request, response);
			System.out.println("SENT! ID: " + id);
			
  		} catch (ClassNotFoundException e){
			e.printStackTrace();
		} catch (java.sql.SQLException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		
	}
	
}
