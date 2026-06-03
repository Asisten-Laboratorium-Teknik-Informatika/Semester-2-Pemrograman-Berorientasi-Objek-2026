create database db_sdm;

create schema project;

create table project.departemen(
	id_departemen serial primary key,
	nama_departemen varchar(100) 
);

insert into project.departemen (id_departemen, nama_departemen)
values
(1, 'Human Resources'),
(2, 'Finance & Accounting'),
(3, 'Information Technology'),
(4, 'Marketing'),
(5, 'Operations');

select * from project.departemen;

-- Update
update project.departemen
set nama_departemen = ?
where id_departemen = ?;

-- Hapus
delete from project.departemen
where id_departemen = ?;

create table project.jabatan(
	id_jabatan serial primary key,
	id_departemen int,
	nama_jabatan varchar(100) not null,
	gaji_pokok decimal(12,2) not null,
	foreign key (id_departemen) references project.departemen(id_departemen)
);

insert into project.jabatan (id_jabatan, id_departemen, nama_jabatan, gaji_pokok) 
values
(1, 1, 'HR Manager', 12000000),
(2, 1, 'HR Staff', 8000000),
(3, 2, 'Finance Manager', 16000000),
(4, 2, 'Accounting Staff', 8000000),
(5, 3, 'IT Manager', 25000000),
(6, 3, 'Software Engineer', 25000000),
(7, 4, 'Marketing Manager', 15000000),
(8, 4, 'Marketing Staff', 8000000),
(9, 5, 'Operations Manager', 10000000),
(10, 5, 'Operations Staff', 8000000);

-- Update 
update project.jabatan
set nama_jabatan = ?,
    gaji_pokok = ?
where id_jabatan = ?;

-- Hapus 
delete from project.jabatan
where id_jabatan = ?;

select * from project.jabatan;

create table project.pegawai(
	id_pegawai serial primary key,
	id_jabatan int,
	nama_pegawai varchar(100) not null,
	alamat text,
	no_hp varchar(20),
	email varchar(100),
	tanggal_masuk date,
	foreign key (id_jabatan) references project.jabatan(id_jabatan)
);

insert into project.pegawai (id_jabatan, nama_pegawai, alamat, no_hp, email, tanggal_masuk)
values
(5, 'Hary Sanjaya Siregar', 'Jl. Sisingamangaraja No.04', '081264472893', 'ryyyfrontera@gmail.com', '2020-03-01'),
(7, 'Nazril Habib Siregar', 'Jl. Khairil Anwar No.11', '081211113277', 'nazrilhabib@gmail.com', '2017-06-15'),
(6, 'Alya Dafany', 'Jl. Jamin Ginting No.7', '081288835646', 'alyadafany@gmail.com', '2021-01-10'),
(1, 'Maslia Tumanggor', 'Jl. Ahmad Yani No.22', '081287889002', 'masliatumanggor@gmail.com','2015-08-01'),
(3, 'Nailah Rizki', 'Jl. Sipirok No.3', '081234326787', 'nailahrizky@gmail.com', '2021-05-20'),
(9, 'Joong Archen', 'Jl. Pahlawan No.10', '081256567676', 'joongarchen@gmail.com', '2023-02-14'),
(10, 'Dunk Natacai', 'Jl. Imam Bonjol No.15', '081234345454', 'dunknatacai@gmail.com', '2024-11-01'),
(6, 'Pond Naravit', 'Jl. Raya No.9', '081243267897', 'pondnaravit@gmail.com', '2022-07-30'),
(2, 'Lolita Karimah', 'Jl.Jebol No.15', '081262897898', 'lolitakarimah@gmail.com', '2017-04-05'),
(4, 'Wowo Wibawa', 'Jl.Makmur No.12', '081264247899', 'wowowibawa@gmail.com', '2023-09-17'),
(8, 'Lana Safitri', 'Jl.Karya Jaya No.25', '081232567900', 'lanasfitri@gmail.com', '2020-01-03'),
(3, 'Jeff Satur', 'Jl.Pemuda No.99', '081239567901', 'jeffsatur@gmail.com', '2024-12-01'),
(5, 'Veronica', 'Jl.Cendrawasih No.5', '081231567902', 'veronica@gmail.com', '2022-07-20'),
(1, 'Fakhri Hidayat', 'Jl. Teuku Umar No.19', '081234167903', 'fakhrihidayat@gmail.com','2017-03-11'),
(7, 'Lya Kamila', 'Jl. Pahlawan No.102', '081214567904', 'lyakamila@gmail.com', '2020-10-25');

select * from project.pegawai
order by id_pegawai;

-- Update
update project.pegawai
set nama_pegawai
where id_pegawi;

-- Hapus
delete from project.pegawai
where id_pegawai = ?;

-- Profil pegawai
select p.nama_pegawai,
       j.nama_jabatan,
       p.alamat,
       p.no_hp,
       p.email,
       p.tanggal_masuk
from project.pegawai p
join project.jabatan j
    on p.id_jabatan = j.id_jabatan
where p.id_pegawai = ?;

create table project.users(
	id_user serial primary key,
	id_pegawai int,
	username varchar(50) not null,
	user_password varchar (100) not null,
	role varchar(100),
	foreign key (id_pegawai) references project.pegawai(id_pegawai)
);

