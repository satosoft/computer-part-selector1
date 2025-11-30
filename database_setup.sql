-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1:3306
-- Generation Time: Mar 14, 2025 at 04:57 PM
-- Server version: 9.1.0
-- PHP Version: 8.3.14

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `computerparts`
--

-- --------------------------------------------------------

--
-- Table structure for table `computercase`
--

DROP TABLE IF EXISTS `computercase`;
CREATE TABLE IF NOT EXISTS `computercase` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `manufacturer` varchar(100) NOT NULL,
  `caseSize` enum('mATX','ATX','ITX') NOT NULL,
  `caseMaterial` varchar(50) DEFAULT NULL,
  `caseType` varchar(50) DEFAULT NULL,
  `price` double DEFAULT NULL,
  `power` int DEFAULT NULL,
  `stock` int DEFAULT NULL,
  `color` varchar(20) NOT NULL DEFAULT 'Black',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=31 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `computercase`
--

INSERT INTO `computercase` (`id`, `name`, `manufacturer`, `caseSize`, `caseMaterial`, `caseType`, `price`, `power`, `stock`, `color`) VALUES
(1, 'NZXT H7 Flow', 'NZXT', 'ATX', 'Steel, Glass', 'Mid Tower', 3500000, 10, 10, 'Black'),
(2, 'Corsair 4000D Airflow', 'Corsair', 'ATX', 'Steel, Glass', 'Mid Tower', 3000000, 10, 10, 'Black'),
(3, 'Lian Li PC-O11 Dynamic', 'Lian Li', 'ATX', 'Aluminum, Glass', 'Mid Tower', 4000000, 15, 10, 'White'),
(4, 'Fractal Design Meshify 2', 'Fractal Design', 'ATX', 'Steel, Glass', 'Mid Tower', 3200000, 10, 10, 'Black'),
(5, 'Phanteks Eclipse P400A', 'Phanteks', 'ATX', 'Steel, Glass', 'Mid Tower', 2800000, 10, 10, 'Black'),
(6, 'Cooler Master MasterBox TD500', 'Cooler Master', 'ATX', 'Steel, Glass', 'Mid Tower', 3500000, 15, 10, 'Black'),
(7, 'NZXT H510i', 'NZXT', 'ATX', 'Steel, Glass', 'Mid Tower', 2500000, 10, 10, 'White'),
(8, 'Corsair 5000D', 'Corsair', 'ATX', 'Steel, Glass', 'Mid Tower', 4000000, 15, 10, 'Black'),
(9, 'Lian Li Lancool III', 'Lian Li', 'ATX', 'Steel, Glass', 'Mid Tower', 3800000, 10, 10, 'Black'),
(10, 'Fractal Design Define 7', 'Fractal Design', 'ATX', 'Steel', 'Mid Tower', 4500000, 5, 10, 'Black'),
(11, 'Phanteks Enthoo Pro II', 'Phanteks', 'ATX', 'Steel, Glass', 'Full Tower', 5000000, 15, 10, 'Black'),
(12, 'Cooler Master HAF 700 Evo', 'Cooler Master', 'ATX', 'Steel, Glass', 'Full Tower', 6000000, 20, 10, 'Black'),
(13, 'Be Quiet! Silent Base 802', 'Be Quiet!', 'ATX', 'Steel, Glass', 'Mid Tower', 4000000, 10, 10, 'White'),
(14, 'NZXT H210', 'NZXT', 'ITX', 'Steel, Glass', 'Mini Tower', 2000000, 5, 10, 'Black'),
(15, 'Corsair 280X', 'Corsair', 'mATX', 'Steel, Glass', 'Mid Tower', 3000000, 10, 10, 'White'),
(16, 'Lian Li TU150', 'Lian Li', 'ITX', 'Aluminum', 'Mini Tower', 2500000, 5, 10, 'Black'),
(17, 'Fractal Design Node 304', 'Fractal Design', 'ITX', 'Steel', 'Mini Tower', 2200000, 5, 10, 'Black'),
(18, 'Phanteks Evolv Shift 2', 'Phanteks', 'ITX', 'Aluminum, Glass', 'Mini Tower', 2800000, 10, 10, 'Black'),
(19, 'Cooler Master NR200P', 'Cooler Master', 'ITX', 'Steel, Glass', 'Mini Tower', 2500000, 10, 10, 'White'),
(20, 'Be Quiet! Pure Base 500DX', 'Be Quiet!', 'ATX', 'Steel, Glass', 'Mid Tower', 3200000, 15, 10, 'Black'),
(21, 'Corsair Crystal 280X RGB', 'Corsair', 'mATX', 'Steel, Glass', 'Mid Tower', 3500000, 15, 10, 'White'),
(22, 'Fractal Design Meshify C Mini', 'Fractal Design', 'mATX', 'Steel, Glass', 'Mid Tower', 2800000, 10, 10, 'Black'),
(23, 'NZXT H400i', 'NZXT', 'mATX', 'Steel, Glass', 'Mid Tower', 3000000, 10, 10, 'Black'),
(24, 'Lian Li Lancool 205M', 'Lian Li', 'mATX', 'Steel, Glass', 'Mid Tower', 3200000, 10, 10, 'Black'),
(25, 'Cooler Master MasterBox Q300L', 'Cooler Master', 'mATX', 'Steel, Acrylic', 'Mid Tower', 2000000, 5, 10, 'Black'),
(26, 'Phanteks Eclipse P360A', 'Phanteks', 'mATX', 'Steel, Glass', 'Mid Tower', 2900000, 10, 10, 'White'),
(27, 'Be Quiet! Pure Base 600', 'Be Quiet!', 'mATX', 'Steel', 'Mid Tower', 2700000, 10, 10, 'Black'),
(28, 'ASUS TUF Gaming GT301', 'ASUS', 'mATX', 'Steel, Glass', 'Mid Tower', 3100000, 15, 10, 'Black'),
(29, 'Thermaltake Versa H18', 'Thermaltake', 'mATX', 'Steel, Glass', 'Mid Tower', 1800000, 5, 10, 'Black'),
(30, 'SilverStone Fara R1', 'SilverStone', 'mATX', 'Steel, Glass', 'Mid Tower', 2500000, 10, 10, 'White');

-- --------------------------------------------------------

--
-- Table structure for table `cooler`
--

DROP TABLE IF EXISTS `cooler`;
CREATE TABLE IF NOT EXISTS `cooler` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `manufacturer` varchar(100) NOT NULL,
  `coolerType` varchar(50) DEFAULT NULL,
  `socketSupport` text,
  `ledColor` varchar(20) DEFAULT NULL,
  `price` double DEFAULT NULL,
  `power` int DEFAULT NULL,
  `stock` int DEFAULT NULL,
  `color` varchar(20) NOT NULL DEFAULT 'Black',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=31 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `cooler`
--

