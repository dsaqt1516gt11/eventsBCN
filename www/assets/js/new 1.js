var API_BASE_URL = "http://localhost:8080/eventsBCN";


$(document).ready(function(){	    
    /* location.href="Eventos.html" */	
	getEventos();
	$("body").on("click", ".foto", function(e) {
	$('#myModal5').modal('show');
	getEvento();
});
$("body").on("click", ".titulo", function(e) {
	$('#myModal5').modal('show');
});
$(document).keydown(function(e){
	if(e.keyCode == 27)
		$('#myModal5').modal('hide');
	});
	
});


function linksToMap(links){
	var map = {};
	$.each(links, function(i, link){
		$.each(link.rels, function(j, rel){
			map[rel] = link;
		});
	});

	return map;
}



function getEventos(uri, complete){
	// var authToken = JSON.parse(sessionStorage["auth-token"]);
	// var uri = authToken["links"]["current-events"].uri;
	$.get(uri)
		.done(function(events){
			events.links = linksToMap(events.links);
			complete(events);
			console.log(events);
		})
		.fail(function(){});
}

function getEvento(uri, complete){
	$.get(uri)
		.done(function(event){
			complete(event);
			console.log(event);
		})
		.fail(function(data){
		});
}