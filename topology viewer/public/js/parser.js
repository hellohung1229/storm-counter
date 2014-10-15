define(['jquery-private'], function(jq) {
  var proxyUrl = 'http://localhost:8746';

  var URIs = {
    topologies:'/api/topology/summary',
    supervisors:'/api/supervisors/summary'
  };

  var loadedTopologies = {};

  function init() {
    console.log(jq);
    jq('#APIFormButton').click(function(e) {
      e.preventDefault();
      var apiURL = jq('#stormAPIURL').val();
      var apiPort = jq('#stormAPIPort').val();
      if (apiURL !== '' && apiPort !== '') {
        jq.ajax({
          url: proxyURL + '/connect',
          type: 'GET',
          data: {stormAPIUrl: apiURL, stormAPIPort: apiPort},
          success: function (data) {
            getTopologies();
          }
        });
      }

    });
  };

  function getTopologies() {
    jq.ajax({
      url: proxyURL + topologies,
      type: 'GET',
      dataType: 'json',
      success: function (data) {
        if (data.length !== 0) {
          var topologiesList = jq('#topologySelectionDropdown');
          topologiesList.empty();

          var topologies = data;
          jq.each(topologies, function(key, val) {
            topologiesList.append(
              jq('<li>').attr('role', 'presentation').append(
                jq('<a>').attr('role', 'menuitem').attr('tabindex', '-1').data('name', 'lol').text(val.name + ' - ' + val.id)
              )
            );
            loadedTopologies[val.id] = {name:val.name};
          });

          topologiesList.find('li').each(function(key, val) {
            jq(this).data('name', topologies[key].name);
            jq(this).data('id', topologies[key].id);
            jq(this).click(function() {
              loadTopology(jq(this).data('id'));
            });
          });
          jq('#stormAPIForm').hide();
          jq('#topologySelectionRow').show();
        } else {
          alert('No topology found on this server');
        }
      }
    });
  };

  return {
    init: init
  };
});
