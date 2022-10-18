<?php
include "connect.php";
$maND = $_POST['maND'];

$query = 'SELECT * FROM `donHang` WHERE `maND` = "'.$maND'"';
$data = mysqli_query($con, $query);
$result = array();
while($row = mysqli_fetch_assoc($data)){
	$truyvan = 'SELECT * FROM `chiTietDonHang` INNER JOIN sanPham ON chiTietDonHang.maSP = sanPham.maSP WHERE chiTietDonHang.maCTDH = '.$row['maSP'];
	$data1 = mysqli_query($con, $truyvan);
	$item = array();
	while($row1 = mysqli_fetch_assoc($data1)){
		$item[] = $row1;
	}

	$row['item'] = $item;
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
print_r(json_encode($arr));

?>