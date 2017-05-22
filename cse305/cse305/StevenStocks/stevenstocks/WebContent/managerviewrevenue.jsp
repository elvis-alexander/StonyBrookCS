<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page import="settings.Logout" %>
<%@page import="settings.StockSearch" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<link rel='shortcut icon' href='res/favicon.ico' type='image/x-icon'>
	<link rel="stylesheet" href="style/employeehomepage.css">
	<link rel="stylesheet" href="style/manageorders.css">
	<link href='https://fonts.googleapis.com/css?family=Patua+One' rel='stylesheet' type='text/css'>
	<title>Revenue Summary</title>
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
		 			String filter = (String) request.getAttribute("filter");
		 			String result = (String) request.getAttribute("searchresult");
	 %>
	
	<div id="main-container">
		<div id="heading-container">
		 		<form action="RevenueSearch" method="post">
		 			<input type="submit" id="search-button" name="searchbutton" value="Apply Filter">
					<select class="filter"  name="option">
						    <option value="stock">Stock</option>
						    <option value="industry">Industry</option>
						    <option value="customer">Customer</option>
						    <option value="representatives">Representatives</option>					    
				  	</select>
					<h1 id="search-label">Select Filter</h1>
			  	</form>
		 		<h1>Revenue by: <%=filter%></h1><hr id="title-line">
		 </div>
		 		

					<%
		 			if(filter == null || filter.equalsIgnoreCase("stock")){
		 				%>
		 					<div id="order-container">
								<div id="order-table-header">
									<div class="col">
										<h1>Rank</h1>
									</div>
									<div class="col">
										<h1>Symbol</h1>
									</div>
									<div class="col">
										<h1>Information</h1>
									</div>
									<div class="col">
										<h1>Sales</h1>
									</div>
									<div class="col">
										<h1>Revenue</h1>
									</div>
								</div>
								<%
									out.println(result);
								%>
							</div>
		 				<%
		 			}else if(filter.equalsIgnoreCase("industry")){
		 				 %>
		 					<div id="order-container">
								<div id="order-table-header">
									<div class="col">
										<h1>Rank</h1>
									</div>
									<div class="col">
										<h1>Industry</h1>
									</div>
									<div class="col">
										<h1>Sales</h1>
									</div>
									<div class="col">
										<h1>Revenue</h1>
									</div>
								</div>
								<%
									out.println(result);
								%>
							</div>
		 				 <%
		 			}else if(filter.equalsIgnoreCase("customer")){
		 				 %>
		 					<div id="order-container">
								<div id="order-table-header">
									<div class="col">
										<h1>Rank</h1>
									</div>
									<div class="col">
										<h1>Information</h1>
									</div>
									<div class="col">
										<h1>Sales</h1>
									</div>
									<div class="col">
										<h1>Revenue</h1>
									</div>
								</div>
								<%
									out.println(result);
								%>
							</div>
		 				 <%
		 			}else if(filter.equalsIgnoreCase("representative")){
		 				 %>
		 					<div id="order-container">
								<div id="order-table-header">
									<div class="col">
										<h1>Rank</h1>
									</div>
									<div class="col">
										<h1>Information</h1>
									</div>
									<div class="col">
										<h1>Sales</h1>
									</div>
									<div class="col">
										<h1>Revenue</h1>
									</div>
								</div>
								<%
									out.println(result);
								%>
							</div>
		 				 <%
		 			}else{
		 				%>
		 					out.println(result);
		 				 <%
		 			}
		 		%>
		
		
	</div>

</body>
</html>