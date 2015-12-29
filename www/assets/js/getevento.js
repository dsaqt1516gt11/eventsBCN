
/* $(document).ready(function(){	    */ 
    /* location.href="Eventos.html" */		
/* 	$("body").on("click", ".foto", function(e) {
	$('#myModal5').modal('show');
	getEvento();
});


function getEvento() {
	var token = Cookies.get('Token');
	var id = Cookies.get('id');
	var url = API_BASE_URL + '/events/' + id;
	console.log(url)
	$("#result").text('');
	
	$.ajax({
		url : url,
		headers: {
        'X-Auth-Token':token,        
		}, 
		type : 'GET',
		crossDomain : true,
		dataType : 'json',
	}).done(function(data, status, jqxhr) {

		var event = data;

			$('<strong> Title: </strong>' + event.title +'<br>' ).appendTo($('#result'));
			$('<strong> Author: </strong> ' + event.description + '<br>').appendTo($('#result'));
			$('<strong> Difficulty: </strong> ' + event.date + '<br>').appendTo($('#result'));
			$('<strong> Ingredients: </strong> ' + event.category + '<br>').appendTo($('#result'));			
			
			
	}).fail(function() {
		$("#result").text("No files");
	}); */