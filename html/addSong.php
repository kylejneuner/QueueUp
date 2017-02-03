<?
  require ('dbconnect.php');

  if(empty($_SERVER['REQUEST_URI'])) {
      $_SERVER['REQUEST_URI'] = $_SERVER['SCRIPT_NAME'];
  }
  // Strip off query string so dirname() doesn't get confused
  $url = preg_replace('/\?.*$/', '', $_SERVER['REQUEST_URI']);
  $folderpath = 'http://'.$_SERVER['HTTP_HOST'].'/'.ltrim(dirname($url), '/').'/';

  $songid=$_REQUEST['songid'];
	$roomid=$_REQUEST['roomid'];

	$flag['code']=0;

  $addsong = mysql_query("INSERT INTO songs (songid,roomid) VALUES(\"$songid\",\"$roomid\")") or die(mysql_error());
	{
		$flag['code']=1;
		echo"hi";
	}

	print(json_encode($flag));


?>
