/* var API_BASE_URL = "http://147.83.7.207:8080/eventsBCN"; */
var API_BASE_URL = "http://localhost:8080/eventsBCN";
var token = $.cookie('token');
var namecomp = $.cookie('namecomp');
var role = $.cookie('role');
var companyid = $.cookie('companyid');;
var empresa = new Object();
var fotoUsu;



/*--------------------------------------------------------------------------------------------------------------*/

$(document).ready(function(){	    
   
   console.log("a");
   
   	getUsuPorId();
	
	
	$("#Ajustes").click(function() {
		
		$.removeCookie('name');
		$.removeCookie('password');
		$.removeCookie('email');
		getUsuPorId();
		console.log($.cookie('name'));
		console.log($.cookie('password'));
			
			document.getElementById("name2").placeholder = $.cookie('name');			
			document.getElementById("email2").placeholder = $.cookie('email');
			/* document.getElementById("photo2").placeholder = $.cookie('imagen'); */
							
	});
	
	console.log("b");

	if (role == 'registered'){
		document.getElementById('CreateEvent').style.display = 'none';
		document.getElementById('DeleteEvent').style.display = 'none';
		console.log(role + "dentro del registered");
		getEventos();
		$("main").on("click", "article", function(e) {
			console.log(e);
			var arr = e.target.className.split(' ');
			eventid = arr[1];		
		$('#myModal5').modal('show');
		console.log("IDEVENTO ANTES: " +eventid);
		$.cookie('eventoid', eventid);
		
		getEvento(eventid);		
		console.log(eventid);
		$("#result10").text('');
		$("#result4").text('');
		});
		
		
		
		$("#nombre").click(function() {
			$.removeCookie('name');
			$.removeCookie('password');
			$.removeCookie('email');
			
			getUsuPorId();		
			
			window.location.replace('PerfilUsu.html');		
			
		});
				
	}
	else{
		getCompañiaporUserID();
		
		$("main").on("click", "article", function(e) {
			console.log(e);
			var arr = e.target.className.split(' ');
			var eventid = arr[1];		
		$('#myModal5').modal('show');
		$.cookie('eventoid', eventid);
		getEvento(eventid);	
		$("#result10").text('');
		$("#result4").text('');
		console.log($.cookie('eventoid'));
		
		});
		document.getElementById('Assist').style.display = 'none';
		
		$("#nombre").click(function() {
		
		$.removeCookie('title');
		$.removeCookie('description');
		$.removeCookie('email');
		getCompañiaporUserID();
		
		window.location.replace('PerfilEmpresa.html');		
			
	});
	}
 
	$(document).keydown(function(e){
		if(e.keyCode == 27)
			$('#myModal5').modal('hide');
		});
		
	$("#button_updateusu").click(function() {
		
		UpdateUser(); 
			
		});
		
	$("#button_CreateEvent").click(function() {
		
		createEvent();
			
		});	
		
		
	$("#DeleteEvent").click(function() {
		
		deleteEvent();
			
		});	
	
		


});

/*--------------------------------------------------------------------------------------------------------------*/

function getUsuPorId() {	
		
	var url = API_BASE_URL + '/users/' + $.cookie('userid');
	console.log(url);
		
	
	console.log("getusuporid");
	
	$.ajax({
		url : url,
		type : 'GET',
		crossDomain : true,
		headers: {
        'X-Auth-Token':token,        
		}, 		
	}).done(function(data, status, jqxhr) {
		console.log("preok");
		console.log(data);
		console.log("ok");
	
		console.log(data.email);
			$.cookie('name', data.name);
			$.cookie('password', data.password);
			$.cookie('email', data.email);			
			$.cookie('categorias', data.categories);
			
			
			console.log($.cookie('email'));		
				
				
			$('<article class="blog-item"><div class="row" id="columna"><div class="col-md-8"><img style="width: 100%;id="imagen" class="foto" src="'+data.photoURL+'" class="img-thumbnail center-block"'
			+'alt="Blog Post Thumbnail"></a></div>'
			+'<div class="col-md-4"><strong> Nombre: </strong> ' + data.name + '<br>' + '<strong> Email: </strong> ' + data.email + '<br>' + '<strong> Categorias: </strong> ' + data.categories + '<br>'			
			+'</div></div></article>').appendTo($('#result100')); 	
			
			$("#result500").text('');
			$("#result501").text('');
			
			$('<strong>Nombre: </strong>' + data.name + '<br>').appendTo($('#result500'));

			$('<img style="width: 60%;id="imagen" class="foto" src="'+ data.photoURL +'" class="img-thumbnail center-block" alt="Blog Post Thumbnail">').appendTo($('#result501'));
			

			fotoUsu = data.photoURL;
			
	}).fail(function() {
		
		console.log(error);
	});

}
 

 
/*--------------------------------------------------------------------------------------------------------------*/

function assisttoEvent(id, eventid) {
	
	var url = API_BASE_URL + '/companies/1sdf89dsf8/events/' + eventid +'/assist';
		
	$.ajax({
		url : url,
		type : 'POST',
		crossDomain : true,
		dataType : 'json',
		contentType:'application/vnd.dsa.eventsBCN.user+json',
		headers: {
        'X-Auth-Token':token,        
		},
		   		
	}).done(function(/* data, status, jqxhr */) {
		
		console.log("Asistiré");	
		
		 
	}).fail(function() {
		console.log("Error");		
	});
}


