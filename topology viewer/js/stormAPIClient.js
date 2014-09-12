(function() {
	engine = {
		init: function() {
			$("#queryAPIButton").click(function() {
				var apiURL = $("#stormAPIURL").val();
				$.ajax({
					beforeSend: function(xhrObj){
							xhrObj.setRequestHeader("Access-Control-Allow-Origin","*");
					},
					url: apiURL + engine.URIs.supervisors,
					type: 'GET',
					dataType: 'json',
					error: function(jqXHR, textStatus, errorThrown) {   
						alert('Error Message: '+textStatus);
						alert('HTTP Error: '+errorThrown);
					},
					success: function (data) {
						console.log(data);
					}
				});
			});
		},
		URIs: {
			topologies:"/api/topology/summary",
			supervisors:"/api/supervisors/summary"
		}
	};
	
	engine.init();
})();