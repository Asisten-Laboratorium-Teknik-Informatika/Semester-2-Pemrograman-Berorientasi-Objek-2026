create schema event_platform;

create extension if not exists "pgcrypto";

create type user_role as enum ('customer', 'organizer', 'admin', 'staff');

--user
create table event_platform.users (
    user_id serial primary key,
    name varchar(50) not null,
    email varchar(50) not null unique,
    password_hash text not null,
    phone varchar(20),
    role user_role not null default 'customer',
    created_at timestamp not null default now(),
    updated_at timestamp not null default now()
);
select * from event_platform.users;

create or replace function set_updated_at()
returns trigger as $$
begin
	new.updated_at = now();
	return new;
end;
$$ language plpgsql;

create trigger users_updated_at
    before update on event_platform.users 
    for each row execute function set_updated_at();

--user admin
insert into event_platform.users(name, email, password_hash, phone, role) values
('Andi Saputra', 'andi.saputra@admin.id', crypt('Admin@123', gen_salt('bf')), '081234567890', 'admin'),
('Budi Santoso', 'budi.santoso@admin.id', crypt('Admin@345', gen_salt('bf')), '089878671527', 'admin');

--user organizer
insert into event_platform.users (name, email, password_hash, phone, role) values
('Budi Sentosa', 'budi.org@org.id', crypt('Org@7890', gen_salt('bf')), '081200000001', 'organizer'),
('Dewi Rahayu',  'dewi.org@org.id', crypt('Org@0000', gen_salt('bf')), '081200000002', 'organizer'),
('Eko Prasetyo', 'eko.org@org.id',  crypt('Org@9999', gen_salt('bf')), '081200000003', 'organizer');

--staff
insert into event_platform.users (name, email, password_hash, phone, role) values
('Fandi Ahmad',     'fandi.staff@staff.id',   crypt('Staff@523', gen_salt('bf')), '08160000001', 'staff'),
('Gita Pratiwi',    'gita.staff@staff.id',    crypt('Staff@187', gen_salt('bf')), '08160000002', 'staff'),
('Hamdan Fuadi',    'hamdan.staff@staff.id',  crypt('Staff@902', gen_salt('bf')), '08160000003', 'staff'),
('Irma Suryani',    'irma.staff@staff.id',    crypt('Staff@341', gen_salt('bf')), '08160000004', 'staff'),
('Junaedi Saleh',   'junaedi.staff@staff.id', crypt('Staff@234', gen_salt('bf')), '08160000005', 'staff'),
('Kartika Sari',    'kartika.staff@staff.id', crypt('Staff@567', gen_salt('bf')), '08160000006', 'staff'),
('Luthfi Hasan',    'luthfi.staff@staff.id',  crypt('Staff@890', gen_salt('bf')), '08160000007', 'staff'),
('Marlina Dwi',     'marlina.staff@staff.id', crypt('Staff@321', gen_salt('bf')), '08160000008', 'staff'),
('Nanang Sugito',   'nanang.staff@staff.id',  crypt('Staff@523', gen_salt('bf')), '08160000009', 'staff'),
('Oky Ramadan',     'oky.staff@staff.id',     crypt('Staff@131', gen_salt('bf')), '08160000010', 'staff'),
('Pandu Wicaksono', 'pandu.staff@staff.id',   crypt('Staff@576', gen_salt('bf')), '08160000011', 'staff'),
('Qisthi Amira',    'qisthi.staff@staff.id',  crypt('Staff@444', gen_salt('bf')), '08160000012', 'staff');

