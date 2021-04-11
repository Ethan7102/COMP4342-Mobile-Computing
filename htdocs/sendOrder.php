<?php include 'dbconnect.php';
	$email=$_POST['email'];
	$code=rand(100000, 999999);
	$sql_insert="INSERT INTO `order` (`confirmationCode`, `email`) VALUES ('$code', '$email');";
	$link->query($sql_insert) or die($link->error);
	$orderID = $link->insert_id;
	$sql_detail="INSERT INTO orderDetail (orderID, productID, quantity) VALUES";
	$obj=json_decode($_POST['idQuantity'], true);
	$i = count($obj);
	$count=0;
	foreach($obj as $key => $value) {
		$sql_detail.="($orderID, $key, $value)";
		++$count;
		if($count==$i) {
			$sql_detail.=";";
		} else {
			$sql_detail.=",";
		}
	}
	$link->query($sql_detail) or die($link->error);
	$array=array(
		"email" => $email,
		"code" => $code,
	);
		
	echo json_encode($array, JSON_UNESCAPED_UNICODE);
	$link->close();
?>