<?php
$conn = new mysqli("localhost", "venn", "puffsucks", "venn_db_james");

function super_unique($array)
{
  $result = array_map("unserialize", array_unique(array_map("serialize", $array)));

  foreach ($result as $key => $value)
  {
    if ( is_array($value) )
    {
      $result[$key] = super_unique($value);
    }
  }

  return $result;
}



$user_id = $_GET['user_id'];

if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

// prepare and bind
$stmt = $conn->prepare("SELECT communities.comm_id FROM memberships JOIN communities on communities.comm_id=memberships.comm_id where memberships.user_id = ?");
$stmt->bind_param("s", $user_id);

// set parameters and execute
$stmt->execute();

$stmt->bind_result($joined_comm_id);

//fetch and output to array
while ($stmt ->fetch()) {
  $joined_comm_ids[]=$joined_comm_id;
}


$stmt->close();

//case where user is not in any community
if(empty($joined_comm_ids)) {
  $stmt2 = $conn->prepare("SELECT communities.name, communities.comm_id, communities.description FROM communities");

  $stmt2->execute();

  $stmt2->bind_result($new_comm_name, $new_comm_id, $new_comm_desc);


  while ($stmt2 ->fetch()) {
    // $new_comms[]=$new_comm_id;
    $output[]=array('name'=>$new_comm_name, 'comm_id'=>$new_comm_id, 'description'=>$new_comm_desc);
  }
  //print out the json
  $stmt2->close();
  if(empty($output)) {
    print("");
  }
  else {
    // print(json_encode(super_unique($output))); //remove duplicates in array
    print(json_encode($output));
  }
}

else {
  $joined_comm_list = join("','",$joined_comm_ids);  //join the user id's into a list

  $stmt2 = $conn->prepare("SELECT communities.name, communities.comm_id, communities.description FROM communities where communities.comm_id not in ('$joined_comm_list')");

  $stmt2->execute();

  $stmt2->bind_result($new_comm_name, $new_comm_id, $new_comm_desc);


  while ($stmt2 ->fetch()) {
    // $new_comms[]=$new_comm_id;
    $output[]=array('name'=>$new_comm_name, 'comm_id'=>$new_comm_id, 'description'=>$new_comm_desc);
  }
  //print out the json
  $stmt2->close();
  if(empty($output)) {
    print("");
  }
  else {
    // print(json_encode(super_unique($output))); //remove duplicates in array //this is what caused description to fuck up when description==name
    print(json_encode($output));
  }
}
$conn->close();

?>
