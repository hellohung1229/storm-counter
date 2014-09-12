(function() {
	var engine = {
		svgAttributes: {},
		init: function(width, height, nodes, links) {
			this.nodes = nodes;
			this.links = links;
			this.svgAttributes.width = width;
			this.svgAttributes.height = height;
			var svg = d3.select(".container").append("svg")
				.attr("width", width)
				.attr("height", height);
		
			engine.topologyFormater.mapNodesOnCanva();
			engine.canvaDrawer.drawTopology(engine.nodes, engine.links);
		},
		topologyFormater: {
			getNodeById: function(id) {
				var targetNode;
				engine.nodes.forEach(function(node) {
					if (node.id === id) {
						targetNode = node;
					}
				});
				return targetNode;
			},
			getNodesPerLevel: function(level) {
				var nodes = [];
				engine.nodes.forEach(function(node) {
					if (node.level === level) {
						nodes.push(node);
					}
				});
				return nodes;
			},
			mapNodesOnCanva: function() {
				var countOfLevels = engine.topologyFormater.getLevelsCount();
				for (i = 1; i <= countOfLevels; i++) {
					nodesAtThisLevel = engine.topologyFormater.getNodesPerLevel(i);
					levelX = i * (engine.svgAttributes.width / (countOfLevels + 1));
					levelStepY = engine.svgAttributes.height / (nodesAtThisLevel.length + 1);
					nodesAtThisLevel.forEach(function(node, index) {
						engine.topologyFormater.setNodeCoordinates(node, levelX, (index + 1) * levelStepY);
					});
				}
			},
			getLevelsCount: function() {
				var minLevel = 1;
				var maxLevel = 1;
				engine.nodes.forEach(function(node) {
					if (node.level < minLevel) {
						minLevel = node.level;
					}
					if (node.level > maxLevel) {
						maxLevel = node.level;
					}
				});
				return maxLevel - minLevel + 1;
			},
			setNodeCoordinates: function(node, x, y) {
				node.coordinates = {x:x, y:y};
			}
		},
		canvaDrawer: {
			drawTopology: function(nodes, links) {
				this.drawLinks(links);
				this.drawNodes(nodes);
			},
			drawLinks: function(links) {
				var svg = d3.select("svg");
				var line = svg.selectAll("line")
					.data(links);
					
				line.enter().append("line")
					.attr("x1", function(d) {return engine.topologyFormater.getNodeById(d.sourceId).coordinates.x})
					.attr("y1", function(d) {return engine.topologyFormater.getNodeById(d.sourceId).coordinates.y})
					.attr("x2", function(d) {return engine.topologyFormater.getNodeById(d.targetId).coordinates.x})
					.attr("y2", function(d) {return engine.topologyFormater.getNodeById(d.targetId).coordinates.y})
					.style("stroke", "steelblue");
			},
			drawNodes: function(nodes) {
				var svg = d3.select("svg");
								
				var node = svg.selectAll("node")
					.data(nodes);
				
				var nodeEnter = node.enter()
					.append("g");
					
				var outterCircle = nodeEnter.append("circle")
					.attr("cx", function(d) {return d.coordinates.x;})
					.attr("cy", function(d) {return d.coordinates.y;})
					.attr("r", 0)
					.style("fill", "steelblue")
					.transition()
					.attr("r",50);
				
				var innerCircle = nodeEnter.append("circle")
					.attr("cx", function(d) {return d.coordinates.x;})
					.attr("cy", function(d) {return d.coordinates.y;})
					.attr("r", 0)
					.style("fill", "grey")
					.transition()
					.attr("r",40);
				
				nodeEnter.append("text")
					.attr("x", function(d) {return d.coordinates.x;})
					.attr("y", function(d) {return d.coordinates.y;})
					.attr("font-family", "FontAwesome")
					.attr("font-size", function(d) { return "2em"} )
					.attr("text-anchor", "middle")
					.text(function(d) { return d.icon })
				
				
				
			}
		}
	};
	
	var nodes = [
		{
			id:1,
			name:"node1",
			level:1,
			icon:"\uF099"
		},
		{
			id:2,
			name:"node2",
			level:1,
			icon:"\uF143"
		},
		{
			id:3,
			name:"node3",
			level:2,
			icon:"\uF02B"
		},
		{
			id:4,
			name:"node4",
			level:2,
			icon:"\uF0C9"
		},
		{
			id:5,
			name:"node5",
			level:2,
			icon:"\uF0B0"
		},
		{
			id:6,
			name:"node6",
			level:3,
			icon:"\uF0EE"
		},
		{
			id:7,
			name:"node7",
			level:3,
			icon:"\uF046"
		},
		{
			id:8,
			name:"node8",
			level:4,
			icon:"\uF1C0"
		}
	];
	var links = [
		{
			sourceId:1,
			targetId:3
		},
		{
			sourceId:1,
			targetId:4
		},
		{
			sourceId:2,
			targetId:4
		},
		{
			sourceId:2,
			targetId:5
		},
		{
			sourceId:3,
			targetId:6
		},
		{
			sourceId:3,
			targetId:7
		},
		{
			sourceId:4,
			targetId:6
		},
		{
			sourceId:4,
			targetId:7
		},
		{
			sourceId:6,
			targetId:8
		},
		{
			sourceId:7,
			targetId:8
		}
	];
	
	engine.init(1000,500, nodes, links);
})();