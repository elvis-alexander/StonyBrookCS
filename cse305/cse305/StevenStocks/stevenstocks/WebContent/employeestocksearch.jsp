<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page import="settings.Logout" %>
<%@page import="settings.StockSearch" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<link rel='shortcut icon' href='res/favicon.ico' type='image/x-icon'>
	<link rel="stylesheet" href="style/employeehomepage.css">
	<link rel="stylesheet" href="style/managestocks.css">
	<link href='https://fonts.googleapis.com/css?family=Patua+One' rel='stylesheet' type='text/css'>
	<title>Stock Manager</title>
</head>
<body>

	<div id="sidebar-container">
		<img id="logo"src="res/ss_logo.png">
	    <ul class="sidebar-nav">
	        <li><a href="employeehomepage.jsp">Account</a></li>
	        <li><a href="#">Settings</a></li>
	        <li><a href="customer.jsp">Customers</a></li>
	        <li><a href="employeeemployee.jsp">Employees</a></li>
	        <li><a href="mailinglist.jsp">Mailing List</a></li>
	        <li><a href="Logout">Logout</a></li>
	    </ul>
	</div>
	
	<%
		String customerFirstName = (String) request.getAttribute("customerfirstname");
		String customerLastName = (String) request.getAttribute("customerlastname");
		String customerID = (String) request.getAttribute("customerid");
	%>
	
	<div id="main-container">
		<div id="heading-container">
		 		<form action="PlaceOrderEmployee" method="post">
		 			<input type="hidden" name="firstname" value=<%=customerFirstName %>>
		 			<input type="hidden" name="lastname" value=<%=customerLastName%>>
		 			<input type="hidden" name="id" value=<%=customerID %>>
		 			<input type="submit" id="search-button" name="searchbutton" value="Search">
					<input type="text" placeholder="symbol or type" value="" name="searchstring">
				</form>
				<h1 id="search-label">Search</h1>
		 		<h1>Select stock for customer: <%=customerFirstName %> <%=customerLastName %></h1><hr id="title-line">
		</div>
		
		
		
		<div id="stock-container">
			<div id="stock-table-header">
				<div class="col">
				<h1>Symbol</h1>
				</div>
				<div class="col">
				<h1>Information</h1>
				</div>
				<div class="col">
				<h1>Pricing</h1>
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