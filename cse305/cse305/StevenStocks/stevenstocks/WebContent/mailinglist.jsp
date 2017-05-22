<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page import="settings.SelectCustomer"%>
<%@page import="settings.Logout"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<link rel='shortcut icon' href='res/favicon.ico' type='image/x-icon'>
	<link rel="stylesheet" href="style/employeehomepage.css">
	<link href='https://fonts.googleapis.com/css?family=Patua+One' rel='stylesheet' type='text/css'>
	<title>Mailing List</title>
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
		 		<h1>Mailing List</h1><hr id="title-line">
		</div>
		
		<div id="mailing-list">
		<table style="width:100%">			
  			<tr>
			    <th>First Name</th>
			    <th>Last Name</th> 
			    <th>Email</th>
			    <th>Address</th>
  			</tr>
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
					java.sql.ResultSet result = stmt.executeQuery("SELECT C.* FROM Client_ C ORDER BY C.FirstName ASC;");
					while(result.next()){
						StringBuilder  sb = new StringBuilder("");
						sb.append("<tr><td>" + result.getString("FirstName") + "</td>");
						sb.append("<td>" + result.getString("LastName") + "</td>");
						sb.append("<td>" + result.getString("Email") + "</td>");
						sb.append("<td>" + result.getString("Address") + " " + result.getString("City") + " " + result.getString("State") + " " + result.getString("ZipCode") + " " +  "</td></tr>");
						out.println(sb);
					}
		  		} catch (ClassNotFoundException e){
					e.printStackTrace();
				} catch (java.sql.SQLException e) {
					e.printStackTrace();
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			%>
		</table>
	 	</div>
		
	</div>

</body>
</html>