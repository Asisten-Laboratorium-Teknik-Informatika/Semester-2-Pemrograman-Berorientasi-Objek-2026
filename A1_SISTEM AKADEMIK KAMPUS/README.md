# 📚 PANDUAN LENGKAP — Sistem Informasi Akademik Kampus
## Kelompok 4 | Java + PostgreSQL

---

## 🗂️ STRUKTUR FOLDER PROJECT

```
ProjectAkhir/
│
├── lib/
│   └── postgresql-42.7.3.jar       ← WAJIB ADA (download dulu)
│
├── src/
│   ├── util/
│   │   └── DatabaseConnection.java  ← Konfigurasi koneksi DB
│   ├── model/
│   │   └── Models.java              ← Kelas model (Mahasiswa, Nilai, dll)
│   ├── dao/
│   │   └── DAOClasses.java          ← Operasi database (CRUD)
│   └── main/
│       └── MainApp.java             ← Aplikasi utama (GUI)
│
├── out/                             ← Hasil compile (.class files)
├── database_kelompok4_final.sql     ← File database PostgreSQL
└── README.md                        ← File ini
```

---

## ⚙️ LANGKAH 1: INSTALL TOOLS YANG DIBUTUHKAN

### A. Install Java JDK
1. Download dari: https://www.oracle.com/java/technologies/downloads/
   (Pilih JDK 17 atau 21, gratis)
2. Install dan ikuti wizard-nya
3. Cek di CMD/Terminal:
   ```
   java -version
   javac -version
   ```
   Harus muncul versi Java

### B. Install PostgreSQL
1. Download dari: https://www.postgresql.org/download/
2. Install, ingat password yang kamu set untuk user "postgres"
3. Cek apakah berjalan: buka pgAdmin atau jalankan:
   ```
   psql -U postgres
   ```

### C. Download Driver JDBC PostgreSQL
1. Buka: https://jdbc.postgresql.org/download/
2. Download file: **postgresql-42.7.3.jar** (atau versi terbaru)
3. Simpan di folder `lib/` dalam project kamu

---

## 🗄️ LANGKAH 2: SETUP DATABASE

### A. Buka pgAdmin atau psql terminal

### B. Jalankan file SQL
Di psql terminal:
```sql
\i /path/ke/file/database_kelompok4_final.sql
```

Atau di pgAdmin:
1. Buka pgAdmin → Connect ke server
2. Klik kanan → Query Tool
3. Buka file `database_kelompok4_final.sql`
4. Klik tombol Run (▶)

### C. Verifikasi database berhasil dibuat
```sql
\c ProjectAkhir
SET search_path TO kelompok4;
SELECT COUNT(*) FROM mahasiswa;
-- Harus menampilkan angka (misal: 4)
```

---

## ☕ LANGKAH 3: KONFIGURASI KONEKSI JAVA

Buka file `src/util/DatabaseConnection.java` dan sesuaikan:

```java
private static final String HOST     = "localhost";    // server PostgreSQL
private static final String PORT     = "5432";          // port default PostgreSQL
private static final String DB_NAME  = "ProjectAkhir"; // nama database
private static final String USERNAME = "postgres";      // ← GANTI username kamu
private static final String PASSWORD = "password_kalian"; // ← GANTI password kamu
```

> ⚠️ **PENTING:** Password harus sama dengan yang kamu set saat install PostgreSQL!

---

## 🔨 LANGKAH 4: COMPILE KODE JAVA

### Buat folder output dulu:
```bash
mkdir out
```

### Compile semua file Java:

**Windows (CMD):**
```batch
javac -cp ".;lib/postgresql-42.7.3.jar" ^
      src/util/DatabaseConnection.java ^
      src/model/Models.java ^
      src/dao/DAOClasses.java ^
      src/main/MainApp.java ^
      -d out/
```

**Mac/Linux (Terminal):**
```bash
javac -cp ".:lib/postgresql-42.7.3.jar" \
      src/util/DatabaseConnection.java \
      src/model/Models.java \
      src/dao/DAOClasses.java \
      src/main/MainApp.java \
      -d out/
```

> ✅ Jika berhasil: tidak ada pesan error, folder `out/` berisi file `.class`

---

## ▶️ LANGKAH 5: JALANKAN APLIKASI

**Windows:**
```batch
java -cp "out;lib/postgresql-42.7.3.jar" main.MainApp
```

**Mac/Linux:**
```bash
java -cp "out:lib/postgresql-42.7.3.jar" main.MainApp
```

