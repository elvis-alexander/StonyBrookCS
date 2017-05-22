package settings;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class RevenueSearch
 */
@WebServlet("/RevenueSearch")
public class RevenueSearch extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RevenueSearch() {
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
					
					String option = request.getParameter("option");
					System.out.println("Selected Filter: " + option);
				
					if(request.getParameter("option") == null || option.equalsIgnoreCase("Stock")){
						java.sql.ResultSet result = stmt.executeQuery(
								"SELECT S.StockSymbol, S.CompanyName, S.Type, SUM(O.NumberShares) As SharesSold, SUM(T2.Fee) AS Revenue "
							  + "FROM Stock S, Order_ O, Trade T1, Transaction_ T2 "
							  + "WHERE O.State LIKE 'Complete' AND O.OrderType LIKE 'Sell' AND O.StockSymbol = S.StockSymbol "
							  + "AND O.OrderNumber = T1.OrderNumber AND T2.TransactionID = T1.TransactionID "
							  + "GROUP BY S.StockSymbol "
							  + "ORDER BY Revenue DESC;");
						
						int rank = 1;
						StringBuilder  sb = new StringBuilder("");
						if(result.isBeforeFirst()){
							while(result.next()){
								// Stock Rank
								sb.append("<div class=\"order-info-container\"><div class=\"col\"><h1 class=\"order-number\">" + rank++ + "</h1></div>");
								// Stock Symbol
								sb.append("<div class=\"col\"><h1 class=\"order-number\">" + result.getString("S.StockSymbol") + "</h1></div>");
								// Stock Information
								sb.append("<div class=\"col\"><h1>Copany: " + result.getString("S.CompanyName") + "</h1><h1>Industry: " + result.getString("S.Type") + "</h1></div>");
								// Sales
								sb.append("<div class=\"col\"><h1>Shares Sold: " + result.getString("SharesSold") + "</h1></div>");
								// Revenue
								sb.append("<div class=\"col\"><h1 class=\"order-number\">" + String.format("%.2f",result.getDouble("Revenue")) + "</h1></div></div><hr>");
							}
						}else{
							sb.append("<h1 class=\"no-orders\"> Could not filter revenue by stock.</h1>");
						}
						 
						request.setAttribute("searchresult", sb.toString());
						request.setAttribute("filter", "stock");
						RequestDispatcher rd = request.getRequestDispatcher("managerviewrevenue.jsp");
						rd.forward(request, response);
						
					}
					else if(option.equalsIgnoreCase("Industry")){
						System.out.println("view order button" + request.getParameter("searchstring"));
						java.sql.ResultSet result = stmt.executeQuery(
								"SELECT S.Type, SUM(O.NumberShares) As SharesSold, SUM(T2.Fee) AS Revenue "
							  + "FROM Stock S, Order_ O, Trade T1, Transaction_ T2 "
							  + "WHERE O.State LIKE 'Complete' AND O.OrderType LIKE 'Sell' AND O.StockSymbol = S.StockSymbol "
							  + "AND O.OrderNumber = T1.OrderNumber AND T2.TransactionID = T1.TransactionID "
							  + "GROUP BY S.Type "
							  + "ORDER BY Revenue DESC;");
						
						int rank = 1;
						StringBuilder  sb = new StringBuilder("");
						if(result.isBeforeFirst()){
							while(result.next()){
								// Industry Rank
								sb.append("<div class=\"order-info-container\"><div class=\"col\"><h1 class=\"order-number\">" + rank++ + "</h1></div>");
								// Industry 
								sb.append("<div class=\"col\"><h1>" + result.getString("S.Type") + "</h1></div>");
								// Sales
								sb.append("<div class=\"col\"><h1>Shares Sold: " + result.getString("SharesSold") + "</h1></div>");
								// Revenue
								sb.append("<div class=\"col\"><h1 class=\"order-number\">" + String.format("%.2f",result.getDouble("Revenue")) + "</h1></div></div><hr>");
							}
						}else{
							sb.append("<h1 class=\"no-orders\"> Could not filter revenue by industry.</h1>");
						}

						request.setAttribute("searchresult", sb.toString());
						request.setAttribute("filter", "industry");
						RequestDispatcher rd = request.getRequestDispatcher("managerviewrevenue.jsp");
						rd.forward(request, response);
					}else if(option.equalsIgnoreCase("Customer")){
					
						java.sql.ResultSet result = stmt.executeQuery(
								"SELECT * FROM CustomerRevenue;");
						
						int rank = 1;
						StringBuilder  sb = new StringBuilder("");
						if(result.isBeforeFirst()){
							while(result.next()){
								// Customer Rank
								sb.append("<div class=\"order-info-container\"><div class=\"col\"><h1 class=\"order-number\">" + rank++ + "</h1></div>");
								// Customer Information
								sb.append("<div class=\"col\"><h1> First Name: " + result.getString("FirstName") + "</h1><h1>Last Name: " + result.getString("LastName") 
										+ "</h1>" + "</h1><h1>Email: " + result.getString("Email") + "</h1></div>");
								// Sales
								sb.append("<div class=\"col\"><h1>Shares Sold: " + result.getString("SharesSold") + "</h1></div>");
								// Revenue
								sb.append("<div class=\"col\"><h1 class=\"order-number\">" + String.format("%.2f",result.getDouble("Revenue")) + "</h1></div></div><hr>");
							}
						}else{
							sb.append("<h1 class=\"no-orders\"> Could not filter revenue by representative.</h1>");
						}
						
						conn.close();
						request.setAttribute("searchresult", sb.toString());
						request.setAttribute("filter", "customer");
						request.setAttribute("title", "Customers who hav");
						RequestDispatcher rd = request.getRequestDispatcher("managerviewrevenue.jsp");
						rd.forward(request, response);
					}else{
						java.sql.ResultSet result = stmt.executeQuery(
								"SELECT * FROM RepresentativeRevenue;");
						
						int rank = 1;
						StringBuilder  sb = new StringBuilder("");
						if(result.isBeforeFirst()){
							while(result.next()){
								// Customer Rank
								sb.append("<div class=\"order-info-container\"><div class=\"col\"><h1 class=\"order-number\">" + rank++ + "</h1></div>");
								// Customer Information
								sb.append("<div class=\"col\"><h1> First Name: " + result.getString("FirstName") + "</h1><h1>Last Name: " + result.getString("LastName") 
										+ "</h1>" + "</h1><h1>Start Date: " + result.getString("StartDate") + "</h1></div>");
								// Sales
								sb.append("<div class=\"col\"><h1>Shares Sold: " + result.getString("SharesSold") + "</h1></div>");
								// Revenue
								sb.append("<div class=\"col\"><h1 class=\"order-number\">" + String.format("%.2f",result.getDouble("Revenue")) + "</h1></div></div><hr>");
							}
						}else{
							sb.append("<h1 class=\"no-orders\"> Could not filter revenue by representative.</h1>");
						}
						
						conn.close();
						request.setAttribute("searchresult", sb.toString());
						request.setAttribute("filter", "representative");
						RequestDispatcher rd = request.getRequestDispatcher("managerviewrevenue.jsp");
						rd.forward(request, response);
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
