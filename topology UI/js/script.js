(function() {
	var width = 960;
	var height = 500;
	var svg = d3.select("body").append("svg")
    .attr("width", width)
    .attr("height", height);
	
	var nodes = [
		{x:50, y:100},
		{x:100, y:300},
		{x:150, y:100},
		{x:200, y:100}
	];
	
	var links = [
		{source:1,
		target:2,
		weight:1}
	];
	
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
	
	setTimeout(function() {
		nodes.push({x:300,y:100});
		circle.enter();
		circle.transition()
			.attr("r",10);
	}, 3000);
})();