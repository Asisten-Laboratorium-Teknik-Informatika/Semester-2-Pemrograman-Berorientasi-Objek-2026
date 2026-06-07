CREATE DATABASE gudang_logistik;

CREATE SCHEMA gudang;

--tabel fondasi:

CREATE TABLE gudang.roles (
    id_roles INT PRIMARY KEY,
    name VARCHAR(50),
    description VARCHAR(50)
);

CREATE TABLE gudang.users (
    id_user INT PRIMARY KEY,
    id_roles INT,
    username VARCHAR(50),
    full_name VARCHAR(50),
    password VARCHAR(50),
    email VARCHAR(50),
    created_at timestamp(0) default current_timestamp,

    FOREIGN KEY (id_roles) REFERENCES gudang.roles(id_roles)
);

--master data:

CREATE TABLE gudang.categories (
    id_categories INT PRIMARY KEY,
    name VARCHAR(50),
    description VARCHAR(70)
);

CREATE TABLE gudang.supplier (
    id_supplier INT PRIMARY KEY,
    name VARCHAR(50),
    phone VARCHAR(20),
    address TEXT,
    created_at timestamp(0) default current_timestamp
);

CREATE TABLE gudang.product (
    id_product INT PRIMARY KEY,
    id_categories INT,
    id_supplier INT,
    name VARCHAR(50),
    price NUMERIC(12,2),
    unit VARCHAR(20),
    created_at timestamp(0) default current_timestamp,

    FOREIGN KEY (id_categories) REFERENCES gudang.categories(id_categories),
    FOREIGN KEY (id_supplier) REFERENCES gudang.supplier(id_supplier)
);

--warehouse & stock

CREATE TABLE gudang.warehouse (
    id_warehouse INT PRIMARY KEY,
    name VARCHAR(50),
    address VARCHAR(50),
	capacity INT,
    created_at timestamp(0) default current_timestamp
);

CREATE TABLE gudang.warehouse_stocks (
    id_stock INT PRIMARY KEY,
    id_product INT,
    id_warehouse INT,
    quantity INT,
    updated_at timestamp(0) default current_timestamp,

    FOREIGN KEY (id_product) REFERENCES gudang.product(id_product),
    FOREIGN KEY (id_warehouse) REFERENCES gudang.warehouse(id_warehouse)
);

--incoming goods

CREATE TABLE gudang.incoming_goods (
    id_ingoods INT PRIMARY KEY,
    id_supplier INT,
    id_warehouse INT,
    id_user INT,
    invoice_code VARCHAR(50),
    incoming_date timestamp(0) default current_timestamp,
    status VARCHAR(20),

    FOREIGN KEY (id_supplier) REFERENCES gudang.supplier(id_supplier),
    FOREIGN kEY (id_warehouse) REFERENCES gudang.warehouse(id_warehouse),
    FOREIGN KEY (id_user) REFERENCES gudang.users(id_user)
);

CREATE TABLE gudang.incoming_goods_detail (
    id_ingoods_detail INT PRIMARY KEY,
    id_ingoods INT,
    id_product INT,
    quantity INT,
    created_at timestamp(0) default current_timestamp,

    FOREIGN KEY (id_ingoods) REFERENCES gudang.incoming_goods(id_ingoods),
    FOREIGN KEY (id_product) REFERENCES gudang.product(id_product)
);

--cust

CREATE TABLE gudang.customer ( 
    id_customer INT PRIMARY KEY,
    name VARCHAR(50),
    phone VARCHAR(20),
    address VARCHAR(50),
    created_at timestamp(0) default current_timestamp
);

--outgoing goods

CREATE TABLE gudang.outgoing_goods (
    id_outgoods INT PRIMARY KEY,
    id_customer INT,
    id_warehouse INT,
    id_user INT,
    invoice_code VARCHAR(50),
    destination_address TEXT,
    outgoing_date timestamp(0) default current_timestamp,
    status VARCHAR(20),

    FOREIGN KEY (id_customer) REFERENCES gudang.customer(id_customer),
    FOREIGN KEY (id_warehouse) REFERENCES gudang.warehouse(id_warehouse),
    FOREIGN KEY (id_user) REFERENCES gudang.users(id_user)
);

CREATE TABLE gudang.outgoing_goods_detail (
    id_outgoods_detail INT PRIMARY KEY,
    id_outgoods INT,
    id_product INT,
    quantity INT,

    FOREIGN KEY (id_outgoods) REFERENCES gudang.outgoing_goods(id_outgoods),
    FOREIGN KEY (id_product) REFERENCES gudang.product(id_product)
);

