/*
 *NOTE: WARNING THAT THIS FILE IS HIGHLY DEPENDENT AND COUPLED WITH :
 * UPLOADFORM.JS
 * THIS AND UPLOADFORM.JS CAN BE MERGED INTO ONE FILE, BUT FOR READABILITY
 * IS SPLIT UP. MAKE SURE BOTH ARE INCLUDED INTO THE HTML AS SCRIPTS.
 * THE TWO ARE DEPENDENT ON THE SHARED GLOBAL VARIABLE mediaArray
 */

/****************************************/
/*    GLOBAL VARIABLES                  */
/****************************************/

//check if the global mediaArray is defined, if not define it.
// temporary holds media Ids until it can be linked to a tweet item.
if(typeof(mediaArray)=="undefined"){
  window.mediaArray = [];
}

//check if the global tweetMap is defined, if not define it.
// contains a list of retrieved tweets, waiting to be rendered.
// render after all media binary blobs are retrieved, if media ids present.
if(typeof(tweetMap)=="undefined"){
  window.tweetMap = {};
}

/****************************************/
/*    GETTERS/SETTERS                  */
/****************************************/
// retrieved from queue to render the tweet
function getTweetMapItem(tweetId){
  return tweetMap[tweetId];
}


//get contents of global array of media that has been uploaded.
// after posting the media and linking it with a tweet, clear that array
function getMediaArrayIds(){
  var mediaIds = [];
  //declare here to avoid array length mutation side effects
  var mediaArrayItemCount = mediaArray.length;
  for(var i=0 ; i < mediaArrayItemCount; i++){
    mediaIds.push(mediaArray.shift());
    console.log("pushed mediaId : " + mediaIds.slice(-1)[0] );
  }
  console.log(" getMediaArrayIds() attaching media ids to tweet, mediaArray should now be empty" + JSON.stringify(mediaArray));
  return mediaIds;
}

// insert tweet item into map to render when all data is retrieved.
function insertTweetMap(tweetId,auth,content,mediaMap){
  tweetMap[tweetId] ={
    "auth" : auth,
    "content" : content,
    "mediaMap" : mediaMap,
    "id" : tweetId
  }
  return tweetMap[tweetId];
}

// deletes the tweetMapItem from the tweetMap
// happens after the tweetMapItem is rendered.
function deleteTweetMapItem(tweetMapItemId){
  delete tweetMap[tweetMapItemId];
}


// takes a server response object, and adds fields into tweetMap
// for later rendering
function insertResponseTweetMap(response){
  var id = response.item.id;
  var username = response.item.username;
  var content =  response.item.content;
  var mediaList =  response.item.media;
  //create map of media id's and ready-to-be filled content, from mediaList
  var mediaMap = {};
  for(var i=0; i< mediaList.length; i++){
    var mediaId = mediaList[i];
    console.log("insertResponseTweetMap(): adding media id to mediaMap: " + mediaId);
    mediaMap[mediaId] = {};
    console.log("insertResponseTweetMap(): mediaMap: " + JSON.stringify(mediaMap));
  }
  return insertTweetMap(id,username,content,mediaMap);
}

/***********************************/
/*    RENDER FUNCTIONS             */
/***********************************/
// render a tweetMapItem, determining whether to use media or text-only functions.
function renderTweetMapItem(tweetMapItem){
  // if map is empty, just render the tweet.
  if(Object.keys(tweetMapItem.mediaMap).length == 0){
    renderTweetTextOnly(tweetMapItem);
  }else{
    //render the tweet with media
    renderTweetWithMedia(tweetMapItem);
  }
}

// Just render the tweet, without media content.
function renderTweetTextOnly(tweetMapItem){
  renderTweetFeedList(tweetMapItem.auth, tweetMapItem.content);
  delete tweetMap[tweetMapItem.id];
  console.log("finished rendering tweet without any media, tweetMap:" + JSON.stringify(tweetMap));
  return;
}

// render the tweet, including the media content
function renderTweetWithMedia(tweetMapItem){
  console.log("renderTweetWithMedia() rendering tweetMapItem:" + JSON.stringify(tweetMapItem));
  renderTweetFeedList(tweetMapItem.auth, tweetMapItem.content,tweetMapItem.id);
  renderMediaContentsOfTweet(tweetMapItem.mediaMap,tweetMapItem.id);
  //remove the rendered tweetMapItem from global variable
  delete tweetMap[tweetMapItem.id];
  console.log("renderTweetWithMedia() finished rendering and removing tweetMapItem :" + tweetMapItem.id + "\ntweetMap:" + JSON.stringify(tweetMap));
}

