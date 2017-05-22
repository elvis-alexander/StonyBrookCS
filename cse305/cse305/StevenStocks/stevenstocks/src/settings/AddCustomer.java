package settings;

import java.io.IOException;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.sql.ResultSet;

/**
 * Servlet implementation class AddCustomer
 */
@WebServlet("/AddCustomer")
public class AddCustomer extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AddCustomer() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		System.out.println("Adding Customer");

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

			boolean query = true;
			int random_id = 0;
			while (query) {
				random_id = 100000 + (int)(Math.random() * ((900000 - 100000) + 1));
				ResultSet result = stmt
						.executeQuery("SELECT * FROM Client_ WHERE ID ="
								+ random_id + ";");
				if (!result.isBeforeFirst() )
					query = false;
			}
			
			String id = Integer.toString(random_id);
			String firstName = request.getParameter("firstname");
			String lastName = request.getParameter("lastname");
			String address = request.getParameter("address");
			String city = request.getParameter("city");
			String state = request.getParameter("state");
			String zipcode = request.getParameter("zipcode");
			String telephone = request.getParameter("telephone");
			String email = request.getParameter("email");
			String creditCard = request.getParameter("creditcard");
			String password = request.getParameter("password");
			String rating = "0";

			stmt.executeUpdate("INSERT INTO Client_ VALUES (" + String.format("%s,\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",%s,%s,\"%s\",%s,%s,\"%s\");", 
					id, firstName, lastName, address, city, state, zipcode, telephone, email, creditCard, rating, password));
			
			conn.close();
			if(request.getParameter("managertrigger") == null)
				response.sendRedirect("customer.jsp");
			else
				response.sendRedirect("managecustomers.jsp");
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
