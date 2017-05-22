package settings;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class PlaceOrderEmployee
 */
@WebServlet("/PlaceOrderEmployee")
public class PlaceOrderEmployee extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PlaceOrderEmployee() {
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
	   	java.sql.Statement stmt2 = null;
	   	
	   	try {
  			Class.forName(jdbc_driver).newInstance();
			conn = java.sql.DriverManager.getConnection(url, user, pass);
			
			stmt = conn.createStatement();
			
			if(request.getParameter("searchbutton") != null && request.getParameter("searchstring") != null){
				java.sql.ResultSet result = stmt.executeQuery("SELECT * FROM Stock WHERE StockSymbol LIKE \'%" + request.getParameter("searchstring") + "%\' OR " 
						 + "Type LIKE \'%" + request.getParameter("searchstring") + "%\' OR " 
						 + "CompanyName LIKE \'%" + request.getParameter("searchstring") + "%\'" + ";");
				
				System.out.println("result set calculated");
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
								+ "<input class=\"select-customer\" type=\"submit\" value=\"Buy Stock\" name=\"employeebutton\">"
								+ "<input class=\"hidden\" name=\"stocksymbol\" value=\"" + result.getString("StockSymbol") + "\">"
								+ "<input class=\"hidden\" name=\"id\" value=\"" + request.getParameter("id") + "\">"
								+ "<input class=\"hidden\" name=\"firstname\" value=\"" + request.getParameter("firstname") + "\">"
								+ "<input class=\"hidden\" name=\"lastname\" value=\"" + request.getParameter("lastname") + "\">"
								+ "</form></div>"
								+ "</div><hr>");
						
					}
					
				}else{
					sb.append("<h1 class=\"no-stocks\"> No stocks.</h1>");
				}
				 
				request.setAttribute("searchresult", sb.toString());
				request.setAttribute("customerfirstname", request.getParameter("firstname"));
				request.setAttribute("customerlastname", request.getParameter("lastname"));
				request.setAttribute("customerid", request.getParameter("id"));
				RequestDispatcher rd = null;
				rd = request.getRequestDispatcher("employeestocksearch.jsp");
					
				rd.forward(request, response);
				
			}else if(request.getParameter("selltrigger") != null){
				System.out.println("sell triggggggggggggergggg: " + request.getParameter("id"));
				java.sql.ResultSet result = stmt.executeQuery("SELECT A.AccountNumber FROM Account_ A WHERE A.ClientID = " + request.getParameter("id") + ";");
				
				ArrayList<String> accountNumbers = new ArrayList<String>();
				
				while(result.next()){
					accountNumbers.add(result.getString("A.AccountNumber"));
				}
				System.out.println("num acc: " + accountNumbers.size());
				
				
				StringBuilder  sb = new StringBuilder("");
				for(String accountNumber: accountNumbers){
					java.sql.ResultSet stockHoldings = stmt.executeQuery("SELECT * FROM hasstock WHERE AccountNumber = " + accountNumber + " AND NumShares > 0;");
					
					StringBuilder temp = new StringBuilder("");
					temp.append("<div id=\"stock-container-2\"><div id=\"stock-table-header\"><h1 class=\"holding-header\">Stocks under account: " + accountNumber + "</h1><hr>"
						      + "<div class=\"col\"><h1>Symbol</h1></div><div class=\"col\"><h1>Information</h1></div>"
							  + "<div class=\"col\"><h1>Pricing</h1></div></div>");
					
					if(stockHoldings.isBeforeFirst()){
						while(stockHoldings.next()){
							stmt2 = conn.createStatement();
							String symbol 		  = stockHoldings.getString("Symbol");
							String numberOfShares = stockHoldings.getString("NumShares");
							java.sql.ResultSet stockResult = stmt2.executeQuery("SELECT * FROM Stock WHERE StockSymbol = \"" + symbol +  "\";");
							stockResult.next();
							String companyName = stockResult.getString("CompanyName");
							String industry = stockResult.getString("Type");
							String priceOfStock = stockResult.getString("PricePerShare");
							
							temp.append("<div class=\"stock-info-container-2\"><div class=\"col\"><h1 class=\"stock-symbol\">" + symbol + "</h1></div>");
							temp.append("<div class=\"col\"><h1> Company: " + companyName + "</h1><h1>Industry: " + industry + "</h1></div>");
							temp.append("<div class=\"col\"><h1> Share Price: " + priceOfStock + "</h1><h1>Shares: " + numberOfShares + "</h1></div>");
							temp.append("<div class=\"col\"><h1></h1><form action=\"EmployeeSellStock\" method=\"post\">");
							temp.append("<input class=\"select-customer\" type=\"submit\" value=\"Sell Stock\" name=\"\">");
							temp.append("<input class=\"hidden\" name=\"numberofshares\" value= " + numberOfShares + ">");
							temp.append("<input class=\"hidden\" name=\"stocksymbol\" value= " + symbol + ">");
							temp.append("<input class=\"hidden\" name=\"customerfirstname\" value= " + request.getParameter("firstname") + ">");
							temp.append("<input class=\"hidden\" name=\"customerlastname\" value= " + request.getParameter("lastname") + ">");
							temp.append("<input class=\"hidden\" name=\"customerid\" value= " + request.getParameter("id") + ">");
							temp.append("<input type=\"text\" class=\"accountnumber\" name=\"accountnumber\" value=\"" + accountNumber + "\">");
							temp.append("</form></div>");
							temp.append("</div>");
						}
						
						temp.append("</div>");
						
					}else{
						temp.append("<h1 class=\"no-stocks\"> No stocks.</h1>");
					}
					
					sb.append(temp.toString());
				}
				
				 
				request.setAttribute("searchresult", sb.toString());
				request.setAttribute("customerfirstname", request.getParameter("firstname"));
				request.setAttribute("customerlastname", request.getParameter("lastname"));
				request.setAttribute("customerid", request.getParameter("id"));
				RequestDispatcher rd = null;
				rd = request.getRequestDispatcher("employeeselectwhattosell.jsp");
					
				rd.forward(request, response);
		}else{
			System.out.println("elsesing");
			java.sql.ResultSet result = stmt.executeQuery("SELECT * FROM Stock;");
			
			StringBuilder  sb = new StringBuilder("");
			
			if(result.isBeforeFirst()){
				while(result.next()){
					sb.append("<div class=\"stock-info-container\"><div class=\"col\"><h1 class=\"stock-symbol\">" + result.getString("StockSymbol") + "</h1></div>");
					sb.append("<div class=\"col\"><h1> Company: " + result.getString("CompanyName") + "</h1><h1>Industry: " + result.getString("Type") + "</h1></div>");
					sb.append("<div class=\"col\"><h1> Share Price: " + result.getString("PricePerShare") + "</h1><h1>Shares: " + result.getString("NumShares") + "</h1></div>");
					sb.append("<div class=\"col\"><h1></h1><form action=\"SelectStockOrder\" method=\"post\">"
							+ "<input class=\"select-customer\" type=\"submit\" value=\"Buy Stock\" name=\"employeebutton\">"
							+ "<input class=\"hidden\" name=\"stocksymbol\" value=\"" + result.getString("StockSymbol") + "\">"
							+ "<input class=\"hidden\" name=\"id\" value=\"" + request.getParameter("id") + "\">"
							+ "<input class=\"hidden\" name=\"firstname\" value=\"" + request.getParameter("firstname") + "\">"
							+ "<input class=\"hidden\" name=\"lastname\" value=\"" + request.getParameter("lastname") + "\">"
							+ "</form></div>"
							+ "</div><hr>");
				}
			}else{
				sb.append("<h1 class=\"no-stocks\"> There are currently no stocks.</h1>");
			}
			
			request.setAttribute("searchresult", sb.toString());
			request.setAttribute("customerfirstname", request.getParameter("firstname"));
			request.setAttribute("customerlastname", request.getParameter("lastname"));
			request.setAttribute("customerid", request.getParameter("id"));
			RequestDispatcher rd = null;
			rd = request.getRequestDispatcher("employeestocksearch.jsp");
				
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
