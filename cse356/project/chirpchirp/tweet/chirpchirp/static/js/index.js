//toggles submission form to create user mode.
function createUserMode(){
  clearFields();
  $(".collapse-email").show();
  $(".addUser").hide();
  $("#submit-login").text("Create Account");
  $("#main-form").attr("action","/adduser");
}

//toggles submission form to log in user mode
function loginUserMode(){
  clearFields();
  $(".collapse-email").hide();
  $(".addUser").show();
  $("#submit-login").text("Log In");
  $("#main-form").attr("action","/login");
}


//toggles from verify mode to submission mode
function submissionMode(){
  console.log("setting up submission mode")
  clearFields();
  //shows the submission form(create user and login), hides the verify forms.
  $(".submission-container").show();
  $(".verify-account-container").hide();
  loginUserMode();
}


//toggles from submission mode to verify mode
function verifyUserMode(){
  console.log("setting up verification mode")
  clearFields();
  //hide the submission forms(create user and login), to show the verify forms
  $(".submission-container").hide();
  $(".verify-account-container").show();
}


//clear submission fields
function clearFields(){
  $("#messageDiv").text('');
  $("#usernameField").val('');
  $("#passwordField").val('');
  $("#emailField").val('');
  $("#messageDiv").hide();
  //clear verification fields
  $("#verifyEmailField").val('');
  $("#verifyKeyField").val('');
}

//on submit send form data. on response, either redirect or display server message.
function submitLoginHandler(){
    $("#submit-login").click(function(e){
      e.preventDefault();
      $.ajax({
  			type: "post",
  			url:$("#main-form").attr("action"),
        data:JSON.stringify({
          "username":$("#usernameField").val(),
          "password":$("#passwordField").val(),
          "email":$("#emailField").val()
        }),
  			timeout: 2000
  		}).done(function(data){
        clearFields();
        console.log("received from server:"+ JSON.stringify(data));
        //check if redirects to another page
        if(data.redirect && typeof(data.redirect)== "string"){
          //server responded a redirect of this page.
          window.location.replace(data.redirect);
        }else if(data.error && typeof(data.error) =='string'){
          // show message response from server
          $("#messageDiv").text(data.error);
          $("#messageDiv").show();
        }else if($("#main-form").attr("action") =="/adduser" && data.status== "OK"){
          //adding user to server db was successful. Allow user to log in.
          loginUserMode();
        }
  		});
    });
}

//on submit, send verify form data. On response, redirect, or show submission forms.
function verifyAccountHandler(){
  $("#submit-verify").click(function(e){
    e.preventDefault();
    $.ajax({
      type: "post",
      url:$("#verify-form").attr("action"),
      data:JSON.stringify({
        "email":$("#verifyEmailField").val(),
        "key":$("#verifyKeyField").val()
      }),
      timeout: 2000
    }).done(function(data){
      clearFields();
      console.log("received from server:"+ JSON.stringify(data));
      //check if redirects to another page
      if(data.redirect && typeof(data.redirect)== "string"){
        window.location.replace(data.redirect);
      }else if(data.error && typeof(data.error) =='string'){
        submissionMode();
        $("#messageDiv").text(data.error);
        $("#messageDiv").show();
      }else if(data.status== "OK"){
        submissionMode();
        $("#messageDiv").text("verified successfully, please log in");
        $("#messageDiv").show();
      }
    });
  });
}

$( document ).ready(function() {
  //default is submission mode
  $(".submission-container").show();
  // should not display, since no error messages to client upon loading
  $("#messageDiv").hide();
  //should be redirecting to user account creation rest endpoint
  $(".addUser").click(function(){
    createUserMode();
  });
  // should show forms to input activation key.
  $(".verifyUser").click(function(){
    verifyUserMode();
  });

  //assign login handler
  submitLoginHandler();
  verifyAccountHandler();

});
