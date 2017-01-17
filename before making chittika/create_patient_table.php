<?php
$servername = "localhost";
$username = "root";
$password = "";
$dbname = "medical_web";
$table = "patient_record";

// Create connection
$conn = new mysqli($servername, $username, $password, $dbname);
// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
} 

// sql to create table
$sql = "CREATE TABLE " .$table. "(
id INT(255) UNSIGNED AUTO_INCREMENT PRIMARY KEY, 
title VARCHAR(255) NOT NULL,
name VARCHAR(255) NOT NULL,
age VARCHAR(255) NOT NULL,
sex VARCHAR(255) NOT NULL,
weight VARCHAR(255) NOT NULL,
address VARCHAR(255) NOT NULL,
chiefComplaints VARCHAR(255) NOT NULL,
onExamination VARCHAR(255) NOT NULL,
assessment VARCHAR(255) NOT NULL,
provisionalDiagnosis VARCHAR(255) NOT NULL,
plan VARCHAR(255) NOT NULL,
reg_date TIMESTAMP
)";

if ($conn->query($sql) === TRUE) {
    echo "Table MyGuests created successfully";
} else {
    echo "Error creating table: " . $conn->error;
}

$conn->close();
?>