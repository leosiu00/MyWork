
<!DOCTYPE html><html>
<head>
<meta charset='UTF-8'>
<title>Search</title>
<style>
	.tab button:hover,.characters:hover,.words:hover {
		background-color: #ddd;
	}
</style>
</head>
<body  align=center>
<h1>搜尋結果</h1>
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
$target_table = "" ;
$condition = "" ;
foreach ($_POST as $key => $value) { 

	if ($key == 'base_name'){
	  $target_table = $value;
	}
	elseif($key == 'Base'){
		if ($value !=""){
			$condition = $condition." Base like '%".$value."%' " ;
	 }
	}
	elseif ($value !=""){
		if ($condition != ""){
			$condition = $condition." AND " ;
	 }
	   $sub_query = "SELECT Base FROM ".substr($key,0,strpos($key,"/"))." WHERE ". substr($key,strpos($key,"/")+1)." like '%".$value."%' ";
	   $condition = $condition." Base in (".$sub_query.") " ;
	}
 
}



$query = "SELECT * FROM ".$target_table." WHERE ".$condition." ;";

// Step 4. Execute the query
$result = mysql_query($query) or die( "Unable to execute query:".mysql_error());

// Step 5. Display the results
if(mysql_num_rows($result)==0){
	echo '找不到相關結果';
}else{
	while($row = mysql_fetch_array($result, MYSQL_ASSOC))
	{
		 echo "<h1 id=".$row['Base']." class=\"words\" onclick=\"display_info(this);\">".$row['Base']."</h1>";
	}
		echo '<br>' ;
	}



echo "</body>";

// Last step. Close the MySQL database connection
mysql_close();
?>
</html>
<script>
target_table="<?php echo $target_table?>";
function display_info(e){
	window.open("show_info.php?target="+e.id+"&target_table="+target_table);
}
</script>


