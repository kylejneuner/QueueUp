<?
  require ('checkID.php');

  $songid = $_POST['song'];
  $addsong = mysql_query("INSERT INTO songs (songid,roomid) VALUES(\"$songid\",\"$roomid\")") or die(mysql_error());

?>
