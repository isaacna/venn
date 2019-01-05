<?php
$conn = new mysqli("localhost", "venn", "puffsucks", "venn_db_james");


$user_id = $_GET['user_id'];

if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}


// Query 1 - getting list of communities
$stmt1 = $conn->prepare( "SELECT communities.comm_id FROM memberships JOIN communities on communities.comm_id=memberships.comm_id where memberships.user_id = ?");
$stmt1->bind_param("s", $user_id);

// set parameters and execute
$stmt1->execute();
$stmt1->bind_result($communities);

//fetch and output to array
while ($stmt1 ->fetch()) {
	$comms[]=$communities; //since it is a single element we don't want it to store like a json object in its own array
	//$output[]=array('candidate_id'=>$candidate_id);
}
//print(json_encode($comms));

$stmt1->close();


$comm_ids = join("','",$comms);  //join the community id's into a list
//print($comm_ids);


//Query 2 - getting pairs of candidate + community
$stmt2 = $conn->prepare(
"SELECT swipes.comm_id,
    CASE
        WHEN (swipes.user_1_id=? and swipes.comm_id in ('$comm_ids') and (swipes.user_1_ans IS NULL)) THEN swipes.user_2_id
        WHEN (swipes.user_2_id=? and swipes.comm_id in ('$comm_ids') and (swipes.user_2_ans IS NULL)) THEN swipes.user_1_id
        ELSE 'Irrelevant'
    END,
    CASE
        WHEN (swipes.user_1_id=? and swipes.comm_id in ('$comm_ids') and (swipes.user_1_ans IS NULL)) THEN swipes.user_2_ans
        WHEN (swipes.user_2_id=? and swipes.comm_id in ('$comm_ids') and (swipes.user_2_ans IS NULL)) THEN swipes.user_1_ans
        ELSE 'Something went wrong'
    END,
    CASE
        WHEN (swipes.user_1_id=? and swipes.comm_id in ('$comm_ids') and (swipes.user_1_ans IS NULL)) THEN 1
        WHEN (swipes.user_2_id=? and swipes.comm_id in ('$comm_ids') and (swipes.user_2_ans IS NULL)) THEN 2
        ELSE 'Something went wrong'
    END,
    swipes.swipe_id
FROM swipes WHERE swipes.visible_1=1 and swipes.visible_2=1");

//print($stmt2);
$stmt2->bind_param("ssssss", $user_id, $user_id, $user_id, $user_id, $user_id, $user_id);

// set parameters and execute
$stmt2->execute();

$stmt2->bind_result($comm_id,$candidate_id,$candidate_ans, $user_number, $swipe_id);

//fetch and output to array
while ($stmt2 ->fetch()) {
	//$output[]=$candidate_id; //since it is a single element we don't want it to store like a json object in its own array
	$comm_half[]=$comm_id;//put into communities array
  $user_half[]=$candidate_id ; //put into user array
  $candidate_ans_array[]=$candidate_ans; //keep track of candidate's answer to you
  $user_number_array[]=$user_number; //keep track of swiper's number
  $swipe_id_array[]=$swipe_id; //keep track of swipe_id
}

// print(json_encode($comm_half));
// print(json_encode($user_half));

$stmt2->close();

// $final_output = array():

//Query 3 - getting info for each candidate + community pair
//select info from each pair of userid + community id and put into array

$arrlength = count($user_half);




//iterate through the length of either community or user array
for($i = 0; $i < $arrlength; $i++) {
    // echo $cars[$i];
    //print($i);
  //  print($user_half[$i]);
    //print(":");
    if(strcmp((string)$user_half[$i],"Irrelevant")!==0) {
      //print("ya");
      $stmt3=$conn->prepare(
          "SELECT users.first_name, users.last_name, users.bio, users.user_id, users.picture, communities.name, communities.comm_id,
          communities.p1, communities.p2, communities.p3, memberships.p1, memberships.p2, memberships.p3
              FROM users
          JOIN communities on communities.comm_id=?
          JOIN memberships ON memberships.user_id=? AND memberships.comm_id=?
          WHERE (users.user_id=? and communities.comm_id=?)"
      );
      $stmt3->bind_param("sssss", $comm_half[$i], $user_half[$i],$comm_half[$i], $user_half[$i],$comm_half[$i]);
      $stmt3->execute();
      $stmt3->bind_result($first_name, $last_name, $bio, $user_id, $picture, $community, $community_id, $f1, $f2, $f3, $p1, $p2, $p3);


      while($stmt3-> fetch()) {//put to output array
        $final_output[]=array('first_name'=>$first_name, 'last_name'=>$last_name, 'bio'=>$bio, 'user_id'=>$user_id, 'picture'=>$picture, 'community'=>$community, 'comm_id'=>$community_id, 'candidate_ans'=>$candidate_ans_array[$i],
        'swiper_number'=>$user_number_array[$i], 'swipe_id'=>$swipe_id_array[$i], 'f1'=>$f1, 'f2'=>$f2,'f3'=>$f3, 'p1'=>$p1, 'p2'=>$p2,'p3'=>$p3);
      }
      $stmt3->close();
  }
}

print(json_encode($final_output));




$conn->close();

?>