insert into project.users (id_pegawai, username, user_password, role)
values
(1, 'HarySnjy', 'hary123', 'admin'),
(2, 'nzrl.hbib', 'esteh', 'pegawai'),
(3, 'Lyagurl', 'POCKET', 'admin'),
(4, 'LIaa', 'istriruben', 'pegawai'),
(5, 'nailarhn', 'jekey', 'pegawai'),
(6, 'archen.dnk', 'klop', 'pegawai'),
(7, 'natachai', 'navori', 'pegawai'),
(8, 'NAR4', 'aiaiai', 'pegawai'),
(9, 'lolita', 'lolypop', 'pegawai'),
(10, 'wowo.wib', 'nyawitnie', 'pegawai'),
(11, 'Lana.S', 'orangimup', 'pegawai'),
(12, 'jeffSatr', 'ghost', 'pegawai'),
(13, 'V3ron1ca', 'sumpitgacoan', 'pegawai'),
(14, 'fkhri', 'stecu', 'pegawai'),
(15, 'K4mila', 'naspadceceacid', 'pegawai');

select * from project.users;

select u.id_user, u.id_pegawai,  u.username, u.role
from project.users u
where username = ?;

create table project.absensi(
	id_absensi serial primary key,
	id_pegawai int,
	tanggal date not null,
	status varchar(50),
	foreign key (id_pegawai) references project.pegawai(id_pegawai)
);

insert into project.absensi (id_pegawai, tanggal, status)
values
-- Pegawai 1
(1, '2026-05-01', 'Sakit'),
(1, '2026-05-02', 'Hadir'),
(1, '2026-05-03', 'Hadir'),
(1, '2026-05-04', 'Hadir'),
(1, '2026-05-05', 'Hadir'),
(1, '2026-05-06', 'Hadir'),
(1, '2026-05-07', 'Hadir'),
(1, '2026-05-08', 'Hadir'),
(1, '2026-05-09', 'Hadir'),
(1, '2026-05-10', 'Hadir'),
(1, '2026-05-11', 'Hadir'),
(1, '2026-05-12', 'Hadir'),
(1, '2026-05-13', 'Izin'),
(1, '2026-05-14', 'Hadir'),
(1, '2026-05-15', 'Hadir'),
(1, '2026-05-16', 'Hadir'),
(1, '2026-05-17', 'Hadir'),
(1, '2026-05-18', 'Hadir'),
(1, '2026-05-19', 'Hadir'),
(1, '2026-05-20', 'Hadir'),
(1, '2026-05-21', 'Hadir'),
(1, '2026-05-22', 'Alpha'),
(1, '2026-05-23', 'Hadir'),
(1, '2026-05-24', 'Hadir'),
(1, '2026-05-25', 'Hadir'),
(1, '2026-05-26', 'Hadir'),
(1, '2026-05-27', 'Hadir'),
(1, '2026-05-28', 'Hadir'),
(1, '2026-05-29', 'Hadir'),

-- Pegawai 2
(2, '2026-05-01', 'Hadir'),
(2, '2026-05-02', 'Hadir'),
(2, '2026-05-03', 'Sakit'),
(2, '2026-05-04', 'Alpha'),
(2, '2026-05-05', 'Hadir'),
(2, '2026-05-06', 'Hadir'),
(2, '2026-05-07', 'Hadir'),
(2, '2026-05-08', 'Hadir'),
(2, '2026-05-09', 'Hadir'),
(2, '2026-05-10', 'Hadir'),
(2, '2026-05-11', 'Hadir'),
(2, '2026-05-12', 'Hadir'),
(2, '2026-05-13', 'Hadir'),
(2, '2026-05-14', 'Hadir'),
(2, '2026-05-15', 'Izin'),
(2, '2026-05-16', 'Hadir'),
(2, '2026-05-17', 'Hadir'),
(2, '2026-05-18', 'Hadir'),
(2, '2026-05-19', 'Hadir'),
(2, '2026-05-20', 'Hadir'),
(2, '2026-05-21', 'Hadir'),
(2, '2026-05-22', 'Hadir'),
(2, '2026-05-23', 'Alpha'),
(2, '2026-05-24', 'Hadir'),
(2, '2026-05-25', 'Hadir'),
(2, '2026-05-26', 'Hadir'),
(2, '2026-05-27', 'Hadir'),
(2, '2026-05-28', 'Hadir'),
(2, '2026-05-29', 'Hadir'),

-- Pegawai 3
(3, '2026-05-01', 'Hadir'),
(3, '2026-05-02', 'Hadir'),
(3, '2026-05-03', 'Hadir'),
(3, '2026-05-04', 'Hadir'),
(3, '2026-05-05', 'Hadir'),
(3, '2026-05-06', 'Hadir'),
(3, '2026-05-07', 'Hadir'),
(3, '2026-05-08', 'Hadir'),
(3, '2026-05-09', 'Hadir'),
(3, '2026-05-10', 'Hadir'),
(3, '2026-05-11', 'Hadir'),
(3, '2026-05-12', 'Hadir'),
(3, '2026-05-13', 'Hadir'),
(3, '2026-05-14', 'Hadir'),
(3, '2026-05-15', 'Hadir'),
(3, '2026-05-16', 'Hadir'),
(3, '2026-05-17', 'Izin'),
(3, '2026-05-18', 'Hadir'),
(3, '2026-05-19', 'Hadir'),
(3, '2026-05-20', 'Hadir'),
(3, '2026-05-21', 'Hadir'),
(3, '2026-05-22', 'Hadir'),
(3, '2026-05-23', 'Hadir'),
(3, '2026-05-24', 'Sakit'),
(3, '2026-05-25', 'Hadir'),
(3, '2026-05-26', 'Hadir'),
(3, '2026-05-27', 'Hadir'),
(3, '2026-05-28', 'Hadir'),
(3, '2026-05-29', 'Hadir'),

