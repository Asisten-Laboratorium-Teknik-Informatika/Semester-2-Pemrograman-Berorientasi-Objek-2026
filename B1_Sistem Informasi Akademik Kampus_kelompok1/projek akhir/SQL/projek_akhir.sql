
CREATE SCHEMA b1;

-- PROGRAM STUDI
CREATE TYPE jenjang_enum
AS ENUM('D3', 'D4', 'S1', 'S2', 'S3');

CREATE TYPE akreditasi_enum 
AS ENUM('Unggul', 'Baik sekali', 'Baik', 'Buruk');

CREATE TABLE b1.program_studi (
	id_program_studi varchar (10) PRIMARY KEY,
	nama_program_studi varchar (40) NOT NULL,
	fakultas varchar (60) NOT NULL,
	jenjang jenjang_enum NOT NULL,
	akreditasi akreditasi_enum NOT NULL
);

SELECT * FROM b1.program_studi;

DROP TABLE b1.program_studi;

-- DOSEN
CREATE TYPE jenis_kelamin_dosen_enum
AS ENUM('Laki-Laki', 'Perempuan');

CREATE TABLE b1.dosen (
	nidn varchar (15) PRIMARY KEY,
	nama varchar (100) NOT NULL,
	email varchar (40) NOT NULL UNIQUE,
	jenis_kelamin jenis_kelamin_dosen_enum NOT NULL,
	no_hp varchar (15),
	id_program_studi varchar (100) NOT NULL,
	FOREIGN KEY (id_program_studi) REFERENCES b1.program_studi (id_program_studi)
);

SELECT * FROM b1.dosen;

DROP TABLE b1.dosen;

-- MAHASISWA
CREATE TYPE jenis_kelamin_mahasiswa_enum
AS ENUM('Laki-Laki', 'Perempuan');

CREATE TABLE b1.mahasiswa (
	nim varchar (20) PRIMARY KEY,
	nama varchar (100) NOT NULL,
	email varchar (50) NOT NULL UNIQUE,
	jenis_kelamin jenis_kelamin_mahasiswa_enum NOT NULL,
	no_hp varchar (20) DEFAULT '-',
	tanggal_lahir date NOT NULL,
	id_program_studi varchar (10) NOT NULL,
	FOREIGN KEY (id_program_studi) REFERENCES b1.program_studi (id_program_studi)
);

SELECT * FROM b1.mahasiswa;

DROP TABLE b1.mahasiswa;

-- AKUN
CREATE TYPE role_akun_enum
AS ENUM('Mahasiswa', 'Dosen');

CREATE TABLE b1.akun (
	id_akun serial PRIMARY KEY,
	username varchar (50) NOT NULL UNIQUE,
	password_akun varchar (100) NOT NULL,
	role_akun role_akun_enum NOT NULL,
	nim varchar (15) UNIQUE,
	nidn varchar (15) UNIQUE,
	dibuat_pada timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
	FOREIGN KEY (nim) REFERENCES b1.mahasiswa (nim),
	FOREIGN KEY (nidn) REFERENCES b1.dosen (nidn)	
);

SELECT * FROM b1.akun;

DROP TABLE b1.akun;

-- MATA KULIAH
CREATE TYPE sks_enum
AS ENUM('1', '2', '3');

CREATE TABLE b1.mata_kuliah (
	id_mata_kuliah varchar (10) PRIMARY KEY,
	id_program_studi varchar (10) NOT NULL,
	nidn varchar (15) NOT NULL,
	nama_mata_kuliah varchar (30) NOT NULL,
	sks sks_enum NOT NULL,
	semester_ke smallint NOT NULL CHECK (semester_ke BETWEEN 1 AND 14),
	FOREIGN KEY (id_program_studi) REFERENCES b1.program_studi (id_program_studi),
	FOREIGN KEY (nidn) REFERENCES b1.dosen (nidn)
);

