<?php
require ('dbconnect.php');

	if(empty($_SERVER['REQUEST_URI'])) {
    	$_SERVER['REQUEST_URI'] = $_SERVER['SCRIPT_NAME'];
	}

	// Strip off query string so dirname() doesn't get confused
	$url = preg_replace('/\?.*$/', '', $_SERVER['REQUEST_URI']);
	$folderpath = 'http://'.$_SERVER['HTTP_HOST'].'/'.ltrim(dirname($url), '/').'/';


// Generate the unique download key
	$digits = 5;
	$key = str_pad(rand(0, pow(10, $digits)-1), $digits, '0', STR_PAD_LEFT);
	$result = mysql_query("SELECT room_id FROM users WHERE room_id = $key");

	while(mysql_num_rows($result)!=0) {
		$key = str_pad(rand(0, pow(10, $digits)-1), $digits, '0', STR_PAD_LEFT);
		$result = mysql_query("SELECT room_id FROM users WHERE room_id = $key");
	}


	$flag['key'] = $key;
// Get the activation time
	$time = date('U');
	$mac = 123;

// Write the key and activation time to the database as a new row
	$registerid = mysql_query("INSERT INTO users (room_id,mac_address,time_created) VALUES(\"$key\",\"$mac\",\"$time\")") or die(mysql_error());

	print(json_encode($flag));
?>
