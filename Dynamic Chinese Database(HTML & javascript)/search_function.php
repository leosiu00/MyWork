
<!DOCTYPE html><html>
<head>
<meta charset='UTF-8'>
<title>Search</title>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
<style>


/* Style the tab */
.tab {
    overflow: hidden;
    border: 1px solid #ccc;
    background-color: #f1f1f1;
}

/* Style the buttons inside the tab */
.tab button {
    background-color: inherit;
    float: left;
    border: none;
    outline: none;
    cursor: pointer;
    padding: 14px 16px;
    transition: 0.3s;
    font-size: 17px;
}

/* Change background color of buttons on hover */
.tab button:hover {
    background-color: #ddd;
}

/* Create an active/current tablink class */
.tab button.active {
    background-color: #ccc;
}

/* Style the tab content */
.tabcontent {
    display: none;
    padding: 6px 12px;
    border: 1px solid #ccc;
    border-top: none;
}
</style>


</head>
<body  align=center>
<h1>搜尋</h1>
<?php

$host = "sophia"; // In CS department, the database is located in a machine called sophia.
$username="fyp17033"; // Your CSID.
$password="EgVRjpRi";  // Your MySQL password.
$database="fyp17033"; // In CS department, we create a database for you with name equal to your CSID.

// Step 1. Connect to database server
$link = mysql_connect($host,$username,$password);
mysql_set_charset('utf8',$link);
// Step 2. Select the database we work on
mysql_select_db($database) or die( "Unable to select database");

// Step 3. Prepare the database query
$target = '好';
$target_table = 'base_1';
$query = "SELECT * FROM map WHERE Type ='B';";

// Step 4. Execute the query
$result = mysql_query($query) or die( "Unable to execute query:".mysql_error());

// Step 5. Display the results

//https://www.w3schools.com/howto/howto_js_tabs.asp
echo '<div class="tab">' ;

while($row = mysql_fetch_array($result, MYSQL_ASSOC))
{
   echo '<button class="tablinks" onclick="openTab(event,'."'".$row['Table_name']."'".')">'.$row['Describ'].'</button>'." ";
}
echo '</div>' ; 
echo '<br>' ;


$query0 = "SELECT * FROM map WHERE Type ='B';";
$result0 = mysql_query($query0) or die( "Unable to execute query:".mysql_error());
while($row0 = mysql_fetch_array($result0, MYSQL_ASSOC))
{
  
  echo '<div id="'.$row0['Table_name'].'" class="tabcontent">' ;
  echo '<form id="'.$row0['Table_name'].'" action="search_result.php" onsubmit="return validateForm(this)" method="post" target="_blank">' ;
  echo '<input type="hidden" value="'.$row0['Table_name'].'" name="base_name" >'; 
  echo $row0['Describ']."<br>" ;
  echo '<input type="text" name="Base"><br><br>';
  $query1 = "SELECT * FROM map WHERE Type='T' AND Relation = '".$row0['Table_name']."' ;";
  $result1 = mysql_query($query1) or die( "Unable to execute query:".mysql_error());
while($row1 = mysql_fetch_array($result1, MYSQL_ASSOC))
{
  echo $row1['Describ'] ."<br>";
  $query2 = "SELECT * FROM map WHERE Table_name = '".$row1['Table_name']."' AND Type='C';";
  $result2 = mysql_query($query2) or die( "Unable to execute query:".mysql_error());
	while($row2 = mysql_fetch_array($result2, MYSQL_ASSOC))
	{
    		echo $row2['Describ'].": ";
                echo '<input type="text" name="'.$row1['Table_name']."/".$row2['Column_name'].'"><br><br>';
	}	
 echo "<br>" ;
}
echo '<Input type="submit" value="搜尋" >' ;
echo '</form>' ;
echo "</div>";

}
echo "</body>";

// Last step. Close the MySQL database connection
mysql_close();
?>
<script>
var search_base;
$(document).ready(function() {
    $("#"+search_base+" :text").click(function(){
	
		//alert($("#base_1 :input[type=text]").length);
	});
});
function validateForm(e){
	var test_length=0;
	$("#"+search_base+" :input[type=text]").each(function(){
		if($(this).val()=="")	
			test_length++;
	});
	if(test_length==$("#"+search_base+" :input[type=text]").length){
		alert('請輸入資料');
		return false;
	}
		
}
$("input[type=text]").click(function(){
	$(this).val("");
});
	

function openTab(evt, tabName) {
    // Declare all variables
    var i, tabcontent, tablinks;

    // Get all elements with class="tabcontent" and hide them
    tabcontent = document.getElementsByClassName("tabcontent");
    for (i = 0; i < tabcontent.length; i++) {
        tabcontent[i].style.display = "none";
    }

    // Get all elements with class="tablinks" and remove the class "active"
    tablinks = document.getElementsByClassName("tablinks");
    for (i = 0; i < tablinks.length; i++) {
        tablinks[i].className = tablinks[i].className.replace(" active", "");
    }

    // Show the current tab, and add an "active" class to the button that opened the tab
    document.getElementById(tabName).style.display = "block";
    evt.currentTarget.className += " active";
	
	search_base=tabName;
}
</script>
</html>



