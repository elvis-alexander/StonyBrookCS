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
	<title>Sell</title>
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
			 
			 <a id="add-customer" href="customer.jsp">Back</a>
			 <h1><%=customerFirstName%> <%=customerLastName%>'s Current Stock Holdings</h1><hr id="title-line">
		</div>
<%
		String result = (String)request.getAttribute("searchresult");
		out.println(result);
	
%>

	</div>
	<script type="text/javascript">
		 	var elements = document.getElementsByClassName("accountnumber");
			console.log(elements.length);
		 	for(var i=0; i < elements.length; i++) {
		 		elements[i].style.display = 'none';
		 	}
	</script>
</body>
</html>

