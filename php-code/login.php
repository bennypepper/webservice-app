<?php
session_start();
include 'koneksi.php';

$user = $_POST['username'] ?? '';
$pass = $_POST['password'] ?? '';

// Periksa apakah username ada di tabel
$query = mysqli_query($koneksi, "SELECT * FROM users WHERE username='$user'");

if (mysqli_num_rows($query) == 0) {
    echo json_encode(["status" => "gagal", "pesan" => "Username tidak ditemukan di database"]);
} else {
    $data = mysqli_fetch_assoc($query);
    // Cek password langsung dari database
    if ($data['password'] == $pass) {
        $_SESSION['login'] = true;
        echo json_encode(["status" => "sukses", "pesan" => "Login berhasil"]);
    } else {
        echo json_encode(["status" => "gagal", "pesan" => "Password salah."]);
    }
}
?>