package settings;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class ViewOrderHistory
 */
@WebServlet("/ViewOrderHistory")
public class ViewOrderHistory extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ViewOrderHistory() {
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
			String orderNumber = request.getParameter("ordernumber");
			System.out.println("Getting history of order: " + orderNumber);
			java.sql.ResultSet result = stmt.executeQuery("SELECT OH.* FROM OrderHistory OH WHERE OH.OrderNumber = " + orderNumber + " ORDER BY OH.Datetime ASC;");
			
			StringBuilder labelList = new StringBuilder("labels : [");
			StringBuilder stockPriceList = new StringBuilder("data : [");
			StringBuilder orderPriceList = new StringBuilder("data : [");
			while(result.next()){
				labelList.append("\"" + result.getString("OH.DateTime") + "\"");
				stockPriceList.append("\"" + result.getString("OH.StockSharePrice") + "\"");
				orderPriceList.append("\"" + result.getString("OH.PricePerShare") + "\"");
				
				if(!result.isLast()){
					labelList.append(",");
					stockPriceList.append(",");
					orderPriceList.append(",");
				}
			}
			
			labelList.append("],");
			stockPriceList.append("]");
			orderPriceList.append("]");
			
			StringBuilder finalData = new StringBuilder("");
			finalData.append("var stockData = { " + labelList.toString() + " datasets : [ { label: \"Stop Price\", tension : 0, borderColor : \"rgba(51,51,51,.9)\","
					       + " pointColor : \"rgba(0,175,94,.9)\", " + orderPriceList.toString() + "}, { label: \"Stock Price\", tension : 0, borderColor : \"rgba(0,175,94,.9)\","
					       + " pointColor : \"rgba(51,51,51,.9)\", " + stockPriceList.toString() + "},]} ");
			
			
			stmt.close();
			conn.close();
			 
			System.out.println(finalData.toString());
			request.setAttribute("data", finalData.toString());
			request.setAttribute("ordernumber", orderNumber);
			RequestDispatcher rd = request.getRequestDispatcher("customervieworderhistory.jsp");
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
