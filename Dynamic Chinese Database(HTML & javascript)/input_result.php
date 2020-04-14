
<!DOCTYPE html><html>
<head>
<meta charset='UTF-8'>
<title>Input</title>

</head>
<body  align=center>
<h1>輸入資料</h1>
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
$base_table = "" ;
$target_table = "" ;
$target = "" ;
$column_list = "" ;
$input_value = "" ;
$describ ="" ;
foreach ($_POST as $key => $value) { 

if ($key == 'target_table'){
  $target_table = $value;
}
elseif($key == 'base_table'){
  $base_table = $value ;
}
elseif($key == 'describ'){
  $describ = $value ;
}
elseif($key == 'input_base'){
  if ($value !=""){
    $target = $value ;
 }
}
elseif ($value !=""){
   if ($column_list != ""){
    $column_list = $column_list." , " ;
    $input_value = $input_value." , " ;
 }
   $column_list = $column_list.$key ;
   $input_value = $input_value."'".$value."'" ;
   
}
 
}
$query = "INSERT INTO ".$target_table."( Base, ".$column_list." ) VALUES( '".$target."' , ".$input_value." ) ;" ;

// Step 4. Execute the query
$result = mysql_query($query) or die( "Unable to execute query:".mysql_error());

// Step 5. Display the results
echo "已輸入資料到現有".$describ."資料庫" ;
echo "<br><br>";
echo '<a href="enter_data.php?input_base='.$target.'&base_table='.$base_table.'&describ='.$describ.'" >返回</a>' ;

echo "</body>";

// Last step. Close the MySQL database connection
mysql_close();
?>
</html>



