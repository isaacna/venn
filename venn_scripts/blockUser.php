<?php
$conn = new mysqli("localhost", "venn", "puffsucks", "venn_db_james");

//binding variables
$user_id = $_GET['user_id']; //id of the user blocking
$block_id = $_GET['block_id']; //id of person to block

if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

//update swipes
$stmt1 = $conn->prepare("UPDATE swipes SET blocked=1
  WHERE (swipes.user_1_id=? and swipes.user_2_id=?) OR (swipes.user_2_id=? and swipes.user_1_id=?)");

$stmt1->bind_param("ssss", $user_id, $block_id,  $user_id, $block_id);
$stmt1->execute();
$stmt1->close();
$conn->close();
?>
