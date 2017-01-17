<?php 

require('connection.php');

function verify($data) {
	$data = trim($data);
	$data = stripslashes($data);
	$data = htmlspecialchars($data);
	
	return $data;
}

if($_SERVER["REQUEST_METHOD"] == "POST") {

		$name = verify($_POST['name']);
		$email = verify($_POST['email']);
		$institution = verify($_POST['institution']);
		$phone = verify($_POST['phone']);
		$password = md5(verify($_POST['password']));	
		$pqr=array();
		$dummy_patient_id = serialize($pqr);
		$dummy_patient_title = serialize($pqr);
		$dummy_friend_id = serialize($pqr);
		$dummy_friend_name = serialize($pqr);
		
		$response['success'] = 'false';
		$response['esuccess'] = 'true';
		$response['usuccess'] = 'true';
		
		
		if (!filter_var($email, FILTER_VALIDATE_EMAIL)) {
  			$response['email_err'] = "Invalid email format"; 
			$response['esuccess'] = 'false';
			echo json_encode($response);
			die();
		}
		
		$queryEmail = "select `email` from " .$table. " where `email` = '" .$email. "'";		
		$eResult = mysqli_query($con, $queryEmail);

		$queryUser = "select `name` from " .$table. " where `name` = '" .$name. "'";
		$uResult = mysqli_query($con, $queryUser);

		if(!$eResult || !$uResult) {
			$response['msg'] = "Error in Connection";
			echo json_encode($response);
			die("error authentication " .mysqli_error($con));
		}

		else {
			if(mysqli_num_rows($eResult) === 1) {
				$response['esuccess'] = 'false';
				
				
			} 
			if(mysqli_num_rows($uResult) === 1) {
				$response['usuccess'] = 'false';
			
				
			} 
			
			if($response['esuccess'] === 'false' && $response['usuccess'] === 'false') {
				$response['success'] = 'false';
				$response['email_err'] = 'Email already registerd';
				$response['username_err'] = 'Username already used';
				echo json_encode($response);
			} else if($response['usuccess'] === 'false') {
				$response['success'] = 'false';
				$response['username_err'] = 'Username already used';
				echo json_encode($response);
			} else if($response['esuccess'] === 'false') {
				$response['success'] = 'false';
				$response['email_err'] = 'Email already registerd';
				echo json_encode($response);
			} else {
			
               $insert = "INSERT INTO " .$table. "(name, email, institution, phone, password, patient_id, patient_title,friend_id,friend_name)
			    VALUES ('".$name."', '".$email."', '".$institution."', '".$phone."', '".$password."', '".$dummy_patient_id."', '".$dummy_patient_title."', '".$dummy_friend_id."', '".$dummy_friend_name."')";
				$query = mysqli_query($con, $insert);
				
				if(!$query) {
					$response['msg'] = "Error in Insertion";
					echo json_encode($response);
				} else {
					
					$response['success'] = 'true';
					$response['msg'] = 'Resgistered Successfully';
					echo json_encode($response);
				}
			}
		}

}
?>