-- Pegawai 4
(4, '2026-05-01', 'Hadir'),
(4, '2026-05-02', 'Izin'),
(4, '2026-05-03', 'Hadir'),
(4, '2026-05-04', 'Hadir'),
(4, '2026-05-05', 'Sakit'),
(4, '2026-05-06', 'Hadir'),
(4, '2026-05-07', 'Hadir'),
(4, '2026-05-08', 'Hadir'),
(4, '2026-05-09', 'Hadir'),
(4, '2026-05-10', 'Hadir'),
(4, '2026-05-11', 'Hadir'),
(4, '2026-05-12', 'Hadir'),
(4, '2026-05-13', 'Hadir'),
(4, '2026-05-14', 'Hadir'),
(4, '2026-05-15', 'Hadir'),
(4, '2026-05-16', 'Hadir'),
(4, '2026-05-17', 'Izin'),
(4, '2026-05-18', 'Hadir'),
(4, '2026-05-19', 'Hadir'),
(4, '2026-05-20', 'Hadir'),
(4, '2026-05-21', 'Hadir'),
(4, '2026-05-22', 'Hadir'),
(4, '2026-05-23', 'Hadir'),
(4, '2026-05-24', 'Hadir'),
(4, '2026-05-25', 'Hadir'),
(4, '2026-05-26', 'Sakit'),
(4, '2026-05-27', 'Hadir'),
(4, '2026-05-28', 'Hadir'),
(4, '2026-05-29', 'Alpha'),

-- Pegawai 5
(5, '2026-05-01', 'Hadir'),
(5, '2026-05-02', 'Alpha'),
(5, '2026-05-03', 'Hadir'),
(5, '2026-05-04', 'Hadir'),
(5, '2026-05-05', 'Hadir'),
(5, '2026-05-06', 'Hadir'),
(5, '2026-05-07', 'Hadir'),
(5, '2026-05-08', 'Hadir'),
(5, '2026-05-09', 'Izin'),
(5, '2026-05-10', 'Hadir'),
(5, '2026-05-11', 'Hadir'),
(5, '2026-05-12', 'Hadir'),
(5, '2026-05-13', 'Hadir'),
(5, '2026-05-14', 'Hadir'),
(5, '2026-05-15', 'Sakit'),
(5, '2026-05-16', 'Hadir'),
(5, '2026-05-17', 'Hadir'),
(5, '2026-05-18', 'Hadir'),
(5, '2026-05-19', 'Hadir'),
(5, '2026-05-20', 'Hadir'),
(5, '2026-05-21', 'Hadir'),
(5, '2026-05-22', 'Alpha'),
(5, '2026-05-23', 'Hadir'),
(5, '2026-05-24', 'Hadir'),
(5, '2026-05-25', 'Hadir'),
(5, '2026-05-26', 'Hadir'),
(5, '2026-05-27', 'Hadir'),
(5, '2026-05-28', 'Hadir'),
(5, '2026-05-29', 'Hadir'),

-- Pegawai 6
(6, '2026-05-01', 'Hadir'),
(6, '2026-05-02', 'Hadir'),
(6, '2026-05-03', 'Hadir'),
(6, '2026-05-04', 'Sakit'),
(6, '2026-05-05', 'Hadir'),
(6, '2026-05-06', 'Hadir'),
(6, '2026-05-07', 'Hadir'),
(6, '2026-05-08', 'Hadir'),
(6, '2026-05-09', 'Hadir'),
(6, '2026-05-10', 'Hadir'),
(6, '2026-05-11', 'Hadir'),
(6, '2026-05-12', 'Hadir'),
(6, '2026-05-13', 'Izin'),
(6, '2026-05-14', 'Hadir'),
(6, '2026-05-15', 'Hadir'),
(6, '2026-05-16', 'Hadir'),
(6, '2026-05-17', 'Hadir'),
(6, '2026-05-18', 'Hadir'),
(6, '2026-05-19', 'Hadir'),
(6, '2026-05-20', 'Alpha'),
(6, '2026-05-21', 'Hadir'),
(6, '2026-05-22', 'Hadir'),
(6, '2026-05-23', 'Hadir'),
(6, '2026-05-24', 'Hadir'),
(6, '2026-05-25', 'Hadir'),
(6, '2026-05-26', 'Sakit'),
(6, '2026-05-27', 'Hadir'),
(6, '2026-05-28', 'Hadir'),
(6, '2026-05-29', 'Hadir'),

-- Pegawai 7
(7, '2026-05-01', 'Hadir'),
(7, '2026-05-02', 'Alpha'),
(7, '2026-05-03', 'Hadir'),
(7, '2026-05-04', 'Hadir'),
(7, '2026-05-05', 'Hadir'),
(7, '2026-05-06', 'Hadir'),
(7, '2026-05-07', 'Hadir'),
(7, '2026-05-08', 'Hadir'),
(7, '2026-05-09', 'Hadir'),
(7, '2026-05-10', 'Izin'),
(7, '2026-05-11', 'Hadir'),
(7, '2026-05-12', 'Hadir'),
(7, '2026-05-13', 'Hadir'),
(7, '2026-05-14', 'Hadir'),
(7, '2026-05-15', 'Hadir'),
(7, '2026-05-16', 'Sakit'),
(7, '2026-05-17', 'Hadir'),
(7, '2026-05-18', 'Hadir'),
(7, '2026-05-19', 'Hadir'),
(7, '2026-05-20', 'Hadir'),
(7, '2026-05-21', 'Hadir'),
(7, '2026-05-22', 'Alpha'),
(7, '2026-05-23', 'Hadir'),
(7, '2026-05-24', 'Hadir'),
(7, '2026-05-25', 'Hadir'),
(7, '2026-05-26', 'Hadir'),
(7, '2026-05-27', 'Hadir'),
(7, '2026-05-28', 'Hadir'),
(7, '2026-05-29', 'Hadir'),

