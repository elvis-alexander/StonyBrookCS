<!-- AFTER CLICKED ON STOCK -->

<!DOCTYPE html>
<html lang="en">
<head>
<%@page import="settings.Quote"%>
<%@page import="settings.Logout"%>

    <title>Buy Stock</title>
    <meta charset="utf-8">
    <link rel='shortcut icon' href='res/favicon.ico' type='image/x-icon'>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="style/customerhomepage.css">
   	<link rel="stylesheet" href="style/employeetransaction.css">
    <link href='https://fonts.googleapis.com/css?family=Patua+One' rel='stylesheet' type='text/css'>
</head>
<body onload="onLoadBody()">

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
			String stockSymbol = (String) request.getAttribute("stocksymbol");
			String type = (String) request.getAttribute("type");
			String numShares = (String) request.getAttribute("numshares");
			String sharePrice = (String) request.getAttribute("shareprice");
			String companyName = (String) request.getAttribute("companyname");
			String customerFirstName = (String) request.getAttribute("customerfirstname");
			String customerLastName = (String) request.getAttribute("customerlastname");
			String customerID = (String) request.getAttribute("customerid");
		%>
	
	<div id="main-container">
		<div id="heading-container">
			<form id='stock-search'action="PlaceOrderEmployee" method="post">
				<input type="hidden" name="firstname" value="<%=customerFirstName%>"><br>
				<input type="hidden" name="lastname" value="<%=customerLastName%>"><br>
				<input type="hidden" name="id" value="<%=customerID%>"><br>
	        	<a href="javascript:{}" onclick="document.getElementById('stock-search').submit();">Back</a>
	        </form>
	 		<h1>Place Order for customer: <%=customerFirstName %> <%=customerLastName %></h1><hr id="title-line">	 	 
		</div>
		
		<style>
			#stocksymbol {
			    background: #EAEAEA;
			}
			#companyname {
			    background: #EAEAEA;
			}
			#stocktype {
			    background: #EAEAEA;
			}
			#numshares {
			    background: #EAEAEA;
			}
			#shareprice {
			    background: #EAEAEA;
			}
		</style>

		<div id="customer-form-container">
			<div class="col">
				<form  class="customer-form" action="PlaceOrderEmployeeBuy" method="post">
					<label class="form-label">Stock Symbol:</label>
					<input type="text" name="stocksymbol" value="<%=stockSymbol%>" id="stocksymbol" ><br>
					
					<label class="form-label">Company Name:</label>
					<input type="text" name="companyname" value="<%=companyName%>" id="companyname" ><br>
					
					<label class="form-label">Industry:</label>
					<input type="text" name="stocktype" value="<%=type%>" id="stocktype" ><br>
					
					<label class="form-label">Total Shares:</label>
					<input type="text" name="numshares" value="<%=numShares%>" id="numshares" ><br>
					
					<label class="form-label">Share Price:</label>
					<input type="text" name="shareprice" value="<%=sharePrice%>" id="shareprice" ><br>
					
					<label class="form-label">Amount:</label>
					<input id="amount" type="number" min="1" max="<%=numShares%>" name="amountshares" value=""><br>

					<label class="form-label">Percentage:</label>
					<input id="percentage" type="number" min="1" max="100" step="1" name="percentage" value="" disabled><br>
					
					<label class="form-label">Trigger Price:</label>
					<input id="triggerprice" type="number" min="1" step="1" name="triggerprice" value=""><br>

					<!-- Price type drop down -->
					<label class="form-label">Price Type:</label>
					<select id="type-selection-menu-drop-operator-menu-option"  name="option">
					    <option value="market">Market</option>
					    <option value="marketonclose">Market on Close</option>
					    <option value="trailingstop">Trailing Stop</option>
					    <option value="hiddenstop">Hidden Stop</option>
				  	</select><br>
				  	
				  	<label class="form-label">Account:</label>
					<select id=""  name="account_number">
				  	<!-- Account drop down -->
				  	<% 
				  	// utilities
					String jdbc_driver= "com.mysql.jdbc.Driver";  
					String url = "jdbc:mysql://mysql2.cs.stonybrook.edu:3306/blanunez";
			   		String user = "blanunez";
			   		String pass = "109162285";
			   		java.sql.Connection conn = null;
				   	java.sql.Statement stmt = null;
				   	// querying
				   	try {
			  			Class.forName(jdbc_driver).newInstance();
						conn = java.sql.DriverManager.getConnection(url, user, pass);
						stmt = conn.createStatement();
				  		// GET ALL ACCOUNTS CORRESPONDING TO CURRENT CUSTOMER
				  		String id = (String) request.getAttribute("customerid");
						java.sql.ResultSet result = stmt.executeQuery("SELECT * FROM account_ WHERE ClientID = " + id + ";");
						while(result.next()){
							StringBuilder sb = new StringBuilder("");
							String accountNumber = result.getString("AccountNumber");
							sb.append("<option value= \"" + accountNumber + "\">" + accountNumber + "</option>");
							out.println(sb);
						}
						
						conn.close();
				   	} catch(Exception e) {
				   		System.out.println("ERROR");
				   		e.printStackTrace();
				   	}
					%>
				  	</select><br>
				  	<!-- Formatting -->
					<label class="form-label hidden">Zip Code:</label>
					<input id="save-button" type="submit" value="Buy Stock" name="employeetrigger">

				</form>
			</div>	
		</div>	
		<script type="text/javascript">
			function onLoadBody() {		
				document.getElementById('stocksymbol').readOnly = true;
				document.getElementById('companyname').readOnly = true;
				document.getElementById('stocktype').readOnly = true;			
				document.getElementById('numshares').readOnly = true;			
				document.getElementById('shareprice').readOnly = true;	
				document.getElementById('triggerprice').readOnly = true;	
			}
		</script>
		
		<script>
			var amountOfStocks = document.getElementById("amount");
			amountOfStocks.addEventListener('keyup', function() {
				var amountOfStocks = document.getElementById("amount");
				var totalShares = document.getElementById("numshares");
				
				if(Number(amountOfStocks.value) > Number(totalShares.value)) {
					amountOfStocks.value = totalShares.value;
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

	</div>	
</body>