INSERT INTO `cooler` (`id`, `name`, `manufacturer`, `coolerType`, `socketSupport`, `ledColor`, `price`, `power`, `stock`, `color`) VALUES
(1, 'Noctua NH-D15 G2', 'Noctua', 'Air', 'LGA1700, LGA1200, AM5, AM4', 'None', 3000000, 10, 10, 'Black'),
(2, 'Corsair iCUE H150i Elite', 'Corsair', 'AIO', 'LGA1700, LGA1200, AM5, AM4', 'RGB', 5000000, 30, 10, 'Black'),
(3, 'Cooler Master Hyper 212 Black', 'Cooler Master', 'Air', 'LGA1700, LGA1151, AM5, AM4', 'None', 1000000, 8, 10, 'Black'),
(4, 'NZXT Kraken X63', 'NZXT', 'AIO', 'LGA1700, AM5, AM4', 'RGB', 3500000, 25, 10, 'White'),
(5, 'Be Quiet! Pure Rock 2', 'Be Quiet!', 'Air', 'LGA1700, LGA1155, AM5, AM4', 'None', 1200000, 7, 10, 'Black'),
(6, 'Arctic Liquid Freezer II 360', 'Arctic', 'AIO', 'LGA1700, LGA1200, AM5, AM4', 'None', 4000000, 35, 10, 'Black'),
(7, 'Deepcool AK620', 'Deepcool', 'Air', 'LGA1700, AM5, AM4', 'None', 1500000, 12, 10, 'White'),
(8, 'Thermaltake TOUGHAIR 510', 'Thermaltake', 'Air', 'LGA1700, LGA1156, AM5, AM4', 'None', 1100000, 9, 10, 'Black'),
(9, 'MSI MAG CoreLiquid 240R', 'MSI', 'AIO', 'LGA1700, AM5, AM4', 'RGB', 3000000, 20, 10, 'Black'),
(10, 'Noctua NH-U12S', 'Noctua', 'Air', 'LGA1700, LGA1200, AM5, AM4', 'None', 1800000, 8, 10, 'Black'),
(11, 'Corsair H100i RGB Platinum', 'Corsair', 'AIO', 'LGA1700, LGA 1150, AM5, AM4', 'RGB', 4000000, 25, 10, 'White'),
(12, 'Cooler Master MasterLiquid ML360R', 'Cooler Master', 'AIO', 'LGA1700, AM5, AM4', 'RGB', 4500000, 35, 10, 'Black'),
(13, 'Be Quiet! Silent Loop 2 280', 'Be Quiet!', 'AIO', 'LGA1700, LGA1200, AM5, AM4', 'White', 3800000, 30, 10, 'Black'),
(14, 'Arctic Freezer 34 eSports', 'Arctic', 'Air', 'LGA1700, AM5, AM4', 'None', 900000, 6, 10, 'White'),
(15, 'Deepcool LS720', 'Deepcool', 'AIO', 'LGA1700, LGA1151, AM5, AM4', 'RGB', 4200000, 40, 10, 'Black'),
(16, 'Thermaltake Water 3.0 240', 'Thermaltake', 'AIO', 'LGA1700, AM5, AM4', 'Blue', 3200000, 25, 10, 'Black'),
(17, 'NZXT T120 RGB', 'NZXT', 'Air', 'LGA1700, AM5, AM4', 'RGB', 1400000, 10, 10, 'White'),
(18, 'MSI MAG CoreLiquid P360', 'MSI', 'AIO', 'LGA1700, LGA1200, AM5, AM4', 'None', 3500000, 30, 10, 'Black'),
(19, 'Cooler Master Hyper 620S', 'Cooler Master', 'Air', 'LGA1700, AM5, AM4', 'None', 1300000, 9, 10, 'Black'),
(20, 'Noctua NH-P1', 'Noctua', 'Air', 'LGA1700, LGA1155, AM5, AM4', 'None', 2500000, 5, 10, 'Black'),
(21, 'Noctua NH-D15 G2', 'Noctua', 'Air', 'LGA1851, LGA1700, LGA1200, AM5, AM4', 'None', 3000000, 10, 10, 'Black'),
(22, 'Corsair iCUE H150i Elite', 'Corsair', 'AIO', 'LGA1851, LGA1700, LGA1200, AM5, AM4', 'RGB', 5000000, 30, 10, 'Black'),
(23, 'Cooler Master Hyper 212 Evo V2', 'Cooler Master', 'Air', 'LGA1851, LGA1700, LGA1151, AM5, AM4', 'None', 1000000, 8, 10, 'Black'),
(24, 'NZXT Kraken Z73', 'NZXT', 'AIO', 'LGA1851, LGA1700, AM5, AM4', 'RGB', 6000000, 35, 10, 'White'),
(25, 'Be Quiet! Dark Rock Pro 5', 'Be Quiet!', 'Air', 'LGA1851, LGA1700, LGA1200, AM5, AM4', 'None', 2500000, 12, 10, 'Black'),
(26, 'Arctic Liquid Freezer III 280', 'Arctic', 'AIO', 'LGA1851, LGA1700, LGA1155, AM5, AM4', 'None', 3500000, 25, 10, 'Black'),
(27, 'Deepcool Assassin IV', 'Deepcool', 'Air', 'LGA1851, LGA1700, AM5, AM4', 'None', 2000000, 10, 10, 'White'),
(28, 'Thermaltake TOUGHLIQUID 360', 'Thermaltake', 'AIO', 'LGA1851, LGA1700, LGA1200, AM5, AM4', 'RGB', 4500000, 30, 10, 'Black'),
(29, 'MSI MAG CoreLiquid 360R V2', 'MSI', 'AIO', 'LGA1851, LGA1700, AM5, AM4', 'RGB', 4000000, 25, 10, 'Black'),
(30, 'EK-AIO 240 D-RGB', 'EKWB', 'AIO', 'LGA1851, LGA1700, LGA1150, AM5, AM4', 'RGB', 3800000, 20, 10, 'Black');

-- --------------------------------------------------------

--
-- Table structure for table `cpu`
--

DROP TABLE IF EXISTS `cpu`;
CREATE TABLE IF NOT EXISTS `cpu` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `manufacturer` varchar(100) NOT NULL,
  `generation` varchar(50) DEFAULT NULL,
  `socket` varchar(50) DEFAULT NULL,
  `benchmark` double DEFAULT NULL,
  `price` double DEFAULT NULL,
  `power` int DEFAULT NULL,
  `stock` int DEFAULT NULL,
  `coreNum` int NOT NULL,
  `color` varchar(20) NOT NULL DEFAULT 'Black',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=41 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `cpu`
--

