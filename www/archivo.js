var API_BASE_URL = "http://localhost:8080/eventsBCN";

$("#button_get_todos").click(function(e) {
	e.preventDefault();
	getEventos();
});

function getEventos() {
	var url = API_BASE_URL + '/list';
	$("#result").text('');

	$.ajax({
		url : url,
		type : 'GET',
		crossDomain : true,
		dataType : 'json',
	}).done(function(data, status, jqxhr) {
		var f = data.eventos;

		$.each(f, function(i, v) {
			var evento = v;
			$('<strong> Titulo: </strong>' + evento.title +'<br>' ).appendTo($('#result'));
			$('<strong> Fecha: </strong> ' + evento.fecha + '<br>').appendTo($('#result'));
			$('<strong> Lugar: </strong> ' + evento.lugar + '<br>').appendTo($('#result'));
			$('<strong> Empresa: </strong> ' + evento.empresa + '<br>').appendTo($('#result'));
			
		});
	}).fail(function() {
		$("#result").text("No files");
	});

}

/*--------------------------------------------------------------------------------------------------------------*/

$("#button_createUsu").click(function(e) {
	e.preventDefault();

	var newUser = new Object();
	newUser.name = $("#name").val();
	newUser.password = $("#password").val();
	newUser.email = $("#email").val();
	newUser.photo =$("#photo").val();
	newUser.catefories =$ ("#categories").val();
	
	createUser(newUser);
});

function createUser(dades) {
	var url = API_BASE_URL;
	var data = JSON.stringify(dades);
	console.log(dades);

	$("#result").text('');

	$.ajax({
		url : url,
		type : 'POST',
		crossDomain : true,
		dataType : 'json',
		contentType:'application/json',
		data : data,
	}).done(function(data, status, jqxhr) {
			
		$('<div class="alert alert-success"> <strong>Ok!</strong> File Created</div>').appendTo($("#result"));
		
	}).fail(function() {
		$('<div class="alert alert-danger"> <strong>Oh!</strong> Error </div>').appendTo($("#result"));
	});

}

