<?php

//include 'DatabaseConfig.php';

// Create connection
//$conn = new mysqli($HostName, $HostUser, $HostPass, $DatabaseName);
$conn = new mysqli("localhost", "venn", "puffsucks", "venn_db_james");

 if($_SERVER['REQUEST_METHOD'] == 'POST')
 {
 $DefaultId = 0;

 $ImageData = $_POST['image_path'];

 $ImageName = $_POST['image_name'];

 $GetOldIdSQL ="SELECT id FROM images ORDER BY id ASC";

 //$Query = mysqli_query($conn,$GetOldIdSQL);

// while($row = mysqli_fetch_array($Query)){

 //$DefaultId = $row['id'];
 //}

 //
 //$ServerURL = "http://ec2-34-215-159-222.us-west-2.compute.amazonaws.com/alt/images/$ImagePath";
 //
 // $InsertSQL = "insert into images (image_path,image_name) values ('$ServerURL','$ImageName')";

$imageNumString = (string) $ImageName;
$pngName =  $imageNumString . '.png';

$ImagePath = "images/".$pngName;

print($ImagePath);

$stmt = $conn->prepare("UPDATE users set picture=? where user_id=?");
$stmt->bind_param("ss",$pngName,$ImageName);
$stmt->execute();
$stmt->close();

print("Inserted");


 // if(mysqli_query($conn, $InsertSQL)){

 file_put_contents($ImagePath,base64_decode($ImageData));

 echo "Your Image Has Been Uploaded.";
 // }

 mysqli_close($conn);
 }
 else{
 echo "Not Uploaded";
 }
// $conn->close();
?>
