# Student CRUD Android Application (Web Service)
## Web Service Final Project | Informatics | Ma Chung University

An Android native mobile application connected to a PHP Web Service to perform CRUD (Create, Read, Update, Delete) operations on student data stored in a MySQL database (hosted on ByetHost).

---

## 👥 Team Members (Alphabetical Order)
- Alfond Manuel Joseph
- Benedict Michael Pepper
- Ivan Lie Nagasena
- Rivian Naufal Dzakki Pradana
- Steven Surya Putra

---

## 🚀 Project Structure

```
webservice-app/
├── php-code/              # PHP Web Service & Web Panel
│   ├── koneksi.php        # MySQL Database connection configuration
│   ├── mahasiswa.php      # GET: Retrieve data / search student
│   ├── insert.php         # POST: Create a new student (supports photo upload)
│   ├── update.php         # POST: Update student details (by id/NIM)
│   ├── delete.php         # POST: Delete student (by id/NIM)
│   ├── login.php          # POST: User login endpoint
│   ├── logout.php         # Destroys login session
│   └── index.php          # Administration web panel dashboard
│
├── source-code/           # Copy of Android Studio Project Files
│   ├── java/              # Java source code classes
│   ├── layout/            # XML Layouts for activities and items
│   ├── drawable/          # Vector drawables and style configurations
│   └── config/            # AndroidManifest and Gradle configurations
│
├── endpoint-test/         # Automated Endpoint Test Scripts
│   └── test_endpoints.py  # Python automated test script (Playwright)
│
├── database.sql           # Database schema SQL script
└── README.md              # Project documentation
```

---

## 🛠️ Environment Requirements

### 1. Database & Hosting
- Web Server with PHP 7.4+ and MySQL support (ByetHost/InfinityFree recommended).
- MySQL database with `data` and `users` tables (can be imported from `database.sql`).

### 2. Android App
- Android Studio Koala or newer.
- Minimum SDK: API 24 (Android 7.0).
- Programming Language: Java.

### 3. Testing (Python)
- Python 3.7+
- Playwright (required to bypass ByetHost/InfinityFree security bot-challenge).

---

## 📦 Setup & Usage Guide

### 1. Backend Setup
1. Log in to your hosting cPanel and create a new database under **MySQL Databases**.
2. Open **phpMyAdmin**, select your newly created database, and import the `database.sql` script.
3. Open `php-code/koneksi.php` and configure your database credentials:
   ```php
   $host = "your_mysql_host";
   $user = "your_mysql_user";
   $pass = "your_mysql_password";
   $db   = "your_mysql_database_name";
   ```
4. Upload all files from the `php-code/` directory to the document root of your hosting provider (typically `htdocs/` or `public_html/`).

### 2. Run / Build Android App
1. Open your Android Studio project in Android Studio (the main project directory, not the `source-code` copy folder).
2. Ensure the base URL in `network/RetrofitClient.java` points to your backend URL:
   ```java
   private static final String BASE_URL = "https://your-domain.byethost11.com/";
   ```
3. Run **Sync Project with Gradle Files**.
4. Run the application on an emulator or a physical Android device.
5. Default login credentials:
   - **Username:** `admin`
   - **Password:** `admin123`

### 3. Running Endpoint Tests
Run the automated test suite using Playwright to complete the ByetHost JavaScript security challenge:
```bash
pip install playwright requests
playwright install chromium
cd endpoint-test
python test_endpoints.py
```

---

## 🌟 Key Features
1. **Student CRUD:** Support creating, reading, updating, and deleting student data.
2. **Headless Security Bot-Bypass:** Integrated `BotBypassHelper` in Android using a headless WebView to bypass the ByetHost AES security verification for Retrofit calls.
3. **User Authentication:** Login gate to prevent unauthorized access.
4. **Swipe-to-Refresh & Search:** Real-time client-side student search and smooth pull-to-refresh actions.
5. **Web Administration Dashboard:** Responsive admin web interface to manage student data directly from desktop browsers.
