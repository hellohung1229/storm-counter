requirejs.config({
  baseUrl: 'js/lib',
  paths: {
    d3: 'd3.v3.min',
    graphViewer_core: 'graphViewer/core',
    graphViewer_status: 'graphViewer/status',
    client: '../client'
  }
});

require(['client'], function(client) {
  client.init();
});
