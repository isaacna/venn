<?php
$conn = new mysqli("localhost", "venn", "puffsucks", "venn_db_james");


$other_id = $_GET['other_id'];
$swipe_id = $_GET['swipe_id'];

if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

//Get the newest user
$stmt = $conn->prepare("SELECT users.first_name, users.last_name, users.bio, users.user_id, users.picture, communities.name, communities.comm_id,
communities.p1, communities.p2, communities.p3, memberships.p1, memberships.p2, memberships.p3
FROM users
JOIN swipes ON swipes.swipe_id=?
JOIN memberships ON memberships.user_id=users.user_id AND memberships.comm_id=swipes.comm_id
JOIN communities ON communities.comm_id=swipes.comm_id
WHERE users.user_id=? and swipes.swipe_id=?");
$stmt->bind_param("sss", $swipe_id, $other_id, $swipe_id);

// set parameters and execute
$stmt->execute();

$stmt->bind_result($first_name, $last_name, $bio, $user_id, $picture, $community, $community_id, $f1, $f2, $f3, $p1, $p2, $p3);

while($stmt->fetch()) {
  $output=array('first_name'=>$first_name, 'last_name'=>$last_name, 'bio'=>$bio, 'user_id'=>$user_id, 'picture'=>$picture, 'community'=>$community, 'comm_id'=>$community_id,
    'f1'=>$f1, 'f2'=>$f2,'f3'=>$f3, 'p1'=>$p1, 'p2'=>$p2,'p3'=>$p3);
}
//print out the json
print(json_encode($output));

$stmt->close();
$conn->close();

?>