INSERT INTO `cpu` (`id`, `name`, `manufacturer`, `generation`, `socket`, `benchmark`, `price`, `power`, `stock`, `coreNum`, `color`) VALUES
(1, 'Intel Core i9-14900K', 'Intel', '14', 'LGA1700', 59234, 15000000, 253, 10, 24, 'Silver'),
(2, 'AMD Ryzen 9 7950X', 'AMD', 'Zen 4', 'AM5', 64512, 14500000, 230, 10, 16, 'Silver'),
(3, 'Intel Core i7-14700K', 'Intel', '14', 'LGA1700', 49876, 11000000, 253, 10, 20, 'Silver'),
(4, 'AMD Ryzen 7 7800X3D', 'AMD', 'Zen 4', 'AM5', 34799, 11500000, 120, 10, 8, 'Silver'),
(5, 'Intel Core i5-14600K', 'Intel', '14', 'LGA1700', 34721, 8000000, 181, 10, 14, 'Silver'),
(6, 'AMD Ryzen 5 7600X', 'AMD', 'Zen 4', 'AM5', 28543, 6500000, 105, 10, 6, 'Silver'),
(7, 'Intel Core i9-13900K', 'Intel', '13', 'LGA1700', 56891, 14000000, 253, 10, 24, 'Silver'),
(8, 'AMD Ryzen 9 7900X', 'AMD', 'Zen 4', 'AM5', 52413, 12000000, 170, 10, 12, 'Silver'),
(9, 'Intel Core i7-13700K', 'Intel', '13', 'LGA1700', 46532, 10000000, 253, 10, 16, 'Silver'),
(10, 'AMD Ryzen 7 7700X', 'AMD', 'Zen 4', 'AM5', 35678, 9000000, 105, 10, 8, 'Silver'),
(11, 'Intel Core i5-13600K', 'Intel', '13', 'LGA1700', 32567, 7500000, 181, 10, 14, 'Silver'),
(12, 'AMD Ryzen 5 5600X', 'AMD', 'Zen 3', 'AM4', 21876, 5000000, 65, 10, 6, 'Silver'),
(13, 'Intel Core i9-12900K', 'Intel', '12', 'LGA1700', 41234, 13000000, 241, 10, 16, 'Silver'),
(14, 'AMD Ryzen 9 5950X', 'AMD', 'Zen 3', 'AM4', 39123, 12500000, 105, 10, 16, 'Silver'),
(15, 'Intel Core i7-12700K', 'Intel', '12', 'LGA1700', 34678, 9500000, 190, 10, 12, 'Silver'),
(16, 'AMD Ryzen 7 5800X', 'AMD', 'Zen 3', 'AM4', 28543, 7000000, 105, 10, 8, 'Silver'),
(17, 'Intel Core i5-12600K', 'Intel', '12', 'LGA1700', 27654, 6500000, 150, 10, 10, 'Silver'),
(18, 'AMD Ryzen 5 5500', 'AMD', 'Zen 3', 'AM4', 19876, 4000000, 65, 10, 6, 'Silver'),
(19, 'Intel Core i3-14100', 'Intel', '14', 'LGA1700', 14532, 4000000, 110, 10, 4, 'Silver'),
(20, 'AMD Ryzen 3 5300G', 'AMD', 'Zen 3', 'AM4', 13245, 3500000, 65, 10, 4, 'Silver'),
(21, 'Intel Core i9-11900K', 'Intel', '11', 'LGA1200', 34678, 9500000, 125, 10, 8, 'Silver'),
(22, 'Intel Core i7-11700K', 'Intel', '11', 'LGA1200', 29654, 8000000, 125, 10, 8, 'Silver'),
(23, 'Intel Core i5-11600K', 'Intel', '11', 'LGA1200', 24678, 6500000, 125, 10, 6, 'Silver'),
(24, 'Intel Core i3-11100', 'Intel', '11', 'LGA1200', 12345, 3500000, 65, 10, 4, 'Silver'),
(25, 'Intel Core i9-10900K', 'Intel', '10', 'LGA1200', 32543, 9000000, 125, 10, 10, 'Silver'),
(26, 'Intel Core i7-10700K', 'Intel', '10', 'LGA1200', 27654, 7500000, 125, 10, 8, 'Silver'),
(27, 'Intel Core i5-10600K', 'Intel', '10', 'LGA1200', 19876, 6000000, 125, 10, 6, 'Silver'),
(28, 'Intel Core i3-10100F', 'Intel', '10', 'LGA1200', 9876, 2500000, 65, 10, 4, 'Silver'),
(29, 'Intel Core i5-11400', 'Intel', '11', 'LGA1200', 18765, 5000000, 65, 10, 6, 'Silver'),
(30, 'Intel Core i7-10700', 'Intel', '10', 'LGA1200', 25678, 7000000, 65, 10, 8, 'Silver'),
(31, 'Intel Core Ultra 9 285K', 'Intel', '15', 'LGA1851', 62000, 15500000, 125, 10, 24, 'Silver'),
(32, 'Intel Core Ultra 7 265K', 'Intel', '15', 'LGA1851', 52000, 12500000, 125, 10, 20, 'Silver'),
(33, 'Intel Core Ultra 5 245K', 'Intel', '15', 'LGA1851', 38000, 8500000, 125, 10, 14, 'Silver'),
(34, 'Intel Core Ultra 9 285KF', 'Intel', '15', 'LGA1851', 60000, 15000000, 125, 10, 24, 'Silver'),
(35, 'Intel Core Ultra 7 265KF', 'Intel', '15', 'LGA1851', 50000, 12000000, 125, 10, 20, 'Silver'),
(36, 'Intel Core Ultra 5 245KF', 'Intel', '15', 'LGA1851', 36000, 8000000, 125, 10, 14, 'Silver'),
(37, 'Intel Core Ultra 7 255', 'Intel', '15', 'LGA1851', 45000, 10000000, 100, 10, 16, 'Silver'),
(38, 'Intel Core Ultra 5 235F', 'Intel', '15', 'LGA1851', 30000, 7000000, 90, 10, 10, 'Silver'),
(39, 'Intel Core Ultra 7 265', 'Intel', '15', 'LGA1851', 48000, 11500000, 125, 10, 20, 'Silver'),
(40, 'Intel Core Ultra 9 275', 'Intel', '15', 'LGA1851', 55000, 13500000, 125, 10, 20, 'Silver');

-- --------------------------------------------------------

--
-- Table structure for table `gpu`
--

DROP TABLE IF EXISTS `gpu`;
CREATE TABLE IF NOT EXISTS `gpu` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `manufacturer` varchar(100) NOT NULL,
  `capacity` int DEFAULT NULL,
  `benchmark` double DEFAULT NULL,
  `price` double DEFAULT NULL,
  `power` int DEFAULT NULL,
  `stock` int DEFAULT NULL,
  `color` varchar(20) NOT NULL DEFAULT 'Black',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `gpu`
--

INSERT INTO `gpu` (`id`, `name`, `manufacturer`, `capacity`, `benchmark`, `price`, `power`, `stock`, `color`) VALUES
(1, 'NVIDIA GeForce RTX 4090', 'NVIDIA', 24, 39123, 20000000, 450, 10, 'Black'),
(2, 'AMD Radeon RX 7900 XTX', 'AMD', 24, 30876, 16500000, 355, 10, 'Black'),
(3, 'NVIDIA GeForce RTX 4080', 'NVIDIA', 16, 34123, 17500000, 320, 10, 'White'),
(4, 'AMD Radeon RX 7800 XT', 'AMD', 16, 19876, 11000000, 263, 10, 'Black'),
(5, 'NVIDIA GeForce RTX 4070 Ti', 'NVIDIA', 12, 28765, 14000000, 285, 10, 'Black'),
(6, 'Intel Arc A770', 'Intel', 16, 14321, 8000000, 225, 10, 'Black'),
(7, 'NVIDIA GeForce RTX 4060', 'NVIDIA', 8, 16789, 7500000, 115, 10, 'Black'),
(8, 'AMD Radeon RX 7600', 'AMD', 8, 13245, 6500000, 165, 10, 'Black'),
(9, 'NVIDIA GeForce RTX 3090', 'NVIDIA', 24, 32543, 15000000, 350, 10, 'White'),
(10, 'AMD Radeon RX 6900 XT', 'AMD', 16, 26789, 12000000, 300, 10, 'Black'),
(11, 'NVIDIA GeForce RTX 3080', 'NVIDIA', 10, 28765, 13000000, 320, 10, 'Black'),
(12, 'AMD Radeon RX 6700 XT', 'AMD', 12, 18765, 9000000, 230, 10, 'Black'),
(13, 'NVIDIA GeForce RTX 3060 Ti', 'NVIDIA', 8, 19876, 8500000, 200, 10, 'Black'),
(14, 'Intel Arc A750', 'Intel', 8, 12345, 6000000, 225, 10, 'Black'),
(15, 'NVIDIA GeForce RTX 3050', 'NVIDIA', 8, 11234, 5500000, 130, 10, 'White'),
(16, 'AMD Radeon RX 6600', 'AMD', 8, 14567, 7000000, 132, 10, 'Black'),
(17, 'NVIDIA GeForce GTX 1660 Super', 'NVIDIA', 6, 9876, 4500000, 125, 10, 'Black'),
(18, 'AMD Radeon RX 6500 XT', 'AMD', 4, 7654, 4000000, 107, 10, 'Black'),
(19, 'NVIDIA GeForce RTX 2060', 'NVIDIA', 6, 13245, 6000000, 160, 10, 'Black'),
(20, 'AMD Radeon RX 580', 'AMD', 8, 8765, 3500000, 185, 10, 'White');

