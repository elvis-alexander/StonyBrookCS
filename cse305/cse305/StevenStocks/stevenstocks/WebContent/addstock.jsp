<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page import="settings.AddStock"%>
<%@page import="settings.Logout"%>


<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<link rel='shortcut icon' href='res/favicon.ico' type='image/x-icon'>
	<link rel="stylesheet" href="style/employeehomepage.css">
	<link rel="stylesheet" href="style/employeetransaction.css">
	<link href='https://fonts.googleapis.com/css?family=Patua+One' rel='stylesheet' type='text/css'>
	<title>Add Stock</title>
</head>
<body>

	<div id="sidebar-container">
		<img id="logo"src="res/ss_logo.png">
	    <ul class="sidebar-nav">
	        <li><a href="managerhomepage.jsp">Account</a></li>
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
	
	<div id="main-container">
		<div id="heading-container">
			<form id='back-stock-search'action="StockSearch" method="post">
				<a href="javascript:{}" onclick="document.getElementById('back-stock-search').submit();">Back</a>
			</form>		
	 		<h1>Add Stock</h1><hr id="title-line">	 	 
		</div>

		<div id="customer-form-container">
			<form  class="customer-form" action="AddStock" method="post">
				<label class="form-label">Stock Symbol:</label>
				<input type="text" name="stocksymbol" value=""><br>
				
				<label class="form-label">Company Name:</label>
				<input type="text" name="companyname" value=""><br>

				<label class="form-label">Stock type</label>
				<input type="text" name="stocktype" value=""><br>
				
				<label class="form-label">Share Price:</label>
				<input type="text" name="shareprice" value=""><br>

				<label class="form-label">Total Shares:</label>
				<input type="text" name="numshares" value=""><br>
				
				<label class="form-label hidden">Zip Code:</label>
				<input id="save-button" type="submit" value="Create Stock">
	
			</form>
		</div>
		
	</div>

</body>
</html>