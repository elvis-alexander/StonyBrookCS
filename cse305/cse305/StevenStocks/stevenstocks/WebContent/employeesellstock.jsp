<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page import="settings.Logout" %>
<%@page import="settings.EditStock" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<link rel='shortcut icon' href='res/favicon.ico' type='image/x-icon'>
	<link rel="stylesheet" href="style/employeehomepage.css">
	<link rel="stylesheet" href="style/employeetransaction.css">
	<link href='https://fonts.googleapis.com/css?family=Patua+One' rel='stylesheet' type='text/css'>
	<title>Sell Stock</title>
</head>
<body>

	<!-- Navigation bar-->
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
		String stockSymbol = (String)request.getAttribute("stocksymbol");
		String companyName = (String)request.getAttribute("companyname");
		String stockType = (String)request.getAttribute("stocktype");
		String sharePrice = (String)request.getAttribute("shareprice");
		String numShares = (String)request.getAttribute("numshares");
		String accountNumber = (String)request.getAttribute("accountnumber");
		String customerFirstName = (String)request.getAttribute("customerfirstname");
		String customerLastName = (String)request.getAttribute("customerlastname");
		String customerID = (String)request.getAttribute("customerid");
	%>
	<!-- Begins content -->
	<div id="main-container">
		<div id="heading-container">
			<form id='back-to-stock'action="PlaceOrderEmployee" method="post">
				<input type="hidden" name="selltrigger" value="">
				<input type="hidden" name="firstname" value="<%=customerFirstName%>">
				<input type="hidden" name="lastname" value="<%=customerLastName%>">
				<input type="hidden" name="id" value="<%=customerID%>">
	        	<a href="javascript:{}" onclick="document.getElementById('back-to-stock').submit();">Back</a>
	        </form> 
	 		<h1>Sell Stock for Customer: <%=customerFirstName %> <%=customerLastName %></h1><hr id="title-line">
		</div>	
		
		<div id="customer-form-container">
			<div class="col">
				<form  class="customer-form" action="EmployeeSellStockFinal" method="post">
					<label class="form-label">Stock Symbol:</label>
					<input type="text" id="stocksymbol" name="stocksymbol" value="<%=stockSymbol%>"><br>
					
					<label class="form-label">Company Name:</label>
					<input type="text" id="companyname" name="companyname" value="<%=companyName%>"><br>
					
					<label class="form-label">Stock Type:</label>
					<input type="text" id="stocktype" name="stocktype" value="<%=stockType%>"><br>
					
					<label class="form-label">Share Price:</label>
					<input type="text" id="shareprice" name="shareprice" value="<%=sharePrice%>"><br>
					
					<label class="form-label">Max Shares:</label>
					<input type="text" id="numshares" name="numshares" value="<%=numShares%>"><br>
					
					<label class="form-label">Account:</label>
					<input type="text" id="accountnumber" name="accountnumber" value=<%=accountNumber%> ><br>
				  	
				  	<label class="form-label">Percentage:</label>
					<input id="percentage" type="number" min="1" step="any" name="percentage" value="" disabled><br>
					
					<label class="form-label">Trigger Price:</label>
					<input id="triggerprice" type="number" step="any" name="triggerprice" value=""><br>
				  	
				  	
					<!-- Price type drop down -->
					<label class="form-label">Price Type:</label>
					<select id="type-selection-menu-drop-operator-menu-option"  name="option">
					    <option value="market">Market</option>
					    <option value="marketonclose">Market on Close</option>
					    <option value="trailingstop">Trailing Stop</option>
					    <option value="hiddenstop">Hidden Stop</option>					    
				  	</select><br>
				  	
				  	<script type="text/javascript">
				  	var drop = document.getElementById('type-selection-menu-drop-operator-menu-option');
					drop.addEventListener('change', function() {
						var option = drop.options[drop.selectedIndex].value;
						console.log(option);		
							if(option == "trailingstop" || option == "hiddenstop"){
								var percentage = document.getElementById("percentage");
								var triggerPrice = document.getElementById("triggerprice");
								percentage.disabled = false;
								triggerPrice.disabled = false;
								//triggerPrice.disabled = false;
							}else{
								var percentage = document.getElementById("percentage");
								var triggerPrice = document.getElementById("triggerprice");
								triggerPrice.value = "";
								triggerPrice.disabled = true;
								percentage.disabled = true;
								//triggerPrice.disabled = true;
							}
					});
					</script>
					
					
		<script type="text/javascript">
			var percentage = document.getElementById('percentage');
			percentage.addEventListener('keyup', function() {
				var trigger = document.getElementById('triggerprice');
				var percentage = document.getElementById('percentage');
				var sharePrice = document.getElementById('shareprice').value;
				
				if(percentage.value > 100){
					percentage.value = 100;
					
				}
				
				var triggerPrice = sharePrice - (sharePrice * (percentage.value / 100));
				trigger.value = triggerPrice.toFixed(2);
				console.log(trigger.value);
				
				
			});
			
			
		</script>
					
					<label class="form-label">Shares to Sell:</label>
					<input id="sharestosell" type="number"  min="1" max="<%=numShares%>" step="1" name="sharestosell" value=""><br>					
					
					<label class="form-label hidden">Zip Code:</label>
					<input id="save-button" type="submit" value="Sell Stock" name="editbutton">
					
						<script>
			var amountOfStocks = document.getElementById("sharestosell");
			amountOfStocks.addEventListener('keyup', function() {
				var amountOfStocks = document.getElementById("numshares");
				var totalShares = document.getElementById("sharestosell");
				console.log(amountOfStocks.value + " " + totalShares.value);
				
				if(Number(amountOfStocks.value) < Number(totalShares.value)) {
					totalShares.value = amountOfStocks.value;
				} 
			});
		</script>
					
					<script type="text/javascript">
					    document.getElementById('stocksymbol').readOnly = true;
					    document.getElementById('companyname').readOnly = true;
					    document.getElementById('stocktype').readOnly = true;
					    document.getElementById('shareprice').readOnly = true;
					    document.getElementById('numshares').readOnly = true;
					    document.getElementById('accountnumber').readOnly = true;
					    document.getElementById('triggerprice').readOnly = true;
					</script>
					
					
					<style type="text/css">
						#stocksymbol, #companyname, #stocktype, #shareprice, #numshares, #accountnumber {
							background-color: #EAEAEA;
						}
					</style>
				</form>
			</div>	
		</div>	
	</div>
</body>
</html>