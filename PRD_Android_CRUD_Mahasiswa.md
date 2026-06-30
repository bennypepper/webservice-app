# PRD — Aplikasi Android CRUD Data Mahasiswa (Web Service)

> **Project:** UAS Web Service — Teknik Informatika Ma Chung  
> **Tim:** Alfond, Steven, Rivian, Ivan  
> **Backend Live URL:** `https://devwsp.infinityfree.io`  
> **Status Backend:** ✅ Running (verified 30 Jun 2026)

---

## 1. Ringkasan Produk

Aplikasi Android native yang berfungsi sebagai **client** untuk web service PHP yang sudah di-deploy di InfinityFree. Semua operasi CRUD terhadap data mahasiswa dilakukan melalui HTTP request ke endpoint PHP, yang kemudian berinteraksi dengan database MySQL. Tidak ada koneksi langsung dari Android ke database.

### Arsitektur

```
Android App  ←→  HTTPS  ←→  PHP Endpoints  ←→  MySQL (InfinityFree)
               (Retrofit)   devwsp.infinityfree.io
```

---

## 2. Endpoint Web Service (Live)

Base URL: `https://devwsp.infinityfree.io`

### 2.1 GET /mahasiswa.php — Read All

**Response JSON aktual (verified):**
```json
[
  {
    "id": "5",
    "NIM": "312210021",
    "Nama": "Rivian Naufal",
    "Jurusan": "Teknik Informatika",
    "Alamat": "Merjosari",
    "foto": ""
  },
  {
    "id": "2",
    "NIM": "312210011",
    "Nama": "Ivan Lie",
    "Jurusan": "Teknik Informatika",
    "Alamat": "Villa Puncak Tidar H-21",
    "foto": ""
  }
]
```

> **PENTING:** Field JSON menggunakan **PascalCase** (`NIM`, `Nama`, `Jurusan`, `Alamat`, `foto`) dan ada field `id` tambahan. Sesuaikan model class Android!

### 2.2 POST /insert.php — Create

**Request body (form-data / urlencoded):**
```
NIM=312210099&Nama=Benny&Jurusan=Teknik Informatika&Alamat=Malang
```

**Expected Response:**
```json
{"status": "sukses", "pesan": "Data berhasil ditambahkan"}
```

### 2.3 POST /update.php — Update

**Request body:**
```
NIM=312210099&Nama=Benny Updated&Jurusan=Teknik Informatika&Alamat=Malang Baru
```

**Expected Response:**
```json
{"status": "sukses", "pesan": "Data berhasil diupdate"}
```

### 2.4 POST /delete.php — Delete

**Request body:**
```
NIM=312210099
```

**Expected Response:**
```json
{"status": "sukses", "pesan": "Data berhasil dihapus"}
```

### 2.5 POST /login.php — Login (Bonus)

**Request body:**
```
username=admin&password=admin123
```

**Expected Response:**
```json
{"status": "sukses"} atau {"status": "gagal", "pesan": "..."}
```

---

## 3. Model Data Android

```java
// Mahasiswa.java
public class Mahasiswa {
    private String id;
    private String NIM;
    private String Nama;
    private String Jurusan;
    private String Alamat;
    private String foto;
    
    // Getter & Setter untuk semua field
}
```

> **Catatan:** Gunakan `@SerializedName("NIM")` jika nama field Java-nya berbeda (konvensi Java = camelCase, tapi JSON server = PascalCase).

---

## 4. Tech Stack Android

| Komponen | Library/Tool | Versi |
|---|---|---|
| Language | Java (atau Kotlin) | — |
| Min SDK | API 21 (Android 5.0) | — |
| HTTP Client | Retrofit 2 | 2.9.0 |
| JSON Parser | Gson Converter | 2.9.0 |
| UI List | RecyclerView | latest |
| Swipe Refresh | SwipeRefreshLayout | latest |
| Build Tool | Gradle | — |

