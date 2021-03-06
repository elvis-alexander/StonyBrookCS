<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@page import="settings.Logout" %>
<%@page import="settings.ViewStockHistory" %>
<%@page import="settings.StockSearchCustomer" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<link rel="stylesheet" href="style/employeehomepage.css">
	<link rel="stylesheet" href="style/manageorders.css">
	<link rel="stylesheet" href="style/employeetransaction.css">
	<link href='https://fonts.googleapis.com/css?family=Patua+One' rel='stylesheet' type='text/css'>
	
	<script type="text/javascript" src="js/chart2.js"></script>
	
	<title>Stock History</title>
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
		String stockSymbol = (String) request.getAttribute("stocksymbol");
	%>
	
	<div id="main-container">
		<div id="heading-container">
			<form id='back-stock-search'action="StockSearch" method="post">
				<a href="javascript:{}" onclick="document.getElementById('back-stock-search').submit();">Back</a>
			</form>		
	 		<h1>History for stock: <%=stockSymbol%></h1><hr id="title-line">	 	 
		</div>
			
			<div>
				<canvas id="canvas"></canvas>
			</div>
			<script type="text/javascript">
				<%
					String data = (String) request.getAttribute("data");
					out.println(data);
				%>
				
				var stockHistory = document.getElementById('canvas').getContext('2d');
				var myLineChart = Chart.Line(stockHistory, {data: stockData, options: {responsive: true,maintainAspectRatio: true, lineTension:0}});
			</script>
	</div>

</body>
</html>