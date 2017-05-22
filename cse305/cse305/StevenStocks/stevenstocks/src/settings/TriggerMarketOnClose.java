package settings;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Calendar;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class TriggerMarketOnClose
 */
@WebServlet("/TriggerMarketOnClose")
public class TriggerMarketOnClose extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TriggerMarketOnClose() {
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
   		java.sql.Connection conn2 = null;
	   	java.sql.Statement stmt = null;
	   	java.sql.Statement stmt2 = null;
	   	
	   	try {
  			Class.forName(jdbc_driver).newInstance();
			conn = java.sql.DriverManager.getConnection(url, user, pass);
			conn2 = java.sql.DriverManager.getConnection(url, user, pass);
			
			stmt = conn.createStatement();
			stmt2 = conn2.createStatement();
			
			ResultSet result = stmt.executeQuery("SELECT O.OrderNumber, O.StockSymbol, O.NumberShares, O.OrderType, "
											   + "S.PricePerShare, S.NumShares, T1.TransactionID, T1.AccountNumber "
											   + "FROM Order_ O, Stock S, Trade T1 "
											   + "WHERE O.OrderNumber = T1.OrderNumber AND O.StockSymbol = S.StockSymbol AND O.State LIKE 'Pending' "
											   + "AND O.PriceType LIKE 'marketonclose';");
			int ordersExecuted = 0;
				while(result.next()){
					String orderNumber = result.getString("O.OrderNumber");
					String stockSymbol = result.getString("O.StockSymbol");
					int numberShares = result.getInt("O.NumberShares");
					String orderType = result.getString("O.OrderType");
					String pricePerShare = result.getString("S.PricePerShare");
					String totalStocks = result.getString("S.NumShares");
					String transactionID = result.getString("T1.TransactionID");
					String accountNumber = result.getString("T1.AccountNumber");
					
					double fee = Double.parseDouble(pricePerShare) * numberShares;
					Calendar cal = Calendar.getInstance(); 
					java.sql.Timestamp timestamp = new Timestamp(cal.getTimeInMillis());
					
					java.sql.Statement stmt3 = conn2.createStatement();
					
					stmt3.executeUpdate("UPDATE Order_ SET PricePerShare = " + pricePerShare + " WHERE OrderNumber = " + orderNumber + ";");
					stmt3.executeUpdate("UPDATE Order_ SET State = 'Complete' WHERE OrderNumber = " + orderNumber + ";");
					
					stmt3.executeUpdate("UPDATE Transaction_ SET DateTime = \"" + timestamp + "\" WHERE TransactionID = " + transactionID + ";");
					stmt3.executeUpdate("UPDATE Transaction_ SET PricePerShare = " + pricePerShare + "  WHERE TransactionID = " + transactionID + ";");
					stmt3.executeUpdate("UPDATE Transaction_ SET Fee = " + fee + " WHERE TransactionID = " + transactionID + ";");
					
					stmt3.close();
					
					ResultSet hasStock = stmt2.executeQuery("SELECT NumShares FROM HasStock WHERE AccountNumber = " + accountNumber 
                            + " AND Symbol = '" + stockSymbol +"' ;");
					
					if(orderType.equalsIgnoreCase("Buy")){
						if(hasStock.isBeforeFirst()){
							hasStock.next();
							
							int currentAmmountOfShareInTheHasStockTable = hasStock.getInt("numShares");
							int newAmmountOfShares = numberShares + currentAmmountOfShareInTheHasStockTable;
							
							stmt2.executeUpdate("UPDATE HasStock SET NumShares = " + newAmmountOfShares+ " WHERE AccountNumber = " + accountNumber 
									         + " AND Symbol = '" + stockSymbol+ "';");
							
						}else{
							
							stmt2.executeUpdate("INSERT INTO HasStock VALUES (" + accountNumber + ", '" + stockSymbol + "', " + numberShares + ");");
						}
						
						stmt2.executeUpdate("UPDATE Stock SET NumShares = " + totalStocks + " - " + numberShares + " WHERE StockSymbol = '" + stockSymbol + "';");
						
					}else{
						if(hasStock.isBeforeFirst()){
							hasStock.next();
							
							int currentAmmountOfShareInTheHasStockTable = hasStock.getInt("numShares");
							int newAmmountOfShares = currentAmmountOfShareInTheHasStockTable - numberShares;
							
							stmt2.executeUpdate("UPDATE HasStock SET NumShares= " + newAmmountOfShares+ " WHERE AccountNumber = " + accountNumber 
									         + " AND Symbol = '" + stockSymbol+ "';");
							
						}else{
							
							stmt2.executeUpdate("INSERT INTO HasStock VALUES (" + accountNumber + ", '" + stockSymbol + "', " + numberShares + ");");
						}
						
						stmt2.executeUpdate("UPDATE Stock SET NumShares = " + totalStocks + " + " + numberShares + " WHERE StockSymbol = '" + stockSymbol + "';");
					}
					
					ordersExecuted++;
				}
			
			conn.close();
			conn2.close();
			stmt.close();
			stmt2.close();
			RequestDispatcher rd = request.getRequestDispatcher("managersettings.jsp");
			request.setAttribute("result", "Market on close orders executed: " + Integer.toString(ordersExecuted));
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
