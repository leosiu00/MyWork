
<!DOCTYPE html><html>
<head>
<meta charset='UTF-8'>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
<style>
body {font-family: Arial;}

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
.tab button:hover,#add:hover{
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
#add{
	float:right;
}
.edit{
	display:none;
}
</style>



</head>
<body  align=center>

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

$target = $_GET['target'];
$target_table = $_GET['target_table'];
$query = "SELECT * FROM ". $target_table." WHERE Base = '".$target."';";
echo $query;

// Step 4. Execute the query
$result = mysql_query($query) or die( "Unable to execute query:".mysql_error());



// Step 5. Display the results


while($row = mysql_fetch_array($result, MYSQL_ASSOC))
{
    echo "<h1>".$row['Base']."</h1>";
}

//https://www.w3schools.com/howto/howto_js_tabs.asp
echo '<div class="tab">' ;
$query0 = "SELECT * FROM map WHERE Relation = '".$target_table."' AND Type='T';";
$result0 = mysql_query($query0) or die( "Unable to execute query:".mysql_error());
while($row0 = mysql_fetch_array($result0, MYSQL_ASSOC))
{
$query1 = "SELECT count(*) FROM ".$row0['Table_name']." WHERE Base = '".$target."';";
$result1 = mysql_query($query1) or die( "Unable to execute query:".mysql_error());
while($row1 = mysql_fetch_array($result1, MYSQL_ASSOC)){
   if ($row1['count(*)'] > 0)
   echo '<button class="tablinks" onclick="openTab(event,'."'".$row0['Describ']."'".')">'.$row0['Describ'].'</button>'." ";
}

}
echo "<button id='add'>ADD</button>";
echo '</div>' ; 
echo '<br>' ;

$result0 = mysql_query($query0) or die( "Unable to execute query:".mysql_error());
while($row0 = mysql_fetch_array($result0, MYSQL_ASSOC))
{
  echo '<div id="'.$row0['Describ'].'" class="tabcontent">' ;
 
$query1 = "SELECT * FROM ".$row0['Table_name']." WHERE Base = '".$target."';";
$query2 = "SELECT * FROM map WHERE Table_name = '".$row0['Table_name']."' AND Type='C';";
$result1 = mysql_query($query1) or die( "Unable to execute query:".mysql_error());

while($row1 = mysql_fetch_array($result1, MYSQL_ASSOC))
{
$result2 = mysql_query($query2) or die( "Unable to execute query:".mysql_error());
	while($row2 = mysql_fetch_array($result2, MYSQL_ASSOC))
	{
    		echo "<div id=".$row2['Describ'].">".$row2['Describ'].": </div>";
		echo "<div class='meaning' id=".$row1[$row2['Column_name']]." ><text>".$row1[$row2['Column_name']]."</text><button>修改</button></div>";
		echo "<div class = 'edit' id=".$row1[$row2['Column_name']]."><textarea></textarea><button >儲存</button></div >";
		
	}	
 echo "<br>" ;
}

echo "</div>";
}
echo "<div  id='option'>";
$query3="SELECT * FROM map WHERE Relation = '".$target_table."' AND Type='T';";
while($row0 = mysql_fetch_array($result0, MYSQL_ASSOC))
{
echo $row0['Describ'];

}
echo "</div>";

echo "</body>";

// Last step. Close the MySQL database connection
mysql_close();
?>
<script>
$('.meaning>button').click(function(e){

	var id=$(this).parent().attr('id');
	var meaning=".meaning#"+id;
	var text=".meaning#"+id+" text";
	var edit=".edit#"+id;
	var textarea= ".edit#"+id+" textarea";
	$(textarea).val($(text).text());
	$(meaning).hide();
	$(edit).show();
});

$('.edit>button').click(function(e){
	var id=$(this).parent().attr('id');
	var meaning=".meaning#"+id;
	var text=".meaning#"+id+" text";
	var edit=".edit#"+id;
	var textarea= ".edit#"+id+" textarea";
	$(text).text($(textarea).val());
	$(meaning).show();
	$(edit).hide();
	
});
/*
function save(e){
	alert(e.parent().attr('id'));
	
	var str1= ".edit"+e.id+" textarea"
	alert($(str1).val());
	var str2=".meaning#"+e.id;
	alert(str2);
	//alert($(str1).text());
	var str3 =".edit#"+e.id;
	var str4="button#"+e.id;
	$(str2).text($(str1).val());
	$(str2).show();
	$(str1).hide();
	$(str3).hide();
	
	
}*/
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
}
</script>
</html>



