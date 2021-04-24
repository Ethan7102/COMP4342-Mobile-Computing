<?php include 'dbconnect.php';
	$obj=json_decode($_POST['idQuantity'], true);
	$i = count($obj);
	$count=0;
	$sql_check = "select productID, quantity from product where productID in (";
	foreach($obj as $key => $value) {
		$sql_check .= "$value";
		++$count;
		if(!$count==$i) {
			$sql_detail.=",";
		}
	}
	$sql_check .= ") for update";
	$result = $link->query($sql_check);
	while ($row = $result->fetch_assoc()) {
		if(!$obj[$row['productID']]<=$row['quantity']) {
			exit();
		}
	}
	
	
	
	$email=$_POST['email'];
	$code=rand(100000, 999999);
	$status="success";
	$sql_insert="INSERT INTO `order` (`confirmationCode`, `email`) VALUES ('$code', '$email');";
	if(!$link->query($sql_insert)){
		$status="fail";
		echo json_encode($status, JSON_UNESCAPED_UNICODE);
		exit();
	}
	$orderID = $link->insert_id;
	$sql_detail="INSERT INTO orderDetail (orderID, productID, quantity) VALUES";
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
	foreach($obj as $key => $value) {
		$sql_update="UPDATE product SET product.quantity = product.quantity - $value WHERE product.productID = $key";
		if(!$link->query($sql_update)){
			$status="fail";
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