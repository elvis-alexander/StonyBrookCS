<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>Coming Soon...</title>
<link rel='shortcut icon' href='res/favicon.ico' type='image/x-icon'>
<link href='https://fonts.googleapis.com/css?family=Patua+One' rel='stylesheet' type='text/css'>
<link rel="stylesheet" type="text/css" href="style/index.css">

<script type="text/javascript" src="js/anim.js"></script>
<script type="text/javascript" src="js/Chart.js"></script>
</head>

<body>
	<div class="banner">
		<img id="logo" width="250" height="100" src="res/ss_logo.png">
	</div>

	<canvas id="background-canvas"></canvas>

	<div class="main-container">
		<div class="chart-container">
			<div style="width: 50%; margin-left: 20px; float: left;">
				<h1 id="welcome-header">Welcome to StevenStocks</h1>
				<hr />
				<p id="welcome-paragraph">We are a financial trade solution
					company. Allowing customers to buy and sell stocks between
					themselves and stock brokers. Managers may be able to modify any
					existing values on stocks and control the information regarding
					stock brokers. They are able to determine information such as which
					customer generated the most total revenue, most actively traded
					stocks and more.</p>
				<div align="center" style="width: 100%">
					<canvas id="canvas" height="350" width="600"></canvas> 
				</div>
			</div>
		</div>
		
		<% 
			String error_msg = (String) request.getAttribute("login_error");
			
			if(error_msg == null)
				error_msg = "";
			
		%>
		
 		<form action="Login" method="post">
			<div class="login-container">
				<h2 class="login-header">Login</h2>
				<input class="login-field" type="text" placeholder="Username" name="username" required> 
				<input class="login-field" type="password" placeholder="Password" name="password" required>
				<br> 
				<input id="login-button" type="submit" value="Login">
				<input id="login-button" onclick="register()" value="Register">
				<h2 class="login-header login-error"><%=error_msg%></h2>		
			</div>
 		</form>
		<script>
			function register()
			{
				location.href = "/stevenstocks/register.jsp";
			}
		</script>
	</div>
</body>
</html>
