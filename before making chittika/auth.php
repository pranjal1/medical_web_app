<?php

require_once('connection.php');

function verify($data) {
	$data = trim($data);
	$data = stripslashes($data);
	$data = htmlspecialchars($data);
	
	return $data;
}

if($_SERVER["REQUEST_METHOD"] == "POST") {
	
	if(isset($_POST['name']) && isset($_POST['email']) && isset($_POST['password'])) {
		$name = verify($_POST['name']);
		$password = md5(verify($_POST['password']));
		$email = verify($_POST['email']);


		$response['success'] = 'false';
		$response['msg'] = 'Nil';

		$query = "select * from " .$table. " where `name` = '" .$name. "' AND `email` = '" .$email. "' AND `password` = '" .$password. "';";
		$result = mysqli_query($con, $query);

		if(!$result) {
			$response['msg'] = "Error in Connection";
			echo json_encode($response);
			die("error authentication " .mysqli_error($con));
		}

		else {
			if(mysqli_num_rows($result) >= 1) {
				$row = mysqli_fetch_assoc($result);

				
				$response['msg'] = 'Logged in Successfully';
				$response['Name'] = $row['name'];
				$response['Email'] = $row['email'];
				$response['Institute'] = $row['institution'];
				$response['Phone'] = $row['phone'];
				$response['success'] = "true";
				echo json_encode($response);
			
		
			} else {
				$response['msg'] = "Error in Authentication";
				echo json_encode($response);
			}
		}
	}
}
mysqli_close($con);

?>
