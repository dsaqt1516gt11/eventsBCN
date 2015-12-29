/* var API_BASE_URL = "http://147.83.7.207:8080/eventsBCN"; */
var API_BASE_URL = "http://localhost:8080/eventsBCN";
var token = $.cookie('token');
console.log(token);

$(document).ready(function(){	    
    /* location.href="Eventos.html" */	
	getEventos();
	$("main").on("click", "article", function(e) {
		console.log(e);
		var arr = e.target.className.split(' ');
		var eventid = arr[1];		
	$('#myModal5').modal('show');
	getEvento(eventid);
	
	$("#Assist").click(function() {
	if ($("#Assist").text() == "NO ASISTIRÉ!"){
		$("#Assist").text("ASISTIRÉ!");		
		wontassit(id, eventid)	
	}else{
		$("#Assist").text("NO ASISTIRÉ!");	
		assisttoEvent(id, eventid);	
	}
});
	
});




$(document).keydown(function(e){
	if(e.keyCode == 27)
		$('#myModal5').modal('hide');
	});
		
});

function assisttoEvent(id, eventid) {
	
	var url = API_BASE_URL + '/companies/1sdf89dsf8/events/' + eventid +'/assist';
		
	$.ajax({
		url : url,
		type : 'POST',
		crossDomain : true,
		dataType : 'json',
		contentType:'application/vnd.dsa.eventsBCN.user+json',
		   		
	}).done(function(/* data, status, jqxhr */) {
		
		console.log("Asistiré");			
		 
	}).fail(function() {
		console.log("Error");
	});
}
	


function linksToMap(links){
	var map = {};
	$.each(links, function(i, link){
		$.each(link.rels, function(j, rel){
			map[rel] = link;
		});
	});

	return map;
} 

function getEventos() {
	var url = API_BASE_URL + '/events';
	$("#result").text('');
	console.log(token);
	
	$.ajax({
		url : url,
		type : 'GET',
		crossDomain : true,
		dataType : 'json',
		headers: {
        'X-Auth-Token':token,        
		}, 
	}).done(function(data, status, jqxhr) {
		var f = data.events ;
				console.log("estoy dentro");		
		$.each(f, function(i, v) {
									
			var evento = v; 
				console.log("estoy dentro");		
			
			/* var img=document.createElement('img');
            img.setAttribute("src",evento.photo);
            img.setAttribute("align","left");
            img.setAttribute("width","168");
            img.setAttribute("height","66");
            img.setAttribute("class","img-thumbnail");
            $(img).appendTo($('#get_thread_result')); */
			
			$('<article class="blog-item"><div class="row" id="columna"><div class="col-md-8"><img style="width: 100%;id="imagen" class="foto '+evento.id+'" src="assets/img/corporate.jpg" class="img-thumbnail center-block"'
			+'alt="Blog Post Thumbnail"></a></div>'
			+'<div class="col-md-4"><h1 style="text-align:center;class="titulo" id="titulo"><strong> Titulo: </strong>' + evento.title +'<br>' 
			+'<strong> Fecha: </strong> ' + evento.date + '<br>'
			+'</div></div></article>').appendTo($('#result')); 
			
			
			/* $('<strong> Lugar: </strong> ' + evento.lugar + '<br>').appendTo($('#result'));
			$('<strong> Empresa: </strong> ' + evento.empresa + '<br>').appendTo($('#result')); */
				
			
		 });
		 
		
		 
	}).fail(function() {
		$("#result").text("No files");
	});
} 

/*--------------------------------------------------------------------------------------------------------------*/
function getEvento(eventid) {
	
	var url = API_BASE_URL + '/events/' + eventid;
	console.log(url);
	$("#result1").text('');
	
	$.ajax({
		url : url,
		headers: {
        'X-Auth-Token':token,        
		}, 
		type : 'GET',
		crossDomain : true,
		dataType : 'json',
	}).done(function(data, status, jqxhr) {

		var evento = data;
			
			$('<strong> Titulo: </strong>' + evento.title +'<br>' ).appendTo($('#result1'));
			$('<strong> Descripción: </strong> ' + evento.description + '<br>').appendTo($('#result1'));
			$('<strong> Fecha: </strong> ' + evento.date + '<br>').appendTo($('#result1'));
			$('<strong> Categoria: </strong> ' + evento.category + '<br>').appendTo($('#result1'));			
			
			
	}).fail(function() {
		$("#result").text("No files");
	});
} 
 


