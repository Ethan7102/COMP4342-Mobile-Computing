<?php
	$servername="";
	$username="";
	$password="";
	$dbname="";
    $link = mysqli_connect($servername, $username, $password) or die("cannot connect")
	mysqli_select_db($link, $dbname) or die("connect db fail");
	$link -> set_charset("UTF8");
?>