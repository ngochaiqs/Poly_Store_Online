<?php
include = "connect.php"

if(isset($_POST['submit_password']) && $_POST['email'])
{
	$email = $_POST['email'];
	$matKhau = $_POST['matKhau'];

	$query = "update nguoiDung set matKhau='$matKhau' where email='$email'";

	$data = mysqli_query($con, $query);
	if($data == true){
		echo "Thanh cong";
	}
}
?>