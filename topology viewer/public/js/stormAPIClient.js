(function() {
	engine = {
		init: function() {
			$("#topologySelectionRow").hide();
			$("#topologyViewer").hide();
			$("#APIFormButton").click(function(e) {
				e.preventDefault();
				var proxyURL = 'http://localhost:8746/connect';
				var apiURL = $("#stormAPIURL").val();
				
				$.ajax({
					url: proxyURL,
					type: 'GET',
					data: {stormAPIUrl: apiURL, stormAPIPort: 8745},
					success: function (data) {
						// engine.onAPICallSuccess(data);
						console.log('success');
					},
					error: function(data) {
						console.log('error');
					}
				});
				
			});
		},
		onAPICallSuccess: function(data) {
			var topologiesList = $("#topologySelectionDropdown");
			topologiesList.empty();
			var topologies = data.topologies;
			
			if (topologies) {
				$.each(topologies, function(key, val) {
					topologiesList.append(
						$("<li>").attr("role", "presentation").append(
							$("<a>").attr("role", "menuitem").attr("tabindex", "-1").data("name", "lol").text(val.name + " - " + val.id)
						)
					);
					engine.loadedTopologies[val.id] = {name:val.name};
				});
				
				topologiesList.find("li").each(function(key, val) {
					$(this).data("name", topologies[key].name);
					$(this).data("id", topologies[key].id);
				});
				
				topologiesList.find("li").each(function(key, val) {
					$(this).click(function() {
						engine.loadTopology($(this).data("id"));
					});
				});
				$("#topologySelectionRow").show();
			} else {
				flash('No topology found on this server', true);
			}
		},
		loadTopology: function(id) {
			var topology = engine.loadedTopologies[id];
			$("#topologyViewer").find("h1").text(topology.name);
			$("#topologyViewer").show();
		},
		URIs: {
			topologies:"/api/topology/summary",
			supervisors:"/api/supervisors/summary"
		},
		loadedTopologies: {}
	};
	
	engine.init();
})();