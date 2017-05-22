<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%> 
<%@page import="settings.AddCustomer"%>
<%@page import="settings.SelectCustomer"%> 
<%@page import="settings.Logout"%> 



<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<link rel='shortcut icon' href='res/favicon.ico' type='image/x-icon'>
	<link rel="stylesheet" href="style/employeehomepage.css">
	<link rel="stylesheet" href="style/employeetransaction.css">
	<link href='https://fonts.googleapis.com/css?family=Patua+One' rel='stylesheet' type='text/css'>
	<title>Customers</title>
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
			<a id="add-customer"href="managecustomers.jsp">Back</a>	
	 		<h1>Add Customer</h1><hr id="title-line">	 	 
		</div>

		<div id="customer-form-container">
			<form  class="customer-form" action="AddCustomer" method="post">
				<label class="form-label">First Name:</label>
				<input type="text" name="firstname" value=""><br>

				<label class="form-label">Last Name:</label>
				<input type="text" name="lastname" value=""><br>

				<label class="form-label">Email Address:</label>
				<input type="text" name="email" value=""><br>
				
				<label class="form-label">Password:</label>
				<input type="password" name="password" value=""><br>

				<label class="form-label">Phone Number:</label>
				<input type="text" name="telephone" value=""><br>

				<label class="form-label">Address:</label>
				<input type="text" name="address" value=""><br>

				<label class="form-label">City:</label>
				<input type="text" name="city" value=""><br>

				<label class="form-label">State:</label>
				<input type="text" name="state" value=""><br>

				<label class="form-label">Zip Code:</label>
				<input type="text" name="zipcode" value=""><br>
				
				<label class="form-label">Credit Card:</label>
				<input type="text" name="creditcard" value=""><br>
				
				<label class="form-label hidden">Zip Code:</label>
				<input id="save-button" type="submit" name="managertrigger"value="Create Customer">
	
			</form>
		</div>
		
	</div>

</body>
</html>