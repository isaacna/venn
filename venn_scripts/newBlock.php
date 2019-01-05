<?php
$conn = new mysqli("localhost", "venn", "puffsucks", "venn_db_james");

//binding variables
$user_id = $_GET['user_id']; //id of the user blocking
$block_id = $_GET['block_id']; //id of person to block

if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

//update swipes
$stmt1 = $conn->prepare("INSERT INTO blocks (blocker, blockee) VALUES (?,?)");

$stmt1->bind_param("ss", $user_id, $block_id);
$stmt1->execute();
$stmt1->close();
$conn->close();
?>
