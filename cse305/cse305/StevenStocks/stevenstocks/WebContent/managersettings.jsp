<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page import="settings.Logout" %>
<%@page import="settings.TriggerMarketOnClose" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<link rel='shortcut icon' href='res/favicon.ico' type='image/x-icon'>
	<link rel="stylesheet" href="style/employeehomepage.css">
	<link rel="stylesheet" href="style/manageorders.css">
	<link rel="stylesheet" href="style/managersettings.css">
	<link href='https://fonts.googleapis.com/css?family=Patua+One' rel='stylesheet' type='text/css'>
	<title>Settings</title>
</head>
<body> 

	<div id="sidebar-container">
		<img id="logo"src="res/ss_logo.png">
	    <ul class="sidebar-nav">
	        <li><a href="managerhomepage.jsp">Account</a></li>
	        <li><a href="#">Settings</a></li>
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
	
	<%
		String result = (String) request.getAttribute("result");
		
		if(result == null)
			result = "";
	%>
	<div id="main-container">
		<div id="heading-container">
		 		<h1>Settings</h1><hr id="title-line">
		</div>
		
		<div class="trigger-container">
			<h1 class="order-number">Simulate market on close</h1>
			 <form id="trigger-form"action="TriggerMarketOnClose" method="post">
	        	<a id="trigger-button" href="javascript:{}" onclick="document.getElementById('trigger-form').submit();">Simulate</a>
	        </form>
	        <h1 class="no-orders"><%=result%></h1>  
		</div>
		
	</div>

</body>
</html>