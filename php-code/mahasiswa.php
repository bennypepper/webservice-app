<?php
header('Content-Type: application/json');
include 'koneksi.php';

$keyword = $_GET['cari'] ?? '';
$query = "SELECT * FROM data WHERE Nama LIKE '%$keyword%' OR NIM LIKE '%$keyword%'";
$result = mysqli_query($koneksi, $query);

$response = array();
while ($row = mysqli_fetch_assoc($result)) {
    $response[] = $row;
}

echo json_encode($response);
?>