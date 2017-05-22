// handler to logout client from session.
function logoutHandler(){
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


// create dom element to wrap around search results.
function createDomFollower(username){
  return '\
  <li>\
    <div>'+username+'</div>\
	</li>';
}


//renders the entire server response, loads entire users list.
function renderFollowList(data,followListDiv){
	console.log("rendering:"+followListDiv);
	$(followListDiv).html('');
	if(data.users){
		for(var i=0;i<data.users.length;i++){
			var user = data.users[i];
			console.log("follow user:"+JSON.stringify(user));
			$(followListDiv).append(createDomFollower(user));
		}
	}
}


//print the response object.
function handleServerMessages(data){
  if(data.redirect){
    window.location.replace(data.redirect);
  }else if(data.message){
    console.log("displaying server message");
    $("#serverMessage").html(data.message);
  }else if (data.error){
    console.log("displaying server error");
    $("#serverMessage").html(data.error);
  }
}


function getFollowersAjax(username){
	$.ajax({
		type: "post",
		url: "/user/"+username+"/followers",
		timeout: 2000
	}).done(function(data){
		console.log("received from server:"+JSON.stringify(data));
		handleServerMessages(data);
		renderFollowList(data,"#followersList");
	});
}

function getFollowingAjax(username){
	$.ajax({
		type: "post",
		url: "/user/"+username+"/following",
		timeout: 2000
	}).done(function(data){
		console.log("received from server:"+JSON.stringify(data));
		handleServerMessages(data);
		renderFollowList(data,"#followingList");
	});
}


$(document).ready(function(){
	logoutHandler();
  getFollowingAjax(username);
  getFollowersAjax(username);
});