--shipment

CREATE TABLE gudang.shipment (
    id_shipment INT PRIMARY KEY,
    id_warehouse INT,
    destination VARCHAR(50),
    shipment_date timestamp(0) default current_timestamp,
    status VARCHAR(20),

    FOREIGN KEY (id_warehouse) REFERENCES gudang.warehouse(id_warehouse)
);

CREATE TABLE gudang.shipment_details (
    id_shipment_details INT PRIMARY KEY,
    id_shipment INT,
    id_product INT,
    quantity INT,
    created_at timestamp(0) default current_timestamp,

    FOREIGN KEY (id_shipment) REFERENCES gudang.shipment(id_shipment),
    FOREIGN KEY (id_product) REFERENCES gudang.product(id_product)
);

CREATE TABLE gudang.distribution (
    id_distribution SERIAL PRIMARY KEY,
    distribution_code VARCHAR(30),
    id_source_warehouse INT,
    id_destination_warehouse INT,
    id_product INT,
    quantity INT,
    distribution_date DATE DEFAULT CURRENT_DATE,

    FOREIGN KEY (id_source_warehouse)
    REFERENCES gudang.warehouse(id_warehouse),

    FOREIGN KEY (id_destination_warehouse)
    REFERENCES gudang.warehouse(id_warehouse),

    FOREIGN KEY (id_product)
    REFERENCES gudang.product(id_product)
);

--TEST QUERY
SELECT * FROM gudang.roles;

INSERT INTO gudang.roles (id_roles, name)
VALUES (1, 'admin');




--	TEST DATA

DELETE FROM gudang.roles;

INSERT INTO gudang.roles (id_roles, name, description) VALUES
(1, 'admin', 'Akses penuh ke seluruh sistem'),
(2, 'warehouse_staff', 'Mengelola stok dan barang di gudang'),
(3, 'logistics_staff', 'Mengatur pengiriman barang');

INSERT INTO gudang.users (id_user, id_roles, username, full_name, password, email) VALUES
(1, 1, 'admin01', 'Haikala', '123', 'adminHaikala@mail.com'),
(2, 2, 'staff01', 'Narda', '123', 'staffNarda@mail.com'),
(3, 3, 'logistik01', 'Juna', '123', 'logistikJuna@mail.com'),
(4, 1, 'admin02', 'Raka', '123', 'adminRaka@mail.com'),
(5, 2, 'staff02', 'Jinan', '123', 'staffJinan@mail.com'),
(6, 3, 'logistik02', 'Arga', '123', 'logistikArga@mail.com'),
(7, 2, 'staff03', 'Jeff', '123', 'staffJeff@mail.com'),
(8, 2, 'staff04', 'Elion', '123', 'staffElion@mail.com'),
(9, 3, 'logistik03', 'Robby', '123', 'logistikRobby@mail.com'),
(10, 3, 'logistik04', 'Rifqi', '123', 'logistikRifqi@mail.com');

INSERT INTO gudang.categories (id_categories, name, description) VALUES
(1, 'Produk Instan', 'Produk makanan yang dapat disajikan dengan cepat.'),
(2, 'Makanan Kaleng', 'Produk makanan yang dikemas dalam kaleng untuk daya tahan lebih lama.'),
(3, 'Snack', 'Makanan ringan');

INSERT INTO gudang.supplier (id_supplier, name, phone, address) VALUES
(1, 'PT Sumber Makmur', '0811111111', 'Medan'),
(2, 'PT Jaya Abadi', '0822222222', 'Bandung'),
(3, 'PT Snack Enak', '0866666666', 'Jakarta'),
(4, 'PT Maju Bersama', '0833333333', 'Surabaya'),
(5, 'PT Nusantara Food', '0844444444', 'Yogyakarta'),
(6, 'PT Sentosa', '0855555555', 'Semarang'),
(7, 'PT Prima Logistik', '0877777777', 'Palembang'),
(8, 'PT Cahaya Abadi', '0888888888', 'Makassar'),
(9, 'PT Sejahtera', '0899999999', 'Pekanbaru'),
(10, 'PT Global Distribusi', '0812345678', 'Medan');

