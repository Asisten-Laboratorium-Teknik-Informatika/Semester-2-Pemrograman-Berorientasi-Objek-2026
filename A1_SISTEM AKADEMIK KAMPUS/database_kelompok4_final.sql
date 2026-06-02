CREATE SCHEMA IF NOT EXISTS kelompok4;
CREATE TABLE kelompok4.admin (
    id_admin    SERIAL PRIMARY KEY,
    username    VARCHAR(50)  UNIQUE NOT NULL,
    password    VARCHAR(255) NOT NULL,
    nama_admin  VARCHAR(100),
    role        VARCHAR(20) DEFAULT 'staff',
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
CREATE TABLE kelompok4.fakultas (
    id_fakultas  SERIAL PRIMARY KEY,
    kode_fakultas VARCHAR(10) UNIQUE NOT NULL,
    nama_fakultas VARCHAR(100) NOT NULL,
    dekan         VARCHAR(100),
    created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
CREATE TABLE kelompok4.program_studi (
    id_prodi     SERIAL PRIMARY KEY,
    kode_prodi   VARCHAR(10) UNIQUE NOT NULL,
    nama_prodi   VARCHAR(100) NOT NULL,
    jenjang      VARCHAR(5) DEFAULT 'S1',
    akreditasi   CHAR(1),
    id_fakultas  INT REFERENCES kelompok4.fakultas(id_fakultas) ON DELETE RESTRICT,
    created_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
CREATE TABLE kelompok4.tahun_akademik (
    id_ta          SERIAL PRIMARY KEY,
    tahun_akademik VARCHAR(10) NOT NULL,
    semester       VARCHAR(10) NOT NULL,
    tanggal_mulai  DATE,
    tanggal_selesai DATE,
    batas_krs      DATE,
    is_aktif       BOOLEAN DEFAULT FALSE,
    UNIQUE(tahun_akademik, semester)
);
CREATE TABLE kelompok4.mahasiswa (
    nim             VARCHAR(15) PRIMARY KEY,
    nama_mahasiswa  VARCHAR(100) NOT NULL,
    jenis_kelamin   CHAR(1),
    tempat_lahir    VARCHAR(50),
    tanggal_lahir   DATE,
    alamat          TEXT,
    no_telp         VARCHAR(15),
    email           VARCHAR(100) UNIQUE,
    foto            VARCHAR(255),
    angkatan        INT NOT NULL,
    id_prodi        INT REFERENCES kelompok4.program_studi(id_prodi) ON DELETE RESTRICT,
    id_wali         VARCHAR(20),
    status          VARCHAR(20) DEFAULT 'Aktif'
        CHECK (status IN ('Aktif','Cuti','DO','Lulus')),
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
CREATE TABLE kelompok4.dosen (
    nidn        VARCHAR(20) PRIMARY KEY,
    nama_dosen  VARCHAR(100) NOT NULL,
    jenis_kelamin CHAR(1),
    email       VARCHAR(100) UNIQUE,
    no_telp     VARCHAR(15),
    foto        VARCHAR(255),
    jabatan     VARCHAR(50),
    id_prodi    INT REFERENCES kelompok4.program_studi(id_prodi) ON DELETE RESTRICT,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
ALTER TABLE kelompok4.mahasiswa
    ADD CONSTRAINT fk_wali FOREIGN KEY (id_wali)
    REFERENCES kelompok4.dosen(nidn) ON DELETE SET NULL;
CREATE TABLE kelompok4.mata_kuliah (
    kode_mk          VARCHAR(10) PRIMARY KEY,
    nama_mata_kuliah VARCHAR(100) NOT NULL,
    jumlah_sks       INT NOT NULL CHECK (jumlah_sks > 0),
    sks_teori        INT DEFAULT 0,
    sks_praktik      INT DEFAULT 0,
    semester_anjuran INT,
    tipe             VARCHAR(20) DEFAULT 'Wajib'
        CHECK (tipe IN ('Wajib','Pilihan')),
    deskripsi        TEXT,
    id_prodi         INT REFERENCES kelompok4.program_studi(id_prodi) ON DELETE RESTRICT,
    created_at       TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
CREATE TABLE kelompok4.prasyarat_mk (
    id          SERIAL PRIMARY KEY,
    kode_mk     VARCHAR(10) REFERENCES kelompok4.mata_kuliah(kode_mk) ON DELETE CASCADE,
    kode_prasyarat VARCHAR(10) REFERENCES kelompok4.mata_kuliah(kode_mk) ON DELETE CASCADE,
    nilai_min   CHAR(2) DEFAULT 'D',
    UNIQUE(kode_mk, kode_prasyarat)
);
CREATE TABLE kelompok4.ruangan (
    id_ruangan   SERIAL PRIMARY KEY,
    kode_ruangan VARCHAR(20) UNIQUE NOT NULL,
    nama_ruangan VARCHAR(50) NOT NULL,
    gedung       VARCHAR(50),
    lantai       INT,
    kapasitas    INT NOT NULL,
    fasilitas    TEXT,
    tipe         VARCHAR(20) DEFAULT 'Kelas'
);
CREATE TABLE kelompok4.jenis_pembayaran (
    id_jenis   SERIAL PRIMARY KEY,
    kode_jenis VARCHAR(20) UNIQUE NOT NULL,
    nama_jenis VARCHAR(50) NOT NULL,
    keterangan TEXT
);
CREATE TABLE kelompok4.beasiswa (
    id_beasiswa    SERIAL PRIMARY KEY,
    nama_beasiswa  VARCHAR(100) NOT NULL,
    penyelenggara  VARCHAR(100),
    jumlah_beasiswa NUMERIC(12,2),
    kuota          INT,
    persyaratan    TEXT,
    is_aktif       BOOLEAN DEFAULT TRUE
);
CREATE TABLE kelompok4.organisasi (
    id_organisasi   SERIAL PRIMARY KEY,
    nama_organisasi VARCHAR(100) NOT NULL,
    tipe            VARCHAR(30),
    deskripsi       TEXT
);
CREATE TABLE kelompok4.kelas (
    id_kelas   SERIAL PRIMARY KEY,
    kode_mk    VARCHAR(10) REFERENCES kelompok4.mata_kuliah(kode_mk) ON DELETE RESTRICT,
    nidn       VARCHAR(20) REFERENCES kelompok4.dosen(nidn) ON DELETE RESTRICT,
    id_ta      INT REFERENCES kelompok4.tahun_akademik(id_ta) ON DELETE RESTRICT,
    nama_kelas VARCHAR(10) DEFAULT 'A',
    kuota      INT DEFAULT 40,
    UNIQUE(kode_mk, id_ta, nama_kelas)
);
CREATE TABLE kelompok4.jadwal (
    id_jadwal  SERIAL PRIMARY KEY,
    id_kelas   INT REFERENCES kelompok4.kelas(id_kelas) ON DELETE CASCADE,
    id_ruangan INT REFERENCES kelompok4.ruangan(id_ruangan) ON DELETE RESTRICT,
    hari       VARCHAR(10) NOT NULL
        CHECK (hari IN ('Senin','Selasa','Rabu','Kamis','Jumat','Sabtu')),
    jam_mulai  TIME NOT NULL,
    jam_selesai TIME NOT NULL,
    CHECK (jam_selesai > jam_mulai)
);
CREATE TABLE kelompok4.krs (
    id_krs     SERIAL PRIMARY KEY,
    nim        VARCHAR(15) REFERENCES kelompok4.mahasiswa(nim) ON DELETE RESTRICT,
    id_kelas   INT REFERENCES kelompok4.kelas(id_kelas) ON DELETE RESTRICT,
    id_ta      INT REFERENCES kelompok4.tahun_akademik(id_ta) ON DELETE RESTRICT,
    semester   INT NOT NULL,
    status_krs VARCHAR(20) DEFAULT 'Diajukan'
        CHECK (status_krs IN ('Diajukan','Disetujui','Ditolak')),
    tgl_pengisian TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(nim, id_kelas, id_ta)
);
CREATE TABLE kelompok4.nilai (
    id_nilai      SERIAL PRIMARY KEY,
    id_krs        INT UNIQUE REFERENCES kelompok4.krs(id_krs) ON DELETE CASCADE,
    nilai_tugas   FLOAT CHECK (nilai_tugas BETWEEN 0 AND 100),
    nilai_uts     FLOAT CHECK (nilai_uts BETWEEN 0 AND 100),
    nilai_uas     FLOAT CHECK (nilai_uas BETWEEN 0 AND 100),
    nilai_angka   FLOAT CHECK (nilai_angka BETWEEN 0 AND 100),
    nilai_huruf   CHAR(2),
    bobot_nilai   FLOAT,
    is_final      BOOLEAN DEFAULT FALSE
);
CREATE TABLE kelompok4.presensi (
    id_presensi SERIAL PRIMARY KEY,
    id_krs      INT REFERENCES kelompok4.krs(id_krs) ON DELETE CASCADE,
    id_jadwal   INT REFERENCES kelompok4.jadwal(id_jadwal) ON DELETE CASCADE,
    tanggal     DATE NOT NULL,
    status      VARCHAR(15) NOT NULL
        CHECK (status IN ('Hadir','Izin','Sakit','Alpha')),
    keterangan  TEXT,
    UNIQUE(id_krs, id_jadwal, tanggal)
);
CREATE TABLE kelompok4.status_akademik (
    id          SERIAL PRIMARY KEY,
    nim         VARCHAR(15) REFERENCES kelompok4.mahasiswa(nim) ON DELETE CASCADE,
    id_ta       INT REFERENCES kelompok4.tahun_akademik(id_ta) ON DELETE RESTRICT,
    semester_ke INT NOT NULL,
    total_sks   INT DEFAULT 0,
    sks_lulus   INT DEFAULT 0,
    ip_semester FLOAT CHECK (ip_semester BETWEEN 0 AND 4),
    ipk         FLOAT CHECK (ipk BETWEEN 0 AND 4),
    status      VARCHAR(20) DEFAULT 'Aktif'
        CHECK (status IN ('Aktif','Cuti','DO','Lulus')),
    UNIQUE(nim, id_ta)
);
CREATE TABLE kelompok4.pembayaran (
    id_pembayaran      SERIAL PRIMARY KEY,
    nim                VARCHAR(15) REFERENCES kelompok4.mahasiswa(nim) ON DELETE RESTRICT,
    id_jenis           INT REFERENCES kelompok4.jenis_pembayaran(id_jenis) ON DELETE RESTRICT,
    id_ta              INT REFERENCES kelompok4.tahun_akademik(id_ta) ON DELETE RESTRICT,
    jumlah_pembayaran  NUMERIC(12,2) NOT NULL,
    tanggal_pembayaran DATE,
    batas_pembayaran   DATE,
    no_transaksi       VARCHAR(50) UNIQUE,
    metode_pembayaran  VARCHAR(30),
    status_pembayaran  VARCHAR(20) DEFAULT 'Belum Lunas'
        CHECK (status_pembayaran IN ('Lunas','Belum Lunas','Cicilan')),
    bukti_bayar        VARCHAR(255),
    created_at         TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
CREATE TABLE kelompok4.jadwal_ujian (
    id_ujian    SERIAL PRIMARY KEY,
    id_kelas    INT REFERENCES kelompok4.kelas(id_kelas) ON DELETE CASCADE,
    id_ruangan  INT REFERENCES kelompok4.ruangan(id_ruangan) ON DELETE RESTRICT,
    jenis_ujian VARCHAR(20) NOT NULL
        CHECK (jenis_ujian IN ('UTS','UAS','Remedial','Praktikum')),
    tanggal     DATE NOT NULL,
    jam_mulai   TIME NOT NULL,
    jam_selesai TIME NOT NULL,
    pengawas    VARCHAR(100),
    CHECK (jam_selesai > jam_mulai)
);
CREATE TABLE kelompok4.penerima_beasiswa (
    id          SERIAL PRIMARY KEY,
    nim         VARCHAR(15) REFERENCES kelompok4.mahasiswa(nim) ON DELETE CASCADE,
    id_beasiswa INT REFERENCES kelompok4.beasiswa(id_beasiswa) ON DELETE CASCADE,
    id_ta       INT REFERENCES kelompok4.tahun_akademik(id_ta),
    tahun       INT NOT NULL,
    ipk_saat_daftar FLOAT,
    status      VARCHAR(20) DEFAULT 'Aktif'
);
CREATE TABLE kelompok4.anggota_organisasi (
    id             SERIAL PRIMARY KEY,
    nim            VARCHAR(15) REFERENCES kelompok4.mahasiswa(nim) ON DELETE CASCADE,
    id_organisasi  INT REFERENCES kelompok4.organisasi(id_organisasi) ON DELETE CASCADE,
    jabatan        VARCHAR(50),
    tahun_masuk    INT NOT NULL,
    tahun_keluar   INT,
    is_aktif       BOOLEAN DEFAULT TRUE
);
CREATE TABLE kelompok4.pengumuman (
    id_pengumuman SERIAL PRIMARY KEY,
    judul         VARCHAR(200) NOT NULL,
    isi           TEXT,
    kategori      VARCHAR(50),
    id_admin      INT REFERENCES kelompok4.admin(id_admin) ON DELETE SET NULL,
    tanggal_post  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    tanggal_expire DATE,
    is_aktif      BOOLEAN DEFAULT TRUE
);
CREATE TABLE kelompok4.log_aktivitas (
    id_log      SERIAL PRIMARY KEY,
    nim         VARCHAR(15),
    id_admin    INT,
    aksi        VARCHAR(100) NOT NULL,
    detail      TEXT,
    ip_address  VARCHAR(45),
    waktu       TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_mahasiswa_prodi    ON kelompok4.mahasiswa(id_prodi);
CREATE INDEX idx_mahasiswa_angkatan ON kelompok4.mahasiswa(angkatan);
CREATE INDEX idx_mahasiswa_status   ON kelompok4.mahasiswa(status);
CREATE INDEX idx_krs_nim            ON kelompok4.krs(nim);
CREATE INDEX idx_krs_id_ta          ON kelompok4.krs(id_ta);
CREATE INDEX idx_presensi_id_krs    ON kelompok4.presensi(id_krs);
CREATE INDEX idx_presensi_tanggal   ON kelompok4.presensi(tanggal);
CREATE INDEX idx_nilai_id_krs       ON kelompok4.nilai(id_krs);
CREATE INDEX idx_pembayaran_nim     ON kelompok4.pembayaran(nim);
CREATE INDEX idx_jadwal_hari        ON kelompok4.jadwal(hari);
CREATE OR REPLACE VIEW kelompok4.v_mahasiswa_lengkap AS
SELECT m.nim, m.nama_mahasiswa, m.angkatan, m.status,
       m.email, m.no_telp, m.jenis_kelamin,
       p.nama_prodi, p.jenjang,
       f.nama_fakultas,
       d.nama_dosen AS nama_dosen_wali
FROM kelompok4.mahasiswa m
JOIN kelompok4.program_studi p ON m.id_prodi = p.id_prodi
JOIN kelompok4.fakultas f ON p.id_fakultas = f.id_fakultas
LEFT JOIN kelompok4.dosen d ON m.id_wali = d.nidn;
CREATE OR REPLACE VIEW kelompok4.v_krs_aktif AS
SELECT kr.id_krs, m.nim, m.nama_mahasiswa,
       mk.kode_mk, mk.nama_mata_kuliah, mk.jumlah_sks,
       d.nama_dosen, k.nama_kelas,
       ta.tahun_akademik, ta.semester,
       kr.status_krs
FROM kelompok4.krs kr
JOIN kelompok4.mahasiswa m ON kr.nim = m.nim
JOIN kelompok4.kelas k ON kr.id_kelas = k.id_kelas
JOIN kelompok4.mata_kuliah mk ON k.kode_mk = mk.kode_mk
JOIN kelompok4.dosen d ON k.nidn = d.nidn
JOIN kelompok4.tahun_akademik ta ON kr.id_ta = ta.id_ta
WHERE ta.is_aktif = TRUE;
CREATE OR REPLACE VIEW kelompok4.v_nilai_lengkap AS
SELECT m.nim, m.nama_mahasiswa,
       mk.nama_mata_kuliah, mk.jumlah_sks,
       n.nilai_tugas, n.nilai_uts, n.nilai_uas,
       n.nilai_angka, n.nilai_huruf, n.bobot_nilai,
       ta.tahun_akademik, ta.semester
FROM kelompok4.nilai n
JOIN kelompok4.krs kr ON n.id_krs = kr.id_krs
JOIN kelompok4.mahasiswa m ON kr.nim = m.nim
JOIN kelompok4.kelas k ON kr.id_kelas = k.id_kelas
JOIN kelompok4.mata_kuliah mk ON k.kode_mk = mk.kode_mk
JOIN kelompok4.tahun_akademik ta ON kr.id_ta = ta.id_ta;
CREATE OR REPLACE VIEW kelompok4.v_rekap_presensi AS
SELECT m.nim, m.nama_mahasiswa, mk.nama_mata_kuliah,
       ta.tahun_akademik, ta.semester,
       COUNT(*) AS total_pertemuan,
       SUM(CASE WHEN p.status='Hadir' THEN 1 ELSE 0 END) AS hadir,
       SUM(CASE WHEN p.status='Izin'  THEN 1 ELSE 0 END) AS izin,
       SUM(CASE WHEN p.status='Sakit' THEN 1 ELSE 0 END) AS sakit,
       SUM(CASE WHEN p.status='Alpha' THEN 1 ELSE 0 END) AS alpha,
       ROUND(
           SUM(CASE WHEN p.status='Hadir' THEN 1 ELSE 0 END)*100.0/COUNT(*), 2
       ) AS persen_kehadiran
FROM kelompok4.presensi p
JOIN kelompok4.krs kr ON p.id_krs = kr.id_krs
JOIN kelompok4.mahasiswa m ON kr.nim = m.nim
JOIN kelompok4.kelas k ON kr.id_kelas = k.id_kelas
JOIN kelompok4.mata_kuliah mk ON k.kode_mk = mk.kode_mk
JOIN kelompok4.tahun_akademik ta ON kr.id_ta = ta.id_ta
GROUP BY m.nim, m.nama_mahasiswa, mk.nama_mata_kuliah,
         ta.tahun_akademik, ta.semester;
INSERT INTO kelompok4.admin (username, password, nama_admin, role)
VALUES ('admin', 'admin123', 'Administrator', 'superadmin');
INSERT INTO kelompok4.fakultas (kode_fakultas, nama_fakultas, dekan) VALUES
('FT',  'Fakultas Teknik',                'Ade Maulana,S.kom, M.T.I'),
('FV', 'Fakultas Vokasi ',                'Reza Taqyuddin , M.Kom'),
('FIK', 'Fakultas Ilmu Komputer',         'Dra. Normalina Napitupulu ,M.sc');
INSERT INTO kelompok4.program_studi (kode_prodi, nama_prodi, jenjang, id_fakultas) VALUES
('TI',  'Teknik Informatika',   'S1', 3),
('SI',  'Sistem Informasi',     'S1', 3),
('TK',  'Teknik Komputer',      'S1', 1),
('MNJ', 'Manajemen',            'S1', 2);
INSERT INTO kelompok4.tahun_akademik (tahun_akademik, semester, tanggal_mulai, tanggal_selesai, batas_krs, is_aktif) VALUES
('2023/2024', 'Ganjil',  '2023-09-01', '2024-01-31', '2023-09-15', FALSE),
('2023/2024', 'Genap',   '2024-02-01', '2024-06-30', '2024-02-15', FALSE),
('2024/2025', 'Ganjil',  '2024-09-01', '2025-01-31', '2024-09-15', TRUE);
INSERT INTO kelompok4.dosen (nidn, nama_dosen, email, jabatan, id_prodi) VALUES
('0012345678', 'Dr. Rina Sari, M.Kom.',    'rina@univ.ac.id',   'Lektor Kepala', 1),
('0023456789', 'Ahmad Fauzi, S.T., M.T.',  'ahmad@univ.ac.id',  'Lektor',        1),
('0034567890', 'Dewi Lestari, M.Cs.',      'dewi@univ.ac.id',   'Asisten Ahli',  2);
INSERT INTO kelompok4.mahasiswa (nim, nama_mahasiswa, jenis_kelamin, tanggal_lahir, email, angkatan, id_prodi, id_wali, status) VALUES
('2021001001', 'Budi Santoso',     'L', '2002-05-10', 'budi@student.ac.id',   2021, 1, '0012345678', 'Aktif'),
('2021001002', 'Sari Dewi',        'P', '2002-07-22', 'sari@student.ac.id',   2021, 1, '0012345678', 'Aktif'),
('2022001001', 'Eko Prasetyo',     'L', '2003-03-15', 'eko@student.ac.id',    2022, 1, '0023456789', 'Aktif'),
('2022001002', 'Putri Rahayu',     'P', '2003-11-08', 'putri@student.ac.id',  2022, 2, '0034567890', 'Aktif');
INSERT INTO kelompok4.mata_kuliah (kode_mk, nama_mata_kuliah, jumlah_sks, sks_teori, sks_praktik, semester_anjuran, tipe, id_prodi) VALUES
('TI101', 'Algoritma dan Pemrograman',  3, 2, 1, 1, 'Wajib', 1),
('TI102', 'Matematika Diskrit',         3, 3, 0, 1, 'Wajib', 1),
('TI201', 'Basis Data',                 3, 2, 1, 3, 'Wajib', 1),
('TI202', 'Pemrograman Berorientasi Objek', 3, 2, 1, 3, 'Wajib', 1),
('TI301', 'Rekayasa Perangkat Lunak',   3, 3, 0, 5, 'Wajib', 1);
INSERT INTO kelompok4.ruangan (kode_ruangan, nama_ruangan, gedung, lantai, kapasitas, tipe) VALUES
('GK-101', 'Ruang Kuliah 101',  'Gedung A', 1, 40, 'Kelas'),
('GK-201', 'Ruang Kuliah 201',  'Gedung A', 2, 40, 'Kelas'),
('LAB-01', 'Lab Komputer 1',    'Gedung B', 1, 30, 'Lab'),
('AULA',   'Aula Utama',        'Gedung C', 1, 200,'Aula');
INSERT INTO kelompok4.jenis_pembayaran (kode_jenis, nama_jenis) VALUES
('UKT',  'Uang Kuliah Tunggal'),
('PRKT', 'Biaya Praktikum'),
('WISU', 'Biaya Wisuda');
INSERT INTO kelompok4.beasiswa (nama_beasiswa, penyelenggara, jumlah_beasiswa, kuota) VALUES
('KIP Kuliah',        'Kemendikbud', 12000000, 50),
('Beasiswa Prestasi', 'Universitas', 5000000,  20);
INSERT INTO kelompok4.organisasi (nama_organisasi, tipe) VALUES
('BEM Universitas',         'BEM'),
('Himpunan Mahasiswa TI',   'Himpunan'),
('UKM Robotika',            'UKM');
INSERT INTO kelompok4.kelas (kode_mk, nidn, id_ta, nama_kelas, kuota) VALUES
('TI101', '0023456789', 3, 'A', 40),
('TI102', '0012345678', 3, 'A', 40),
('TI201', '0012345678', 3, 'A', 40),
('TI202', '0023456789', 3, 'A', 40);
INSERT INTO kelompok4.jadwal (id_kelas, id_ruangan, hari, jam_mulai, jam_selesai) VALUES
(1, 1, 'Senin',  '08:00', '10:30'),
(2, 2, 'Selasa', '08:00', '10:30'),
(3, 3, 'Rabu',   '08:00', '10:30'),
(4, 1, 'Kamis',  '08:00', '10:30');
INSERT INTO kelompok4.krs (nim, id_kelas, id_ta, semester, status_krs) VALUES
('2022001001', 1, 3, 3, 'Disetujui'),
('2022001001', 2, 3, 3, 'Disetujui'),
('2022001001', 3, 3, 3, 'Disetujui'),
('2022001002', 1, 3, 3, 'Disetujui'),
('2022001002', 4, 3, 3, 'Disetujui');
INSERT INTO kelompok4.nilai (id_krs, nilai_tugas, nilai_uts, nilai_uas, nilai_angka, nilai_huruf, bobot_nilai, is_final) VALUES
(1, 85, 78, 82, 81.9, 'A',  4.0, TRUE),
(2, 70, 65, 72, 69.4, 'B',  3.0, TRUE),
(4, 90, 88, 92, 90.4, 'A',  4.0, TRUE),
(5, 75, 72, 78, 75.3, 'B+', 3.5, TRUE);
INSERT INTO kelompok4.presensi (id_krs, id_jadwal, tanggal, status) VALUES
(1, 1, '2024-09-16', 'Hadir'),
(1, 1, '2024-09-23', 'Hadir'),
(1, 1, '2024-09-30', 'Izin'),
(1, 1, '2024-10-07', 'Hadir'),
(2, 2, '2024-09-17', 'Hadir'),
(2, 2, '2024-09-24', 'Alpha'),
(2, 2, '2024-10-01', 'Hadir');
INSERT INTO kelompok4.pembayaran (nim, id_jenis, id_ta, jumlah_pembayaran, tanggal_pembayaran, status_pembayaran, metode_pembayaran) VALUES
('2022001001', 1, 3, 5000000, '2024-09-01', 'Lunas',       'Transfer'),
('2022001002', 1, 3, 5000000, '2024-09-05', 'Lunas',       'Virtual Account'),
('2021001001', 1, 3, 5000000,  NULL,         'Belum Lunas', NULL);
INSERT INTO kelompok4.jadwal_ujian (id_kelas, id_ruangan, jenis_ujian, tanggal, jam_mulai, jam_selesai, pengawas) VALUES
(1, 1, 'UTS', '2024-11-04', '08:00', '10:00', 'Ahmad Fauzi, S.T., M.T.'),
(2, 2, 'UTS', '2024-11-05', '08:00', '10:00', 'Dr. Rina Sari, M.Kom.'),
(3, 3, 'UTS', '2024-11-06', '08:00', '10:00', 'Dr. Rina Sari, M.Kom.');
INSERT INTO kelompok4.penerima_beasiswa (nim, id_beasiswa, id_ta, tahun, ipk_saat_daftar, status) VALUES
('2022001001', 1, 3, 2024, 3.75, 'Aktif'),
('2021001002', 2, 3, 2024, 3.50, 'Aktif');
SELECT * FROM kelompok4.v_mahasiswa_lengkap;
SELECT * FROM kelompok4.v_krs_aktif;
SELECT * FROM kelompok4.v_nilai_lengkap;
SELECT * FROM kelompok4.v_rekap_presensi;
SELECT * FROM kelompok4.v_rekap_presensi
WHERE persen_kehadiran < 75;
SELECT mk.nama_mata_kuliah, d.nama_dosen, r.nama_ruangan,
       j.hari, j.jam_mulai, j.jam_selesai
FROM kelompok4.jadwal j
JOIN kelompok4.kelas k ON j.id_kelas = k.id_kelas
JOIN kelompok4.mata_kuliah mk ON k.kode_mk = mk.kode_mk
JOIN kelompok4.dosen d ON k.nidn = d.nidn
JOIN kelompok4.ruangan r ON j.id_ruangan = r.id_ruangan
JOIN kelompok4.tahun_akademik ta ON k.id_ta = ta.id_ta
WHERE ta.is_aktif = TRUE
ORDER BY j.hari, j.jam_mulai;
SELECT m.nama_mahasiswa, SUM(mk.jumlah_sks) AS total_sks
FROM kelompok4.krs kr
JOIN kelompok4.mahasiswa m ON kr.nim = m.nim
JOIN kelompok4.kelas k ON kr.id_kelas = k.id_kelas
JOIN kelompok4.mata_kuliah mk ON k.kode_mk = mk.kode_mk
JOIN kelompok4.tahun_akademik ta ON kr.id_ta = ta.id_ta
WHERE ta.is_aktif = TRUE
GROUP BY m.nama_mahasiswa;
SELECT m.nama_mahasiswa, m.nim, m.no_telp, m.email
FROM kelompok4.mahasiswa m
LEFT JOIN kelompok4.pembayaran p
    ON m.nim = p.nim
    AND p.id_ta = (SELECT id_ta FROM kelompok4.tahun_akademik WHERE is_aktif = TRUE LIMIT 1)
    AND p.status_pembayaran = 'Lunas'
WHERE p.id_pembayaran IS NULL AND m.status = 'Aktif';
SELECT mk.nama_mata_kuliah, ju.jenis_ujian, ju.tanggal,
       ju.jam_mulai, ju.jam_selesai, r.nama_ruangan, ju.pengawas
FROM kelompok4.jadwal_ujian ju
JOIN kelompok4.kelas k ON ju.id_kelas = k.id_kelas
JOIN kelompok4.mata_kuliah mk ON k.kode_mk = mk.kode_mk
JOIN kelompok4.ruangan r ON ju.id_ruangan = r.id_ruangan
WHERE ju.jenis_ujian = 'UTS'
ORDER BY ju.tanggal, ju.jam_mulai;
SELECT m.nim, m.nama_mahasiswa,
       ROUND(
           SUM(n.bobot_nilai * mk.jumlah_sks)::NUMERIC / SUM(mk.jumlah_sks), 2
       ) AS ipk
FROM kelompok4.nilai n
JOIN kelompok4.krs kr ON n.id_krs = kr.id_krs
JOIN kelompok4.mahasiswa m ON kr.nim = m.nim
JOIN kelompok4.kelas k ON kr.id_kelas = k.id_kelas
JOIN kelompok4.mata_kuliah mk ON k.kode_mk = mk.kode_mk
WHERE n.is_final = TRUE
GROUP BY m.nim, m.nama_mahasiswa
ORDER BY ipk DESC;
ALTER TABLE kelompok4.dosen
ADD COLUMN IF NOT EXISTS password_dosen VARCHAR(255);
UPDATE kelompok4.dosen
SET password_dosen = nidn
WHERE password_dosen IS NULL;
ALTER TABLE kelompok4.krs
DROP CONSTRAINT IF EXISTS krs_status_krs_check;
ALTER TABLE kelompok4.krs
ADD CONSTRAINT krs_status_krs_check
CHECK (status_krs IN ('Diajukan','Disetujui','Ditolak'));
UPDATE kelompok4.dosen SET password_dosen = '0012345678' WHERE nidn = '0012345678';
UPDATE kelompok4.dosen SET password_dosen = '0023456789' WHERE nidn = '0023456789';
UPDATE kelompok4.dosen SET password_dosen = '0034567890' WHERE nidn = '0034567890';
INSERT INTO kelompok4.krs (nim, id_kelas, id_ta, semester, status_krs)
VALUES ('2021001001', 1, 3, 3, 'Diajukan')
ON CONFLICT (nim, id_kelas, id_ta) DO NOTHING;
INSERT INTO kelompok4.krs (nim, id_kelas, id_ta, semester, status_krs)
VALUES ('2021001002', 2, 3, 3, 'Diajukan')
ON CONFLICT (nim, id_kelas, id_ta) DO NOTHING;
CREATE OR REPLACE VIEW kelompok4.v_kelas_aktif_tersedia AS
SELECT k.id_kelas,
       mk.kode_mk,
       mk.nama_mata_kuliah,
       mk.jumlah_sks,
       d.nidn,
       d.nama_dosen,
       k.nama_kelas,
       k.kuota,
       COUNT(kr.id_krs) AS jumlah_peserta,
       ta.tahun_akademik,
       ta.semester
FROM kelompok4.kelas k
JOIN kelompok4.mata_kuliah mk ON k.kode_mk = mk.kode_mk
JOIN kelompok4.dosen d ON k.nidn = d.nidn
JOIN kelompok4.tahun_akademik ta ON k.id_ta = ta.id_ta
LEFT JOIN kelompok4.krs kr ON kr.id_kelas = k.id_kelas
WHERE ta.is_aktif = TRUE
GROUP BY k.id_kelas, mk.kode_mk, mk.nama_mata_kuliah, mk.jumlah_sks,
         d.nidn, d.nama_dosen, k.nama_kelas, k.kuota,
         ta.tahun_akademik, ta.semester;
CREATE OR REPLACE VIEW kelompok4.v_krs_per_dosen AS
SELECT d.nidn, d.nama_dosen,
       mh.nim, mh.nama_mahasiswa,
       mk.nama_mata_kuliah, mk.jumlah_sks,
       k.nama_kelas, ta.tahun_akademik, ta.semester,
       kr.status_krs, kr.tgl_pengisian
FROM kelompok4.krs kr
JOIN kelompok4.kelas k ON kr.id_kelas = k.id_kelas
JOIN kelompok4.dosen d ON k.nidn = d.nidn
JOIN kelompok4.mahasiswa mh ON kr.nim = mh.nim
JOIN kelompok4.mata_kuliah mk ON k.kode_mk = mk.kode_mk
JOIN kelompok4.tahun_akademik ta ON k.id_ta = ta.id_ta
ORDER BY d.nama_dosen, kr.status_krs, mh.nama_mahasiswa;
SELECT nidn, nama_dosen, password_dosen FROM kelompok4.dosen;
SELECT kr.id_krs, mh.nama_mahasiswa, mk.nama_mata_kuliah, kr.status_krs
FROM kelompok4.krs kr
JOIN kelompok4.mahasiswa mh ON kr.nim = mh.nim
JOIN kelompok4.kelas k ON kr.id_kelas = k.id_kelas
JOIN kelompok4.mata_kuliah mk ON k.kode_mk = mk.kode_mk
WHERE kr.status_krs = 'Diajukan';
CREATE ROLE role_admin;
CREATE ROLE role_dosen;
CREATE ROLE role_mahasiswa;
CREATE USER user_admin     WITH PASSWORD 'Admin@2024'     IN ROLE role_admin;
CREATE USER user_dosen     WITH PASSWORD 'Dosen@2024'     IN ROLE role_dosen;
CREATE USER user_mahasiswa WITH PASSWORD 'Mhs@2024'       IN ROLE role_mahasiswa;
GRANT USAGE ON SCHEMA kelompok4 TO role_admin;
GRANT SELECT, INSERT, UPDATE, DELETE ON
    kelompok4.admin,
    kelompok4.fakultas,
    kelompok4.program_studi,
    kelompok4.tahun_akademik,
    kelompok4.mahasiswa,
    kelompok4.dosen,
    kelompok4.mata_kuliah,
    kelompok4.prasyarat_mk,
    kelompok4.ruangan,
    kelompok4.jenis_pembayaran,
    kelompok4.beasiswa,
    kelompok4.organisasi,
    kelompok4.kelas,
    kelompok4.jadwal,
    kelompok4.krs,
    kelompok4.nilai,
    kelompok4.presensi,
    kelompok4.status_akademik,
    kelompok4.pembayaran,
    kelompok4.jadwal_ujian,
    kelompok4.penerima_beasiswa,
    kelompok4.anggota_organisasi,
    kelompok4.pengumuman,
    kelompok4.log_aktivitas
TO role_admin;
GRANT SELECT ON
    kelompok4.v_mahasiswa_lengkap,
    kelompok4.v_krs_aktif,
    kelompok4.v_nilai_lengkap,
    kelompok4.v_rekap_presensi,
    kelompok4.v_kelas_aktif_tersedia,
    kelompok4.v_krs_per_dosen
TO role_admin;
GRANT USAGE ON SCHEMA kelompok4 TO role_dosen;
GRANT SELECT ON
    kelompok4.mahasiswa,
    kelompok4.mata_kuliah,
    kelompok4.kelas,
    kelompok4.jadwal,
    kelompok4.jadwal_ujian,
    kelompok4.tahun_akademik,
    kelompok4.program_studi,
    kelompok4.fakultas,
    kelompok4.ruangan,
    kelompok4.v_mahasiswa_lengkap,
    kelompok4.v_krs_aktif,
    kelompok4.v_nilai_lengkap,
    kelompok4.v_rekap_presensi,
    kelompok4.v_kelas_aktif_tersedia,
    kelompok4.v_krs_per_dosen
TO role_dosen;
GRANT SELECT, INSERT, UPDATE ON
    kelompok4.nilai,
    kelompok4.presensi,
    kelompok4.krs
TO role_dosen;
GRANT INSERT ON kelompok4.log_aktivitas TO role_dosen;
GRANT USAGE ON SCHEMA kelompok4 TO role_mahasiswa;
GRANT SELECT ON
    kelompok4.mata_kuliah,
    kelompok4.kelas,
    kelompok4.jadwal,
    kelompok4.jadwal_ujian,
    kelompok4.tahun_akademik,
    kelompok4.ruangan,
    kelompok4.beasiswa,
    kelompok4.pengumuman,
    kelompok4.v_kelas_aktif_tersedia
TO role_mahasiswa;
GRANT SELECT, INSERT ON kelompok4.krs TO role_mahasiswa;
GRANT SELECT ON kelompok4.nilai    TO role_mahasiswa;
GRANT SELECT ON kelompok4.presensi TO role_mahasiswa;
GRANT SELECT, UPDATE ON kelompok4.mahasiswa TO role_mahasiswa;
REVOKE DELETE ON kelompok4.krs  FROM role_mahasiswa;
REVOKE UPDATE ON kelompok4.nilai FROM role_mahasiswa;
BEGIN;
SAVEPOINT sp_sebelum_krs;
INSERT INTO kelompok4.krs (nim, id_kelas, id_ta, semester, status_krs)
VALUES ('2021001001', 2, 3, 3, 'Diajukan')
ON CONFLICT (nim, id_kelas, id_ta) DO NOTHING;
INSERT INTO kelompok4.log_aktivitas (nim, aksi, detail, waktu)
VALUES (
    '2021001001',
    'PENGISIAN_KRS',
    'Mahasiswa mengisi KRS untuk kelas id=2, TA id=3',
    CURRENT_TIMESTAMP
);
COMMIT;
BEGIN;
SAVEPOINT sp_sebelum_nilai;
INSERT INTO kelompok4.nilai (id_krs, nilai_tugas, nilai_uts, nilai_uas, nilai_angka, nilai_huruf, bobot_nilai, is_final)
VALUES (3, 80, 75, 85, 80.5, 'A', 4.0, TRUE)
ON CONFLICT (id_krs) DO UPDATE
    SET nilai_tugas  = EXCLUDED.nilai_tugas,
        nilai_uts    = EXCLUDED.nilai_uts,
        nilai_uas    = EXCLUDED.nilai_uas,
        nilai_angka  = EXCLUDED.nilai_angka,
        nilai_huruf  = EXCLUDED.nilai_huruf,
        bobot_nilai  = EXCLUDED.bobot_nilai,
        is_final     = EXCLUDED.is_final;
INSERT INTO kelompok4.log_aktivitas (aksi, detail, waktu)
VALUES (
    'INPUT_NILAI',
    'Dosen input nilai untuk id_krs=3',
    CURRENT_TIMESTAMP
);
COMMIT;
BEGIN;
SAVEPOINT sp_update_status;
UPDATE kelompok4.mahasiswa
SET status = 'Lulus'
WHERE nim = '2021001001';
UPDATE kelompok4.status_akademik
SET status = 'Lulus'
WHERE nim = '2021001001'
  AND id_ta = (SELECT id_ta FROM kelompok4.tahun_akademik WHERE is_aktif = TRUE LIMIT 1);
INSERT INTO kelompok4.log_aktivitas (nim, aksi, detail, waktu)
VALUES (
    '2021001001',
    'UPDATE_STATUS_LULUS',
    'Status mahasiswa diubah menjadi Lulus',
    CURRENT_TIMESTAMP
);
COMMIT;
BEGIN;
SAVEPOINT sp_pembayaran;
INSERT INTO kelompok4.pembayaran (nim, id_jenis, id_ta, jumlah_pembayaran, tanggal_pembayaran, status_pembayaran, metode_pembayaran)
VALUES ('2021001001', 1, 3, 5000000, CURRENT_DATE, 'Lunas', 'Transfer')
ON CONFLICT DO NOTHING;
UPDATE kelompok4.pembayaran
SET status_pembayaran = 'Lunas',
    tanggal_pembayaran = CURRENT_DATE
WHERE nim    = '2021001001'
  AND id_ta  = 3
  AND id_jenis = 1;
INSERT INTO kelompok4.log_aktivitas (nim, aksi, detail, waktu)
VALUES (
    '2021001001',
    'PEMBAYARAN_UKT',
    'Pembayaran UKT berhasil, jumlah Rp5.000.000 via Transfer',
    CURRENT_TIMESTAMP
);
COMMIT;
BEGIN;
SAVEPOINT sp_rollback_demo;
UPDATE kelompok4.mahasiswa
SET status = 'Cuti'
WHERE nim = '2022001001';
ROLLBACK TO SAVEPOINT sp_rollback_demo;
ROLLBACK;

-- ═══════════════════════════════════════════════════════════════════
-- MIGRASI SQL — Fitur Password (Ganti & Reset)
-- Jalankan di database ProjectAkhir, schema kelompok4
-- ═══════════════════════════════════════════════════════════════════

-- ─── 1. Pastikan kolom password_dosen ada (sudah ada di database asli) ───
ALTER TABLE kelompok4.dosen
    ADD COLUMN IF NOT EXISTS password_dosen VARCHAR(255);

UPDATE kelompok4.dosen
    SET password_dosen = nidn
WHERE password_dosen IS NULL;

-- ─── 2. Kolom password terpisah untuk mahasiswa (WAJIB — dipakai kode Java) ──
ALTER TABLE kelompok4.mahasiswa
    ADD COLUMN IF NOT EXISTS password_mahasiswa VARCHAR(255);

-- Password default = tanggal_lahir (format YYYY-MM-DD) untuk data lama
UPDATE kelompok4.mahasiswa
    SET password_mahasiswa = CAST(tanggal_lahir AS VARCHAR)
WHERE password_mahasiswa IS NULL;

-- ─── 3. Tabel log_reset_password (OPSIONAL, untuk audit trail) ───────────
-- Mencatat setiap percobaan/permintaan reset password.

CREATE TABLE IF NOT EXISTS kelompok4.log_reset_password (
    id_log      SERIAL PRIMARY KEY,
    role        VARCHAR(20) NOT NULL,       -- 'MAHASISWA' | 'DOSEN' | 'ADMIN'
    user_id     VARCHAR(50) NOT NULL,       -- NIM / NIDN / username
    email_used  VARCHAR(100),              -- email yang digunakan untuk verifikasi
    status      VARCHAR(20) NOT NULL,       -- 'BERHASIL' | 'GAGAL' | 'DITOLAK'
    keterangan  TEXT,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Contoh insert log (dipanggil dari Java setelah reset berhasil/gagal):
-- INSERT INTO kelompok4.log_reset_password
--     (role, user_id, email_used, status, keterangan)
-- VALUES ('MAHASISWA', '2022001001', 'user@email.com', 'BERHASIL', 'Reset via verifikasi email');

-- ─── 4. Verifikasi isi data (cek setelah migrasi) ────────────────────────
SELECT nidn, nama_dosen, password_dosen FROM kelompok4.dosen LIMIT 5;
SELECT nim, nama_mahasiswa, email, tanggal_lahir FROM kelompok4.mahasiswa LIMIT 5;
SELECT username, nama_admin FROM kelompok4.admin;

UPDATE kelompok4.admin
SET password = encode(sha256('admin123'::bytea), 'hex')
WHERE username = 'admin';

-- Admin
UPDATE kelompok4.admin
SET password = encode(sha256('admin123'::bytea), 'hex')
WHERE username = 'admin';

-- Dosen (password default = NIDN)
UPDATE kelompok4.dosen
SET password_dosen = encode(sha256(nidn::bytea), 'hex');

-- Mahasiswa (password default = tanggal_lahir, format YYYY-MM-DD)
UPDATE kelompok4.mahasiswa
SET password_mahasiswa = encode(sha256(CAST(tanggal_lahir AS VARCHAR)::bytea), 'hex');

select nama_mahasiswa , nim, password_mahasiswa from kelompok4.mahasiswa;

UPDATE kelompok4.mahasiswa
SET password_mahasiswa =
TO_CHAR(tanggal_lahir, 'YYYYMMDD');

SELECT nim,
       password_mahasiswa
FROM kelompok4.mahasiswa
WHERE nim='2025001001';

SELECT username, password
FROM kelompok4.admin;

UPDATE kelompok4.dosen
SET password_dosen = nidn;