--regular cust(70)
insert into event_platform.users (name, email, password_hash, phone, role) values
('Agus Mulyadi',      'agus.mulyadi@mail.id',   crypt('Pass@1234', gen_salt('bf')), '08130000001', 'customer'),
('Siti Nuraeni',      'siti.nuraeni@mail.id',   crypt('Pass@1234', gen_salt('bf')), '08130000002', 'customer'),
('Bambang Suroso',    'bambang.suroso@mail.id', crypt('Pass@1234', gen_salt('bf')), '08130000003', 'customer'),
('Rina Susanti',      'rina.susanti@mail.id',   crypt('Pass@1234', gen_salt('bf')), '08130000004', 'customer'),
('Doni Hermawan',     'doni.hermawan@mail.id',  crypt('Pass@1234', gen_salt('bf')), '08130000005', 'customer'),
('Lestari Wulandari', 'lestari.wulan@mail.id',  crypt('Pass@1234', gen_salt('bf')), '08130000006', 'customer'),
('Hendra Wijaya',     'hendra.wijaya@mail.id',  crypt('Pass@1234', gen_salt('bf')), '08130000007', 'customer'),
('Maya Indah',        'maya.indah@mail.id',     crypt('Pass@1234', gen_salt('bf')), '08130000008', 'customer'),
('Rizky Pratama',     'rizky.pratama@mail.id',  crypt('Pass@1234', gen_salt('bf')), '08130000009', 'customer'),
('Ninda Permata',     'ninda.permata@mail.id',  crypt('Pass@1234', gen_salt('bf')), '08130000010', 'customer'),
('Fajar Nugroho',     'fajar.nugroho@mail.id',  crypt('Pass@1234', gen_salt('bf')), '08130000011', 'customer'),
('Rini Handayani',    'rini.handayani@mail.id', crypt('Pass@1234', gen_salt('bf')), '08130000012', 'customer'),
('Andi Saputra',      'andi.saputra@mail.id',   crypt('Pass@1234', gen_salt('bf')), '08130000013', 'customer'),
('Tini Marlina',      'tini.marlina@mail.id',   crypt('Pass@1234', gen_salt('bf')), '08130000014', 'customer'),
('Wahyu Hidayat',     'wahyu.hidayat@mail.id',  crypt('Pass@1234', gen_salt('bf')), '08130000015', 'customer'),
('Elsa Kurniawati',   'elsa.kurniawati@mail.id',crypt('Pass@1234', gen_salt('bf')), '08130000016', 'customer'),
('Joko Purnomo',      'joko.purnomo@mail.id',   crypt('Pass@1234', gen_salt('bf')), '08130000017', 'customer'),
('Yuli Astuti',       'yuli.astuti@mail.id',    crypt('Pass@1234', gen_salt('bf')), '08130000018', 'customer'),
('Aris Setiawan',     'aris.setiawan@mail.id',  crypt('Pass@1234', gen_salt('bf')), '08130000019', 'customer'),
('Nurul Hidayah',     'nurul.hidayah@mail.id',  crypt('Pass@1234', gen_salt('bf')), '08130000020', 'customer'),
('Bayu Firmansyah',   'bayu.firmansyah@mail.id',crypt('Pass@1234', gen_salt('bf')), '08130000021', 'customer'),
('Sari Dewi',         'sari.dewi@mail.id',      crypt('Pass@1234', gen_salt('bf')), '08130000022', 'customer'),
('Dimas Arjuna',      'dimas.arjuna@mail.id',   crypt('Pass@1234', gen_salt('bf')), '08130000023', 'customer'),
('Putri Anggraini',   'putri.anggraini@mail.id',crypt('Pass@1234', gen_salt('bf')), '08130000024', 'customer'),
('Galih Kusuma',      'galih.kusuma@mail.id',   crypt('Pass@1234', gen_salt('bf')), '08130000025', 'customer'),
('Fitri Handayani',   'fitri.handayani@mail.id',crypt('Pass@1234', gen_salt('bf')), '08130000026', 'customer'),
('Rendy Prasetya',    'rendy.prasetya@mail.id', crypt('Pass@1234', gen_salt('bf')), '08130000027', 'customer'),
('Anisa Rahma',       'anisa.rahma@mail.id',    crypt('Pass@1234', gen_salt('bf')), '08130000028', 'customer'),
('Teguh Santoso',     'teguh.santoso@mail.id',  crypt('Pass@1234', gen_salt('bf')), '08130000029', 'customer'),
('Winda Lestari',     'winda.lestari@mail.id',  crypt('Pass@1234', gen_salt('bf')), '08130000030', 'customer'),
('Ridwan Fauzi',      'ridwan.fauzi@mail.id',   crypt('Pass@1234', gen_salt('bf')), '08130000031', 'customer'),
('Mega Susanti',      'mega.susanti@mail.id',   crypt('Pass@1234', gen_salt('bf')), '08130000032', 'customer'),
('Haris Munandar',    'haris.munandar@mail.id', crypt('Pass@1234', gen_salt('bf')), '08130000033', 'customer'),
('Della Safira',      'della.safira@mail.id',   crypt('Pass@1234', gen_salt('bf')), '08130000034', 'customer'),
('Surya Dinata',      'surya.dinata@mail.id',   crypt('Pass@1234', gen_salt('bf')), '08130000035', 'customer'),
('Ratna Dewi',        'ratna.dewi@mail.id',     crypt('Pass@1234', gen_salt('bf')), '08130000036', 'customer'),
('Gilang Permadi',    'gilang.permadi@mail.id', crypt('Pass@1234', gen_salt('bf')), '08130000037', 'customer'),
('Tika Rahmawati',    'tika.rahmawati@mail.id', crypt('Pass@1234', gen_salt('bf')), '08130000038', 'customer'),
('Yusuf Effendi',     'yusuf.effendi@mail.id',  crypt('Pass@1234', gen_salt('bf')), '08130000039', 'customer'),
('Aini Maulida',      'aini.maulida@mail.id',   crypt('Pass@1234', gen_salt('bf')), '08130000040', 'customer'),
('Evan Setiaji',      'evan.setiaji@mail.id',   crypt('Pass@1234', gen_salt('bf')), '08130000041', 'customer'),
('Novi Andriani',     'novi.andriani@mail.id',  crypt('Pass@1234', gen_salt('bf')), '08130000042', 'customer'),
('Adnan Maulana',     'adnan.maulana@mail.id',  crypt('Pass@1234', gen_salt('bf')), '08130000043', 'customer'),
('Prita Hapsari',     'prita.hapsari@mail.id',  crypt('Pass@1234', gen_salt('bf')), '08130000044', 'customer'),
('Fikri Hamdani',     'fikri.hamdani@mail.id',  crypt('Pass@1234', gen_salt('bf')), '08130000045', 'customer'),
('Reza Kurniawan',    'reza.kurniawan@mail.id', crypt('Pass@1234', gen_salt('bf')), '08130000046', 'customer'),
('Mira Oktaviani',    'mira.oktaviani@mail.id', crypt('Pass@1234', gen_salt('bf')), '08130000047', 'customer'),
('Aditya Prabowo',    'aditya.prabowo@mail.id', crypt('Pass@1234', gen_salt('bf')), '08130000048', 'customer'),
('Laila Sari',        'laila.sari@mail.id',     crypt('Pass@1234', gen_salt('bf')), '08130000049', 'customer'),
('Ihsan Habibie',     'ihsan.habibie@mail.id',  crypt('Pass@1234', gen_salt('bf')), '08130000050', 'customer'),
('Yeni Ratnasari',    'yeni.ratnasari@mail.id', crypt('Pass@1234', gen_salt('bf')), '08130000051', 'customer'),
('Farel Gunawan',     'farel.gunawan@mail.id',  crypt('Pass@1234', gen_salt('bf')), '08130000052', 'customer'),
('Cantika Novia',     'cantika.novia@mail.id',  crypt('Pass@1234', gen_salt('bf')), '08130000053', 'customer'),
('Mulyono Agung',     'mulyono.agung@mail.id',  crypt('Pass@1234', gen_salt('bf')), '08130000054', 'customer'),
('Indah Puspitasari', 'indah.puspita@mail.id',  crypt('Pass@1234', gen_salt('bf')), '08130000055', 'customer'),
('Zulkifli Rahman',   'zulkifli.rahman@mail.id',crypt('Pass@1234', gen_salt('bf')), '08130000056', 'customer'),
('Wulan Sari',        'wulan.sari@mail.id',     crypt('Pass@1234', gen_salt('bf')), '08130000057', 'customer'),
('Prayogi Santosa',   'prayogi.santosa@mail.id',crypt('Pass@1234', gen_salt('bf')), '08130000058', 'customer'),
('Diana Permata',     'diana.permata@mail.id',  crypt('Pass@1234', gen_salt('bf')), '08130000059', 'customer'),
('Hasbi Assidiq',     'hasbi.assidiq@mail.id',  crypt('Pass@1234', gen_salt('bf')), '08130000060', 'customer'),
('Nisa Amalia',       'nisa.amalia@mail.id',    crypt('Pass@1234', gen_salt('bf')), '08130000061', 'customer'),
('Thoriq Aziz',       'thoriq.aziz@mail.id',    crypt('Pass@1234', gen_salt('bf')), '08130000062', 'customer'),
('Siska Mutiara',     'siska.mutiara@mail.id',  crypt('Pass@1234', gen_salt('bf')), '08130000063', 'customer'),
('Okky Pratama',      'okky.pratama@mail.id',   crypt('Pass@1234', gen_salt('bf')), '08130000064', 'customer'),
('Yolanda Putri',     'yolanda.putri@mail.id',  crypt('Pass@1234', gen_salt('bf')), '08130000065', 'customer'),
('Rangga Adiputra',   'rangga.adiputra@mail.id',crypt('Pass@1234', gen_salt('bf')), '08130000066', 'customer'),
('Aulia Rachman',     'aulia.rachman@mail.id',  crypt('Pass@1234', gen_salt('bf')), '08130000067', 'customer'),
('Boby Irawan',       'boby.irawan@mail.id',    crypt('Pass@1234', gen_salt('bf')), '08130000068', 'customer'),
('Nia Ambarwati',     'nia.ambarwati@mail.id',  crypt('Pass@1234', gen_salt('bf')), '08130000069', 'customer'),
('Arya Budiman',      'arya.budiman@mail.id',   crypt('Pass@1234', gen_salt('bf')), '08130000070', 'customer');

