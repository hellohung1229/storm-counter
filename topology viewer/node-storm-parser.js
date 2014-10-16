/**
*	Node parser for the Storm UI
*
*	This parser is made to replace the Storm API, which is uncompleted and not working.
*	The parser's config is set up by calling the '/checkUrl' with a Storm UI URL as a query parameter.
*	The parser is called with the same URLs as the Storm UI, except for the "component" page, which is here splitted between "spouts" and "bolts".
*	The responses are all given in Json.
*/

var express = require('express');
var app = express();
var request = require('request');
var cheerio = require('cheerio');

// MIDDLEWARES
app.use(function (req, res, next) {
	res.header('Access-Control-Allow-Origin', 'null');
	next();
});

app.route('/checkUrl')
	.get(function(req, res, next) {
		var url = req.query.url;
		if (url) {
			request(url, function(err, response, body) {
				if (err) {
					res.status(500).json({error:'bad url'});
				} else{
					parser.config.url = url;
					res.status(200).json({success:true});
				}
			});
		} else {
			res.status(500).json({error:'missing url'});
		}
	});

app.route('/topologies')
	.get(function(req, res, next) {
		parser.getTopologies(res);
	});

app.route('/topology/:topologyId')
	.get(function(req, res, next) {
		parser.getComponents(req.topologyId, res);
	});

app.route('/topology/:topologyId/bolt/:componentName')
	.get(function(req, res, next) {
		parser.getBolt(req.topologyId, req.componentName, res);
	});

app.route('/topology/:topologyId/spout/:componentName')
	.get(function(req, res, next) {
		parser.getSpout(req.topologyId, req.componentName, res);
	});

app.param('topologyId', function(req, res, next, id) {
		req.topologyId = id;
		next();
	});

app.param('componentName', function(req, res, next, name) {
		req.componentName = name;
		next();
	});

