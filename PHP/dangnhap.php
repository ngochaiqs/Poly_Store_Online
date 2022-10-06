<?php
include "connect.php";
$email = $_POST['email'];
$matKhau = $_POST['matKhau'];



//check data

$query = 'SELECT * FROM `nguoiDung` WHERE `email` = "'.$email.'" AND `matKhau` = "'.$matKhau.'"';
$data = mysqli_query($con, $query);
$result = array();
while ($row = mysqli_fetch_assoc($data)){
	$result[] = ($row);
	//code
}
if (!empty($result)){
	$arr = [
		'success' => true,
		'message' => "thanh cong",
		'result' => $result
	];
}else{
	$arr = [
		'success' => false,
		'message' => "khong thanh cong",
		'result' => $result
	];
}


print_r(json_encode($arr));

?>