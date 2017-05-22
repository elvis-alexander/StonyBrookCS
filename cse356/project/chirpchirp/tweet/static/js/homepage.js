function logoutHandler(){
	// handler to logout client from session.
	$("#logoutButton").click(function(){
		console.log("logging out");
		$.ajax({
			type: "post",
			url: "/logout",
			timeout: 2000
		}).done(function(data){
			console.log("received from server:"+JSON.stringify(data))
			window.location.replace(data.redirect);
		});
	});
}


$(document).ready(function(){
	logoutHandler();
});
