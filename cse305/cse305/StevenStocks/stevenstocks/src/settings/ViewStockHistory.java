package settings;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class ViewStockHistory
 */
@WebServlet("/ViewStockHistory")
public class ViewStockHistory extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ViewStockHistory() {
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
	   	
	   	String stockSymbol = request.getParameter("stocksymbol");
	   	String startDate = request.getParameter("startdate");
	   	String endDate = request.getParameter("enddate");
	   	
	   	try {
  			Class.forName(jdbc_driver).newInstance();
			conn = java.sql.DriverManager.getConnection(url, user, pass);
			
			stmt = conn.createStatement();
			System.out.println("Getting history of stock: " + stockSymbol);
			System.out.println("StartDate: " + startDate + " EndDate: " + endDate);
			java.sql.ResultSet result = stmt.executeQuery("SELECT SH.* "
													    + "FROM StockHistory SH "
														+ "WHERE SH.StockSymbol = '" + stockSymbol + "' "
														+ "AND SH.DateTime >= \"" + startDate + "\" AND SH.DateTime <= \"" + endDate + "\" "
													    + "ORDER BY SH.DateTime ASC;");
			
			StringBuilder labelList = new StringBuilder("labels : [");
			StringBuilder stockPriceList = new StringBuilder("data : [");
			while(result.next()){
				labelList.append("\"" + result.getString("SH.DateTime") + "\"");
				stockPriceList.append("\"" + result.getString("SH.PricePerShare") + "\"");
				
				if(!result.isLast()){
					labelList.append(",");
					stockPriceList.append(",");
				}
			}
			
			labelList.append("],");
			stockPriceList.append("]");
			
			StringBuilder finalData = new StringBuilder("");
			finalData.append("var stockData = { " + labelList.toString() + " datasets : [{ label: \"Stock Price\", tension : 0,borderColor : \"rgba(0,175,94,.9)\","
					       + " pointColor : \"rgba(51,51,51,.9)\", " + stockPriceList.toString() + "}]} ");
			
			stmt.close();
			conn.close();
			 
			System.out.println(finalData.toString());
			System.out.println(stockSymbol); 
			request.setAttribute("data", finalData.toString());
			request.setAttribute("stocksymbol", stockSymbol);
			
			RequestDispatcher rd = null;
			
			if(request.getParameter("managertrigger") == null)
				rd = request.getRequestDispatcher("customerviewstockhistory.jsp");
			else
				rd = request.getRequestDispatcher("managerviewstockhistory.jsp");
			
			rd.forward(request, response);
			
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