--vip cust(20)
insert into event_platform.users (name, email, password_hash, phone, role) values
('Bintang Ramadhan',  'bintang.vip@mail.id',    crypt('Pass@1234', gen_salt('bf')), '08140000001', 'customer'),
('Cindy Wijayanti',   'cindy.vip@mail.id',      crypt('Pass@1234', gen_salt('bf')), '08140000002', 'customer'),
('Daffa Ardiansyah',  'daffa.vip@mail.id',      crypt('Pass@1234', gen_salt('bf')), '08140000003', 'customer'),
('Erline Kusuma',     'erline.vip@mail.id',     crypt('Pass@1234', gen_salt('bf')), '08140000004', 'customer'),
('Farel Darmawan',    'farel.vip@mail.id',      crypt('Pass@1234', gen_salt('bf')), '08140000005', 'customer'),
('Gina Pramesti',     'gina.vip@mail.id',       crypt('Pass@1234', gen_salt('bf')), '08140000006', 'customer'),
('Hilman Syarif',     'hilman.vip@mail.id',     crypt('Pass@1234', gen_salt('bf')), '08140000007', 'customer'),
('Intan Maharani',    'intan.vip@mail.id',      crypt('Pass@1234', gen_salt('bf')), '08140000008', 'customer'),
('Jefri Gunawan',     'jefri.vip@mail.id',      crypt('Pass@1234', gen_salt('bf')), '08140000009', 'customer'),
('Karina Setyowati',  'karina.vip@mail.id',     crypt('Pass@1234', gen_salt('bf')), '08140000010', 'customer'),
('Lukman Hakim',      'lukman.vip@mail.id',     crypt('Pass@1234', gen_salt('bf')), '08140000011', 'customer'),
('Meilani Putri',     'meilani.vip@mail.id',    crypt('Pass@1234', gen_salt('bf')), '08140000012', 'customer'),
('Naldi Sugiarto',    'naldi.vip@mail.id',      crypt('Pass@1234', gen_salt('bf')), '08140000013', 'customer'),
('Oktafia Sari',      'oktafia.vip@mail.id',    crypt('Pass@1234', gen_salt('bf')), '08140000014', 'customer'),
('Panji Wibisono',    'panji.vip@mail.id',      crypt('Pass@1234', gen_salt('bf')), '08140000015', 'customer'),
('Qorina Hidayati',   'qorina.vip@mail.id',     crypt('Pass@1234', gen_salt('bf')), '08140000016', 'customer'),
('Rafi Zulhilmi',     'rafi.vip@mail.id',       crypt('Pass@1234', gen_salt('bf')), '08140000017', 'customer'),
('Salwa Nabila',      'salwa.vip@mail.id',      crypt('Pass@1234', gen_salt('bf')), '08140000018', 'customer'),
('Triyana Putra',     'triyana.vip@mail.id',    crypt('Pass@1234', gen_salt('bf')), '08140000019', 'customer'),
('Utami Noviana',     'utami.vip@mail.id',      crypt('Pass@1234', gen_salt('bf')), '08140000020', 'customer');

--vvip cust(10)
insert into event_platform.users (name, email, password_hash, phone, role) values
('Valentino Ardi',    'valentino.vvip@mail.id', crypt('Pass@1234', gen_salt('bf')), '08150000001', 'customer'),
('Windy Elisa',       'windy.vvip@mail.id',     crypt('Pass@1234', gen_salt('bf')), '08150000002', 'customer'),
('Xandria Permata',   'xandria.vvip@mail.id',   crypt('Pass@1234', gen_salt('bf')), '08150000003', 'customer'),
('Yoga Mahardika',    'yoga.vvip@mail.id',       crypt('Pass@1234', gen_salt('bf')), '08150000004', 'customer'),
('Zahara Amira',      'zahara.vvip@mail.id',    crypt('Pass@1234', gen_salt('bf')), '08150000005', 'customer'),
('Alfarizi Sultan',   'alfarizi.vvip@mail.id',  crypt('Pass@1234', gen_salt('bf')), '08150000006', 'customer'),
('Berliana Citra',    'berliana.vvip@mail.id',  crypt('Pass@1234', gen_salt('bf')), '08150000007', 'customer'),
('Callista Reza',     'callista.vvip@mail.id',  crypt('Pass@1234', gen_salt('bf')), '08150000008', 'customer'),
('Darryl Chandra',    'darryl.vvip@mail.id',    crypt('Pass@1234', gen_salt('bf')), '08150000009', 'customer'),
('Elvira Santika',    'elvira.vvip@mail.id',    crypt('Pass@1234', gen_salt('bf')), '08150000010', 'customer');


--organizer
create type organizer_status as enum ('pending', 'active', 'suspended');

create table event_platform.organizers (
    organizer_id serial primary key,
    user_id int not null unique 
references event_platform.users(user_id) on delete cascade,
    organizer_name varchar(200) not null,
    status organizer_status not null default 'pending',
    verified_at timestamp,
    created_at timestamp not null default now()
);
select * from event_platform.organizers;

insert into event_platform.organizers (user_id, organizer_name, status, verified_at) values
(3, 'saha Entertainment',  'active',    NOW() - INTERVAL '60 days'),
(4, 'Dewi Event Organizer','active',    NOW() - INTERVAL '45 days'),
(5, 'Eko Production House','pending',   null);


--venue
create table event_platform.venues(
	venue_id serial primary key,
	venue_name varchar(100) not null,
	address text not null,
	city varchar (50) not null,
	capacity int not null 
check(capacity>0),
	maps text
);

insert into event_platform.venues (venue_name, address, city, capacity, maps) values
(
'Skyline Garden Amphitheater',
'Jl. Aurora No. 10, District Central Park',
'Eurazania',
250,
'https://maps.app.goo.gl/r9NiB9grVryd719v6'
);
select * from event_platform.venues;


--facility
create type facility_category as enum ('umum', 'teknis', 'keamanan');

create table event_platform.venue_facilities (
    facilities_id serial primary key,
    venue_id int not null 
references event_platform.venues(venue_id) on delete cascade,
    facility_name varchar(50) not null,
    description text,
    is_available  boolean not null default true,
    category facility_category not null
);

insert into event_platform.venue_facilities(venue_id, facility_name, description, is_available, category)values
--fasilitas umum
(1, 'Toilet','Tersedia 4 toilet (2 pria dan 2 wanita) di dalam gedung', TRUE, 'umum'),
(1, 'Parkir', 'Area parkir outdoor untuk ±80 kendaraan roda dua dan ±30 kendaraan roda empat',TRUE, 'umum'),
(1, 'AC / Ventilasi','Sistem AC sentral dengan kapasitas pendinginan untuk 200 orang',TRUE, 'umum'),
(1, 'Listrik / Power Supply','Panel listrik 33.000 VA dengan backup genset 20 kVA',TRUE, 'umum'),
(1, 'Internet / WiFi','Koneksi fiber 100 Mbps, tersedia hotspot di seluruh area gedung',TRUE, 'umum'),
-- Fasilitas Teknis
(1, 'Sound System','Speaker line array + subwoofer, mixing console Behringer X32',TRUE, 'teknis'),
(1, 'Lighting System','Moving head 8 unit + par LED 16 unit + hazer/fogger',TRUE, 'teknis'),
(1, 'LED Screen / Projector','LED screen 5×3 m di panggung utama + projector 10.000 lumens cadangan',TRUE, 'teknis'),
(1, 'Microphone','6 unit mic wireless Shure + 4 unit mic kabel',  TRUE, 'teknis'),
(1, 'CCTV','16 kamera CCTV full-HD mencakup seluruh area gedung & parkir, rekaman 30 hari',TRUE, 'teknis'),
-- Fasilitas Keamanan
(1, 'Security Post','Pos keamanan di pintu masuk utama dan belakang gedung, dijaga 24 jam',TRUE, 'keamanan'),
(1, 'Emergency Exit','3 pintu darurat dilengkapi tanda exit dan lampu darurat',TRUE, 'keamanan'),
(1, 'Fire Extinguisher (APAR)','10 unit APAR dry powder 6 kg tersebar di setiap sudut gedung, cek berkala',TRUE, 'keamanan'),
(1, 'First Aid / P3K','Kotak P3K lengkap + 1 unit tandu, penanggungjawab berlatih pertolongan pertama',TRUE,'keamanan');
select * from event_platform.venue_facilities;