### Gradle Dependencies
```gradle
dependencies {
    implementation 'com.squareup.retrofit2:retrofit:2.9.0'
    implementation 'com.squareup.retrofit2:converter-gson:2.9.0'
    implementation 'androidx.recyclerview:recyclerview:1.3.2'
    implementation 'androidx.swiperefreshlayout:swiperefreshlayout:1.1.0'
}
```

### AndroidManifest.xml
```xml
<uses-permission android:name="android.permission.INTERNET" />
```

---

## 5. Struktur Layar Aplikasi

### Layar 1: LoginActivity
- Input: Username, Password
- Tombol: Login
- On success: pindah ke MainActivity
- Credentials: `admin` / `admin123`

### Layar 2: MainActivity (List)
- RecyclerView menampilkan daftar mahasiswa (NIM, Nama, Jurusan, Alamat)
- SwipeRefreshLayout untuk pull-to-refresh
- SearchView untuk pencarian (bonus)
- FloatingActionButton atau tombol "Tambah Data"
- Tap pada item → EditActivity

### Layar 3: TambahActivity (Create)
- Input fields: NIM, Nama, Jurusan, Alamat
- Tombol: Simpan, Batal
- Validasi: semua field wajib diisi, NIM tidak duplikat
- On success: kembali ke MainActivity dan refresh

### Layar 4: EditActivity (Update/Delete)
- Pre-filled dengan data mahasiswa yang dipilih
- Tombol: Update, Delete, Kembali
- On update/delete success: kembali ke MainActivity dan refresh

---

## 6. Retrofit API Interface

```java
public interface ApiService {
    
    @GET("mahasiswa.php")
    Call<List<Mahasiswa>> getAllMahasiswa();
    
    @FormUrlEncoded
    @POST("insert.php")
    Call<ResponseBody> insertMahasiswa(
        @Field("NIM") String nim,
        @Field("Nama") String nama,
        @Field("Jurusan") String jurusan,
        @Field("Alamat") String alamat
    );
    
    @FormUrlEncoded
    @POST("update.php")
    Call<ResponseBody> updateMahasiswa(
        @Field("NIM") String nim,
        @Field("Nama") String nama,
        @Field("Jurusan") String jurusan,
        @Field("Alamat") String alamat
    );
    
    @FormUrlEncoded
    @POST("delete.php")
    Call<ResponseBody> deleteMahasiswa(
        @Field("NIM") String nim
    );
    
    @FormUrlEncoded
    @POST("login.php")
    Call<ResponseBody> login(
        @Field("username") String username,
        @Field("password") String password
    );
}
```

### Retrofit Client

```java
public class RetrofitClient {
    private static final String BASE_URL = "https://devwsp.infinityfree.io/";
    private static Retrofit retrofit = null;
    
    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        }
        return retrofit;
    }
    
    public static ApiService getApiService() {
        return getClient().create(ApiService.class);
    }
}
```

---

## 7. Struktur Proyek Android Studio

```
app/
├── manifests/
│   └── AndroidManifest.xml          ← tambah INTERNET permission
├── java/com.yourpackage.mahasiswaapp/
│   ├── model/
│   │   └── Mahasiswa.java           ← Data model
│   ├── network/
│   │   ├── ApiService.java          ← Retrofit interface
│   │   └── RetrofitClient.java      ← Singleton client
│   ├── adapter/
│   │   └── MahasiswaAdapter.java    ← RecyclerView adapter
│   └── activity/
│       ├── LoginActivity.java
│       ├── MainActivity.java
│       ├── TambahActivity.java
│       └── EditActivity.java
└── res/
    ├── layout/
    │   ├── activity_login.xml
    │   ├── activity_main.xml
    │   ├── activity_tambah.xml
    │   ├── activity_edit.xml
    │   └── item_mahasiswa.xml       ← Item layout RecyclerView
    └── values/
        └── strings.xml
```

---

## 8. Roadmap Implementasi (Urutan Kerja)

