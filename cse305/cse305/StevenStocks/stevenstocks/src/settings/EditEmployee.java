package settings;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class EditEmployee
 */
@WebServlet("/EditEmployee")
public class EditEmployee extends HttpServlet {
	private static final long serialVersionUssn = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EditEmployee() {
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
		
			if(request.getParameter("editbutton") != null){
				String firstName = request.getParameter("firstname");
				firstName.trim();
				if(!firstName.equals("") && firstName != null){
					stmt.executeUpdate("UPDATE employee SET FirstName = \"" + firstName + "\" WHERE SSN = " + ssn + ";");
				}
				
				String lastName = request.getParameter("lastname");
				lastName.trim();
				if(!lastName.equals("") && lastName != null){
					stmt.executeUpdate("UPDATE employee SET LastName = \"" + lastName + "\" WHERE ssn = " + ssn + ";");
				}
				
				String address = request.getParameter("address");
				address.trim();
				if(!address.equals("") && address != null){
					stmt.executeUpdate("UPDATE employee SET Address = \"" + address + "\" WHERE ssn = " + ssn + ";");
				}

				
				String city = request.getParameter("city");
				city.trim();
				if(!city.equals("") && city != null){
					stmt.executeUpdate("UPDATE employee SET City = \"" + city + "\" WHERE ssn = " + ssn + ";");
				}
				
				
				String state = request.getParameter("state");
				state.trim();
				if(!state.equals("") && state != null){
					stmt.executeUpdate("UPDATE employee SET State = \"" + state + "\" WHERE ssn = " + ssn + ";");
				}
				
				String zipcode = request.getParameter("zipcode");
				zipcode.trim();
				if(!zipcode.equals("") && zipcode != null){
					stmt.executeUpdate("UPDATE employee SET Zipcode = \"" + zipcode + "\" WHERE ssn = " + ssn + ";");
				}
				
				
				String telephone = request.getParameter("telephone");
				telephone.trim();
				if(!telephone.equals("") && telephone != null){
					stmt.executeUpdate("UPDATE employee SET telephone = \"" + telephone + "\" WHERE ssn = " + ssn + ";");
				}
				
				String hourlyrate = request.getParameter("hourlyrate");
				hourlyrate.trim();
				if(!hourlyrate.equals("") && hourlyrate != null){
					stmt.executeUpdate("UPDATE employee SET hourlyrate = \"" + hourlyrate + "\" WHERE ssn = " + ssn + ";");
				}
				
				
				String startdate = request.getParameter("startdate");
				startdate.trim();
				if(!startdate.equals("") && startdate != null){
					stmt.executeUpdate("UPDATE employee SET startdate = \"" + startdate + "\" WHERE ssn = " + ssn + ";");
				}
				
				
				String username = request.getParameter("username");
				username.trim();
				if(!username.equals("") && username != null){
					stmt.executeUpdate("UPDATE employee SET username = \"" + username + "\" WHERE ssn = " + ssn + ";");
				}
				
				java.sql.ResultSet result = stmt.executeQuery("SELECT * FROM employee C WHERE C.ssn = " + ssn + ";");
				result.next();
				
				conn.close();
				response.sendRedirect("employee.jsp");			
			}else{
				conn.close();
				stmt.executeUpdate("DELETE FROM employee WHERE ssn = " + ssn + ";");
				response.sendRedirect("employee.jsp");
			}
			
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