--event
create type event_status as enum ('draft', 'published', 'ongoing', 'completed', 'cancelled');

create table event_platform.events (
    event_id serial primary key,
    organizer_id int not null 
references event_platform.organizers(organizer_id),
    venue_id int not null 
references event_platform.venues(venue_id),
    event_name varchar(255) not null,
    description text,
    event_date date not null,
    event_time time not null,
    status event_status not null default 'draft',
    created_at timestamp not null default now()
);

insert into event_platform.events(organizer_id, venue_id, event_name, description, event_date, event_time, status) values
(
1, 1,
'Midnight Harmony Fest — The Grand Music Experience',
'Festival musik tahunan dengan penampilan artis lokal dan nasional, bazar, dan area hiburan keluarga.',
'2050-12-20',
'16:00:00',
'published'
);
select * from event_platform.events;


--ticket
create type ticket_type as enum ('regular', 'vip', 'vvip');
create type ticket_status as enum ('available', 'sold_out', 'cancelled');

create table event_platform.tickets (
    ticket_id serial primary key,
    event_id int not null 
references event_platform.events(event_id),
    ticket_type ticket_type not null,
    price numeric(12,2) not null 
check (price >= 0),
    total_quota int not null 
check (total_quota > 0),
    sale_start timestamp not null,
    sale_end timestamp not null,
    status ticket_status not null default 'available',
    updated_at timestamp not null default now(),
check (sale_end > sale_start)
);

create or replace function event_platform.set_updated_at()
returns trigger
language plpgsql as $$
begin
    new.updated_at = now();
    return new;
end;
$$;

create trigger tickets_updated_at
    before update on event_platform.tickets
    for each row execute function event_platform.set_updated_at();

insert into event_platform.tickets (event_id, ticket_type, price, total_quota, sale_start, sale_end, status) values
(1, 'regular', 150000.00, 85, '2050-12-14 23:59:59','2050-12-18 23:59:59', 'available'),
(1, 'vip',     350000.00, 32, '2050-12-14 23:59:59','2050-12-18 23:59:59', 'available'),
(1, 'vvip',    750000.00, 13, '2050-12-14 23:59:59','2050-12-18 23:59:59', 'available');
select * from event_platform.tickets;


--ticket_benefit
create type ticket_benefit as enum ('akses', 'merchandise', 'konsumsi', 'fasilitas', 'eksklusif');

create table event_platform.ticket_benefits (
    benefit_id serial primary key,
    name varchar(50) not null,
    description text,
    category ticket_benefit not null,
    created_at  timestamp not null default now()
);

-- benefit ticket
insert into event_platform.ticket_benefits (name, description, category) values
('akses area regular', 'akses masuk ke zona penonton umum',               'akses'),
('akses area vip',     'akses masuk ke zona vip dengan kursi prioritas',  'akses'),
('akses area vvip',    'akses masuk ke zona vvip eksklusif paling depan', 'akses'),
('merchandise pack',   'tote bag, kaos, dan stiker eksklusif event',      'merchandise'),
('welcome drink',      'minuman selamat datang di lounge vvip',           'konsumsi'),
('snack box',          'snack box premium selama event berlangsung',      'konsumsi'),
('meet and greet',     'sesi foto dan tanda tangan bersama artis utama',  'eksklusif'),
('priority queue',     'jalur antrean prioritas di semua gerbang masuk',  'akses'),
('free parking',       'voucher parkir gratis 1 kendaraan',               'fasilitas'),
('lanyard eksklusif',  'lanyard edisi terbatas bertanda tangan artis',    'merchandise');
select * from event_platform.ticket_benefits;


create table event_platform.ticket_benefit_map (
    ticket_id  int not null 
references event_platform.tickets(ticket_id) on delete cascade,
    benefit_id int not null 
references event_platform.ticket_benefits(benefit_id) on delete cascade,
    primary key (ticket_id, benefit_id)
);

--regular ticket
insert into event_platform.ticket_benefit values 
(1, 1);

--vip ticket
insert into event_platform.ticket_benefit values 
(2, 2), 
(2, 4), 
(2, 8), 
(2, 9);

--vvip ticket
insert into event_platform.ticket_benefit values
(3, 3), 
(3, 4), 
(3, 5), 
(3, 6), 
(3, 7), 
(3, 8), 
(3, 9), 
(3, 10);
select * from event_platform.ticket_benefit;


--sponsor
create type sponsor_benefits as enum ('branding', 'booth', 'media', 'promosi', 'eksklusif');
create table event_platform.sponsor_benefits (
    benefit_id serial primary key,
    name varchar(50) not null,
    description text,
    category sponsor_benefits not null,
    created_at timestamp not null default now()
);

--benefit sponsor
insert into event_platform.sponsor_benefits (name, description, category) values
('logo di backdrop',     'logo perusahaan tampil di backdrop panggung utama',           'branding'),
('logo di semua media',  'logo tampil di banner, flyer, poster, dan x-banner event',    'branding'),
('logo di media sosial', 'mention dan tag di semua postingan media sosial event',       'media'),
('booth kecil',          'booth 2x2 m di area lobby untuk display produk',             'booth'),
('booth premium',        'booth 3x3 m di lokasi strategis dekat pintu masuk',          'booth'),
('mc mention',           'nama sponsor disebut oleh mc minimal 3x selama event',       'promosi'),
('branding di stage',    'logo dan nama sponsor tampil di layar led panggung utama',   'branding'),
('iklan di rundown',     'slot iklan 30 detik ditampilkan di layar led saat jeda',     'media'),
('naming rights',        'nama perusahaan menjadi bagian dari nama resmi event',       'eksklusif'),
('presentasi eksklusif', 'slot presentasi produk 10 menit di panggung',                'eksklusif'),
('goodie bag insert',    'materi promosi dimasukkan ke goodie bag peserta',            'promosi'),
('vip invitation',       'undangan vip untuk 5 perwakilan perusahaan sponsor',         'eksklusif');

create table event_platform.sponsor_tier (
    tier_id serial primary key,
    tier_name varchar(50)  not null,
    min_contribution numeric(15,2) not null,
    description text,
    created_at timestamp not null default now()
);

insert into event_platform.sponsor_tier (tier_name, min_contribution, description) values
('bronze',    5000000.00, 'sponsor tingkat pemula, logo pada backdrop dan media sosial'),
('silver',   15000000.00, 'sponsor menengah, logo pada semua media promosi + booth kecil'),
('gold',     35000000.00, 'sponsor utama, branding di stage + booth premium + mc mention'),
('platinum', 75000000.00, 'sponsor title, naming rights, presentasi eksklusif di event');

