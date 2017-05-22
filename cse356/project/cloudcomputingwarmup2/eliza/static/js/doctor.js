/**
 * Created by elvis on 1/30/17.
 */
$(document).ready(function(){
    /* nice local configurations */
    $('#chat_box').append('E: Welcome to Eliza\n');
    $('#human').focus();
});

function talk_to_eliza(event) {
    if(event.keyCode == 13 || event.which == 13) {
        /* update chat box with user input */
        var human_input = $("#human").val();
        $('#chat_box').append('H: ' + human_input + '\n');
        // reset human input
        $('#human').val('');
        // scroll to bottom
        $('#chat_box').scrollTop($('#chat_box')[0].scrollHeight);
        /* send to server */
        var request = $.ajax({
            url: "/eliza/DOCTOR",
            type: "POST",
            data: {"human":human_input},
            // handle a successful response
            success : function(json) {
                $('#chat_box').append('E: ' + json['eliza'] + '\n');
                console.log("success");
            },
            // handle a non-successful response
            error : function(xhr,errmsg,err) {
                console.log(errMsg);
                console.log(xhr.status + ": " + xhr.responseText);
            }
        });
    }
}