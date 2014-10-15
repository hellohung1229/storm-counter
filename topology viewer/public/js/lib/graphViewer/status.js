define(['d3'], function(d3) {
  var svg;

  function setSvgVariable (settings) {
    svg = d3.select(settings.target).select('svg');
  };

  function createTranslateString (x, y) {
    return 'translate(' + x + ',' + y + ')';
  };

  function appendStatusData (graphStatus) {
    graphStatus.nodes.forEach(function(nodeStatus) {
      svg.selectAll('g.node').filter(function(node) {
        return node.id === nodeStatus.nodeId;
      }).data()[0].status = nodeStatus.status;
    });
    graphStatus.links.forEach(function(linkStatus) {
      svg.selectAll('g.link').filter(function(link) {
        return (link.sourceId === linkStatus.sourceId) && (link.targetId === linkStatus.targetId);
      }).data()[0].status = linkStatus.status;
    });
    graphStatus.groups.forEach(function(groupStatus) {
      svg.selectAll('g.group').filter(function(group) {
        return group.id === groupStatus.groupId;
      }).data()[0].status = groupStatus.status;
    });
  };

  function registerElementsForDragEvents(graphStatus, settings) {
    svg.selectAll('g.node')[0].forEach(function(g) {
      g.addEventListener('draggedNode', function (e) {
        drawGroupStatus(graphStatus, settings);
      }, false);
    });
  };

  function drawNodeStatus(graphStatus, settings) {
    // Data JOIN
    var statefulNodes = svg.selectAll('g.node').filter(function(node) {
      return node.status;
    });

    statefulNodes.each(function() {
      var node = d3.select(this);
      var nodeStatus = node.select('circle.status');
      if (nodeStatus.empty()) {
        // ENTER
        node.append('circle')
          .attr('transform', node.select('circle.base').attr('transform'))
          .attr('r', settings.borderSize + parseInt(node.select('circle.base').attr('r')))
          .classed('status', true);
        node.selectAll('circle').sort(function() {return 1});
      }
    });

    // ENTER + UPDATE
    svg.selectAll('g.node').selectAll('circle.status')
      .style('fill', function(d) {
        return settings.status[d.status].style.color;
      });

    // EXIT
    var statelessNodes = svg.selectAll('g.node').filter(function(node) {
      return node.status === undefined;
    });
    statelessNodes.selectAll('circle.status').remove();
  };

  function drawGroupStatus(graphStatus, settings) {
    // Data JOIN
    var statefulGroups = svg.selectAll('g.group').filter(function(group) {
      return group.status;
    });

    statefulGroups.each(function() {
      var group = d3.select(this);
      var groupData = group.data()[0];
      var baseRect = group.select('rect.base');
      var groupStatus = group.select('rect.status');
      if (groupStatus.empty()) {
        // ENTER
        group.append('rect')
          .attr('x', 0)
          .attr('y', 0)
          .classed('status', true);
        group.selectAll('rect').sort(function() {return 1});
      }
      group.select('rect.status')
        .attr('transform', createTranslateString(groupData.coordinates.x - settings.borderSize, groupData.coordinates.y - settings.borderSize))
        .attr('height', 2*settings.borderSize + parseInt(baseRect.attr('height')))
        .attr('width', 2*settings.borderSize + parseInt(baseRect.attr('width')));
    });

    // ENTER + UPDATE
    svg.selectAll('g.group').selectAll('rect.status')
      .style('fill', function(d) {
        return settings.status[d.status].style.color;
      });

    // EXIT
    var statelessGroups = svg.selectAll('g.group').filter(function(group) {
      return group.status === undefined;
    });
    statelessGroups.selectAll('rect.status').remove();
  };

  function drawLinkStatus(graphStatus, settings) {
    // Data JOIN
    var statefulLinks = svg.selectAll('g.link').filter(function(link) {
      return link.status;
    });

    statefulLinks.each(function() {
      var link = d3.select(this);
      var linkStatus = link.select('line.status');
      var baseLine = link.select('line.base');
      if (linkStatus.empty()) {
        // ENTER
        link.append('line')
          .attr('x1', baseLine.attr('x1'))
          .attr('y1', baseLine.attr('y1'))
          .attr('x2', baseLine.attr('x2'))
          .attr('y2', baseLine.attr('y2'))
          .style('stroke-width', settings.borderSize / 1.5 + parseInt(baseLine.style('stroke-width'), 10))
          .classed('status', true);
        link.selectAll('line').sort(function() {return 1});
      }
    });

    // ENTER + UPDATE
    svg.selectAll('g.link').selectAll('line.status')
      .style('stroke', function(d) {
        return settings.status[d.status].style.color;
      });

    // EXIT
    var statelessLinks = svg.selectAll('g.link').filter(function(link) {
      return link.status === undefined;
    });
    statelessLinks.selectAll('line.status').remove();
  };

  function doDrawStatus (graphStatus, settings) {
    drawNodeStatus(graphStatus, settings);
    drawGroupStatus(graphStatus, settings);
    drawLinkStatus(graphStatus, settings);
  };

  return {
    drawStatus: function(graphStatus, settings) {
      setSvgVariable(settings);
      appendStatusData(graphStatus);
      registerElementsForDragEvents(graphStatus, settings);
      doDrawStatus(graphStatus, settings);
    },
    clearStatus: function(settings) {

    }
  }
});
