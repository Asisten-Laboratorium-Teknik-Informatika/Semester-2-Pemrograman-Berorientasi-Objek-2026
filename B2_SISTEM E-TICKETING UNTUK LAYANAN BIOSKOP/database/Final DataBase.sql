-- ==============================================================================
-- 1. HAPUS SEMUA TABEL LAMA (Mencegah Bentrok / Error Column Not Exist)
-- ==============================================================================
DROP TABLE IF EXISTS shift_karyawan CASCADE;
DROP TABLE IF EXISTS karyawan CASCADE;
DROP TABLE IF EXISTS jadwal_maintenance CASCADE;
DROP TABLE IF EXISTS promo_diskon CASCADE;
DROP TABLE IF EXISTS pembelian_merchandise CASCADE;
DROP TABLE IF EXISTS merchandise CASCADE;
DROP TABLE IF EXISTS sponsor CASCADE;
DROP TABLE IF EXISTS review_film CASCADE;
DROP TABLE IF EXISTS pembelian_snack CASCADE;
DROP TABLE IF EXISTS tiket_bioskop CASCADE;
DROP TABLE IF EXISTS film CASCADE;
DROP TABLE IF EXISTS studio CASCADE;
DROP TABLE IF EXISTS kategori_film CASCADE;
DROP TABLE IF EXISTS admin_bioskop CASCADE;
DROP TABLE IF EXISTS users CASCADE;

-- ==============================================================================
-- 2. TABEL USERS & ADMIN (Dengan Sistem POIN)
-- ==============================================================================
CREATE TABLE users (
    id_user SERIAL PRIMARY KEY,
    username VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    role VARCHAR(20) DEFAULT 'user',
    poin INT DEFAULT 0
);
select * from users;

CREATE TABLE admin_bioskop (
    id_admin SERIAL PRIMARY KEY,
    username VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL
);
select * from admin_bioskop;
-- ==============================================================================
-- 3. TABEL KATEGORI, STUDIO & FILM
-- ==============================================================================
CREATE TABLE kategori_film (
    id_kategori SERIAL PRIMARY KEY,
    nama_kategori VARCHAR(100)
);

CREATE TABLE studio (
    id_studio SERIAL PRIMARY KEY,
    nama_studio VARCHAR(50),
    kapasitas INTEGER
);

CREATE TABLE film (
    id_film SERIAL PRIMARY KEY,
    judul VARCHAR(100),
    genre VARCHAR(100),
    durasi INTEGER,
    rating_usia VARCHAR(20),
    harga NUMERIC(12,2),
    id_kategori INTEGER REFERENCES kategori_film(id_kategori),
    is_tayang BOOLEAN DEFAULT true,
    is_imax BOOLEAN DEFAULT false
);

-- ==============================================================================
-- 4. TABEL TRANSAKSI UTAMA (TIKET & SNACK)
-- ==============================================================================
CREATE TABLE tiket_bioskop (
    id SERIAL PRIMARY KEY,
    user_id INTEGER REFERENCES users(id_user) ON DELETE CASCADE,
    film_id INTEGER REFERENCES film(id_film) ON DELETE CASCADE,
    studio_id INTEGER REFERENCES studio(id_studio) ON DELETE CASCADE,
    kursi VARCHAR(10),
    pembayaran VARCHAR(50),
    harga INTEGER,
    jam_tayang VARCHAR(20) DEFAULT '10:00'
);

CREATE TABLE pembelian_snack (
    id SERIAL PRIMARY KEY,
    user_id INTEGER REFERENCES users(id_user) ON DELETE CASCADE,
    nama_snack VARCHAR(100),
    jumlah INTEGER,
    total_harga INTEGER
);

