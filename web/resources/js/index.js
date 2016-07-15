
     $(".email-Estudiante").hide();
	 $(".email-Padre").hide();
	 
$("#Estudiante-box-link").click(function(){
  $(".email-Docente").fadeOut(100); 
  $(".email-Padre").fadeOut(100);
  $(".email-Estudiante").delay(100).fadeIn(100);
  
  $("#Docente-box-link").removeClass("active");
  $("#Estudiante-box-link").addClass("active");
  $("#Padre-box-link").removeClass("active");
  
});
$("#Padre-box-link").click(function(){
  $(".email-Docente").fadeOut(100);
  $(".email-Estudiante").fadeOut(100);
  $(".email-Padre").delay(100).fadeIn(100);
   
  $("#Docente-box-link").removeClass("active");
  $("#Estudiante-box-link").removeClass("active");
  $("#Padre-box-link").addClass("active");
  
});

$("#Docente-box-link").click(function(){
	 $(".email-Estudiante").fadeOut(100);
  $(".email-Padre").fadeOut(100);
  $(".email-Docente").delay(100).fadeIn(100);
  
  $("#Docente-box-link").addClass("active");
  $("#Estudiante-box-link").removeClass("active");
  $("#Padre-box-link").removeClass("active");
 

});