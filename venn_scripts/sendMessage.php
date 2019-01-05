<?php
$conn = new mysqli("localhost", "venn", "puffsucks", "venn_db_james");


//binding variables
$sender_id = $_GET['sender_id']; //swiper's id
$receiver_id = $_GET['receiver_id']; //candidate's id
$swipe_id = $_GET['swipe_id']; //yes or nos
$body = $_GET['body']; //community id


if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

$stmt = $conn->prepare(
  "INSERT into messages (swipe_id, sender, receiver, body) VALUES (?,?,?,?)"
);



$stmt->bind_param("ssss", $swipe_id, $sender_id, $receiver_id, $body);

// set parameters and execute
$stmt->execute();


print("Success! Hopefully");

$stmt->close();

$conn->close();

?>
