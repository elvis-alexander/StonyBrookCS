<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page import="settings.SelectCustomer"%>
<%@page import="settings.UpdateCustomer"%>
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
	 		<h1>Customer Editor</h1><hr id="title-line">	 	 
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

		<div id="customer-form-container">
			<form  class="customer-form" action="UpdateCustomer" method="post">
				<label class="form-label">First Name:</label>
				<input type="text" name="firstname" value="<%=firstname%>"><br>

				<label class="form-label">Last Name:</label>
				<input type="text" name="lastname" value="<%=lastname%>"><br>

				<label class="form-label">Email Address:</label>
				<input type="text" name="email" value="<%=email%>"><br>

				<label class="form-label">Phone Number:</label>
				<input type="text" name="telephone" value="<%=phonenumber%>"><br>

				<label class="form-label">Address:</label>
				<input type="text" name="address" value="<%=address%>"><br>

				<label class="form-label">City:</label>
				<input type="text" name="city" value="<%=city%>"><br>

				<label class="form-label">State:</label>
				<input type="text" name="state" value="<%=state%>"><br>

				<label class="form-label">Zip Code:</label>
				<input type="text" name="zipcode" value="<%=zipcode%>"><br>
				
				<label class="form-label hidden">Zip Code:</label>
				<input id="save-button" type="submit" value="Save Changes" name="savebutton">
				<input id="delete-button" type="submit" value="Remove" name="removebutton">
				<input type="hidden"  name="id" value="<%=id%>">
	
			</form>
		</div>
		
	</div>

</body>
</html>