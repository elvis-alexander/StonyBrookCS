package settings;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Random;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class PlaceOrder
 */
@WebServlet("/PlaceOrder")
public class PlaceOrder extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PlaceOrder() {
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
		String jdbc_driver = "com.mysql.jdbc.Driver";
		String url = "jdbc:mysql://mysql2.cs.stonybrook.edu:3306/blanunez";
		String user = "blanunez";
		String pass = "109162285";
		java.sql.Connection conn = null;
		java.sql.Statement stmt = null;
		try {
			Class.forName(jdbc_driver).newInstance();
			conn = java.sql.DriverManager.getConnection(url, user, pass);
			stmt = conn.createStatement();
			
			// Insert into orders table
			boolean query = true;
			int randomOrderNumber = 0;
			while (query) {
				Random rand = new Random();
			    randomOrderNumber = rand.nextInt((100000- 1) + 1) + 1;
				ResultSet result = stmt.executeQuery("SELECT * FROM order_ WHERE OrderNumber =" + randomOrderNumber + ";");
				if (!result.isBeforeFirst() )
					query = false;
			}
			// 
			String totalAmountOfSharesCurrentlyAvailable = request.getParameter("numshares");
			// order information
			String orderNumber = Integer.toString(randomOrderNumber);
			// num shares (to buy or sell)
			String numberOfShares = request.getParameter("amountshares");
			// price per share 
			String pricePerShare = request.getParameter("shareprice");
			// stock symbol
			String stockSymbol = request.getParameter("stocksymbol");
			// current date time
			Calendar cal = Calendar.getInstance(); 
			java.sql.Timestamp timestamp = new Timestamp(cal.getTimeInMillis());
			// sell type
			String orderType = "buy";
			// price type (Market, Market on Close, Hidden Stop, Trailing Stop)
			String priceType = request.getParameter("option");
			// percentage
			String percentage = request.getParameter("percentage");
			// insert into orders table (configure state)
			
			String triggerPrice = request.getParameter("triggerprice");
			
			System.out.println("Trigger: " + triggerPrice);
			
			if(priceType.equalsIgnoreCase("market")) {
				String s = "Complete";
				stmt.executeUpdate("INSERT INTO order_ VALUES (" + String.format("%s,%s,%s,\"%s\",\"%s\",\"%s\",\"%s\",%s,\"%s\");", 
						orderNumber, numberOfShares, pricePerShare, stockSymbol, timestamp, orderType, priceType, percentage, s));
			} else {
				if(priceType.equalsIgnoreCase("marketonclose")){
					stmt.executeUpdate("INSERT INTO order_ VALUES (" + String.format("%s,%s,%s,\"%s\",\"%s\",\"%s\",\"%s\",%s,\"%s\");", 
							orderNumber, numberOfShares, pricePerShare, stockSymbol, timestamp, orderType, priceType, percentage, "Pending"));
				}else{
					stmt.executeUpdate("INSERT INTO order_ VALUES (" + String.format("%s,%s,%s,\"%s\",\"%s\",\"%s\",\"%s\",%s,\"Pending\");", 
							orderNumber, numberOfShares, triggerPrice, stockSymbol, timestamp, orderType, priceType, percentage));
					stmt.executeUpdate("INSERT INTO OrderHistory VALUES (" + String.format("%s,%s, %s,\"%s\" );", orderNumber, pricePerShare ,triggerPrice, timestamp));
				}
			}
			
			
			// INSERT INTO TRANSACTION TABLE			
			boolean notFoundTransactionID = true;
			int randomtransactionID = 0;
			while (notFoundTransactionID) {
				Random rand = new Random();
				randomtransactionID = rand.nextInt((100000 - 1) + 1) + 1;
				ResultSet result = stmt.executeQuery("SELECT * FROM transaction_ WHERE TransactionID = " + randomtransactionID + ";");
				if (!result.isBeforeFirst() )
					notFoundTransactionID = false;
			}
			String transactionID = Integer.toString(randomtransactionID);
			double transactionFee = (Integer.parseInt(numberOfShares)) * (Double.parseDouble(pricePerShare));
			transactionFee += transactionFee *  0.05;
			
			if(priceType.equalsIgnoreCase("market")) {
				StringBuffer s = new StringBuffer("");
				s.append("INSERT INTO transaction_ VALUES (" + String.format("%s, %.2f, %s, \"%s\");",
						transactionID, transactionFee, pricePerShare, timestamp));
				System.out.println(s.toString());
				
				
						
				stmt.executeUpdate("INSERT INTO transaction_ VALUES (" + String.format("%s, %.2f, %s, \"%s\");",
						transactionID, transactionFee, pricePerShare, timestamp));				
			} else {
				stmt.executeUpdate("INSERT INTO transaction_ VALUES (" + String.format("%s, NULL, NULL, NULL);",
						transactionID));
			}
			
			// AccountNumber OrderNumber TransactionID EmployeeID
			String accountNumber = request.getParameter("account_number");
			stmt.executeUpdate("INSERT INTO trade VALUES (" + String.format("%s, %s, %s, NULL);", accountNumber, orderNumber, transactionID));
			
			/* Account Number Symbol NumShares */
			
			if(priceType.equalsIgnoreCase("market")) {
				
				System.out.println("HEY BUDDY YOU ARE IN HASSTOCK UPDATE YOURSELF FOOL");
				ResultSet hasStockResult = stmt.executeQuery("SELECT * FROM hasstock WHERE AccountNumber = " + accountNumber + " AND Symbol = \"" + stockSymbol + "\";");
				if(hasStockResult.isBeforeFirst()) {
					hasStockResult.next();
					String currentNumberOfShares = hasStockResult.getString("NumShares");
					stmt.executeUpdate("UPDATE hasstock SET NumShares = " + currentNumberOfShares + " + " + numberOfShares +" WHERE AccountNumber = " 
					                  + accountNumber + " AND Symbol = \"" + stockSymbol + "\";" );
				} else {
					stmt.executeUpdate("INSERT INTO hasstock VALUES (" + String.format("%s, \"%s\", %s);", accountNumber, stockSymbol, numberOfShares));
				}
//				totalAmountOfSharesCurrentlyAvailable
				stmt.executeUpdate("UPDATE stock SET NumShares = " + totalAmountOfSharesCurrentlyAvailable + " - " + numberOfShares 
									+ " WHERE StockSymbol = \"" + stockSymbol + "\""+ ";");
			
			}
			
			
			
			RequestDispatcher rd = request.getRequestDispatcher("confirmcustomerbuy.jsp");
			
			StringBuilder orderConfirmationMessage = new StringBuilder("");

			if(priceType.equalsIgnoreCase("market")) {
				orderConfirmationMessage.append("<h1>Congratulations, your order is complete.</h1><h1>Order Number: " + orderNumber + "</h1>"
											  + "<h1> Transaction ID: " + transactionID + "</h1><h1>Transaction Fee: " + transactionFee + "</h1>");
			} else {
				orderConfirmationMessage.append("<h1>Congratulations, your order is pending.</h1><h1>Order Number: " + orderNumber + "</h1>"
						+				        "<h1>Transaction ID: " + transactionID + "</h1><h1> You will be charged a 5% fee upon execution of your order.</h1>");				
			}
			 
			conn.close();
			request.setAttribute("orderConfirmationMessage", orderConfirmationMessage.toString());
			rd.forward(request, response);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
/*
	INSERT INTO Order_ VALUES (?, ? , ?, ?, ? ,? ,?);
	INSERT INTO Transaction_ VALUES(? ,? ,? ,?);
	INSERT INTO Trade VALUES(?, ?, ? ,?);
	#-------------->EXAMPLE QUERY
	INSERT INTO Order_ VALUES(1, 75, 34.23, 'GM', '10-3-14 1:30:00', 'Sell', 'Market');
	INSERT INTO Transaction_ VALUES(1, 2567.25, 34.23, '10-3-15 12:00:00');
	INSERT INTO Trade VALUES(101, 1, 1, 789123456);
*/
}
