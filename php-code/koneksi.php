<?php
$host = "sql213.byethost11.com";
$user = "b11_42305866";
$pass = "nf67wd4s";
$db   = "b11_42305866_database";

$koneksi = mysqli_connect($host, $user, $pass, $db);

if (mysqli_connect_errno()) {
    echo "Koneksi Gagal: " . mysqli_connect_error();
    exit();
}
?>