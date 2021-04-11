<?php include 'dbconnect.php';
	$email=$_POST['email'];
	$code=rand(100000, 999999);
	$sql_insert="INSERT INTO `order` (`confirmationCode`, `email`) VALUES ('$code', '$email');";
	$link->query($sql_insert) or die($link->error);
	$last_id = $link->insert_id;
	echo $last_id;
	$sql_detail="INSERT INTO orderDetail (orderID, productID, quantity) VALUES";
	$obj=json_decode($_POST['idQuantity'], true);
	$i = count($obj);
	$count=0;
	foreach($obj as $key => $value) {
		$sql_detail.="('$last_id', '$key', '$value')";
		++$count;
		if($count==$i) {
			$sql_detail.=";";
		} else {
			$sql_detail.=",";
		}
	}
	echo $sql_detail;
		/*$result=$link->query($sql) or die($link->error);
		while ($row = $result->fetch_assoc())
		{
			$output[] = $row;
		}
	echo json_encode($output, JSON_UNESCAPED_UNICODE);*/
	$link->close();
?>