-- Pegawai 8
(8, '2026-05-01', 'Hadir'),
(8, '2026-05-02', 'Hadir'),
(8, '2026-05-03', 'Hadir'),
(8, '2026-05-04', 'Hadir'),
(8, '2026-05-05', 'Izin'),
(8, '2026-05-06', 'Hadir'),
(8, '2026-05-07', 'Hadir'),
(8, '2026-05-08', 'Hadir'),
(8, '2026-05-09', 'Hadir'),
(8, '2026-05-10', 'Hadir'),
(8, '2026-05-11', 'Hadir'),
(8, '2026-05-12', 'Hadir'),
(8, '2026-05-13', 'Sakit'),
(8, '2026-05-14', 'Hadir'),
(8, '2026-05-15', 'Hadir'),
(8, '2026-05-16', 'Hadir'),
(8, '2026-05-17', 'Hadir'),
(8, '2026-05-18', 'Hadir'),
(8, '2026-05-19', 'Hadir'),
(8, '2026-05-20', 'Hadir'),
(8, '2026-05-21', 'Alpha'),
(8, '2026-05-22', 'Hadir'),
(8, '2026-05-23', 'Hadir'),
(8, '2026-05-24', 'Hadir'),
(8, '2026-05-25', 'Hadir'),
(8, '2026-05-26', 'Hadir'),
(8, '2026-05-27', 'Hadir'),
(8, '2026-05-28', 'Izin'),
(8, '2026-05-29', 'Hadir'),

-- Pegawai 9
(9, '2026-05-01', 'Hadir'),
(9, '2026-05-02', 'Hadir'),
(9, '2026-05-03', 'Hadir'),
(9, '2026-05-04', 'Hadir'),
(9, '2026-05-05', 'Hadir'),
(9, '2026-05-06', 'Hadir'),
(9, '2026-05-07', 'Hadir'),
(9, '2026-05-08', 'Sakit'),
(9, '2026-05-09', 'Hadir'),
(9, '2026-05-10', 'Hadir'),
(9, '2026-05-11', 'Hadir'),
(9, '2026-05-12', 'Hadir'),
(9, '2026-05-13', 'Hadir'),
(9, '2026-05-14', 'Hadir'),
(9, '2026-05-15', 'Hadir'),
(9, '2026-05-16', 'Hadir'),
(9, '2026-05-17', 'Hadir'),
(9, '2026-05-18', 'Alpha'),
(9, '2026-05-19', 'Hadir'),
(9, '2026-05-20', 'Hadir'),
(9, '2026-05-21', 'Hadir'),
(9, '2026-05-22', 'Hadir'),
(9, '2026-05-23', 'Hadir'),
(9, '2026-05-24', 'Izin'),
(9, '2026-05-25', 'Hadir'),
(9, '2026-05-26', 'Hadir'),
(9, '2026-05-27', 'Hadir'),
(9, '2026-05-28', 'Hadir'),
(9, '2026-05-29', 'Hadir'),

-- PEGAWAI 10
(10, '2026-05-01', 'Sakit'),
(10, '2026-05-02', 'Hadir'),
(10, '2026-05-03', 'Hadir'),
(10, '2026-05-04', 'Alpha'),
(10, '2026-05-05', 'Hadir'),
(10, '2026-05-06', 'Hadir'),
(10, '2026-05-07', 'Hadir'),
(10, '2026-05-08', 'Alpha'),
(10, '2026-05-09', 'Hadir'),
(10, '2026-05-10', 'Hadir'),
(10, '2026-05-11', 'Hadir'),
(10, '2026-05-12', 'Hadir'),
(10, '2026-05-13', 'Hadir'),
(10, '2026-05-14', 'Hadir'),
(10, '2026-05-15', 'Sakit'),
(10, '2026-05-16', 'Hadir'),
(10, '2026-05-17', 'Hadir'),
(10, '2026-05-18', 'Hadir'),
(10, '2026-05-19', 'Alpha'),
(10, '2026-05-20', 'Hadir'),
(10, '2026-05-21', 'Hadir'),
(10, '2026-05-22', 'Hadir'),
(10, '2026-05-23', 'Hadir'),
(10, '2026-05-24', 'Izin'),
(10, '2026-05-25', 'Hadir'),
(10, '2026-05-26', 'Hadir'),
(10, '2026-05-27', 'Hadir'),
(10, '2026-05-28', 'Hadir'),
(10, '2026-05-29', 'Hadir'),

-- PEGAWAI 11
(11, '2026-05-01', 'Hadir'),
(11, '2026-05-02', 'Hadir'),
(11, '2026-05-03', 'Izin'),
(11, '2026-05-04', 'Hadir'),
(11, '2026-05-05', 'Hadir'),
(11, '2026-05-06', 'Hadir'),
(11, '2026-05-07', 'Hadir'),
(11, '2026-05-08', 'Hadir'),
(11, '2026-05-09', 'Hadir'),
(11, '2026-05-10', 'Hadir'),
(11, '2026-05-11', 'Hadir'),
(11, '2026-05-12', 'Sakit'),
(11, '2026-05-13', 'Hadir'),
(11, '2026-05-14', 'Hadir'),
(11, '2026-05-15', 'Hadir'),
(11, '2026-05-16', 'Hadir'),
(11, '2026-05-17', 'Hadir'),
(11, '2026-05-18', 'Alpha'),
(11, '2026-05-19', 'Hadir'),
(11, '2026-05-20', 'Hadir'),
(11, '2026-05-21', 'Hadir'),
(11, '2026-05-22', 'Hadir'),
(11, '2026-05-23', 'Hadir'),
(11, '2026-05-24', 'Izin'),
(11, '2026-05-25', 'Hadir'),
(11, '2026-05-26', 'Hadir'),
(11, '2026-05-27', 'Hadir'),
(11, '2026-05-28', 'Hadir'),
(11, '2026-05-29', 'Hadir'),

