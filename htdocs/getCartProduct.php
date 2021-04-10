<?php include 'dbconnect.php';
	$obj=json_decode($_POST['idList'], true);
	foreach($obj as $value) {
		$sql="SELECT * FROM product WHERE productID='$value'";
		$result=$link->query($sql) or die($link->error);
		while ($row = $result->fetch_assoc())
		{
			$output[] = $row;
		}
	}
	echo json_encode($output, JSON_UNESCAPED_UNICODE);
	$link->close();
?>