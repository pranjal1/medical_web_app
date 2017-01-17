<?php 

require('connection.php');

function verify($data) {
	$data = trim($data);
	$data = stripslashes($data);
	$data = htmlspecialchars($data);
	
	return $data;
}

if($_SERVER["REQUEST_METHOD"] == "POST") {

		$pat_id = verify($_POST['id']);
		$pat_title = verify($_POST['title']);
		$doc_name = verify($_POST['doc_name']);
		$doc_email = verify($_POST['doc_email']);
	
		$query = "select * from " .$table. " where `name` = '" .$doc_name. "' AND `email` = '" .$doc_email. "';";
		$result = mysqli_query($con, $query);
		$row = mysqli_fetch_assoc($result);
		
		$array_id = unserialize($row['patient_id']);
		$array_name = unserialize($row['patient_title']);
		$counter=0;
		
		foreach($array_id as $value){
		$counter++;
		}
		
		$array_id[$counter] = $pat_id;
		$id_insert = serialize($array_id);
		$counter=0;
		
		foreach($array_name as $value){
		$counter++;
		}
		
		$array_name[$counter] = $pat_title;
		
		$title_insert = serialize($array_name);

		$sql = "UPDATE " .$table. " SET patient_id= '" .$id_insert. "', patient_title= '" .$title_insert. "' WHERE name= '" .$doc_name. "'";
			
		if (mysqli_query($con, $sql)) {
			$response['success'] = true;
		$response['msg']='record is created';
        echo json_encode($response);
         } else {
		$response['success'] = false;
        $response['msg']='record is not created';
        echo json_encode($response);
        }
}
?>