create table event_platform.sponsor_tier_benefit (
    tier_id int not null 
references event_platform.sponsor_tier(tier_id) on delete cascade,
    benefit_id int not null 
references event_platform.sponsor_benefits(benefit_id) on delete cascade,
    primary key (tier_id, benefit_id)
);

-- bronze benefit
insert into event_platform.sponsor_tier_benefit values 
(1,1),
(1,3),
(1,11);

--silver benefit
insert into event_platform.sponsor_tier_benefit values 
(2,1),
(2,2),
(2,3),
(2,4),
(2,11);

--gold benefit
insert into event_platform.sponsor_tier_benefit values
(3,1),
(3,2),
(3,3),
(3,5),
(3,6),
(3,7),
(3,8),
(3,11),
(3,12);

--platinum benefit
insert into event_platform.sponsor_tier_benefit values
(4,1),
(4,2),
(4,3),
(4,5),
(4,6),
(4,7),
(4,8),
(4,9),
(4,10),
(4,11),
(4,12);
select * from event_platform.sponsor_tier_benefit;


create table event_platform.event_sponsors (
    sponsor_id serial primary key,
    event_id int not null 
references event_platform.events(event_id),
    tier_id int not null 
references event_platform.sponsor_tier(tier_id),
    company_name varchar(50) not null,
    contact_person varchar(50) not null,
    contact_email varchar(50) not null,
    contribution_amount numeric(15,2) not null,
    start_at date not null,
    end_at date not null,
    status sponsor_status not null default 'pending',
    created_at timestamp not null default now(),
    updated_at timestamp not null default now()
);
 
create trigger event_sponsors_updated_at
    before update on event_platform.event_sponsors
    for each row execute function event_platform.set_updated_at();

insert into event_platform.event_sponsors (event_id, tier_id, company_name, contact_person, contact_email, contribution_amount, start_at, end_at, status) values
(1, 4, 'pt maju bersama tbk',   'hendri kusuma', 'hendri@majubersama.id', 80000000.00, '2050-11-01', '2050-12-31', 'active'),
(1, 3, 'cv berkah jaya',        'sinta dewi',    'sinta@berkahjaya.id',   40000000.00, '2050-11-05', '2050-12-31', 'active'),
(1, 2, 'ud semangat kita',      'rudi hartono',  'rudi@semangatk.id',     18000000.00, '2050-11-10', '2050-12-31', 'active'),
(1, 1, 'warung kopi nusantara', 'tono susilo',   'tono@kopinus.id',        6000000.00, '2050-11-15', '2050-12-31', 'active');
select * from event_platform.event_sponsors;


--event_staff
create type staff_role as enum ('checker', 'pic', 'usher', 'crew', 'security');

create table event_platform.event_staff (
    staff_id serial primary key,
    user_id int not null 
references event_platform.users(user_id),
    event_id int not null 
references event_platform.events(event_id),
    role staff_role not null,
    area varchar(50),
    assigned_at timestamp not null default now(),
    unique (user_id, event_id)
);

insert into event_platform.event_staff (user_id, event_id, role, area, assigned_at) values
(112, 1, 'crew',     'Stage Crew — Setup & Breakdown Panggung', '2050-12-20 07:00:00'),
(113, 1, 'crew',     'Technical Crew — Sound & Lighting',       '2050-12-20 07:00:00'),
(117, 1, 'crew',     'Logistik & Runner',                       '2050-12-20 07:00:00'),
(114, 1, 'security', 'Pos Keamanan Depan & Area Parkir',        '2050-12-20 07:00:00'),
(115, 1, 'security', 'Pos Keamanan Dalam Gedung',               '2050-12-20 07:00:00'),
(109, 1, 'pic',      'Koordinator Lapangan Keseluruhan',        '2050-12-20 12:00:00'),
(110, 1, 'usher',    'Zona Regular — Pengarah Penonton',        '2050-12-20 12:00:00'),
(111, 1, 'usher',    'Zona VIP/VVIP — Pengarah Penonton',       '2050-12-20 12:00:00'),
(116, 1, 'usher',    'Zona Bazar & Area Umum',                  '2050-12-20 12:00:00'),
(106, 1, 'checker',  'Gate A — Pintu Masuk Utama',              '2050-12-20 14:00:00'),
(107, 1, 'checker',  'Gate B — Pintu Samping Kiri', 			'2050-12-20 14:00:00'),
(108, 1, 'checker',  'Gate C — Pintu Samping Kanan',			'2050-12-20 14:00:00');
select * from event_platform.event_staff;


create type discount_type as enum ('percentage', 'fixed');
create type discount_status as enum ('active', 'inactive', 'expired');

create table event_platform.discounts (
    discount_id serial primary key,
    code varchar(50) not null unique,
    organizer_id int not null 
references event_platform.organizers(organizer_id),
    event_id int 
references event_platform.events(event_id),
    discount_type discount_type not null,
    discount_value numeric(10,2) not null check (discount_value > 0),
    min_purchase numeric(12,2) not null default 0,
    usage_limit int,
    status discount_status not null default 'active',
    start_date date not null,
    end_date date not null,
    check (end_date >= start_date)
);

insert into event_platform.discounts (code, organizer_id, event_id, discount_type, discount_value, min_purchase, usage_limit, status, start_date, end_date) values
('earlybird25',  1, 1, 'percentage', 25.00, 150000.00, 30,  'active',   '2050-12-15', '2050-12-16'),
('VIPFEST50K',   1, 1, 'fixed',      50000.00, 350000.00, 20, 'active',  '2050-12-15', '2050-12-16'),
('PEPEKFEST10',  1, 1, 'percentage', 10.00,  0.00,      100, 'active',   '2050-12-15', '2050-12-16'),
('STUDENT20',    1, 1, 'percentage', 20.00, 150000.00,  50,  'active',   '2050-12-15', '2050-12-16');
select * from event_platform.discounts;


create type booking_status as enum ('pending', 'confirmed', 'cancelled', 'expired');

create table event_platform.bookings (
    booking_id serial primary key,
    user_id int not null 
references event_platform.users(user_id),
    event_id int not null 
references event_platform.events(event_id),
    total_amount numeric(14,2) not null 
check (total_amount >= 0),
    discount_amount numeric(14,2) not null default 0 check (discount_amount >= 0),
    booking_at timestamp not null default now(),
    expired_at timestamp not null default now() + interval '30 minutes',
    status booking_status not null default 'pending',
);

alter table event_platform.bookings
    add column discount_id int references event_platform.discounts(discount_id);


insert into event_platform.bookings (user_id, event_id, total_amount, discount_amount, booking_at, expired_at, status) values
-- REGULAR
(6,  1, 150000.00, 0.00, TIMESTAMP '2050-12-14 10:00:00', TIMESTAMP '2050-12-14 10:30:00', 'confirmed'),
(8,  1, 150000.00, 0.00, TIMESTAMP '2050-12-16 10:00:00', TIMESTAMP '2050-12-16 10:30:00', 'confirmed'),
(9,  1, 150000.00, 0.00, TIMESTAMP '2050-12-17 10:00:00', TIMESTAMP '2050-12-17 10:30:00', 'confirmed'),
(10, 1, 150000.00, 0.00, TIMESTAMP '2050-12-18 10:00:00', TIMESTAMP '2050-12-18 10:30:00', 'confirmed'),