function renderMediaContentsOfTweet(mediaMap,tweetId){
  for(var mediaId in mediaMap){
    console.log('renderMediaContentsOfTweet() rendering : ' + mediaId);
    //create dom elements first for regular tweet.
    var selectorString = "div.tweetFeedItemContent[tweetId='"+ tweetId + "']"
    var imageDom = createTweetImageDom(mediaId);
    $(selectorString).append(imageDom);
  }
}


//render the view of tweets in tweet feed list
function renderTweetFeedList(username, tweet, tweetId, mediaId){
  var listElement = createTweetDomContainer(username,tweet,true, tweetId, mediaId);
  $("#tweetFeedList").append(listElement);
}

// get a single tweet by id and render it to itemResult div.
function renderTweetItem(username,tweet){
  $("#itemResult").html(''); // clear previous list elements
  var listElement = createTweetDomContainer(username,tweet,false);
  $("#itemResult").append(listElement);
}



/**************************************/
/*  DOM MANIPULATION                  */
/************************************/
// creates a dom string for an image div for a media tweet.
// stores identifier in the div target attribute
function createTweetImageDom(mediaId){
  return '<img class="tweetImage" src="/media/' + mediaId + '" target="' +mediaId+'"/>'
}

//creates a tweet post in the tweet feed section.
function createTweetDomContainer(user,tweet,wellOption,tweetId,mediaId){
  // safe-check for undefined params.
  if(!mediaId){
    mediaId = "";
  }
  if(!tweetId){
    tweetId = "";
  }
  // check whether to encase comment in wells from bootstrap
  var well = "";
  if(wellOption){
    well="well";
  }
  //create and return dom string.
  return '\
  <div class="row tweetFeedItemContainer" tweetId="'+ tweetId + '">\
    <div class="col-sm-3">\
      <div class="'+well+' tweetFeedItemAuthor">\
       <p><strong>@'+username+'<strong></p>\
      </div>\
    </div>\
    <div class="col-sm-9">\
      <div class="'+well+' tweetFeedItemContent" tweetId="' + tweetId +'">\
        <p>'+ tweet + '</p>\
      </div>\
    </div>\
  </div>';
}


/**************************************/
/*  AJAX FUNCTIONS                    */
/*************************************/
//send tweet to the server, on response update the tweet feed view.
function tweetAjaxPost(tweet){
  console.log("posting tweet: " + tweet + " by :" + username);
  var postdata = JSON.stringify({
    'content': tweet,
    'media' : getMediaArrayIds()
  });
  console.log("sending this in posted body :"+JSON.stringify(postdata));
  $.ajax({
    type: "post",
    url: "/additem",
    data: postdata,
    timeout: 2000
  }).done(function(response){
    console.log("received from server:" +JSON.stringify(response));
    clearPreviewContainer("#preview-image-container"); //clear <img>. method defined in uploadForm.js
    //request tweet data that we just posted
    if(response.id){
      ajaxRetrieveTweet(response.id);
    }
  });
}


// GET a tweet from endpoint /item/id
function ajaxRetrieveTweet(id){
  console.log("GET retrieving tweet: " + id);
  $.ajax({
    type: "get",
    url: "/item/"+id , //get the specific tweet
    timeout: 2000
  }).done(function(response){
    console.log("received from server:" +JSON.stringify(response));
    if(response.status != "OK"){
      alert("error retrieving tweet Id:" + id);
      return
    }
    //add as an item into the tweetMap for rendering later.
    var tweetMapItem = insertResponseTweetMap(response);
    renderTweetMapItem(tweetMapItem);
  });
}



/******************************************/
/*  EVENT HANDLERS                       */
/****************************************/
//Handler to post a tweet
function postTweetHandler(){
	$('#postTweetButton').on('click', function(e){
		e.preventDefault();
		var tweetContent = $('#tweetText').val();
    tweetAjaxPost(tweetContent)
    $('#tweetText').val(""); //clear
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
        alert("encountered server error");
        return;
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