INSERT INTO gudang.product (id_product, id_categories, id_supplier, name, price, unit) VALUES
(1, 1, 2, 'Indomie Goreng', 50000, 'Dus'),
(2, 2, 3, 'Sarden', 20000, 'Kaleng'),
(3, 3, 1, 'Keripik Kentang', 15000, 'Pack'),
(4, 1, 2, 'Mie Kuah Soto', 48000, 'Dus'),
(5, 1, 3, 'Pop Mie Kari Ayam', 50000, 'Dus'),
(6, 2, 1, 'Kornet Sapi', 35000, 'Kaleng'),
(7, 2, 2, 'Tuna Kaleng', 28000, 'Kaleng'),
(8, 3, 3, 'Chitato Balado', 12000, 'Pack'),
(9, 3, 1, 'Qtela Singkong', 10000, 'Pack'),
(10, 3, 2, 'Taro Net BBQ', 8000, 'Pack');

INSERT INTO gudang.warehouse (id_warehouse, name, address, capacity) VALUES
(1, 'Gudang Berkah', 'Jl. Ringroad No.45', 1500),
(2, 'Gudang Sukses', 'Jl. Gatot Subroto No.123', 1000),
(3, 'Gudang Cahaya', 'Jl. Sisingamangaraja No.32', 500),
(4, 'Gudang Sentosa', 'Jl. Asia No.88', 1200),
(5, 'Gudang Makmur', 'Jl. Setia Budi No.12', 900),
(6, 'Gudang Sejahtera', 'Jl. Krakatau No.7', 800),
(7, 'Gudang Nusantara', 'Jl. Diponegoro No.21', 1100),
(8, 'Gudang Maju Jaya', 'Jl. Veteran No.15', 1300),
(9, 'Gudang Prima', 'Jl. Cemara No.10', 700),
(10, 'Gudang Mandiri', 'Jl. Sudirman No.99', 1600);

INSERT INTO gudang.warehouse_stocks (id_stock, id_product, id_warehouse, quantity) VALUES
(1, 1, 1, 100),
(2, 2, 1, 150),
(3, 3, 2, 70),
(4, 4, 2, 50),
(5, 5, 3, 90),
(6, 6, 3, 80),
(7, 7, 4, 110),
(8, 8, 4, 40),
(9, 9, 5, 120),
(10, 10, 5, 60),
(11, 1, 6, 50),
(12, 2, 6, 90),
(13, 3, 7, 100),
(14, 4, 7, 80),
(15, 5, 8, 50),
(16, 6, 8, 60),
(17, 7, 9, 40),
(18, 8, 9, 130),
(19, 9, 10, 100),
(20, 10, 10, 70);

INSERT INTO gudang.incoming_goods (id_ingoods, id_supplier, id_warehouse, id_user, invoice_code, status) VALUES
(1, 1, 1, 2, 'INV-IN-001', 'Received'),
(2, 2, 2, 2, 'INV-IN-002', 'Received'),
(3, 3, 3, 2, 'INV-IN-003', 'Received'),
(4, 4, 4, 2, 'INV-IN-004', 'Received'),
(5, 5, 5, 2, 'INV-IN-005', 'Received'),
(6, 6, 6, 2, 'INV-IN-006', 'Received'),
(7, 7, 7, 2, 'INV-IN-007', 'Received'),
(8, 8, 8, 2, 'INV-IN-008', 'Received'),
(9, 9, 9, 2, 'INV-IN-009', 'Received'),
(10, 10, 10, 2, 'INV-IN-010', 'Received'); 

INSERT INTO gudang.incoming_goods_detail (id_ingoods_detail, id_ingoods, id_product, quantity) VALUES
(1, 1, 1, 20),
(2, 2, 2, 15),
(3, 3, 3, 10),
(4, 4, 4, 25),
(5, 5, 5, 30),
(6, 6, 6, 40),
(7, 7, 7, 25),
(8, 8, 8, 15),
(9, 9, 9, 30),
(10, 10, 10, 35);

INSERT INTO gudang.customer (id_customer, name, phone, address) VALUES
(1, 'Toko Abadi', '0812345678', 'Medan'),
(2, 'Toko Citra', '0823456789', 'Jakarta'),
(3, 'Toko Sanjaya', '0834567890', 'Bandung'),
(4, 'Swalayan 77', '0845678901', 'Surabaya'),
(5, 'Toko Harapan Baru', '0856789012', 'Yogyakarta'),
(6, 'Minimarket Family', '0867890123', 'Semarang'),
(7, 'Toko Rezeki Lancar', '0878901234', 'Palembang'),
(8, 'Swalayan Sentosa', '0889012345', 'Makassar'),
(9, 'Toko Serba Ada', '0890123456', 'Pekanbaru'),
(10, 'Minimarket Cemerlang', '0811122233', 'Medan');

