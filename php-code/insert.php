<?php
header('Content-Type: application/json');
include 'koneksi.php';

$nim = $_POST['NIM'] ?? '';
$nama = $_POST['Nama'] ?? '';
$jurusan = $_POST['Jurusan'] ?? '';
$alamat = $_POST['Alamat'] ?? '';
$foto = '';

if (isset($_FILES['foto']) && $_FILES['foto']['error'] == 0) {
    $foto = time() . "_" . $_FILES['foto']['name'];
    move_uploaded_file($_FILES['foto']['tmp_name'], 'uploads/' . $foto);
}

$query = "INSERT INTO data (NIM, Nama, Jurusan, Alamat, foto) VALUES ('$nim', '$nama', '$jurusan', '$alamat', '$foto')";

if (mysqli_query($koneksi, $query)) {
    echo json_encode(["status" => "sukses", "pesan" => "Data berhasil ditambah"]);
} else {
    echo json_encode(["status" => "gagal", "pesan" => "Gagal: " . mysqli_error($koneksi)]);
}
?>