-- PEGAWAI 12
(12, '2026-05-01', 'Hadir'),
(12, '2026-05-02', 'Alpha'),
(12, '2026-05-03', 'Hadir'),
(12, '2026-05-04', 'Hadir'),
(12, '2026-05-05', 'Sakit'),
(12, '2026-05-06', 'Hadir'),
(12, '2026-05-07', 'Hadir'),
(12, '2026-05-08', 'Hadir'),
(12, '2026-05-09', 'Hadir'),
(12, '2026-05-10', 'Hadir'),
(12, '2026-05-11', 'Hadir'),
(12, '2026-05-12', 'Hadir'),
(12, '2026-05-13', 'Hadir'),
(12, '2026-05-14', 'Izin'),
(12, '2026-05-15', 'Hadir'),
(12, '2026-05-16', 'Hadir'),
(12, '2026-05-17', 'Hadir'),
(12, '2026-05-18', 'Hadir'),
(12, '2026-05-19', 'Alpha'),
(12, '2026-05-20', 'Hadir'),
(12, '2026-05-21', 'Hadir'),
(12, '2026-05-22', 'Hadir'),
(12, '2026-05-23', 'Hadir'),
(12, '2026-05-24', 'Hadir'),
(12, '2026-05-25', 'Sakit'),
(12, '2026-05-26', 'Hadir'),
(12, '2026-05-27', 'Hadir'),
(12, '2026-05-28', 'Hadir'),
(12, '2026-05-29', 'Hadir'),

-- PEGAWAI 13
(13, '2026-05-01', 'Hadir'),
(13, '2026-05-02', 'Hadir'),
(13, '2026-05-03', 'Hadir'),
(13, '2026-05-04', 'Sakit'),
(13, '2026-05-05', 'Hadir'),
(13, '2026-05-06', 'Hadir'),
(13, '2026-05-07', 'Hadir'),
(13, '2026-05-08', 'Hadir'),
(13, '2026-05-09', 'Hadir'),
(13, '2026-05-10', 'Hadir'),
(13, '2026-05-11', 'Hadir'),
(13, '2026-05-12', 'Hadir'),
(13, '2026-05-13', 'Hadir'),
(13, '2026-05-14', 'Hadir'),
(13, '2026-05-15', 'Izin'),
(13, '2026-05-16', 'Hadir'),
(13, '2026-05-17', 'Hadir'),
(13, '2026-05-18', 'Hadir'),
(13, '2026-05-19', 'Alpha'),
(13, '2026-05-20', 'Hadir'),
(13, '2026-05-21', 'Hadir'),
(13, '2026-05-22', 'Hadir'),
(13, '2026-05-23', 'Hadir'),
(13, '2026-05-24', 'Hadir'),
(13, '2026-05-25', 'Hadir'),
(13, '2026-05-26', 'Sakit'),
(13, '2026-05-27', 'Hadir'),
(13, '2026-05-28', 'Hadir'),
(13, '2026-05-29', 'Hadir'),

-- PEGAWAI 14
(14, '2026-05-01', 'Hadir'),
(14, '2026-05-02', 'Sakit'),
(14, '2026-05-03', 'Hadir'),
(14, '2026-05-04', 'Hadir'),
(14, '2026-05-05', 'Alpha'),
(14, '2026-05-06', 'Hadir'),
(14, '2026-05-07', 'Hadir'),
(14, '2026-05-08', 'Hadir'),
(14, '2026-05-09', 'Hadir'),
(14, '2026-05-10', 'Hadir'),
(14, '2026-05-11', 'Hadir'),
(14, '2026-05-12', 'Izin'),
(14, '2026-05-13', 'Hadir'),
(14, '2026-05-14', 'Hadir'),
(14, '2026-05-15', 'Hadir'),
(14, '2026-05-16', 'Hadir'),
(14, '2026-05-17', 'Sakit'),
(14, '2026-05-18', 'Hadir'),
(14, '2026-05-19', 'Hadir'),
(14, '2026-05-20', 'Hadir'),
(14, '2026-05-21', 'Hadir'),
(14, '2026-05-22', 'Alpha'),
(14, '2026-05-23', 'Hadir'),
(14, '2026-05-24', 'Hadir'),
(14, '2026-05-25', 'Hadir'),
(14, '2026-05-26', 'Hadir'),
(14, '2026-05-27', 'Hadir'),
(14, '2026-05-28', 'Hadir'),
(14, '2026-05-29', 'Hadir'),

-- PEGAWAI 15
(15, '2026-05-01', 'Alpha'),
(15, '2026-05-02', 'Hadir'),
(15, '2026-05-03', 'Hadir'),
(15, '2026-05-04', 'Izin'),
(15, '2026-05-05', 'Hadir'),
(15, '2026-05-06', 'Hadir'),
(15, '2026-05-07', 'Hadir'),
(15, '2026-05-08', 'Hadir'),
(15, '2026-05-09', 'Hadir'),
(15, '2026-05-10', 'Hadir'),
(15, '2026-05-11', 'Hadir'),
(15, '2026-05-12', 'Sakit'),
(15, '2026-05-13', 'Hadir'),
(15, '2026-05-14', 'Hadir'),
(15, '2026-05-15', 'Hadir'),
(15, '2026-05-16', 'Hadir'),
(15, '2026-05-17', 'Hadir'),
(15, '2026-05-18', 'Alpha'),
(15, '2026-05-19', 'Hadir'),
(15, '2026-05-20', 'Hadir'),
(15, '2026-05-21', 'Hadir'),
(15, '2026-05-22', 'Hadir'),
(15, '2026-05-23', 'Hadir'),
(15, '2026-05-24', 'Izin'),
(15, '2026-05-25', 'Hadir'),
(15, '2026-05-26', 'Hadir'),
(15, '2026-05-27', 'Hadir'),
(15, '2026-05-28', 'Hadir'),
(15, '2026-05-29', 'Hadir');

