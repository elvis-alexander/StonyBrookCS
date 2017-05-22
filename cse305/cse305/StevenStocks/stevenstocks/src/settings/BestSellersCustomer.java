package settings;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class BestSellersCustomer
 */
@WebServlet("/BestSellersCustomer")
public class BestSellersCustomer extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public BestSellersCustomer() {
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
			
			// code to be executed on search
			java.sql.ResultSet result = stmt.executeQuery(
					"SELECT S.*, SUM(T2.Fee) AS Revenue, SUM(O.NumberShares) AS SharesBought  "
				  + "FROM Order_ O, Stock S, Trade T, Transaction_ T2 "
				  + "WHERE O.OrderType LIKE 'buy' AND O.State LIKE 'complete' "
				  + "AND O.OrderNumber = T.OrderNumber AND O.StockSymbol = S.StockSymbol AND T.TransactionID = T2.TransactionID "
				  + "GROUP BY S.StockSymbol "
				  + "ORDER BY SharesBought DESC "
				  + "LIMIT 10;");
			
			int rank = 1;
			StringBuilder  sb = new StringBuilder("");
			if(result.isBeforeFirst()){
				while(result.next()){
					// Stock Rank
					sb.append("<div class=\"stock-info-container\"><div class=\"col\"><h1 class=\"order-number\">" + rank++ + "</h1></div>");
					// Stock Symbol
					sb.append("<div class=\"col\"><h1 class=\"stock-symbol\">" + result.getString("S.StockSymbol") + "</h1></div>");
					// Company name
					sb.append("<div class=\"col\"><h1> Company: " + result.getString("S.CompanyName") + "</h1><h1>Industry: " + result.getString("S.Type") + "</h1></div>");
					// Pricing 
					sb.append("<div class=\"col\"><h1> Share Price: " + result.getString("S.PricePerShare") + "</h1><h1>Shares: " + result.getString("S.NumShares") + "</h1></div>");
					// Sales
					sb.append("<div class=\"col\"><h1 class=\"order-number\">Shares Sold: " + result.getString("SharesBought") + "</h1></div>");
					// Revenue
					sb.append("<div class=\"col\"><h1 class=\"order-number\">" + result.getString("Revenue") + "</h1></div></div><hr>");
					// form to buy stock
//							sb.append("<div class=\"col\"><h1></h1><form action=\"SelectStockOrder\" method=\"post\">"
//									+ "<input class=\"select-customer\" type=\"submit\" value=\"Buy Stock\" name=\"selectstock\">"
//									+ "<input class=\"hidden\" name=\"stocksymbol\" value=\"" + result.getString("S.StockSymbol") + "\">"
//									+ "</form></div>");
//							// form to view stock history
//							sb.append("<div class=\"col\"><h1></h1><form action=\"SelectHistoryRange\" method=\"post\">"
//									+ "<input class=\"select-customer\" type=\"submit\" value=\"View History\" name=\"viewhistorycustomer\">"
//									+ "<input class=\"hidden\" name=\"stocksymbol\" value=\"" + result.getString("S.StockSymbol") + "\">"
//									+ "</form></div>");
//							// form to view most recent order
//							sb.append("<div class=\"col\"><h1></h1><form action=\"ViewMostRecentOrder\" method=\"post\">"
//								+ "<input class=\"select-customer\" type=\"submit\" value=\"Recent Order\" name=\"viewhistorycustomer\">"
//								+ "<input class=\"hidden\" name=\"searchstring\" value=\"" + result.getString("S.StockSymbol") + "\">"
//								+ "<input class=\"hidden\" name=\"customerid\" value=\"" + ((String) request.getSession().getAttribute("id")) + "\">"
//								+ "</form></div>"
//								+ "</div><hr>");
				}	
			}else{
				sb.append("<h1 class=\"no-stocks\">No results for best sellers.</h1>");
			}
			request.setAttribute("searchresult", sb.toString());
			RequestDispatcher rd = request.getRequestDispatcher("customerbestsellers.jsp");
			rd.forward(request, response);
			
			
			conn.close();
  		} catch (Exception e) {
  			e.printStackTrace();
  		}
	}

}
