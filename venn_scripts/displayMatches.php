<?php
$conn = new mysqli("localhost", "venn", "puffsucks", "venn_db_james");


$user_id = $_GET['user_id'];
$comm_id = $_GET['comm_id'];

if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

$blocked_users=array();
$block=$conn->prepare(
  "SELECT
    CASE
      WHEN blocks.blocker=? THEN blocks.blockee
      WHEN blocks.blockee=? THEN blocks.blocker
      ELSE 'Irrelevant'
    END
    FROM blocks");
$block->bind_param("ss", $user_id,$user_id);
$block->execute();
$block->bind_result($blocked_user);
while($block->fetch()) {
  // if(!is_null($blocked_user)) {
    $blocked_users[]=$blocked_user;
  // }
}
if(is_null($blocked_users)) {
  $blocked_ids = join("','",array(-1));

}
else {
  $blocked_ids = join("','", $blocked_users);

}
$block->close();

//Query 2 - getting pairs list of matches for a community
$stmt1 = $conn->prepare(
"SELECT swipes.swipe_id,
    CASE
        WHEN (swipes.user_1_id=? and swipes.comm_id=? and swipes.user_1_ans=1 and swipes.user_2_ans=1 and swipes.user_2_id not in ('$blocked_ids')) THEN swipes.user_2_id
        WHEN (swipes.user_2_id=? and swipes.comm_id=? and swipes.user_2_ans=1 and swipes.user_1_ans=1 and swipes.user_1_id not in ('$blocked_ids')) THEN swipes.user_1_id
        ELSE 'Irrelevant'
    END
FROM swipes");

$stmt1->bind_param("ssss", $user_id, $comm_id, $user_id, $comm_id);

// set parameters and execute
$stmt1->execute();

$stmt1->bind_result($swipe_id, $user_match_id);

//fetch and output to array
while ($stmt1 ->fetch()) {
  $user_match_ids[]=$user_match_id; //put into user array
  $swipe_id_array[]=$swipe_id; //put into swipe_id array
}

$stmt1->close();

$user_match_list = join("','",$user_match_ids);  //join the user id's into a list


$arrlength = count($user_match_ids);


for($i = 0; $i < $arrlength; $i++) { //do this method since swipe_id is needed

  //second query gets user info for each user_id
  $stmt2 = $conn->prepare(
    "SELECT users.first_name, users.last_name, users.bio, users.user_id from users
    JOIN swipes on swipes.swipe_id=?
    where users.user_id =? AND users.user_id!=0 AND swipes.visible_1=1 AND swipes.visible_2=1");
  $stmt2->bind_param("ss",$swipe_id_array[$i], $user_match_ids[$i]);

  $stmt2->execute();

  $stmt2->bind_result($first_name, $last_name, $bio, $user_id);

  while($stmt2->fetch()) {
    $output[]=array('first_name'=>$first_name, 'last_name'=> $last_name, 'bio'=> $bio,'user_id'=> $user_id,'swipe_id'=>$swipe_id_array[$i]);
  }
  $stmt2->close();


}
print(json_encode($output));


$conn->close();

?>
