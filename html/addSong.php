<?php
  require ('dbconnect.php');
  $flag['songAdded']=0;
  $songid = $_POST['song'];
  $roomid = $_POST['room'];
  $addsong = mysql_query("INSERT INTO songs (songid,roomid) VALUES(\"$songid\",\"$roomid\")") or die(mysql_error());
  if($addsong){
    $flag['songAdded']=1;
  }
  print(json_encode($flag));

?>
