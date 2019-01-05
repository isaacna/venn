<?php
$conn = new mysqli("localhost", "venn", "puffsucks", "venn_db_james");

//binding variables
$first_name = $_GET['firstName']; //joiner of the community
$last_name = $_GET['lastName']; //id of the community
$email = $_GET['email']; //first item of the community
$pass = $_GET['pass']; //second
$bio = $_GET['bio']; //third


if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

//insert into users
$stmt1 = $conn->prepare("INSERT INTO users (first_name, last_name, bio, email, password) Values (?,?,?,?,?)");

$stmt1->bind_param("sssss", $first_name, $last_name, $bio, $email, $pass);
$stmt1->execute();
$stmt1->close();

//return newly created user
$stmt2 = $conn->prepare("SELECT MAX(user_id) FROM users");
$stmt2->execute();
$stmt2->bind_result($max_id);

while($stmt2->fetch()) {
  $newest_user_id = $max_id;
}

$stmt2->close();

print($newest_user_id);


$conn->close();

?>
