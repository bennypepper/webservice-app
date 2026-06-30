# PROYEK AKHIR MATA KULIAH WEB SERVICE

**Universitas Ma Chung — Fakultas Sains dan Teknologi**
Gedung Bhakti Persada lt.1, Villa Puncak Tidar N-01, Malang 65151, Indonesia

---

## Informasi Mata Kuliah

| Atribut | Detail |
|---|---|
| Semester | Genap 2025/2026 |
| Mata Kuliah | Web Service |
| Program Studi | Teknik Informatika |
| Bentuk UAS | Project |
| Durasi | 135 Menit |
| Waktu | 13.30–15.00 |
| Dosen Pengampu | Mochamad Subianto, S.Kom., M.Cs. |

---

## Judul Proyek

**Aplikasi Mobile Android CRUD Data Mahasiswa Menggunakan Web Service**

---

## Deskripsi Project

Buatlah sebuah aplikasi mobile berbasis Android yang terhubung dengan Web Service yang berada pada domain/server hosting. Aplikasi digunakan untuk mengelola data mahasiswa dengan fitur **Create, Read, Update, dan Delete (CRUD)**.

---

## Struktur Data Mahasiswa

| Field | Tipe Data |
|---|---|
| NIM | VARCHAR(15) — Primary Key |
| Nama | VARCHAR(100) |
| Jurusan | VARCHAR(100) |
| Alamat | TEXT |

---

## Web Service (PHP)

Buat Web Service berbasis PHP yang menghasilkan data dalam format **JSON**.

### Endpoint Minimal

| Endpoint | Method | Fungsi |
|---|---|---|
| `mahasiswa.php` | GET | Menampilkan seluruh data mahasiswa |
| `insert.php` | POST | Menambah data mahasiswa |
| `update.php` | POST | Mengubah data mahasiswa |
| `delete.php` | POST | Menghapus data mahasiswa berdasarkan NIM |

---

## Aplikasi Android

Buat aplikasi Android Studio dengan fitur berikut:

### Halaman Utama
- Menampilkan daftar mahasiswa dari Web Service menggunakan **ListView atau RecyclerView**
- Tombol **Tambah Data**

### Form Tambah Data
**Input:**
- NIM
- Nama
- Jurusan
- Alamat

**Tombol:**
- Simpan
- Batal

### Form Edit Data
Ketika salah satu data dipilih:
- Menampilkan data mahasiswa
- Dapat melakukan update data
- Dapat menghapus data

**Tombol:**
- Update
- Delete
- Kembali

---

## Output yang Dikumpulkan

1. Source Code Android Studio
2. Source Code Web Service (PHP)
3. File Database (`.sql`)
4. APK hasil build
5. Laporan Project (PDF): Sampul, Tugas Anggota, Source Code Android, Source Code Web Service

---

## Kriteria Penilaian

| Aspek Penilaian | Bobot |
|---|---|
| Desain Database | 10% |
| Pembuatan Web Service | 25% |
| Fungsi Insert | 15% |
| Fungsi Read | 15% |
| Fungsi Update | 15% |
| Fungsi Delete | 10% |
| Tampilan dan UX | 5% |
| Dokumentasi & Presentasi | 5% |
| **Total** | **100%** |

---

## Bonus (+10 Nilai)

Tambahkan fitur:
- Pencarian data mahasiswa
- Validasi input
- Swipe Refresh
- Login pengguna
- Upload foto mahasiswa

---

## Pengumpulan

- **Deadline:** Sesuai jadwal UAS
- **Dikerjakan:** Kelompok

---

*Selamat Mengerjakan — Yakinlah dengan kemampuan Anda Sendiri*
