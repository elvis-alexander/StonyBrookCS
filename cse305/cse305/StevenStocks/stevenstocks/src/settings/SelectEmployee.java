package settings;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class SelectEmployee
 */
@WebServlet("/SelectEmployee")
public class SelectEmployee extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SelectEmployee() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		response.sendRedirect("index.html");
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
			
			String ssn = request.getParameter("ssn");
			java.sql.ResultSet result = stmt.executeQuery("SELECT * FROM employee E WHERE E.SSN = " + ssn + ";");
			result.next();
			
			String firstName = result.getString("FirstName");
			String lastName = result.getString("LastName");
			String address = result.getString("Address");
			String city = result.getString("City");
			String state = result.getString("State");
			String zipCode = result.getString("Zipcode");
			String telephone = result.getString("Telephone");
			String hourlyRate = result.getString("HourlyRate");
			String startDate = result.getString("StartDate");
			String userName = result.getString("UserName");
			
			// page
			RequestDispatcher rd = request.getRequestDispatcher("editemployee.jsp");
			
			request.setAttribute("ssn", ssn);
			request.setAttribute("firstname",firstName);
			request.setAttribute("lastname",lastName);
			request.setAttribute("address",address);
			request.setAttribute("city",city);
			request.setAttribute("state",state);
			request.setAttribute("zipcode",zipCode);
			request.setAttribute("telephone",telephone);
			request.setAttribute("hourlyrate",hourlyRate);
			request.setAttribute("startdate",startDate);
			request.setAttribute("username",userName);
			
			rd.forward(request, response);
			System.out.println("SENT! SSN: " + ssn);
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
