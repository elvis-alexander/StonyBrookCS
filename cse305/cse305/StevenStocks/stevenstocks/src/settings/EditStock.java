package settings;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class EditStock
 */
@WebServlet("/EditStock")
public class EditStock extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EditStock() {
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
			String stockSymbol = request.getParameter("stocksymbol");
		
			if(request.getParameter("editbutton") != null){
				
				String companyName = request.getParameter("companyname");
				if(!companyName.equals("") && companyName != null){
					stmt.executeUpdate("UPDATE Stock SET CompanyName = \"" + companyName + "\" WHERE StockSymbol = \"" + stockSymbol + "\";");
				}
				
				String stockType = request.getParameter("stocktype");
				if(!stockType.equals("") && stockType != null){
					stmt.executeUpdate("UPDATE Stock SET Type = \"" + stockType + "\" WHERE StockSymbol = \"" + stockSymbol + "\";");
				}

				
				String sharePrice = request.getParameter("shareprice");
				if(!sharePrice.equals("") && sharePrice != null){
					handleStockPriceChange(conn, Double.parseDouble(sharePrice), stockSymbol);
				}
				
				
				String numshares = request.getParameter("numshares");
				if(!numshares.equals("") && numshares != null){
					stmt.executeUpdate("UPDATE Stock SET NumShares = " + numshares + " WHERE StockSymbol = \"" + stockSymbol + "\";");
				}
				
				stmt.close();
				RequestDispatcher rd = request.getRequestDispatcher("/StockSearch");
				rd.forward(request, response);
				
			}else{
				stmt.executeUpdate("DELETE FROM Stock WHERE StockSymbol = \"" + stockSymbol + "\";");
				stmt.close();
				conn.close();
				RequestDispatcher rd = request.getRequestDispatcher("/StockSearch");
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
	
		private void handleStockPriceChange(java.sql.Connection conn, double newPricePerShare, String stockSymbol){
			try {
				java.sql.Statement stmt = conn.createStatement();
				
				java.sql.ResultSet result = stmt.executeQuery("SELECT O.OrderNumber, O.NumberShares, O.Percentage, O.OrderType, S.StockSymbol, "
															+ "O.PriceType, O.PricePerShare ,S.PricePerShare, S.NumShares, T1.TransactionID, T1.AccountNumber "
									                        + "FROM Order_ O, Stock S, Trade T1 "
									                        + "WHERE O.OrderNumber = T1.OrderNumber AND O.StockSymbol = S.StockSymbol "
									                        + "AND O.StockSymbol = '" + stockSymbol + "' AND O.StockSymbol = '" + stockSymbol + "' " 
									                        + "AND O.State = 'Pending' AND (O.PriceType = 'trailingstop' OR O.PriceType = 'hiddenstop');");
				
				while(result.next()){
					System.out.println("result.next(), OrderNumber:  " + result.getString("O.OrderNumber"));
					String orderNumber = result.getString("O.OrderNumber");
					int numberShares = result.getInt("O.NumberShares");
					double percentage = result.getDouble("O.Percentage") / 100;
					String orderType = result.getString("O.OrderType");
					String priceType = result.getString("O.PriceType");
					double currentPricePerShare = result.getDouble("S.PricePerShare");
					String totalStock = result.getString("S.NumShares");
					double orderPricePerShare = result.getDouble("O.PricePerShare");
					String transactionID = result.getString("T1.TransactionID");
					String accountNumber = result.getString("T1.AccountNumber");
					Calendar cal = Calendar.getInstance();
					java.sql.Timestamp timestamp = new Timestamp(cal.getTimeInMillis());
					
					System.out.println("New price: " + newPricePerShare + " Current price: " + currentPricePerShare);
					if(newPricePerShare > currentPricePerShare){ // increase in stock price
						System.out.println("Increase in stock price");
						java.sql.Statement stmt2 = conn.createStatement();
						
						if(priceType.equalsIgnoreCase("trailingstop")){
							System.out.println("Increase in stock price/trailingstop");
							double triggerPricePerShare = newPricePerShare - (newPricePerShare * percentage);
							stmt2.executeUpdate(String.format("UPDATE Order_ SET PricePerShare = %.2f WHERE OrderNumber = %s ;", triggerPricePerShare, orderNumber));
							
							stmt2.executeUpdate(String.format("INSERT INTO OrderHistory VALUES(%s, %.2f, %.2f, \"%s\");", 
									orderNumber, newPricePerShare, triggerPricePerShare, timestamp.toString()));
						}
						else{
							System.out.println("Increase in stock price/hiddenstop");
							stmt2.executeUpdate(String.format("INSERT INTO OrderHistory VALUES(%s, %.2f, %.2f, \"%s\");", 
									orderNumber, newPricePerShare, orderPricePerShare, timestamp.toString()));
						}
						stmt2.close();
					}else{ // decrease in stock price
						System.out.println("Decrease in stock price");
						java.sql.Statement stmt2 = conn.createStatement();
						
							if(newPricePerShare > orderPricePerShare){ // new price still greater than trailing stop price
								System.out.println("Decrease in stock price/new price still greater than trigger price");
								stmt2.executeUpdate(String.format("INSERT INTO OrderHistory VALUES(%s, %.2f, %.2f, \"%s\");", 
										orderNumber, newPricePerShare, orderPricePerShare, timestamp.toString()));
							}else{ // execute order
								System.out.println("Decrease in stock price/execute order");
								double fee = newPricePerShare * numberShares;
								// update Order_ tuple
								stmt2.executeUpdate(String.format("UPDATE Order_ SET PricePerShare = %.2f WHERE OrderNumber = %s ;", newPricePerShare, orderNumber));
								stmt2.executeUpdate(String.format("UPDATE Order_ SET State = 'Complete' WHERE OrderNumber = %s ;", orderNumber));
								
								// update transaction tuple
								stmt2.executeUpdate(String.format("UPDATE Transaction_ SET PricePerShare = %.2f WHERE TransactionID = %s ;", 
										newPricePerShare, transactionID));
								stmt2.executeUpdate(String.format("UPDATE Transaction_ SET Fee = %.2f WHERE TransactionID = %s ;", 
										fee, transactionID));
								stmt2.executeUpdate(String.format("UPDATE Transaction_ SET DateTime = \"%s\" WHERE TransactionID = %s ;", 
										timestamp, transactionID));
								
								stmt2.executeUpdate(String.format("INSERT INTO OrderHistory VALUES(%s, %.2f, %.2f, \"%s\");", 
										orderNumber, newPricePerShare, newPricePerShare, timestamp.toString()));
								
								java.sql.Statement stmt3 = conn.createStatement();
								ResultSet hasStock = stmt3.executeQuery("SELECT NumShares FROM HasStock WHERE AccountNumber = " + accountNumber 
			                            + " AND Symbol = '" + stockSymbol +"' ;");
								
								if(orderType.equalsIgnoreCase("Buy")){
									if(hasStock.isBeforeFirst()){
										hasStock.next();
										
										int currentAmmountOfShareInTheHasStockTable = hasStock.getInt("numShares");
										int newAmmountOfShares = numberShares + currentAmmountOfShareInTheHasStockTable;
										
										stmt3.executeUpdate("UPDATE HasStock SET NumShares= " + newAmmountOfShares+ " WHERE AccountNumber = " + accountNumber 
												         + " AND Symbol = '" + stockSymbol+ "';");
										
									}else{
										
										stmt3.executeUpdate("INSERT INTO HasStock VALUES (" + accountNumber + ", '" + stockSymbol + "', " + numberShares + ");");
									}
									
									System.out.println("Decrease in stock price/execute order/buy/update stock/" + stockSymbol);
									java.sql.Statement stmt4 = conn.createStatement();
									System.out.println("Executing query: " + "UPDATE Stock SET NumShares = " + totalStock + " - " + numberShares + " WHERE StockSymbol = '" + stockSymbol + "';");
									int x = stmt4.executeUpdate("UPDATE Stock SET NumShares = " + totalStock + " - " + numberShares + " WHERE StockSymbol = '" + stockSymbol + "';");
									System.out.println("Affected rows: " + x);
									stmt4.close();
									
								}else{
									if(hasStock.isBeforeFirst()){
										hasStock.next();
										
										int currentAmmountOfShareInTheHasStockTable = hasStock.getInt("numShares");
										int newAmmountOfShares = currentAmmountOfShareInTheHasStockTable - numberShares;
										
										stmt3.executeUpdate("UPDATE HasStock SET NumShares= " + newAmmountOfShares + " WHERE AccountNumber = " + accountNumber 
												         + " AND Symbol = '" + stockSymbol+ "';");
										
									}else{
										
										stmt3.executeUpdate("INSERT INTO HasStock VALUES (" + accountNumber + ", '" + stockSymbol + "', " + numberShares + ");");
									}
									
									System.out.println("Decrease in stock price/execute order/sell/update stock/" + stockSymbol);
									java.sql.Statement stmt4 = conn.createStatement();
									System.out.println("Executing query: " + "UPDATE Stock SET NumShares = " + totalStock + " + " + numberShares + " WHERE StockSymbol = '" + stockSymbol + "';");
									int x = stmt4.executeUpdate("UPDATE Stock SET NumShares = " + totalStock + " + " + numberShares + " WHERE StockSymbol = '" + stockSymbol + "';");
									System.out.println("Affected rows: " + x);
									stmt4.close();
								}
								
								stmt3.close();
							}
						
						stmt2.close();
					}
					
				}
				
				java.sql.Statement stmt4 = conn.createStatement();
				stmt4.executeUpdate(String.format("UPDATE Stock SET PricePerShare = %.2f WHERE StockSymbol = '%s' ;", newPricePerShare, stockSymbol ));
				stmt4.executeUpdate(String.format("INSERT INTO StockHistory VALUES('%s', %s, \"%s\");"
						, stockSymbol, newPricePerShare, new Timestamp(Calendar.getInstance().getTimeInMillis())));
				stmt4.close();
				
				
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
}
