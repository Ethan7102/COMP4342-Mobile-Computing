<?php include 'dbconnect.php';
		
	$sql="SELECT * FROM product WHERE id=productID";
	$result=$link->query($sql);
	
	while ($row = $result->fetch_assoc())
    {
        $output[] = $row; // 就逐項將回傳的東西放到陣列中
    }
	echo json_encode($output, JSON_UNESCAPED_UNICODE);
	$link->close();
?>