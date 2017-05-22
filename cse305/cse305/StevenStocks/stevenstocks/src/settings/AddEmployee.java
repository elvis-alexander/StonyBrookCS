package settings;

import java.io.IOException;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class AddEmployee
 */
@WebServlet("/AddEmployee")
public class AddEmployee extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddEmployee() {
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
		// TODO Auto-generated method stub
		String jdbc_driver = "com.mysql.jdbc.Driver";
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
			String lastName = request.getParameter("lastname");
			String firstName = request.getParameter("firstname");
			String address = request.getParameter("address");
			String city = request.getParameter("city");
			String state = request.getParameter("state");
			String zipcode = request.getParameter("zipcode");
			String telephone = request.getParameter("telephone");
			String hourlyrate = request.getParameter("hourlyrate");
			String startdate = request.getParameter("startdate");
			String role = request.getParameter("role");
			String password = request.getParameter("password");
			String username = request.getParameter("username");

			stmt.executeUpdate("INSERT INTO Employee VALUES (" + String.format("%s,\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",%s,%s,\"%s\",%s,\"%s\",\"%s\",\"%s\");", 
					ssn, lastName, firstName, address, city, state, zipcode, telephone, startdate, hourlyrate, role, password, username));
			
			stmt.close();
			conn.close();
			response.sendRedirect("employee.jsp");
		} catch (ClassNotFoundException e) {
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