-- --------------------------------------------------------

--
-- Table structure for table `harddisk`
--

DROP TABLE IF EXISTS `harddisk`;
CREATE TABLE IF NOT EXISTS `harddisk` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `manufacturer` varchar(100) NOT NULL,
  `type` enum('HDD','SSD','NVME') NOT NULL,
  `speed` int DEFAULT NULL,
  `price` double DEFAULT NULL,
  `power` int DEFAULT NULL,
  `stock` int DEFAULT NULL,
  `color` varchar(20) NOT NULL DEFAULT 'Black',
  `capacity` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=41 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `harddisk`
--

INSERT INTO `harddisk` (`id`, `name`, `manufacturer`, `type`, `speed`, `price`, `power`, `stock`, `color`, `capacity`) VALUES
(1, 'WD Blue 2TB', 'Western Digital', 'HDD', 150, 1500000, 6, 10, 'Black', 2048),
(2, 'Seagate Barracuda 4TB', 'Seagate', 'HDD', 180, 2500000, 8, 10, 'Black', 4096),
(3, 'Samsung 870 EVO 1TB', 'Samsung', 'SSD', 560, 3000000, 4, 10, 'Black', 1024),
(4, 'WD Black SN850X 2TB', 'Western Digital', 'NVME', 7300, 6000000, 7, 10, 'Black', 2048),
(5, 'Kingston NV2 1TB', 'Kingston', 'NVME', 3500, 2500000, 6, 10, 'Black', 1024),
(6, 'Crucial MX500 500GB', 'Crucial', 'SSD', 560, 1500000, 3, 10, 'Black', 500),
(7, 'Seagate FireCuda 530 1TB', 'Seagate', 'NVME', 7000, 4500000, 7, 10, 'Black', 1024),
(8, 'WD Red 6TB', 'Western Digital', 'HDD', 200, 4000000, 9, 10, 'Black', 6144),
(9, 'Samsung 990 PRO 2TB', 'Samsung', 'NVME', 7450, 7000000, 8, 10, 'Black', 2048),
(10, 'Toshiba X300 8TB', 'Toshiba', 'HDD', 220, 5500000, 10, 10, 'Black', 8192),
(11, 'Crucial P3 Plus 1TB', 'Crucial', 'NVME', 5000, 3000000, 6, 10, 'Black', 1024),
(12, 'WD Blue SN580 500GB', 'Western Digital', 'NVME', 4150, 2000000, 5, 10, 'Black', 500),
(13, 'Seagate IronWolf 4TB', 'Seagate', 'HDD', 190, 3500000, 7, 10, 'Black', 4096),
(14, 'Samsung 860 QVO 2TB', 'Samsung', 'SSD', 520, 4500000, 4, 10, 'Black', 2048),
(15, 'Kingston A400 960GB', 'Kingston', 'SSD', 500, 2200000, 3, 10, 'Black', 960),
(16, 'WD Black 10TB', 'Western Digital', 'HDD', 250, 7000000, 10, 10, 'Black', 10240),
(17, 'Crucial BX500 1TB', 'Crucial', 'SSD', 540, 2500000, 3, 10, 'Black', 1024),
(18, 'Seagate FireCuda 520 500GB', 'Seagate', 'NVME', 5000, 2000000, 6, 10, 'White', 500),
(19, 'Samsung 970 EVO Plus 1TB', 'Samsung', 'NVME', 3500, 3500000, 6, 10, 'Black', 1024),
(20, 'Toshiba Canvio Advance 2TB', 'Toshiba', 'HDD', 140, 1800000, 5, 10, 'White', 2048),
(21, 'Kingston XS2000 250GB', 'Kingston', 'SSD', 2000, 1500000, 5, 20, 'Black', 250),
(22, 'Samsung T7 250GB', 'Samsung', 'SSD', 1050, 1800000, 5, 15, 'Gray', 250),
(23, 'SanDisk Extreme Portable 250GB', 'SanDisk', 'SSD', 1050, 1600000, 5, 10, 'Black', 250),
(24, 'WD My Passport SSD 256GB', 'Western Digital', 'SSD', 1050, 1700000, 5, 12, 'Red', 256),
(25, 'ADATA SE800 256GB', 'ADATA', 'SSD', 1000, 1550000, 5, 18, 'Blue', 256),
(26, 'Kingston XS2000 500GB', 'Kingston', 'SSD', 2000, 2500000, 5, 20, 'Black', 500),
(27, 'Samsung T7 500GB', 'Samsung', 'SSD', 1050, 2800000, 5, 15, 'Gray', 500),
(28, 'SanDisk Extreme Portable 500GB', 'SanDisk', 'SSD', 1050, 2600000, 5, 10, 'Black', 500),
(29, 'WD My Passport SSD 512GB', 'Western Digital', 'SSD', 1050, 2700000, 5, 12, 'Red', 512),
(30, 'ADATA SE800 512GB', 'ADATA', 'SSD', 1000, 2550000, 5, 18, 'Blue', 512),
(31, 'Kingston XS2000 1TB', 'Kingston', 'SSD', 2000, 4500000, 5, 20, 'Black', 1024),
(32, 'Samsung T7 1TB', 'Samsung', 'SSD', 1050, 4800000, 5, 15, 'Gray', 1024),
(33, 'SanDisk Extreme Portable 1TB', 'SanDisk', 'SSD', 1050, 4600000, 5, 10, 'Black', 1024),
(34, 'WD My Passport SSD 1TB', 'Western Digital', 'SSD', 1050, 4700000, 5, 12, 'Red', 1024),
(35, 'ADATA SE800 1TB', 'ADATA', 'SSD', 1000, 4550000, 5, 18, 'Blue', 1024),
(36, 'Kingston XS2000 2TB', 'Kingston', 'SSD', 2000, 8500000, 5, 20, 'Black', 2048),
(37, 'Samsung T7 2TB', 'Samsung', 'SSD', 1050, 8800000, 5, 15, 'Gray', 2048),
(38, 'SanDisk Extreme Portable 2TB', 'SanDisk', 'SSD', 1050, 8600000, 5, 10, 'Black', 2048),
(39, 'WD My Passport SSD 2TB', 'Western Digital', 'SSD', 1050, 8700000, 5, 12, 'Red', 2048),
(40, 'ADATA SE800 2TB', 'ADATA', 'SSD', 1000, 8550000, 5, 18, 'Blue', 2048);

