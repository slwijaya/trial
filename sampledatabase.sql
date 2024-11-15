CREATE TABLE products (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    price DECIMAL(10, 2) NOT NULL,
    stock INT NOT NULL
);

INSERT INTO products (name, description, price, stock) VALUES
('Credit Card', 'Kartu kredit dengan limit tinggi untuk nasabah premium', 1000000, 100),
('Savings Account', 'Akun tabungan dengan bunga kompetitif', 50000, 500),
('Loan', 'Produk pinjaman untuk berbagai kebutuhan dengan suku bunga rendah', 1000000, 50),
('Fixed Deposit', 'Deposito tetap dengan jangka waktu fleksibel', 2000000, 200),
('Investment Fund', 'Produk investasi untuk diversifikasi portofolio nasabah', 100000, 150);

