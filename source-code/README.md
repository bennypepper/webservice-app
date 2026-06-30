# Source Code — Aplikasi Android CRUD Data Mahasiswa
## UAS Web Service | Teknik Informatika | Ma Chung University | Genap 2025/2026

**Backend URL:** `https://devwsp.infinityfree.io`  
**Package:** `com.example.uas_webservice`  
**Language:** Java | **Min SDK:** API 24 (Android 7.0)

---

## Struktur Folder

```
source-code/
├── java/
│   ├── model/
│   │   └── Mahasiswa.java          → Data model (NIM, Nama, Jurusan, Alamat, foto)
│   ├── network/
│   │   ├── ApiService.java         → Retrofit interface (5 endpoint)
│   │   └── RetrofitClient.java     → HTTP client singleton
│   ├── adapter/
│   │   └── MahasiswaAdapter.java   → RecyclerView adapter + search filter
│   ├── LoginActivity.java          → Halaman login (admin/admin123)
│   ├── MainActivity.java           → Halaman utama — list + search + swipe refresh
│   ├── TambahActivity.java         → Form tambah data (INSERT)
│   └── EditActivity.java           → Form edit/hapus data (UPDATE + DELETE)
├── layout/
│   ├── activity_login.xml          → Layout halaman login
│   ├── activity_main.xml           → Layout halaman utama (RecyclerView + FAB)
│   ├── activity_tambah.xml         → Layout form tambah
│   ├── activity_edit.xml           → Layout form edit
│   └── item_mahasiswa.xml          → Layout satu item di RecyclerView (CardView)
└── config/
    ├── AndroidManifest.xml         → Permission INTERNET + deklarasi 4 activity
    └── build.gradle.kts            → Dependencies Retrofit, RecyclerView, dll
```

---

## Endpoint Web Service

| File PHP | Method | Fungsi |
|---|---|---|
| `mahasiswa.php` | GET | Ambil semua data mahasiswa |
| `insert.php` | POST | Tambah data mahasiswa |
| `update.php` | POST | Update data mahasiswa |
| `delete.php` | POST | Hapus data berdasarkan NIM |
| `login.php` | POST | Login sistem |

**Format JSON Response (GET mahasiswa.php):**
```json
[
  {
    "id": "5",
    "NIM": "312210021",
    "Nama": "Rivian Naufal",
    "Jurusan": "Teknik Informatika",
    "Alamat": "Merjosari",
    "foto": ""
  }
]
```

---

## Fitur Aplikasi

| Fitur | Status | File |
|---|---|---|
| Tampilkan list mahasiswa (READ) | ✅ | `MainActivity.java` |
| Tambah data (INSERT) | ✅ | `TambahActivity.java` |
| Edit data (UPDATE) | ✅ | `EditActivity.java` |
| Hapus data (DELETE) | ✅ | `EditActivity.java` |
| Login pengguna (BONUS) | ✅ | `LoginActivity.java` |
| Search/Pencarian (BONUS) | ✅ | `MahasiswaAdapter.java` |
| Swipe Refresh (BONUS) | ✅ | `MainActivity.java` |
| Validasi input (BONUS) | ✅ | `TambahActivity.java`, `EditActivity.java` |
| Konfirmasi sebelum hapus | ✅ | `EditActivity.java` |

---

## Dependencies (build.gradle.kts)

```gradle
implementation("com.squareup.retrofit2:retrofit:2.9.0")
implementation("com.squareup.retrofit2:converter-gson:2.9.0")
implementation("androidx.recyclerview:recyclerview:1.3.2")
implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
implementation("androidx.cardview:cardview:1.0.0")
```

---

## Alur Aplikasi

```
[LoginActivity]
     ↓ (login sukses)
[MainActivity] ←──────────────────────────┐
     │                                     │
     ├─ tap item → [EditActivity]          │
     │              ├─ UPDATE → finish()   ┘ (onResume refresh)
     │              ├─ DELETE → finish()
     │              └─ KEMBALI → finish()
     │
     └─ FAB (+) → [TambahActivity]
                   ├─ SIMPAN → finish()
                   └─ BATAL  → finish()
```

---

*Generated for UAS Web Service Report | Ma Chung University 2026*