-- --------------------------------------------------------

--
-- Table structure for table `mainboard`
--

DROP TABLE IF EXISTS `mainboard`;
CREATE TABLE IF NOT EXISTS `mainboard` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `manufacturer` varchar(100) NOT NULL,
  `cpuSocket` varchar(50) DEFAULT NULL,
  `ramType` varchar(20) DEFAULT NULL,
  `ramSlots` int NOT NULL DEFAULT '2',
  `gpuSlots` int NOT NULL DEFAULT '1',
  `cpuSlots` int NOT NULL DEFAULT '1',
  `supportNVME` tinyint(1) DEFAULT '0',
  `mainSize` enum('mATX','ATX') NOT NULL DEFAULT 'ATX',
  `price` double DEFAULT NULL,
  `power` int DEFAULT NULL,
  `stock` int DEFAULT NULL,
  `color` varchar(20) NOT NULL DEFAULT 'Black',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=81 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `mainboard`
--

INSERT INTO `mainboard` (`id`, `name`, `manufacturer`, `cpuSocket`, `ramType`, `ramSlots`, `gpuSlots`, `cpuSlots`, `supportNVME`, `mainSize`, `price`, `power`, `stock`, `color`) VALUES
(1, 'ASUS ROG Strix Z790-E', 'ASUS', 'LGA1700', 'DDR5', 4, 2, 1, 1, 'ATX', 7500000, 100, 10, 'Black'),
(2, 'MSI MAG B650 Tomahawk', 'MSI', 'AM5', 'DDR5', 4, 1, 1, 1, 'ATX', 6000000, 90, 10, 'Black'),
(3, 'Gigabyte B760M Aorus Elite', 'Gigabyte', 'LGA1700', 'DDR5', 4, 1, 1, 1, 'mATX', 4500000, 80, 10, 'Black'),
(4, 'ASRock B550M Pro4', 'ASRock', 'AM4', 'DDR4', 4, 1, 1, 1, 'mATX', 3000000, 70, 10, 'Black'),
(5, 'ASUS Prime Z690-P', 'ASUS', 'LGA1700', 'DDR5', 4, 1, 1, 1, 'ATX', 5000000, 85, 10, 'White'),
(6, 'MSI MPG X670E Carbon', 'MSI', 'AM5', 'DDR5', 4, 2, 1, 1, 'ATX', 8000000, 110, 10, 'Black'),
(7, 'Gigabyte Z790 Aorus Master', 'Gigabyte', 'LGA1700', 'DDR5', 4, 2, 1, 1, 'ATX', 9000000, 120, 10, 'Black'),
(8, 'ASRock A520M-HDV', 'ASRock', 'AM4', 'DDR4', 2, 1, 1, 0, 'mATX', 2000000, 60, 10, 'Black'),
(9, 'ASUS TUF Gaming B550-Plus', 'ASUS', 'AM4', 'DDR4', 4, 2, 1, 1, 'ATX', 4000000, 80, 10, 'Black'),
(10, 'MSI B560M Pro-VDH', 'MSI', 'LGA1200', 'DDR4', 4, 1, 1, 1, 'mATX', 3500000, 75, 10, 'Black'),
(11, 'Gigabyte B450M DS3H', 'Gigabyte', 'AM4', 'DDR4', 4, 1, 1, 1, 'mATX', 2500000, 65, 10, 'Black'),
(12, 'ASUS ROG Crosshair X670E Hero', 'ASUS', 'AM5', 'DDR5', 4, 2, 1, 1, 'ATX', 10000000, 115, 10, 'Black'),
(13, 'MSI Z790 Edge WiFi', 'MSI', 'LGA1700', 'DDR5', 4, 2, 1, 1, 'ATX', 7000000, 105, 10, 'White'),
(14, 'Gigabyte X570 Aorus Elite', 'Gigabyte', 'AM4', 'DDR4', 4, 1, 1, 1, 'ATX', 4500000, 90, 10, 'Black'),
(15, 'ASRock B660M Steel Legend', 'ASRock', 'LGA1700', 'DDR4', 4, 1, 1, 1, 'mATX', 4000000, 80, 10, 'White'),
(16, 'ASUS Prime B450M-A', 'ASUS', 'AM4', 'DDR4', 4, 1, 1, 1, 'mATX', 2800000, 70, 10, 'Black'),
(17, 'MSI A520M-A Pro', 'MSI', 'AM4', 'DDR4', 2, 1, 1, 0, 'mATX', 1800000, 55, 10, 'Black'),
(18, 'Gigabyte Z690 Gaming X', 'Gigabyte', 'LGA1700', 'DDR5', 4, 1, 1, 1, 'ATX', 5500000, 95, 10, 'Black'),
(19, 'ASRock X670E Taichi', 'ASRock', 'AM5', 'DDR5', 4, 2, 1, 1, 'ATX', 8500000, 110, 10, 'Black'),
(20, 'ASUS TUF Gaming Z790-Plus', 'ASUS', 'LGA1700', 'DDR5', 4, 2, 1, 1, 'ATX', 6500000, 100, 10, 'Black'),
(21, 'ASUS Prime Z490-A', 'ASUS', 'LGA1200', 'DDR4', 4, 2, 1, 1, 'ATX', 5609745, 65, 50, 'Black'),
(22, 'MSI MPG Z490 Gaming Edge', 'MSI', 'LGA1200', 'DDR4', 4, 3, 1, 1, 'ATX', 4844745, 60, 40, 'Black'),
(23, 'Gigabyte Z490 Vision G', 'Gigabyte', 'LGA1200', 'DDR4', 4, 2, 1, 1, 'ATX', 5099745, 70, 30, 'White'),
(24, 'ASRock Z490 Taichi', 'ASRock', 'LGA1200', 'DDR4', 4, 3, 1, 1, 'ATX', 9434745, 90, 20, 'Black'),
(25, 'ASUS ROG Strix Z490-E', 'ASUS', 'LGA1200', 'DDR4', 4, 2, 1, 1, 'ATX', 7649745, 75, 35, 'Black'),
(26, 'MSI MEG Z490 Godlike', 'MSI', 'LGA1200', 'DDR4', 4, 4, 1, 1, 'mATX', 15299745, 100, 10, 'Black'),
(27, 'Gigabyte Z490 Aorus Master', 'Gigabyte', 'LGA1200', 'DDR4', 4, 3, 1, 1, 'ATX', 9944745, 85, 25, 'Black'),
(28, 'ASRock Z490 Phantom Gaming', 'ASRock', 'LGA1200', 'DDR4', 4, 2, 1, 1, 'ATX', 4589745, 65, 45, 'Black'),
(29, 'ASUS TUF Gaming Z490-Plus', 'ASUS', 'LGA1200', 'DDR4', 4, 2, 1, 1, 'ATX', 4334745, 60, 50, 'Black'),
(30, 'MSI Z490-A Pro', 'MSI', 'LGA1200', 'DDR4', 4, 2, 1, 1, 'ATX', 4079745, 55, 60, 'Black'),
(31, 'Gigabyte Z490 UD', 'Gigabyte', 'LGA1200', 'DDR4', 4, 2, 1, 1, 'ATX', 3569745, 50, 70, 'Black'),
(32, 'ASRock Z490 Pro4', 'ASRock', 'LGA1200', 'DDR4', 4, 2, 1, 1, 'ATX', 3314745, 50, 65, 'Black'),
(33, 'ASUS Prime B460M-A', 'ASUS', 'LGA1200', 'DDR4', 2, 1, 1, 0, 'mATX', 2549745, 40, 80, 'Black'),
(34, 'MSI MAG B460M Mortar', 'MSI', 'LGA1200', 'DDR4', 2, 1, 1, 0, 'mATX', 2804745, 45, 75, 'Black'),
(35, 'Gigabyte B460M DS3H', 'Gigabyte', 'LGA1200', 'DDR4', 2, 1, 1, 0, 'mATX', 2294745, 35, 90, 'Black'),
(36, 'ASRock B460M Steel Legend', 'ASRock', 'LGA1200', 'DDR4', 2, 1, 1, 0, 'mATX', 2932245, 50, 70, 'Black'),
(37, 'ASUS ROG Strix B460-H', 'ASUS', 'LGA1200', 'DDR4', 4, 2, 1, 1, 'ATX', 3824745, 55, 60, 'Black'),
(38, 'MSI MPG B460I Gaming Edge', 'MSI', 'LGA1200', 'DDR4', 2, 1, 1, 1, 'mATX', 4079745, 50, 40, 'Black'),
(39, 'Gigabyte B460 Aorus Pro AC', 'Gigabyte', 'LGA1200', 'DDR4', 4, 2, 1, 1, 'ATX', 4589745, 60, 50, 'Black'),
(40, 'ASRock B460 Phantom Gaming 4', 'ASRock', 'LGA1200', 'DDR4', 4, 2, 1, 1, 'ATX', 3314745, 50, 65, 'Black'),
(41, 'ASUS ROG Maximus Z790 Hero', 'ASUS', 'LGA1700', 'DDR5', 4, 2, 1, 1, 'ATX', 15299745, 100, 20, 'Black'),
(42, 'MSI MPG Z790 Carbon WiFi', 'MSI', 'LGA1700', 'DDR5', 4, 2, 1, 1, 'ATX', 10199745, 85, 30, 'Black'),
(43, 'Gigabyte Z790 Aorus Elite AX', 'Gigabyte', 'LGA1700', 'DDR5', 4, 2, 1, 1, 'ATX', 6884745, 70, 40, 'Black'),
(44, 'ASRock Z790 Taichi', 'ASRock', 'LGA1700', 'DDR5', 4, 2, 1, 1, 'ATX', 12749745, 90, 15, 'Black'),
(45, 'ASUS Prime Z790-A WiFi', 'ASUS', 'LGA1700', 'DDR5', 4, 2, 1, 1, 'ATX', 6629745, 65, 45, 'White'),
(46, 'MSI PRO Z790-P WiFi', 'MSI', 'LGA1700', 'DDR5', 4, 2, 1, 1, 'ATX', 5864745, 55, 50, 'Black'),
(47, 'Gigabyte B760 Aorus Elite AX', 'Gigabyte', 'LGA1700', 'DDR4', 4, 2, 1, 1, 'ATX', 4844745, 60, 35, 'Black'),
(48, 'ASRock B760M Steel Legend', 'ASRock', 'LGA1700', 'DDR4', 4, 2, 1, 1, 'mATX', 4334745, 55, 60, 'Black'),
(49, 'ASUS TUF Gaming B760-Plus WiFi', 'ASUS', 'LGA1700', 'DDR4', 4, 1, 1, 1, 'ATX', 4589745, 60, 40, 'Black'),
(50, 'MSI MAG B760 Tomahawk WiFi', 'MSI', 'LGA1700', 'DDR4', 4, 2, 1, 1, 'ATX', 5099745, 65, 30, 'Black'),
(51, 'ASUS ROG Maximus Z890 Hero', 'ASUS', 'LGA1851', 'DDR5', 4, 2, 1, 1, 'ATX', 16574745, 110, 15, 'Black'),
(52, 'MSI MPG Z890 Carbon WiFi', 'MSI', 'LGA1851', 'DDR5', 4, 2, 1, 1, 'ATX', 10964745, 90, 20, 'Black'),
(53, 'Gigabyte Z890 Aorus Master', 'Gigabyte', 'LGA1851', 'DDR5', 4, 2, 1, 1, 'ATX', 12749745, 100, 25, 'Black'),
(54, 'ASRock Z890 Taichi', 'ASRock', 'LGA1851', 'DDR5', 4, 2, 1, 1, 'ATX', 14024745, 95, 20, 'Black'),
(55, 'ASUS Prime Z890-A WiFi', 'ASUS', 'LGA1851', 'DDR5', 4, 2, 1, 1, 'ATX', 7394745, 75, 40, 'White'),
(56, 'MSI PRO Z890-P WiFi', 'MSI', 'LGA1851', 'DDR5', 4, 2, 1, 1, 'ATX', 6629745, 70, 50, 'Black'),
(57, 'Gigabyte B860 Aorus Elite AX', 'Gigabyte', 'LGA1851', 'DDR5', 4, 2, 1, 1, 'ATX', 5099745, 65, 45, 'Black'),
(58, 'ASRock B860M Steel Legend', 'ASRock', 'LGA1851', 'DDR5', 4, 2, 1, 1, 'mATX', 4844745, 60, 55, 'Black'),
(59, 'ASUS TUF Gaming B860-Plus WiFi', 'ASUS', 'LGA1851', 'DDR5', 4, 1, 1, 1, 'ATX', 5354745, 70, 35, 'Black'),
(60, 'MSI MAG B860 Tomahawk WiFi', 'MSI', 'LGA1851', 'DDR5', 4, 2, 1, 1, 'ATX', 5609745, 75, 30, 'Black'),
(61, 'ASUS ROG Crosshair X670E Hero', 'ASUS', 'AM5', 'DDR5', 4, 2, 1, 1, 'ATX', 17849745, 120, 10, 'Black'),
(62, 'MSI MEG X670E Ace', 'MSI', 'AM5', 'DDR5', 4, 2, 1, 1, 'ATX', 15299745, 110, 15, 'Black'),
(63, 'Gigabyte X670E Aorus Master', 'Gigabyte', 'AM5', 'DDR5', 4, 2, 1, 1, 'ATX', 12749745, 100, 20, 'Black'),
(64, 'ASRock X670E Taichi', 'ASRock', 'AM5', 'DDR5', 4, 2, 1, 1, 'ATX', 12239745, 95, 25, 'Black'),
(65, 'ASUS Prime X670-P WiFi', 'ASUS', 'AM5', 'DDR5', 4, 2, 1, 1, 'ATX', 7394745, 80, 35, 'White'),
(66, 'MSI PRO X670-P WiFi', 'MSI', 'AM5', 'DDR5', 4, 2, 1, 1, 'ATX', 6629745, 75, 40, 'Black'),
(67, 'Gigabyte B650 Aorus Elite AX', 'Gigabyte', 'AM5', 'DDR5', 4, 2, 1, 1, 'ATX', 5354745, 65, 50, 'Black'),
(68, 'ASRock B650M Steel Legend', 'ASRock', 'AM5', 'DDR5', 4, 2, 1, 1, 'mATX', 5099745, 60, 55, 'Black'),
(69, 'ASUS TUF Gaming B650-Plus WiFi', 'ASUS', 'AM5', 'DDR5', 4, 1, 1, 1, 'ATX', 5609745, 70, 30, 'Black'),
(70, 'MSI MAG B650 Tomahawk WiFi', 'MSI', 'AM5', 'DDR5', 4, 2, 1, 1, 'ATX', 6119745, 75, 25, 'Black'),
(71, 'ROG Maximus Z790 Extreme', 'ASUS', 'LGA1700', 'DDR5', 4, 2, 2, 1, 'ATX', 17990000, 85, 15, 'Black'),
(72, 'MSI MEG Z790 GODLIKE', 'MSI', 'LGA1700', 'DDR5', 4, 2, 2, 1, 'ATX', 20990000, 90, 10, 'Black'),
(73, 'ASRock Z790 Taichi Carrara', 'ASRock', 'LGA1700', 'DDR5', 4, 4, 2, 1, 'ATX', 15990000, 75, 12, 'White'),
(74, 'Gigabyte Z790 AORUS XTREME', 'Gigabyte', 'LGA1700', 'DDR5', 4, 2, 2, 1, 'ATX', 18990000, 80, 8, 'Black'),
(75, 'ASUS ROG STRIX B650E-E', 'ASUS', 'AM5', 'DDR5', 4, 2, 2, 1, 'ATX', 12990000, 70, 20, 'Black'),
(76, 'MSI MAG X670E Tomahawk', 'MSI', 'AM5', 'DDR5', 4, 2, 2, 1, 'ATX', 14990000, 75, 15, 'Black'),
(77, 'ASRock X670E Taichi', 'ASRock', 'AM5', 'DDR5', 4, 4, 2, 1, 'ATX', 16990000, 78, 10, 'Black'),
(78, 'Gigabyte X670 AORUS XTREME', 'Gigabyte', 'AM5', 'DDR5', 4, 2, 2, 1, 'ATX', 19990000, 85, 12, 'Black'),
(79, 'ASUS ROG STRIX X670E-F', 'ASUS', 'AM5', 'DDR5', 4, 2, 2, 1, 'ATX', 13990000, 72, 18, 'Black'),
(80, 'MSI MEG X670E ACE', 'MSI', 'AM5', 'DDR5', 4, 2, 2, 1, 'ATX', 17990000, 80, 14, 'Black');

