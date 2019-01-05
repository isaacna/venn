<?php
$conn = new mysqli("localhost", "venn", "puffsucks", "venn_db_james");

//binding variables
$user_id = $_GET['user_id']; //user's id
$swipe_id = $_GET['swipe_id']; //swipe to unmatch

if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

//adjust when user 1 in swipes
$stmt1 = $conn->prepare( "UPDATE swipes SET swipes.user_1_ans=0, swipes.user_2_ans=0,swipes.visible_1 = 0, swipes.visible_2 = 0 WHERE swipes.user_1_id=? and swipes.swipe_id=?");
$stmt1->bind_param("ss", $user_id, $swipe_id);
$stmt1->execute();
$stmt1->close();

//adjust when user 2 in swipes
$stmt2 = $conn->prepare("UPDATE swipes SET swipes.user_1_ans=0, swipes.user_2_ans=0,swipes.visible_1 = 0, swipes.visible_2 = 0 WHERE swipes.user_2_id=? and swipes.swipe_id=?");
$stmt2->bind_param("ss", $user_id, $swipe_id);
$stmt2->execute();
$stmt2->close();

$stmt3=$conn->prepare("DELETE FROM messages WHERE messages.swipe_id=?");
$stmt3->bind_param("s", $swipe_id);
$stmt3->execute();
$stmt3->close();

print("Success! Hopefully");

$conn->close();
?>
