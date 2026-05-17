DROP VIEW  IF EXISTS v_riwayat_pemesanan   CASCADE;
DROP VIEW  IF EXISTS v_laporan_harian      CASCADE;
DROP VIEW  IF EXISTS v_stok_menipis        CASCADE;
DROP VIEW  IF EXISTS v_menu_terlaris       CASCADE;
DROP VIEW  IF EXISTS v_member_terbaik      CASCADE;
DROP VIEW  IF EXISTS v_stok_bahan_menipis  CASCADE;
DROP VIEW  IF EXISTS v_karyawan_aktif      CASCADE;

DROP TABLE IF EXISTS reservasi             CASCADE;
DROP TABLE IF EXISTS log_aktivitas         CASCADE;
DROP TABLE IF EXISTS pembayaran            CASCADE;
DROP TABLE IF EXISTS detail_pesanan        CASCADE;
DROP TABLE IF EXISTS pesanan               CASCADE;
DROP TABLE IF EXISTS metode_bayar          CASCADE;
DROP TABLE IF EXISTS resep                 CASCADE;
DROP TABLE IF EXISTS menu                  CASCADE;
DROP TABLE IF EXISTS bahan_baku            CASCADE;
DROP TABLE IF EXISTS satuan                CASCADE;
DROP TABLE IF EXISTS supplier              CASCADE;
DROP TABLE IF EXISTS kategori              CASCADE;
DROP TABLE IF EXISTS meja                  CASCADE;
DROP TABLE IF EXISTS shift                 CASCADE;
DROP TABLE IF EXISTS pelanggan             CASCADE;
DROP TABLE IF EXISTS karyawan              CASCADE;
DROP TABLE IF EXISTS jabatan               CASCADE;



-- ----------------------------------------------------------------
--  TABEL 1 : jabatan
-- ----------------------------------------------------------------

CREATE TABLE jabatan (
    id_jabatan   SERIAL        PRIMARY KEY,
    nama_jabatan VARCHAR(50)   NOT NULL UNIQUE,
    deskripsi    TEXT,
    gaji_pokok   NUMERIC(12,2) NOT NULL DEFAULT 0,
    created_at   TIMESTAMP     NOT NULL DEFAULT NOW()
);

INSERT INTO jabatan (nama_jabatan, deskripsi, gaji_pokok) VALUES
    ('Manajer',  'Mengelola operasional cafe secara keseluruhan', 8000000),
    ('Kasir',    'Melayani pembayaran dan transaksi pelanggan',    4000000),
    ('Barista',  'Membuat minuman dan kopi',                       3500000),
    ('Koki',     'Memasak dan menyiapkan makanan',                 3500000),
    ('Waiter',   'Melayani pelanggan di meja',                     3000000),
    ('Cleaning', 'Menjaga kebersihan cafe',                        2800000);

-- Lihat semua data jabatan
SELECT * FROM jabatan ORDER BY id_jabatan;

-- Jabatan dengan gaji tertinggi ke terendah
SELECT nama_jabatan, deskripsi, gaji_pokok
FROM jabatan
ORDER BY gaji_pokok DESC;

-- Total anggaran gaji jika tiap jabatan diisi 1 orang
SELECT SUM(gaji_pokok) AS total_anggaran_gaji FROM jabatan;


-- ----------------------------------------------------------------
--  TABEL 2 : karyawan
-- ----------------------------------------------------------------

CREATE TABLE karyawan (
    id_karyawan   SERIAL       PRIMARY KEY,
    id_jabatan    INT          NOT NULL REFERENCES jabatan(id_jabatan),
    nama_karyawan VARCHAR(100) NOT NULL,
    email         VARCHAR(100) UNIQUE,
    telepon       VARCHAR(20),
    alamat        TEXT,
    nim_password  VARCHAR(20),
    tanggal_masuk DATE         NOT NULL DEFAULT CURRENT_DATE,
    status_aktif  BOOLEAN      NOT NULL DEFAULT TRUE,
    created_at    TIMESTAMP    NOT NULL DEFAULT NOW()
);

INSERT INTO karyawan
    (id_karyawan, id_jabatan, nama_karyawan, email, telepon, nim_password, tanggal_masuk)
VALUES
    (1, 2, 'Basya',      'basya@tecafe.id',      '081311111111', '251712033', '2024-01-01'),
    (2, 1, 'Tesalonika', 'tesalonika@tecafe.id', '081322222222', '251712036', '2024-01-01'),
    (3, 3, 'Farah',      'farah@tecafe.id',      '081333333333', '251712038', '2023-08-15'),
    (4, 4, 'Margareth',  'margareth@tecafe.id',  '081344444444', '251712043', '2023-08-15'),
    (5, 5, 'Indah',      'indah@tecafe.id',      '081355555555', '251712030', '2024-01-10');

SELECT setval('karyawan_id_karyawan_seq', 10);

-- Lihat semua data karyawan
SELECT * FROM karyawan ORDER BY id_karyawan;

-- Karyawan lengkap dengan nama jabatan 
SELECT
    k.id_karyawan,
    k.nama_karyawan,
    j.nama_jabatan,
    j.gaji_pokok,
    k.email,
    k.telepon,
    k.nim_password   AS password_login,
    k.tanggal_masuk,
    k.status_aktif
FROM karyawan k
JOIN jabatan j ON j.id_jabatan = k.id_jabatan
ORDER BY j.id_jabatan, k.nama_karyawan;

-- Hanya karyawan yang masih aktif
SELECT
    k.id_karyawan,
    k.nama_karyawan,
    j.nama_jabatan,
    k.email,
    k.telepon,
    k.tanggal_masuk,
    k.nim_password
FROM karyawan k
JOIN jabatan j ON j.id_jabatan = k.id_jabatan
WHERE k.status_aktif = TRUE
ORDER BY j.id_jabatan, k.nama_karyawan;

-- Simulasi query login 
SELECT
    k.id_karyawan,
    k.nama_karyawan,
    j.nama_jabatan,
    k.status_aktif,
    k.nim_password
FROM karyawan k
JOIN jabatan j ON j.id_jabatan = k.id_jabatan
WHERE LOWER(k.email) = LOWER('basya@tecafe.id');

-- Karyawan nonaktif / sudah dipecat
SELECT id_karyawan, nama_karyawan, email, tanggal_masuk
FROM karyawan
WHERE status_aktif = FALSE;

-- Jumlah karyawan aktif per jabatan
SELECT
    j.nama_jabatan,
    COUNT(k.id_karyawan) AS jumlah_karyawan
FROM jabatan j
LEFT JOIN karyawan k ON k.id_jabatan = j.id_jabatan
                     AND k.status_aktif = TRUE
GROUP BY j.id_jabatan, j.nama_jabatan
ORDER BY j.id_jabatan;


-- ----------------------------------------------------------------
--  TABEL 3 : shift
-- ----------------------------------------------------------------

CREATE TABLE shift (
    id_shift   SERIAL      PRIMARY KEY,
    nama_shift VARCHAR(30) NOT NULL UNIQUE,
    jam_masuk  TIME        NOT NULL,
    jam_keluar TIME        NOT NULL,
    keterangan TEXT
);

INSERT INTO shift (nama_shift, jam_masuk, jam_keluar, keterangan) VALUES
    ('Pagi',   '07:00', '14:00', 'Shift pagi hari'),
    ('Siang',  '14:00', '21:00', 'Shift siang-malam'),
    ('Malam',  '21:00', '07:00', 'Shift malam (khusus)');

-- Lihat semua data shift
SELECT * FROM shift ORDER BY id_shift;

-- Durasi kerja tiap shift dalam jam
SELECT
    nama_shift,
    jam_masuk,
    jam_keluar,
    CASE
        WHEN jam_keluar > jam_masuk
            THEN ROUND(EXTRACT(EPOCH FROM (jam_keluar - jam_masuk)) / 3600, 1)
        ELSE
            ROUND(EXTRACT(EPOCH FROM (jam_keluar + INTERVAL '24 hour' - jam_masuk)) / 3600, 1)
    END AS durasi_jam
FROM shift
ORDER BY id_shift;


-- ----------------------------------------------------------------
--  TABEL 4 : meja
-- ----------------------------------------------------------------


CREATE TABLE meja (
    id_meja    SERIAL      PRIMARY KEY,
    nomor_meja VARCHAR(10) NOT NULL UNIQUE,
    kapasitas  INT         NOT NULL DEFAULT 4,
    lokasi     VARCHAR(50),
    status     VARCHAR(20) NOT NULL DEFAULT 'tersedia',
    created_at TIMESTAMP   NOT NULL DEFAULT NOW(),
    CONSTRAINT chk_status_meja CHECK (
        status IN ('tersedia','terisi','reservasi','nonaktif')
    )
);

