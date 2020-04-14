
<!DOCTYPE html><html>
<head>
<meta charset='UTF-8'>
<title>Input</title>


</head>
<body  align=center>
<h1>輸入資料</h1>
<form action="enter_data.php" method ="GET">
<?php
echo $_GET['describ'].": " ;
echo '<Input name="input_base"><br><br>' ;
echo '<Input name="base_table" type="hidden" value="'.$_GET['base'].'">' ;
echo '<Input name="describ" type="hidden" value="'.$_GET['describ'].'">' ;
echo '<Input type="Submit" value="下一步">';

?>

</form>
</body>
</html>



