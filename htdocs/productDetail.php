<?php include 'dbconnect.php';
		
	$sql="SELECT * FROM product WHERE productID='123'";
	$result=$link->query($sql) or die($link->error);
	
	while ($row = $result->fetch_assoc())
    {
        $output[] = $row;
    }
	echo json_encode($output, JSON_UNESCAPED_UNICODE);
	$link->close();
?>