### Tahap 1 — Setup Project (30 min)
- [ ] Buat project baru di Android Studio (Empty Views Activity, Java)
- [ ] Tambah dependencies Retrofit + Gson di `build.gradle`
- [ ] Tambah INTERNET permission di `AndroidManifest.xml`
- [ ] Buat package structure (`model`, `network`, `adapter`, `activity`)

### Tahap 2 — Model & Network Layer (20 min)
- [ ] Buat `Mahasiswa.java` dengan field: `id`, `NIM`, `Nama`, `Jurusan`, `Alamat`, `foto`
- [ ] Buat `ApiService.java` dengan semua endpoint
- [ ] Buat `RetrofitClient.java` dengan base URL

### Tahap 3 — Read (GET) + RecyclerView (45 min)
- [ ] Buat `item_mahasiswa.xml` (layout satu item list)
- [ ] Buat `MahasiswaAdapter.java`
- [ ] Buat `activity_main.xml` dengan RecyclerView + SwipeRefreshLayout
- [ ] Implementasi `MainActivity.java`: fetch data, tampilkan di RecyclerView
- [ ] Test: data dari server harus muncul di list ✓

### Tahap 4 — Create (POST Insert) (30 min)
- [ ] Buat `activity_tambah.xml` (form input)
- [ ] Implementasi `TambahActivity.java`: kirim POST, handle response
- [ ] Setelah simpan: kembali ke MainActivity dan refresh list
- [ ] Test: tambah data baru, cek muncul di list ✓

### Tahap 5 — Update & Delete (30 min)
- [ ] Buat `activity_edit.xml` (form pre-filled)
- [ ] Implementasi `EditActivity.java`: update dan delete
- [ ] Pass data dari MainActivity ke EditActivity via Intent extras
- [ ] Test: edit dan hapus data ✓

### Tahap 6 — Login (Bonus) (20 min)
- [ ] Buat `activity_login.xml`
- [ ] Implementasi `LoginActivity.java`
- [ ] Set `LoginActivity` sebagai launcher activity
- [ ] Test: login dengan admin/admin123 ✓

### Tahap 7 — Polish & Bonus (30 min)
- [ ] SearchView di MainActivity
- [ ] Validasi input (field kosong, format NIM)
- [ ] Loading indicator (ProgressBar/ProgressDialog)
- [ ] Error handling (koneksi gagal, response error)
- [ ] Toast messages untuk feedback user

---

## 9. Mapping Nilai

| Aspek | Bobot | Implementasi |
|---|---|---|
| Desain Database | 10% | Sudah done (Rivian/Ivan) |
| Pembuatan Web Service | 25% | Sudah done (Rivian/Ivan) |
| Fungsi Insert | 15% | TambahActivity |
| Fungsi Read | 15% | MainActivity + RecyclerView |
| Fungsi Update | 15% | EditActivity → update.php |
| Fungsi Delete | 10% | EditActivity → delete.php |
| Tampilan dan UX | 5% | Material Design, loading indicator |
| Dokumentasi | 5% | Laporan PDF |
| **Bonus** | +10% | Search, Validasi, SwipeRefresh, Login |

---

## 10. Catatan Teknis Penting

> **Field Naming:** Server mengembalikan `NIM` (huruf kapital semua), bukan `nim`. Pastikan `@SerializedName` di model Java sesuai, atau gunakan nama field yang persis sama.

> **HTTPS:** Server sudah pakai HTTPS (devwsp.infinityfree.io), tidak perlu konfigurasi cleartext. Retrofit akan bekerja langsung.

> **Response Format Login:** Server menggunakan `"status": "sukses"` (bukan `"success"`). Sesuaikan kondisi pengecekan di Java.

> **Foto:** Field `foto` ada di response, isinya nama file atau string kosong. Untuk tampilan foto (bonus), perlu load dari URL base + `/uploads/` + nama_file.

---

*Dokumen ini berdasarkan analisis PDF soal UAS + inspeksi langsung endpoint live.*
