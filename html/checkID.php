<?php
  require('dbconnect.php');
  $roomid = $_POST['room'];
  $result = mysql_query("SELECT room_id FROM users WHERE room_id = $roomid");

  $flag['isRoom'] = 0;

  if(mysql_num_rows($result)!=0) {
    $flag['isRoom'] = 1;
  }

  print(json_encode($flag));

?>