INSERT INTO meja (nomor_meja, kapasitas, lokasi, status) VALUES
    ('A1',  2, 'Indoor',   'tersedia'),
    ('A2',  2, 'Indoor',   'tersedia'),
    ('A3',  4, 'Indoor',   'tersedia'),
    ('A4',  4, 'Indoor',   'tersedia'),
    ('A5',  4, 'Indoor',   'tersedia'),
    ('A6',  6, 'Indoor',   'tersedia'),
    ('B1',  4, 'Outdoor',  'tersedia'),
    ('B2',  4, 'Outdoor',  'tersedia'),
    ('B3',  6, 'Outdoor',  'tersedia'),
    ('B4',  6, 'Outdoor',  'tersedia'),
    ('B5',  8, 'Outdoor',  'tersedia'),
    ('C1',  4, 'Teras',    'tersedia'),
    ('C2',  4, 'Teras',    'tersedia'),
    ('C3',  6, 'Teras',    'tersedia'),
    ('V1',  8, 'VIP',      'tersedia'),
    ('V2',  8, 'VIP',      'tersedia'),
    ('V3', 10, 'VIP',      'tersedia'),
    ('V4', 12, 'VIP Hall', 'tersedia');

-- Lihat semua data meja
SELECT * FROM meja ORDER BY lokasi, nomor_meja;

-- Status meja beserta jumlah pesanan hari ini (tampilSemuaMeja)
SELECT
    m.id_meja,
    m.nomor_meja,
    m.kapasitas,
    m.lokasi,
    m.status,
    COUNT(p.id_pesanan)
        FILTER (WHERE DATE(p.waktu_pesan) = CURRENT_DATE) AS pesanan_hari_ini
FROM meja m
LEFT JOIN pesanan p ON p.nomor_meja = m.nomor_meja
GROUP BY m.id_meja
ORDER BY m.lokasi, m.nomor_meja;

-- Meja yang tersedia untuk dipilih saat pesanan baru
SELECT nomor_meja, kapasitas, lokasi, status
FROM meja
WHERE status = 'tersedia'
ORDER BY lokasi, nomor_meja;

-- Meja yang sedang terisi (tampilMejaTermisi)
SELECT nomor_meja, kapasitas, lokasi
FROM meja
WHERE status = 'terisi'
ORDER BY nomor_meja;

-- Ringkasan: berapa meja per status
SELECT status, COUNT(*) AS jumlah_meja
FROM meja
GROUP BY status
ORDER BY status;

-- Total kapasitas per area/lokasi
SELECT
    lokasi,
    COUNT(*) AS jumlah_meja,
    SUM(kapasitas) AS total_kapasitas
FROM meja
GROUP BY lokasi
ORDER BY lokasi;


-- ----------------------------------------------------------------
--  TABEL 5 : pelanggan
-- ----------------------------------------------------------------


CREATE TABLE pelanggan (
    id_pelanggan   SERIAL       PRIMARY KEY,
    nama           VARCHAR(100) NOT NULL,
    telepon        VARCHAR(20)  UNIQUE,
    email          VARCHAR(100) UNIQUE,
    poin           INT          NOT NULL DEFAULT 0,
    tanggal_daftar DATE         NOT NULL DEFAULT CURRENT_DATE,
    created_at     TIMESTAMP    NOT NULL DEFAULT NOW()
);

INSERT INTO pelanggan (nama, telepon, email, poin) VALUES
    ('Andi Wijaya',   '085100000001', 'andi.w@email.com',  120),
    ('Budi Santoso',  '085100000002', 'budi.s@email.com',   85),
    ('Citra Lestari', '085100000003', 'citra.l@email.com', 200),
    ('Dewi Rahayu',   '085100000004', 'dewi.r@email.com',   55),
    ('Eko Prasetyo',  '085100000005', 'eko.p@email.com',   310);

-- Lihat semua data pelanggan
SELECT * FROM pelanggan ORDER BY id_pelanggan;

-- Semua pelanggan + total transaksi & total belanja (tampilSemuaPelanggan)
SELECT
    p.id_pelanggan,
    p.nama,
    p.telepon,
    p.email,
    p.poin,
    p.tanggal_daftar,
    COUNT(ps.id_pesanan)             AS total_transaksi,
    COALESCE(SUM(ps.total_akhir), 0) AS total_belanja
FROM pelanggan p
LEFT JOIN pesanan ps ON ps.id_pelanggan = p.id_pelanggan
GROUP BY p.id_pelanggan
ORDER BY total_belanja DESC;

-- Top 10 member terbaik (tampilTopPelanggan)
SELECT
    p.id_pelanggan,
    p.nama,
    p.telepon,
    p.poin,
    COUNT(ps.id_pesanan)             AS total_transaksi,
    COALESCE(SUM(ps.total_akhir), 0) AS total_belanja
FROM pelanggan p
LEFT JOIN pesanan ps ON ps.id_pelanggan = p.id_pelanggan
GROUP BY p.id_pelanggan
ORDER BY total_belanja DESC
LIMIT 10;

-- Cari pelanggan by nama / HP / email (cariPelanggan)
SELECT
    p.id_pelanggan,
    p.nama,
    p.telepon,
    p.email,
    p.poin
FROM pelanggan p
WHERE LOWER(p.nama)  LIKE LOWER('%andi%')
   OR p.telepon      LIKE '%andi%'
   OR LOWER(p.email) LIKE LOWER('%andi%')
ORDER BY p.nama;

-- Pelanggan yang belum pernah bertransaksi
SELECT p.id_pelanggan, p.nama, p.telepon, p.tanggal_daftar
FROM pelanggan p
LEFT JOIN pesanan ps ON ps.id_pelanggan = p.id_pelanggan
WHERE ps.id_pesanan IS NULL;

-- Kategori poin member
SELECT
    CASE
        WHEN poin = 0                 THEN '0 poin  — Baru Daftar'
        WHEN poin BETWEEN 1 AND 100   THEN '1-100   — Bronze'
        WHEN poin BETWEEN 101 AND 300 THEN '101-300 — Silver'
        ELSE                               '> 300   — Gold'
    END AS level_member,
    COUNT(*) AS jumlah
FROM pelanggan
GROUP BY level_member
ORDER BY MIN(poin);


-- ----------------------------------------------------------------
--  TABEL 6 : kategori
-- ----------------------------------------------------------------


CREATE TABLE kategori (
    id_kategori   SERIAL      PRIMARY KEY,
    nama_kategori VARCHAR(50) NOT NULL
);

INSERT INTO kategori (nama_kategori) VALUES
    ('Minuman'),
    ('Makanan'),
    ('Snack');

-- Lihat semua data kategori
SELECT * FROM kategori ORDER BY id_kategori;

-- Jumlah menu aktif & nonaktif per kategori
SELECT
    k.id_kategori,
    k.nama_kategori,
    COUNT(m.id_menu)
        FILTER (WHERE m.tersedia = TRUE)  AS menu_aktif,
    COUNT(m.id_menu)
        FILTER (WHERE m.tersedia = FALSE) AS menu_nonaktif,
    COUNT(m.id_menu)                       AS total_menu
FROM kategori k
LEFT JOIN menu m ON m.id_kategori = k.id_kategori
GROUP BY k.id_kategori, k.nama_kategori
ORDER BY k.id_kategori;


-- ----------------------------------------------------------------
--  TABEL 7 : supplier
-- ----------------------------------------------------------------


CREATE TABLE supplier (
    id_supplier   SERIAL       PRIMARY KEY,
    nama_supplier VARCHAR(100) NOT NULL,
    kontak        VARCHAR(100),
    telepon       VARCHAR(20),
    alamat        TEXT,
    email         VARCHAR(100),
    created_at    TIMESTAMP    NOT NULL DEFAULT NOW()
);

INSERT INTO supplier (nama_supplier, kontak, telepon, alamat) VALUES
    ('CV Segar Jaya',      'Pak Jaya',   '0811111111', 'Jl. Pasar Baru No.1, Medan'),
    ('UD Kopi Nusantara',  'Bu Dewi',    '0822222222', 'Jl. Sisingamangaraja No.5, Medan'),
    ('Toko Bahan Makanan', 'Pak Hendra', '0833333333', 'Jl. Gatot Subroto No.12, Medan'),
    ('Distributor Susu',   'Bu Ani',     '0844444444', 'Jl. Diponegoro No.8, Medan'),
    ('Supplier Buah',      'Pak Rudi',   '0855555555', 'Jl. Asia No.22, Medan');

-- Lihat semua data supplier
SELECT * FROM supplier ORDER BY id_supplier;

-- Supplier beserta jumlah bahan yang dipasok
SELECT
    s.id_supplier,
    s.nama_supplier,
    s.kontak,
    s.telepon,
    COUNT(b.id_bahan) AS jumlah_bahan_dipasok
FROM supplier s
LEFT JOIN bahan_baku b ON b.id_supplier = s.id_supplier
GROUP BY s.id_supplier
ORDER BY jumlah_bahan_dipasok DESC;

-- Supplier yang memasok bahan dengan stok kritis (perlu dihubungi)
SELECT DISTINCT
    s.nama_supplier,
    s.telepon,
    b.nama_bahan,
    b.stok,
    b.stok_minimum
