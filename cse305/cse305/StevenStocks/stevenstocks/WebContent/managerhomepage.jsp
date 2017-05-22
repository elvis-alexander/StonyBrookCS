<!DOCTYPE html>
<html lang="en">
<head>
<%@page import="settings.Quote"%>
<%@page import="settings.Logout"%>
<%@page import="settings.StockSearch"%>
<%@page import="settings.OrderSearch"%>

    <title>Your Account</title>
    <meta charset="utf-8">
    <link rel='shortcut icon' href='res/favicon.ico' type='image/x-icon'>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="style/employeehomepage.css">
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
	        <li><a href="#">Account</a></li>
	        <li><a href="managersettings.jsp">Settings</a></li>
	        <li><a href="employee.jsp">Employees</a></li>
	        <li><a href="managecustomers.jsp">Customers</a></li>
	        <li>
	        <form id='stock-search'action="StockSearch" method="post">
	        	<a href="javascript:{}" onclick="document.getElementById('stock-search').submit();">Stocks</a>
	        </form>      
	        </li>
	        <li>
	        <form id='order-search'action="OrderSearch" method="post">
	        	<a href="javascript:{}" onclick="document.getElementById('order-search').submit();">Orders</a>
	        </form>      
	        </li>
	        <li>
	        <form id='revenue-search'action="RevenueSearch" method="post">
	        	<a href="javascript:{}" onclick="document.getElementById('revenue-search').submit();">Revenue</a>
	        </form>      
	        </li> 
	        <li><a href="Logout">Logout</a></li>
	    </ul>
	</div>

	<!-- content container -->

	 <div id="main-container">
		<div id="heading-container">
	 		<h1>Welcome, ${user_firstName} ${user_lastName}!</h1><hr id="title-line">
	 		<div id="img-quote-container">
			    <div class="col"><img width="250" height="250" alt="Profile" src="res/Manager0.png"></div>
			    <div class="col">
			    	<h1>Quote of The Day:</h1>
			    	<p id="quote"><%=s%></p>
			    	<hr id="title-line">
			 	</div>
			</div>		 	 
		</div>
		<br/>
	 </div>
</body>
</html>


