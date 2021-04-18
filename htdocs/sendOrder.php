<?php include 'dbconnect.php';
	$email=$_POST['email'];
	$code=rand(100000, 999999);
	$status="success";
	$sql_insert="INSERT INTO `order` (`confirmationCode`, `email`) VALUES ('$code', '$email');";
	if(!$link->query($sql_insert)){
		$status="fail";
	}
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
	if(!$link->query($sql_detail)){
		$status="fail";
	}
	$array=array(
		"email" => $email,
		"code" => $code,
		"status" => $status,
	);
		
	echo json_encode($array, JSON_UNESCAPED_UNICODE);
	$link->close();
?>