/* var API_BASE_URL = "http://147.83.7.207:8080/eventsBCN"; */
var API_BASE_URL = "http://localhost:8080/eventsBCN";
/*--------------------------------------------------------------------------------------------------------------*/


$(document).ready(function(){
$("#button_createusu").on("click", function(e) {
	e.preventDefault();	
	console.log("estoy dentro funcion	"); 
	createUser();
});
	
});
$("#tipo").click(function(){
	var role_registered = document.getElementById("registered");
	var role_company = document.getElementById("company");

	if($("#registered").is(':checked')){
		console.log("estoy dentro");
		$('#myModal2').modal("show");
		
	}	
	if($("#company").is(':checked')){
		console.log("estoy fuerisima");
		$('#myModal3').modal("show");		
	} 
});
$("#siguiente").click(function(){
		$('#myModal3').modal("hide");		
		createUser2();	 
});
$("#AceptarEmpresa").click(function(){				
		createCompany();	 
});


function createCompany(){
	
	var role_registered = document.getElementById("registered");
	var role_company = document.getElementById("company");

	if($("#registered").is(':checked')){
		var role = 'registered';
		console.log("estoy dentro");
		
		
	}	
	if($("#company").is(':checked')){
		var role = 'companies';
		console.log("estoy fuerisima");
				
	} 
	
	objeto = {
        "companyname" : $('#companyname').val(),
        "description" : $('#description').val(),
        "localizacion" : $('#localizacion').val(),
        "latitud" : $('#latitud').val(),
		"longitud" : $('#longitud').val(),	
    }
	
		
	
	var data = JSON.stringify(objeto);
	console.log(data);

	$("#result").text('');

	$.ajax({
		url : API_BASE_URL + '/companies',
		type : 'POST',
		crossDomain : true,
		dataType : 'json',
		contentType:'application/vnd.dsa.eventsBCN.company+json',
		data : data,
		headers: {
        'X-Auth-Token':token,        
		}, 
	}).done(function(data, status, jqxhr) {
		console.log(data);
		Cookies.set('Token', data.token);
		Cookies.set('id', data.id);
		
		window.location.replace('Eventos.html');
		
		$('<div class="alert alert-success"> <strong>Ok!</strong> File Created</div>').appendTo($("#result"));
		$('<strong> Fecha: </strong> ' + data.name).appendTo($('#nombre')); 
		
	}).fail(function() {
		$('<div class="alert alert-danger"> <strong>Oh!</strong> Error </div>').appendTo($("#result"));
	});

	
}

function createUser() {
	
	var musica = document.getElementById("cat1");
	var teatro = document.getElementById("cat2");
	var cine = document.getElementById("cat3");
	var discoteca = document.getElementById("cat4");
	var bar = document.getElementById("cat5");
	if(musica.checked==false){
		musica.value=null;
	}	
	if(teatro.checked==false){
		teatro.value=null;
	}
	if(cine.checked==false){
		cine.value=null;
	}
	if(discoteca.checked==false){
		discoteca.value=null;
	}
	if(bar.checked==false){
		bar.value=null;
	}

	var role_registered = document.getElementById("registered");
	var role_company = document.getElementById("company");

	if($("#registered").is(':checked')){
		var role = 'registered';
		console.log("estoy dentro");
		
		
	}	
	if($("#company").is(':checked')){
		var role = 'companies';
		console.log("estoy fuerisima");
				
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
			discoteca.value,
			bar.value,
						
        ],  	
		
    }
	
		
	
	var data = JSON.stringify(objeto);
	console.log(data);

	$("#result").text('');

	$.ajax({
		url : API_BASE_URL + '/users?role=' + role,
		type : 'POST',
		crossDomain : true,
		dataType : 'json',
		contentType:'application/vnd.dsa.eventsBCN.user+json',
		data : data,
	}).done(function(data, status, jqxhr) {
		console.log(data);
		Cookies.set('Token', data.token);
		console.log(data.token);
		Cookies.set('id', data.id);
		
		window.location.replace('Eventos.html');
		
		$('<div class="alert alert-success"> <strong>Ok!</strong> File Created</div>').appendTo($("#result"));
		$('<strong> Fecha: </strong> ' + data.name).appendTo($('#nombre')); 
		
	}).fail(function() {
		$('<div class="alert alert-danger"> <strong>Oh!</strong> Error </div>').appendTo($("#result"));
	});

}

/*--------------------------------------------------------------------------------------------------------------*/
function createUser2() {
	
		objeto = {
        "name" : $('#name1').val(),
        "password" : $('#password1').val(),
        "email" : $('#email1').val(),
        "photo" : $('#photo1').val(),
        	
		
    }	
		
    
	var data = JSON.stringify(objeto);
	console.log(data);

	$("#result").text('');

	$.ajax({
		url : API_BASE_URL + '/users?role=registered',
		type : 'POST',
		crossDomain : true,
		dataType : 'json',
		contentType:'application/vnd.dsa.eventsBCN.user+json',
		data : data,
	}).done(function(data, status, jqxhr) {
		console.log(data);
		Cookies.set('Token', data.token);
		console.log(data.token);
		Cookies.set('id', data.id);
		$('<div class="alert alert-success"> <strong>Ok!</strong> File Created</div>').appendTo($("#result"));
		$('<strong> Fecha: </strong> ' + data.name).appendTo($('#nombre')); 
		
	}).fail(function() {
		$('<div class="alert alert-danger"> <strong>Oh!</strong> Error </div>').appendTo($("#result"));
	});

}

/*--------------------------------------------------------------------------------------------------------------*/

$( "#button_login" ).click(function(e) {
	event.preventDefault();
	login($("#login").val(), $("#password").val(), function(){
  	console.log("change");
  	window.location.replace('Eventos.html');
	
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

function loadAPI(complete){
	$.get(API_BASE_URL)
		.done(function(data){
			var api = linksToMap(data.links);
			sessionStorage["api"] = JSON.stringify(api);
			complete();
		})
		.fail(function(data){
		});
}

function login(login, password, complete){
	loadAPI(function(){
		var api = JSON.parse(sessionStorage.api);
		var uri = api.login.uri;
		$.post(uri,
			{
				login: login,
				password: password
			}).done(function(authToken){
				authToken.links = linksToMap(authToken.links);
				sessionStorage["auth-token"] = JSON.stringify(authToken);
				complete();
				$('Bienvenido, ' + login).appendTo($('#nombre'));
				$.cookie('token', authToken.token);
				/* Cookies.set('Token', authToken.token); */
				var token2 = $.cookie('token');				
				console.log(token2);
				console.log(login);
				console.log("segundo token" + authToken.token);
				window.location.replace('Eventos.html');
			}).fail(function(jqXHR, textStatus, errorThrown){
				var error = jqXHR.responseJSON;
				alert(error.reason);
			}
		);
	});
}

function logout(complete){
	var authToken = JSON.parse(sessionStorage["auth-token"]);
	var uri = authToken["links"]["logout"].uri;
	console.log(authToken.token);
	$.ajax({
    	type: 'DELETE',
   		url: uri,
    	headers: {
        	"X-Auth-Token":authToken.token
    	}
    }).done(function(data) { 
    	sessionStorage.removeItem("api");
    	sessionStorage.removeItem("auth-token");
    	complete();
  	}).fail(function(){});
}



				 
				
				
				
				