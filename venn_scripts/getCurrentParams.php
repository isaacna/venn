<?php
$conn = new mysqli("localhost", "venn", "puffsucks", "venn_db_james");


$comm_id = $_GET['comm_id'];
$user_id= $_GET['user_id'];

if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

//Get the newest user
$stmt = $conn->prepare("SELECT p1,p2,p3 FROM memberships where comm_id=? and user_id=?");
$stmt->bind_param("ss", $comm_id, $user_id);

// set parameters and execute
$stmt->execute();

$stmt->bind_result($p1, $p2, $p3);

while($stmt->fetch()) {
  $output=array('p1'=>$p1,'p2'=>$p2,'p3'=>$p3);
}
//print out the json
print(json_encode($output));

$stmt->close();
$conn->close();

?>
