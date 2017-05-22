<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page import="settings.Logout"%>



<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<link rel='shortcut icon' href='res/favicon.ico' type='image/x-icon'>
 	<link rel="stylesheet" href="style/customerhomepage.css">
   	<link rel="stylesheet" href="style/employeetransaction.css">
	<link href='https://fonts.googleapis.com/css?family=Patua+One' rel='stylesheet' type='text/css'>
	<title>History Range</title>
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
	
	<%
		String stockSymbol = (String) request.getParameter("stocksymbol");
	%>
	
	<div id="main-container">
		<div id="heading-container">
			<form id='back-stock-search'action="StockSearch" method="post">
				<a href="javascript:{}" onclick="document.getElementById('back-stock-search').submit();">Back</a>
			</form>	
	 		<h1>Select Search Range for Stock: <%=stockSymbol%></h1><hr id="title-line">	 	 
		</div>

		<div id="customer-form-container">
			<form  class="customer-form" action="ViewStockHistory" method="post">
				
				<label class="form-label">Start Date:</label>
				<input id="startdate" type="date" name="startdate" value="" required="required"><br>
				
				<label class="form-label">End Date:</label>
				<input id="enddate" type="date" name="enddate" value="" required="required"><br>
				
				<input id="save-button" type="submit"  name="managertrigger" value="View History">
				<input type="hidden" name="stocksymbol" value="<%=stockSymbol%>"><br>
	
			</form>
			
			<script type="text/javascript">
				document.getElementById('startdate').valueAsDate = new Date();
				document.getElementById('enddate').valueAsDate = new Date();
			</script>
		</div>
		
	</div>

</body>
</html>