<?
  require ('dbconnect.php');



  $roomid=$_REQUEST['roomid'];

  $getSongs = mysql_query("select songid from songs where roomid='$roomid'") or die(mysql_error());
  mysql_query("delete * from songs where roomid='$roomid'") or die(mysql_error());
	while($s=mysql_fetch_array($getSongs)){
    $flag[songid]=$s[songid];
  }

	print(json_encode($flag));


?>
