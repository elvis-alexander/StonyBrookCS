/*
 * This will be used to support when a customer selects a stock to place an order on
 */
package settings;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class SelectStockOrder
 */
@WebServlet("/SelectStockOrder")
public class SelectStockOrder extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SelectStockOrder() {
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
		// utilities
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
			java.sql.ResultSet result = stmt.executeQuery("SELECT * FROM Stock WHERE StockSymbol =\"" + stockSymbol + "\";");
			
			result.next();
			String type = result.getString("Type");
			String numShares= result.getString("NumShares");
			String sharePrice = result.getString("PricePerShare");
			String companyName = result.getString("CompanyName");			
			
			RequestDispatcher rd = null;
			
			if(request.getParameter("employeebutton") ==  null)
				rd = request.getRequestDispatcher("selectstockorder.jsp");
			else
				rd = request.getRequestDispatcher("selectstockorderemployee.jsp");
			
			request.setAttribute("stocksymbol",stockSymbol);
			request.setAttribute("type",type);
			request.setAttribute("numshares",numShares);
			request.setAttribute("shareprice",sharePrice);
			request.setAttribute("companyname",companyName);
			request.setAttribute("customerfirstname", request.getParameter("firstname"));
			request.setAttribute("customerlastname", request.getParameter("lastname"));
			request.setAttribute("customerid", request.getParameter("id"));
			rd.forward(request, response);
	   	} catch(Exception e) {
	   		e.printStackTrace();
	   	}
	}

}


