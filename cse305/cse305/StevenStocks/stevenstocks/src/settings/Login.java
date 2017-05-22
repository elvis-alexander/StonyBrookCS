package settings;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class Login
 */
@WebServlet("/Login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Login() {
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
			
			String username = request.getParameter("username");
			String password = request.getParameter("password");
			
			if(username.indexOf("@") != -1) {
				java.sql.ResultSet result = stmt.executeQuery("SELECT * FROM client_ C WHERE C.Email = \"" + username + "\" AND C.Pass = \"" + password + "\";");
				if (result.next())
				{
					String id = result.getString("ID");
					String lastName = result.getString("LastName");
					String firstName = result.getString("FirstName");			
					
					HttpSession session = request.getSession();
					session.setAttribute("id", id);
					session.setAttribute("lastname", lastName);
					session.setAttribute("firstname", firstName);
					response.sendRedirect("customerhomepage.jsp");
				} else
				{
					request.setAttribute("login_error", "Incorrect username or password");
					RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
					
					rd.forward(request, response);
				}
			}
			else
			{
				java.sql.ResultSet result = stmt.executeQuery("SELECT * FROM Employee E WHERE E.UserName = \"" + 
						username + "\" AND E.Pass = \"" + password + "\";");
				
				if (result.next())
				{
					String ssn = result.getString("SSN");
					String lastName = result.getString("LastName");
					String firstName = result.getString("FirstName");
					String address = result.getString("Address");
					String city = result.getString("City");
					String state = result.getString("State");
					String zipCode = result.getString("ZipCode");
					String telephone = result.getString("Telephone");
					String startDate = result.getString("StartDate");
					String hourlyRate = result.getString("HourlyRate");
					String role = result.getString("Role");
					String userName = result.getString("UserName");
					
					HttpSession session = request.getSession();
					session.setAttribute("user", result);
					session.setAttribute("user_ssn", ssn);
					session.setAttribute("user_lastName", lastName);
					session.setAttribute("user_firstName", firstName);
					session.setAttribute("user_address", address);
					session.setAttribute("user_city", city);
					session.setAttribute("user_state", state);
					session.setAttribute("user_zipCode", zipCode);
					session.setAttribute("user_telephone", telephone);
					session.setAttribute("user_startDate", startDate);
					session.setAttribute("user_hourlyRate", hourlyRate);
					session.setAttribute("user_role", role);
					session.setAttribute("user_userName", userName);
					
					if (session.getAttribute("user") != null)
					{
						if (session.getAttribute("user_role") != null)
						{
							if(session.getAttribute("user_role").equals("Employee"))
								response.sendRedirect("employeehomepage.jsp");
							else
								response.sendRedirect("managerhomepage.jsp");
						} else
						{
							response.sendRedirect("customerhomepage.jsp");
						}
					} else{
						session.invalidate();
						request.setAttribute("login_error", "Incorrect username or password");
						RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
						
						rd.forward(request, response);
					}
				} else
				{
					request.setAttribute("login_error", "Incorrect username or password");
					RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
					
					rd.forward(request, response);
					return;
				}
				
			}
			
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
	
	public void redirectTo(HttpServletResponse response, String url) throws IOException
	{
		response.sendRedirect(url);
	}

}
