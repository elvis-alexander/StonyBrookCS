var express = require('express');
var app = express();
var fs = require('fs');
var bodyParser = require('body-parser');
var dateFormat = require('dateformat');

/* eliza response */
var input_output_json = {
    'thank you (.*)' : ['I am glad I can help', 'I am happy I can help'],
    'thank you' : ['I am glad to be of your service'],
    'what is your name(.*)' : ['My name is Eliza', 'Eliza'],
    'who created you (.*)' : ['Joseph Weizenbaum'],
    'who made you (.*)' : ['Joseph Weizenbaum'],
    'what are you doing(.*)' : ['I am talking with you'],
    'when were you born(.*)' : ['I was born between 1964 to 1966'],
    'how old are you(.*)' : ['I am in my forties'],
    'no(.*)'  : ['OK, I didn\'t think so.'],
    'yes(.*)' : ['Are you sure?', 'Ok, I agree with you'],
    'well(.*)' :['You were saying...'],
    'why(.*)' : ['Well, first tell me what you think.'],
    'what(.*)' : ['What do you think?', 'Good question. Anything else I can do for you'],
    'where am i(.*)' : ['In New York', 'In the United States'],
    'where (.*)' : ['It should be nearby', 'Look around', 'What do you think'],
    'when(.*)' : ['When, indeed?', 'Perhaps in the near future'],
    'talking (.*) you (.*)' : ['Yes you are', 'Yes tell me more'],
    'ok(.*)' : ['Tell me more', 'I see'],
    'is there (.*)' : ['Do you think so?'],
    'i feel (.*)' : ['Do often feel that way?'],
    'i want (.*)' : ['Why?'],
    'i am (.*)' : ['Is that what you want to be doing?'],
    'i think (.*)' : ['I agree with you', 'I don\'t agree with what you are saying', 'Sounds interesting'],
    'i (.*)' : ['Tell me more', 'I understand'],
    'hello(.*)' : ['Hi, how are you?', 'Hi'],
    'hi(.*)' : ['Hi, what are you doing?', 'Greetings!'],
    'hey(.*)' : ['Hey'],
    '.*' : 
            [   'Why do you say that?', 
                'How do you feel when you say that?',
                'Very Interesting',
                'Do you feel strongly about that?',
                'Hmm I understand',
                'Is everything ok?',
                'That makes sense, tell me more',
                'Ok... anything else you want to add?',
            ]
};

/* getter for eliza response */
function get_eliza_response(human) {
    human = human.replace("'", '').replace(",", '').toLowerCase();
    for(var key in input_output_json) {
        var re = new RegExp(key);
        if(re.test(human)) {
            var len = input_output_json[key].length;
            return input_output_json[key][Math.floor(Math.random() * len)];
        }
    }
}


/* used to retrieve post parameters */
app.use(bodyParser.urlencoded({
  extended: true
}));
app.use(bodyParser.json());
app.use(express.static(__dirname + '/public'));

function show_name_form(res) {
	fs.readFile('public/name.html', function(err, data) {
		res.writeHead(200, {
			'Content-Type' : 'text/html',
			'Content-Length' : data.length
		});
		res.write(data);
		res.end();
	});
}


/* routing handling */
/* show name form on get request */
app.get('/eliza', function(req, res) {
	show_name_form(res);
});

app.post('/eliza', function(req, res) {
	// console.log(req.body.name);
	fs.readFile('public/doctor.html', function(err, data) {
		res.writeHead(200, {
			'Content-Type' : 'text/html'
		});
		var now = new Date();
		dateFormat(now, "dddd, mmmm dS, yyyy, h:MM:ss TT");
		res.write("<h4>Hello " + req.body.name + ' ' + now + "</h4>");
		res.write(data);
		res.end();
	});
});

/* 
	input: {"human" : "input"}
	output: {"eliza" : "output"}
 */
app.post('/eliza/DOCTOR', function(req, res) {
	var human = req.body.human;
	console.log('human input from server: ' + human);
	res.setHeader('Content-Type', 'application/json');
	res.send(JSON.stringify({"eliza" : get_eliza_response(human)}));
	res.end();
});

/* server */
var server = app.listen(8000, function() {
	var host = server.address().address;
	var port = server.address().port;
	console.log("Example app listening at http://%s:%s", host, port);
});