FROM supplier s
JOIN bahan_baku b ON b.id_supplier = s.id_supplier
WHERE b.stok <= b.stok_minimum
ORDER BY s.nama_supplier;


-- ----------------------------------------------------------------
--  TABEL 8 : satuan
-- ----------------------------------------------------------------


CREATE TABLE satuan (
    id_satuan   SERIAL      PRIMARY KEY,
    nama_satuan VARCHAR(20) NOT NULL UNIQUE,
    singkatan   VARCHAR(10) NOT NULL UNIQUE
);

INSERT INTO satuan (nama_satuan, singkatan) VALUES
    ('Kilogram',  'kg'),
    ('Gram',      'gr'),
    ('Liter',     'ltr'),
    ('Mililiter', 'ml'),
    ('Buah/Pcs',  'pcs'),
    ('Sachet',    'sct'),
    ('Botol',     'btl'),
    ('Pak',       'pak');

-- Lihat semua data satuan
SELECT * FROM satuan ORDER BY id_satuan;

-- Satuan yang paling banyak digunakan di bahan baku
SELECT
    s.nama_satuan,
    s.singkatan,
    COUNT(b.id_bahan) AS dipakai_di_bahan
FROM satuan s
LEFT JOIN bahan_baku b ON b.id_satuan = s.id_satuan
GROUP BY s.id_satuan
ORDER BY dipakai_di_bahan DESC;


-- ----------------------------------------------------------------
--  TABEL 9 : bahan_baku
-- ----------------------------------------------------------------


CREATE TABLE bahan_baku (
    id_bahan     SERIAL        PRIMARY KEY,
    id_supplier  INT           REFERENCES supplier(id_supplier),
    id_satuan    INT           NOT NULL REFERENCES satuan(id_satuan),
    nama_bahan   VARCHAR(100)  NOT NULL,
    stok         NUMERIC(10,2) NOT NULL DEFAULT 0,
    stok_minimum NUMERIC(10,2) NOT NULL DEFAULT 5,
    harga_beli   NUMERIC(12,2) NOT NULL DEFAULT 0,
    created_at   TIMESTAMP     NOT NULL DEFAULT NOW()
);

INSERT INTO bahan_baku (id_supplier, id_satuan, nama_bahan, stok, stok_minimum, harga_beli) VALUES
    (2, 2, 'Kopi Arabika',    500,  100, 120000),
    (2, 2, 'Kopi Robusta',    500,  100,  80000),
    (4, 4, 'Susu Segar',     5000, 1000,  18000),
    (1, 6, 'Teh Celup',       100,   20,  15000),
    (1, 2, 'Gula Pasir',     3000,  500,  14000),
    (1, 5, 'Telur Ayam',      500,   50,   2000),
    (3, 2, 'Tepung Terigu',  5000, 1000,  10000),
    (3, 2, 'Beras',         10000, 2000,  13000),
    (1, 2, 'Ayam Fillet',    3000,  500,  38000),
    (1, 7, 'Sirup Coklat',     10,    2,  35000),
    (5, 2, 'Buah Alpukat',    200,   30,  25000),
    (5, 2, 'Buah Mangga',     200,   30,  20000),
    (3, 2, 'Daging Sapi',    2000,  300,  95000),
    (3, 8, 'Sosis',           100,   20,  25000),
    (4, 7, 'Susu Kental',      30,    5,  15000),
    (2, 2, 'Bubuk Matcha',    500,   50, 180000),
    (3, 2, 'Kentang',        3000,  500,  15000),
    (1, 7, 'Minyak Goreng',    20,    5,  18000);

-- Lihat semua data bahan baku
SELECT * FROM bahan_baku ORDER BY id_bahan;

-- Bahan baku lengkap + satuan + supplier + kondisi stok
SELECT
    b.id_bahan,
    b.nama_bahan,
    s.singkatan       AS satuan,
    b.stok,
    b.stok_minimum,
    CASE
        WHEN b.stok <= b.stok_minimum       THEN '⚠ KRITIS — segera restock'
        WHEN b.stok <= b.stok_minimum * 2   THEN '⚡ Perlu diperhatikan'
        ELSE                                     '✔ Aman'
    END               AS kondisi_stok,
    b.harga_beli,
    sp.nama_supplier,
    sp.telepon        AS telp_supplier
FROM bahan_baku b
JOIN satuan       s  ON s.id_satuan    = b.id_satuan
LEFT JOIN supplier sp ON sp.id_supplier = b.id_supplier
ORDER BY b.id_bahan;

-- Bahan baku stok kritis (tampilBahanMenipis)
SELECT
    b.id_bahan,
    b.nama_bahan,
    s.singkatan,
    b.stok,
    b.stok_minimum,
    sp.nama_supplier,
    sp.telepon
FROM bahan_baku b
JOIN satuan       s  ON s.id_satuan    = b.id_satuan
LEFT JOIN supplier sp ON sp.id_supplier = b.id_supplier
WHERE b.stok <= b.stok_minimum
ORDER BY b.stok ASC;

-- Estimasi nilai persediaan bahan baku saat ini
SELECT
    b.nama_bahan,
    s.singkatan,
    b.stok,
    b.harga_beli,
    ROUND(b.stok * b.harga_beli) AS nilai_persediaan
FROM bahan_baku b
JOIN satuan s ON s.id_satuan = b.id_satuan
ORDER BY nilai_persediaan DESC;

-- Total nilai seluruh bahan baku di gudang
SELECT
    SUM(ROUND(b.stok * b.harga_beli)) AS total_nilai_gudang
FROM bahan_baku b;


-- ----------------------------------------------------------------
--  TABEL 10 : menu
-- ----------------------------------------------------------------


CREATE TABLE menu (
    id_menu     SERIAL        PRIMARY KEY,
    id_kategori INT           NOT NULL REFERENCES kategori(id_kategori),
    nama_menu   VARCHAR(100)  NOT NULL,
    deskripsi   TEXT,
    harga       NUMERIC(12,2) NOT NULL,
    stok        INT           NOT NULL DEFAULT 0,
    tersedia    BOOLEAN       NOT NULL DEFAULT TRUE,
    created_at  TIMESTAMP     NOT NULL DEFAULT NOW()
);

-- Minuman (id_kategori = 1, id_menu 1-20)
INSERT INTO menu (id_menu, id_kategori, nama_menu, deskripsi, harga, stok) VALUES
    ( 1, 1, 'Es Teh Manis',           'Teh manis dingin segar',             5000,  80),
    ( 2, 1, 'Es Jeruk',               'Jeruk peras segar',                  7000,  60),
    ( 3, 1, 'Kopi Hitam',             'Kopi arabika murni',                 8000,  50),
    ( 4, 1, 'Kopi Susu',              'Kopi dengan susu segar',            12000,  50),
    ( 5, 1, 'Matcha Latte',           'Matcha Jepang dengan susu',         18000,  40),
    ( 6, 1, 'Jus Alpukat',            'Jus alpukat segar dengan susu',     15000,  35),
    ( 7, 1, 'Jus Mangga',             'Jus mangga segar',                  13000,  35),
    ( 8, 1, 'Air Mineral',            'Air mineral botol',                  3000, 200),
    ( 9, 1, 'Teh Tarik',              'Teh khas dengan susu kental',       10000,  50),
    (10, 1, 'Coklat Panas',           'Coklat premium panas',              12000,  45),
    (11, 1, 'Cappuccino',             'Espresso dengan foam susu',         20000,  30),
    (12, 1, 'Americano',              'Espresso encer air panas',          15000,  30),
    (13, 1, 'Lemon Tea',              'Teh dengan perasan lemon',           9000,  50),
    (14, 1, 'Es Kopi Susu Gula Aren', 'Kopi susu gula aren kekinian',     18000,  40),
    (15, 1, 'Thai Tea',               'Teh susu ala Thailand',             15000,  40),
    (16, 1, 'Strawberry Smoothie',    'Smoothie strawberry segar',         20000,  25),
    (17, 1, 'Mango Lassi',            'Minuman yogurt mangga India',       18000,  25),
    (18, 1, 'Es Cincau',              'Es cincau hitam menyegarkan',        8000,  50),
    (19, 1, 'Soda Gembira',           'Soda dengan es krim dan sirup',     12000,  40),
    (20, 1, 'Jus Jambu',              'Jus jambu biji merah',              12000,  30);

