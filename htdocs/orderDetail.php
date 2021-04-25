<?php include 'dbconnect.php';
	$email=$_POST["email"];	
	$code=$_POST["code"];	
	$sql="SELECT product.productID, product.productName, product.price, orderdetail.quantity From orderdetail Inner Join product ON product.productID=orderdetail.productID WHERE orderdetail.orderID=(SELECT orderID FROM `order` WHERE email='$email' AND confirmationCode='$code')";
	
	$result=$link->query($sql) or die($link->error);
	while ($row = $result->fetch_assoc())
    {
        $output[] = $row;
    }
	//get order status
	$sql="SELECT * FROM comp4342_msdb.order WHERE email='$email' AND confirmationCode='$code'";
	$result=$link->query($sql) or die($link->error);
	
	while ($row = $result->fetch_assoc())
    {
        $status = $row["status"];
    }
	if(isset($output)) {
		$json = ["status" => $status, "product" => $output];
		echo json_encode($json, JSON_UNESCAPED_UNICODE);
	} else {
		$status = "confirmation code or email is incorrect";
		$json = ["status" => $status];
		echo json_encode($json, JSON_UNESCAPED_UNICODE);
	}
	$link->close();
?>