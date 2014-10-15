requirejs.config({
  baseUrl: 'js/lib',
  paths: {
    d3: 'd3.v3.min',
    jquery: 'jquery.min',
    graphViewer_core: 'graphViewer/core',
    graphViewer_status: 'graphViewer/status',
    parser: '../parser'
  }
});

define('jquery-private', ['jquery'], function (jq) {
    return jq.noConflict( true );
});

require(['parser'], function(parser) {
  $('#topologySelectionRow').hide();
  $('#topologyViewer').hide();
  parser.init();
});
