<?php
$conn = new mysqli("localhost", "venn", "puffsucks", "venn_db_james");


// $sender_id = $_GET['sender_id'];
// $receiver_id= $_GET['receiver_id'];
$swipe_id = $_GET['swipe_id'];

if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

// prepare and bind
// $stmt = $conn->prepare("SELECT messages.message_id, messages.sender, messages.receiver, messages.body FROM messages
// WHERE (messages.sender=? and messages.receiver=?) OR (messages.sender=? and messages.receiver=?) and messages.swipe_id=?
// ORDER BY messages.message_id");
// $stmt->bind_param("sssss", $sender_id, $receiver_id,$receiver_id, $sender_id, $swipe_id);

$stmt = $conn->prepare("SELECT messages.message_id, messages.sender, messages.receiver, messages.body FROM messages
WHERE messages.swipe_id=?
ORDER BY messages.message_id");
$stmt->bind_param("s", $swipe_id);


// set parameters and execute
$stmt->execute();

$stmt->bind_result($message_id,$sender,$receiver, $body);

//fetch and output to array
while ($stmt ->fetch()) {
	// $output=array('name'=>$name, 'description'=>$description,'comm_id'=>$comm_id);
  $output[]=array('message_id'=>$message_id,'sender_id'=>$sender,'receiver_id'=>$receiver,'body'=>$body);
}
//print out the json
print(json_encode($output));

$stmt->close();
$conn->close();

?>
