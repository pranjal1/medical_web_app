<?php

require_once('connection1.php');

function verify($data) {
	$data = trim($data);
	$data = stripslashes($data);
	$data = htmlspecialchars($data);
	
	return $data;
}

if($_SERVER["REQUEST_METHOD"] == "POST") {
	
	if(isset($_POST['title'])) {
		$case_title = verify($_POST['title']);



		$response['success'] = 'false';
		$response['msg'] = 'Nil';

		$query = "select * from " .$table. " where `title` = '" .$case_title. "';";
		$result = mysqli_query($con, $query);

		if(!$result) {
			$response['msg'] = "Error in Connection";
			echo json_encode($response);
			die("error loading case details " .mysqli_error($con));
		}

		else {
			if(mysqli_num_rows($result) >= 1) {
				$row = mysqli_fetch_assoc($result);

				
				$response['msg'] = 'Logged in Successfully';
				$response['title'] = $row['title'];
				$response['name'] = $row['name'];
				$response['age'] = $row['age'];
				$response['sex'] = $row['sex'];
				$response['weight'] = $row['weight'];
				$response['address'] = $row['address'];
				$response['chiefcomp'] = $row['chiefComplaints'];
				$response['onexam'] = $row['onExamination'];
				$response['assess'] = $row['assessment'];
				$response['prov'] = $row['provisionalDiagnosis'];
				$response['plan'] = $row['plan'];
				$response['success'] = true;
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
