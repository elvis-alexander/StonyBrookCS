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
	<link rel="stylesheet" href="style/manageorders.css">
	<link href='https://fonts.googleapis.com/css?family=Patua+One' rel='stylesheet' type='text/css'>
	<title>Stock Manager</title>
</head>
<body>
	
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
	        <li><a href="Logout">Logout</a></li>
	    </ul>
	</div>

	<div id="main-container">
	
		<div id="heading-container">
			<% String accNumber = (String)request.getAttribute("accountnumber"); %>
			 
			 <a id="add-customer" href="customerhomepage.jsp">Back</a>
			 <h1>Current Stock Holdings Under Account: <%=accNumber%></h1><hr id="title-line">
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
	String result = (String)request.getAttribute("customerstocksell");
	out.println(result);
	
%>
	<script type="text/javascript">
		 	var elements = document.getElementsByClassName("accountnumber");
			console.log(elements.length);
		 	for(var i=0; i < elements.length; i++) {
		 		elements[i].style.display = 'none';
		 	}
	</script>
		</div>
	</div>
</body>
</html>