(11, 1, 150000.00, 0.00, TIMESTAMP '2050-12-14 11:00:00', TIMESTAMP '2050-12-14 11:30:00', 'confirmed'),
(12, 1, 150000.00, 0.00, TIMESTAMP '2050-12-15 11:00:00', TIMESTAMP '2050-12-15 11:30:00', 'confirmed'),
(13, 1, 150000.00, 0.00, TIMESTAMP '2050-12-16 11:00:00', TIMESTAMP '2050-12-16 11:30:00', 'confirmed'),
(14, 1, 150000.00, 0.00, TIMESTAMP '2050-12-17 11:00:00', TIMESTAMP '2050-12-17 11:30:00', 'confirmed'),
(15, 1, 150000.00, 0.00, TIMESTAMP '2050-12-18 11:00:00', TIMESTAMP '2050-12-18 11:30:00', 'confirmed'),

-- VIP
(76, 1, 350000.00, 0.00, TIMESTAMP '2050-12-14 12:00:00', TIMESTAMP '2050-12-14 12:30:00', 'confirmed'),
(77, 1, 350000.00, 0.00, TIMESTAMP '2050-12-15 12:00:00', TIMESTAMP '2050-12-15 12:30:00', 'confirmed'),
(78, 1, 350000.00, 0.00, TIMESTAMP '2050-12-16 12:00:00', TIMESTAMP '2050-12-16 12:30:00', 'confirmed'),
(79, 1, 350000.00, 0.00, TIMESTAMP '2050-12-17 12:00:00', TIMESTAMP '2050-12-17 12:30:00', 'confirmed'),
(80, 1, 350000.00, 0.00, TIMESTAMP '2050-12-18 12:00:00', TIMESTAMP '2050-12-18 12:30:00', 'confirmed'),

-- VVIP
(96, 1, 750000.00, 0.00, TIMESTAMP '2050-12-14 13:00:00', TIMESTAMP '2050-12-14 13:30:00', 'confirmed'),
(97, 1, 750000.00, 0.00, TIMESTAMP '2050-12-15 13:00:00', TIMESTAMP '2050-12-15 13:30:00', 'confirmed'),
(98, 1, 750000.00, 0.00, TIMESTAMP '2050-12-16 13:00:00', TIMESTAMP '2050-12-16 13:30:00', 'confirmed'),
(99, 1, 750000.00, 0.00, TIMESTAMP '2050-12-17 13:00:00', TIMESTAMP '2050-12-17 13:30:00', 'confirmed'),
(100,1, 750000.00, 0.00, TIMESTAMP '2050-12-18 13:00:00', TIMESTAMP '2050-12-18 13:30:00', 'confirmed');


create table event_platform.booking_details (
    booking_detail_id serial primary key,
    booking_id int not null 
references event_platform.bookings(booking_id) on delete cascade,
    ticket_id int not null 
references event_platform.tickets(ticket_id),
    quantity int not null 
check (quantity > 0),
    price_snapshot numeric(12,2) not null 
check (price_snapshot >= 0)
);

insert into event_platform.booking_details (booking_id, ticket_id, quantity, price_snapshot) values
(1,1,1,150000.00),
(2,1,1,150000.00),
(3,1,1,150000.00),
(4,1,1,150000.00),
(5,1,1,150000.00),
(6,1,1,150000.00),
(7,1,1,150000.00),
(8,1,1,150000.00),
(9,1,1,150000.00),
(10,1,1,150000.00),
(11,2,1,350000.00),
(12,2,1,350000.00),
(13,2,1,350000.00),
(14,2,1,350000.00),
(15,2,1,350000.00),
(16,3,1,750000.00),
(17,3,1,750000.00),
(18,3,1,750000.00),
(19,3,1,750000.00),
(20,3,1,750000.00);
select * from event_platform.booking_details;

create type payment_status as enum ('pending', 'paid', 'failed', 'refunded');

create table event_platform.payments (
    payment_id serial primary key,
    booking_id int not null unique 
references event_platform.bookings(booking_id),
    amount numeric(14,2) not null 
check (amount >= 0),
    status payment_status not null default 'pending',
    payment_method varchar(60) not null,
    created_at timestamp not null default now(),
    paid_at timestamp
);

insert into event_platform.payments (booking_id, amount, status, payment_method, paid_at) values
-- REGULAR (2050-12-14 - 2050-12-18)
(1,  150000.00, 'paid', 'transfer_bank', TIMESTAMP '2050-12-14 10:30:00'),
(2,  150000.00, 'paid', 'qris',          TIMESTAMP '2050-12-14 11:30:00'),
(3,  150000.00, 'paid', 'e_wallet',      TIMESTAMP '2050-12-15 10:30:00'),
(4,  150000.00, 'paid', 'transfer_bank', TIMESTAMP '2050-12-15 11:30:00'),
(5,  150000.00, 'paid', 'qris',          TIMESTAMP '2050-12-16 10:30:00'),
(6,  150000.00, 'paid', 'e_wallet',      TIMESTAMP '2050-12-16 11:30:00'),
(7,  150000.00, 'paid', 'transfer_bank', TIMESTAMP '2050-12-17 10:30:00'),
(8,  150000.00, 'paid', 'qris',          TIMESTAMP '2050-12-17 11:30:00'),
(9,  150000.00, 'paid', 'e_wallet',      TIMESTAMP '2050-12-18 10:30:00'),
(10, 150000.00, 'paid', 'transfer_bank', TIMESTAMP '2050-12-18 11:30:00'),

-- VIP
(11, 350000.00, 'paid', 'transfer_bank', TIMESTAMP '2050-12-14 12:30:00'),
(12, 350000.00, 'paid', 'qris',          TIMESTAMP '2050-12-15 12:30:00'),
(13, 350000.00, 'paid', 'e_wallet',      TIMESTAMP '2050-12-16 12:30:00'),
(14, 350000.00, 'paid', 'transfer_bank', TIMESTAMP '2050-12-17 12:30:00'),
(15, 350000.00, 'paid', 'qris',          TIMESTAMP '2050-12-18 12:30:00'),

-- VVIP
(16, 750000.00, 'paid', 'transfer_bank', TIMESTAMP '2050-12-14 13:30:00'),
(17, 750000.00, 'paid', 'transfer_bank', TIMESTAMP '2050-12-15 13:30:00'),
(18, 750000.00, 'paid', 'e_wallet',      TIMESTAMP '2050-12-16 13:30:00'),
(19, 750000.00, 'paid', 'qris',          TIMESTAMP '2050-12-17 13:30:00'),
(20, 750000.00, 'paid', 'transfer_bank', TIMESTAMP '2050-12-18 13:30:00');
select * from event_platform.payments;


create type refund_status as enum ('requested', 'approved', 'rejected', 'processed');

create table event_platform.refund (
    refund_id serial primary key,
    payment_id int not null 
references event_platform.payments(payment_id),
    amount numeric(14,2) not null check (amount > 0),
    reason text not null,
    status refund_status not null default 'requested',
    requested_at timestamptz   not null default now(),
    process_at timestamptz,
    rejection_note text,
    processed_by int 
references event_platform.users(user_id)
);

