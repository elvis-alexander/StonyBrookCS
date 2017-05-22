/*
 *NOTE: WARNING THAT THIS FILE IS HIGHLY DEPENDENT AND COUPLED WITH :
 * TWEETBOX.JS
 * THIS AND UPLOADFORM.JS CAN BE MERGED INTO ONE FILE, BUT FOR READABILITY
 * IS SPLIT UP. MAKE SURE BOTH ARE INCLUDED INTO THE HTML AS SCRIPTS.
 * THE TWO ARE DEPENDENT ON THE SHARED GLOBAL VARIABLE mediaArray
 */

/***********************************/
/*  GLOBAL VARIABLES
/***********************************/

//check if the global mediaArray is defined, if not define it.
if(typeof(mediaArray)=="undefined"){
  window.mediaArray = [];
}


//finds the matching media id from global array and removes it.
function removeFromMediaArray(id){
  for(var i=0;i<mediaArray.length;i++){
    if(id==mediaArray[i]){
      mediaArray.splice(i,1); //removes one element at position,i.e. target media ID
      break;
    }
  }
}


/***********************************/
/*  IMAGE PREVIEW FUNCTIONS
/***********************************/
//add an img div into the container area
function addPreview(mediaId){
  //create an image preview with associated delete button.
  // Both image and button identified by the target attribute.
  var imageDiv = createImagePreviewDom(mediaId);
  //add event handler to newly created dom button.
  deleteUploadHandler(mediaId);
  //add to the preview section, and show it.
  $("#preview-image-container").append(imageDiv);
  $("#preview-image-container").show();
  return mediaId;
}

//clear image div from container area.
function clearPreviewContainer(previewImageContainerDiv){
  $(previewImageContainerDiv).html('');
  $(previewImageContainerDiv).hide();
}

// deletes the img preview with target=mediaId
function deletePreviewImg(mediaId){
  $("img.preview-image[target='" + mediaId +"']").remove();
}




/***********************************/
/*  AJAX
/***********************************/

//sends the file input form to server, and on success, uses the idea to
// perform GET request to show preview of uploaded data.
function ajaxSendFileInput(formdata){
  $.ajax({
      url: '/addmedia',
      data: formdata,
      cache: false,
      contentType: false,
      processData: false,
      type: 'POST',
      success: function(data){
          if(data.id){
            //store the media id as part of the global mediaArray, for tweet posts attachments.
            mediaArray.push(data.id);
            //create a new img div for previewing the data, keep track of media id by target attr.
            addPreview(data.id);
            console.log("updated global mediaArray:" + JSON.stringify(mediaArray));
          }
          alert(JSON.stringify(data));
      }
  });
}


/***********************************/
/*  DOM CREATION
/***********************************/
function createImagePreviewDom(mediaId){
  return '<img class="preview-image" src="/media/'
  + mediaId + '" target="'+ mediaId+'"/>\
    <button class="delete-preview-image" target="'+ mediaId +'">\
      <span class="glyphicon glyphicon-remove-sign"></span>\
    </button>';
}


/***********************************/
/*  EVENT HANDLERS
/***********************************/
//handles the upload of a media file, posts to the server
// and renders preview on ajax callback
function uploadInputHandler(uploadInputDiv){
  $(uploadInputDiv).change(function(){
      alert("input received an image upload");
      //create a FormData object to hold the media file data.
      var formData = new FormData();
      formData.append("content",this.files[0]);
      //send the file in the input div, and clear its value.
      ajaxSendFileInput(formData);
      $("input[type='file']").val('');
  });
}


//bind buttons with target=mediaId with deletion event handling.
// happens before mediaIds are binded to a tweet.
function deleteUploadHandler(mediaId){
  $("button[target='" + mediaId +"']").on('click',function(){
    // remove from global array where media Ids are binded to next tweet.
    removeFromMediaArray(mediaId);
    // remove the preview img of this media.
    deletePreviewImg(mediaId);
  });
}



$(document).ready(function(){
  //bind div to detect file uploads
  uploadInputHandler("#upload-photo");
  //Make sure the div is reset and cleared
  clearPreviewContainer("#preview-image-container");
});
