var target_table;
var page;
function display_info(e){
	window.open("show_info.php?target="+e.id+"&target_table="+target_table);
}

$(document).ready(function(){
	
	$("#search").hide();
	$("#word").hide();
	$("#character").hide()
	$("#1").click(function() {
		page=1;
		$("#character").show();
		$("#word").hide();
		$("#search").hide();
		$("#edit").hide();
		target_table="base_1";
		$("#1").css({"background-color":"#ddd"});
		$("#2").css({"background-color":"white"});
		$("#3").css({"background-color":"white"});
		$("#4").css({"background-color":"white"});
		$("#5").css({"background-color":"white"});
		$.get("display.php?page="+page+"&target_table="+target_table,
			function(data,status){
				$("#character_show").html(data);
		});
		
	});
	$("#2").click(function() {
		page=1;
		$("#character").hide();
		$("#word").show();
		$("#search").hide();
		$("#edit").hide();
		target_table="base_2";
		$("#2").css({"background-color":"#ddd"});
		$("#1").css({"background-color":"white"});
		$("#3").css({"background-color":"white"});
		$("#4").css({"background-color":"white"});
		$("#5").css({"background-color":"white"});
		$.get("display.php?page="+page+"&target_table="+target_table,
			function(data,status){
				$("#word_show").html(data);
			});
	});
	
	$("#3").click(function() {
		$("#character").hide();
		$("#word").hide();
		$("#search").show();
		$("#edit").hide();
		$("#3").css({"background-color":"#ddd"});
		$("#1").css({"background-color":"white"});
		$("#2").css({"background-color":"white"});
		$("#4").css({"background-color":"white"});
		$("#5").css({"background-color":"white"});
		$.get("search_function.php",
			function(data,status){
				$(".search").html(data);
			});
	});
	
	$("#4").click(function() {
		$("#character").hide();
		$("#word").hide();
		$("#search").hide();
		$("#edit").show();
		$("#4").css({"background-color":"#ddd"});
		$("#1").css({"background-color":"white"});
		$("#2").css({"background-color":"white"});
		$("#3").css({"background-color":"white"});
		$("#5").css({"background-color":"white"});
		$.get("input_function.php",
			function(data,status){
				$(".edit").html(data);
			});
	});
	$("#5").click(function() {
		$("#edit").hide();
		$("#character").hide();
		$("#word").hide();
		$("#search").hide();
		$("#5").css({"background-color":"#ddd"});
		$("#1").css({"background-color":"white"});
		$("#2").css({"background-color":"white"});
		$("#3").css({"background-color":"white"});
		$("#4").css({"background-color":"white"});
		
	});
	
});


	