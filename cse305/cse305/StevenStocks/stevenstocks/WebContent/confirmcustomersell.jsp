<!-- AFTER CLICKED ON STOCK -->

<!DOCTYPE html>
<html lang="en">
<head>
<%@page import="settings.Quote"%>
<%@page import="settings.Logout"%>

    <title>Confirmation</title>
    <meta charset="utf-8">
    <link rel='shortcut icon' href='res/favicon.ico' type='image/x-icon'>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="style/customerhomepage.css">
   	<link rel="stylesheet" href="style/employeetransaction.css">
    <link href='https://fonts.googleapis.com/css?family=Patua+One' rel='stylesheet' type='text/css'>
</head>
<body onload="onLoadBody()">

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
	
	<!-- trasactionid trasactionfee ordernumber -->
	<%
	String orderConfirmationMessage = (String)request.getAttribute("orderConfirmationMessage");
	%>
	
	<div id="main-container">
		<div id="heading-container">
			<div><%=orderConfirmationMessage%></div>
		</div>
	</div>	
</body>