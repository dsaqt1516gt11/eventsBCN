/* var API_BASE_URL = "http://147.83.7.207:8080/eventsBCN"; */
var API_BASE_URL = "http://localhost:8080/eventsBCN";
/*--------------------------------------------------------------------------------------------------------------*/
var token = $.cookie('token');

/*--------------------------------------------------------------------------------------------------------------*/

$(document).ready(function(){
	
	
});

/*--------------------------------------------------------------------------------------------------------------*/

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
$("#button_createusu").on("click", function(e) {
	e.preventDefault();	
	console.log("estoy dentro funcion"); 
	createUser();
});


/*--------------------------------------------------------------------------------------------------------------*/

function createCompany(){

	objeto = {
        "name" : $('#companyname').val(),
        "description" : $('#description').val(),
        "location" : $('#calle').val(),
        "latitude" : parseFloat(document.getElementById("coords1").value),
		"longitude" : parseFloat(document.getElementById("coords1").value),
    }



	var data = JSON.stringify(objeto);
	console.log(data);

	$("#result").text('');

	console.log($.cookie('token'));

	$.ajax({
		url : API_BASE_URL + '/companies',
		type : 'POST',
		crossDomain : true,
		dataType : 'json',
		contentType:'application/vnd.dsa.eventsBCN.company+json',
		data : data,
		headers: {
        'X-Auth-Token':$.cookie('token'),
		},
	}).done(function(data, status, jqxhr) {
		console.log(data);
		
		window.location.replace('Eventos.html');

		$('<div class="alert alert-success"> <strong>Ok!</strong> Company Created</div>').appendTo($("#result"));

	}).fail(function() {
		$('<div class="alert alert-danger"> <strong>Oh!</strong> Error </div>').appendTo($("#result"));
	});


}

/*--------------------------------------------------------------------------------------------------------------*/

function createUser() {

	$("#nombre").text('');

	var restaurante = document.getElementById("cat1");
	var cine = document.getElementById("cat2");
	var discoteca = document.getElementById("cat3");
	var bar = document.getElementById("cat4");
    var formData = new FormData();

	if(restaurante.checked==true){
		formData.append("categories", restaurante.value);
				console.log("LERDOOOOO");
	}
	if(cine.checked==true){
	    formData.append("categories", cine.value);
	}
	if(discoteca.checked==true){
		formData.append("categories", discoteca.value);
	}
	if(bar.checked==true){
        formData.append("categories", bar.value);
	}

	var role_registered = document.getElementById("registered");
	var role_company = document.getElementById("company");

	if($("#registered").is(':checked')){
		var role = 'registered';
		console.log("estoy dentro");
	}
	if($("#company").is(':checked')){
		var role = 'company';
		console.log("estoy fuerisima");

	}

	formData.append("name",$('#name').val());
    formData.append("password",$('#password').val());
    formData.append("email",$('#email').val());
    formData.append("image",$("#inputFile")[0].files[0]);


	console.log(formData);

	$("#result").text('');

	$.ajax({
		url : API_BASE_URL + '/users?role=registered',
		type : 'POST',
		crossDomain : true,
		processData: false,
      //dataType : 'json',
	    //contentType: 'multipart/form-data',
	    contentType: false,
		data : formData,
	}).done(function(data, status, jqxhr) {
		console.log(data);
		$.cookie('token', data.token);
		$.cookie('userid', data.userid);
		$.cookie('role', data.role);
		
		$.removeCookie('name');
		$.removeCookie('password');
		$.removeCookie('email');
		
		
		
		$('#nombre').text('Bienvenido, ' + $.cookie('name'));
		$.cookie('password', $('#password').val());
		$.cookie('email', $('#email').val());
		$.cookie('name', $('#name').val());
		
		
		window.location.replace('Eventos.html');

		
		$('<strong> Bienvenido, </strong>' + data.name).appendTo($('#result5'));

	}).fail(function() {
		$('<div class="alert alert-danger"> <strong>Oh!</strong> Error </div>').appendTo($("#result"));
	});

}

/*--------------------------------------------------------------------------------------------------------------*/
function createUser2() {

       $("#nombre").text('');

       var formData = new FormData();
       formData.append("name",$('#name1').val());
       formData.append("password",$('#password1').val());
       formData.append("email",$('#email1').val());
       formData.append("image",$("#inputFile2")[0].files[0]);
       formData.append("categories","bar");


       	$("#result").text('');

       	$.ajax({
       		url : API_BASE_URL + '/users?role=company',
            type : 'POST',
            crossDomain : true,
            processData: false,
          //dataType : 'json',
            //contentType: 'multipart/form-data',
            contentType: false,
            data : formData,
       	}).done(function(data, status, jqxhr) {
       		console.log(data);

			$.cookie('role', data.role);
       		$.cookie('token', data.token);       		
       		$.cookie('userid', data.userid);
       		var id = $.cookie('id');
       		       		
       		$.removeCookie('name');
			$.removeCookie('password');
			$.removeCookie('email');
		
       		$.cookie('name', $('#name1').val());
       		$.cookie('password', $('#password1').val());
       		$.cookie('email', $('#email1').val());
       		
       		$('#nombre').text('Bienvenido, ' + $.cookie('name'));
			
		       		
       	}).fail(function() {
       		$('<div class="alert alert-danger"> <strong>Oh!</strong> Error </div>').appendTo($("#result"));
       	});

       }

       var id = $.cookie('id');


/*--------------------------------------------------------------------------------------------------------------*/

$( "#button_login" ).click(function(e) {
	event.preventDefault();
	login($("#login").val(), $("#passwordLogin").val(), function(){
  	console.log("change");
  	window.location.replace('Eventos.html');	
  }); 	
  });
  
/*--------------------------------------------------------------------------------------------------------------*/

$( "#button_logout" ).click(function(e) {
	event.preventDefault();
	logout();
  	console.log("change");
  	window.location.replace('index.html');	
   	
  });

/*--------------------------------------------------------------------------------------------------------------*/
  
function linksToMap(links){
	var map = {};
	$.each(links, function(i, link){
		$.each(link.rels, function(j, rel){
			map[rel] = link;
		});
	});

	return map;
}

/*--------------------------------------------------------------------------------------------------------------*/

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

/*--------------------------------------------------------------------------------------------------------------*/

function login(login, password, complete){
	
	loadAPI(function(){
		var api = JSON.parse(sessionStorage.api);
		var uri = api.login.uri;
		$("#nombre").text('');
		$.post(uri,
			{
				login: login,
				password: password
			}).done(function(authToken){
				console.log("estoy dentro del login");
				authToken.links = linksToMap(authToken.links);
				sessionStorage["auth-token"] = JSON.stringify(authToken);
				complete();		
				
				$.cookie('token', authToken.token);				
				$.cookie('role', authToken.role);
				$.cookie('userid', authToken.userid);
				
				$.removeCookie('name');
				$.cookie('name', login);
				
				$('#nombre').text('Bienvenido, ' + $.cookie('name'));
														
				window.location.replace('Eventos.html');	

				
			}).fail(function(jqXHR, textStatus, errorThrown){
				var error = jqXHR.responseJSON;
				alert(error.reason);
			}
		);
	});
}


/*--------------------------------------------------------------------------------------------------------------*/

function logout(){
	
	var url = API_BASE_URL + '/login';
	console.log(token);
	$.ajax({
    	type: 'DELETE',
   		url: url,
    	headers: {
        	"X-Auth-Token":token,
    	}
    }).done(function(data) { 
    	console.log("logout hecho");
		
  	}).fail(function(){});
}




				
				
				
				
				