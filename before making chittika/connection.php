<?php

$hostname='localhost';
$user='root';
$pass='';
$dbname='medical_web';
$table='doctors_info';

$con = mysqli_connect($hostname, $user, $pass, $dbname);
if(mysqli_connect_error($con))
	die('Error in connection' .mysqli_error($con));
