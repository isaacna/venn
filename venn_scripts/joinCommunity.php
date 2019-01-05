<?php
$conn = new mysqli("localhost", "venn", "puffsucks", "venn_db_james");



//binding variables
$user_id = $_GET['user_id']; //joiner of the community
$comm_id = $_GET['comm_id']; //id of the community
$p1 = $_GET['p1']; //first item of the community
$p2 = $_GET['p2']; //second
$p3 = $_GET['p3']; //third


if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

//insert into memberships
$stmt1 = $conn->prepare("INSERT INTO memberships (comm_id, user_id, p1, p2, p3) Values (?,?,?,?,?)");

$stmt1->bind_param("sssss", $comm_id, $user_id, $p1, $p2, $p3);
$stmt1->execute();
$stmt1->close();


//select list of other members in community
$stmt2 = $conn->prepare("SELECT memberships.user_id FROM memberships WHERE memberships.comm_id=? and memberships.user_id!=?");
$stmt2->bind_param("ss",$comm_id,$user_id);
$stmt2->execute();
$stmt2->bind_result($other_member);


while ($stmt2 ->fetch()) {
	$other_members[]=$other_member;
}
$stmt2->close();


//fetch user id list of previous existing swipe pairs (rejoin edge case)
$stmt3=$conn->prepare("SELECT
  CASE
      WHEN (swipes.user_1_id=? and swipes.comm_id=?) THEN swipes.user_2_id
      WHEN (swipes.user_2_id=? and swipes.comm_id=?) THEN swipes.user_1_id
      ELSE 'Irrelevant'
  END
  FROM swipes
");
$stmt3->bind_param("ssss", $user_id, $comm_id, $user_id, $comm_id);
$stmt3->execute();
$stmt3->bind_result($existing_pair);
while ($stmt3 ->fetch()) {
	$existing_pairs[]=$existing_pair;
}

$stmt3->close();


$non_existing_pairs=array();

for($i=0; $i < count($other_members); $i++) {
  for($j=0; $j < count($existing_pairs); $j++) {
    if($other_members[$i]==$existing_pairs[$j]) {
      $other_members[$i]= -1;
    }
  }
}

for($i=0; $i < count($other_members); $i++) {
  if($other_members[$i]!=-1) {
    array_push($non_existing_pairs,$other_members[$i]);

  }
}


// $non_existing_pairs = arr_diff($other_members,$existing_pairs);
// print(implode(" ",$non_existing_pairs));

$arrlength = count($non_existing_pairs);

$non_existing_blocks = array();

//create swipe pairs for every other member in community
for($i = 0; $i < $arrlength; $i++) {
  // if(!in_array($other_members[$i],$existing_pairs)) {
    print("fuck");
    print($non_existing_pairs[$i]);
    $stmt4 = $conn->prepare("INSERT INTO swipes (comm_id, user_1_id, user_2_id, visible_1, visible_2) VALUES (?,?,?,1,1)");
    $stmt4->bind_param("sss", $comm_id, $user_id, $non_existing_pairs[$i]);
    $stmt4->execute();
    $stmt4->close();

    //if any existing blocks, set blocks for that potential candidate to true
    $checkblock = $conn->prepare("SELECT blocked from swipes where (user_1_id=? AND user_2_id=?) OR (user_2_id=? AND user_1_id=?)");
    $checkblock->bind_param("ssss", $user_id, $non_existing_pairs[$i], $user_id, $non_existing_pairs[$i]);
    $checkblock->execute();
    $checkblock->bind_result($non_existing_block);
    while ($checkblock ->fetch()) {
    	$already_blocked[]=$non_existing_block;
    }
    if(in_array(1,$already_blocked)) {
      $non_existing_blocks[$i]=1;
    }
    $checkblock->close();
  // }
}

//if non_existing_blocks contains any 1's, update blocks again
for($i=0; $i < $arrlength; $i++) {
  if($non_existing_blocks[$i]==1) {
    $blockstmt = $conn->prepare("UPDATE swipes SET blocked=1
      WHERE (swipes.user_1_id=? and swipes.user_2_id=?) OR (swipes.user_2_id=? and swipes.user_1_id=?)");
    $blockstmt->bind_param("ssss", $user_id, $non_existing_pairs[$i],  $user_id, $non_existing_pairs[$i]);
    $blockstmt->execute();
    $blockstmt->close();
  }
}



//update visibility upon rejoining
for($i=0; $i < count($existing_pairs); $i++) {
  if($existing_pairs[$i] != 'Irrelevant') {

    //get if the user is user 1 or 2 of the swipe
    $stmt5 = $conn->prepare("SELECT
      CASE
        WHEN user_1_id=? and user_2_id=? THEN 1
        WHEN user_2_id=? and user_1_id=? then 2
        ELSE 'Help'
      END
      FROM swipes
      where comm_id=? and  ((user_1_id=? and user_2_id=?) OR (user_2_id=? and user_1_id=?))");
    $stmt5->bind_param("sssssssss", $user_id, $existing_pairs[$i], $user_id, $existing_pairs[$i], $comm_id,$user_id, $existing_pairs[$i],$user_id, $existing_pairs[$i]);
    $stmt5->execute();
    $stmt5->bind_result($user1or2);
    while ($stmt5 ->fetch()) { //fetch the value
    	$user1or2Val=$user1or2;
    }
    $stmt5->close();


    //update user 1 answer if user 1
    if($user1or2Val==1) {
      // print("one\n");
      // print("user_1- ". $user_id);
      $stmt6 = $conn->prepare("UPDATE swipes set visible_1=1 where user_1_id=? and user_2_id=? and comm_id=?");
      $stmt6->bind_param("sss", $user_id, $existing_pairs[$i], $comm_id);
      $stmt6->execute();
      $stmt6->close();
    }

    //update user 2 answer if user 2
    else if($user1or2Val==2){
      // print("two\n");
      // print("user_1- ". $existing_pairs[$i]);
      $stmt6 = $conn->prepare("UPDATE swipes set visible_2=1 where user_2_id=? and user_1_id=? and comm_id=?");
      $stmt6->bind_param("sss", $user_id, $existing_pairs[$i], $comm_id);
      $stmt6->execute();
      $stmt6->close();
    }
  }
}
// */



$conn->close();



//case for rejoining
//get previous user id's where swipes.comm_id = comm and swipes.user_1_id or swipes.user_2_id = joiner's id
//get list of user's in that $community
//for list of users in community, create swipe pair where user not in previous user ids
?>
