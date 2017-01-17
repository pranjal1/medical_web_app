<?php

require_once('connection.php');

$sql = "SELECT name, institution FROM ".$table."";
$result = mysqli_query($con, $sql);
$storeArray = Array();

while ($row = mysqli_fetch_array($result, MYSQL_ASSOC)) {
    $storeArray[] =  $row['name'];
	$storeArray[] =  $row['institution'];

}
echo json_encode($storeArray);
mysqli_close($con);

?>