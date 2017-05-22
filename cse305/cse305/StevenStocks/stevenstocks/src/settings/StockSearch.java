package settings;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class StockSearch
 */
@WebServlet("/StockSearch")
public class StockSearch extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public StockSearch() {
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
				java.sql.ResultSet result = stmt.executeQuery("SELECT * FROM Stock WHERE StockSymbol LIKE \'%" + request.getParameter("searchstring") + "%\' OR " 
														 + "Type LIKE \'%" + request.getParameter("searchstring") + "%\' OR " 
														 + "CompanyName LIKE \'%" + request.getParameter("searchstring") + "%\'" + ";");
				
				StringBuilder  sb = new StringBuilder("");
				
				if(result.isBeforeFirst()){
					while(result.next()){
						sb.append("<div class=\"stock-info-container\"><div class=\"col\"><h1 class=\"stock-symbol\">" + result.getString("StockSymbol") + "</h1></div>");
						sb.append("<div class=\"col\"><h1> Company: " + result.getString("CompanyName") + "</h1><h1>Industry: " + result.getString("Type") + "</h1></div>");
						sb.append("<div class=\"col\"><h1> Share Price: " + result.getString("PricePerShare") + "</h1><h1>Shares: " + result.getString("NumShares") + "</h1></div>");
						sb.append("<div class=\"col\"><h1></h1><form action=\"SelectStock\" method=\"post\">"
								+ "<input class=\"select-customer\" type=\"submit\" value=\"Edit Stock\" name=\"editbutton\">"
								+ "<input class=\"hidden\" name=\"stocksymbol\" value=\"" + result.getString("StockSymbol") + "\">"
								+ "</form></div>"
								+ "<div class=\"col\"><h1></h1><form action=\"SelectHistoryRange\" method=\"post\">"
								+ "<input class=\"select-customer\" type=\"submit\" value=\"View History\" name=\"managertrigger\">"
								+ "<input class=\"hidden\" name=\"stocksymbol\" value=\"" + result.getString("StockSymbol") + "\">"
								+ "</form></div>"
								+ "<div class=\"col\"><h1></h1><form action=\"OrderSearch\" method=\"post\">"
								+ "<input class=\"select-customer\" type=\"submit\" value=\"View Orders\" name=\"vieworderbutton\">"
								+ "<input class=\"hidden\" name=\"searchstring\" value=\"" + result.getString("StockSymbol") + "\">"
								+ "</form></div>"
								+ "</div><hr>");
					}
				}else {
					sb.append("<h1 class=\"no-stocks\"> No results found for \"" + request.getParameter("searchstring") + "\".</h1>");
				}
				 
				request.setAttribute("searchresult", sb.toString());
				RequestDispatcher rd = request.getRequestDispatcher("managestocks.jsp");
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
						sb.append("<div class=\"col\"><h1></h1><form action=\"SelectStock\" method=\"post\">"
								+ "<input class=\"select-customer\" type=\"submit\" value=\"Edit Stock\" name=\"editbutton\">"
								+ "<input class=\"hidden\" name=\"stocksymbol\" value=\"" + result.getString("StockSymbol") + "\">"
								+ "</form></div>"
								+ "<div class=\"col\"><h1></h1><form action=\"SelectHistoryRange\" method=\"post\">"
								+ "<input class=\"select-customer\" type=\"submit\" value=\"View History\" name=\"managertrigger\">"
								+ "<input class=\"hidden\" name=\"stocksymbol\" value=\"" + result.getString("StockSymbol") + "\">"
								+ "</form></div>"
								+ "<div class=\"col\"><h1></h1><form action=\"OrderSearch\" method=\"post\">"
								+ "<input class=\"select-customer\" type=\"submit\" value=\"View Orders\" name=\"vieworderbutton\">"
								+ "<input class=\"hidden\" name=\"searchstring\" value=\"" + result.getString("StockSymbol") + "\">"
								+ "</form></div>"
								+ "</div><hr>");
					}
				}else{
					sb.append("<h1 class=\"no-stocks\"> There are currently no stocks.</h1>");
				}
				
				request.setAttribute("searchresult", sb.toString());
				RequestDispatcher rd = request.getRequestDispatcher("managestocks.jsp");
				rd.forward(request, response);
			}
			conn.close();
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
