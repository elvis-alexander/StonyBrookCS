<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page import="settings.EditCustomer"%>
<%@page import="settings.PlaceOrder"%>
<%@page import="settings.StockSuggestion"%>
<%@page import="settings.Logout"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<link rel='shortcut icon' href='res/favicon.ico' type='image/x-icon'>
	<link rel="stylesheet" href="style/employeehomepage.css">
	<link rel="stylesheet" href="style/managersettings.css">
	<link href='https://fonts.googleapis.com/css?family=Patua+One' rel='stylesheet' type='text/css'>
	<title>Customer Options</title>
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
	
	<div id="main-container">
		<div id="heading-container">
			<a id="add-customer"href="customer.jsp">Back</a>		 	 
	 		<h1>Customer Options</h1><hr id="title-line">
		</div>
		
<%
	String id = (String) request.getAttribute("id");
	String firstname = (String) request.getAttribute("firstname");
	String lastname = (String) request.getAttribute("lastname");
	String email = (String) request.getAttribute("email");
	String phonenumber = (String) request.getAttribute("phonenumber");
	String address = (String) request.getAttribute("address");
	String city = (String) request.getAttribute("city");
	String state = (String) request.getAttribute("state");
	String zipcode = (String) request.getAttribute("zipcode");
%>
		<div id="customer-container">
		
			<h1>Edit Customer Information</h1>
			<!-- EditCustomer  -->
		
				<form  class="customer-form" action="EditCustomer" method="post">
				<input class="select-customer" type="submit" value="Edit Customer" name="editbutton">
				<input type="hidden" name="firstname" value="<%=firstname%>"><br>
				<input type="hidden" name="lastname" value="<%=lastname%>"><br>
				<input type="hidden" name="email" value="<%=email%>"><br>
				<input type="hidden" name="telephone" value="<%=phonenumber%>"><br>
				<input type="hidden" name="address" value="<%=address%>"><br>
				<input type="hidden" name="city" value="<%=city%>"><br>
				<input type="hidden" name="state" value="<%=state%>"><br>
				<input type="hidden" name="zipcode" value="<%=zipcode%>"><br>
				<input type="hidden" name="id" value="<%=id%>">	
				</form>
			
			<h1>Place Order to Buy</h1>
			<!-- Place Order  -->
				<form  class="customer-form" action="PlaceOrderEmployee" method="post">
				<input class="select-customer" type="submit" value="Place Order" name="searchbutton">
				<input type="hidden" name="firstname" value="<%=firstname%>"><br>
				<input type="hidden" name="lastname" value="<%=lastname%>"><br>
				<input type="hidden" name="email" value="<%=email%>"><br>
				<input type="hidden" name="telephone" value="<%=phonenumber%>"><br>
				<input type="hidden" name="address" value="<%=address%>"><br>
				<input type="hidden" name="city" value="<%=city%>"><br>
				<input type="hidden" name="state" value="<%=state%>"><br>
				<input type="hidden" name="zipcode" value="<%=zipcode%>"><br>
				<input type="hidden" name="id" value="<%=id%>">	
				</form>
				
			<h1>Place Order to Sell</h1>
			<!-- Place Order  -->
				<form  class="customer-form" action="PlaceOrderEmployee" method="post">
				<input class="select-customer" type="submit" value="Place Order" name="selltrigger">
				<input type="hidden" name="firstname" value="<%=firstname%>"><br>
				<input type="hidden" name="lastname" value="<%=lastname%>"><br>
				<input type="hidden" name="email" value="<%=email%>"><br>
				<input type="hidden" name="telephone" value="<%=phonenumber%>"><br>
				<input type="hidden" name="address" value="<%=address%>"><br>
				<input type="hidden" name="city" value="<%=city%>"><br>
				<input type="hidden" name="state" value="<%=state%>"><br>
				<input type="hidden" name="zipcode" value="<%=zipcode%>"><br>
				<input type="hidden" name="id" value="<%=id%>">	
				</form>

			
			<h1>Get stock suggestions</h1>
			<!-- Stock Suggestion  -->
				<form  class="customer-form" action="StockSuggestion" method="post">
				<input class="select-customer" type="submit" value="Get Suggestions" name="editbutton">
				<input type="hidden" name="firstname" value="<%=firstname%>"><br>
				<input type="hidden" name="lastname" value="<%=lastname%>"><br>
				<input type="hidden" name="email" value="<%=email%>"><br>
				<input type="hidden" name="telephone" value="<%=phonenumber%>"><br>
				<input type="hidden" name="address" value="<%=address%>"><br>
				<input type="hidden" name="city" value="<%=city%>"><br>
				<input type="hidden" name="state" value="<%=state%>"><br>
				<input type="hidden" name="zipcode" value="<%=zipcode%>"><br>
				<input type="hidden" name="id" value="<%=id%>">	
				</form>

		</div>	
	</div>
</body>
</html>