-- Makanan (id_kategori = 2, id_menu 21-40)
INSERT INTO menu (id_menu, id_kategori, nama_menu, deskripsi, harga, stok) VALUES
    (21, 2, 'Nasi Goreng Spesial', 'Nasi goreng dengan telur & ayam',    22000, 30),
    (22, 2, 'Mie Goreng',          'Mie goreng bumbu special',            18000, 30),
    (23, 2, 'Ayam Bakar',          'Ayam bakar bumbu kecap',              28000, 20),
    (24, 2, 'Soto Ayam',           'Soto ayam kuah bening',               18000, 25),
    (25, 2, 'Gado-Gado',           'Sayuran dengan saus kacang',          16000, 20),
    (26, 2, 'Nasi Uduk',           'Nasi gurih dengan lauk',              17000, 25),
    (27, 2, 'Bakso',               'Bakso sapi dengan mie',               18000, 30),
    (28, 2, 'Burger Jumbo',        'Burger isi daging sapi double',       32000, 15),
    (29, 2, 'Mie Kuah',            'Mie kuah dengan telur dan sayur',     15000, 25),
    (30, 2, 'Sosis Bakar',         'Sosis dibakar dengan bumbu spesial',  16000, 25),
    (31, 2, 'Nasi Putih',          'Nasi putih hangat',                    5000, 50),
    (32, 2, 'Ayam Goreng',         'Ayam goreng crispy',                  22000, 25),
    (33, 2, 'Mie Rebus',           'Mie rebus kuah pedas',                15000, 25),
    (34, 2, 'Steak Ayam',          'Steak ayam dengan saus lada',         30000, 15),
    (35, 2, 'Steak Sapi',          'Steak sapi dengan saus mushroom',     35000, 10),
    (36, 2, 'Sandwich',            'Sandwich roti dengan isian daging',   20000, 20),
    (37, 2, 'Pasta Bolognese',     'Pasta dengan saus daging tomat',      25000, 15),
    (38, 2, 'Pizza Mini',          'Pizza mini topping keju & ayam',      28000, 10),
    (39, 2, 'Salad Buah',          'Salad buah segar dengan mayo',        15000, 20),
    (40, 2, 'Sup Ayam',            'Sup ayam hangat dengan sayur',        16000, 20);

-- Snack (id_kategori = 3, id_menu 41-60)
INSERT INTO menu (id_menu, id_kategori, nama_menu, deskripsi, harga, stok) VALUES
    (41, 3, 'French Fries',         'Kentang goreng crispy',               13000, 50),
    (42, 3, 'Onion Ring',           'Cincin bawang goreng',                12000, 40),
    (43, 3, 'Pisang Goreng Crispy', 'Pisang goreng tepung crispy',          9000, 45),
    (44, 3, 'Nugget Ayam',          'Nugget ayam goreng',                  12000, 40),
    (45, 3, 'Roti Bakar',           'Roti panggang dengan selai',           9000, 35),
    (46, 3, 'Dimsum',               'Dimsum kukus variasi',                16000, 30),
    (47, 3, 'Cireng',               'Aci goreng sambal',                    8000, 50),
    (48, 3, 'Tahu Crispy',          'Tahu goreng dengan saus',              9000, 40),
    (49, 3, 'Donat',                'Donat glaze berbagai rasa',            6000, 50),
    (50, 3, 'Cake Coklat',          'Slice cake coklat premium',           14000, 25),
    (51, 3, 'Kentang Wedges',       'Kentang wedges dengan saus',          14000, 35),
    (52, 3, 'Mozzarella Stick',     'Stik keju mozzarella goreng',         18000, 25),
    (53, 3, 'Bruschetta',           'Roti panggang topping tomat',         15000, 20),
    (54, 3, 'Potato Skin',          'Kulit kentang isi keju & bacon',      20000, 20),
    (55, 3, 'Waffles',              'Waffle dengan butter & madu',         16000, 25),
    (56, 3, 'Cheesecake',           'Slice cheesecake NY style',           18000, 20),
    (57, 3, 'Churros',              'Churros dengan saus coklat',          15000, 20),
    (58, 3, 'Tempura Udang',        'Udang goreng tepung ala Jepang',      20000, 15),
    (59, 3, 'Keripik Singkong',     'Keripik singkong renyah pedas',        8000, 40),
    (60, 3, 'Martabak Mini',        'Martabak mini manis berbagai rasa',   12000, 30);

SELECT setval('menu_id_menu_seq', 70);

-- Lihat semua data menu
SELECT * FROM menu ORDER BY id_kategori, id_menu;

-- Semua menu + nama kategori (tampilSemuaMenu di Java)
SELECT
    m.id_menu,
    k.nama_kategori,
    m.nama_menu,
    m.deskripsi,
    m.harga,
    m.stok,
    CASE WHEN m.tersedia THEN '✔ Aktif' ELSE '✘ Nonaktif' END AS status
FROM menu m
JOIN kategori k ON k.id_kategori = m.id_kategori
ORDER BY k.id_kategori, m.id_menu;

-- Menu Minuman saja — tersedia & ada stok (dipakai pesanan baru)
SELECT id_menu, nama_menu, harga, stok
FROM menu
WHERE id_kategori = 1 AND tersedia = TRUE AND stok > 0
ORDER BY id_menu;

-- Menu Makanan saja
SELECT id_menu, nama_menu, harga, stok
FROM menu
WHERE id_kategori = 2 AND tersedia = TRUE AND stok > 0
ORDER BY id_menu;

-- Menu Snack saja
SELECT id_menu, nama_menu, harga, stok
FROM menu
WHERE id_kategori = 3 AND tersedia = TRUE AND stok > 0
ORDER BY id_menu;

-- Menu stok kritis ≤ 5 (tampilStokMenipis)
SELECT
    m.id_menu,
    k.nama_kategori,
    m.nama_menu,
    m.stok,
    m.harga
FROM menu m
JOIN kategori k ON k.id_kategori = m.id_kategori
WHERE m.stok <= 5 AND m.tersedia = TRUE
ORDER BY m.stok ASC;

-- Harga rata-rata, termurah, termahal per kategori
SELECT
    k.nama_kategori,
    ROUND(AVG(m.harga)) AS harga_rata_rata,
    MIN(m.harga)        AS harga_termurah,
    MAX(m.harga)        AS harga_termahal
FROM menu m
JOIN kategori k ON k.id_kategori = m.id_kategori
WHERE m.tersedia = TRUE
GROUP BY k.id_kategori, k.nama_kategori
ORDER BY k.id_kategori;


-- ----------------------------------------------------------------
--  TABEL 11 : resep
-- ----------------------------------------------------------------


CREATE TABLE resep (
    id_resep   SERIAL        PRIMARY KEY,
    id_menu    INT           NOT NULL REFERENCES menu(id_menu) ON DELETE CASCADE,
    id_bahan   INT           NOT NULL REFERENCES bahan_baku(id_bahan),
    jumlah     NUMERIC(10,2) NOT NULL,
    keterangan TEXT,
    UNIQUE(id_menu, id_bahan)
);

INSERT INTO resep (id_menu, id_bahan, jumlah, keterangan) VALUES
    ( 3,  1,  15, '15 gr kopi arabika per cangkir'),
    ( 3,  5,   5, '5 gr gula pasir'),
    ( 4,  1,  15, '15 gr kopi arabika'),
    ( 4,  3, 100, '100 ml susu segar'),
    ( 5, 16,  10, '10 gr bubuk matcha'),
    ( 5,  3, 120, '120 ml susu segar'),
    ( 6, 11, 150, '150 gr alpukat'),
    ( 7, 12, 150, '150 gr mangga'),
    (21,  8, 200, '200 gr beras nasi'),
    (21,  6,   2, '2 butir telur'),
    (21,  9,  50, '50 gr ayam'),
    (23,  9, 150, '150 gr ayam fillet'),
    (41, 17, 150, '150 gr kentang'),
    (41, 18,  20, '20 ml minyak goreng');

-- Lihat semua data resep
SELECT * FROM resep ORDER BY id_menu, id_resep;

-- Resep lengkap beserta nama menu dan nama bahan
SELECT
    r.id_resep,
    m.nama_menu,
    b.nama_bahan,
    r.jumlah,
    s.singkatan AS satuan,
    r.keterangan
FROM resep r
JOIN menu       m ON m.id_menu   = r.id_menu
JOIN bahan_baku b ON b.id_bahan  = r.id_bahan
JOIN satuan     s ON s.id_satuan = b.id_satuan
ORDER BY m.id_menu, r.id_resep;

-- Resep untuk 1 menu tertentu (contoh: Kopi Susu = id 4)
SELECT
    b.nama_bahan,
    r.jumlah,
    s.singkatan AS satuan,
    r.keterangan
FROM resep r
JOIN bahan_baku b ON b.id_bahan  = r.id_bahan
JOIN satuan     s ON s.id_satuan = b.id_satuan
WHERE r.id_menu = 4;

-- Bahan yang paling banyak dipakai di berbagai menu
SELECT
    b.nama_bahan,
    COUNT(r.id_resep) AS dipakai_di_berapa_menu
FROM resep r
JOIN bahan_baku b ON b.id_bahan = r.id_bahan
GROUP BY b.id_bahan, b.nama_bahan
ORDER BY dipakai_di_berapa_menu DESC;


-- ----------------------------------------------------------------
--  TABEL 12 : metode_bayar
-- ----------------------------------------------------------------


CREATE TABLE metode_bayar (
    id_metode   SERIAL      PRIMARY KEY,
    nama_metode VARCHAR(50) NOT NULL UNIQUE,
    keterangan  TEXT,
    aktif       BOOLEAN     NOT NULL DEFAULT TRUE
);

