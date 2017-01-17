<?php 

require('connection1.php');

function verify($data) {
	$data = trim($data);
	$data = stripslashes($data);
	$data = htmlspecialchars($data);
	
	return $data;
}

if($_SERVER["REQUEST_METHOD"] == "POST") {
	
	    $title = verify($_POST['title']);
		$name = verify($_POST['name']);
		$age = verify($_POST['age']);
		$sex = verify($_POST['sex']);
		$weight = verify($_POST['weight']);
		$address = verify($_POST['address']);
		$chief = verify($_POST['chief']);
		$vitals = verify($_POST['vitals']);
		$onexam = verify($_POST['onexam']);
		$assessment = verify($_POST['assessment']);
		$prov = verify($_POST['prov']);
		$plan = verify($_POST['plan']);
		
		
	
		
		$response['success'] = 'false';
		$response['tsuccess'] = 'true';
		
		
		
		$queryTitle = "select `title` from " .$table. " where `title` = '" .$title. "'";		
		$tResult = mysqli_query($con, $queryTitle);

		if(!$tResult) {
			$response['msg'] = "Error in Connection";
			echo json_encode($response);
			die("error authentication " .mysqli_error($con));
		}

		else {
			if(mysqli_num_rows($tResult) === 1) {
				$response['tsuccess'] = 'false';	
			} 
			
			if($response['tsuccess'] === 'false') {
				$response['success'] = 'false';
				$response['title_err'] = 'Title already registerd';
				echo json_encode($response);
			} else {
			
			 $insert = "INSERT INTO " .$table. "(title, name, age, sex, weight, address, chiefComplaints, onExamination, assessment, provisionalDiagnosis,plan)
			 VALUES ('".$title."', '".$name."', '".$age."','".$sex."', '".$weight."', '".$address."', '".$chief."', '".$onexam."', '".$assessment."', '".$prov."','".$plan."')";
	         $query = mysqli_query($con, $insert);
				
				if(!$query) {
					$response['msg'] = "Error in Insertion";
					echo json_encode($response);
				} else {
					$query = "select * from " .$table. " where `name` = '" .$name. "' AND `title` = '" .$title. "';";
					$result = mysqli_query($con, $query);
					$row = mysqli_fetch_assoc($result);
					$response['id'] = $row['id'];
					$response['success'] = 'true';
					$response['msg'] = 'Resgistered Successfully';
					echo json_encode($response);
				}
			}
		}

}
?>




