
<!DOCTYPE html><html>
<head>
<meta charset='UTF-8'>
<title>Input</title>


</head>
<body  align=center>
<h1>加入新資料</h1>
<?php


if ($_GET['type'] == 'addcolumn'){
echo '<form id="Input" action="input_sql.php" method="POST" >' ;
echo '<Input type="hidden" name="commandtype" value="'.$_GET['type'].'">';
echo '<Input type="hidden" name="target" value="'.$_GET['target'].'">';
echo '<Input type="hidden" name="base" value="'.$_GET['base'].'">';
echo '<table> <tr>';
echo "<td>資料類型</td>" ;
echo "<td>儲存的項目是...</td>" ;
echo "</tr><tr>" ;
echo '<td><select name="datatype">' ;
echo '<option value="text">文字 </option>';
echo '<option value="number">數字 </option>';
echo '</select></td>';
echo '<td><Input type="text" name="describ"></td>' ;
echo "</tr></table>";
echo '<br><Input type="Submit" value="加入" >';
} elseif ($_GET['type'] == 'addtable'){
echo '<form id="Input" action="input_sql.php" method="POST" >' ;
echo '<Input type="hidden" name="commandtype" value="'.$_GET['type'].'">';
echo '<Input type="hidden" name="base" value="'.$_GET['base'].'">';
echo '<table> <tr>';
echo "<td>新欄目的標題是...</td>" ;
echo "</tr><tr>" ;
echo '<td><Input type="text" name="describ"></td>' ;
echo "</tr></table>";
echo '<br><Input type="Submit" value="加入" >';

}

echo "</form>";
echo "</body>";


?>
</html>



