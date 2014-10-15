define(['d3'], function(d3) {
	function getLayersCount(nodes) {
		return getNodesCountPerLayer(nodes).length;
	};

	function getNodeById (nodes, nodeId) {
		return nodes.filter(function(node) {
			return node.id === nodeId;
		})[0];
	};

	function getNodesPerLayer (nodes) {
		var result = [];
		var minLayerIndex = nodes[0].layerIndex;
		var maxLayerIndex = nodes[0].layerIndex;
		nodes.forEach(function(node) {
			if (node.layerIndex > maxLayerIndex) {
				maxLayerIndex = node.layerIndex;
			}
			if (node.layerIndex < minLayerIndex) {
				minLayerIndex = node.layerIndex;
			}
		});
		for (var layerIndex = minLayerIndex; layerIndex <= maxLayerIndex; layerIndex++) {
			var layerNodes = nodes.filter(function(node) {
				return node.layerIndex === layerIndex;
			});
			if (layerNodes.length > 0) {
				result.push(layerNodes);
			}
		}
		return result;
	};

	function getNodesCountPerLayer (nodes) {
		var result = [];
		getNodesPerLayer(nodes).forEach(function(layer) {
			result.push(layer.length);
		})
		return result;
	}

	function getTargetDimensions (target) {
		var height = d3.select(target).style('height');
		var width = d3.select(target).style('width');
		return {
			height: parseInt(height.substring(0, height.length - 2)),
			width: parseInt(width.substring(0, width.length - 2))
		};
	};

	function getHighestVerticalNodesCount (nodes) {
		var result = 0;
		getNodesCountPerLayer(nodes).forEach(function(count) {
			if (count > result) {
				result = count;
			}
		});
		return result;
	};

	function getGridSettings (nodes, targetDimensions) {
		var itemsCountX = getLayersCount(nodes) + 1;
		var itemsCountY = getHighestVerticalNodesCount(nodes) + 1;
		var unitX = targetDimensions.width / itemsCountX;
		var unitY = targetDimensions.height / itemsCountY;
		return {
			unitX: unitX,
			unitY: unitY,
			highestVerticalNodesCount: itemsCountY - 1,
			smallestUnit: (unitX < unitY ? unitX : unitY)
		};
	};

	function setNodesCoordinates (graph, gridSettings) {
		var nodesOrderedByLayer = getNodesPerLayer(graph.nodes);
		for (var layerIndex = 0; layerIndex < nodesOrderedByLayer.length; layerIndex++) {
			var layer = nodesOrderedByLayer[layerIndex];
			for (var nodeIndex = 0; nodeIndex < layer.length; nodeIndex++) {
				var node = layer[nodeIndex];
				node.coordinates = {
					x: gridSettings.unitX * (layerIndex + 1),
					y: gridSettings.unitY * (nodeIndex + 1 + (gridSettings.highestVerticalNodesCount - layer.length) / 2) // Small adjustment to vertically center the nodes
				};
			}
		};
	};

	function setLinksCoordinates (graph) {
		graph.links.forEach(function(link) {
			var sourceNode = getNodeById(graph.nodes, link.sourceId);
			var targetNode = getNodeById(graph.nodes, link.targetId);
			link.coordinates = {
				x1: sourceNode.coordinates.x,
				y1: sourceNode.coordinates.y,
				x2: targetNode.coordinates.x,
				y2: targetNode.coordinates.y
			};
		});
	};

	function setGroupsCoordinates (graph, gridSettings) {
		graph.groups.forEach(function(group) {
			var minX = getNodeById(graph.nodes, group.members[0]).coordinates.x;
			var maxX = minX;
			var minY = getNodeById(graph.nodes, group.members[0]).coordinates.y;
			var maxY = minY;
			group.members.forEach(function(nodeId) {
				var node = getNodeById(graph.nodes, nodeId);
				if (node.coordinates.x < minX) {
					minX = node.coordinates.x;
				}
				if (node.coordinates.y < minY) {
					minY = node.coordinates.y;
				}
				if (node.coordinates.x > maxX) {
					maxX = node.coordinates.x;
				}
				if (node.coordinates.y > maxY) {
					maxY = node.coordinates.y;
				}
			});
			group.coordinates = {
				x: minX - 0.45 * gridSettings.unitX,
				y: minY - 0.45 * gridSettings.unitY,
				width: maxX - minX + 0.9 * gridSettings.unitX,
				height:  maxY - minY + 0.9 * gridSettings.unitY
			};
		});
	};

	function doDrawGraph(graph, settings, targetDimensions, gridSettings) {
		var svg = d3.select(settings.target).select('svg');
		if (svg.empty()) {
			svg = d3.select(settings.target).append('svg');
		}
		svg.attr('width', targetDimensions.width);
		svg.attr('height', targetDimensions.height);

		// Data JOIN
		var link = svg.selectAll('g.link').data(graph.links);
		var node = svg.selectAll('g.node').data(graph.nodes);
		var group = svg.selectAll('g.group').data(graph.groups);

		// ENTER
		var groupEnter = group.enter().append('g');
		groupEnter.append('rect')
			.attr('transform', function(d) {return 'translate(' + d.coordinates.x + ',' + d.coordinates.y +')';})
			.attr('height', function(d) {return d.coordinates.height;})
			.attr('width', function(d) {return d.coordinates.width;})
			.classed('base', true)
			.style('fill', settings.groupColor);

		var linkEnter = link.enter().append('g');
		linkEnter.append('line')
			.attr('x1', function(d) {return d.coordinates.x1})
			.attr('y1', function(d) {return d.coordinates.y1})
			.attr('x2', function(d) {return d.coordinates.x2})
			.attr('y2', function(d) {return d.coordinates.y2})
			.classed('base', true)
			.style('stroke', settings.linkColor)
			.style('stroke-width', '3');

		var nodeEnter = node.enter().append('g');
		nodeEnter.append('circle')
				.attr('transform', function(d) {return 'translate(' + d.coordinates.x + ',' + d.coordinates.y +')';})
				.attr('r', gridSettings.smallestUnit / 3)
				.classed('base', true)
				.style('fill', settings.nodeColor);

		nodeEnter.append('text')
			.attr('transform', function(d) {return 'translate(' + d.coordinates.x + ',' + d.coordinates.y +')';})
			.attr('dy', '.3em')
			.attr('font-size', settings.nodeFontSize)
			.attr('text-anchor', 'middle')
			.style('fill', settings.nodeFontColor)
			.text(function(d) { return d.text });

		// ENTER+UPDATE
		link.classed('link', true);
		node.classed('node', true);
		group.classed('group', true);
		makeDraggable(node, graph, gridSettings, svg);
		node.selectAll('text').text(function(d) { return d.text });

		// EXIT
		group.exit().remove();
		link.exit().remove();
		node.exit().remove();
	};

	function makeDraggable(items, graph, gridSettings, svg) {
		items.call(d3.behavior.drag().on("drag", function(node) {
			var x = d3.event.x;
			var y = d3.event.y;
			node.coordinates = {
				x: x,
				y:y
			};
			d3.select(this).selectAll('circle').attr("transform", "translate(" + x + "," + y + ")");
			d3.select(this).select('text').attr("transform", "translate(" + x + "," + y + ")");
			svg.selectAll('line')
				.filter(function(link) {
					return link.sourceId == node.id;
				})
				.attr('x1', x)
				.attr('y1', y);
			svg.selectAll('line')
				.filter(function(link) {
					return link.targetId == node.id;
				})
				.attr('x2', x)
				.attr('y2', y);
			setGroupsCoordinates(graph, gridSettings);
			svg.selectAll('rect.base')
				.attr('transform', function(d) {return 'translate(' + d.coordinates.x + ',' + d.coordinates.y +')';})
				.attr('height', function(d) {return d.coordinates.height;})
				.attr('width', function(d) {return d.coordinates.width;});
			var event = new Event('draggedNode');
			this.dispatchEvent(event);
		}));
	};

	return {
		drawGraph: function(graph, settings) {
			var targetDimensions = getTargetDimensions(settings.target);
			var gridSettings = getGridSettings(graph.nodes, targetDimensions);
			setNodesCoordinates(graph, gridSettings);
			setLinksCoordinates(graph);
			setGroupsCoordinates(graph, gridSettings);
			doDrawGraph(graph, settings, targetDimensions, gridSettings);
		},
		clearGraph: function(settings) {
			d3.select(settings.target).select('svg').remove();
		}
	};
});