-- Anti absen 2x
alter table project.absensi
add constraint uq_absensi
unique
(id_pegawai,tanggal);

-- Cek Absen
select *
from project.absensi
where id_pegawai = ?
and tanggal = ?;

-- lihat absen pegawai
select *
from project.absensi
where id_pegawai = ?
order by tanggal;

update project.absensi
set status = ? 
where id_absensi = ?;

-- Hapus 
delete from project.absensi
where id_pegawai = ?
and tanggal = ?;

-- Rekap Absensi
select 
    p.id_pegawai,
    p.nama_pegawai,
count(
      case
      when a.status = 'Hadir'
      then 1
      end
     ) as hadir
from project.pegawai p
join project.absensi a
on p.id_pegawai = a.id_pegawai

where extract(
    month from a.tanggal) = ?
and extract(
    year from a.tanggal) = ?

group by
    p.id_pegawai,
    p.nama_pegawai;

select * from project.absensi
order by id_absensi;

select count(*)
from project.absensi
where id_pegawai = ?
and status = 'Alpha'

-- Cek sudah absen hari ini
select count(*)
from project.absensi
where id_pegawai = ?
and tanggal = ?;

create table project.cuti(
	id_cuti serial primary key,
	id_pegawai int,
    tanggal_mulai date,
    tanggal_selesai date,
    jumlah_hari int,
    alasan text,
	catatan varchar(100),
    status varchar(20) default 'Pending',
	tanggal_pengajuan date default current_date,
    foreign key (id_pegawai) references project.pegawai(id_pegawai)
);

insert into project.cuti(id_pegawai, tanggal_mulai, tanggal_selesai, alasan, jumlah_hari)
values
(?, ?, ?, ?, ?, 'PENDING');

select * from project.cuti;

-- Lihat Cuti
select *
from project.cuti
where id_pegawai = ?
order by id_cuti desc;

-- Lihat Cuti Pending
select *
from project.cuti
where status = 'PENDING'
order by id_cuti;

-- Approval Admin disetujui
update project.cuti
set status = 'APPROVED'
where id_cuti = ? ;

-- Approval Admin ditolak
update project.cuti
set status='REJECT'
catatan = ?
where id_cuti = ? ;

-- Tampil Data Cuti
select
    c.id_cuti,
    c.id_pegawai,
    p.nama_pegawai,
    c.tanggal_mulai,
    c.tanggal_selesai,
    c.alasan,
    c.status
from project.cuti c
join project.pegawai p
on c.id_pegawai = p.id_pegawai

ORDER BY c.id_cuti DESC;

-- Hapus 
delete from project.cuti
where id_cuti = ?;

create table project.tunjangan(
	id_tunjangan serial primary key,
	nama_tunjangan varchar(100),
	jumlah decimal (12,2)
);

insert into project.tunjangan (nama_tunjangan, jumlah)
values
('Tunjangan Transportasi', 500000),
('Tunjangan Makan', 900000),
('Tunjangan Jabatan', 1000000),
('Tunjangan Kesehatan', 2000000),
('Tunjangan Komunikasi', 500000),
('Tunjangan Hari Raya', 2000000),
('Tunjangan Keluarga', 200000);

select * from project.tunjangan;

create table project.potongan(
	id_potongan serial primary key,
	nama_potongan varchar(100),
	jumlah decimal(12,2)
);

insert into project.potongan (nama_potongan, jumlah)
values
('BPJS Kesehatan', 250000),
('BPJS Ketenagakerjaan', 300000),
('pajak PPh 21', 500000),
('Potongan Absensi', 20000),
('Pinjaman Karyawan', 5000000);

select * from project.potongan;

create table project.pegawai_tunjangan(
	id_pegawai_tunjangan serial primary key,
	id_pegawai int,
	id_tunjangan int,
	foreign key (id_pegawai) references project.pegawai(id_pegawai),
	foreign key (id_tunjangan) references project.tunjangan(id_tunjangan)
);

insert into project.pegawai_tunjangan (id_pegawai, id_tunjangan)
values
(?, ?);

alter table project.pegawai_tunjangan
add constraint uq_pt
unique(id_pegawai,id_tunjangan);

create view project.view_tunjangan_pegawai as
select
    pt.id_pegawai,
    t.nama_tunjangan,
    t.jumlah
from project.pegawai_tunjangan pt
join project.tunjangan t
on pt.id_tunjangan = t.id_tunjangan;

select * from project.pegawai_tunjangan;

create table project.pegawai_potongan(
	id_pegawai_potongan serial primary key,
	id_pegawai int,
	id_potongan int,
	jumlah decimal(12,2),
	foreign key(id_pegawai) references project.pegawai(id_pegawai),
	foreign key(id_potongan) references project.potongan(id_potongan)
);

insert into project.pegawai_potongan (id_pegawai, id_potongan)
values
(?, ?);

-- Update
update project.pegawai_potongan
set id_pegawai = ?,
    id_potongan = ?
where id_pegawai_potongan = ?;

-- Hapus
delete from project.pegawai_potongan
where id_pegawai_potongan = ?;

alter table project.pegawai_potongan
add constraint uq_pp
unique(id_pegawai,id_potongan);

select * from project.pegawai_potongan
order by id_pegawai_potongan;

create table project.kalender_libur (
	id_libur serial primary key,
	tanggal_libur date not null unique,
	nama_libur varchar (100),
	jenis_libur varchar (50)
);

