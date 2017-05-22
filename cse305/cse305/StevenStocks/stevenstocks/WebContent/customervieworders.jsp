<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@page import="settings.Logout" %>
<%@page import="settings.OrderSearchCustomer" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<link rel="stylesheet" href="style/employeehomepage.css">
	<link rel="stylesheet" href="style/manageorders.css">
	<link href='https://fonts.googleapis.com/css?family=Patua+One' rel='stylesheet' type='text/css'>
	
	<title>Your Orders</title>
</head>
<body>

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
	        <li><a href="Logout">Logout</a></li>
	    </ul>
	</div>
	<%
		String customerID = (String) session.getAttribute("id");
	%>
	
	<div id="main-container">
		<div id="heading-container">
			<form action="OrderSearchCustomer" method="post">
		 			<input type="hidden" id="search-button"  name="customerID" value="<%=customerID%>">
		 			<input type="submit" id="search-button"  name="searchbutton" value="Search">
					<input type="text" placeholder="symbol" value="" name="searchstring">
				</form>
				<h1 id="search-label">Search</h1>
		 		<h1>Your Orders</h1><hr id="title-line">
		</div>
		
		<div id="order-container">
			<div id="order-table-header">
				<div class="col">
					<h1>Order Number</h1>
				</div>
				<div class="col">
					<h1>Information</h1>
				</div>
				<div class="col">
					<h1>Pricing</h1>
				</div>
				<div class="col">
					<h1>Date</h1>
				</div>
				<div class="col">
					<h1>Status</h1>
				</div>
			</div>
			<%
				String result = (String)request.getAttribute("searchresult");
				out.println(result);
			%>
		</div>
	</div>

</body>
</html>