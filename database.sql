-- IMPORTANT: Do not run CREATE DATABASE on free hosting like ByetHost/InfinityFree.
-- Create the database via the ByetHost Control Panel (cPanel) instead, select it in phpMyAdmin, and then run the SQL below.

-- CREATE DATABASE IF NOT EXISTS `b11_42305866_database`;
-- USE `b11_42305866_database`;

-- Table structure for table `data` (Mahasiswa)
CREATE TABLE IF NOT EXISTS `data` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `NIM` varchar(15) NOT NULL,
  `Nama` varchar(100) NOT NULL,
  `Jurusan` varchar(100) NOT NULL,
  `Alamat` text NOT NULL,
  `foto` varchar(255) DEFAULT '',
  PRIMARY KEY (`id`),
  UNIQUE KEY `NIM` (`NIM`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Table structure for table `users` (User Login)
CREATE TABLE IF NOT EXISTS `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Dumping data for table `users` (Credentials: admin / admin123)
INSERT INTO `users` (`username`, `password`) VALUES ('admin', 'admin123')
ON DUPLICATE KEY UPDATE `password` = VALUES(`password`);
