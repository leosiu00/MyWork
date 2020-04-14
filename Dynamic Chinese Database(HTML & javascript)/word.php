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
$target_table = 'base_2';
$query = "SELECT * FROM ". $target_table.";";


echo "<meta charset='UTF-8'>" ;


// Step 4. Execute the query
$result = mysql_query($query) or die( "Unable to execute query:".mysql_error());



// Step 5. Display the results

while($row = mysql_fetch_array($result, MYSQL_ASSOC))
{
    echo "<h1 id=".$row['Base']." class=\"words\" onclick=\"display_word_info(this);\">".$row['Base']."</h1>";
}



// Last step. Close the MySQL database connection
mysql_close();
?>