-- ==============================================================================
-- 5. TABEL FITUR TAMBAHAN (REVIEW, SPONSOR, MERCHANDISE, PROMO, MAINTENANCE)
-- ==============================================================================
CREATE TABLE review_film (
    id_review SERIAL PRIMARY KEY,
    id_user INTEGER REFERENCES users(id_user) ON DELETE CASCADE,
    id_film INTEGER REFERENCES film(id_film) ON DELETE CASCADE,
    rating INTEGER CHECK (rating >= 1 AND rating <= 5),
    komentar TEXT,
    tanggal_review TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE sponsor (
    id_sponsor SERIAL PRIMARY KEY,
    nama_sponsor VARCHAR(100) NOT NULL,
    jenis_sponsor VARCHAR(50),
    nilai_kontrak NUMERIC(15,2),
    tanggal_mulai DATE,
    tanggal_selesai DATE
);

CREATE TABLE merchandise (
    id_merch SERIAL PRIMARY KEY,
    nama_merch VARCHAR(100) NOT NULL,
    kategori VARCHAR(50),
    harga INTEGER NOT NULL,
    stok INTEGER DEFAULT 0
);

CREATE TABLE pembelian_merchandise (
    id_pembelian SERIAL PRIMARY KEY,
    id_user INTEGER REFERENCES users(id_user) ON DELETE CASCADE,
    id_merch INTEGER REFERENCES merchandise(id_merch) ON DELETE CASCADE,
    jumlah INTEGER NOT NULL,
    total_harga INTEGER NOT NULL,
    tanggal_beli TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE promo_diskon (
    id_promo SERIAL PRIMARY KEY,
    kode_promo VARCHAR(20) UNIQUE NOT NULL,
    potongan_harga INTEGER NOT NULL,
    kuota INTEGER DEFAULT 0,
    tanggal_berakhir DATE
);

CREATE TABLE jadwal_maintenance (
    id_maintenance SERIAL PRIMARY KEY,
    id_studio INTEGER REFERENCES studio(id_studio) ON DELETE CASCADE,
    deskripsi_perbaikan TEXT,
    tanggal_maintenance DATE,
    status VARCHAR(20) DEFAULT 'Terjadwal'
);

-- ==============================================================================
-- 6. TABEL DATA PEGAWAI (KARYAWAN & SHIFT)
-- ==============================================================================
CREATE TABLE karyawan (
    id_karyawan SERIAL PRIMARY KEY,
    nama_karyawan VARCHAR(100) NOT NULL,
    posisi VARCHAR(50) NOT NULL,
    no_hp VARCHAR(15),
    tanggal_bergabung DATE DEFAULT CURRENT_DATE
);

CREATE TABLE shift_karyawan (
    id_shift SERIAL PRIMARY KEY,
    id_karyawan INTEGER REFERENCES karyawan(id_karyawan) ON DELETE CASCADE,
    hari VARCHAR(20) NOT NULL,
    jam_mulai TIME NOT NULL,
    jam_selesai TIME NOT NULL
);

-- ==============================================================================
-- 7. INSERT DATA AKUN ADMIN & USER DUMMY
-- ==============================================================================
INSERT INTO admin_bioskop (username, password) VALUES ('admin', 'admin123');

-- Akun User 'hamid' dengan modal 500 poin untuk test fitur tukar poin
INSERT INTO users (username, password, role, poin) VALUES ('hamid', 'hamid', 'user', 500);

-- ==============================================================================
-- 8. INSERT DATA KATEGORI FILM & STUDIO
-- ==============================================================================
INSERT INTO kategori_film (nama_kategori) VALUES
('Laga / Action'), ('Drama Romantis'), ('Horror'), ('Anime'), 
('Komedi'), ('Biografi & Sejarah'), ('Keluarga & Petualangan');

-- Masukkan 23 Studio Sesuai Standar Bioskop
INSERT INTO studio (nama_studio, kapasitas) VALUES
('Studio Regular 1', 100), ('Studio Regular 2', 100), ('Studio Regular 3', 100), 
('Studio Regular 4', 100), ('Studio Regular 5', 100), ('Studio Regular 6', 100), 
('Studio Regular 7', 100), ('Studio Regular 8', 100), ('Studio Regular 9', 100), 
('Studio Regular 10', 100),
('Studio Premiere 1', 120), ('Studio Premiere 2', 120), ('Studio Premiere 3', 120), 
('Studio Premiere 4', 120), ('Studio Premiere 5', 120), ('Studio Premiere 6', 120), 
('Studio Premiere 7', 120), ('Studio Premiere 8', 120), ('Studio Premiere 9', 120), 
('Studio Premiere 10', 120),
('Studio IMAX 1', 150), ('Studio IMAX 2', 150), ('Studio IMAX 3', 150);

-- ==============================================================================
-- 9. INSERT DAFTAR FILM TRAILER
-- ==============================================================================
INSERT INTO film (judul, genre, durasi, rating_usia, harga, id_kategori, is_tayang, is_imax) VALUES
('DILAN ITB 1997', 'Drama Romantis', 115, '13+', 50000, 2, true, false),
('Ghost in the Cell', 'Laga / Action', 120, '13+', 50000, 1, true, true),
('Michael', 'Biografi & Sejarah', 105, '13+', 50000, 6, true, false),
('Mortal Kombat II', 'Laga / Action', 130, '17+', 50000, 1, true, true),
('Project Hail Mary', 'Keluarga & Petualangan', 145, '13+', 50000, 7, true, true),
('SALMOKJI WHISPERING WATER', 'Horror', 110, '17+', 50000, 3, true, false),
('THE BELL  PANGGILAN UNTUK MATI', 'Horror', 95, '17+', 50000, 3, true, false);

-- ==============================================================================
-- 10. INSERT DATA TAMBAHAN LAINNYA
-- ==============================================================================
INSERT INTO sponsor (nama_sponsor, jenis_sponsor, nilai_kontrak) VALUES 
('Coca Cola', 'Minuman Utama', 50000000.00),
('Bank BCA', 'Partner Pembayaran', 75000000.00);

INSERT INTO merchandise (nama_merch, kategori, harga, stok) VALUES
('Tumbler Dilan 1997', 'Aksesoris', 75000, 50),
('Kaos Mortal Kombat', 'Pakaian', 120000, 30);

INSERT INTO promo_diskon (kode_promo, potongan_harga, kuota) VALUES
('NONTONHEMAT', 15000, 100),
('MTIXWEEKEND', 25000, 50);

INSERT INTO karyawan (nama_karyawan, posisi, no_hp) VALUES
('Budi Santoso', 'Kasir Tiket', '081234567890'),
('Siti Aminah', 'Staff Snack', '081298765432'),
('Agus Pratama', 'Security', '081345678901');

INSERT INTO shift_karyawan (id_karyawan, hari, jam_mulai, jam_selesai) VALUES
(1, 'Senin', '08:00', '16:00'),
(1, 'Selasa', '08:00', '16:00'),
(2, 'Senin', '10:00', '18:00'),
(3, 'Sabtu', '16:00', '23:59'),
(3, 'Minggu', '16:00', '23:59');

-- Reset sequences secara dinamis (opsional tapi disarankan agar ID increment tidak error)
SELECT setval('users_id_user_seq', (SELECT MAX(id_user) FROM users));
SELECT setval('admin_bioskop_id_admin_seq', (SELECT MAX(id_admin) FROM admin_bioskop));
SELECT setval('kategori_film_id_kategori_seq', (SELECT MAX(id_kategori) FROM kategori_film));
SELECT setval('studio_id_studio_seq', (SELECT MAX(id_studio) FROM studio));
SELECT setval('film_id_film_seq', (SELECT MAX(id_film) FROM film));
SELECT setval('sponsor_id_sponsor_seq', (SELECT MAX(id_sponsor) FROM sponsor));
SELECT setval('merchandise_id_merch_seq', (SELECT MAX(id_merch) FROM merchandise));
SELECT setval('promo_diskon_id_promo_seq', (SELECT MAX(id_promo) FROM promo_diskon));
SELECT setval('karyawan_id_karyawan_seq', (SELECT MAX(id_karyawan) FROM karyawan));

ALTER TABLE film ADD COLUMN urutan INT DEFAULT 0;