var parser = {
	config: {},
	uris: {
		topologies: function() {return '/';},
		topology: function(topologyId){return '/topology/' + topologyId;},
		component: function(topologyId, componentName) {return parser.uris.topology(topologyId) + '/component/' + componentName;}
	},
	loadPage: function(url, callback) {
		request(url, function(err, response, body) {
			if (err) {
				response.status(500).json({error: err});
			} else {
				callback(body);
			}
		});
	},
	getTopologies: function(res) {
		var onPageLoaded = function(body) {
			var topologies = [];
			var $ = cheerio.load(body);
			var topologiesTable = $('table').eq(1);
			topologiesTable.find('tbody>tr').each(function(index, item) {
				topologies.push({
						id: $(item).find('td').eq(1).text(),
						name: $(item).find('a').text(),
						status: $(item).find('td').eq(2).text(),
						uptime: $(item).find('td').eq(3).text(),
						num_workers: $(item).find('td').eq(4).text(),
						num_executors: $(item).find('td').eq(5).text(),
						num_tasks: $(item).find('td').eq(6).text()
					});
			});
			res.status(200).json(topologies);
		};
		parser.loadPage(parser.config.url + parser.uris.topologies(), onPageLoaded);
	},
	getComponents: function(topologyId, res) {
		var onPageLoaded = function(body) {
			var spouts = [];
			var bolts = [];
			var $ = cheerio.load(body);
			var spoutsTable = $('table').eq(2);
			var boltsTable = $('table').eq(3);
			spoutsTable.find('tbody>tr').each(function(index, item) {
				spouts.push({
						name: $(item).find('a').text(),
						executors: $(item).find('td').eq(1).text(),
						tasks: $(item).find('td').eq(2).text(),
						emitted: $(item).find('td').eq(3).text(),
						transferred: $(item).find('td').eq(4).text(),
						complete_latency: $(item).find('td').eq(5).text(),
						acked: $(item).find('td').eq(6).text(),
						failed: $(item).find('td').eq(7).text(),
						last_error: $(item).find('td').eq(8).text()
					});
			});
			boltsTable.find('tbody>tr').each(function(index, item) {
				bolts.push({
						name: $(item).find('a').text(),
						executors: $(item).find('td').eq(1).text(),
						tasks: $(item).find('td').eq(2).text(),
						emitted: $(item).find('td').eq(3).text(),
						transferred: $(item).find('td').eq(4).text(),
						capacity: $(item).find('td').eq(5).text(),
						execute_latency: $(item).find('td').eq(6).text(),
						executed: $(item).find('td').eq(7).text(),
						process_latency: $(item).find('td').eq(8).text(),
						acked: $(item).find('td').eq(9).text(),
						failed: $(item).find('td').eq(10).text(),
						last_error: $(item).find('td').eq(11).text()
					});
			});
			res.status(200).json({spouts:spouts, bolts:bolts});
		};
		parser.loadPage(parser.config.url + parser.uris.topology(topologyId), onPageLoaded);
	},
	getSpout: function(topologyId, componentName, res) {
		var onPageLoaded = function(body) {
			var outputStats = [];
			var executors = [];
			var $ = cheerio.load(body);
			var outputStatsTable = $('table').eq(2);
			var executorsTable = $('table').eq(3);
			outputStatsTable.find('tbody>tr').each(function(index, item) {
				outputStats.push({
						stream: $(item).find('td').eq(0).text(),
						emitted: $(item).find('td').eq(1).text(),
						transferred: $(item).find('td').eq(2).text()
					});
			});
			executorsTable.find('tbody>tr').each(function(index, item) {
				executors.push({
						id: $(item).find('td').eq(0).text(),
						uptime: $(item).find('td').eq(1).text(),
						host: $(item).find('td').eq(2).text(),
						port: $(item).find('a').text(),
						emmited: $(item).find('td').eq(4).text(),
						transferred: $(item).find('td').eq(5).text(),
						capacity: $(item).find('td').eq(6).text(),
						execute_latency: $(item).find('td').eq(7).text(),
						executed: $(item).find('td').eq(8).text(),
						process_latency: $(item).find('td').eq(9).text(),
						acked: $(item).find('td').eq(10).text(),
						failed: $(item).find('td').eq(11).text()
					});
			});
			res.status(200).json({output_stats:outputStats, executors:executors});
		};
		parser.loadPage(parser.config.url + parser.uris.component(topologyId, componentName), onPageLoaded);
	},
	getBolt: function(topologyId, componentName, res) {
		var onPageLoaded = function(body) {
			var inputStats = [];
			var outputStats = [];
			var executors = [];
			var $ = cheerio.load(body);
			var inputStatsTable = $('table').eq(2);
			var outputStatsTable = $('table').eq(3);
			var executorsTable = $('table').eq(4);
			inputStatsTable.find('tbody>tr').each(function(index, item) {
				inputStats.push({
						component: $(item).find('td').eq(0).text(),
						stream: $(item).find('td').eq(1).text(),
						execute_latency: $(item).find('td').eq(2).text(),
						executed: $(item).find('td').eq(3).text(),
						process_latency: $(item).find('td').eq(4).text(),
						acked: $(item).find('td').eq(5).text(),
						failed: $(item).find('td').eq(6).text()
					});
			});
			outputStatsTable.find('tbody>tr').each(function(index, item) {
				outputStats.push({
						stream: $(item).find('td').eq(0).text(),
						emitted: $(item).find('td').eq(1).text(),
						transferred: $(item).find('td').eq(2).text()
					});
			});
			executorsTable.find('tbody>tr').each(function(index, item) {
				executors.push({
						id: $(item).find('td').eq(0).text(),
						uptime: $(item).find('td').eq(1).text(),
						host: $(item).find('td').eq(2).text(),
						port: $(item).find('a').text(),
						emmited: $(item).find('td').eq(4).text(),
						transferred: $(item).find('td').eq(5).text(),
						capacity: $(item).find('td').eq(6).text(),
						execute_latency: $(item).find('td').eq(7).text(),
						executed: $(item).find('td').eq(8).text(),
						process_latency: $(item).find('td').eq(9).text(),
						acked: $(item).find('td').eq(10).text(),
						failed: $(item).find('td').eq(11).text()
					});
			});
			res.status(200).json({input_stats:inputStats, output_stats:outputStats, executors:executors});
		};
		parser.loadPage(parser.config.url + parser.uris.component(topologyId, componentName), onPageLoaded);
	}
};

var server = app.listen(8888, function() {
    console.log('Listening on port %d', server.address().port);
});
