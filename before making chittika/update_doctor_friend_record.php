<?php 

require('connection.php');

function verify($data) {
	$data = trim($data);
	$data = stripslashes($data);
	$data = htmlspecialchars($data);
	
	return $data;
}

if($_SERVER["REQUEST_METHOD"] == "POST") {

	
		$doc_name = verify($_POST['d_name']);
		$fr_id =verify($_POST['f_id']);
		$fr_name =verify($_POST['f_name']);
		
	
		
		$query = "select * from " .$table. " where `name` = '" .$doc_name. "';";
		$result = mysqli_query($con, $query);
		$row = mysqli_fetch_assoc($result);
		
		$array_id = unserialize($row['friend_id']);
		$array_name = unserialize($row['friend_name']);
		$counter=0;
		
		foreach($array_id as $value){
		$counter++;
		}
		
		$array_id[$counter] = $fr_id;
		$id_insert = serialize($array_id);
		$counter=0;
		
		foreach($array_name as $value){
		$counter++;
		}
		
		$array_name[$counter] = $fr_name;
		
		$title_insert = serialize($array_name);

		$sql = "UPDATE " .$table. " SET friend_id= '" .$id_insert. "', friend_name= '" .$title_insert. "' WHERE name= '" .$doc_name. "'";
			
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




