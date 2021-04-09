<?php
include 'dbconnect.php';
$sql = "select * from product";
$result = $link->query($sql) or die($link->error);
while ($row = $result->fetch_assoc()) {
    $output[] = $row;
}
$result="{\"products\":";
$result.=json_encode($output, JSON_UNESCAPED_UNICODE);
$result.="}";
echo $result;
$link->close();
?>