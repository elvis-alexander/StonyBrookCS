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
        console.log('attempting ajax call');
        /* update chat box with user input */
        var human_input = $("#human").val();
        $('#chat_box').append('H: ' + human_input + '\n');
        // reset human input
        $('#human').val('');
        // scroll to bottom
        $('#chat_box').scrollTop($('#chat_box')[0].scrollHeight);

        /* ajax call here */
        var request = $.ajax({
            url: "/eliza/DOCTOR",
            type: "post",
            data: {"human" : human_input}
        });

        /* get responce from eliza */
        request.done(function(r, t, j) {
            $('#chat_box').append('E: ' + r['eliza'] + '\n');
        });

        /* log failure */
        request.fail(function () {
            console.log('ajax failure');
        });
    }
}







