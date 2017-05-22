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

/**
 * Servlet implementation class EmployeeSellStock
 */
@WebServlet("/EmployeeSellStockFinal")
public class EmployeeSellStockFinal extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EmployeeSellStockFinal() {
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
			// INSERT INTO ORDERS TABLE
			boolean query = true;
			int randomOrderNumber = 0;
			while (query) {
				Random rand = new Random();
			    randomOrderNumber = rand.nextInt((100000- 1) + 1) + 1;
				ResultSet result = stmt.executeQuery("SELECT * FROM order_ WHERE OrderNumber =" + randomOrderNumber + ";");
				if (!result.isBeforeFirst() )
					query = false;
			}
			// percentage
			String percentage = request.getParameter("percentage");
			// order information
			String orderNumber = Integer.toString(randomOrderNumber);
			// num shares (to buy or sell)
			String numberOfShares = request.getParameter("sharestosell");
			// price per share 
			String pricePerShare = request.getParameter("shareprice");
			// stock symbol
			String stockSymbol = request.getParameter("stocksymbol");
			// current date time
			Calendar cal = Calendar.getInstance(); 
			java.sql.Timestamp timestamp = new Timestamp(cal.getTimeInMillis());
			// sell type
			String orderType = "sell";
			// price type
			String priceType = request.getParameter("option");
			String triggerPrice = request.getParameter("triggerprice");
			
			System.out.println("****************************************************************");
			System.out.println(orderNumber);
			System.out.println(numberOfShares);
			System.out.println(pricePerShare);
			System.out.println(stockSymbol);
			System.out.println(timestamp);
			System.out.println(orderType);
			System.out.println(priceType);
			System.out.println("****************************************************************");
			
			
			// insert into orders table
			if(priceType.equalsIgnoreCase("market")) {
				String state = "Complete";
				stmt.executeUpdate("INSERT INTO order_ VALUES (" + String.format("%s, %s, %s, \"%s\", \"%s\", \"%s\", \"%s\", NULL, \"%s\");", 
						orderNumber, numberOfShares, pricePerShare, stockSymbol, timestamp, orderType, priceType, state));
			} else {
				String state = "Pending";
				if(priceType.equalsIgnoreCase("marketonclose")) {
					stmt.executeUpdate("INSERT INTO order_ VALUES (" + String.format("%s,%s,%s,\"%s\",\"%s\",\"%s\",\"%s\", NULL, \"%s\");", 
							orderNumber, numberOfShares, pricePerShare, stockSymbol, timestamp, orderType, priceType, state));									
				} else {
					stmt.executeUpdate("INSERT INTO order_ VALUES (" + String.format("%s,%s,%s,\"%s\",\"%s\",\"%s\",\"%s\", %s, \"%s\");", 
							orderNumber, numberOfShares, triggerPrice, stockSymbol, timestamp, orderType, priceType, percentage, state));									
					stmt.executeUpdate("INSERT INTO OrderHistory VALUES (" + String.format("%s, %s, %s,\"%s\" );", orderNumber, pricePerShare, triggerPrice, timestamp));
				}
			}
			
			// INSERT INTO TRANSACTION TABLE			
			boolean notFoundTransactionID = true;
			int randomtransactionID = 0;
			while (notFoundTransactionID) {
				Random rand = new Random();
				randomtransactionID = rand.nextInt((100000- 1) + 1) + 1;
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
			System.out.println("****************************************************************");
			String accountNumber = request.getParameter("accountnumber");
			System.out.println(accountNumber);
			System.out.println("****************************************************************");
			
			stmt.executeUpdate("INSERT INTO trade VALUES (" + String.format("%s, %s, %s, %s);", accountNumber, orderNumber, transactionID, request.getSession().getAttribute("user_ssn")));
			
			
			if(priceType.equalsIgnoreCase("market")) {
				ResultSet hasStockResult = stmt.executeQuery("SELECT * FROM hasstock WHERE AccountNumber = " + accountNumber + " AND Symbol = \"" + stockSymbol + "\";");
				if(hasStockResult.isBeforeFirst()) {
					hasStockResult.next();
					String currentNumberOfShares = hasStockResult.getString("NumShares");
					stmt.executeUpdate("UPDATE hasstock SET NumShares = " + currentNumberOfShares + " - " + numberOfShares +" WHERE AccountNumber = " 
					                  + accountNumber + " AND Symbol = \"" + stockSymbol + "\";" );
				} else {
					stmt.executeUpdate("INSERT INTO hasstock VALUES (" + String.format("%s, \"%s\", %s);", accountNumber, stockSymbol, numberOfShares));
				}
//				totalAmountOfSharesCurrentlyAvailable
				
				hasStockResult = stmt.executeQuery("SELECT NumShares FROM Stock WHERE StockSymbol = \"" + stockSymbol + "\";");
				hasStockResult.next();
				
				String totalAmountOfSharesCurrentlyAvailable = hasStockResult.getString("NumShares");
				stmt.executeUpdate("UPDATE stock SET NumShares = " + totalAmountOfSharesCurrentlyAvailable + " + " + numberOfShares 
									+ " WHERE StockSymbol = \"" + stockSymbol + "\""+ ";");
			
			}
			
			
			RequestDispatcher rd = request.getRequestDispatcher("comfirmemployeesell.jsp");
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

}