INSERT INTO metode_bayar (nama_metode, keterangan) VALUES
    ('Tunai',    'Pembayaran langsung dengan uang cash'),
    ('Transfer', 'Transfer bank / mobile banking'),
    ('QRIS',     'Scan QR Code (GoPay, OVO, Dana, ShopeePay)'),
    ('Debit',    'Kartu debit EDC'),
    ('Kredit',   'Kartu kredit EDC');

-- Lihat semua data metode bayar
SELECT * FROM metode_bayar ORDER BY id_metode;

-- Metode yang aktif saja (dipakai saat pesanan baru)
SELECT id_metode, nama_metode, keterangan
FROM metode_bayar
WHERE aktif = TRUE
ORDER BY id_metode;

-- Statistik penggunaan metode bayar (butuh data pembayaran)
SELECT
    mb.nama_metode,
    COUNT(pb.id_pembayaran)    AS jumlah_transaksi,
    COALESCE(SUM(pb.jumlah_bayar), 0) AS total_uang_diterima
FROM metode_bayar mb
LEFT JOIN pembayaran pb ON pb.id_metode = mb.id_metode
GROUP BY mb.id_metode, mb.nama_metode
ORDER BY jumlah_transaksi DESC;


-- ----------------------------------------------------------------
--  TABEL 13 : pesanan
-- ----------------------------------------------------------------


CREATE TABLE pesanan (
    id_pesanan   SERIAL        PRIMARY KEY,
    id_karyawan  INT           REFERENCES karyawan(id_karyawan),
    id_pelanggan INT           REFERENCES pelanggan(id_pelanggan),
    nomor_meja   VARCHAR(10)   NOT NULL,
    nama_pemesan VARCHAR(100)  NOT NULL,
    subtotal     NUMERIC(12,2) NOT NULL DEFAULT 0,
    diskon       NUMERIC(12,2) NOT NULL DEFAULT 0,
    biaya_parkir NUMERIC(12,2) NOT NULL DEFAULT 2000,
    total_akhir  NUMERIC(12,2) NOT NULL DEFAULT 0,
    waktu_pesan  TIMESTAMP     NOT NULL DEFAULT NOW(),
    status       VARCHAR(20)   NOT NULL DEFAULT 'selesai',
    catatan      TEXT,
    CONSTRAINT chk_status_pesanan CHECK (
        status IN ('proses','selesai','dibatalkan')
    )
);


-- ----------------------------------------------------------------
--  TABEL 14 : detail_pesanan
-- ----------------------------------------------------------------


CREATE TABLE detail_pesanan (
    id_detail     SERIAL        PRIMARY KEY,
    id_pesanan    INT           NOT NULL REFERENCES pesanan(id_pesanan) ON DELETE CASCADE,
    id_menu       INT           NOT NULL REFERENCES menu(id_menu),
    jumlah        INT           NOT NULL CHECK (jumlah > 0),
    harga_satuan  NUMERIC(12,2) NOT NULL,
    subtotal_item NUMERIC(12,2) NOT NULL,
    catatan_item  TEXT
);


-- ----------------------------------------------------------------
--  TABEL 15 : pembayaran
-- ----------------------------------------------------------------


CREATE TABLE pembayaran (
    id_pembayaran SERIAL        PRIMARY KEY,
    id_pesanan    INT           NOT NULL UNIQUE REFERENCES pesanan(id_pesanan) ON DELETE CASCADE,
    id_metode     INT           NOT NULL REFERENCES metode_bayar(id_metode) DEFAULT 1,
    jumlah_bayar  NUMERIC(12,2) NOT NULL,
    kembalian     NUMERIC(12,2) NOT NULL DEFAULT 0,
    waktu_bayar   TIMESTAMP     NOT NULL DEFAULT NOW(),
    status_bayar  VARCHAR(20)   NOT NULL DEFAULT 'lunas',
    CONSTRAINT chk_status_bayar CHECK (
        status_bayar IN ('lunas','pending','gagal')
    )
);


-- ================================================================
--  DATA TRANSAKSI SAMPLE
--  (stok dikurangi manual karena trigger belum aktif saat ini)
-- ================================================================

-- Transaksi 1: Member Andi (id=1), diskon 5%
INSERT INTO pesanan
    (id_karyawan, id_pelanggan, nomor_meja, nama_pemesan,
     subtotal, diskon, biaya_parkir, total_akhir, status, waktu_pesan)
VALUES
    (1, 1, 'A3', 'Andi Wijaya', 65000, 3250, 2000, 63750, 'selesai', NOW() - INTERVAL '2 days');

INSERT INTO detail_pesanan (id_pesanan, id_menu, jumlah, harga_satuan, subtotal_item) VALUES
    (1,  4, 2, 12000, 24000),
    (1, 21, 1, 22000, 22000),
    (1, 41, 1, 13000, 13000),
    (1, 49, 1,  6000,  6000);

INSERT INTO pembayaran (id_pesanan, id_metode, jumlah_bayar, kembalian, status_bayar)
VALUES (1, 1, 70000, 6250, 'lunas');

UPDATE menu SET stok = stok - 2 WHERE id_menu = 4;
UPDATE menu SET stok = stok - 1 WHERE id_menu = 21;
UPDATE menu SET stok = stok - 1 WHERE id_menu = 41;
UPDATE menu SET stok = stok - 1 WHERE id_menu = 49;

-- Transaksi 2: Member Citra (id=3), diskon 10%
INSERT INTO pesanan
    (id_karyawan, id_pelanggan, nomor_meja, nama_pemesan,
     subtotal, diskon, biaya_parkir, total_akhir, status, waktu_pesan)
VALUES
    (1, 3, 'V1', 'Citra Lestari', 120000, 12000, 2000, 110000, 'selesai', NOW() - INTERVAL '1 day');

INSERT INTO detail_pesanan (id_pesanan, id_menu, jumlah, harga_satuan, subtotal_item) VALUES
    (2, 11, 2, 20000, 40000),
    (2, 23, 1, 28000, 28000),
    (2, 23, 1, 28000, 28000),
    (2, 50, 1, 14000, 14000),
    (2, 49, 2,  6000, 12000);

INSERT INTO pembayaran (id_pesanan, id_metode, jumlah_bayar, kembalian, status_bayar)
VALUES (2, 3, 110000, 0, 'lunas');

UPDATE menu SET stok = stok - 2 WHERE id_menu = 11;
UPDATE menu SET stok = stok - 2 WHERE id_menu = 23;
UPDATE menu SET stok = stok - 1 WHERE id_menu = 50;
UPDATE menu SET stok = stok - 2 WHERE id_menu = 49;

-- Transaksi 3: Non-member, hari ini
INSERT INTO pesanan
    (id_karyawan, id_pelanggan, nomor_meja, nama_pemesan,
     subtotal, diskon, biaya_parkir, total_akhir, status, waktu_pesan)
VALUES
    (1, NULL, 'B2', 'Budi Non-Member', 45000, 0, 2000, 47000, 'selesai', NOW());

INSERT INTO detail_pesanan (id_pesanan, id_menu, jumlah, harga_satuan, subtotal_item) VALUES
    (3,  5, 1, 18000, 18000),
    (3, 25, 1, 16000, 16000),
    (3, 41, 1, 13000, 13000);

INSERT INTO pembayaran (id_pesanan, id_metode, jumlah_bayar, kembalian, status_bayar)
VALUES (3, 1, 50000, 3000, 'lunas');

UPDATE menu SET stok = stok - 1 WHERE id_menu = 5;
UPDATE menu SET stok = stok - 1 WHERE id_menu = 25;
UPDATE menu SET stok = stok - 1 WHERE id_menu = 41;

-- Transaksi 4: Member Eko (id=5), diskon 15%
INSERT INTO pesanan
    (id_karyawan, id_pelanggan, nomor_meja, nama_pemesan,
     subtotal, diskon, biaya_parkir, total_akhir, status, waktu_pesan)
VALUES
    (1, 5, 'V2', 'Eko Prasetyo', 230000, 34500, 2000, 197500, 'selesai', NOW() - INTERVAL '3 days');

INSERT INTO detail_pesanan (id_pesanan, id_menu, jumlah, harga_satuan, subtotal_item) VALUES
    (4, 35, 2, 35000, 70000),
    (4, 34, 2, 30000, 60000),
    (4, 11, 3, 20000, 60000),
    (4, 54, 1, 20000, 20000),
    (4,  5, 1, 18000, 18000),
    (4,  3, 1,  8000,  8000);

INSERT INTO pembayaran (id_pesanan, id_metode, jumlah_bayar, kembalian, status_bayar)
VALUES (4, 2, 197500, 0, 'lunas');

