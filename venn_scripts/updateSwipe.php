<?php
$conn = new mysqli("localhost", "venn", "puffsucks", "venn_db_james");

//use to decide which query
$user_number = $_GET['swiper_num']; //whether swiper is 1 or 2

//binding variables
$user_id = $_GET['user_id']; //swiper's id
$candidate_id = $_GET['candidate_id']; //candidate's id
$user_ans = $_GET['user_ans']; //yes or nos
$comm_id = $_GET['comm_id']; //community id


if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

if($user_number==1) {
  $stmt1 = $conn->prepare(
  "UPDATE swipes
  SET swipes.user_1_ans = ?
  WHERE swipes.user_1_id=? and swipes.user_2_id=? and swipes.comm_id=?");

}

else {//swiper is user 2
  $stmt1 = $conn->prepare(
  "UPDATE swipes
  SET swipes.user_2_ans = ?
  WHERE swipes.user_2_id=? and swipes.user_1_id=? and swipes.comm_id=?");

}

$stmt1->bind_param("ssss", $user_ans, $user_id, $candidate_id, $comm_id);

// set parameters and execute
$stmt1->execute();
// $stmt1->bind_result($communities);

//fetch and output to array
// while ($stmt1 ->fetch()) {
// 	$comms[]=$communities; //since it is a single element we don't want it to store like a json object in its own array
// 	//$output[]=array('candidate_id'=>$candidate_id);
// }
print("Success! Hopefully");

$stmt1->close();

$conn->close();

?>
