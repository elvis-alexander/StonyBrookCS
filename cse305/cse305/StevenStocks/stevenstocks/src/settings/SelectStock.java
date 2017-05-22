package settings;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class SelectStock
 */
@WebServlet("/SelectStock")
public class SelectStock extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SelectStock() {
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
			
			String stockSymbol = request.getParameter("stocksymbol");
			java.sql.ResultSet result = stmt.executeQuery("SELECT * FROM Stock S WHERE S.StockSymbol = \"" + stockSymbol+ "\";");
			System.out.println(stockSymbol);
			result.next();
			
			String companyName = result.getString("CompanyName");
			String stockType = result.getString("Type");
			String sharePrice = result.getString("PricePerShare");
			String numShares = result.getString("NumShares");
			
			// page
			RequestDispatcher rd = request.getRequestDispatcher("editstock.jsp");
			
			request.setAttribute("stocksymbol", stockSymbol);
			request.setAttribute("companyname",companyName);
			request.setAttribute("stocktype",stockType);
			request.setAttribute("shareprice",sharePrice);
			request.setAttribute("numshares",numShares);
			
			conn.close();
			rd.forward(request, response);
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
