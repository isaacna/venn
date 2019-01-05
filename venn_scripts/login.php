<?php
$conn = new mysqli("localhost", "venn", "puffsucks", "venn_db_james");


$email = $_GET['email'];
$pw_hash = $_GET['pw_hash'];

if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}


//Query 2 - getting pairs list of matches for a community
$stmt1 = $conn->prepare(
"SELECT users.first_name, users.last_name, users.bio, users.picture, users.user_id FROM users
WHERE users.email=? and users.password=?");

$stmt1->bind_param("ss", $email, $pw_hash);

// set parameters and execute
$stmt1->execute();

$stmt1->bind_result($first_name, $last_name, $bio, $picture, $user_id);

//fetch and output to array
while ($stmt1 ->fetch()) {
  if(!is_null($user_id)) {
    $output = array('first_name'=>$first_name, 'last_name'=>$last_name,'bio'=>$bio,'picture'=>$picture,'user_id'=>$user_id);
    print(json_encode($output));
  }

}

$stmt1->close();



$conn->close();

?>
