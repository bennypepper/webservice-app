<?php
header('Content-Type: application/json');
include 'koneksi.php';

$id = $_POST['id'] ?? '';
$nim = $_POST['NIM'] ?? '';
$nama = $_POST['Nama'] ?? '';
$jurusan = $_POST['Jurusan'] ?? '';
$alamat = $_POST['Alamat'] ?? '';

if (!empty($id)) {
    $query = "UPDATE data SET NIM='$nim', Nama='$nama', Jurusan='$jurusan', Alamat='$alamat' WHERE id='$id'";
} elseif (!empty($nim)) {
    $query = "UPDATE data SET Nama='$nama', Jurusan='$jurusan', Alamat='$alamat' WHERE NIM='$nim'";
} else {
    echo json_encode(["status" => "gagal", "pesan" => "Parameter id atau NIM diperlukan"]);
    exit();
}

if (mysqli_query($koneksi, $query)) {
    echo json_encode(["status" => "sukses", "pesan" => "Data berhasil diupdate"]);
} else {
    echo json_encode(["status" => "gagal", "pesan" => "Error: " . mysqli_error($koneksi)]);
}
?>