-- --------------------------------------------------------

--
-- Table structure for table `powersource`
--

DROP TABLE IF EXISTS `powersource`;
CREATE TABLE IF NOT EXISTS `powersource` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `manufacturer` varchar(100) NOT NULL,
  `price` double DEFAULT NULL,
  `power` int DEFAULT NULL,
  `stock` int DEFAULT NULL,
  `color` varchar(20) NOT NULL DEFAULT 'Black',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=61 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `powersource`
--

INSERT INTO `powersource` (`id`, `name`, `manufacturer`, `price`, `power`, `stock`, `color`) VALUES
(1, 'Corsair RM850x', 'Corsair', 3500000, 850, 10, 'Black'),
(2, 'Seasonic Focus GX-750', 'Seasonic', 3000000, 750, 10, 'Black'),
(3, 'EVGA SuperNOVA 1000 G6', 'EVGA', 4500000, 1000, 10, 'Black'),
(4, 'MSI MPG A850G PCIE5', 'MSI', 4000000, 850, 10, 'Black'),
(5, 'ASUS ROG Strix 750W', 'ASUS', 3200000, 750, 10, 'White'),
(6, 'Cooler Master MWE Gold 650', 'Cooler Master', 2500000, 650, 10, 'Black'),
(7, 'Thermaltake Toughpower GF3 1200', 'Thermaltake', 5500000, 1200, 10, 'Black'),
(8, 'Gigabyte UD850GM', 'Gigabyte', 3300000, 850, 10, 'Black'),
(9, 'Corsair HX1000', 'Corsair', 5000000, 1000, 10, 'Black'),
(10, 'Seasonic Prime TX-850', 'Seasonic', 4500000, 850, 10, 'Black'),
(11, 'EVGA 650 GQ', 'EVGA', 2200000, 650, 10, 'Black'),
(12, 'MSI MAG A750GL', 'MSI', 3500000, 750, 10, 'White'),
(13, 'ASUS TUF Gaming 850W', 'ASUS', 3800000, 850, 10, 'Black'),
(14, 'Cooler Master V850 SFX', 'Cooler Master', 4000000, 850, 10, 'Black'),
(15, 'Thermaltake Smart 500W', 'Thermaltake', 1500000, 500, 10, 'Black'),
(16, 'Gigabyte P750GM', 'Gigabyte', 2800000, 750, 10, 'Black'),
(17, 'Corsair CX650M', 'Corsair', 2000000, 650, 10, 'Black'),
(18, 'Seasonic Vertex GX-1000', 'Seasonic', 4800000, 1000, 10, 'White'),
(19, 'EVGA 550 B5', 'EVGA', 1800000, 550, 10, 'Black'),
(20, 'Cooler Master Elite V3 600', 'Cooler Master', 1600000, 600, 10, 'Black'),
(21, 'Corsair AX1600i', 'Corsair', 12500000, 1600, 10, 'Black'),
(22, 'Seasonic Prime TX-1600', 'Seasonic', 13000000, 1600, 10, 'Black'),
(23, 'EVGA SuperNOVA 1600 T2', 'EVGA', 11500000, 1600, 10, 'Black'),
(24, 'ASUS ROG Thor 1200P', 'ASUS', 9500000, 1200, 10, 'Black'),
(25, 'Cooler Master V1300 Platinum', 'Cooler Master', 9800000, 1300, 10, 'Black'),
(26, 'Thermaltake Toughpower iRGB 1250', 'Thermaltake', 9200000, 1250, 10, 'Black'),
(27, 'Gigabyte AORUS P1200W', 'Gigabyte', 9000000, 1200, 10, 'Black'),
(28, 'Corsair HX1500i', 'Corsair', 11000000, 1500, 10, 'Black'),
(29, 'Seasonic Prime PX-1300', 'Seasonic', 10500000, 1300, 10, 'Black'),
(30, 'EVGA SuperNOVA 1300 G2', 'EVGA', 9500000, 1300, 10, 'Black'),
(31, 'MSI MPG A1300G PCIE5', 'MSI', 9800000, 1300, 10, 'Black'),
(32, 'ASUS ROG Thor 1600T', 'ASUS', 13500000, 1600, 10, 'White'),
(33, 'Cooler Master V1200 Platinum', 'Cooler Master', 9200000, 1200, 10, 'Black'),
(34, 'Thermaltake Toughpower PF1 1550', 'Thermaltake', 11500000, 1550, 10, 'Black'),
(35, 'Gigabyte UD1300GM PG5', 'Gigabyte', 9700000, 1300, 10, 'Black'),
(36, 'Corsair HX1200 Platinum', 'Corsair', 8800000, 1200, 10, 'Black'),
(37, 'Seasonic Vertex PX-1200', 'Seasonic', 9400000, 1200, 10, 'Black'),
(38, 'EVGA SuperNOVA 1600 P2', 'EVGA', 12000000, 1600, 10, 'Black'),
(39, 'MSI MPG A1200G', 'MSI', 9100000, 1200, 10, 'Black'),
(40, 'Thermaltake Toughpower Grand 1350', 'Thermaltake', 10000000, 1350, 10, 'Black'),
(41, 'Corsair AX1700i Titanium', 'Corsair', 15000000, 1700, 10, 'Black'),
(42, 'Seasonic Prime TX-1800', 'Seasonic', 16000000, 1800, 10, 'Black'),
(43, 'EVGA SuperNOVA 2000 T2', 'EVGA', 17500000, 2000, 10, 'Black'),
(44, 'ASUS ROG Thor 1800P', 'ASUS', 15500000, 1800, 10, 'White'),
(45, 'Cooler Master V2000 Platinum', 'Cooler Master', 16500000, 2000, 10, 'Black'),
(46, 'Thermaltake Toughpower iRGB 1700', 'Thermaltake', 14500000, 1700, 10, 'Black'),
(47, 'Gigabyte AORUS P1800W', 'Gigabyte', 15200000, 1800, 10, 'Black'),
(48, 'Corsair HX2000i', 'Corsair', 18000000, 2000, 10, 'Black'),
(49, 'Seasonic Prime PX-1900', 'Seasonic', 17000000, 1900, 10, 'Black'),
(50, 'EVGA SuperNOVA 1800 G2', 'EVGA', 15800000, 1800, 10, 'Black'),
(51, 'MSI MPG A2000G PCIE5', 'MSI', 16800000, 2000, 10, 'Black'),
(52, 'ASUS ROG Strix 1700W', 'ASUS', 14800000, 1700, 10, 'Black'),
(53, 'Cooler Master V1800 SFX', 'Cooler Master', 16000000, 1800, 10, 'Black'),
(54, 'Thermaltake Toughpower PF3 2000', 'Thermaltake', 17500000, 2000, 10, 'Black'),
(55, 'Gigabyte UD2000GM PG5', 'Gigabyte', 16500000, 2000, 10, 'Black'),
(56, 'Corsair AX1800i', 'Corsair', 16200000, 1800, 10, 'Black'),
(57, 'Seasonic Vertex PX-1700', 'Seasonic', 15500000, 1700, 10, 'Black'),
(58, 'EVGA SuperNOVA 1900 P3', 'EVGA', 17000000, 1900, 10, 'Black'),
(59, 'MSI MPG A1800G', 'MSI', 15800000, 1800, 10, 'Black'),
(60, 'Thermaltake Toughpower Grand 2000', 'Thermaltake', 18000000, 2000, 10, 'Black');

