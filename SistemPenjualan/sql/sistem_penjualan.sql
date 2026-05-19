CREATE DATABASE sistem_penjualan;

\c sistem_penjualan;

CREATE TABLE "user" (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(50) NOT NULL,
    email VARCHAR(100),
    role VARCHAR(20) DEFAULT 'kasir',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE kategori (
    id SERIAL PRIMARY KEY,
    nama_kategori VARCHAR(100) NOT NULL,
    deskripsi TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
); 

CREATE TABLE supplier (
    id SERIAL PRIMARY KEY,
    nama_supplier VARCHAR(100) NOT NULL,
    alamat TEXT,
    telepon VARCHAR(20),
    email VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE barang (
    id SERIAL PRIMARY KEY,
    nama_barang VARCHAR(100) NOT NULL,
    harga NUMERIC(10,2) NOT NULL CHECK (harga >= 0),
    stok INT NOT NULL DEFAULT 0 CHECK (stok >= 0),
    id_kategori INT REFERENCES kategori(id) ON DELETE SET NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE barang_masuk (
    id SERIAL PRIMARY KEY,
    id_barang INT REFERENCES barang(id) ON DELETE CASCADE,
    id_supplier INT REFERENCES supplier(id) ON DELETE SET NULL,
    jumlah INT NOT NULL CHECK (jumlah > 0),
    harga_beli NUMERIC(10,2) NOT NULL CHECK (harga_beli >= 0),
    tgl_masuk DATE NOT NULL DEFAULT CURRENT_DATE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE pelanggan (
    id SERIAL PRIMARY KEY,
    nama VARCHAR(100) NOT NULL,
    alamat TEXT,
    telepon VARCHAR(20),
    email VARCHAR(100),
    poin INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE transaksi (
    id SERIAL PRIMARY KEY,
    id_pelanggan INT REFERENCES pelanggan(id) ON DELETE SET NULL,
    id_user INT REFERENCES "user"(id) ON DELETE SET NULL,
    tgl_transaksi DATE NOT NULL DEFAULT CURRENT_DATE,
    total_harga NUMERIC(10,2) NOT NULL DEFAULT 0 CHECK (total_harga >= 0),
    status VARCHAR(20) DEFAULT 'proses',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE detail_transaksi (
    id SERIAL PRIMARY KEY,
    id_transaksi INT REFERENCES transaksi(id) ON DELETE CASCADE,
    id_barang INT REFERENCES barang(id) ON DELETE SET NULL,
    jumlah INT NOT NULL CHECK (jumlah > 0),
    harga_satuan NUMERIC(10,2) NOT NULL CHECK (harga_satuan >= 0),
    subtotal NUMERIC(10,2) NOT NULL CHECK (subtotal >= 0),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE pembayaran (
    id SERIAL PRIMARY KEY,
    id_transaksi INT REFERENCES transaksi(id) ON DELETE CASCADE,
    metode VARCHAR(50) NOT NULL,
    jumlah_dibayar NUMERIC(10,2) NOT NULL CHECK (jumlah_dibayar >= 0),
    kembalian NUMERIC(10,2) DEFAULT 0,
    tgl_pembayaran DATE NOT NULL DEFAULT CURRENT_DATE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE pengiriman (
    id SERIAL PRIMARY KEY,
    id_transaksi INT REFERENCES transaksi(id) ON DELETE CASCADE,
    kurir VARCHAR(50),
    no_resi VARCHAR(50),
    tgl_kirim DATE,
    tgl_terima DATE,
    status VARCHAR(20) DEFAULT 'dikirim',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO "user" (username, password, email, role) VALUES 
('admin', '123', 'admin@sistem.com', 'admin'),
('kasir1', '123', 'kasir1@sistem.com', 'kasir'),
('kasir2', '123', 'kasir2@sistem.com', 'kasir'),
('manager', '123', 'manager@sistem.com', 'manager'),
('gudang', '123', 'gudang@sistem.com', 'gudang');

INSERT INTO kategori (nama_kategori, deskripsi) VALUES 
('Elektronik', 'Barang elektronik dan gadget'),
('Perabot', 'Perabotan rumah tangga'),
('Aksesoris', 'Aksesoris komputer dan gadget');

INSERT INTO supplier (nama_supplier, alamat, telepon, email) VALUES 
('PT Elektronik Jaya', 'Jl. Raya No. 123 Jakarta', '021-5551234', 'sales@elektronikjaya.com'),
('CV Komputer Sejahtera', 'Jl. Tekno No. 45 Bandung', '022-5555678', 'cs@komputersejahtera.com'),
('UD Perabot Nusantara', 'Jl. Industri No. 89 Surabaya', '031-5559012', 'info@perabotnusantara.com');

INSERT INTO barang (nama_barang, harga, stok, id_kategori) VALUES 
('Laptop ASUS ROG', 15000000, 15, 1),
('Laptop Acer Swift', 8500000, 12, 1),
('Mouse Logitech', 150000, 30, 1),
('Keyboard Mechanical', 350000, 20, 1),
('Monitor Samsung 24"', 1800000, 8, 1),
('Meja Komputer', 750000, 10, 2),
('Kursi Gaming', 1250000, 8, 2),
('Mouse Pad', 75000, 50, 3),
('USB Flashdisk 64GB', 120000, 40, 3),
('Power Bank 10000mAh', 250000, 20, 3);

INSERT INTO pelanggan (nama, alamat, telepon, email, poin) VALUES 
('Budi Santoso', 'Jl. Merdeka No.10 Jakarta', '08123456789', 'budi@email.com', 100),
('Siti Aminah', 'Jl. Sudirman No.5 Bandung', '08567891234', 'siti@email.com', 50),
('Andi Wijaya', 'Jl. Diponegoro No.20 Surabaya', '08789012345', 'andi@email.com', 75),
('Dewi Kartika', 'Jl. Gatot Subroto No.15 Semarang', '08134567890', 'dewi@email.com', 30),
('Rudi Hermawan', 'Jl. Pahlawan No.8 Yogyakarta', '08225678901', 'rudi@email.com', 60);

INSERT INTO barang_masuk (id_barang, id_supplier, jumlah, harga_beli, tgl_masuk) VALUES 
(1, 1, 20, 13500000, '2026-01-05'),
(2, 1, 25, 7500000, '2026-01-05'),
(3, 2, 50, 120000, '2026-01-10'),
(4, 2, 30, 280000, '2026-01-10'),
(6, 3, 15, 600000, '2026-01-10'),
(7, 3, 12, 1000000, '2026-01-15');

INSERT INTO transaksi (id_pelanggan, id_user, tgl_transaksi, total_harga, status) VALUES 
(1, 1, '2026-05-01', 15750000, 'selesai'),
(2, 2, '2026-05-02', 1050000, 'selesai'),
(3, 1, '2026-05-03', 1500000, 'selesai'),
(4, 2, '2026-05-03', 1850000, 'selesai'),
(5, 1, '2026-05-04', 450000, 'selesai');

INSERT INTO detail_transaksi (id_transaksi, id_barang, jumlah, harga_satuan, subtotal) VALUES 
(1, 1, 1, 15000000, 15000000),
(1, 3, 5, 150000, 750000),
(2, 4, 3, 350000, 1050000),
(3, 7, 1, 1250000, 1250000),
(3, 8, 3, 75000, 225000),
(4, 6, 2, 750000, 1500000),
(4, 10, 10, 35000, 350000),
(5, 3, 3, 150000, 450000); 

UPDATE barang SET stok = stok - 1 WHERE id = 1;
UPDATE barang SET stok = stok - 5 WHERE id = 3;
UPDATE barang SET stok = stok - 3 WHERE id = 4;
UPDATE barang SET stok = stok - 1 WHERE id = 7;
UPDATE barang SET stok = stok - 3 WHERE id = 8;
UPDATE barang SET stok = stok - 2 WHERE id = 6;
UPDATE barang SET stok = stok - 10 WHERE id = 10;

INSERT INTO pembayaran (id_transaksi, metode, jumlah_dibayar, kembalian, tgl_pembayaran) VALUES 
(1, 'Tunai', 15750000, 0, '2026-05-01'),
(2, 'Transfer Bank', 1050000, 0, '2026-05-02'),
(3, 'Tunai', 1500000, 0, '2026-05-03'),
(4, 'QRIS', 1850000, 0, '2026-05-03'),
(5, 'Tunai', 500000, 50000, '2026-05-04');

INSERT INTO pengiriman (id_transaksi, kurir, no_resi, tgl_kirim, tgl_terima, status) VALUES 
(1, 'JNE', 'JNE123456789', '2026-05-02', '2026-05-05', 'DITERIMA'),
(2, 'J&T', 'JT987654321', '2026-05-03', '2026-05-04', 'DITERIMA'),
(3, 'SiCepat', 'SC555666777', '2026-05-04', '2026-05-07', 'DITERIMA'),
(4, 'JNE', 'JNE111222333', '2026-05-04', '2026-05-06', 'DITERIMA'),
(5, 'Pos Indonesia', 'POS999888777', '2026-05-05', NULL, 'DIKIRIM');

SELECT 'Database siap digunakan!' as status;
SELECT COUNT(*) as total_user FROM "user";
SELECT COUNT(*) as total_kategori FROM kategori;
SELECT COUNT(*) as total_supplier FROM supplier;
SELECT COUNT(*) as total_barang FROM barang;
SELECT COUNT(*) as total_pelanggan FROM pelanggan;
SELECT COUNT(*) as total_transaksi FROM transaksi;
SELECT COUNT(*) as total_detail_transaksi FROM detail_transaksi;
SELECT COUNT(*) as total_pembayaran FROM pembayaran;
SELECT COUNT(*) as total_pengiriman FROM pengiriman;