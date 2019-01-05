<?php
$conn = new mysqli("localhost", "venn", "puffsucks", "venn_db_james");


$user_id = $_GET['user_id'];
$comm_id = $_GET['comm_id'];

if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

// prepare and bind
$stmt = $conn->prepare(
"SELECT                                                                                                                                                     CASE
        WHEN (swipes.user_1_id=? and swipes.comm_id=? and (swipes.user_1_ans IS NULL)) THEN swipes.user_2_id
        WHEN (swipes.user_2_id=? and swipes.comm_id=? and (swipes.user_2_ans IS NULL)) THEN swipes.user_1_id
        ELSE 'Irrelevant'
    END
FROM swipes
JOIN memberships on memberships.comm_id=?
WHERE memberships.user_id =?");
$stmt->bind_param("ssssss", $user_id, $comm_id, $user_id, $comm_id, $comm_id, $user_id);

// set parameters and execute
$stmt->execute();

$stmt->bind_result($candidate_id);

//fetch and output to array
while ($stmt ->fetch()) {
	$output[]=$candidate_id; //since it is a single element we don't want it to store like a json object in its own array
	//$output[]=array('candidate_id'=>$candidate_id);
}


$stmt->close();

$ids = join("','",$output);  //join the id's into a list
// print($ids);
foreach($output as $id) {
	//echo $id;
}


$queryStr = "SELECT users.first_name, users.last_name, users.bio, users.user_id from users where users.user_id in ('$ids') ";


$result = $conn->query($queryStr);

if ($result->num_rows > 0) {
    // output data of each row
    while($row = $result->fetch_assoc()) {
        $output2[]=array('first_name'=>$row["first_name"], 'last_name'=>$row["last_name"], 'bio'=> $row["bio"], 'user_id'=>$row['user_id']); //put output to new array
    }
}
else {
    echo "0 results";
}

print(json_encode($output2));

$conn->close();

?>
