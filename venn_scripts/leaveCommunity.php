<?php
$conn = new mysqli("localhost", "venn", "puffsucks", "venn_db_james");

//binding variables
$user_id = $_GET['user_id']; //leaver of the community
$comm_id = $_GET['comm_id']; //id of the community


if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

//delete from memberships
$stmt1 = $conn->prepare("DELETE FROM memberships WHERE memberships.user_id=? and memberships.comm_id=?");

$stmt1->bind_param("ss", $user_id,$comm_id);
$stmt1->execute();
$stmt1->close();


//set swipes visibility to 0
$stmt2 = $conn->prepare("UPDATE swipes SET visible_1=0 where swipes.user_1_id=? and swipes.comm_id=?");
$stmt2->bind_param("ss",$user_id,$comm_id);
$stmt2->execute();
$stmt2->close();

$stmt3 = $conn->prepare("UPDATE swipes SET visible_2=0 where swipes.user_2_id=? and swipes.comm_id=?");
$stmt3->bind_param("ss",$user_id,$comm_id);
$stmt3->execute();
$stmt3->close();


print("Success! Hopefully");


$conn->close();

?>
