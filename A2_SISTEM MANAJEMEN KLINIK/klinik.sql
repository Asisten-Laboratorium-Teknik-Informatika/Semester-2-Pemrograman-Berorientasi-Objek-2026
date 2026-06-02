BEGIN;

DROP VIEW IF EXISTS
    v_pendaftaran_lengkap,
    v_pemeriksaan_lengkap,
    v_detail_tindakan_lengkap,
    v_detail_resep_lengkap,
    v_tagihan_pendaftaran,
    v_laporan_kunjungan_poli
CASCADE;

DROP FUNCTION IF EXISTS fn_total_tindakan(integer);
DROP FUNCTION IF EXISTS fn_total_obat_resep(integer);
DROP FUNCTION IF EXISTS fn_total_tagihan_pendaftaran(integer);
DROP FUNCTION IF EXISTS fn_set_subtotal_detail_tindakan();

DROP PROCEDURE IF EXISTS sp_buat_pembayaran(integer, varchar);

DROP TABLE IF EXISTS
    pembayaran,
    rawat_inap,
    rujukan,
    lab_test,
    detail_resep,
    resep,
    detail_tindakan,
    pemeriksaan,
    pendaftaran,
    dokter,
    obat,
    tindakan,
    pasien,
    pegawai,
    poli,
    role
CASCADE;