insert into project.kalender_libur (tanggal_libur, nama_libur, jenis_libur)
values
('2026-05-01', 'Hari Buruh Nasional', 'Libur Nasional'),
('2026-05-02', 'Hari Pendidikan Nasional', 'Libur Nasional'),
('2026-05-14', 'Kenaikan Isa Al-Masih', 'Libur Nasional'),
('2026-05-15', 'Cuti Bersama Kenaikan Isa Al-Masih', 'Cuti Bersama'),
('2026-05-20', 'Hari Kebangkitan Nasional', 'Libur Nasional'),
('2026-05-27', 'Hari Raya Idul Adha', 'Libur Nasional'),
('2026-05-28', 'Cuti Bersama Idul Adha', 'Cuti Bersama'),
('2026-05-31', 'Hari Raya Waisak', 'Libur Nasional'),
('2026-06-01', 'Hari Lahir Pancasila', 'Libur Nasional'),
('2026-06-16', 'Tahun Baru Hijriah', 'Libur Nasional');

select * from project.kalender_libur;

-- Tampil 
select * from project.kalender_libur
order by id_libur;

-- Update
update project.kalender_libur
set nama_libur = ?
where id_libur = ?;

-- Hapus 
delete from project.kalender_libur
where id_libur = ?;

create table project.lembur(
    id_lembur serial primary key,
    id_pegawai int,
	tanggal_lembur date,
	jam_lembur int,
    jenis_hari varchar(50),
	upah_per_jam decimal(12,2),
	upah_lembur decimal(12,2),
    foreign key(id_pegawai) references project.pegawai(id_pegawai)  
    );

insert into project.lembur (id_pegawai, tanggal_lembur, jam_lembur)
values 
(?, ?, ?);

-- Tampil Lembur
select * from project.lembur
order by id_lembur;

-- Update
update project.lembur
set jam_lembur = ?
where id_lembur = ?;

-- Delete
delete from project.lembur
where id_lembur = 1 ;

create or replace function project.hitung_lembur()
returns trigger as
$$
begin

if extract(dow from new.tanggal_lembur)
between 1 and 5 then
    NEW.jenis_hari := 'Hari Biasa';

elsif extract(dow from new.tanggal_lembur)
in (0,6) then
    NEW.jenis_hari := 'Weekend';

end if;
if new.jenis_hari='Hari Biasa' then
	new.upah_per_jam:=15000;
elsif new.jenis_hari='Weekend' then
	new.upah_per_jam:=20000;
elsif new.jenis_hari='Libur Nasional' then
    new.upah_per_jam:=30000;

else

    raise exception
    'Jenis hari tidak valid!';

end if;

if new.jam_lembur>10 then
    new.upah_per_jam :=
    new.upah_per_jam + 10000;

end if;
new.upah_lembur =
new.jam_lembur
*
new.upah_per_jam;
return new;

end;
$$
language plpgsql;

-- Trigger
create trigger trg_hitung_lembur
before insert or update
on project.lembur
for each row
execute function
project.hitung_lembur();

update project.lembur
set jam_lembur = jam_lembur;

select
tanggal_lembur,
jam_lembur,
jenis_hari,
upah_per_jam,
upah_lembur
from project.lembur;

select * from project.lembur;

create table project.payroll(
	id_payroll serial primary key,
	id_pegawai int,
	periode varchar(50),
	gaji_pokok decimal(12,2),
	total_tunjangan decimal(12,2),
	total_potongan decimal(12,2),
	total_jam_lembur int,
	total_upah_lembur decimal(12,2),
	total_gaji decimal(12,2),
	tanggal_generate timestamp default current_timestamp,
	foreign key(id_pegawai) references project.pegawai(id_pegawai)
);

insert into project.payroll (id_pegawai, periode, gaji_pokok, total_tunjangan, total_potongan, total_jam_lembur, total_upah_lembur, total_gaji)
values
(?, ?, ?, ?, ?, ?, ?, ?);

select * from project.payroll;

-- Gaji Pokok berdasarkan Id_pegawai
select j.gaji_pokok
from project.pegawai p
join project.jabatan j
on p.id_jabatan=j.id_jabatan
where p.id_pegawai=?;

-- Total Lembur
select
coalesce(sum(jam_lembur),0) as total_jam,
coalesce(sum(upah_lembur),0) as total_upah
from project.lembur
where id_pegawai=?;

-- Alpha Pegawai
select count(*) as total_alpha
from project.absensi
where id_pegawai=?
and status='Alpha';

-- Ambil nomimal Alpha
select jumlah
from project.potongan
where nama_potongan = 'Potongan Absensi';

-- Tampil
select * from project.payroll
order by id_payroll;

-- Update
update project.payroll
set total_gaji = ?
where id_payroll = ?;

delete from project.payroll 
where id_pegawai = ?;

-- Menghitung payroll otomatis
create or replace function project.hitung_total_gaji()
returns trigger as
$$
begin

NEW.total_gaji :=
    coalesce(new.gaji_pokok,0)
    + coalesce(new.total_tunjangan,0)
    + coalesce(new.total_upah_lembur,0)
    - coalesce(new.total_potongan,0);

return new;

end;
$$
language plpgsql;

-- Trigger
create trigger trg_total_gaji
before insert or update
on project.payroll
for each row
execute function
project.hitung_total_gaji();

drop view project.view_slip_gaji;

create or replace view project.view_slip_gaji as
select 
    p.nama_pegawai,
	d.nama_departemen,
    j.nama_jabatan,
	pr.id_pegawai,
    pr.id_payroll,
    pr.periode,
    coalesce(pr.gaji_pokok, 0) as gaji_pokok,
    coalesce(pr.total_gaji, 0) as total_gaji
from project.payroll pr
left join project.pegawai p
    on pr.id_pegawai = p.id_pegawai
left join project.jabatan j
    on p.id_jabatan = j.id_jabatan
left join project.departemen d
    on j.id_departemen = d.id_departemen;

select * from project.view_slip_gaji
where nama_pegawai = 'Nailah Rizki';

-- Slip gaji detail
select tanggal_lembur,
       jam_lembur,
	   jenis_hari,
	   upah_lembur
