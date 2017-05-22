package settings;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class OrderSearch
 */
@WebServlet("/OrderSearch")
public class OrderSearch extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public OrderSearch() {
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
		
			if(request.getParameter("searchbutton") != null && request.getParameter("searchstring") != null){
				System.out.println("search button " + request.getParameter("searchstring"));
				java.sql.ResultSet result = stmt.executeQuery(
				"SELECT O.OrderNumber, O.StockSymbol, O.PricePerShare, O.NumberShares, O.OrderType, O.PriceType, O.Percentage, O.DateTime, O.State, T1.DateTime "
				+ "FROM Order_ O, Transaction_ T1, Trade T2 WHERE T1.TransactionID = T2.TransactionID AND O.OrderNumber = T2.OrderNumber AND O.StockSymbol LIKE \'%" 
				+ request.getParameter("searchstring") + "%\'"
				+ " ORDER BY O.DateTime DESC;");
				
				StringBuilder  sb = new StringBuilder("");
				if(result.isBeforeFirst()){
					while(result.next()){
						
						sb.append("<div class=\"order-info-container\"><div class=\"col\"><h1 class=\"order-number\">" + result.getString("O.OrderNumber") + "</h1></div>");
						sb.append("<div class=\"col\"><h1>Stock Symbol: " + result.getString("O.StockSymbol") + "</h1><h1>Share Price: " 
						+ result.getString("O.PricePerShare") + "</h1><h1>Shares: " + result.getString("O.NumberShares") +"</h1></div>"
						+ "<div class=\"col\"><h1>Order Type: " + result.getString("O.OrderType") + "</h1><h1>Price Type: " + result.getString("O.PriceType") 
						+ "</h1>" + (result.getString("O.Percentage") != null ? "<h1>Percentage: " + result.getString("O.Percentage") + "%</h1>" :"" ) + "</div>"
						+ "<div class=\"col\"><h1>Order Date: " + result.getString("O.DateTime") + "</h1>"
						+ "<h1>Fullfilled: " + (result.getString("T1.DateTime") != null ? result.getString("T1.DateTime") + "</h1>" :"N/A</h1>" )
						+ "</div><div class=\"col\"><h1>" + result.getString("O.State") + "</h1></div></div><hr>");
						
					}
				}else{
					sb.append("<h1 class=\"no-orders\"> No orders found for stock \"" + request.getParameter("searchstring") + "\".</h1>");
				}
				 
				request.setAttribute("searchresult", sb.toString());
				RequestDispatcher rd = request.getRequestDispatcher("manageorders.jsp");
				rd.forward(request, response);
				
			}
			else if(request.getParameter("vieworderbutton") != null){
				System.out.println("view order button" + request.getParameter("searchstring"));
				java.sql.ResultSet result = stmt.executeQuery(
						"SELECT O.OrderNumber, O.StockSymbol, O.PricePerShare, O.NumberShares, O.OrderType, O.PriceType, O.Percentage, O.DateTime, O.State, T1.DateTime "
						+ "FROM Order_ O, Transaction_ T1, Trade T2 WHERE T1.TransactionID = T2.TransactionID AND O.OrderNumber = T2.OrderNumber AND O.StockSymbol LIKE \'%" 
						+ request.getParameter("searchstring") + "%\'"
						+ " ORDER BY O.DateTime DESC;");
						
				StringBuilder  sb = new StringBuilder("");
				if(result.isBeforeFirst()){
					while(result.next()){
						
						sb.append("<div class=\"order-info-container\"><div class=\"col\"><h1 class=\"order-number\">" + result.getString("O.OrderNumber") + "</h1></div>");
						sb.append("<div class=\"col\"><h1>Stock Symbol: " + result.getString("O.StockSymbol") + "</h1><h1>Share Price: " 
						+ result.getString("O.PricePerShare") + "</h1><h1>Shares: " + result.getString("O.NumberShares") +"</h1></div>"
						+ "<div class=\"col\"><h1>Order Type: " + result.getString("O.OrderType") + "</h1><h1>Price Type: " + result.getString("O.PriceType") 
						+ "</h1>" + (result.getString("O.Percentage") != null ? "<h1>Percentage: " + result.getString("O.Percentage") + "%</h1>" :"" ) + "</div>"
						+ "<div class=\"col\"><h1>Order Date: " + result.getString("O.DateTime") + "</h1>"
						+ "<h1>Fullfilled: " + (result.getString("T1.DateTime") != null ? result.getString("T1.DateTime") + "</h1>" :"N/A</h1>" )
						+ "</div><div class=\"col\"><h1>" + result.getString("O.State") + "</h1></div></div><hr>");
						
					}
				}else{
					sb.append("<h1 class=\"no-orders\"> No orders found for stock \"" + request.getParameter("searchstring") + "\".</h1>");
				}

				request.setAttribute("searchresult", sb.toString());
				RequestDispatcher rd = request.getRequestDispatcher("manageorders.jsp");
				rd.forward(request, response);
			}else if(request.getParameter("managertrigger") != null){
				
				java.sql.ResultSet nameInfo = stmt.executeQuery("SELECT FirstName, LastName FROM CLient_ WHERE ID = " + request.getParameter("id") + ";");
				nameInfo.next();
				
				String customerFistName = nameInfo.getString("FirstName");
				String customerLastName = nameInfo.getString("LastName");
				
				nameInfo.close();
				
				java.sql.ResultSet result = stmt.executeQuery(
						  "SELECT O.*, T1.DateTime "
						+ "FROM Order_ O, Client_ C, Trade T, Transaction_ T1, Account_ A "
						+ "WHERE C.ID = " + request.getParameter("id") + " AND C.ID = A.ClientID AND O.OrderNumber = T.OrderNumber "
						+ "AND A.AccountNumber = T.AccountNumber AND T.TransactionID = T1.TransactionID "
						+ "ORDER BY O.DateTime DESC;");
				
				StringBuilder  sb = new StringBuilder("");
				
				if(result.isBeforeFirst()){
					while(result.next()){
						
						sb.append("<div class=\"order-info-container\"><div class=\"col\"><h1 class=\"order-number\">" + result.getString("O.OrderNumber") + "</h1></div>");
						sb.append("<div class=\"col\"><h1>Stock Symbol: " + result.getString("O.StockSymbol") + "</h1><h1>Share Price: " 
						+ result.getString("O.PricePerShare") + "</h1><h1>Shares: " + result.getString("O.NumberShares") +"</h1></div>"
						+ "<div class=\"col\"><h1>Order Type: " + result.getString("O.OrderType") + "</h1><h1>Price Type: " + result.getString("O.PriceType") 
						+ "</h1>" + (result.getString("O.Percentage") != null ? "<h1>Percentage: " + result.getString("O.Percentage") + "%</h1>" :"" ) + "</div>"
						+ "<div class=\"col\"><h1>Order Date: " + result.getString("O.DateTime") + "</h1>"
						+ "<h1>Fullfilled: " + (result.getString("T1.DateTime") != null ? result.getString("T1.DateTime") + "</h1>" :"N/A</h1>" )
						+ "</div><div class=\"col\"><h1>" + result.getString("O.State") + "</h1></div></div><hr>");
						
					}
				}else{
					sb.append("<h1 class=\"no-orders\">This customer has not placed any orders.</h1>");
				}
				
				conn.close();
				request.setAttribute("searchresult", sb.toString());
				request.setAttribute("customerfirstname", customerFistName);
				request.setAttribute("customerlastname", customerLastName);
				
				RequestDispatcher rd = request.getRequestDispatcher("managervieworderbycustomer.jsp");
				rd.forward(request, response);
				
			}else if(request.getParameter("employeetrigger") != null){
				java.sql.ResultSet nameInfo = stmt.executeQuery("SELECT FirstName, LastName FROM CLient_ WHERE ID = " + request.getParameter("id") + ";");
				nameInfo.next();
				
				String customerFistName = nameInfo.getString("FirstName");
				String customerLastName = nameInfo.getString("LastName");
				
				nameInfo.close();
				
				java.sql.ResultSet result = stmt.executeQuery(
						  "SELECT O.*, T1.DateTime "
						+ "FROM Order_ O, Client_ C, Trade T, Transaction_ T1, Account_ A, Employee E "
						+ "WHERE C.ID = " + request.getParameter("id") + " AND C.ID = A.ClientID AND O.OrderNumber = T.OrderNumber "
						+ "AND A.AccountNumber = T.AccountNumber AND T.TransactionID = T1.TransactionID "
						+ "AND T.EmployeeID = E.SSN AND E.SSN = " + request.getSession().getAttribute("user_ssn") + " "
						+ "ORDER BY O.DateTime DESC;");
				
				StringBuilder  sb = new StringBuilder("");
				
				if(result.isBeforeFirst()){
					while(result.next()){
						
						sb.append("<div class=\"order-info-container\"><div class=\"col\"><h1 class=\"order-number\">" + result.getString("O.OrderNumber") + "</h1></div>");
						sb.append("<div class=\"col\"><h1>Stock Symbol: " + result.getString("O.StockSymbol") + "</h1><h1>Share Price: " 
						+ result.getString("O.PricePerShare") + "</h1><h1>Shares: " + result.getString("O.NumberShares") +"</h1></div>"
						+ "<div class=\"col\"><h1>Order Type: " + result.getString("O.OrderType") + "</h1><h1>Price Type: " + result.getString("O.PriceType") 
						+ "</h1>" + (result.getString("O.Percentage") != null ? "<h1>Percentage: " + result.getString("O.Percentage") + "%</h1>" :"" ) + "</div>"
						+ "<div class=\"col\"><h1>Order Date: " + result.getString("O.DateTime") + "</h1>"
						+ "<h1>Fullfilled: " + (result.getString("T1.DateTime") != null ? result.getString("T1.DateTime") + "</h1>" :"N/A</h1>" )
						+ "</div><div class=\"col\"><h1>" + result.getString("O.State") + "</h1></div></div><hr>");
						
					}
				}else{
					sb.append("<h1 class=\"no-orders\">You have no receipts for this customer.</h1>");
				}
				
				conn.close();
				request.setAttribute("searchresult", sb.toString());
				request.setAttribute("customerfirstname", customerFistName);
				request.setAttribute("customerlastname", customerLastName);
				
				RequestDispatcher rd = request.getRequestDispatcher("employeeviewordersbycustomer.jsp");
				rd.forward(request, response);
	   		}else{
				java.sql.ResultSet result = stmt.executeQuery(
						"SELECT O.OrderNumber, O.StockSymbol, O.PricePerShare, O.NumberShares, O.OrderType, O.PriceType, O.Percentage, O.DateTime, O.State, T1.DateTime "
						+ "FROM Order_ O, Transaction_ T1, Trade T2 WHERE T1.TransactionID = T2.TransactionID AND O.OrderNumber = T2.OrderNumber"
						+ " ORDER BY O.DateTime DESC;");
				
				StringBuilder  sb = new StringBuilder("");
				
				if(result.isBeforeFirst()){
					while(result.next()){
						
						sb.append("<div class=\"order-info-container\"><div class=\"col\"><h1 class=\"order-number\">" + result.getString("O.OrderNumber") + "</h1></div>");
						sb.append("<div class=\"col\"><h1>Stock Symbol: " + result.getString("O.StockSymbol") + "</h1><h1>Share Price: " 
						+ result.getString("O.PricePerShare") + "</h1><h1>Shares: " + result.getString("O.NumberShares") +"</h1></div>"
						+ "<div class=\"col\"><h1>Order Type: " + result.getString("O.OrderType") + "</h1><h1>Price Type: " + result.getString("O.PriceType") 
						+ "</h1>" + (result.getString("O.Percentage") != null ? "<h1>Percentage: " + result.getString("O.Percentage") + "%</h1>" :"" ) + "</div>"
						+ "<div class=\"col\"><h1>Order Date: " + result.getString("O.DateTime") + "</h1>"
						+ "<h1>Fullfilled: " + (result.getString("T1.DateTime") != null ? result.getString("T1.DateTime") + "</h1>" :"N/A</h1>" )
						+ "</div><div class=\"col\"><h1>" + result.getString("O.State") + "</h1></div></div><hr>");
						
					}
				}else{
					sb.append("<h1 class=\"no-orders\"> There are currently no orders</h1>");
				}
				
				conn.close();
				request.setAttribute("searchresult", sb.toString());
				RequestDispatcher rd = request.getRequestDispatcher("manageorders.jsp");
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
