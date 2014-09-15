(function() {
	engine = {
		init: function() {
			$('#topologySelectionRow').hide();
			$('#topologyViewer').hide();
			$('#APIFormButton').click(function(e) {
				e.preventDefault();
				engine.proxyURL = 'http://localhost:8746';
				var apiURL = $('#stormAPIURL').val();
				var apiPort = $('#stormAPIPort').val();
				
				$.ajax({
					url: engine.proxyURL + '/connect',
					type: 'GET',
					data: {stormAPIUrl: apiURL, stormAPIPort: apiPort},
					success: function (data) {
						engine.getTopologies();
					}
				});
				
			});
		},
		getTopologies: function() {
			$.ajax({
				url: engine.proxyURL + engine.URIs.topologies,
				type: 'GET',
				dataType: 'json',
				success: function (data) {
					if (data.length !== 0) {
						var topologiesList = $('#topologySelectionDropdown');
						topologiesList.empty();
						
						var topologies = data;
						$.each(topologies, function(key, val) {
							topologiesList.append(
								$('<li>').attr('role', 'presentation').append(
									$('<a>').attr('role', 'menuitem').attr('tabindex', '-1').data('name', 'lol').text(val.name + ' - ' + val.id)
								)
							);
							engine.loadedTopologies[val.id] = {name:val.name};
						});
						
						topologiesList.find('li').each(function(key, val) {
							$(this).data('name', topologies[key].name);
							$(this).data('id', topologies[key].id);
							$(this).click(function() {
								engine.loadTopology($(this).data('id'));
							});
						});
						$('#stormAPIForm').hide();
						$('#topologySelectionRow').show();
					} else {
						alert('No topology found on this server');
					}
				}
			});
		},
		loadTopology: function(id) {
			var topology = engine.loadedTopologies[id];
			$('#topologyViewer').find('h1').text(topology.name);
			$('#topologyViewer').show();
		},
		URIs: {
			topologies:'/api/topology/summary',
			supervisors:'/api/supervisors/summary'
		},
		loadedTopologies: {}
	};
	
	engine.init();
})();