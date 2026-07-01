<?php
header('Content-Type: application/json');
include 'koneksi.php';

$nim     = $_POST['NIM']     ?? '';
$nama    = $_POST['Nama']    ?? '';
$jurusan = $_POST['Jurusan'] ?? '';
$alamat  = $_POST['Alamat']  ?? '';
$foto    = '';

if (isset($_FILES['foto']) && $_FILES['foto']['error'] == 0) {
    // Validate MIME type
    $allowed = ['image/jpeg', 'image/png', 'image/webp'];
    $mime    = mime_content_type($_FILES['foto']['tmp_name']);

    if (!in_array($mime, $allowed)) {
        echo json_encode(["status" => "gagal", "pesan" => "Tipe file tidak diizinkan. Gunakan JPG, PNG, atau WEBP."]);
        exit();
    }

    // Auto-create uploads/ folder if it doesn't exist
    $uploadDir = __DIR__ . '/uploads/';
    if (!is_dir($uploadDir)) {
        mkdir($uploadDir, 0755, true);
    }

    $ext  = pathinfo($_FILES['foto']['name'], PATHINFO_EXTENSION);
    $foto = time() . '_' . uniqid() . '.' . strtolower($ext);

    if (!move_uploaded_file($_FILES['foto']['tmp_name'], $uploadDir . $foto)) {
        echo json_encode(["status" => "gagal", "pesan" => "Gagal menyimpan file foto."]);
        exit();
    }
}

$nim     = mysqli_real_escape_string($koneksi, $nim);
$nama    = mysqli_real_escape_string($koneksi, $nama);
$jurusan = mysqli_real_escape_string($koneksi, $jurusan);
$alamat  = mysqli_real_escape_string($koneksi, $alamat);
$foto    = mysqli_real_escape_string($koneksi, $foto);

$query = "INSERT INTO data (NIM, Nama, Jurusan, Alamat, foto)
          VALUES ('$nim', '$nama', '$jurusan', '$alamat', '$foto')";

if (mysqli_query($koneksi, $query)) {
    echo json_encode(["status" => "sukses", "pesan" => "Data berhasil ditambah"]);
} else {
    echo json_encode(["status" => "gagal", "pesan" => "Gagal: " . mysqli_error($koneksi)]);
}
?>