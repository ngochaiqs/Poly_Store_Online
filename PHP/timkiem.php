<?php
include "connect.php";
$timKiem = $_POST['timKiem'];

if(empty($timKiem)){
	$arr = [
		'success' => false,
		'message' => "khong thanh cong",
		'result' => $result
	];
}else{
	$query = "SELECT * FROM `sanPham` WHERE `tenSP` LIKE '%".$timKiem."%'";
	$data = mysqli_query($con, $query);
	$result = array();
	while($row = mysqli_fetch_assoc($data)){
		$result[] = ($row);
	}
	if(!empty($result)){
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
	
}


print_r(json_encode($arr));

?>