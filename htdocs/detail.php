<?php include 'dbconnect.php';
		
	$sql="SELECT * FROM product WHERE id=productID";
	$result=$link->query($sql);
	
	while ($row = $result->fetch_assoc())
    {
        $output[] = $row;
    }
	echo json_encode($output, JSON_UNESCAPED_UNICODE);
	$link->close();
?>