package settings;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class UpdateCustomer
 */
@WebServlet("/UpdateCustomer")
public class UpdateCustomer extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UpdateCustomer() {
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
		
			if(request.getParameter("savebutton") != null){
				System.out.println(id + "this is");
				
				String firstName = request.getParameter("firstname");
				firstName.trim();
				if(!firstName.equals("") && firstName != null){
					stmt.executeUpdate("UPDATE Client_ SET FirstName = \"" + firstName + "\" WHERE ID = " + id + ";");
				}
				
				String lastName = request.getParameter("lastname");
				lastName.trim();
				if(!lastName.equals("") && lastName != null){
					stmt.executeUpdate("UPDATE Client_ SET LastName = \"" + lastName + "\" WHERE ID = " + id + ";");
				}
				
				
				String email = request.getParameter("email");
				email.trim();
				if(!lastName.equals("") && lastName != null){
					stmt.executeUpdate("UPDATE Client_ SET Email = \"" + email + "\" WHERE ID = " + id + ";");
				}
				
				String telephone = request.getParameter("telephone");
				telephone.trim();
				if(!telephone.equals("") && telephone != null){
					stmt.executeUpdate("UPDATE Client_ SET Telephone = \"" + telephone + "\" WHERE ID = " + id + ";");
				}
				
				
				String address = request.getParameter("address");
				address.trim();
				if(!address.equals("") && address != null){
					stmt.executeUpdate("UPDATE Client_ SET Address = \"" + address + "\" WHERE ID = " + id + ";");
				}
				
				
				String city = request.getParameter("city");
				city.trim();
				if(!city.equals("") && city != null){
					stmt.executeUpdate("UPDATE Client_ SET City = \"" + city + "\" WHERE ID = " + id + ";");
				}
				
				
				String state = request.getParameter("state");
				state.trim();
				if(!state.equals("") && state != null){
					stmt.executeUpdate("UPDATE Client_ SET State = \"" + state + "\" WHERE ID = " + id + ";");
				}
				
				String zipcode = request.getParameter("zipcode");
				zipcode.trim();
				if(!zipcode.equals("") && zipcode != null){
					stmt.executeUpdate("UPDATE Client_ SET Zipcode = \"" + zipcode + "\" WHERE ID = " + id + ";");
				}
				
				java.sql.ResultSet result = stmt.executeQuery("SELECT * FROM Client_ C WHERE C.ID = " + id + ";");
				result.next();
				
				String firstName1 = result.getString("FirstName");
				String lastName1 = result.getString("LastName");
				String email1 = result.getString("Email");
				String telephone1 = result.getString("Telephone");
				String address1 = result.getString("Address");
				String city1 = result.getString("City");
				String state1 = result.getString("State");
				String zipcode1 = result.getString("Zipcode");
				
				
				
				RequestDispatcher rd = null;
				
				if(request.getParameter("managertrigger") == null)
					rd = request.getRequestDispatcher("customer.jsp");
				else
					rd = request.getRequestDispatcher("managecustomers.jsp");
				
				
				request.setAttribute("id",id);
				request.setAttribute("firstname",firstName1);
				request.setAttribute("lastname",lastName1);
				request.setAttribute("email",email1);
				request.setAttribute("phonenumber",telephone1);
				request.setAttribute("address",address1);
				request.setAttribute("city",city1);
				request.setAttribute("state",state1);
				request.setAttribute("zipcode",zipcode1);
				
				
				
				rd.forward(request, response);
			System.out.println("SENT! :)");
			
			}else{
				stmt.executeUpdate("DELETE FROM Client_ WHERE ID = " + id + ";");
				if(request.getParameter("managertrigger") == null)
					response.sendRedirect("customer.jsp");
				else
					response.sendRedirect("managecustomers.jsp");
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
