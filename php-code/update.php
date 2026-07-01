<?php
header('Content-Type: application/json');
include 'koneksi.php';

$id      = $_POST['id']      ?? '';
$nim     = $_POST['NIM']     ?? '';
$nama    = $_POST['Nama']    ?? '';
$jurusan = $_POST['Jurusan'] ?? '';
$alamat  = $_POST['Alamat']  ?? '';
$fotoClause = '';

// Handle optional photo update
if (isset($_FILES['foto']) && $_FILES['foto']['error'] == 0) {
    $allowed = ['image/jpeg', 'image/png', 'image/webp'];
    $mime    = mime_content_type($_FILES['foto']['tmp_name']);

    if (!in_array($mime, $allowed)) {
        echo json_encode(["status" => "gagal", "pesan" => "Tipe file tidak diizinkan. Gunakan JPG, PNG, atau WEBP."]);
        exit();
    }

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

    $foto = mysqli_real_escape_string($koneksi, $foto);
    $fotoClause = ", foto='$foto'";
}

$id      = mysqli_real_escape_string($koneksi, $id);
$nim     = mysqli_real_escape_string($koneksi, $nim);
$nama    = mysqli_real_escape_string($koneksi, $nama);
$jurusan = mysqli_real_escape_string($koneksi, $jurusan);
$alamat  = mysqli_real_escape_string($koneksi, $alamat);

if (!empty($id)) {
    $query = "UPDATE data SET NIM='$nim', Nama='$nama', Jurusan='$jurusan', Alamat='$alamat'$fotoClause WHERE id='$id'";
} elseif (!empty($nim)) {
    $query = "UPDATE data SET Nama='$nama', Jurusan='$jurusan', Alamat='$alamat'$fotoClause WHERE NIM='$nim'";
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