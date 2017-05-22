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
function createDomTweet(username, tweet,tweetId){
  return '\
  <li>\
	  <div class="wells col-md-2">\
		 		<p><strong>@' + username + '</strong></p>\
			</div>\
			<div class="wells col-md-8"><p>'
			 + tweet +
	  	'</p></div>\
			<div class="wells col-md-2"><p>'
			 + tweetId +
			'</p></div>\
	</li>';
}


//renders the entire server response, loads entire users list.
function renderTweetResults(data,tweetListDiv){
	$(tweetListDiv).html('');
	if(data.items){
		for(var i=0;i<data.items.length;i++){
			var tweet = data.items[i];
			console.log("tweet:"+JSON.stringify(tweet));
			$(tweetListDiv).append(createDomTweet(tweet.username,tweet.content,tweet.id));
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


function buildSearchObject(){
	var searchObject = {};
	if ($("#searchTimestamp").val()){
		searchObject["timestamp"] = $("#searchTimestamp").val();
	}
	if ($("#searchLimit").val()){
		searchObject["limit"] = $("#searchLimit").val()
	}
	if ($("#searchQuery").val()){
		searchObject["q"] = $("#searchQuery").val()
	}
	if ($("#searchUsername").val()){
		searchObject["username"] = $("#searchUsername").val()
	}
	if ($("#searchFollowing").val()){
		searchObject["following"] = $("#searchFollowing").val()
	}
	return searchObject
}



//NOTE: div with id followButton is only available when a user profile search returns the DOM element
function searchTweetHandler(){
  $("#tweetsearchbutton").click(function(e){
    e.preventDefault();
		var searchObject = JSON.stringify(buildSearchObject());
    console.log("Searching Tweets: " + searchObject);
		searchTweetAjax(searchObject)
  });
}


function searchTweetAjax(searchObject){
	$.ajax({
		type: "post",
		url: "/search",
		data: searchObject,
		timeout: 2000
	}).done(function(data){
		console.log("received from server:"+JSON.stringify(data));
		handleServerMessages(data);
		$("#resultsHeader").show();
		renderTweetResults(data,"#tweetResults");
	});
}

$(document).ready(function(){
	logoutHandler();
  searchTweetHandler();
	$("#resultsHeader").hide();
});
