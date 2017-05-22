package settings;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class CustomerViewStockByAccount
 */
@WebServlet("/CustomerViewStockByAccount")
public class CustomerViewStockByAccount extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CustomerViewStockByAccount() {
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
			java.sql.Connection conn2 = java.sql.DriverManager.getConnection(url, user, pass);
			stmt = conn.createStatement();
			java.sql.Statement stmt2 = conn2.createStatement();
	 		// CURRENT ACCOUNT
			String accountNumber = request.getParameter("accountnumber");
			java.sql.ResultSet result = stmt.executeQuery("SELECT * FROM hasstock WHERE AccountNumber = " + accountNumber + " AND NumShares > 0;");
			StringBuilder sb = new StringBuilder("");
			
			if(result.isBeforeFirst()){
				while(result.next()){
					String symbol 		  = result.getString("Symbol");
					String numberOfShares = result.getString("NumShares");
					java.sql.ResultSet stockResult = stmt2.executeQuery("SELECT * FROM Stock WHERE StockSymbol = \"" + symbol +  "\";");
					stockResult.next();
					String companyName = stockResult.getString("CompanyName");
					String industry = stockResult.getString("Type");
					String priceOfStock = stockResult.getString("PricePerShare");
					
					sb.append("<div class=\"stock-info-container\"><div class=\"col\"><h1 class=\"stock-symbol\">" + symbol + "</h1></div>");
					sb.append("<div class=\"col\"><h1> Company: " + companyName + "</h1><h1>Industry: " + industry + "</h1></div>");
					sb.append("<div class=\"col\"><h1> Share Price: " + priceOfStock + "</h1><h1>Shares: " + numberOfShares + "</h1></div>");
					sb.append("<div class=\"col\"><h1></h1><form action=\"CustomerSellStock\" method=\"post\">");
					sb.append("<input class=\"select-customer\" type=\"submit\" value=\"Sell Stock\" name=\"\">");
					sb.append("<input class=\"hidden\" name=numberofshares value= " + numberOfShares + ">");
					sb.append("<input class=\"hidden\" name=stocksymbol value= " + symbol + ">");
					sb.append("<input type=\"text\" class=\"accountnumber\" name=\"accountnumber\" value=\"" + accountNumber + "\">");
					sb.append("</form></div>");
					sb.append("</div>");
				}
			}else{
				sb.append("<h1 class=\"no-orders\">You have stock holdings in this account.</h1>");
			}
			RequestDispatcher rd = request.getRequestDispatcher("customerviewstockbyaccount.jsp");
			request.setAttribute("customerstocksell", sb.toString());
			request.setAttribute("accountnumber", accountNumber);
			System.out.println("------------------------------------------");
			System.out.println(accountNumber);			
			System.out.println("------------------------------------------");
			conn.close();
			conn2.close();
			rd.forward(request, response);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

}