from project.lembur
where id_pegawai=?
order by tanggal_lembur;

-- Tunjangan Dipilih
select
    t.nama_tunjangan,
    t.jumlah
from project.pegawai_tunjangan pt
join project.tunjangan t
    on pt.id_tunjangan = t.id_tunjangan
where pt.id_pegawai = ?;

-- Potongan dipilih
select p.nama_potongan,
       p.jumlah
from project.pegawai_potongan pp
join project.potongan p
    on pp.id_potongan = p.id_potongan
where pp.id_pegawai = ?;

-- Rincian Lembur
select tanggal_lembur,
       jam_lembur,
       upah_lembur
from project.lembur
where id_pegawai = ?;

delete from project.payroll
where total_gaji is null;

create table project.laporan_payroll(
	id_laporan serial primary key,
	periode varchar(50),
	tanggal_cetak timestamp default current_timestamp,
	total_pengeluaran numeric(15,2),
	keterangan varchar(100)
);

insert into project.laporan_payroll (periode, total_pengeluaran, keterangan)
values
(?, ?, ?);

-- Laporan Payroll semua
select p.nama_pegawai,
j.nama_jabatan,
pr.periode,
pr.total_gaji
from project.payroll pr
join project.pegawai p
on pr.id_pegawai=p.id_pegawai
join project.jabatan j
on p.id_jabatan=j.id_jabatan
order by pr.id_payroll;

-- Laporan Periode
select p.nama_pegawai,
pr.total_gaji
from project.payroll pr
join project.pegawai p
on pr.id_pegawai=p.id_pegawai
where pr.periode=?;

-- Total Pengeluaran Gaji
select sum(total_gaji) as total
from project.payroll
where periode = ?;

-- Cetak Laporan Payroll
select
    p.id_payroll,
    pg.nama_pegawai,
    j.nama_jabatan,
    p.periode,
    p.total_gaji
from project.payroll p
join project.pegawai pg
on p.id_pegawai = pg.id_pegawai
join project.jabatan j
on pg.id_jabatan = j.id_jabatan
order by p.id_payroll;

-- Hapus Laporan
delete from project.laporan
where id_laporan = ?;

create or replace view
project.view_laporan_payroll
as
select
	pr.id_payroll,
	p.nama_pegawai,
	j.nama_jabatan,
	d.nama_departemen,
	pr.periode,
	pr.gaji_pokok,
	pr.total_tunjangan,
	pr.total_potongan,
	pr.total_jam_lembur,
	pr.total_upah_lembur,
	pr.total_gaji
	from project.payroll pr
	
	join project.pegawai p
	on pr.id_pegawai=p.id_pegawai
	
	join project.jabatan j
	on p.id_jabatan=j.id_jabatan

	join project.departemen d
	on j.id_departemen=d.id_departemen;

select * 
from project.view_laporan_payroll
where periode = ?;


-- Filter & Sorting Data

-- mencari data pegawai harus sesuai
select *
from project.pegawai
where nama_pegawai like '%Veronica%';

-- Menampilkan total_pegawai
select count(*) as total_pegawai
from project.pegawai;

-- Rata rata gaji pokok
select *
from project.jabatan
where gaji_pokok > (
	select avg(gaji_pokok)
	from project.jabatan
);

-- Total gaji tertinggi"
select *
from project.jabatan
where gaji_pokok = (
	select max(gaji_pokok)
	from project.jabatan
);

-- Total gaji terendah"
select *
from project.jabatan
where gaji_pokok = (
	select min(gaji_pokok)
	from project.jabatan
);

-- Sorting data/ mengurutkan gaji dari max ke min
select *
from project.jabatan
order by gaji_pokok desc;

-- menghitung jumlah pegawai per departemen
select d.nama_departemen,
	count(p.id_pegawai) as total_pegawai
from project.departemen d
join project.jabatan j
on d.id_departemen = j.id_departemen
join project.pegawai p
on j.id_jabatan = p.id_jabatan
group by d.id_departemen, d.nama_departemen
order by d.id_departemen asc;

-- Join
select p.nama_pegawai,
	   j.nama_jabatan,
	   d.nama_departemen,
	   j.gaji_pokok
from project.pegawai p
join project.jabatan j
	on p.id_jabatan = j.id_jabatan
join project.departemen d
	on j.id_departemen = d.id_departemen;

-- Agregrasi
select count(*) as total_pegawai
from project.pegawai;

select avg(gaji_pokok) as rata_rata_gaji
from project.jabatan;

select max(gaji_pokok) as gaji_tertinggi
from project.jabatan;

select min(gaji_pokok) as gaji_terendah
from project.jabatan;

-- group by
select
	d.nama_departemen,
	count(p.id_pegawai) as jumlah_pegawai
from project.departemen d
join project.jabatan j
	on d.id_departemen = j.id_departemen
join project.pegawai p
	on j.id_jabatan = p.id_jabatan
group by d.nama_departemen;

-- Menampilkan rata" gaji_pokok dan nama_jabatan nya apa
select nama_jabatan, gaji_pokok
from project.jabatan
where gaji_pokok > (
	select avg(gaji_pokok)
	from project.jabatan
);

-- Transaction
begin;

-- Kurangi gaji pegawai 
update project.payroll
set total_gaji = total_gaji - 100000
where id_pegawai = 1;

rollback;

-- Tambah gaji pegawai
update project.payroll
set total_gaji = total_gaji + 100000
where id_pegawai = 2;

commit;

-- pegawai paling rajin
select
	p.id_pegawai,
	p.nama_pegawai,
	count(a.id_absensi) as total_hadir
from project.pegawai p
join project.absensi a
on p.id_pegawai = a.id_pegawai
where a.status = 'Hadir'
group by p.id_pegawai, p.nama_pegawai
order by total_hadir desc
limit 3;


