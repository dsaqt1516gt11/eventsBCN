var API_BASE_URL = "http://localhost:8080/eventsBCN";


$("#button_get_todos").click(function(e) {
	e.preventDefault();
	getEventos();
});

function getEventos() {
	var url = API_BASE_URL + '/events';
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



$("#button_createusu").click(function(e) {
	e.preventDefault();	
	var url = API_BASE_URL + '/users';   
	createUser(url);
});

function createUser(url) {
var musica = document.getElementById("cat1");
var teatro = document.getElementById("cat2");
var cine = document.getElementById("cat3");
if(musica.checked==false){
	musica.value=null;
}	
if(teatro.checked==false){
	teatro.value=null;
}
if(cine.checked==false){
	cine.value=null;
}

	
					
		
		objeto = {
        "name" : $('#name').val(),
        "password" : $('#password').val(),
        "email" : $('#email').val(),
        "photo" : $('#photo').val(),
        "categories" : [
            musica.value,
			teatro.value,
			cine.value,
          
						
        ],  
		
		
    }
	
		
	
	var data = JSON.stringify(objeto);
	console.log(data);

	$("#result").text('');

	$.ajax({
		url : url,
		type : 'POST',
		crossDomain : true,
		dataType : 'json',
		contentType:'application/vnd.dsa.eventsBCN.user+json',
		data : data,
	}).done(function(data, status, jqxhr) {
		
		$('#myModal2').modal('hide') 
		$('#myModal3').modal('hide')
		$('#myModal4').modal('show')		
			
		$('<div class="alert alert-success"> <strong>Ok!</strong> File Created</div>').appendTo($("#result"));
		
	}).fail(function() {
		$('<div class="alert alert-danger"> <strong>Oh!</strong> Error </div>').appendTo($("#result"));
	});

}

/*--------------------------------------------------------------------------------------------------------------*/

$( "#button_login" ).click(function(e) {
  e.preventDefault();
  var url = API_BASE_URL + '/login';
  login($("#name1").val(), $("#password1").val(), function(){
  	console.log("change");
  	window.location.replace('Eventos.html');
  });
});

function login(loginid, password, complete){
	loadAPI(function(){
		var api = JSON.parse(sessionStorage.api);
		var uri = api.login.uri;
		$.post(uri,
			{
				login: loginid,
				password: password
			}).done(function(authToken){
				authToken.links = linksToMap(authToken.links);
				sessionStorage["auth-token"] = JSON.stringify(authToken);
				complete();
			}).fail(function(jqXHR, textStatus, errorThrown){
				var error = jqXHR.responseJSON;
				alert(error.reason);
			}
		);
	});
}

/*--------------------------------------------------------------------------------------------------------------*/
/*  $("#tipo").click(function(e) {
	e.preventDefault();
	getTipoUsu();
});

function getTipoUsu() {
	
	var elementos = document.getElementsByName("tipousu");
			for(var i=0; i<elementos.length; i++) {
					if(elementos[0] == true)
						$('#myModal1').modal('hide') && $('#myModal2').modal('show');
					else
						$('#myModal1').modal('hide') && $('#myModal3').modal('show');
										 
				}
				
}  */



				 
				
				
				
				