SELECT * FROM b1.mata_kuliah;

DROP TABLE b1.mata_kuliah;

-- TAHUN AKADEMIK
CREATE TYPE semester_aktif_enum
AS ENUM('Ganjil', 'Genap');

CREATE TYPE status_enum
AS ENUM('Aktif', 'Nonaktif');

CREATE TABLE b1.tahun_akademik (
	id_tahun varchar (10) PRIMARY KEY,
	tahun varchar (10) NOT NULL,
	semester_aktif semester_aktif_enum NOT NULL,
	status status_enum NOT NULL
);

SELECT * FROM b1.tahun_akademik;

DROP TABLE b1.tahun_akademik;

-- RUANGAN
CREATE TABLE b1.ruangan (
	id_ruangan varchar (10) PRIMARY KEY,
	nama_ruangan varchar (50) NOT NULL,
	gedung varchar (30) NOT NULL,
	kapasitas smallint CHECK (kapasitas > 0)
);

SELECT * FROM b1.ruangan;

DROP TABLE b1.ruangan;

-- KELAS KULIAH
CREATE TABLE b1.kelas_kuliah (
	id_kelas_kuliah varchar (10) PRIMARY KEY,
	id_mata_kuliah varchar (10) NOT NULL,
	nidn varchar (15) NOT NULL,
	id_tahun varchar (10) NOT NULL,
	id_ruangan varchar (10) NOT NULL,
	nama_kelas varchar (5) NOT NULL,
	kuota smallint CHECK (kuota > 0),
	FOREIGN KEY (id_mata_kuliah) REFERENCES b1.mata_kuliah (id_mata_kuliah),
	FOREIGN KEY (nidn) REFERENCES b1.dosen (nidn),
	FOREIGN KEY (id_ruangan) REFERENCES b1.ruangan (id_ruangan),
	FOREIGN KEY (id_tahun) REFERENCES b1.tahun_akademik (id_tahun)
);

SELECT * FROM b1.kelas_kuliah;

DROP TABLE b1.kelas_kuliah;

--  JADWAL
CREATE TYPE hari_enum
AS ENUM('Senin', 'Selasa', 'Rabu', 'Kamis', 'Jumat');

CREATE TABLE b1.jadwal (
	id_jadwal varchar (10) PRIMARY KEY,
	id_kelas_kuliah varchar (10) NOT NULL,
	id_ruangan varchar (10) NOT NULL,
	hari hari_enum NOT NULL,
	jam_mulai time NOT NULL,
	jam_selesai time NOT NULL,
	FOREIGN KEY (id_kelas_kuliah) REFERENCES b1.kelas_kuliah (id_kelas_kuliah),
	FOREIGN KEY (id_ruangan) REFERENCES b1.ruangan (id_ruangan)
);

SELECT * FROM b1.jadwal;

DROP TABLE b1.jadwal;

-- KRS
CREATE TYPE status_persetujuan_enum
AS ENUM('Menunggu', 'Disetujui', 'Ditolak');

CREATE TABLE b1.krs (
	id_krs varchar (10) PRIMARY KEY,
	nim varchar (15) NOT NULL,
	id_tahun varchar (10) NOT NULL,
	tanggal_ambil date NOT NULL DEFAULT CURRENT_DATE,
	status_persetujuan status_persetujuan_enum NOT NULL DEFAULT 'Menunggu',
	FOREIGN KEY (nim) REFERENCES b1.mahasiswa (nim),
	FOREIGN KEY (id_tahun) REFERENCES b1.tahun_akademik (id_tahun)
);

SELECT * FROM b1.krs;

DROP TABLE b1.krs;

-- DETAIL_KRS
CREATE TABLE b1.detail_krs (
	id_detail_krs varchar (10) PRIMARY KEY,
	id_krs varchar (10) NOT NULL,
	id_kelas_kuliah varchar (10) NOT NULL,
	CONSTRAINT unik_detail_krs UNIQUE (id_krs, id_kelas_kuliah),
	FOREIGN KEY (id_krs) REFERENCES b1.krs (id_krs),
	FOREIGN KEY (id_kelas_kuliah) REFERENCES b1.kelas_kuliah (id_kelas_kuliah)
);

