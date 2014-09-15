/**
*	Node proxy for Storm API
*
*	This proxy is made to avoid cross-domain problems.
*	The proxy's config is set up by calling the '/connect' with a stormAPIUrl and a stormAPIPort parameters.
*	Every call made to this server will be redirected to the Storm API specified in the config.
*	If the request is correct, the response is first retrieved from the Storm API, then sent to the client.
*	If it fails, it will send a 500 error.
*
*/

var config = {
};

var express = require('express');
var http = require('http');
var app = express();

app.use(express.static(__dirname + '/public'));

app.get('/connect', function(req, res, next) {
	var URL = req.query.stormAPIUrl;
	var port = req.query.stormAPIPort;
	
	http.get(URL + ':' + port + '/api/cluster/summary', function(response) {
		if (response.statusCode ===  200) {
			config.stormAPIUrl = URL.replace(/http:\/\//, '');
			config.stormAPIPort = port;
			res.status(200).send('success');
		}
	}).on('error', function(e) {
		res.status(500).send('error');
	});
});

app.use('/api', function (req, res, next) {
	var options = {
	
		host : config.stormAPIUrl,
		path : '/api' + req.path,
		port : config.stormAPIPort,
		method : req.method,
		data: req.data
	};
	var request = http.request(options, function(response){
		var body = "";
		response.on('data', function(data) {
		  body += data;
		});
		response.on('end', function() {
			try{
				var content = JSON.parse(body);
				res.send(content);
			} catch (err) {
				console.error(err.stack);
				res.status(500).end('Something broke!');
			}
		});
	});
	request.on('error', function(err) {
		console.error(err.stack);;
	});
	request.end();
});

var server = app.listen(8746, function() {
    console.log('Listening on port %d', server.address().port);
});