<?php include 'dbconnect.php';
	$email=$_POST["email"];	
	$code=$_POST["code"];	
	$sql="SELECT product.productID, product.productName, product.price, orderdetail.quantity From orderdetail Inner Join product ON product.productID=orderdetail.productID WHERE orderdetail.orderID=(SELECT orderID FROM `order` WHERE email='$email' AND confirmationCode='$code')";
	
	$result=$link->query($sql) or die($link->error);
	
	while ($row = $result->fetch_assoc())
    {
        $output[] = $row;
    }
	echo json_encode($output, JSON_UNESCAPED_UNICODE);
	$link->close();
?>