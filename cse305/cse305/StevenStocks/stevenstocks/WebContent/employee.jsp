<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page import="settings.SelectEmployee" %>
<%@page import="settings.Logout" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<link rel='shortcut icon' href='res/favicon.ico' type='image/x-icon'>
	<link rel="stylesheet" href="style/employeehomepage.css">
	<link href='https://fonts.googleapis.com/css?family=Patua+One' rel='stylesheet' type='text/css'>
	<title>Employees</title>
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
	
	<div id="main-container">
		<div id="heading-container">
				<!-- create addemployee.jsp -->
		 		<a id="add-employee" href="addemployee.jsp">Add Employee</a>	 	 
		 		<h1>Employees</h1><hr id="title-line">
		</div>
		
		<div id="customer-container">
			<%
				String jdbc_driver= "com.mysql.jdbc.Driver";  
				String url = "jdbc:mysql://mysql2.cs.stonybrook.edu:3306/blanunez";
		
		   		String user = "blanunez";
		   		String pass = "109162285";
		
		   		java.sql.Connection conn = null;
			   	java.sql.Statement stmt = null;
			 
		  		try {
		  			Class.forName(jdbc_driver).newInstance();
					conn = java.sql.DriverManager.getConnection(url, user, pass);
					
					stmt = conn.createStatement();
					java.sql.ResultSet result = stmt.executeQuery("SELECT E.FirstName, E.LastName, E.UserName, E.SSN, E.Role FROM employee E WHERE E.Role != \"Manager\" ORDER BY E.FirstName ASC;");
					
					while(result.next()){
						StringBuilder  sb = new StringBuilder("");
						sb.append("<div class=\"col\"><div id=\"customer-info-container\"><form action=\"SelectEmployee\" method=\"post\"><div class=\"col\">");
						sb.append("<h1>First Name: " + result.getString("FirstName") + "</h1>");
						sb.append("<h1>Last Name: " + result.getString("LastName") + "</h1>");
						sb.append("<h1>Username: " + result.getString("UserName") + "</h1>");
						// dummy input for spacing
						sb.append("<input type=\"text\" class=\"hidden\" name=\"email\" value=\"" + result.getString("UserName") + "\">");
						sb.append("<input type=\"text\" class=\"hidden\" name=\"ssn\" value=\"" + result.getString("SSN") + "\">");
						sb.append("<input class=\"select-customer\" type=\"submit\" value=\"Edit Employee\"></div><div class=\"col\"><img width=\"100\" height=\"100\" alt=\"Profile\" src=\"res/"+result.getString("Role")+"0.png\"></div></form></div><hr></div>");
						out.println(sb);
					}
					
					stmt.close();
					conn.close();
		  		} catch (ClassNotFoundException e){
					e.printStackTrace();
					conn.close();
				} catch (java.sql.SQLException e) {
					e.printStackTrace();
					conn.close();
				} catch (InstantiationException e) {
					e.printStackTrace();
					conn.close();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
					conn.close();
				}
			%>
		   
		</div>
	</div>

</body>
</html>