/*--------------------------------------------------------------------------------------------------------------*/

function wontassit(id, eventid) {
	
	var url = API_BASE_URL + '/companies/1sdf89dsf8/events/' + eventid +'/wontassist';
		
	$.ajax({
		url : url,
		type : 'DELETE',
		crossDomain : true,
		dataType : 'json',
		contentType:'application/vnd.dsa.eventsBCN.user+json',
		headers: {
        'X-Auth-Token':token,        
		},
		   		
	}).done(function(/* data, status, jqxhr */) {
		
		console.log("no Asistiré");	
		
		 
	}).fail(function() {
		console.log("Error");		
	});
}
	

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
			
			$('<article class="blog-item '+evento.id+'"><div class="row" id="columna"><div class="col-md-8"><img style="width: 100%;id="imagen" class="foto '+evento.id+'" src="'+evento.photoURL+'" class="img-thumbnail center-block"'
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
		var x = true;
		
			$('<strong> Titulo: </strong>' + evento.title +'<br>' ).appendTo($('#result1'));
			$('<strong> Descripción: </strong> ' + evento.description + '<br>').appendTo($('#result1'));
			$('<strong> Fecha: </strong> ' + evento.date + '<br>').appendTo($('#result1'));
			$('<strong> Categoria: </strong> ' + evento.category + '<br>').appendTo($('#result1'));			
			console.log("EVENTO: " + evento.assisted);
			console.log(evento.users);
			$('<img id = "imagenrevento" style="width: 70%" class="foto" src="'+evento.photoURL+'" class="img-thumbnail center-block" alt="Blog Post Thumbnail">').appendTo($('#result4'));
			
			if (evento.assisted == false){	
				console.log("no asisto");
				$("#Assist").text("ASISTIRÉ!");	
			}
			else{
				console.log("si asisto");
				$("#Assist").text("NO ASISTIRÉ!");	
			}
			
			$("#Assist").click(function() {
			console.log("IDEVENTO DESPUES: " +eventid);
			if ($("#Assist").text() == "NO ASISTIRÉ!"){
			$("#Assist").text("ASISTIRÉ!");		
			wontassit(id, eventid)	
			}else{
			$("#Assist").text("NO ASISTIRÉ!");	
			assisttoEvent(id, eventid);	
			}			
			});
			
			for(i=0; i<5; i++) {
				
				var persona = JSON.stringify(evento.users[i].name);
				console.log("STRINGIFY: " + persona);
				$('<strong> Asistente: </strong> ' + persona + '<br>').appendTo($('#result10'));
				
				

			} 
			
			console.log("pinto boton");
			
					
			$.cookie('companyid', evento.companyid);
			companyid = $.cookie('companyid');
			getCompañia(companyid);		
			
			
	}).fail(function() {
		$("#result").text("No files");
	});
} 

/*--------------------------------------------------------------------------------------------------------------*/

function getCompañia(companyid) {
	
	var url = API_BASE_URL + '/companies/' + companyid;
	console.log(url);
	$("#result2").text('');
	
	
	$.ajax({
		url : url,
		type : 'GET',
		crossDomain : true,
		dataType : 'json',
		contentType:'application/vnd.dsa.eventsBCN.companies+json',
		headers: {
        'X-Auth-Token':token,        
		}, 
	}).done(function(data, status, jqxhr) {

		var compañia = data;		
		console.log(compañia);
		$.cookie('namecomp', compañia.name);
		$('<strong> Evento creado por: </strong>' + compañia.name +'<br>' ).appendTo($('#result2'));
			
	}).fail(function() {
		$("#result").text("No files");
	});
} 

/*--------------------------------------------------------------------------------------------------------------*/

function getEventosCompañia(companyid) {
	
	var url = API_BASE_URL + '/companies/' + empresa.id + '/events';
	console.log(url);
	$("#result").text('');	
	
	$.ajax({
		url : url,
		type : 'GET',
		crossDomain : true,
		dataType : 'json',
		contentType:'application/vnd.dsa.event.event.collection+json',
		headers: {
        'X-Auth-Token':token,        
		}, 
	}).done(function(data, status, jqxhr) {

		var f = data.events ;				
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
			
			$('<article class="blog-item"><div class="row" id="columna"><div class="col-md-8"><img style="width: 100%;id="imagen" class="foto '+evento.id+'" src='+evento.photoURL+' class="img-thumbnail center-block"'
			+'alt="Blog Post Thumbnail"></a></div>'
			+'<div class="col-md-4"><h1 style="text-align:center;class="titulo" id="titulo"><strong> Titulo: </strong>' + evento.title +'<br>' 
			+'<strong> Fecha: </strong> ' + evento.date + '<br>'			
			+'</div></div></article>').appendTo($('#result')); 
		
			})
	}).fail(function() {
		$("#result").text("No files");
	});
} 