CREATE TABLE role (
    id_role INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nama_role VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE poli (
    id_poli INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nama_poli VARCHAR(100) NOT NULL UNIQUE,
    lokasi_poli VARCHAR(100) NOT NULL
);

CREATE TABLE pasien (
    id_pasien INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nama VARCHAR(100) NOT NULL,
    jenis_kelamin CHAR(1) NOT NULL CHECK (jenis_kelamin IN ('L', 'P')),
    tanggal_lahir DATE NOT NULL,
    alamat VARCHAR(255),
    no_hp VARCHAR(20),
    golongan_darah VARCHAR(3)
        CHECK (golongan_darah IN ('A', 'B', 'AB', 'O', 'A+', 'A-', 'B+', 'B-', 'AB+', 'AB-', 'O+', 'O-'))
);

CREATE TABLE pegawai (
    id_pegawai INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nama_pegawai VARCHAR(100) NOT NULL,
    alamat VARCHAR(255),
    no_hp VARCHAR(20),
    email VARCHAR(100) NOT NULL UNIQUE,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    id_role INT NOT NULL,
    CONSTRAINT fk_pegawai_role
        FOREIGN KEY (id_role)
        REFERENCES role(id_role)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
);

CREATE TABLE dokter (
    id_dokter INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nama_dokter VARCHAR(100) NOT NULL,
    spesialis VARCHAR(100) NOT NULL,
    no_hp VARCHAR(20),
    id_poli INT NOT NULL,
    CONSTRAINT fk_dokter_poli
        FOREIGN KEY (id_poli)
        REFERENCES poli(id_poli)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
);

CREATE TABLE tindakan (
    id_tindakan INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nama_tindakan VARCHAR(100) NOT NULL UNIQUE,
    biaya_tindakan NUMERIC(12,2) NOT NULL CHECK (biaya_tindakan >= 0)
);

CREATE TABLE obat (
    id_obat INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nama_obat VARCHAR(100) NOT NULL UNIQUE,
    jenis_obat VARCHAR(50) NOT NULL,
    harga_obat NUMERIC(12,2) NOT NULL CHECK (harga_obat >= 0),
    stok INT NOT NULL DEFAULT 0 CHECK (stok >= 0)
);

CREATE TABLE pendaftaran (
    id_pendaftaran INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    id_pasien INT NOT NULL,
    id_poli INT NOT NULL,
    tanggal_pendaftaran DATE NOT NULL DEFAULT CURRENT_DATE,
    CONSTRAINT fk_pendaftaran_pasien
        FOREIGN KEY (id_pasien)
        REFERENCES pasien(id_pasien)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,
    CONSTRAINT fk_pendaftaran_poli
        FOREIGN KEY (id_poli)
        REFERENCES poli(id_poli)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
);

CREATE TABLE pemeriksaan (
    id_pemeriksaan INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    id_pendaftaran INT NOT NULL,
    id_dokter INT NOT NULL,
    tanggal_pemeriksaan DATE NOT NULL DEFAULT CURRENT_DATE,
    berat_badan NUMERIC(5,2),
    tekanan_darah VARCHAR(20),
    diagnosa TEXT,
    CONSTRAINT fk_pemeriksaan_pendaftaran
        FOREIGN KEY (id_pendaftaran)
        REFERENCES pendaftaran(id_pendaftaran)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,
    CONSTRAINT fk_pemeriksaan_dokter
        FOREIGN KEY (id_dokter)
        REFERENCES dokter(id_dokter)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
);

CREATE TABLE resep (
    id_resep INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    id_pemeriksaan INT NOT NULL,
    tanggal_resep DATE NOT NULL DEFAULT CURRENT_DATE,
    CONSTRAINT fk_resep_pemeriksaan
        FOREIGN KEY (id_pemeriksaan)
        REFERENCES pemeriksaan(id_pemeriksaan)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

CREATE TABLE detail_resep (
    id_detail_resep INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    id_resep INT NOT NULL,
    id_obat INT NOT NULL,
    jumlah_obat INT NOT NULL CHECK (jumlah_obat > 0),
    dosis VARCHAR(100) NOT NULL,
    CONSTRAINT fk_detail_resep_resep
        FOREIGN KEY (id_resep)
        REFERENCES resep(id_resep)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    CONSTRAINT fk_detail_resep_obat
        FOREIGN KEY (id_obat)
        REFERENCES obat(id_obat)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,
    CONSTRAINT uq_detail_resep UNIQUE (id_resep, id_obat)
);

CREATE TABLE detail_tindakan (
    id_detail_tindakan INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    id_pemeriksaan INT NOT NULL,
    id_tindakan INT NOT NULL,
    jumlah INT NOT NULL CHECK (jumlah > 0),
    sub_total NUMERIC(12,2) NOT NULL DEFAULT 0 CHECK (sub_total >= 0),
    CONSTRAINT fk_detail_tindakan_pemeriksaan
        FOREIGN KEY (id_pemeriksaan)
        REFERENCES pemeriksaan(id_pemeriksaan)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    CONSTRAINT fk_detail_tindakan_tindakan
        FOREIGN KEY (id_tindakan)
        REFERENCES tindakan(id_tindakan)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,
    CONSTRAINT uq_detail_tindakan UNIQUE (id_pemeriksaan, id_tindakan)
);

CREATE TABLE lab_test (
    id_lab_test INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    id_pemeriksaan INT NOT NULL,
    jenis_test VARCHAR(100) NOT NULL,
    hasil_test TEXT,
    tanggal_test DATE NOT NULL DEFAULT CURRENT_DATE,
    CONSTRAINT fk_labtest_pemeriksaan
        FOREIGN KEY (id_pemeriksaan)
        REFERENCES pemeriksaan(id_pemeriksaan)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

CREATE TABLE rawat_inap (
    id_rawat_inap INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    id_pendaftaran INT NOT NULL,
    nomor_kamar VARCHAR(50) NOT NULL,
    tanggal_masuk DATE NOT NULL,
    tanggal_keluar DATE,
    CONSTRAINT fk_rawatinap_pendaftaran
        FOREIGN KEY (id_pendaftaran)
        REFERENCES pendaftaran(id_pendaftaran)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,
    CONSTRAINT ck_rawatinap_tanggal
        CHECK (tanggal_keluar IS NULL OR tanggal_keluar >= tanggal_masuk)
);

CREATE TABLE rujukan (
    id_rujukan INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    id_pasien INT NOT NULL,
    alasan_rujukan TEXT NOT NULL,
    tujuan_rujukan VARCHAR(150) NOT NULL,
    tanggal_rujukan DATE NOT NULL DEFAULT CURRENT_DATE,
    CONSTRAINT fk_rujukan_pasien
        FOREIGN KEY (id_pasien)
        REFERENCES pasien(id_pasien)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
);

CREATE TABLE pembayaran (
    id_pembayaran INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    id_pendaftaran INT NOT NULL UNIQUE,
    total_bayar NUMERIC(12,2) NOT NULL DEFAULT 0 CHECK (total_bayar >= 0),
    tanggal_pembayaran DATE NOT NULL DEFAULT CURRENT_DATE,
    metode_pembayaran VARCHAR(20) NOT NULL DEFAULT 'tunai'
        CHECK (metode_pembayaran IN ('tunai', 'transfer', 'debit', 'qris')),
    status_pembayaran VARCHAR(20) NOT NULL DEFAULT 'belum_bayar'
        CHECK (status_pembayaran IN ('belum_bayar', 'lunas', 'batal')),
    CONSTRAINT fk_pembayaran_pendaftaran
        FOREIGN KEY (id_pendaftaran)
        REFERENCES pendaftaran(id_pendaftaran)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
);

CREATE INDEX idx_pegawai_id_role ON pegawai(id_role);
CREATE INDEX idx_dokter_id_poli ON dokter(id_poli);
CREATE INDEX idx_pendaftaran_id_pasien ON pendaftaran(id_pasien);
CREATE INDEX idx_pendaftaran_id_poli ON pendaftaran(id_poli);
CREATE INDEX idx_pemeriksaan_id_pendaftaran ON pemeriksaan(id_pendaftaran);
CREATE INDEX idx_pemeriksaan_id_dokter ON pemeriksaan(id_dokter);
CREATE INDEX idx_resep_id_pemeriksaan ON resep(id_pemeriksaan);
CREATE INDEX idx_detail_resep_id_resep ON detail_resep(id_resep);
CREATE INDEX idx_detail_resep_id_obat ON detail_resep(id_obat);
CREATE INDEX idx_detail_tindakan_id_pemeriksaan ON detail_tindakan(id_pemeriksaan);
CREATE INDEX idx_detail_tindakan_id_tindakan ON detail_tindakan(id_tindakan);
CREATE INDEX idx_lab_test_id_pemeriksaan ON lab_test(id_pemeriksaan);
CREATE INDEX idx_rawat_inap_id_pendaftaran ON rawat_inap(id_pendaftaran);
CREATE INDEX idx_rujukan_id_pasien ON rujukan(id_pasien);
CREATE INDEX idx_pembayaran_id_pendaftaran ON pembayaran(id_pendaftaran);

CREATE OR REPLACE FUNCTION fn_set_subtotal_detail_tindakan()
RETURNS TRIGGER
LANGUAGE plpgsql
AS $$
DECLARE
    v_biaya NUMERIC(12,2);
BEGIN
    SELECT COALESCE(biaya_tindakan, 0)
    INTO v_biaya
    FROM tindakan
    WHERE id_tindakan = NEW.id_tindakan;

    NEW.sub_total := COALESCE(NEW.jumlah, 1) * COALESCE(v_biaya, 0);
    RETURN NEW;
END;
$$;

CREATE TRIGGER trg_detail_tindakan_subtotal
BEFORE INSERT OR UPDATE OF id_tindakan, jumlah
ON detail_tindakan
FOR EACH ROW
EXECUTE FUNCTION fn_set_subtotal_detail_tindakan();

CREATE OR REPLACE FUNCTION fn_total_tindakan(p_id_pemeriksaan INT)
RETURNS NUMERIC(12,2)
LANGUAGE sql
AS $$
    SELECT COALESCE(SUM(sub_total), 0)
    FROM detail_tindakan
    WHERE id_pemeriksaan = $1;
$$;

CREATE OR REPLACE FUNCTION fn_total_obat_resep(p_id_resep INT)
RETURNS NUMERIC(12,2)
LANGUAGE sql
AS $$
    SELECT COALESCE(SUM(dr.jumlah_obat * o.harga_obat), 0)
    FROM detail_resep dr
    JOIN obat o ON o.id_obat = dr.id_obat
    WHERE dr.id_resep = $1;
$$;

CREATE OR REPLACE FUNCTION fn_total_tagihan_pendaftaran(p_id_pendaftaran INT)
RETURNS NUMERIC(12,2)
LANGUAGE sql
AS $$
    SELECT
        COALESCE((
            SELECT SUM(dt.sub_total)
            FROM pemeriksaan pe
            JOIN detail_tindakan dt ON dt.id_pemeriksaan = pe.id_pemeriksaan
            WHERE pe.id_pendaftaran = $1
        ), 0)
        +
        COALESCE((
            SELECT SUM(dr.jumlah_obat * o.harga_obat)
            FROM pemeriksaan pe
            JOIN resep r ON r.id_pemeriksaan = pe.id_pemeriksaan
            JOIN detail_resep dr ON dr.id_resep = r.id_resep
            JOIN obat o ON o.id_obat = dr.id_obat
            WHERE pe.id_pendaftaran = $1
        ), 0);
$$;

CREATE OR REPLACE PROCEDURE sp_buat_pembayaran(
    IN p_id_pendaftaran INT,
    IN p_metode_pembayaran VARCHAR(20) DEFAULT 'tunai'
)
LANGUAGE plpgsql
AS $$
DECLARE
    v_total NUMERIC(12,2);
BEGIN
    v_total := fn_total_tagihan_pendaftaran(p_id_pendaftaran);

    INSERT INTO pembayaran (
        id_pendaftaran,
        total_bayar,
        tanggal_pembayaran,
        metode_pembayaran,
        status_pembayaran
    )
    VALUES (
        p_id_pendaftaran,
        v_total,
        CURRENT_DATE,
        p_metode_pembayaran,
        CASE
            WHEN v_total > 0 THEN 'lunas'
            ELSE 'belum_bayar'
        END
    )
    ON CONFLICT (id_pendaftaran)
    DO UPDATE SET
        total_bayar = EXCLUDED.total_bayar,
        tanggal_pembayaran = EXCLUDED.tanggal_pembayaran,
        metode_pembayaran = EXCLUDED.metode_pembayaran,
        status_pembayaran = EXCLUDED.status_pembayaran;
END;
$$;

CREATE OR REPLACE VIEW v_pendaftaran_lengkap AS
SELECT
    pendaftaran.id_pendaftaran,
    pendaftaran.tanggal_pendaftaran,
    pasien.id_pasien,
    pasien.nama AS nama_pasien,
    poli.id_poli,
    poli.nama_poli
FROM pendaftaran
JOIN pasien ON pasien.id_pasien = pendaftaran.id_pasien
JOIN poli ON poli.id_poli = pendaftaran.id_poli;

CREATE OR REPLACE VIEW v_pemeriksaan_lengkap AS
SELECT
    pemeriksaan.id_pemeriksaan,
    pemeriksaan.tanggal_pemeriksaan,
    pendaftaran.id_pendaftaran,
    pasien.nama AS nama_pasien,
    dokter.nama_dokter,
    poli.nama_poli,
    pemeriksaan.berat_badan,
    pemeriksaan.tekanan_darah,
    pemeriksaan.diagnosa
FROM pemeriksaan
JOIN pendaftaran ON pendaftaran.id_pendaftaran = pemeriksaan.id_pendaftaran
JOIN pasien ON pasien.id_pasien = pendaftaran.id_pasien
JOIN dokter ON dokter.id_dokter = pemeriksaan.id_dokter
JOIN poli ON poli.id_poli = pendaftaran.id_poli;

CREATE OR REPLACE VIEW v_detail_tindakan_lengkap AS
SELECT
    dt.id_detail_tindakan,
    dt.id_pemeriksaan,
    t.nama_tindakan,
    dt.jumlah,
    t.biaya_tindakan,
    dt.sub_total
FROM detail_tindakan dt
JOIN tindakan t ON t.id_tindakan = dt.id_tindakan;

CREATE OR REPLACE VIEW v_detail_resep_lengkap AS
SELECT
    dr.id_detail_resep,
    r.id_resep,
    o.nama_obat,
    o.jenis_obat,
    dr.jumlah_obat,
    dr.dosis,
    o.harga_obat,
    (dr.jumlah_obat * o.harga_obat) AS total_obat
FROM detail_resep dr
JOIN resep r ON r.id_resep = dr.id_resep
JOIN obat o ON o.id_obat = dr.id_obat;

CREATE OR REPLACE VIEW v_tagihan_pendaftaran AS
SELECT
    pendaftaran.id_pendaftaran,
    pasien.nama AS nama_pasien,
    poli.nama_poli,
    pendaftaran.tanggal_pendaftaran,
    COALESCE((
        SELECT SUM(dt.sub_total)
        FROM pemeriksaan pe
        JOIN detail_tindakan dt ON dt.id_pemeriksaan = pe.id_pemeriksaan
        WHERE pe.id_pendaftaran = pendaftaran.id_pendaftaran
    ), 0) AS total_tindakan,
    COALESCE((
        SELECT SUM(dr.jumlah_obat * o.harga_obat)
        FROM pemeriksaan pe
        JOIN resep r ON r.id_pemeriksaan = pe.id_pemeriksaan
        JOIN detail_resep dr ON dr.id_resep = r.id_resep
        JOIN obat o ON o.id_obat = dr.id_obat
        WHERE pe.id_pendaftaran = pendaftaran.id_pendaftaran
    ), 0) AS total_obat,
    COALESCE(pembayaran.total_bayar, 0) AS total_bayar,
    COALESCE(pembayaran.status_pembayaran, 'belum_bayar') AS status_pembayaran
FROM pendaftaran
JOIN pasien ON pasien.id_pasien = pendaftaran.id_pasien
JOIN poli ON poli.id_poli = pendaftaran.id_poli
LEFT JOIN pembayaran ON pembayaran.id_pendaftaran = pendaftaran.id_pendaftaran;

CREATE OR REPLACE VIEW v_laporan_kunjungan_poli AS
SELECT
    poli.id_poli,
    poli.nama_poli,
    COUNT(pendaftaran.id_pendaftaran) AS total_kunjungan
FROM poli
LEFT JOIN pendaftaran ON pendaftaran.id_poli = poli.id_poli
GROUP BY poli.id_poli, poli.nama_poli;

INSERT INTO role (nama_role) VALUES
('Admin'),
('Pendaftaran'),
('Dokter'),
('Kasir'),
('Apoteker'),
('Lab');

INSERT INTO poli (nama_poli, lokasi_poli) VALUES
('Poli Umum', 'Lantai 1'),
('Poli Anak', 'Lantai 2'),
('Poli Gigi', 'Lantai 2'),
('Poli Penyakit Dalam', 'Lantai 3');

INSERT INTO pasien (nama, jenis_kelamin, tanggal_lahir, alamat, no_hp, golongan_darah) VALUES
('Alya Putri', 'P', '2001-05-10', 'Jl. Sukamaju No. 11, Bandung', '081111111101', 'O'),
('Muhammad Rizky', 'L', '1998-08-22', 'Jl. Cibadak No. 5, Bandung', '081111111102', 'A'),
('Nadia Safitri', 'P', '1987-12-01', 'Jl. Sumbersari No. 7, Bandung', '081111111103', 'B'),
('Yoga Pratama', 'L', '2005-03-17', 'Jl. Veteran No. 19, Bandung', '081111111104', 'AB'),
('Salsa Amelia', 'P', '2010-09-25', 'Jl. Garuda No. 2, Bandung', '081111111105', 'A+'),
('Taufik Hidayat', 'L', '1979-01-30', 'Jl. Asia Afrika No. 45, Bandung', '081111111106', 'O+');

INSERT INTO pegawai (nama_pegawai, alamat, no_hp, email, username, password, id_role) VALUES
('Andi Pratama', 'Jl. Merdeka No. 1, Bandung', '081234567801', 'andi@klinik.com', 'andi.admin', 'admin123', 1),
('Siti Rahma', 'Jl. Melati No. 12, Bandung', '081234567802', 'siti@klinik.com', 'siti.daftar', 'daftar123', 2),
('Budi Santoso', 'Jl. Kenanga No. 8, Bandung', '081234567803', 'budi@klinik.com', 'budi.dokter', 'dokter123', 3),
('Rina Wulandari', 'Jl. Cempaka No. 4, Bandung', '081234567804', 'rina@klinik.com', 'rina.kasir', 'kasir123', 4),
('Dewi Lestari', 'Jl. Anggrek No. 9, Bandung', '081234567805', 'dewi@klinik.com', 'dewi.apoteker', 'apoteker123', 5),
('Fajar Hidayat', 'Jl. Teratai No. 15, Bandung', '081234567806', 'fajar@klinik.com', 'fajar.lab', 'lab123', 6);

INSERT INTO dokter (nama_dokter, spesialis, no_hp, id_poli) VALUES
('dr. Budi Santoso', 'Dokter Umum', '081234567803', 1),
('dr. Rudi Hartono', 'Penyakit Dalam', '081234567808', 4),
('dr. Nina Aisyah', 'Dokter Anak', '081234567807', 2);

INSERT INTO tindakan (nama_tindakan, biaya_tindakan) VALUES
('Pemeriksaan Umum', 50000),
('Cek Tensi', 15000),
('Suntik Vitamin', 40000),
('Cabut Gigi', 200000);

INSERT INTO obat (nama_obat, jenis_obat, harga_obat, stok) VALUES
('Paracetamol 500mg', 'Tablet', 1500, 200),
('Amoxicillin 500mg', 'Kapsul', 2500, 120),
('Vitamin C 1000mg', 'Tablet', 2000, 180),
('Ibuprofen 400mg', 'Tablet', 3000, 90);

INSERT INTO pendaftaran (id_pasien, id_poli, tanggal_pendaftaran) VALUES
(1, 1, '2026-05-10'),
(2, 4, '2026-05-10'),
(3, 2, '2026-05-10'),
(4, 3, '2026-05-11');

INSERT INTO pemeriksaan (id_pendaftaran, id_dokter, tanggal_pemeriksaan, berat_badan, tekanan_darah, diagnosa) VALUES
(1, 1, '2026-05-10', 54.50, '120/80', 'Demam ringan'),
(4, 1, '2026-05-11', 48.20, '118/78', 'Gigi berlubang'),
(2, 2, '2026-05-10', 60.00, '115/75', 'Gastritis');

INSERT INTO detail_tindakan (id_pemeriksaan, id_tindakan, jumlah, sub_total) VALUES
(1, 1, 1, 0),
(1, 2, 1, 0),
(2, 4, 1, 0),
(3, 1, 1, 0);

INSERT INTO resep (id_pemeriksaan, tanggal_resep) VALUES
(1, '2026-05-10'),
(3, '2026-05-10');

INSERT INTO detail_resep (id_resep, id_obat, jumlah_obat, dosis) VALUES
(1, 1, 10, '3x1 sehari'),
(1, 3, 5, '1x1 sehari'),
(2, 2, 14, '3x1 sehari');

INSERT INTO lab_test (id_pemeriksaan, jenis_test, hasil_test, tanggal_test) VALUES
(3, 'Tes Darah', 'Normal', '2026-05-10'),
(1, 'Tes Flu', 'Negatif', '2026-05-10');

INSERT INTO rawat_inap (id_pendaftaran, nomor_kamar, tanggal_masuk, tanggal_keluar) VALUES
(2, 'A101', '2026-05-10', '2026-05-12'),
(4, 'B202', '2026-05-12', NULL);

INSERT INTO rujukan (id_pasien, alasan_rujukan, tujuan_rujukan, tanggal_rujukan) VALUES
(3, 'Perlu pemeriksaan lanjutan', 'RSUD Bandung', '2026-05-10'),
(4, 'Perawatan gigi lanjutan', 'RS Gigi Sehat', '2026-05-11');

CALL sp_buat_pembayaran(1, 'tunai');
CALL sp_buat_pembayaran(2, 'qris');
CALL sp_buat_pembayaran(4, 'transfer');

SELECT id_pasien, nama, jenis_kelamin
FROM pasien
WHERE jenis_kelamin = 'P';

SELECT
    pendaftaran.id_pendaftaran,
    pasien.nama AS nama_pasien,
    poli.nama_poli,
    pendaftaran.tanggal_pendaftaran
FROM pendaftaran
INNER JOIN pasien ON pasien.id_pasien = pendaftaran.id_pasien
INNER JOIN poli ON poli.id_poli = pendaftaran.id_poli
ORDER BY pendaftaran.tanggal_pendaftaran, pendaftaran.id_pendaftaran;

SELECT
    dokter.id_dokter,
    dokter.nama_dokter,
    COUNT(pemeriksaan.id_pemeriksaan) AS jumlah_pemeriksaan
FROM dokter
LEFT JOIN pemeriksaan ON pemeriksaan.id_dokter = dokter.id_dokter
GROUP BY dokter.id_dokter, dokter.nama_dokter
HAVING COUNT(pemeriksaan.id_pemeriksaan) >= 1
ORDER BY jumlah_pemeriksaan DESC;

SELECT
    nama,
    tanggal_lahir
FROM pasien
WHERE id_pasien IN (
    SELECT id_pasien
    FROM pendaftaran
);

WITH kunjungan_poli AS (
    SELECT
        id_poli,
        COUNT(*) AS total_kunjungan
    FROM pendaftaran
    GROUP BY id_poli
)
SELECT
    poli.nama_poli,
    kunjungan_poli.total_kunjungan
FROM kunjungan_poli
JOIN poli ON poli.id_poli = kunjungan_poli.id_poli
ORDER BY kunjungan_poli.total_kunjungan DESC;

COMMIT;