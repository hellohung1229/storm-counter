(function() {
	var engine = {
		init: function() {
			var width = 960;
			var height = 500;
			var svg = d3.select("body").append("svg")
				.attr("width", width)
				.attr("height", height);
				
			this.nodes = [
				{
					id:1,
					name:"node1"
				},
				{
					id:2,
					name:"node2"
				},
				{
					id:3,
					name:"node3"
				},
				{
					id:4,
					name:"node4"
				}
			];
			
			this.links = [
				{
					sourceId:1,
					targetId:2
				},
				{
					sourceId:1,
					targetId:3
				},
				{
					sourceId:3
					targetId:4
				}
			];
		},
		setLevelsToNodes: function() {
			setLevelToSingleNode(nodes[0], 1);
		},
		setLevelToSingleNode: function(node, level) {
			if (node.level === undefined) {
				node.level = level;
				getParents(node).forEach(function(parent) {
					setLevelToSingleNode(parent, level - 1);
				});
				getChildren(node).forEach(function(child) {
					setLevelToSingleNode(child, level + 1);
				});
			}
		},
		getChildren: function(node) {
			var children = [];
			links.forEach(function(link) {
				if (link.sourceId === node.id) {
					children.push(getNodeById(link.targetId));
				}
			});
			return children;
		},
		getParents: function(node) {
			var parents = [];
			links.forEach(function(link) {
				if (link.targetId === node.id) {
					parents.push(getNodeById(link.sourceId));
				}
			});
			return parents;
		},
		getNodeById: function(id) {
			nodes.forEach(function(node) {
				if (node.id === id) {
					return node;
				}
			});
		}
	};
	
	var link = svg.selectAll("line")
		.data(links);
		
	link.enter().append("line")
		.attr("x1", function(d) {return nodes[d.source].x})
		.attr("y1", function(d) {return nodes[d.source].y})
		.attr("x2", function(d) {return nodes[d.target].x})
		.attr("y2", function(d) {return nodes[d.target].y})
		.style("stroke", "rgb(6,120,155)");
	
	var circle = svg.selectAll("circle")
		.data(nodes);
	
	circle.enter().append("circle")
		.attr("cx", function(d) {return d.x;})
		.attr("cy", function(d) {return d.y;})
		.attr("r", 0)
		.style("fill", "steelblue")
		.transition()
		.attr("r",20);
	
})();