package settings;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class StockSuggestion
 */
@WebServlet("/StockSuggestion")
public class StockSuggestion extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public StockSuggestion() {
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
	   	// querying
	   	try {
	   		
	   		Class.forName(jdbc_driver).newInstance();
			conn = java.sql.DriverManager.getConnection(url, user, pass);
			
			stmt = conn.createStatement();
			System.out.println("Stock Suggestion: " +  request.getParameter("id"));
			java.sql.ResultSet result = stmt.executeQuery(
					"SELECT * FROM Stock S WHERE S.Type IN ("
				  + "SELECT S.Type FROM Client_ C, Account_ A, Order_ O, Trade T, Stock S "
				  + "WHERE C.ID = " + request.getParameter("id") + " AND C.ID = A.ClientID AND O.OrderNumber = T.OrderNumber "
				  + "AND A.AccountNumber = T.AccountNumber AND O.StockSymbol = S.StockSymbol );");
			
			StringBuilder  sb = new StringBuilder("");
			if(result.isBeforeFirst()){
				while(result.next()){
					// Stock Symbol
					sb.append("<div class=\"stock-info-container\"><div class=\"col\"><h1 class=\"stock-symbol\">" + result.getString("StockSymbol") + "</h1></div>");
					// Company name
					sb.append("<div class=\"col\"><h1> Company: " + result.getString("CompanyName") + "</h1><h1>Industry: " + result.getString("Type") + "</h1></div>");
					// Pricing 
					sb.append("<div class=\"col\"><h1> Share Price: " + result.getString("PricePerShare") + "</h1><h1>Shares: " + result.getString("NumShares") + "</h1></div>");
					// form to buy stock
					sb.append("<div class=\"col\"><h1></h1><form action=\"SelectStockOrder\" method=\"post\">"
							+ "<input class=\"select-customer\" type=\"submit\" value=\"Buy Stock\" name=\"selectstock\">"
							+ "<input class=\"hidden\" name=\"stocksymbol\" value=\"" + result.getString("StockSymbol") + "\">"
							+ "</form></div></div><hr>");
				}	
			}else{
				sb.append("<h1 class=\"no-stocks\">No suggestions.</h1>");
			}
			request.setAttribute("searchresult", sb.toString());
			request.setAttribute("firstname", request.getParameter("firstname"));
			request.setAttribute("lastname", request.getParameter("lastname"));
			RequestDispatcher rd = request.getRequestDispatcher("employeegetsuggestions.jsp");
			rd.forward(request, response);
		
		
	   	}catch (Exception e) {
			e.printStackTrace();
		}
	}
}	
