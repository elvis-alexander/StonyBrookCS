package settings;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class OrderSearchCustomer
 */
@WebServlet("/OrderSearchCustomer")
public class OrderSearchCustomer extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public OrderSearchCustomer() {
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
				System.out.println("search button " + request.getParameter("customerID"));
				java.sql.ResultSet result = stmt.executeQuery(
					  "SELECT O.*, T1.DateTime "
					+ "FROM Order_ O, Client_ C, Trade T, Transaction_ T1, Account_ A "
					+ "WHERE C.ID = " + request.getParameter("customerID") + " AND C.ID = A.ClientID AND O.OrderNumber = T.OrderNumber "
					+ "AND A.AccountNumber = T.AccountNumber AND T.TransactionID = T1.TransactionID "
					+ "AND O.StockSymbol LIKE '%" + request.getParameter("searchstring") + "%'"
					+ "ORDER BY O.DateTime DESC;");
				System.out.println("result set calculated");
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
						+ "</div><div class=\"col\"><h1>" + result.getString("O.State") + "</h1></div>"
						+ (result.getString("O.PriceType").equalsIgnoreCase("trailingstop") || result.getString("O.PriceType").equalsIgnoreCase("hiddenstop") ? 
						"<div class=\"col\"><form action=\"ViewOrderHistory\" method=\"post\">"
						+ "<input class=\"select-customer\" type=\"submit\" value=\"View History\" name=\"vieworderbutton\">"
						+ "<input class=\"hidden\" type=\"text\" value=\"" + result.getString("O.OrderNumber") + "\" name=\"ordernumber\">"
						+ "</form></div></div><hr>"	: "</div><hr>" ));
						
					}
				}else{
					sb.append("<h1 class=\"no-orders\"> You have not placed orders for stock \"" + request.getParameter("searchstring") + "\".</h1>");
				}
				 
				request.setAttribute("searchresult", sb.toString());
				RequestDispatcher rd = request.getRequestDispatcher("customervieworders.jsp");
				rd.forward(request, response);
				
			}else{
				System.out.println("Direct link to OrderSearchCustomer");
				java.sql.ResultSet result = stmt.executeQuery(
						  "SELECT O.*, T1.DateTime "
						+ "FROM Order_ O, Client_ C, Trade T, Transaction_ T1, Account_ A "
						+ "WHERE C.ID = " + ((String) request.getSession().getAttribute("id")) + " AND C.ID = A.ClientID AND O.OrderNumber = T.OrderNumber "
						+ "AND A.AccountNumber = T.AccountNumber AND T.TransactionID = T1.TransactionID "
						+ (request.getParameter("searchstring") != null ? "AND O.StockSymbol LIKE '%" + request.getParameter("searchstring") + "%'":"AND O.StockSymbol LIKE '%%'")
						+ "ORDER BY O.DateTime DESC;");
				System.out.println("result set calculated");
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
						+ "</div><div class=\"col\"><h1>" + result.getString("O.State") + "</h1></div>"
						+ (result.getString("O.PriceType").equalsIgnoreCase("trailingstop") || result.getString("O.PriceType").equalsIgnoreCase("hiddenstop") ? 
						"<div class=\"col\"><form action=\"ViewOrderHistory\" method=\"post\">"
						+ "<input class=\"select-customer\" type=\"submit\" value=\"View History\" name=\"vieworderbutton\">"
						+ "<input class=\"hidden\" type=\"text\" value=\"" + result.getString("O.OrderNumber") + "\" name=\"ordernumber\">"
						+ "</form></div></div><hr>"	: "</div><hr>" ));
						
					}
				}else{
					sb.append("<h1 class=\"no-orders\">You have not placed any orders.</h1>");
				}
				 
				conn.close();
				request.setAttribute("searchresult", sb.toString());
				RequestDispatcher rd = request.getRequestDispatcher("customervieworders.jsp");
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
