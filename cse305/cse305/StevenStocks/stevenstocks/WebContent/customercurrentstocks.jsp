<!-- customer home page -->
<!DOCTYPE html>
<html lang="en">
<head>
<%@page import="settings.Quote"%>
<%@page import="settings.Logout"%>

    <title>Your Account</title>
    <meta charset="utf-8">
    <link rel='shortcut icon' href='res/favicon.ico' type='image/x-icon'>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="style/customerhomepage.css">
    <link href='https://fonts.googleapis.com/css?family=Patua+One' rel='stylesheet' type='text/css'>
</head>
<body>
<%
	Quote q = new Quote(); 
	String s = q.getQuote();
%>
	<!-- Navigation bar-->
	<div id="sidebar-container">
		<img id="logo"src="res/ss_logo.png">
	    <ul class="sidebar-nav">
	        <li><a href="customerhomepage.jsp">Account</a></li>
	        <li><a href="#">Settings</a></li>
	        <li><a href="customercurrentstocks.jsp">Current Stocks</a></li>
	        
	        <li>
	        <form id='stock-search'action="StockSearchCustomer" method="post">
	        	<a href="javascript:{}" onclick="document.getElementById('stock-search').submit();">Make an Order</a>
	        </form>      
	        </li>	        
	        <li><a href="Logout">Logout</a></li>
	    </ul>
	</div>
	
	
	<div id="main-container">
		<div id="heading-container">		    
<% 
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
			stmt = conn.createStatement();
	 		// GET ALL ACCOUNTS CORRESPONDING TO CURRENT CUSTOMER
	 		String clientID = (String) session.getAttribute("id");
			java.sql.ResultSet result = stmt.executeQuery("SELECT * FROM hasstock WHERE ClientID = " + clientID + ";");
			while(result.next()){
				StringBuilder sb = new StringBuilder("");
				String accountNumber = result.getString("AccountNumber");
				sb.append("<option value= \"" + accountNumber + "\">" + accountNumber + "</option>");
				out.println(sb);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
%>
		</div>
	</div>
	
	
</body>
</html>