INSERT INTO gudang.outgoing_goods (id_outgoods, id_customer, id_warehouse, id_user, invoice_code, destination_address, status) VALUES
(1, 1, 1, 3, 'INV-OUT-001', 'Medan', 'Delivered'),
(2, 2, 2, 3, 'INV-OUT-002', 'Jakarta', 'Delivered'),
(3, 3, 3, 3, 'INV-OUT-003', 'Bandung', 'Delivered'),
(4, 4, 4, 3, 'INV-OUT-004', 'Surabaya', 'Delivered'),
(5, 5, 5, 3, 'INV-OUT-005', 'Yogyakarta', 'Delivered'),
(6, 6, 6, 3, 'INV-OUT-006', 'Semarang', 'Shipped'),
(7, 7, 7, 3, 'INV-OUT-007', 'Palembang', 'Delivered'),
(8, 8, 8, 3, 'INV-OUT-008', 'Makassar', 'Shipped'),
(9, 9, 9, 3, 'INV-OUT-009', 'Pekanbaru', 'Delivered'),
(10, 10, 10, 3, 'INV-OUT-010', 'Medan', 'Pending');

INSERT INTO gudang.outgoing_goods_detail (id_outgoods_detail, id_outgoods, id_product, quantity) VALUES
(1, 1, 1, 40),
(2, 2, 2, 50),
(3, 3, 3, 30),
(4, 4, 4, 25),
(5, 5, 5, 20),
(6, 6, 6, 35),
(7, 7, 7, 15),
(8, 8, 8, 45),
(9, 9, 9, 30),
(10, 10, 10, 25);

INSERT INTO gudang.shipment (id_shipment, id_warehouse, destination, status) VALUES
(1, 1, 'Medan', 'Delivered'),
(2, 2, 'Jakarta', 'Delivered'),
(3, 3, 'Bandung', 'Delivered'),
(4, 4, 'Surabaya', 'Shipped'),
(5, 5, 'Yogyakarta', 'Delivered'),
(6, 6, 'Semarang', 'Pending'),
(7, 7, 'Palembang', 'Delivered'),
(8, 8, 'Makassar', 'Shipped'),
(9, 9, 'Pekanbaru', 'Pending'),
(10, 10, 'Medan', 'Delivered');

INSERT INTO gudang.shipment_details (id_shipment_details, id_shipment, id_product, quantity) VALUES
(1, 1, 1, 50),
(2, 2, 2, 40),
(3, 3, 3, 20),
(4, 4, 4, 25),
(5, 5, 5, 20),
(6, 6, 6, 35),
(7, 7, 7, 15),
(8, 8, 8, 45),
(9, 9, 9, 30),
(10, 10, 10, 25);

SELECT * FROM gudang.product;


--PAKAI JOIN
-- lihat user dan role
SELECT 
    u.id_user,
    u.username,
    u.full_name,
    r.name AS role_name,
    u.email
FROM gudang.users u
JOIN gudang.roles r ON u.id_roles = r.id_roles;

-- lihat product + category + supplier
SELECT
    p.id_product,
    p.name AS product_name,
    c.name AS category_name,
    s.name AS supplier_name,
    p.price,
    p.unit
FROM gudang.product p
JOIN gudang.categories c ON p.id_categories = c.id_categories
JOIN gudang.supplier s ON p.id_supplier = s.id_supplier;

-- lihat stok barang per gudang
SELECT
    w.name AS warehouse,
    p.name AS product,
    ws.quantity,
    p.unit
FROM gudang.warehouse_stocks ws
JOIN gudang.product p ON ws.id_product = p.id_product
JOIN gudang.warehouse w ON ws.id_warehouse = w.id_warehouse;

-- lihat semua stok
SELECT
    w.name AS warehouse,
    p.name AS product,
    ws.quantity,
    p.unit
FROM gudang.warehouse_stocks ws
JOIN gudang.product p ON ws.id_product = p.id_product
JOIN gudang.warehouse w ON ws.id_warehouse = w.id_warehouse
ORDER BY w.name;

-- detail barang masuk
SELECT
    ig.invoice_code,
    s.name AS supplier,
    w.name AS warehouse,
    p.name AS product,
    igd.quantity,
    ig.status,
    ig.incoming_date
