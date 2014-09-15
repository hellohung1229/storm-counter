(function() {
	var engine = {
		svgAttributes: {},
		init: function(target, width, height, topology) {
			engine.topology = topology;
			engine.svgAttributes.width = width;
			engine.svgAttributes.height = height;
			var svg = d3.select(target).append('svg')
				.attr('width', width)
				.attr('height', height);
		
			engine.topologyFormater.mapNodesOnCanva();
			engine.refreshDisplay(engine.topology);
			engine.animateDisplay();
		},
		topologyFormater: {
			getNodeById: function(id) {
				var targetNode;
				engine.topology.nodes.forEach(function(node) {
					if (node.id === id) {
						targetNode = node;
					}
				});
				return targetNode;
			},
			getNodesPerLevel: function(level) {
				var nodes = [];
				engine.topology.nodes.forEach(function(node) {
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
				engine.topology.nodes.forEach(function(node) {
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
		refreshDisplay: function(topology) {
			engine.topology = topology;
			var svg = d3.select('svg');
			var tooltip = d3.select('#tooltip');
			if (tooltip.empty()) {
				tooltip = d3.select('body')
					.append('div')
					.style('position', 'absolute')
					.style('z-index', '10')
					.style('visibility', 'hidden')
					.style('background-color', '#f0fcff')
					.style('box-shadow', '0px 0px 13px 1px #888888')
					.style('padding', '10px')
					.attr('id', 'tooltip')
					.text('Test de text');
			}
			
			// Data JOIN
			var link = svg.selectAll('link')
				.data(engine.topology.links);
			var node = svg.selectAll('g')
				.data(engine.topology.nodes);
			
			//ENTER
			var linkEnter = link.enter();
			linkEnter.append('line')
				.attr('x1', function(d) {return engine.topologyFormater.getNodeById(d.sourceId).coordinates.x})
				.attr('y1', function(d) {return engine.topologyFormater.getNodeById(d.sourceId).coordinates.y})
				.attr('x2', function(d) {return engine.topologyFormater.getNodeById(d.targetId).coordinates.x})
				.attr('y2', function(d) {return engine.topologyFormater.getNodeById(d.targetId).coordinates.y})
				.style('stroke', 'steelblue')
				.style('stroke-width', '3')
				.style('opacity', '0')
				.transition()
				.style('opacity', '1');
			
			var nodeEnter = node.enter().append('g');
			nodeEnter.append('circle')
				.attr('cx', function(d) {return d.coordinates.x;})
				.attr('cy', function(d) {return d.coordinates.y;})
				.attr('r', 0)
				.classed('outter', true)
				.style('fill', 'steelblue')
				.transition()
				.attr('r',50)
				
			nodeEnter.append('circle')
				.on('mouseover', function(d) {
					d3.select(this).style('fill', '#E8DDCC');
					tooltip.style('visibility', 'visible');
					tooltip.text(d.name);
				})
				.on('mousemove', function(){
					tooltip.style('top',(d3.event.pageY-10)+'px').style('left',(d3.event.pageX+20)+'px');
				})
				.on('mouseout', function(d) {
					tooltip.style('visibility', 'hidden');
					d3.select(this).style('fill', '#FCF3E6');
				})
				.attr('cx', function(d) {return d.coordinates.x;})
				.attr('cy', function(d) {return d.coordinates.y;})
				.attr('r', 0)
				.classed('inner', true)
				.style('fill', '#FCF3E6')
				.transition()
				.attr('r',40);					
				
			nodeEnter.append('text')
				.attr('x', function(d) {return d.coordinates.x;})
				.attr('y', function(d) {return d.coordinates.y;})
				.attr('dy', '.3em')
				.attr('font-family', 'FontAwesome')
				.attr('font-size', function(d) { return '2em'} )
				.attr('text-anchor', 'middle')
				.text(function(d) { return d.icon })
				.style('opacity', '0')
				.transition()
				.style('opacity', '1');
			
			// ENTER+UPDATE
			node.attr('class', function(d) {return d.status;});
			link.attr('class', function(d) {return d.status;});
			
			// EXIT
			link.exit().remove();
			node.exit().remove();
		},
		animateDisplay: function() {	
			setInterval(function() {
				var svg = d3.select('svg');
				var errorNode = svg.selectAll('g').filter('.error');
				var errorLink = svg.selectAll('line').filter('.error');
				var inactiveNode = svg.selectAll('g').filter('.inactive');
				errorNode.selectAll('circle')
					.filter('.outter')
					.style('fill', '#E01616');
				
				errorNode.selectAll('circle')
					.transition()
					.duration(400)
					.attr('r', function() {
						return (1.1 * d3.select(this).attr('r'));
					})
					.transition()
					.delay(400)
					.attr('r', function() {
						return (d3.select(this).attr('r'));
					});
					
				errorLink.style('stroke', '#E01616');
				errorLink.transition()
					.duration(400)
					.style('stroke-width', '6')
					.transition()
					.delay(400)
					.style('stroke-width', '3');
				inactiveNode.selectAll('circle')
					.filter('.outter')
					.style('fill', '#BCC2C4');
			}, 800);
		
			setInterval(function() {
				var svg = d3.select('svg');
				var activeNode = svg.selectAll('g').filter('.active');
				var activeLink = svg.selectAll('line').filter('.active');
				var inactiveLink = svg.selectAll('line').filter('.inactive');
				activeNode.selectAll('circle')
					.filter('.outter')
					.transition()
					.duration(1000)
					.style('fill', '#00B049')
					.transition()
					.delay(1000)
					.style('fill', '#00DB5B');
				activeLink.transition()
					.duration(1000)
					.style('stroke', '#00B049')
					.transition()
					.delay(1000)
					.style('stroke', '#00DB5B');
				inactiveLink.style('stroke', '#BCC2C4');
			}, 2000);
		}
	};
	window.topologyViewer = {};
	window.topologyViewer.init = engine.init;
	window.topologyViewer.refreshDisplay = engine.refreshDisplay;
})();