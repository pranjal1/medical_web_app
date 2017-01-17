<?php

require_once('connection.php');

function verify($data) {
	$data = trim($data);
	$data = stripslashes($data);
	$data = htmlspecialchars($data);
	
	return $data;
}

if($_SERVER["REQUEST_METHOD"] == "POST") {
	
	if(isset($_POST['d_name'])) {
		$name = verify($_POST['d_name']);


		$response=array();

		$query = "select * from " .$table. " where `name` = '" .$name. "';";
		$result = mysqli_query($con, $query);
		$row = mysqli_fetch_assoc($result);
			
		$response=unserialize($row['patient_title']);
				
	    echo json_encode($response);
			
		
			
		
	}
}
mysqli_close($con);

?>
