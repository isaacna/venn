<?php
$conn = new mysqli("localhost", "venn", "puffsucks", "venn_db_james");


$user_id = $_GET['user_id'];

if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

// prepare and bind
$stmt = $conn->prepare("SELECT communities.name, communities.description, communities.comm_id FROM memberships JOIN communities on communities.comm_id=memberships.comm_id where memberships.user_id = ?");
$stmt->bind_param("s", $user_id);

// set parameters and execute
$stmt->execute();

$stmt->bind_result($name,$description,$comm_id);

//fetch and output to array
while ($stmt ->fetch()) {
	$output[]=array('name'=>$name, 'description'=>$description,'comm_id'=>$comm_id);
}
//print out the json
print(json_encode($output));

$stmt->close();
$conn->close();

?>
