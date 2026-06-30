<?php
header('Content-Type: application/json');
include 'koneksi.php';

$id = $_POST['id'] ?? '';
$nim = $_POST['NIM'] ?? '';

if (!empty($id)) {
    $query = "DELETE FROM data WHERE id='$id'";
} elseif (!empty($nim)) {
    $query = "DELETE FROM data WHERE NIM='$nim'";
} else {
    echo json_encode(["status" => "gagal", "pesan" => "Parameter id atau NIM diperlukan"]);
    exit();
}

if (mysqli_query($koneksi, $query)) {
    echo json_encode(["status" => "sukses", "pesan" => "Data berhasil dihapus"]);
} else {
    echo json_encode(["status" => "gagal", "pesan" => "Error: " . mysqli_error($koneksi)]);
}
?>