UPDATE menu SET stok = stok - 2 WHERE id_menu = 35;
UPDATE menu SET stok = stok - 2 WHERE id_menu = 34;
UPDATE menu SET stok = stok - 3 WHERE id_menu = 11;
UPDATE menu SET stok = stok - 1 WHERE id_menu = 54;
UPDATE menu SET stok = stok - 1 WHERE id_menu = 5;
UPDATE menu SET stok = stok - 1 WHERE id_menu = 3;

-- Transaksi 5: Non-member, hari ini
INSERT INTO pesanan
    (id_karyawan, id_pelanggan, nomor_meja, nama_pemesan,
     subtotal, diskon, biaya_parkir, total_akhir, status, waktu_pesan)
VALUES
    (1, NULL, 'A1', 'Pelanggan Walk-in', 30000, 0, 2000, 32000, 'selesai', NOW() - INTERVAL '1 hour');

INSERT INTO detail_pesanan (id_pesanan, id_menu, jumlah, harga_satuan, subtotal_item) VALUES
    (5,  1, 2,  5000, 10000),
    (5, 21, 1, 22000, 22000);

INSERT INTO pembayaran (id_pesanan, id_metode, jumlah_bayar, kembalian, status_bayar)
VALUES (5, 1, 35000, 3000, 'lunas');

UPDATE menu SET stok = stok - 2 WHERE id_menu = 1;
UPDATE menu SET stok = stok - 1 WHERE id_menu = 21;

-- Update poin member setelah 5 transaksi sample
UPDATE pelanggan SET poin = poin + 6  WHERE id_pelanggan = 1;
UPDATE pelanggan SET poin = poin + 11 WHERE id_pelanggan = 3;
UPDATE pelanggan SET poin = poin + 19 WHERE id_pelanggan = 5;


-- ================================================================
--  SELECT untuk TABEL 13, 14, 15 (setelah data sample masuk)
-- ================================================================

-- Lihat semua data pesanan
SELECT * FROM pesanan ORDER BY waktu_pesan DESC;

-- 50 pesanan terbaru lengkap + kasir + tipe pelanggan (tampilRiwayatSemua)
SELECT
    p.id_pesanan,
    p.waktu_pesan,
    p.nomor_meja,
    p.nama_pemesan,
    k.nama_karyawan   AS kasir,
    CASE WHEN p.id_pelanggan IS NOT NULL THEN '★ Member' ELSE 'Non-Member' END AS tipe,
    p.subtotal,
    p.diskon,
    p.biaya_parkir,
    p.total_akhir,
    p.status,
    mb.nama_metode
FROM pesanan p
LEFT JOIN karyawan    k  ON k.id_karyawan  = p.id_karyawan
LEFT JOIN pembayaran  pb ON pb.id_pesanan  = p.id_pesanan
LEFT JOIN metode_bayar mb ON mb.id_metode  = pb.id_metode
ORDER BY p.waktu_pesan DESC
LIMIT 50;

-- Pesanan hari ini saja (tampilRiwayatHariIni)
SELECT
    p.id_pesanan,
    p.waktu_pesan,
    p.nomor_meja,
    p.nama_pemesan,
    p.total_akhir,
    p.status,
    CASE WHEN p.id_pelanggan IS NOT NULL THEN '★' ELSE ' ' END AS member
FROM pesanan p
WHERE DATE(p.waktu_pesan) = CURRENT_DATE
ORDER BY p.waktu_pesan DESC;

-- Cari pesanan by nama pemesan (cariRiwayatByNama — contoh keyword 'andi')
SELECT
    p.id_pesanan,
    p.waktu_pesan,
    p.nomor_meja,
    p.nama_pemesan,
    p.total_akhir,
    p.status,
    COUNT(dp.id_detail) AS jumlah_item
FROM pesanan p
LEFT JOIN detail_pesanan dp ON dp.id_pesanan = p.id_pesanan
WHERE LOWER(p.nama_pemesan) LIKE LOWER('%andi%')
GROUP BY p.id_pesanan
ORDER BY p.waktu_pesan DESC;

-- Pesanan by nomor meja (tampilRiwayatByMeja — contoh: 'A3')
SELECT
    p.id_pesanan,
    p.waktu_pesan,
    p.nama_pemesan,
    p.total_akhir,
    p.status,
    COUNT(dp.id_detail) AS jumlah_item
FROM pesanan p
LEFT JOIN detail_pesanan dp ON dp.id_pesanan = p.id_pesanan
WHERE p.nomor_meja = 'A3'
GROUP BY p.id_pesanan
ORDER BY p.waktu_pesan DESC
LIMIT 20;

-- Pesanan by tanggal tertentu (tampilRiwayatByTanggal)
SELECT
    p.id_pesanan,
    p.waktu_pesan,
    p.nomor_meja,
    p.nama_pemesan,
    p.total_akhir,
    p.status
FROM pesanan p
WHERE DATE(p.waktu_pesan) = CURRENT_DATE
ORDER BY p.waktu_pesan DESC;

-- Statistik hari ini: transaksi, pendapatan, diskon
SELECT
    COUNT(*)         AS total_transaksi,
    SUM(total_akhir) AS total_pendapatan,
    SUM(diskon)      AS total_diskon_member,
    AVG(total_akhir) AS rata_rata_transaksi
FROM pesanan
WHERE DATE(waktu_pesan) = CURRENT_DATE
  AND status = 'selesai';

-- ── Detail Pesanan ────────────────────────────────────────────

-- Lihat semua data detail pesanan
SELECT * FROM detail_pesanan ORDER BY id_pesanan, id_detail;

-- Detail pesanan lengkap + nama menu (tampilRiwayatLengkap)
SELECT
    dp.id_pesanan,
    p.nama_pemesan,
    p.nomor_meja,
    m.nama_menu,
    dp.jumlah,
    dp.harga_satuan,
    dp.subtotal_item,
    dp.catatan_item,
    p.waktu_pesan
FROM detail_pesanan dp
JOIN pesanan p ON p.id_pesanan = dp.id_pesanan
JOIN menu    m ON m.id_menu    = dp.id_menu
ORDER BY dp.id_pesanan, dp.id_detail;

-- Detail 1 pesanan tertentu (tampilDetailPesanan — contoh id=1)
SELECT
    m.nama_menu,
    dp.jumlah,
    dp.harga_satuan,
    dp.subtotal_item,
    dp.catatan_item
FROM detail_pesanan dp
JOIN menu m ON m.id_menu = dp.id_menu
WHERE dp.id_pesanan = 1
ORDER BY dp.id_detail;

-- Riwayat lengkap semua pesanan + semua item (tampilRiwayatLengkap)
SELECT
    p.id_pesanan,
    p.waktu_pesan,
    p.nama_pemesan,
    p.nomor_meja,
    m.nama_menu,
    dp.jumlah,
    dp.harga_satuan,
    dp.subtotal_item,
    p.total_akhir,
    p.status,
    mb.nama_metode,
    CASE WHEN p.id_pelanggan IS NOT NULL THEN '★ Member' ELSE 'Non-Member' END AS tipe
FROM pesanan p
JOIN detail_pesanan  dp ON dp.id_pesanan  = p.id_pesanan
JOIN menu            m  ON m.id_menu      = dp.id_menu
LEFT JOIN pembayaran pb ON pb.id_pesanan  = p.id_pesanan
LEFT JOIN metode_bayar mb ON mb.id_metode = pb.id_metode
ORDER BY p.waktu_pesan DESC, p.id_pesanan, dp.id_detail;

-- ── Pembayaran ────────────────────────────────────────────────

-- Lihat semua data pembayaran
SELECT * FROM pembayaran ORDER BY waktu_bayar DESC;

-- Pembayaran lengkap + info pesanan + metode
SELECT
    pb.id_pembayaran,
    p.id_pesanan,
    p.nama_pemesan,
    p.nomor_meja,
    mb.nama_metode,
    p.total_akhir,
    pb.jumlah_bayar,
    pb.kembalian,
    pb.waktu_bayar,
    pb.status_bayar
FROM pembayaran pb
JOIN pesanan      p  ON p.id_pesanan  = pb.id_pesanan
JOIN metode_bayar mb ON mb.id_metode  = pb.id_metode
ORDER BY pb.waktu_bayar DESC;

-- Pendapatan per metode bayar
SELECT
    mb.nama_metode,
    COUNT(pb.id_pembayaran)           AS jumlah_transaksi,
    SUM(pb.jumlah_bayar)              AS total_diterima,
    SUM(pb.kembalian)                 AS total_kembalian
FROM pembayaran pb
JOIN metode_bayar mb ON mb.id_metode = pb.id_metode
WHERE pb.status_bayar = 'lunas'
GROUP BY mb.id_metode, mb.nama_metode
ORDER BY total_diterima DESC;


-- ----------------------------------------------------------------
--  TABEL 16 : reservasi
-- ----------------------------------------------------------------


CREATE TABLE reservasi (
    id_reservasi    SERIAL       PRIMARY KEY,
    nama_pemesan    VARCHAR(100) NOT NULL,
    nomor_meja      VARCHAR(10)  NOT NULL,
    waktu_reservasi TIMESTAMP    NOT NULL,
    jumlah_orang    INT          NOT NULL DEFAULT 1,
    catatan         TEXT,
    created_at      TIMESTAMP    NOT NULL DEFAULT NOW()
);

