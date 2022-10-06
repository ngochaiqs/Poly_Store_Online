<?php
include "connect.php";
$tenND = $_POST['tenND'];
$email = $_POST['email'];
$matKhau = $_POST['matKhau'];
$SDT = $_POST['SDT'];


//check data

$query = 'SELECT * FROM `nguoiDung` WHERE `email` = "'.$email.'"';
$data = mysqli_query($con, $query);
$numrow = mysqli_num_rows($data);



if ($numrow > 0){
	$arr = [
		'success' => false,
		'message' => "Email da ton tai"
	];
}else{
	//insert
	$query = 'INSERT INTO `nguoiDung`(`tenND`,`email`,`matKhau`,`SDT`) VALUES ("'.$tenND.'","'.$email.'","'.$matKhau.'","'.$SDT.'")';
	$data = mysqli_query($con, $query);

	if($data == true){
		$arr = [
			'success' => true,
			'message' => "thanh cong"
		];
		}else{
			$arr = [
				'success' => false,
				'message' => "khong thanh cong"
			];
		}
		
	}
	print_r(json_encode($arr));

?>