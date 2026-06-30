# Aplikasi Android CRUD Mahasiswa (Web Service)
## Proyek Akhir Web Service | Teknik Informatika | Universitas Ma Chung

Aplikasi mobile Android native yang terhubung dengan PHP Web Service untuk melakukan operasi CRUD (Create, Read, Update, Delete) pada data mahasiswa yang disimpan di database MySQL (hosting ByetHost).

---

## 🚀 Struktur Proyek

```
webservice-app/
├── php-code/              # PHP Web Service & Web Panel
│   ├── koneksi.php        # Konfigurasi database MySQL
│   ├── mahasiswa.php      # GET: Mengambil data / pencarian
│   ├── insert.php         # POST: Menambah data mahasiswa (mendukung upload foto)
│   ├── update.php         # POST: Memperbarui data mahasiswa (by id/NIM)
│   ├── delete.php         # POST: Menghapus data mahasiswa (by id/NIM)
│   ├── login.php          # POST: Endpoint login pengguna
│   ├── logout.php         # Menghapus session login
│   └── index.php          # Dashboard web panel administrasi
│
├── source-code/           # Salinan Kode Aplikasi Android Studio
│   ├── java/              # Source code java classes
│   ├── layout/            # XML Layout activity & items
│   ├── drawable/          # Vector drawable & styles
│   └── config/            # Manifest dan Gradle build
│
├── endpoint-test/         # Skrip Pengujian Endpoint
│   └── test_endpoints.py  # Python automated test (Playwright)
│
├── database.sql           # Skrip SQL Skema Database
└── README.md              # Dokumentasi proyek
```

---

## 🛠️ Persyaratan Lingkungan

### 1. Database & Hosting
- Server Web dengan dukungan PHP 7.4+ dan MySQL (disarankan ByetHost/InfinityFree).
- Database MySQL dengan tabel `data` dan `users` (dapat diimpor dari `database.sql`).

### 2. Android App
- Android Studio Koala atau yang lebih baru.
- SDK Minimum: API 24 (Android 7.0).
- Bahasa Pemrograman: Java.

### 3. Pengujian (Python)
- Python 3.7+
- Playwright (diperlukan untuk bypass bot-challenge ByetHost/InfinityFree).

---

## 📦 Panduan Instalasi & Penggunaan

### 1. Setup Backend
1. Masuk ke cPanel hosting Anda, lalu buat database baru melalui **MySQL Databases**.
2. Masuk ke **phpMyAdmin**, pilih database baru tersebut, lalu impor berkas `database.sql`.
3. Buka berkas `php-code/koneksi.php` dan sesuaikan kredensial database Anda:
   ```php
   $host = "HOST_MySQL_Anda";
   $user = "USER_MySQL_Anda";
   $pass = "PASSWORD_MySQL_Anda";
   $db   = "NAMA_Database_Anda";
   ```
4. Unggah seluruh isi direktori `php-code/` ke direktori root hosting Anda (misal `htdocs/` atau `public_html/`).

### 2. Run / Build Android App
1. Buka folder Android project Anda di Android Studio (direktori asli project, bukan salinan `source-code`).
2. Pastikan file `network/RetrofitClient.java` mengarah ke URL backend hosting Anda:
   ```java
   private static final String BASE_URL = "https://domain-anda.byethost11.com/";
   ```
3. Lakukan **Sync Project with Gradle Files**.
4. Run aplikasi pada emulator atau perangkat Android fisik.
5. Kredensial login bawaan:
   - **Username:** `admin`
   - **Password:** `admin123`

### 3. Menjalankan Skrip Pengujian Endpoint
Skrip pengujian otomatis menggunakan Playwright untuk menyelesaikan tantangan keamanan JavaScript dari ByetHost:
```bash
pip install playwright requests
playwright install chromium
cd endpoint-test
python test_endpoints.py
```

---

## 🌟 Fitur Utama
1. **CRUD Mahasiswa:** Mendukung penambahan, pembacaan, pembaruan, dan penghapusan data mahasiswa.
2. **Keamanan Bot-Bypass:** Integrasi `BotBypassHelper` di Android menggunakan WebView headless agar request Retrofit lolos dari tantangan AES ByetHost.
3. **Login Pengguna:** Halaman autentikasi aman sebelum masuk ke menu utama.
4. **Swipe-to-Refresh & Search:** Memudahkan memuat ulang data terbaru dan mencari mahasiswa secara real-time.
5. **Dashboard Web Administrasi:** Halaman antarmuka web responsif untuk mengelola data mahasiswa secara langsung dari browser.
