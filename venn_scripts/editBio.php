<?php
$conn = new mysqli("localhost", "venn", "puffsucks", "venn_db_james");

//binding variables
$user_id = $_GET['user_id']; //id of the user blocking
$bio = $_GET['bio']; //id of person to block

if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

//update swipes
$stmt1 = $conn->prepare("UPDATE users set bio=? WHERE user_id=?");

$stmt1->bind_param("ss", $bio, $user_id);
$stmt1->execute();
$stmt1->close();
$conn->close();
?>
