<?php
$conn = new mysqli("localhost", "venn", "puffsucks", "venn_db_james");



//binding variables
$c_id = $_GET['user_id']; //creator of the community
$c_name = $_GET['name']; //name of the community
$desc = $_GET['desc']; //community descritpion
$p_1 = $_GET['p1']; //first item of the community
$p_2 = $_GET['p2']; //second
$p_3 = $_GET['p3']; //third


if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}


$stmt1 = $conn->prepare(
"INSERT INTO communities
(name, description, owner_id, p1, p2, p3)
Values(?,?,?,?,?,?)");


$stmt1->bind_param("ssssss", $c_name, $desc, $c_id, $p_1, $p_2, $p_3);
$stmt1->execute();
$stmt1->close();

$stmt2 = $conn->prepare("SELECT max(comm_id) from communities");
$stmt2->execute();
$stmt2->bind_result($comm_id);
while($stmt2->fetch()) {
  $the_comm_id=$comm_id;
}

print($the_comm_id);
$stmt2->close();
$conn->close();

?>
