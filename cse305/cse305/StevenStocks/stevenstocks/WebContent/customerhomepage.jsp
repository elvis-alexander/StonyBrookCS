<!-- customer home page -->
<!DOCTYPE html>
<html lang="en">
<head>
<%@page import="settings.Quote"%>
<%@page import="settings.Logout"%>
<%@page import="settings.*"%>


    <title>Home</title>
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
	        <li><a href="customerhomepage.jsp">Home</a></li>
	        <li><a href="#">Settings</a></li>
	        <li>
	        <form id='order-search'action="OrderSearchCustomer" method="post">
	        	<a href="javascript:{}" onclick="document.getElementById('order-search').submit();">My Orders</a>
	        </form>      
	        </li>
	        <li>
	        <form id='stock-search'action="StockSearchCustomer" method="post">
	        	<a href="javascript:{}" onclick="document.getElementById('stock-search').submit();">Buy Stock</a>
	        </form>      
	        </li>
	        <li><a href="help.jsp">Help</a></li>
	        
	        <li><a href="Logout">Logout</a></li>
	    </ul>
	</div>

	<!-- content container -->
	 <div id="main-container">
		<div id="heading-container">
	 		<h1>Welcome, ${firstname} ${lastname}!</h1><hr id="title-line">
	 		<div id="img-quote-container">
			    <div class="col"><img width="250" height="250" alt="Profile" src="res/customer0.png"></div>
			    <div class="col">
			    	<h1>Quote of The Day:</h1>
			    	
			    	<p id="quote"><%=s%></p>
			    	<hr id="title-line">
			 	</div>
			</div>		 	 
		</div>
		<br>
	
	<div id="heading-container">
		<form id="addaccount" action="AddAccount" method="post">
			<a onclick="document.getElementById('addaccount').submit()" href="javascript:{}" id="save-button">Add Account</a>
			<input style="float:right" class="hidden" name="id" value="${id}">
		</form>
		<h1>Accounts</h1><hr id="title-line">
	</div>
		
	
<div id="customer-container">
<%
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
		java.sql.ResultSet result = stmt.executeQuery("SELECT A.AccountNumber, A.CreationDate FROM account_ A WHERE A.clientID = "+ session.getAttribute("id") +" ORDER BY A.CreationDate ASC;");
		while(result.next()){
			StringBuilder  sb = new StringBuilder("");
			sb.append("<div class=\"col\"><div id=\"customer-info-container\">");
			sb.append("<h1>Account Number: " + result.getString("AccountNumber") + "</h1>");
			sb.append("<h1>Creation Date: " + result.getString("CreationDate") + "</h1>");
			sb.append("<form action=\"CustomerViewStockByAccount\" method=\"post\"> <input type=\"hidden\" value=\"" + result.getString("AccountNumber") + "\" name=\"accountnumber\">");
			sb.append("<input type=\"submit\" id=\"search-button\" value=\"View Stocks\"></form>");
			sb.append("</div></div>");
			out.println(sb);
			// type="submit" id="search-button" href="StockSearchCustomer"  name="searchbutton" value="Search"
		}
		stmt.close();
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
%>
		</div>
	 </div>
</body>
</html>


