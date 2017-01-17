<?php
$servername = "localhost";
$username = "root";
$password = "";
$dbname = "medical_web";
$table = "doctors_info";

// Create connection
$conn = new mysqli($servername, $username, $password, $dbname);
// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
} 

// sql to create table
$sql = "CREATE TABLE " .$table. "(
id INT(255) UNSIGNED AUTO_INCREMENT PRIMARY KEY, 
name VARCHAR(255) NOT NULL,
email VARCHAR(255) NOT NULL,
institution VARCHAR(255) NOT NULL,
phone VARCHAR(255) NOT NULL,
password VARCHAR(255) NOT NULL,
friend_id VARCHAR(255) NOT NULL,
friend_name VARCHAR(255) NOT NULL,
patient_id VARCHAR(255) NOT NULL,
patient_title VARCHAR(255) NOT NULL,
reg_date TIMESTAMP
)";

if ($conn->query($sql) === TRUE) {
    echo "Table MyGuests created successfully";
} else {
    echo "Error creating table: " . $conn->error;
}

$conn->close();
?>