-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Máy chủ: 127.0.0.1
-- Thời gian đã tạo: Th12 30, 2023 lúc 02:43 PM
-- Phiên bản máy phục vụ: 10.4.28-MariaDB
-- Phiên bản PHP: 8.2.4

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Cơ sở dữ liệu: `pbl4_ftp`
--

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `account`
--

CREATE TABLE `account` (
  `username` varchar(100) NOT NULL,
  `password` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `account`
--

INSERT INTO `account` (`username`, `password`) VALUES
('danh', 'ftpserver'),
('hungthinh', 'ftpserver'),
('huong', 'ftpserver'),
('loc', '2134644'),
('quan', 'ftpserver'),
('tam', 'ftpserver');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `employeegroup`
--

CREATE TABLE `employeegroup` (
  `iduser` int(11) NOT NULL,
  `idgroup` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `employeegroup`
--

INSERT INTO `employeegroup` (`iduser`, `idgroup`) VALUES
(1, 1),
(5, 1),
(1, 9),
(10, 9);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `groupofcompany`
--

CREATE TABLE `groupofcompany` (
  `idgroup` int(11) NOT NULL,
  `namegroup` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `groupofcompany`
--

INSERT INTO `groupofcompany` (`idgroup`, `namegroup`) VALUES
(1, 'Phòng Nhân Sự'),
(4, 'Phòng Điều Hành'),
(9, 'PBL4');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `information`
--

CREATE TABLE `information` (
  `iduser` int(11) NOT NULL,
  `name` varchar(100) NOT NULL,
  `sdt` varchar(100) NOT NULL,
  `email` varchar(100) NOT NULL,
  `username` varchar(100) NOT NULL,
  `namegroup` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `information`
--

INSERT INTO `information` (`iduser`, `name`, `sdt`, `email`, `username`, `namegroup`) VALUES
(1, 'Nguyễn Phan Bảo Lộc', '0905721218', 'npbl.cntt@gmail.com', 'loc', NULL),
(5, 'Võ Anh Quân', '01231313123', 'vaquan@gmail.com', 'quan', NULL),
(6, 'Thảo Tâm', '010230123013', '12313123', 'tam', NULL),
(7, 'Hoàng Nguyễn Hưng Thịnh', '1231223123', 'jjjjjjjjj@gmail.com', 'hungthinh', NULL),
(9, 'Nguyễn Thị Diễm Hương', '023496712', 'ntdhuong@gmail.com', 'huong', NULL),
(10, 'Lê Hoàng Danh', '0123123123', 'lehdanh@gmail.com', 'danh', NULL);

--
-- Chỉ mục cho các bảng đã đổ
--

--
-- Chỉ mục cho bảng `account`
--
ALTER TABLE `account`
  ADD PRIMARY KEY (`username`);

--
-- Chỉ mục cho bảng `employeegroup`
--
ALTER TABLE `employeegroup`
  ADD KEY `iduser` (`iduser`),
  ADD KEY `idgroup` (`idgroup`);

--
-- Chỉ mục cho bảng `groupofcompany`
--
ALTER TABLE `groupofcompany`
  ADD PRIMARY KEY (`idgroup`);

--
-- Chỉ mục cho bảng `information`
--
ALTER TABLE `information`
  ADD PRIMARY KEY (`iduser`),
  ADD KEY `username` (`username`);

--
-- AUTO_INCREMENT cho các bảng đã đổ
--

--
-- AUTO_INCREMENT cho bảng `groupofcompany`
--
ALTER TABLE `groupofcompany`
  MODIFY `idgroup` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- AUTO_INCREMENT cho bảng `information`
--
ALTER TABLE `information`
  MODIFY `iduser` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

--
-- Các ràng buộc cho các bảng đã đổ
--

--
-- Các ràng buộc cho bảng `employeegroup`
--
ALTER TABLE `employeegroup`
  ADD CONSTRAINT `employeegroup_ibfk_1` FOREIGN KEY (`iduser`) REFERENCES `information` (`iduser`),
  ADD CONSTRAINT `employeegroup_ibfk_2` FOREIGN KEY (`idgroup`) REFERENCES `groupofcompany` (`idgroup`);

--
-- Các ràng buộc cho bảng `information`
--
ALTER TABLE `information`
  ADD CONSTRAINT `information_ibfk_1` FOREIGN KEY (`username`) REFERENCES `account` (`username`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