/*--------------------------------------------------------------------------------------------------------------*/

function getCompañiaporUserID() {
	
	var url = API_BASE_URL + '/companies?userid=' + $.cookie('userid');
	console.log(url);
	$("#result").text('');	
	
	$.ajax({
		url : url,
		type : 'GET',
		crossDomain : true,
		headers: {
        'X-Auth-Token':token,        
		}, 
	}).done(function(data, status, jqxhr) {

			 empresa.id = data.id;
			 console.log("ES ESTE!" + empresa.id);
			 empresa.name = data.name;
			 $.cookie('empresaid', empresa.id);
			getEventosCompañia(empresa.id);
			
			$('<article class="blog-item"><div class="row" id="columna"><div class="col-md-8"><img style="width: 100%;id="imagen" class="foto" src="'+fotoUsu+'" class="img-thumbnail center-block"'
			+'alt="Blog Post Thumbnail"></a></div>'
			+'<div class="col-md-4"><strong> Nombre: </strong> ' + data.name + '<br>' + '<strong> Descripción: </strong> ' + data.description + '<br>'		
			+'</div></div></article>').appendTo($('#result200')); 	
			
	}).fail(function() {
		$("#result").text("No files");
	});
} 

 /*--------------------------------------------------------------------------------------------------------------*/

function UpdateUser(){
	
	var url = API_BASE_URL + '/users/' + $.cookie('userid');
	
	var teatro = document.getElementById("cat10");
	var cine = document.getElementById("cat20");
	var discoteca = document.getElementById("cat30");
	var bar = document.getElementById("cat40");
    var formData = new FormData();

	if(teatro.checked==true){
		formData.append("categories", teatro.value);
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

	formData.append("name",$.cookie('name'));	   
    formData.append("email",$('#email2').val());	
	var objImagen = new Image();
	var img = new Image();
	img.src = fotoUsu;
	
	if ($("#inputFile3")[0].files[0] = null)
		formData.append("image",img[0].files[0]);
	else{
		formData.append("image",$("#inputFile3")[0].files[0]);
	}
    


	console.log(formData);

	$("#result").text('');
	
	$.ajax({
		url : url,
		type : 'PUT',		
		crossDomain : true,
		processData: false,      
	    //contentType: 'multipart/form-data',
	    contentType: false,
		data : formData,
		headers: {
        'X-Auth-Token':token,        
		},
	}).done(function(data, status, jqxhr) {
		
		console.log("Usuario modificado");
			$.removeCookie('name');			
			$.removeCookie('email');
			$.removeCookie('categorias');
		getUsuPorId();
		window.location.replace('PerfilUsu.html');
			
	}).fail(function() {
		$("#result").text("No files");
	});
	
}

 /*--------------------------------------------------------------------------------------------------------------*/	

 function createEvent(){
	
	if($("#bar").is(':checked')){
        console.log("categoria bar MOD");
		var categoria = document.getElementById("bar").value;
					
    }
    if($("#discoteca").is(':checked')){
        console.log("categoria discoteca MOD");
		var categoria = document.getElementById("discoteca").value;
    }
    if($("#restaurante").is(':checked')){
        console.log("categoria restaurante MOD");
        var categoria = document.getElementById("restaurante").value;

    }
    if($("#cine").is(':checked')){
        console.log("categoria cine MOD");
        var categoria = document.getElementById("cine").value;
    }

	console.log(categoria);
		
	var formData = new FormData();
	formData.append("title", $('#title').val());
	formData.append("description", $('#description').val());
	formData.append("date", $('#date').val());
    formData.append("image",$("#inputFile")[0].files[0]);
    formData.append("category",categoria);

	
	console.log($.cookie('empresaid'));

	$.ajax({
		url : API_BASE_URL + '/companies/' + $.cookie('empresaid') + '/events',
		type : 'POST',
        crossDomain : true,
        processData: false,
      //dataType : 'json',
        //contentType: 'application/vnd.dsa.eventsBCN.event+json',
        contentType: false,
        data : formData,
		headers: {
        'X-Auth-Token':token,        
		}, 
	}).done(function(data, status, jqxhr) {
		
		console.log(data);
		
		window.location.replace('Eventos.html');
		
		$('<div class="alert alert-success"> <strong>Ok!</strong> File Created</div>').appendTo($("#result"));
	
	}).fail(function() {
		$('<div class="alert alert-danger"> <strong>Oh!</strong> Error </div>').appendTo($("#result"));
	});

	
}
 /*--------------------------------------------------------------------------------------------------------------*/	

 
 function deleteEvent() {
	
	var url = API_BASE_URL + '/companies/' + $.cookie('empresaid') + '/events/' + $.cookie('eventoid');
	
	$.ajax({
		url : url,
		type : 'DELETE',
		/* crossDomain : true, */
		/* dataType : 'json', */
		/* contentType:'application/vnd.dsa.eventsBCN.event+json', */
		headers: {
        'X-Auth-Token':token,        
		},		   		
	}).done(function(data, status, jqxhr) {
		
		console.log("Elemento borrado");	
		window.location.replace('Eventos.html');
		 
	}).fail(function() {
		console.log("Error");		
	});
}