-- Lihat semua data reservasi
SELECT * FROM reservasi ORDER BY waktu_reservasi;

-- Reservasi yang akan datang (dari sekarang)
SELECT
    id_reservasi,
    nama_pemesan,
    nomor_meja,
    waktu_reservasi,
    jumlah_orang,
    catatan
FROM reservasi
WHERE waktu_reservasi >= NOW()
ORDER BY waktu_reservasi;

-- Reservasi hari ini
SELECT
    nama_pemesan,
    nomor_meja,
    waktu_reservasi,
    jumlah_orang
FROM reservasi
WHERE DATE(waktu_reservasi) = CURRENT_DATE
ORDER BY waktu_reservasi;

-- Total tamu dari semua reservasi yang akan datang
SELECT SUM(jumlah_orang) AS total_tamu_reservasi
FROM reservasi
WHERE waktu_reservasi >= NOW();


-- ----------------------------------------------------------------
--  TABEL 17 : log_aktivitas
-- ----------------------------------------------------------------


CREATE TABLE log_aktivitas (
    id_log        SERIAL       PRIMARY KEY,
    id_karyawan   INT          REFERENCES karyawan(id_karyawan),
    aksi          VARCHAR(100) NOT NULL,
    tabel_terkait VARCHAR(50),
    id_referensi  INT,
    keterangan    TEXT,
    waktu         TIMESTAMP    NOT NULL DEFAULT NOW()
);

-- Lihat semua data log aktivitas (50 terbaru)
SELECT * FROM log_aktivitas ORDER BY waktu DESC LIMIT 50;

-- Log lengkap + nama karyawan
SELECT
    l.id_log,
    k.nama_karyawan,
    l.aksi,
    l.tabel_terkait,
    l.id_referensi,
    l.keterangan,
    l.waktu
FROM log_aktivitas l
LEFT JOIN karyawan k ON k.id_karyawan = l.id_karyawan
ORDER BY l.waktu DESC
LIMIT 50;

-- Riwayat login & logout karyawan
SELECT
    l.waktu,
    k.nama_karyawan,
    l.aksi,
    l.keterangan
FROM log_aktivitas l
LEFT JOIN karyawan k ON k.id_karyawan = l.id_karyawan
WHERE l.aksi IN ('LOGIN', 'LOGOUT')
ORDER BY l.waktu DESC;

-- Log update stok menu (dari trigger trg_log_menu)
SELECT waktu, keterangan
FROM log_aktivitas
WHERE aksi = 'UPDATE stok menu'
ORDER BY waktu DESC;

-- Log pesanan baru hari ini (dari trigger trg_log_pesanan)
SELECT
    l.id_log,
    k.nama_karyawan,
    l.keterangan,
    l.waktu
FROM log_aktivitas l
LEFT JOIN karyawan k ON k.id_karyawan = l.id_karyawan
WHERE l.aksi = 'INSERT pesanan baru'
  AND DATE(l.waktu) = CURRENT_DATE
ORDER BY l.waktu DESC;

-- Aktivitas per karyawan hari ini
SELECT
    k.nama_karyawan,
    COUNT(l.id_log) AS jumlah_aksi
FROM log_aktivitas l
JOIN karyawan k ON k.id_karyawan = l.id_karyawan
WHERE DATE(l.waktu) = CURRENT_DATE
GROUP BY k.id_karyawan, k.nama_karyawan
ORDER BY jumlah_aksi DESC;




-- ----------------------------------------------------------------
--  VIEW 1 : v_riwayat_pemesanan
-- ----------------------------------------------------------------


CREATE OR REPLACE VIEW v_riwayat_pemesanan AS
SELECT
    p.id_pesanan,
    p.waktu_pesan,
    p.nomor_meja,
    p.nama_pemesan,
    k.nama_karyawan                                               AS kasir,
    CASE WHEN p.id_pelanggan IS NOT NULL
         THEN '★ Member' ELSE 'Non-Member' END                    AS status_member,
    pl.nama                                                       AS nama_member,
    m.nama_menu,
    dp.jumlah,
    dp.harga_satuan,
    dp.subtotal_item,
    p.subtotal,
    p.diskon,
    p.biaya_parkir,
    p.total_akhir,
    p.status,
    pb.jumlah_bayar,
    pb.kembalian,
    mb.nama_metode                                                AS metode_bayar
FROM pesanan p
JOIN detail_pesanan   dp ON dp.id_pesanan   = p.id_pesanan
JOIN menu              m ON m.id_menu       = dp.id_menu
LEFT JOIN karyawan     k ON k.id_karyawan   = p.id_karyawan
LEFT JOIN pembayaran  pb ON pb.id_pesanan   = p.id_pesanan
LEFT JOIN metode_bayar mb ON mb.id_metode   = pb.id_metode
LEFT JOIN pelanggan   pl ON pl.id_pelanggan = p.id_pelanggan
ORDER BY p.waktu_pesan DESC, p.id_pesanan, dp.id_detail;

-- Lihat semua data view riwayat pemesanan
SELECT * FROM v_riwayat_pemesanan LIMIT 20;

-- Filter khusus member
SELECT * FROM v_riwayat_pemesanan WHERE status_member = '★ Member' ORDER BY waktu_pesan DESC;

-- Filter hari ini
SELECT * FROM v_riwayat_pemesanan WHERE DATE(waktu_pesan) = CURRENT_DATE;


-- ----------------------------------------------------------------
--  VIEW 2 : v_laporan_harian
-- ----------------------------------------------------------------


CREATE OR REPLACE VIEW v_laporan_harian AS
SELECT
    DATE(p.waktu_pesan)          AS tanggal,
    COUNT(DISTINCT p.id_pesanan) AS jumlah_transaksi,
    SUM(dp.jumlah)               AS total_item_terjual,
    SUM(p.subtotal)              AS total_subtotal,
    SUM(p.diskon)                AS total_diskon,
    SUM(p.biaya_parkir)          AS total_parkir,
    SUM(p.total_akhir)           AS total_pendapatan,
    AVG(p.total_akhir)           AS rata_rata_transaksi
FROM pesanan p
JOIN detail_pesanan dp ON dp.id_pesanan = p.id_pesanan
WHERE p.status = 'selesai'
GROUP BY DATE(p.waktu_pesan)
ORDER BY tanggal DESC;

-- Lihat semua laporan harian
SELECT * FROM v_laporan_harian;

-- 7 hari terakhir
SELECT * FROM v_laporan_harian WHERE tanggal >= CURRENT_DATE - INTERVAL '7 days';


-- ----------------------------------------------------------------
--  VIEW 3 : v_stok_menipis
-- ----------------------------------------------------------------


CREATE OR REPLACE VIEW v_stok_menipis AS
SELECT
    m.id_menu,
    m.nama_menu,
    k.nama_kategori,
    m.stok,
    m.harga,
    m.tersedia
FROM menu m
JOIN kategori k ON k.id_kategori = m.id_kategori
WHERE m.stok <= 5 AND m.tersedia = TRUE
ORDER BY m.stok ASC;

-- Lihat semua menu dengan stok menipis
SELECT * FROM v_stok_menipis;


-- ----------------------------------------------------------------
--  VIEW 4 : v_menu_terlaris
-- ----------------------------------------------------------------


CREATE OR REPLACE VIEW v_menu_terlaris AS
SELECT
    m.id_menu,
    m.nama_menu,
    k.nama_kategori,
    SUM(dp.jumlah)               AS total_terjual,
    SUM(dp.subtotal_item)        AS total_pendapatan,
    COUNT(DISTINCT p.id_pesanan) AS jumlah_order
FROM detail_pesanan dp
JOIN menu      m ON m.id_menu      = dp.id_menu
JOIN kategori  k ON k.id_kategori  = m.id_kategori
JOIN pesanan   p ON p.id_pesanan   = dp.id_pesanan
WHERE p.status = 'selesai'
GROUP BY m.id_menu, k.nama_kategori
ORDER BY total_terjual DESC;

-- Lihat top 10 menu terlaris
SELECT * FROM v_menu_terlaris LIMIT 10;

-- Terlaris per kategori Minuman
SELECT * FROM v_menu_terlaris WHERE nama_kategori = 'Minuman' LIMIT 5;

-- Terlaris per kategori Makanan
SELECT * FROM v_menu_terlaris WHERE nama_kategori = 'Makanan' LIMIT 5;

-- Terlaris per kategori Snack
SELECT * FROM v_menu_terlaris WHERE nama_kategori = 'Snack' LIMIT 5;


-- ----------------------------------------------------------------
--  VIEW 5 : v_member_terbaik
-- ----------------------------------------------------------------


