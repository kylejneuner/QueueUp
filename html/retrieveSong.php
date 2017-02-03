<?
  require ('dbconnect.php');

  if(empty($_SERVER['REQUEST_URI'])) {
      $_SERVER['REQUEST_URI'] = $_SERVER['SCRIPT_NAME'];
  }
  // Strip off query string so dirname() doesn't get confused
  $url = preg_replace('/\?.*$/', '', $_SERVER['REQUEST_URI']);
  $folderpath = 'http://'.$_SERVER['HTTP_HOST'].'/'.ltrim(dirname($url), '/').'/';

  $roomid=$_REQUEST['roomid'];

  $getSongs = mysql_query("select songid from songs where roomid='$roomid'") or die(mysql_error());
  mysql_query("delete * from songs where roomid='$roomid'") or die(mysql_error());
	while($s=mysql_fetch_array($getSongs)){
    $flag[songid]=$s[songid];
  }

	print(json_encode($flag));


?>
