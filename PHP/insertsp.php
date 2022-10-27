<?php
include "connect.php";
$tenSP = $_POST['tensp'];
$email = $_POST['gia'];
$hinhanh = $_POST['hinhanh'];
$mota = $_POST['mota'];
$loai = $_POST['loai'];


//check data

$query = 'INSERT INTO `sanphammoi`( `tenSP`, `giaSP`, `hinhanh`, `mota`, `loai`) VALUES ("'.$tenSP.'","'.$gia.'","'.$hinhanh.'","'.mota.'",'.$loai.')';
$data = mysqli_query($con, $query);
	if($data == true){
		$arr = [
			'success' => true,
			'message' => "Thành công"
		];
		}else{
			$arr = [
				'success' => false,
				'message' => "Không thành công"
			];
		}
		
	print_r(json_encode($arr));

?>