FROM gudang.incoming_goods ig
JOIN gudang.supplier s ON ig.id_supplier = s.id_supplier
JOIN gudang.warehouse w ON ig.id_warehouse = w.id_warehouse
JOIN gudang.incoming_goods_detail igd ON ig.id_ingoods = igd.id_ingoods
JOIN gudang.product p ON igd.id_product = p.id_product;

-- detail barang keluar
SELECT
    og.invoice_code,
    c.name AS customer,
    w.name AS warehouse,
    p.name AS product,
    ogd.quantity,
    og.status,
    og.outgoing_date
FROM gudang.outgoing_goods og
JOIN gudang.customer c ON og.id_customer = c.id_customer
JOIN gudang.warehouse w ON og.id_warehouse = w.id_warehouse
JOIN gudang.outgoing_goods_detail ogd ON og.id_outgoods = ogd.id_outgoods
JOIN gudang.product p ON ogd.id_product = p.id_product;

-- detail shipment/distribusi
SELECT
    s.id_shipment,
    w.name AS warehouse,
    s.destination,
    p.name AS product,
    sd.quantity,
    s.status,
    s.shipment_date
FROM gudang.shipment s
JOIN gudang.warehouse w ON s.id_warehouse = w.id_warehouse
JOIN gudang.shipment_details sd ON s.id_shipment = sd.id_shipment
JOIN gudang.product p ON sd.id_product = p.id_product;

-- riwayat barang keluar / pengiriman
SELECT
    c.name AS customer,
    p.name AS product,
    ogd.quantity,
    og.status,
    og.outgoing_date
FROM gudang.outgoing_goods og
JOIN gudang.customer c ON og.id_customer = c.id_customer
JOIN gudang.outgoing_goods_detail ogd ON og.id_outgoods = ogd.id_outgoods
JOIN gudang.product p ON ogd.id_product = p.id_product;

-- riwayat barang masuk
SELECT
    s.name AS supplier,
    p.name AS product,
    igd.quantity,
    ig.status,
    ig.incoming_date
FROM gudang.incoming_goods ig
JOIN gudang.supplier s ON ig.id_supplier = s.id_supplier
JOIN gudang.incoming_goods_detail igd ON ig.id_ingoods = igd.id_ingoods
JOIN gudang.product p ON igd.id_product = p.id_product;

--   transaction demo:

-- transaction barang keluar:
BEGIN;

-- tambah transaksi barang keluar
INSERT INTO gudang.outgoing_goods
(id_outgoods, id_customer, id_warehouse, invoice_code, destination_address, status)
VALUES
(4, 1, 2, 'INV-OUT-007', 'Medan', 'Delivered');

-- tambah detail barang keluar
INSERT INTO gudang.outgoing_goods_detail
(id_outgoods_detail, id_outgoods, id_product, quantity)
VALUES
(4, 4, 1, 10);

-- update stock gudang
UPDATE gudang.warehouse_stocks
SET quantity = quantity - 10
WHERE id_product = 1
AND id_warehouse = 2;

COMMIT;

-- transaction barang masuk:

BEGIN;

--tambah transaksi barang masuk
INSERT INTO gudang.incoming_goods
(id_ingoods, id_supplier, id_warehouse, invoice_code, status)
VALUES
(4, 1, 2, 'INV-IN-007', 'Received');

-- tambah detail barang masuk
INSERT INTO gudang.incoming_goods_detail
(id_ingoods_detail, id_ingoods, id_product, quantity)
VALUES
(4, 4, 1, 20);

-- update stock gudang
UPDATE gudang.warehouse_stocks
SET quantity = quantity + 20
WHERE id_product = 1
AND id_warehouse = 2;

COMMIT;

-- transaksi distribusi antar gudang

BEGIN;

-- tambah shipment
INSERT INTO gudang.shipment
(id_shipment, id_warehouse, destination, status)
VALUES
(4, 1, 'Gudang Sukses', 'Delivered');

-- tambah detail shipment
INSERT INTO gudang.shipment_details
(id_shipment_details, id_shipment, id_product, quantity)
VALUES
(4, 4, 1, 15);

-- kurangi stok gudang asal
UPDATE gudang.warehouse_stocks
SET quantity = quantity - 15
WHERE id_product = 1
AND id_warehouse = 1;

-- tambah stok gudang tujuan
UPDATE gudang.warehouse_stocks
SET quantity = quantity + 15
WHERE id_product = 1
AND id_warehouse = 2;

COMMIT;