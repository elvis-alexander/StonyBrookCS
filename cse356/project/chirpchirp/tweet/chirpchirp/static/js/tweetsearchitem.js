var searchItemTweet;

function searchItemHandler(){
  $("#searchItem").click(function(e){
    e.preventDefault();
    var tweetId = $("#searchItemField").val();
    console.log("getting /item: "+ tweetId);
    $.ajax({
      type: "get",
      url: "/item/"+tweetId,
      timeout: 2000
    }).done(function(data){
      console.log("received from server:"+JSON.stringify(data));
			handleServerMessages(data)
      renderTweetItem(data.item.username, data.item.content);
      searchItemTweet=tweetId;
      deleteTweetItemHandler();
    });
  });
}

function deleteTweetItemHandler(){
  $(document).on('click','#deleteTweetItem',function(event){
    event.preventDefault();
    console.log("deleting /item: "+ searchItemTweet);
    $.ajax({
      type: "delete",
      url: "/item/"+searchItemTweet,
      timeout: 2000
    }).done(function(data){
      console.log("received from server:"+JSON.stringify(data));
      handleServerMessages(data)
      searchItemTweet="";
      $("#itemResult").html(''); // clear previous list elements
    });

  });
}

// get a single tweet by id and render it to itemResult div.
function renderTweetItem(username,tweet){
  $("#itemResult").html(''); // clear previous list elements
  var listElement = createTweetItemDomContainer(username,tweet,false);
  $("#itemResult").append(listElement);
}

function createTweetItemDomContainer(user,tweet,wellOption){
  var well = "";
  if(wellOption){
    well="well";
  }
  return '\
  <div class="row">\
    <div class="col-sm-2">\
      <div class="'+well+'">\
       <p><strong>@'+ user+'<strong></p>\
      </div>\
    </div>\
    <div class="col-sm-9">\
      <div class="'+well+'">\
        <p>'+ tweet + '</p>\
      </div>\
    </div>\
    <div class="col-sm-1">\
      <button id="deleteTweetItem">\
        <span class="glyphicon glyphicon-remove"></span>\
      </button>\
    </div>\
  </div>';
}

$(document).ready(function(){
	searchItemHandler();
});
