<?php
$conn = new mysqli("localhost", "venn", "puffsucks", "venn_db_james");

//binding variables
$user_id = $_GET['user_id']; //id of the user
$comm_id = $_GET['comm_id']; //id of the community
$p1 = $_GET['p1']; //first item of the community
$p2 = $_GET['p2']; //second
$p3 = $_GET['p3']; //third


if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

//insert into users
$stmt1 = $conn->prepare("UPDATE memberships SET p1=?, p2=?, p3=? WHERE user_id=? and comm_id=?");

$stmt1->bind_param("sssss", $p1, $p2, $p3, $user_id, $comm_id);
$stmt1->execute();
$stmt1->close();


$conn->close();

?>
