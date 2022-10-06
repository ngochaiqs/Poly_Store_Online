<?php
include "connect.php"

if($_GET['key'] && $_GET['reset']) 
{
  $email=$_GET['key'];
  $matKhau=$_GET['reset'];
  $query = "select email,matKhau from nguoiDung where email='$email' and matKhau='$matKhau'";
  $data = mysqli_query($conn, $query);

  if($data == true)
  {
    ?>
    <form method="post" action="submit_new.php">
    <input type="hidden" name="email" value="<?php echo $email;?>">
    <p>Enter New password</p>
    <input type="password" name='password'>
    <input type="submit" name="submit_password">
    </form>
    <?php
  }
}
?>