insert into event_platform.refund(payment_id, amount, reason, status, processed_by, process_at) values
(5, 150000.00, 'Pembatalan karena sakit mendadak', 'approved', 1, NOW() - INTERVAL '17 days');
select * from event_platform.refund;


create type issued_status as enum ('active', 'used', 'expired', 'cancelled');

create table event_platform.issued_tickets (
    issued_ticket_id serial primary key,
    booking_id int not null 
references event_platform.bookings(booking_id),
    ticket_code varchar(100)  not null unique,
    status issued_status not null default 'active',
    used_at timestamp,
    updated_at timestamp not null default now(),
    expired_at timestamp not null default '2050-12-21 00:00'
);
 
create trigger issued_tickets_updated_at
    before update on event_platform.issued_tickets
    for each row execute function set_updated_at();

insert into event_platform.issued_tickets (booking_id, ticket_code, status) values
-- REGULAR
(1,  'REG-2050-000001', 'active'),
(2,  'REG-2050-000002', 'active'),
(3,  'REG-2050-000003', 'active'),
(4,  'REG-2050-000004', 'active'),
(5,  'REG-2050-000005', 'cancelled'),
(6,  'REG-2050-000006', 'active'),
(7,  'REG-2050-000007', 'active'),
(8,  'REG-2050-000008', 'active'),
(9,  'REG-2050-000009', 'active'),
(10, 'REG-2050-000010', 'active'),
-- VIP
(11, 'VIP-2050-000001', 'active'),
(12, 'VIP-2050-000002', 'active'),
(13, 'VIP-2050-000003', 'active'),
(14, 'VIP-2050-000004', 'active'),
(15, 'VIP-2050-000005', 'active'),
-- VVIP
(16, 'VVIP-2050-00001', 'active'),
(17, 'VVIP-2050-00002', 'active'),
(18, 'VVIP-2050-00003', 'active'),
(19, 'VVIP-2050-00004', 'active'),
(20, 'VVIP-2050-00005', 'active');
select * from event_platform.issued_tickets;


create type checkin_method as enum ('qr_scan', 'manual', 'nfc');
create type checkin_status as enum('success', 'failed', 'duplicate');

create table event_platform.checkin (
    id serial primary key,
    issued_ticket_id int not null 
references event_platform.issued_tickets(issued_ticket_id),
    staff_id int not null 
references event_platform.event_staff(staff_id),
    method checkin_method not null default 'qr_scan',
    checked_at timestamp not null default now(),
    status checkin_status not null default 'success'
);

insert into event_platform.checkin (issued_ticket_id, staff_id, method, checked_at, status) values
(1,  1, 'qr_scan', NOW() - INTERVAL '1 hour', 'success'),
(2,  1, 'qr_scan', NOW() - INTERVAL '58 min', 'success'),
(3,  2, 'qr_scan', NOW() - INTERVAL '55 min', 'success'),
(11, 1, 'qr_scan', NOW() - INTERVAL '52 min', 'success'),
(16, 1, 'manual',  NOW() - INTERVAL '50 min', 'success');
select * from event_platform.checkin;


create type waitlist_status as enum ('waiting', 'notified', 'expired', 'converted');

create table event_platform.waitlist (
    waitlist_id serial primary key,
    user_id int not null 
references event_platform.users(user_id),
    ticket_id int not null 
references event_platform.tickets(ticket_id),
    position int not null,
    status waitlist_status not null default 'waiting',
    created_at timestamp not null default now(),
    notification_at timestamp,
    expired_at timestamp,
    unique (user_id, ticket_id)
);

insert into event_platform.waitlist (user_id, position, status, expired_at) values
(16, 1, 'waiting', NOW() + INTERVAL '3 days'),
(17, 2, 'waiting', NOW() + INTERVAL '3 days'),
(18, 3, 'waiting', NOW() + INTERVAL '3 days');

--tambah cust
call event_platform.tambah_customer(
'fani rahayu', 
'fani@mail.id', 
'Pass@1234', 
'08151234567');
select * from event_platform.users
where name ='fani rahayu';

--tambah org
call event_platform.tambah_organizer(
'sandi putra', 
'sandi@org.id', 
'Org@5678', 
'08161234567', 
'sandi production');

--tambah staff
call event_platform.tambah_staff
('doni salmanana', 
'doni@staff.id',
'Staff@123', 
'081361339813',
1, 
'checker', 
'gate d', 
'2025-12-20 14:00:00');


--beli tiket
call event_platform.beli_tiket(118, 2, 1, 'qris');

--cek sisa tiket + pendapatan
create view event_platform.ticket_sales as
select
    t.ticket_type,
    t.total_quota,
    coalesce(sum(bd.quantity), 0) as terjual,
    t.total_quota - coalesce(sum(bd.quantity), 0) as sisa,
    t.price,
    coalesce(sum(bd.quantity), 0) * t.price as total_pendapatan
from event_platform.tickets t
left join event_platform.booking_details bd on bd.ticket_id = t.ticket_id
left join event_platform.bookings b on b.booking_id = bd.booking_id and b.status = 'confirmed'
where t.event_id = 1
group by t.ticket_id, t.ticket_type, t.total_quota, t.price;

select * from event_platform.ticket_sales;


--daftar peserta event
create view event_platform.peserta_event as
select
    u.user_id,
    u.name as nama_peserta,
    u.email,
    u.phone,
    t.ticket_type,
    it.ticket_code,
    it.status as status_tiket,
    b.booking_at,
    p.payment_method
from event_platform.issued_tickets it
join event_platform.bookings b on b.booking_id    = it.booking_id
join event_platform.users u on u.user_id       = b.user_id
join event_platform.booking_details bd on bd.booking_id   = b.booking_id
join event_platform.tickets t on t.ticket_id     = bd.ticket_id
left join event_platform.payments p on p.booking_id    = b.booking_id
where b.status = 'confirmed' and b.event_id = 1
order by t.ticket_type, u.name;

select * from event_platform.peserta_event;


--daftar staff event
create view event_platform.list_event_taff as
select
    es.staff_id,
    u.name as nama_staff,
    es.role as jabatan,
    es.area,
    es.assigned_at
from event_platform.event_staff es
join event_platform.users u on u.user_id = es.user_id
where es.event_id = 1
order by es.role, u.name;

select * from event_platform.list_event_taff;

--tambah cust
create or replace procedure event_platform.tambah_customer(
    p_name  text,
    p_email text,
    p_pass  text,
    p_phone text
)
language plpgsql as $$
declare
    v_user_id int;
begin
    if exists (select 1 from event_platform.users where email = p_email) then
        raise exception 'email "%" sudah terdaftar.', p_email;
    end if;
    insert into event_platform.users (name, email, password_hash, phone, role)
    values (p_name, p_email, public.crypt(p_pass, public.gen_salt('bf')), p_phone, 'customer')
    returning user_id into v_user_id;
    raise notice 'customer baru ditambahkan. user_id = %', v_user_id;
end;
$$;


--tambah org
create or replace procedure event_platform.tambah_organizer(
    p_name     text,
    p_email    text,
    p_pass     text,
    p_phone    text,
    p_org_name text
)
language plpgsql as $$
declare
    v_user_id int;
