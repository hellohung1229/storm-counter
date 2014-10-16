define([], function() {
  var serverUrl = 'http://localhost:8888';

  var state = 'idle';

  var URIs = {
    checkUrl: '/checkUrl',
    topologies:'/topologies',
    topology: function(id) {return '/topology/' + id},
    bolt: function(topologyId, boltId) {return URIs.topology(topologyId) + '/bolt/' + boltId},
    spout: function(topologyId, spoutId) {return URIs.topology(topologyId) + '/spout/' + spoutId}
  };

  var anchors = {
    field_APIUrl: $('#stormAPIURL'),
    field_APIPort: $('#stormAPIPort'),
    a_loadingMessage: $('#loadingMessage'),
    button_connectApi: $('#APIFormButton'),
    dropdown_topologySelection: $('#topologySelectionDropdown'),
    row_urlSelection: $('#urlSelectionRow'),
    row_topologySelection: $('#topologySelectionRow'),
    row_topologyViewer: $('#topologyViewer'),
    row_loader: $('#loader'),
    row_noTopology: $('#noTopology')
  };

  function init() {
    setConnectBehavior();
    setSelectBehavior();
  };

  function setConnectBehavior() {
    anchors.button_connectApi.click(function(e) {
      e.preventDefault();
      anchors.row_urlSelection.hide();
      anchors.row_loader.show();
      anchors.a_loadingMessage.text('Checking provided URL...');
      checkInputUrl();

    });
  };

  function setSelectBehavior() {

  };

  function checkInputUrl() {
    var url = anchors.field_APIUrl.val();
    url = (url.slice(-1) === '/') ? url.slice(0,-1) : url;
    url = url + ':' + anchors.field_APIPort.val();
    $.ajax({
      url: serverUrl + URIs.checkUrl,
      type: 'GET',
      data: {
        url: url
      },
      dataType: 'json',
      success: function(data, status){
        anchors.a_loadingMessage.text('Fetching available topologies...');
        getTopologies();
      },

      error: function(result, status, error){
        anchors.row_urlSelection.show();
        anchors.row_loader.hide();
      }

    });
  };

  function getTopologies() {
    $.ajax({
      url: serverUrl + URIs.topologies,
      type: 'GET',
      dataType: 'json',
      success: function(data) {
        if (data.length !== 0) {
          data.forEach(function(topology) {
            anchors.dropdown_topologySelection.append('<li data-id="' + topology.id + '"><a>' + topology.name + '</a></li>');
          });
          anchors.dropdown_topologySelection.children('li').each(function(index, item) {
            $(item).click(function(){
              console.log('You clicked on ' + $(this).data('id'));
            });
          });

          anchors.row_loader.hide();
          anchors.row_topologySelection.show();
        } else {
          anchors.row_loader.hide();
          anchors.row_noTopology.show();
        }
      },
      error: function(result, status, error) {

      }
    });
  };

  function getTopology(id) {
    console.log('Getting the topology ' + id);
  };

  return {
    init: init
  };
});