---

## 🔧 MENGGUNAKAN IDE (DIREKOMENDASIKAN)

### IntelliJ IDEA (Paling mudah)
1. Download IntelliJ IDEA Community (gratis): https://www.jetbrains.com/idea/
2. Buka IntelliJ → File → New → Project from Existing Sources
3. Pilih folder ProjectAkhir
4. **Tambah Library JDBC:**
   - File → Project Structure → Libraries → klik "+"
   - Pilih file `lib/postgresql-42.7.3.jar`
   - Klik OK
5. Jalankan: klik kanan `MainApp.java` → Run 'MainApp.main()'

### Eclipse
1. Download Eclipse IDE for Java: https://www.eclipse.org/downloads/
2. File → Import → Existing Projects into Workspace
3. **Tambah Library:**
   - Klik kanan project → Properties → Java Build Path
   - Libraries → Add External JARs → pilih `postgresql-42.7.3.jar`
4. Jalankan: klik kanan `MainApp.java` → Run As → Java Application

### VS Code
1. Install extension: "Extension Pack for Java"
2. Buka folder project
3. Tambah ke `.classpath` atau setting `java.project.referencedLibraries`

---

## ❌ TROUBLESHOOTING — MASALAH UMUM

### Error: "Driver PostgreSQL tidak ditemukan"
```
Solusi: Pastikan file postgresql-42.7.3.jar ada di folder lib/
        dan sudah ditambah ke classpath saat compile & run
```

### Error: "Connection refused"
```
Solusi: 
1. Pastikan PostgreSQL sudah berjalan (cek di Services / pgAdmin)
2. Pastikan PORT benar (default: 5432)
3. Coba: psql -U postgres -h localhost
```

### Error: "password authentication failed"
```
Solusi: Periksa USERNAME dan PASSWORD di DatabaseConnection.java
        Harus sama dengan yang diset saat install PostgreSQL
```

### Error: "database ProjectAkhir does not exist"
```
Solusi: Jalankan dulu database_kelompok4_final.sql di pgAdmin/psql
```

### Error saat compile: "package model does not exist"
```
Solusi: Compile semua file sekaligus dalam satu perintah javac
        Jangan compile satu per satu
```

### Tampilan GUI tidak muncul / error Swing
```
Solusi: Pastikan menggunakan Java JDK (bukan JRE)
        Cek dengan: java -version (harus muncul versi JDK)
```

---

## 📊 PERBAIKAN DATABASE (Apa yang Ditambahkan)

| Fitur Lama | Fitur Baru |
|-----------|------------|
| Tidak ada kode fakultas/prodi | Ditambah kode_fakultas, kode_prodi |
| Tidak ada validasi data | Ditambah CHECK constraint |
| Tidak ada index | Ditambah 10 index untuk performa |
| Tidak ada CASCADE | Ditambah ON DELETE behavior |
| Nilai hanya angka & huruf | Ditambah nilai_tugas, nilai_uts, nilai_uas, bobot |
| Tidak ada prasyarat MK | Ditambah tabel prasyarat_mk |
| Tidak ada log aktivitas | Ditambah tabel log_aktivitas |
| Tidak ada view | Ditambah 4 VIEW untuk query yang sering dipakai |
| Tidak ada data contoh | Ditambah INSERT sample data |
| Tidak ada info ruangan lengkap | Ditambah gedung, lantai, fasilitas, tipe ruangan |

---

## 🎯 FITUR APLIKASI JAVA

| Menu | Fitur |
|------|-------|
| 👤 Mahasiswa | Lihat semua, Cari, Tambah, Hapus, Warna status |
| 📋 KRS | Lihat KRS per NIM, Setujui, Hapus, Hitung total SKS |
| 📊 Nilai | Lihat nilai per NIM, Input nilai, Hitung IPK, Ranking |
| 🕐 Jadwal | Semua jadwal, Filter per hari, Jadwal UTS/UAS |
| ✅ Presensi | Rekap kehadiran, Alert < 75%, Input presensi |

---

## 👨‍💻 TIM PENGEMBANG

- **Kelompok 4**
- Mata Kuliah: Basis Data / Sistem Informasi
- Tahun: 2025

---

## 📞 CATATAN PENTING

> Database menggunakan **schema kelompok4**, bukan schema public.
> Semua tabel diakses dengan prefix `kelompok4.namatabel`
> atau dengan mengatur `SET search_path TO kelompok4;`
