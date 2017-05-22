package settings;

import java.io.IOException;
import java.sql.ResultSet;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class AddStock
 */
@WebServlet("/AddStock")
public class AddStock extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddStock() {
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
			
			String stockSymbol = request.getParameter("stocksymbol");
			String companyName = request.getParameter("companyname");
			String stockType = request.getParameter("stocktype");
			String pricePerShare = request.getParameter("shareprice");
			String numShares = request.getParameter("numshares");

			stmt.executeUpdate("INSERT INTO Stock VALUES (" + String.format("\"%s\",\"%s\",\"%s\",%s,%s);", 
					stockSymbol, companyName, stockType, pricePerShare, numShares));
			
			conn.close();
			RequestDispatcher rd = request.getRequestDispatcher("/StockSearch");
			rd.forward(request, response);
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