begin
    if exists (select 1 from event_platform.users where email = p_email) then
        raise exception 'email "%" sudah terdaftar.', p_email;
    end if;
    insert into event_platform.users (name, email, password_hash, phone, role)
    values (p_name, p_email, public.crypt(p_pass, public.gen_salt('bf')), p_phone, 'organizer')
    returning user_id into v_user_id;
    insert into event_platform.organizers (user_id, organizer_name, status)
    values (v_user_id, p_org_name, 'pending');
    raise notice 'organizer "%" didaftarkan (status: pending). user_id = %', p_org_name, v_user_id;
end;
$$;


-tambah staff
create or replace procedure event_platform.tambah_staff(
    p_name        text,
    p_email       text,
    p_pass        text,
    p_phone       text,
    p_event_id    int,
    p_role        staff_role,
    p_area        text,
    p_assigned_at timestamp
)
language plpgsql as $$
declare
    v_user_id  int;
    v_staff_id int;
begin
    if not exists (select 1 from event_platform.events where event_id = p_event_id) then
        raise exception 'event id % tidak ditemukan.', p_event_id;
    end if;
    select user_id into v_user_id from event_platform.users where email = p_email;
    if v_user_id is null then
        insert into event_platform.users (name, email, password_hash, phone, role)
        values (p_name, p_email, public.crypt(p_pass, public.gen_salt('bf')), p_phone, 'customer')
        returning user_id into v_user_id;
    end if;
    if exists (select 1 from event_platform.event_staff where user_id = v_user_id and event_id = p_event_id) then
        raise exception 'user "%" sudah terdaftar sebagai staff di event ini.', p_name;
    end if;
    insert into event_platform.event_staff (user_id, event_id, role, area, assigned_at)
    values (v_user_id, p_event_id, p_role, p_area, p_assigned_at)
    returning staff_id into v_staff_id;
    raise notice 'staff "%" ditambahkan. staff_id = %, role = %', p_name, v_staff_id, p_role;
end;
$$;


--cust beli ticket
create or replace procedure event_platform.beli_tiket(
    p_user_id        int,
    p_ticket_id      int,
    p_quantity       int  default 1,
    p_payment_method text default 'qris',
    p_discount_code  text default null
)
language plpgsql as $$
declare
    v_event_id      int;
    v_ticket_type   ticket_type;
    v_price         numeric(12,2);
    v_quota_sisa    int;
    v_total_amount  numeric(14,2);
    v_discount_amt  numeric(14,2) := 0;
    v_discount_id   int;
    v_discount_type discount_type;
    v_discount_val  numeric(10,2);
    v_booking_id    int;
    v_ticket_code   text;
    v_expire_event  timestamp;
    i               int;
begin
    if not exists (select 1 from event_platform.users where user_id = p_user_id) then
        raise exception 'user id % tidak ditemukan.', p_user_id;
    end if;
 
    select t.event_id, t.ticket_type, t.price,
           t.total_quota - coalesce((
               select sum(bd.quantity)
               from event_platform.booking_details bd
               join event_platform.bookings b on b.booking_id = bd.booking_id
               where bd.ticket_id = t.ticket_id and b.status = 'confirmed'
           ), 0)
    into v_event_id, v_ticket_type, v_price, v_quota_sisa
    from event_platform.tickets t where t.ticket_id = p_ticket_id;
 
    if not found then
        raise exception 'tiket id % tidak ditemukan.', p_ticket_id;
    end if;
 
    if (select status from event_platform.tickets where ticket_id = p_ticket_id) = 'sold_out' then
        raise exception 'tiket % sudah sold out.', v_ticket_type;
    end if;
 
    if (select status from event_platform.tickets where ticket_id = p_ticket_id) = 'cancelled' then
        raise exception 'tiket % sudah dibatalkan.', v_ticket_type;
    end if;
 
    if v_quota_sisa < p_quantity then
        raise exception 'kuota tidak cukup. sisa: % tiket.', v_quota_sisa;
    end if;
 
    v_total_amount := v_price * p_quantity;
 
    if p_discount_code is not null then
        select d.discount_id, d.discount_type, d.discount_value
        into v_discount_id, v_discount_type, v_discount_val
        from event_platform.discounts d
        where lower(d.code) = lower(p_discount_code)
          and d.status = 'active'
          and current_date between d.start_date and d.end_date
          and (d.event_id is null or d.event_id = v_event_id)
          and v_total_amount >= d.min_purchase
          and (d.usage_limit is null or d.usage_limit > 0);
 
        if not found then
            raise exception 'kode diskon "%" tidak valid.', p_discount_code;
        end if;
 
        if v_discount_type = 'percentage' then
            v_discount_amt := round(v_total_amount * v_discount_val / 100, 2);
        else
            v_discount_amt := least(v_discount_val, v_total_amount);
        end if;
 
        v_total_amount := v_total_amount - v_discount_amt;
 
        update event_platform.discounts set usage_limit = usage_limit - 1
        where discount_id = v_discount_id;
    end if;
 
    select (event_date + interval '1 day')::timestamp
    into v_expire_event
    from event_platform.events where event_id = v_event_id;
 
    insert into event_platform.bookings (user_id, event_id, total_amount, discount_amount, status)
    values (p_user_id, v_event_id, v_total_amount, v_discount_amt, 'confirmed')
    returning booking_id into v_booking_id;
 
    insert into event_platform.booking_details (booking_id, ticket_id, quantity, price_snapshot)
    values (v_booking_id, p_ticket_id, p_quantity, v_price);
 
    insert into event_platform.payments (booking_id, amount, status, payment_method, paid_at)
    values (v_booking_id, v_total_amount, 'paid', p_payment_method, now());
 
    for i in 1..p_quantity loop
        v_ticket_code := upper(v_ticket_type::text) || '-' ||
                         to_char(now(), 'YYYY') || '-' ||
                         lpad(nextval('event_platform.issued_tickets_issued_ticket_id_seq')::text, 6, '0');
        insert into event_platform.issued_tickets (booking_id, ticket_code, status, expired_at)
        values (v_booking_id, v_ticket_code, 'active', v_expire_event);
        raise notice 'tiket diterbitkan: %', v_ticket_code;
    end loop;
 
    select t.total_quota - coalesce((
        select sum(bd.quantity)
        from event_platform.booking_details bd
        join event_platform.bookings b on b.booking_id = bd.booking_id
        where bd.ticket_id = p_ticket_id and b.status = 'confirmed'
    ), 0)
    into v_quota_sisa
    from event_platform.tickets t where t.ticket_id = p_ticket_id;
 
    if v_quota_sisa <= 0 then
        update event_platform.tickets set status = 'sold_out' where ticket_id = p_ticket_id;
        raise notice 'tiket % sekarang sold out.', v_ticket_type;
    end if;
 
    raise notice '--- booking berhasil ---';
    raise notice 'booking_id   : %', v_booking_id;
    raise notice 'tipe tiket   : %', v_ticket_type;
    raise notice 'jumlah       : % tiket', p_quantity;
    raise notice 'total bayar  : rp %', to_char(v_total_amount, 'fm999,999,999');
    raise notice 'diskon       : rp %', to_char(v_discount_amt, 'fm999,999,999');
    raise notice 'metode bayar : %', p_payment_method;
end;
$$;