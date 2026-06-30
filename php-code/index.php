<?php
session_start();
$isLoggedIn = isset($_SESSION['login']);
?>
<!DOCTYPE html>
<html lang="id">
<head>
    <meta charset="UTF-8">
    <title>Sistem Data Mahasiswa</title>
    <style>
        body { font-family: sans-serif; padding: 20px; transition: 0.3s; background: #fff; color: #000; }
        body.dark-mode { background: #121212; color: #fff; }
        table { width: 100%; border-collapse: collapse; margin-top: 10px; }
        th, td { border: 1px solid #ccc; padding: 10px; text-align: left; }
        .dark-mode th, .dark-mode td { border-color: #444; }
        .dark-mode input { background: #333; color: #fff; border: 1px solid #555; }
        .dark-mode form { background: #1e1e1e; border-color: #444; }
        form { border: 1px solid #ccc; padding: 15px; margin-bottom: 20px; border-radius: 5px; }
        input, button { padding: 8px; margin: 5px 0; display: block; width: 100%; max-width: 300px; }
        .btn-delete { background: #dc3545; color: #fff; border: none; cursor: pointer; }
        .btn-edit { background: #ffc107; color: #000; border: none; cursor: pointer; }
        img.foto-mhs { width: 50px; height: 50px; object-fit: cover; border-radius: 5px; }
    </style>
</head>
<body>

<button id="darkToggle">Dark Mode</button>

<?php if (!$isLoggedIn): ?>
    <form id="loginForm">
        <h3>Login Sistem</h3>
        <input type="text" name="username" placeholder="Username" required>
        <input type="password" name="password" placeholder="Password" required>
        <button type="submit">Login</button>
    </form>
<?php else: ?>
    <a href="logout.php"><button type="button">Logout</button></a>
    <h2>Kelola Data Mahasiswa</h2>
    
    <div style="display: flex; gap: 5px; margin-bottom: 15px;">
        <input type="text" id="cari" placeholder="Cari Nama atau NIM...">
        <button type="button" onclick="ambilData()">Cari</button>
    </div>

    <form id="mhsForm">
        <h3 id="formTitle">Tambah Data</h3>
        <input type="hidden" id="hidden_id" name="id">
        <input type="number" id="nim" name="NIM" placeholder="NIM" required>
        <input type="text" id="nama" name="Nama" placeholder="Nama Lengkap" required>
        <input type="text" id="jurusan" name="Jurusan" placeholder="Jurusan" required>
        <input type="text" id="alamat" name="Alamat" placeholder="Alamat" required>
        <input type="file" id="foto" name="foto" accept="image/*">
        <button type="submit" id="btnSubmit">Simpan Data</button>
        <button type="button" id="btnBatal" style="display:none;">Batal</button>
    </form>

    <table>
        <thead><tr><th>Foto</th><th>NIM</th><th>Nama</th><th>Jurusan</th><th>Alamat</th><th>Aksi</th></tr></thead>
        <tbody id="tabelData"></tbody>
    </table>
<?php endif; ?>

<script>
    const body = document.body;
    const btnDark = document.getElementById('darkToggle');
    if (localStorage.getItem('theme') === 'dark') {
        body.classList.add('dark-mode');
        btnDark.innerText = 'Light Mode';
    }
    btnDark.onclick = () => {
        body.classList.toggle('dark-mode');
        const isDark = body.classList.contains('dark-mode');
        localStorage.setItem('theme', isDark ? 'dark' : 'light');
        btnDark.innerText = isDark ? 'Light Mode' : 'Dark Mode';
    };

    const lForm = document.getElementById('loginForm');
    if (lForm) {
        lForm.onsubmit = (e) => {
            e.preventDefault();
            fetch('login.php', { method: 'POST', body: new FormData(lForm) })
            .then(r => r.json())
            .then(d => {
                if (d.status === 'sukses') location.reload();
                else alert(d.pesan);
            });
        };
    }

    <?php if ($isLoggedIn): ?>
    const mForm = document.getElementById('mhsForm');
    let urlTarget = 'insert.php';

    function ambilData() {
        const keyword = document.getElementById('cari').value;
        fetch('mahasiswa.php?cari=' + keyword)
        .then(r => r.json())
        .then(d => {
            const tbody = document.getElementById('tabelData');
            tbody.innerHTML = '';
            d.forEach(row => {
                const foto = row.foto ? 'uploads/' + row.foto : 'https://via.placeholder.com/50';
                tbody.innerHTML += `<tr>
                    <td><img src="${foto}" class="foto-mhs" alt="Foto"></td>
                    <td>${row.NIM}</td><td>${row.Nama}</td><td>${row.Jurusan}</td><td>${row.Alamat}</td>
                    <td>
                        <button class="btn-edit" onclick="editData('${row.id}','${row.NIM}','${row.Nama}','${row.Jurusan}','${row.Alamat}')">Update</button>
                        <button class="btn-delete" onclick="hapus('${row.id}')">Delete</button>
                    </td></tr>`;
            });
        });
    }

    function editData(id, nim, nama, jurusan, alamat) {
        document.getElementById('formTitle').innerText = 'Update Data';
        document.getElementById('hidden_id').value = id;
        document.getElementById('nim').value = nim;
        document.getElementById('nama').value = nama;
        document.getElementById('jurusan').value = jurusan;
        document.getElementById('alamat').value = alamat;
        document.getElementById('btnSubmit').innerText = 'Update Data';
        document.getElementById('btnBatal').style.display = 'block';
        urlTarget = 'update.php';
    }

    document.getElementById('btnBatal').onclick = () => {
        mForm.reset();
        document.getElementById('formTitle').innerText = 'Tambah Data';
        document.getElementById('btnSubmit').innerText = 'Simpan Data';
        document.getElementById('btnBatal').style.display = 'none';
        urlTarget = 'insert.php';
    };

    mForm.onsubmit = (e) => {
        e.preventDefault();
        fetch(urlTarget, { method: 'POST', body: new FormData(mForm) })
        .then(r => r.json())
        .then(d => { alert(d.pesan); document.getElementById('btnBatal').click(); ambilData(); });
    };

    function hapus(id) {
        if(confirm('Hapus data ini?')) {
            const fd = new FormData();
            fd.append('id', id);
            fetch('delete.php', { method: 'POST', body: fd })
            .then(r => r.json())
            .then(d => { alert(d.pesan); ambilData(); });
        }
    }
    ambilData();
    <?php endif; ?>
</script>
</body>
</html>