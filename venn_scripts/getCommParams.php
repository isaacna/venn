<?php
$conn = new mysqli("localhost", "venn", "puffsucks", "venn_db_james");


$comm_id = $_GET['comm_id'];

if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

//Get the newest user
$stmt = $conn->prepare("SELECT name,p1,p2,p3 FROM communities where comm_id=?");
$stmt->bind_param("s", $comm_id);

// set parameters and execute
$stmt->execute();

$stmt->bind_result($name,$p1, $p2, $p3);

while($stmt->fetch()) {
  $output=array('comm_name'=>$name,'p1'=>$p1,'p2'=>$p2,'p3'=>$p3);
}
//print out the json
print(json_encode($output));

$stmt->close();
$conn->close();

?>