-- --------------------------------------------------------

--
-- Table structure for table `ram`
--

DROP TABLE IF EXISTS `ram`;
CREATE TABLE IF NOT EXISTS `ram` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `manufacturer` varchar(100) NOT NULL,
  `ramType` varchar(20) DEFAULT NULL,
  `bus` int DEFAULT NULL,
  `capacity` int DEFAULT NULL,
  `benchmark` double DEFAULT NULL,
  `price` double DEFAULT NULL,
  `power` int DEFAULT NULL,
  `stock` int DEFAULT NULL,
  `color` varchar(20) NOT NULL DEFAULT 'Black',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=26 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `ram`
--

INSERT INTO `ram` (`id`, `name`, `manufacturer`, `ramType`, `bus`, `capacity`, `benchmark`, `price`, `power`, `stock`, `color`) VALUES
(1, 'Corsair Vengeance LPX', 'Corsair', 'DDR4', 3200, 16, 5120, 1250000, 8, 10, 'Black'),
(2, 'G.Skill Trident Z5 RGB', 'G.Skill', 'DDR5', 6000, 32, 19200, 4500000, 15, 10, 'White'),
(3, 'Kingston Fury Beast', 'Kingston', 'DDR4', 3600, 8, 2880, 900000, 7, 10, 'Black'),
(4, 'TeamGroup T-Force Xtreem', 'TeamGroup', 'DDR4', 4000, 16, 6400, 1500000, 9, 10, 'White'),
(5, 'Patriot Viper Steel', 'Patriot', 'DDR4', 4400, 32, 14080, 3000000, 12, 10, 'White'),
(6, 'Crucial Ballistix', 'Crucial', 'DDR4', 3200, 8, 2560, 800000, 6, 10, 'Black'),
(7, 'HyperX Predator', 'HyperX', 'DDR4', 3600, 16, 5760, 1400000, 10, 10, 'Black'),
(8, 'Corsair Dominator Platinum', 'Corsair', 'DDR5', 5600, 64, 35840, 7000000, 18, 10, 'White'),
(9, 'G.Skill Ripjaws V', 'G.Skill', 'DDR4', 3000, 16, 4800, 1200000, 8, 10, 'Black'),
(10, 'Adata XPG Spectrix', 'Adata', 'DDR4', 4133, 32, 13225.6, 2800000, 13, 10, 'White'),
(11, 'Kingston Fury Renegade', 'Kingston', 'DDR5', 6400, 16, 10240, 3500000, 16, 10, 'White'),
(12, 'TeamGroup Elite', 'TeamGroup', 'DDR4', 2666, 8, 2132.8, 700000, 5, 10, 'Black'),
(13, 'Patriot Signature Line', 'Patriot', 'DDR4', 2400, 4, 960, 500000, 4, 10, 'Black'),
(14, 'Crucial Pro', 'Crucial', 'DDR5', 5200, 32, 16640, 4000000, 14, 10, 'White'),
(15, 'Corsair Vengeance RGB Pro', 'Corsair', 'DDR4', 3600, 32, 11520, 2500000, 11, 10, 'White'),
(16, 'G.Skill Trident Z Neo', 'G.Skill', 'DDR4', 3800, 16, 6080, 1600000, 9, 10, 'White'),
(17, 'HyperX Impact', 'HyperX', 'DDR4', 3200, 8, 2560, 850000, 6, 10, 'Black'),
(18, 'Adata Premier', 'Adata', 'DDR4', 2666, 16, 4265.6, 1000000, 7, 10, 'Black'),
(19, 'Kingston ValueRAM', 'Kingston', 'DDR4', 2133, 4, 852, 450000, 3, 10, 'Black'),
(20, 'TeamGroup Delta RGB', 'TeamGroup', 'DDR5', 6000, 32, 19200, 4600000, 15, 10, 'White'),
(21, 'G.Skill Trident Z5 RGB', 'G.Skill', 'DDR5', 6000, 32, 19200, 4500000, 15, 10, 'Black'),
(22, 'Corsair Dominator Platinum', 'Corsair', 'DDR5', 5600, 64, 35840, 7000000, 18, 10, 'Black'),
(23, 'Kingston Fury Renegade', 'Kingston', 'DDR5', 6400, 16, 10240, 3500000, 16, 10, 'Black'),
(24, 'Crucial Pro', 'Crucial', 'DDR5', 5200, 32, 16640, 4000000, 14, 10, 'Black'),
(25, 'TeamGroup Delta RGB', 'TeamGroup', 'DDR5', 6000, 32, 19200, 4600000, 15, 10, 'Black');
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