SELECT * FROM b1.detail_krs;

DROP TABLE b1.detail_krs;

-- NILAI
CREATE TABLE b1.nilai (
	id_nilai varchar (10) PRIMARY KEY,
	id_detail_krs varchar (10) NOT NULL UNIQUE,
	nilai_tugas decimal (5, 2) CHECK (nilai_tugas BETWEEN 0 AND 100) DEFAULT 0,
	nilai_uts decimal (5, 2) CHECK (nilai_uts BETWEEN 0 AND 100) DEFAULT 0,
	nilai_uas decimal (5, 2) CHECK (nilai_uas BETWEEN 0 AND 100) DEFAULT 0,
	nilai_akhir decimal (5, 2) DEFAULT 0,
	predikat varchar (5),
	FOREIGN KEY (id_detail_krs) REFERENCES b1.detail_krs (id_detail_krs)
);

SELECT * FROM b1.nilai;

DROP TABLE b1.nilai;

-- PRESENSI
CREATE TYPE status_kehadiran_enum
AS ENUM('Hadir', 'Izin', 'Sakit', 'Absen');

CREATE TABLE b1.presensi (
	id_presensi varchar (10) PRIMARY KEY,
	id_detail_krs varchar (10) NOT NULL,
	id_jadwal varchar (10) NOT NULL,
	tanggal date NOT NULL DEFAULT CURRENT_DATE,
	status_kehadiran status_kehadiran_enum NOT NULL DEFAULT 'Absen',
	pertemuan smallint NOT NULL CHECK (pertemuan BETWEEN 1 AND 16),
	FOREIGN KEY (id_detail_krs) REFERENCES b1.detail_krs (id_detail_krs),
	FOREIGN KEY (id_jadwal)	REFERENCES b1.jadwal (id_jadwal)
);

SELECT * FROM b1.presensi;

DROP TABLE b1.presensi;


-- INDEX
CREATE INDEX idx_dosen_program_studi     ON b1.dosen (id_program_studi);
CREATE INDEX idx_mahasiswa_program_studi ON b1.mahasiswa (id_program_studi);
CREATE INDEX idx_matkul_program_studi    ON b1.mata_kuliah (id_program_studi);
CREATE INDEX idx_matkul_dosen            ON b1.mata_kuliah (nidn);
CREATE INDEX idx_kelas_matkul            ON b1.kelas_kuliah (id_mata_kuliah);
CREATE INDEX idx_kelas_dosen             ON b1.kelas_kuliah (nidn);
CREATE INDEX idx_kelas_tahun       	     ON b1.kelas_kuliah (id_tahun);
CREATE INDEX idx_jadwal_kelas      	     ON b1.jadwal (id_kelas_kuliah);
CREATE INDEX idx_jadwal_ruangan   	     ON b1.jadwal (id_ruangan);
CREATE INDEX idx_krs_nim          	     ON b1.krs (nim);
CREATE INDEX idx_detail_krs_krs          ON b1.detail_krs (id_krs);
CREATE INDEX idx_detail_krs_kelas        ON b1.detail_krs (id_kelas_kuliah);
CREATE INDEX idx_nilai_detail_krs        ON b1.nilai (id_detail_krs);
CREATE INDEX idx_presensi_jadwal         ON b1.presensi (id_jadwal);
CREATE INDEX idx_presensi_detail_krs     ON b1.presensi (id_detail_krs);


