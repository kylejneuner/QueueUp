<?php
require ('dbconnect.php');

	if(empty($_SERVER['REQUEST_URI'])) {
    	$_SERVER['REQUEST_URI'] = $_SERVER['SCRIPT_NAME'];
	}

	// Strip off query string so dirname() doesn't get confused
	$url = preg_replace('/\?.*$/', '', $_SERVER['REQUEST_URI']);
	$folderpath = 'http://'.$_SERVER['HTTP_HOST'].'/'.ltrim(dirname($url), '/').'/';


// Generate the unique download key
	$key = uniqid(md5(rand()));
	$flag['key'] = $key;
// Get the activation time
	$time = date('U');

// Write the key and activation time to the database as a new row
	$registerid = mysql_query("INSERT INTO users (uniqueid,timestamp) VALUES(\"$key\",\"$time\")") or die(mysql_error());

	print(json_encode($flag));
?>