CREATE OR REPLACE VIEW v_member_terbaik AS
SELECT
    p.id_pelanggan,
    p.nama,
    p.telepon,
    p.email,
    p.poin,
    p.tanggal_daftar,
    COUNT(ps.id_pesanan)             AS total_transaksi,
    COALESCE(SUM(ps.total_akhir), 0) AS total_belanja,
    MAX(ps.waktu_pesan)              AS terakhir_transaksi
FROM pelanggan p
LEFT JOIN pesanan ps ON ps.id_pelanggan = p.id_pelanggan
GROUP BY p.id_pelanggan
ORDER BY total_belanja DESC;

-- Lihat semua member terbaik
SELECT * FROM v_member_terbaik;

-- Top 5 member terbaik
SELECT * FROM v_member_terbaik LIMIT 5;


-- ----------------------------------------------------------------
--  VIEW 6 : v_stok_bahan_menipis
-- ----------------------------------------------------------------


CREATE OR REPLACE VIEW v_stok_bahan_menipis AS
SELECT
    b.id_bahan,
    b.nama_bahan,
    s.singkatan       AS satuan,
    b.stok,
    b.stok_minimum,
    b.harga_beli,
    sp.nama_supplier
FROM bahan_baku b
JOIN satuan        s  ON s.id_satuan    = b.id_satuan
LEFT JOIN supplier sp ON sp.id_supplier = b.id_supplier
WHERE b.stok <= b.stok_minimum
ORDER BY b.stok ASC;

-- Lihat semua bahan baku yang stoknya menipis
SELECT * FROM v_stok_bahan_menipis;


-- ----------------------------------------------------------------
--  VIEW 7 : v_karyawan_aktif
-- ----------------------------------------------------------------


CREATE OR REPLACE VIEW v_karyawan_aktif AS
SELECT
    k.id_karyawan,
    k.nama_karyawan,
    j.nama_jabatan,
    j.gaji_pokok,
    k.email,
    k.telepon,
    k.tanggal_masuk,
    k.nim_password
FROM karyawan k
JOIN jabatan j ON j.id_jabatan = k.id_jabatan
WHERE k.status_aktif = TRUE
ORDER BY j.id_jabatan, k.nama_karyawan;

-- Lihat semua karyawan aktif
SELECT * FROM v_karyawan_aktif;


-- ================================================================
--  BAGIAN 5 — INDEX
-- ================================================================

CREATE INDEX idx_pesanan_waktu      ON pesanan(waktu_pesan DESC);
CREATE INDEX idx_pesanan_status     ON pesanan(status);
CREATE INDEX idx_pesanan_meja       ON pesanan(nomor_meja);
CREATE INDEX idx_pesanan_pemesan    ON pesanan(nama_pemesan);
CREATE INDEX idx_pesanan_pelanggan  ON pesanan(id_pelanggan);
CREATE INDEX idx_detail_pesanan     ON detail_pesanan(id_pesanan);
CREATE INDEX idx_detail_menu        ON detail_pesanan(id_menu);
CREATE INDEX idx_menu_kategori      ON menu(id_kategori);
CREATE INDEX idx_menu_tersedia      ON menu(tersedia);
CREATE INDEX idx_log_waktu          ON log_aktivitas(waktu DESC);
CREATE INDEX idx_pelanggan_telepon  ON pelanggan(telepon);
CREATE INDEX idx_reservasi_waktu    ON reservasi(waktu_reservasi);


-- ================================================================
--  BAGIAN 6 — TRIGGER
-- ================================================================


-- ----------------------------------------------------------------
--  TRIGGER 1 : trg_log_pesanan
-- ----------------------------------------------------------------


CREATE OR REPLACE FUNCTION fn_auto_log_pesanan()
RETURNS TRIGGER AS $$
BEGIN
    INSERT INTO log_aktivitas
        (id_karyawan, aksi, tabel_terkait, id_referensi, keterangan)
    VALUES
        (NEW.id_karyawan,
         'INSERT pesanan baru',
         'pesanan',
         NEW.id_pesanan,
         'Pesanan dari: ' || NEW.nama_pemesan
            || ' | Meja: ' || NEW.nomor_meja
            || ' | Total: Rp ' || NEW.total_akhir::TEXT);
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_log_pesanan
AFTER INSERT ON pesanan
FOR EACH ROW EXECUTE FUNCTION fn_auto_log_pesanan();


-- ----------------------------------------------------------------
--  TRIGGER 2 : trg_log_menu
-- ----------------------------------------------------------------


CREATE OR REPLACE FUNCTION fn_log_update_menu()
RETURNS TRIGGER AS $$
BEGIN
    INSERT INTO log_aktivitas
        (id_karyawan, aksi, tabel_terkait, id_referensi, keterangan)
    VALUES
        (NULL,
         'UPDATE stok menu',
         'menu',
         NEW.id_menu,
         'Menu: ' || NEW.nama_menu
            || ' | Stok: ' || OLD.stok::TEXT
            || ' -> '      || NEW.stok::TEXT);
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_log_menu
AFTER UPDATE OF stok ON menu
FOR EACH ROW EXECUTE FUNCTION fn_log_update_menu();



-- Total pendapatan keseluruhan semua waktu
SELECT
    COUNT(DISTINCT id_pesanan) AS total_transaksi,
    SUM(total_akhir)           AS total_pendapatan,
    SUM(diskon)                AS total_diskon_member,
    AVG(total_akhir)           AS rata_rata_per_transaksi
FROM pesanan
WHERE status = 'selesai';

-- Pendapatan 7 hari terakhir dengan grafik batang sederhana
SELECT
    DATE(waktu_pesan)                AS tanggal,
    COUNT(*)                         AS jumlah_transaksi,
    SUM(total_akhir)                 AS pendapatan,
    REPEAT('█', COUNT(*)::INT)       AS grafik_batang
FROM pesanan
WHERE status = 'selesai'
  AND waktu_pesan >= CURRENT_DATE - INTERVAL '7 days'
GROUP BY DATE(waktu_pesan)
ORDER BY tanggal DESC;

-- Jam tersibuk: puncak kunjungan pelanggan
SELECT
    EXTRACT(HOUR FROM waktu_pesan)::INT AS jam,
    COUNT(*)                            AS jumlah_pesanan
FROM pesanan
WHERE status = 'selesai'
GROUP BY jam
ORDER BY jumlah_pesanan DESC;

-- Meja dengan total pendapatan terbesar
SELECT
    p.nomor_meja,
    COUNT(p.id_pesanan) AS total_kunjungan,
    SUM(p.total_akhir)  AS total_pendapatan
FROM pesanan p
WHERE p.status = 'selesai'
GROUP BY p.nomor_meja
ORDER BY total_pendapatan DESC;

-- Perbandingan member vs non-member: transaksi & pendapatan
SELECT
    CASE WHEN id_pelanggan IS NOT NULL
         THEN '★ Member' ELSE 'Non-Member' END AS tipe_pelanggan,
    COUNT(*)         AS jumlah_transaksi,
    SUM(total_akhir) AS total_pendapatan,
    AVG(total_akhir) AS rata_rata_belanja
FROM pesanan
WHERE status = 'selesai'
GROUP BY tipe_pelanggan;


-- ================================================================
--  BAGIAN 7 — VERIFIKASI AKHIR
-- ================================================================

-- Daftar semua tabel yang berhasil dibuat
SELECT
    table_name          AS "Nama Tabel",
    'Berhasil dibuat ✔' AS "Status"
FROM information_schema.tables
WHERE table_schema = 'public'
  AND table_type   = 'BASE TABLE'
ORDER BY table_name;

-- Rekap jumlah data per tabel inti
SELECT 'jabatan'         AS tabel, COUNT(*) AS jumlah FROM jabatan
UNION ALL
SELECT 'karyawan',                 COUNT(*)            FROM karyawan
UNION ALL
SELECT 'shift',                    COUNT(*)            FROM shift
UNION ALL
SELECT 'meja',                     COUNT(*)            FROM meja
UNION ALL
SELECT 'pelanggan',                COUNT(*)            FROM pelanggan
UNION ALL
SELECT 'kategori',                 COUNT(*)            FROM kategori
UNION ALL
SELECT 'supplier',                 COUNT(*)            FROM supplier
UNION ALL
SELECT 'satuan',                   COUNT(*)            FROM satuan
UNION ALL
SELECT 'bahan_baku',               COUNT(*)            FROM bahan_baku
UNION ALL
SELECT 'menu',                     COUNT(*)            FROM menu
UNION ALL
SELECT 'resep',                    COUNT(*)            FROM resep
UNION ALL
SELECT 'metode_bayar',             COUNT(*)            FROM metode_bayar
UNION ALL
SELECT 'pesanan',                  COUNT(*)            FROM pesanan
UNION ALL
SELECT 'detail_pesanan',           COUNT(*)            FROM detail_pesanan
UNION ALL
SELECT 'pembayaran',               COUNT(*)            FROM pembayaran
UNION ALL
SELECT 'reservasi',                COUNT(*)            FROM reservasi
UNION ALL
SELECT 'log_aktivitas',            COUNT(*)            FROM log_aktivitas
ORDER BY tabel;



