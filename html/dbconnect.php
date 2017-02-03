<?php //Connect to database

  $link = mysql_connect('localhost','root','') or die("Could not connect: " . mysql_error());
  mysql_select_db("queueUp") or die(mysql_error());


 ?>