-- TRIGGER
CREATE OR REPLACE FUNCTION hitung_nilai()
RETURNS TRIGGER AS $$
BEGIN
-- Hitung nilai akhir (bobot: tugas 30%, UTS 30%, UAS 40%)
	NEW.nilai_akhir := ROUND(
		(COALESCE (NEW.nilai_tugas, 0) * 0.3) +
		(COALESCE (NEW.nilai_uts, 0) * 0.3) +
		(COALESCE (NEW.nilai_uas, 0) * 0.4),
	2);
 
 -- Tentukan predikat
	NEW.predikat := CASE
		WHEN NEW.nilai_akhir >= 85 THEN 'A'
		WHEN NEW.nilai_akhir >= 75 THEN 'B'
		WHEN NEW.nilai_akhir >= 65 THEN 'C'
		WHEN NEW.nilai_akhir >= 50 THEN 'D'
			ELSE 'E'
	END;
 
	RETURN NEW;
END;
$$ LANGUAGE plpgsql;
 
CREATE TRIGGER trigger_hitung_nilai
BEFORE INSERT OR UPDATE ON b1.nilai
FOR EACH ROW
EXECUTE FUNCTION hitung_nilai();

-- VIEW
CREATE OR REPLACE VIEW b1.view_jadwal_mahasiswa AS
SELECT DISTINCT
    k.nim,
    mk.nama_mata_kuliah,
    kk.nama_kelas,
    mk.sks,
    j.hari,
    j.jam_mulai,
    j.jam_selesai,
    r.nama_ruangan,
    r.gedung,
    d.nama AS nama_dosen
FROM b1.krs k
JOIN b1.detail_krs dk    ON k.id_krs = dk.id_krs
JOIN b1.kelas_kuliah kk  ON dk.id_kelas_kuliah = kk.id_kelas_kuliah
JOIN b1.mata_kuliah mk   ON kk.id_mata_kuliah = mk.id_mata_kuliah
JOIN b1.jadwal j         ON j.id_kelas_kuliah = kk.id_kelas_kuliah
LEFT JOIN b1.ruangan r   ON j.id_ruangan = r.id_ruangan
LEFT JOIN b1.dosen d     ON kk.nidn = d.nidn
WHERE k.status_persetujuan = 'Disetujui';

CREATE VIEW b1.view_tahun_aktif AS
SELECT 
    id_tahun,
    tahun,
    semester_aktif,
    status
FROM b1.tahun_akademik
WHERE status = 'Aktif';

CREATE VIEW b1.view_khs AS
SELECT
    k.nim,
    mk.nama_mata_kuliah,
    mk.sks,
    n.nilai_tugas,
    n.nilai_uts,
    n.nilai_uas,
    n.nilai_akhir,
    n.predikat
FROM b1.nilai n
JOIN b1.detail_krs dk   ON n.id_detail_krs = dk.id_detail_krs
JOIN b1.krs k           ON dk.id_krs = k.id_krs
JOIN b1.kelas_kuliah kk ON dk.id_kelas_kuliah = kk.id_kelas_kuliah
JOIN b1.mata_kuliah mk  ON kk.id_mata_kuliah = mk.id_mata_kuliah;


DROP VIEW b1.view_jadwal_mahasiswa;
DROP VIEW b1.view_tahun_aktif;
DROP VIEW b1.view_khs;


-- DELETE
DELETE FROM b1.presensi;
DELETE FROM b1.nilai;
DELETE FROM b1.detail_krs;
DELETE FROM b1.krs;
DELETE FROM b1.jadwal;
DELETE FROM b1.kelas_kuliah;
DELETE FROM b1.mata_kuliah;
DELETE FROM b1.akun;
DELETE FROM b1.mahasiswa;
DELETE FROM b1.dosen;
DELETE FROM b1.tahun_akademik;
DELETE FROM b1.ruangan;
DELETE FROM b1.program_studi;

delete from b1.krs;

delete from b1.detail_krs;

DELETE FROM b1.nilai;

delete from b1.presensi;

  
-- ALTER TABLE
alter table b1.tahun_akademik
alter column tahun type varchar (10);