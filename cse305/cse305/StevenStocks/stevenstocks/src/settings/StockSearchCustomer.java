package settings;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class StockSearchCustomer
 */
@WebServlet("/StockSearchCustomer")
public class StockSearchCustomer extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public StockSearchCustomer() {
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
	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
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
			
			if(request.getParameter("searchbutton") != null && request.getParameter("searchstring") != null){
				// code to be executed on search
				java.sql.ResultSet result = stmt.executeQuery("SELECT * FROM Stock WHERE StockSymbol LIKE \'%" + request.getParameter("searchstring") + "%\' OR " 
														 + "Type LIKE \'%" + request.getParameter("searchstring") + "%\' OR " 
														 + "CompanyName LIKE \'%" + request.getParameter("searchstring") + "%\'" + ";");
				
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
								+ "</form></div>");
						// form to view stock history
						sb.append("<div class=\"col\"><h1></h1><form action=\"SelectHistoryRange\" method=\"post\">"
								+ "<input class=\"select-customer\" type=\"submit\" value=\"View History\" name=\"viewhistorycustomer\">"
								+ "<input class=\"hidden\" name=\"stocksymbol\" value=\"" + result.getString("StockSymbol") + "\">"
								+ "</form></div>");
						// form to view most recent order
						sb.append("<div class=\"col\"><h1></h1><form action=\"ViewMostRecentOrder\" method=\"post\">"
							+ "<input class=\"select-customer\" type=\"submit\" value=\"Recent Order\" name=\"viewhistorycustomer\">"
							+ "<input class=\"hidden\" name=\"searchstring\" value=\"" + result.getString("StockSymbol") + "\">"
							+ "<input class=\"hidden\" name=\"customerid\" value=\"" + ((String) request.getSession().getAttribute("id")) + "\">"
							+ "</form></div>"
							+ "</div><hr>");
					}	
				}else{
					sb.append("<h1 class=\"no-stocks\"> No results found for \"" + request.getParameter("searchstring") + "\".</h1>");
				}
				request.setAttribute("searchresult", sb.toString());
				RequestDispatcher rd = request.getRequestDispatcher("customeraddorder.jsp");
				rd.forward(request, response);
				
			}else if(request.getParameter("getsuggestions") != null){
				java.sql.ResultSet result = stmt.executeQuery(
						"SELECT * FROM Stock S WHERE S.Type IN ("
					  + "SELECT S.Type FROM Client_ C, Account_ A, Order_ O, Trade T, Stock S "
					  + "WHERE C.ID = " + request.getSession().getAttribute("id") + " AND C.ID = A.ClientID AND O.OrderNumber = T.OrderNumber "
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
								+ "</form></div>");
						// form to view stock history
						sb.append("<div class=\"col\"><h1></h1><form action=\"SelectHistoryRange\" method=\"post\">"
								+ "<input class=\"select-customer\" type=\"submit\" value=\"View History\" name=\"viewhistorycustomer\">"
								+ "<input class=\"hidden\" name=\"stocksymbol\" value=\"" + result.getString("StockSymbol") + "\">"
								+ "</form></div>");
						// form to view most recent order
						sb.append("<div class=\"col\"><h1></h1><form action=\"ViewMostRecentOrder\" method=\"post\">"
							+ "<input class=\"select-customer\" type=\"submit\" value=\"Recent Order\" name=\"viewhistorycustomer\">"
							+ "<input class=\"hidden\" name=\"searchstring\" value=\"" + result.getString("StockSymbol") + "\">"
							+ "<input class=\"hidden\" name=\"customerid\" value=\"" + ((String) request.getSession().getAttribute("id")) + "\">"
							+ "</form></div>"
							+ "</div><hr>");
					}	
				}else{
					sb.append("<h1 class=\"no-stocks\">No suggestions.</h1>");
				}
				request.setAttribute("searchresult", sb.toString());
				RequestDispatcher rd = request.getRequestDispatcher("customeraddorder.jsp");
				rd.forward(request, response);
				
				
				
			}else{
				// clicked from customerhomepage or else where
				java.sql.ResultSet result = stmt.executeQuery("SELECT * FROM Stock;");
				StringBuilder  sb = new StringBuilder("");
				if(result.isBeforeFirst()){
					while(result.next()){
						// Stock Symbol
						sb.append("<div class=\"stock-info-container\"><div class=\"col\"><h1 class=\"stock-symbol\">" + result.getString("StockSymbol") + "</h1></div>");
						// Company Name
						sb.append("<div class=\"col\"><h1> Company: " + result.getString("CompanyName") + "</h1><h1>Industry: " + result.getString("Type") + "</h1></div>");
						// Pricing
						sb.append("<div class=\"col\"><h1> Share Price: " + result.getString("PricePerShare") + "</h1><h1>Shares: " + result.getString("NumShares") + "</h1></div>");
						// form to buy stock
						sb.append("<div class=\"col\"><h1></h1><form action=\"SelectStockOrder\" method=\"post\">"
								+ "<input class=\"select-customer\" type=\"submit\" value=\"Buy Stock\" name=\"selectstock\">"
								+ "<input class=\"hidden\" name=\"stocksymbol\" value=\"" + result.getString("StockSymbol") + "\">"
								+ "</form></div>");
						// form to view stock history
						sb.append("<div class=\"col\"><h1></h1><form action=\"SelectHistoryRange\" method=\"post\">"
								+ "<input class=\"select-customer\" type=\"submit\" value=\"View History\" name=\"viewhistorycustomer\">"
								+ "<input class=\"hidden\" name=\"stocksymbol\" value=\"" + result.getString("StockSymbol") + "\">"
								+ "</form></div>");
						// form to view most recent order
						sb.append("<div class=\"col\"><h1></h1><form action=\"ViewMostRecentOrder\" method=\"post\">"
							+ "<input class=\"select-customer\" type=\"submit\" value=\"Recent Order\" name=\"viewhistorycustomer\">"
							+ "<input class=\"hidden\" name=\"searchstring\" value=\"" + result.getString("StockSymbol") + "\">"
							+ "<input class=\"hidden\" name=\"customerid\" value=\"" + ((String) request.getSession().getAttribute("id")) + "\">"
							+ "</form></div>"
							+ "</div><hr>");
					}
				}else{
					sb.append("<h1 class=\"no-stocks\"> There are currently no stocks.</h1>");
				}
				request.setAttribute("searchresult", sb.toString());
				RequestDispatcher rd = request.getRequestDispatcher("customeraddorder.jsp");
				rd.forward(request, response);
			}
			conn.close();
  		} catch (Exception e) {
  			e.printStackTrace();
  		}
	}

}
