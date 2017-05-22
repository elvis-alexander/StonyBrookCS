package settings;

import java.io.IOException;
import java.sql.ResultSet;
import java.util.Calendar;
import java.util.Random;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Servlet implementation class AddAccount
 */
@WebServlet("/AddAccount")
public class AddAccount extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddAccount() {
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
		    int randomAccountId = -1;
		    while (query) {
				Random random = new Random();
				randomAccountId = random.nextInt(((1000000000-1) - 1) + 1) + 1;
				ResultSet result = stmt
						.executeQuery("SELECT * FROM Account_ WHERE AccountNumber ="
								+ randomAccountId + ";");
				if (!result.isBeforeFirst() )
					query = false;
			}
		    String clientId = request.getParameter("id");
			String accountId = Integer.toString(randomAccountId);
			java.sql.Date date = new java.sql.Date(Calendar.getInstance().getTime().getTime());
			stmt.executeUpdate("INSERT INTO Account_ VALUES (" + String.format("%s, %s, \"%s\");", accountId, clientId, date));
			
			conn.close();
			RequestDispatcher rd = request.getRequestDispatcher("addaccount.jsp");
			request.setAttribute("accountid", accountId);
			rd.forward(request, response);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}

}
