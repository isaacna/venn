<?php
$conn = new mysqli("localhost", "venn", "puffsucks", "venn_db_james");


$user_id = $_GET['user_id'];

if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}


// Query 1 - getting list of communities
$stmt1 = $conn->prepare( "DELETE FROM messages where sender=? or receiver=?");
$stmt1->bind_param("ss", $user_id, $user_id);
$stmt1->execute();
$stmt1->close();

$stmt2 =$conn->prepare("DELETE FROM swipes where user_1_id=? or user_2_id=?");
$stmt2->bind_param("ss", $user_id, $user_id);
$stmt2->execute();
$stmt2->close();

$stmt3 =$conn->prepare("DELETE FROM memberships where user_id=?");
$stmt3->bind_param("s", $user_id);
$stmt3->execute();
$stmt3->close();

$stmt4 =$conn->prepare("DELETE FROM users where user_id=?");
$stmt4->bind_param("s", $user_id);
$stmt4->execute();
$stmt4->close();

$conn->close();

?>
