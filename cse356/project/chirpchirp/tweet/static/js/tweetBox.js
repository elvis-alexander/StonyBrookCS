//send tweet to the server, on response update the tweet feed view.
function tweetAjaxPost(tweet){
  console.log("posting tweet: " + tweet + " by :" + username);
  $.ajax({
    type: "post",
    url: "/additem",
    data: JSON.stringify({'content': tweet}),
    timeout: 2000
  }).done(function(response){
    console.log("received from server:" +JSON.stringify(response))
    renderTweetFeedList(username,tweet)
  });
}

function createTweetDomContainer(user,tweet,wellOption){
  var well = "";
  if(wellOption){
    well="well";
  }
  return '\
  <div class="row">\
    <div class="col-sm-3">\
      <div class="'+well+'">\
       <p><strong>@'+username+'<strong></p>\
      </div>\
    </div>\
    <div class="col-sm-9">\
      <div class="'+well+'">\
        <p>'+ tweet + '</p>\
      </div>\
    </div>\
  </div>';
}

//render the view of tweets in tweet feed list
function renderTweetFeedList(username, tweet){
  var listElement = createTweetDomContainer(username,tweet,true);
  $("#tweetFeedList").append(listElement);
}

// get a single tweet by id and render it to itemResult div.
function renderTweetItem(username,tweet){
  $("#itemResult").html(''); // clear previous list elements
  var listElement = createTweetDomContainer(username,tweet,false);
  $("#itemResult").append(listElement);
}

//Handler to post a tweet
function postTweetHandler(){
	$('#postTweetButton').on('click', function(e){
		e.preventDefault();
		var tweetContent = $('#tweetText').val();
    tweetAjaxPost(tweetContent)
	});
}

function searchFieldHandler(){
	$("#searchFieldSubmit").click(function(e){
		e.preventDefault();
		var search = $("#searchField").val();
		console.log("searching: "+ search);
		$.ajax({
			type: "post",
			url: "/search",
			data: JSON.stringify({
				"timestamp": search,
				"limit":25
			}),
			timeout: 2000
		}).done(function(data){
			console.log("received from server:"+JSON.stringify(data));
      if(data.status !="OK"){
        console.log("encountered server error");
        return
      }

      for(var i=0;i<data.items.length;i++){
        renderTweetFeedList(data.items[i].username, data.items[i].content);
      }
		});
	});
}


$(document).ready(function(){
  // bind handlers
  postTweetHandler();
});
