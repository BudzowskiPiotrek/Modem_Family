-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 19-12-2025 a las 17:18:46
-- Versión del servidor: 10.4.32-MariaDB
-- Versión de PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `centimetromalaga`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `accesos_estaciones`
--

CREATE TABLE `accesos_estaciones` (
  `id_acceso` int(11) NOT NULL,
  `descripcion` varchar(200) DEFAULT NULL,
  `fk_id_estacion` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `cocheras`
--

CREATE TABLE `cocheras` (
  `id_cochera` int(11) NOT NULL,
  `nombre` varchar(50) NOT NULL,
  `direccion` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `cocheras`
--

INSERT INTO `cocheras` (`id_cochera`, `nombre`, `direccion`) VALUES
(1, 'cochera1', 'micasa');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `estaciones`
--

CREATE TABLE `estaciones` (
  `id_estacion` int(11) NOT NULL,
  `nombre` varchar(50) NOT NULL,
  `direccion` varchar(100) NOT NULL,
  `num_accesos` int(11) DEFAULT NULL,
  `cant_lineas` int(11) DEFAULT NULL,
  `cant_viajes` int(11) DEFAULT NULL,
  `cant_origen` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `ftp_file_ownership`
--

CREATE TABLE `ftp_file_ownership` (
  `filename` varchar(255) NOT NULL,
  `uploaded_by` varchar(100) NOT NULL,
  `upload_date` timestamp NOT NULL DEFAULT current_timestamp(),
  `file_type` enum('FILE','FOLDER') DEFAULT 'FILE'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `ftp_file_ownership`
--

INSERT INTO `ftp_file_ownership` (`filename`, `uploaded_by`, `upload_date`, `file_type`) VALUES
('hola', 'prueba', '2025-12-18 12:38:51', 'FOLDER');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `lineas`
--

CREATE TABLE `lineas` (
  `id_linea` int(11) NOT NULL,
  `nombre_linea` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `lineas`
--

INSERT INTO `lineas` (`id_linea`, `nombre_linea`) VALUES
(1, 'Linea 5'),
(2, 'Linea 2');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `lineas_estaciones`
--

CREATE TABLE `lineas_estaciones` (
  `fk_id_linea` int(11) NOT NULL,
  `fk_id_estacion` int(11) NOT NULL,
  `orden` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `lista_blanca`
--

CREATE TABLE `lista_blanca` (
  `correo_electronico` varchar(100) NOT NULL,
  `nombre` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `lista_blanca`
--

INSERT INTO `lista_blanca` (`correo_electronico`, `nombre`) VALUES
('agus@tin.com', 'agus'),
('p97834480@gmail.com', 'alumno'),
('proyectoprofesor8@gmail.com', 'profesor');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `logs`
--

CREATE TABLE `logs` (
  `id_log` int(11) NOT NULL,
  `accion` varchar(200) DEFAULT NULL,
  `username` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `logs`
--

INSERT INTO `logs` (`id_log`, `accion`, `username`) VALUES
(1, 'accion', 'admin'),
(5, 'Successful login attempt', 'admin'),
(6, 'Successful login attempt', 'admin'),
(7, 'Successful login attempt', 'admin'),
(8, 'Successful login attempt', 'admin'),
(9, 'Successful login attempt', 'admin'),
(10, 'Successful login attempt', 'admin'),
(11, 'Successful login attempt', 'admin'),
(13, 'Successful login attempt', 'admin'),
(14, 'Successful login attempt', 'admin'),
(15, 'Successful login attempt', 'admin'),
(16, 'Successful login attempt', 'admin'),
(17, 'Successful login attempt', 'admin'),
(19, 'Successful login attempt', 'alumno'),
(20, 'Successful login attempt', 'alumno'),
(21, 'Successful login attempt', 'alumno'),
(22, 'Successful login attempt', 'alumno'),
(23, 'Successful login attempt', 'admin'),
(24, 'Successful login attempt', 'admin'),
(25, 'Successful login attempt', 'admin'),
(26, 'Successful login attempt', 'admin'),
(27, 'Successful login attempt', 'alumno'),
(28, 'Successful login attempt', 'alumno'),
(29, 'Successful login attempt', 'admin'),
(30, 'Successful login attempt', 'admin'),
(31, 'Successful login attempt', 'admin'),
(32, 'Successful login attempt', 'admin'),
(33, 'Successful login attempt', 'alumno'),
(34, 'Successful login attempt', 'alumno'),
(35, 'Successful login attempt', 'admin'),
(36, 'Successful login attempt', 'alumno'),
(37, 'Successful login attempt', 'admin'),
(38, 'Successful login attempt', 'admin'),
(39, 'Successful login attempt', 'alumno'),
(40, 'Successful login attempt', 'alumno'),
(41, 'Successful login attempt', 'admin'),
(42, 'Successful login attempt', 'dani'),
(43, 'Successful login attempt', 'admin'),
(44, 'Successful login attempt', 'dani'),
(45, 'Successful login attempt', 'admin'),
(46, 'Successful login attempt', 'alumno'),
(47, 'Successful login attempt', 'alumno'),
(48, 'Successful login attempt', 'profesor'),
(49, 'Successful login attempt', 'admin'),
(50, 'Successful login attempt', 'profesor'),
(51, 'Successful login attempt', 'alumno'),
(53, 'Successful login attempt', 'profesor'),
(56, 'Successful login attempt', 'dani'),
(57, 'Successful login attempt', 'alumno'),
(59, 'Successful login attempt', 'alumno'),
(60, 'Successful login attempt', 'alumno'),
(61, 'Successful login attempt', 'dani'),
(62, 'Successful login attempt', 'alumno'),
(63, 'Successful login attempt', 'admin'),
(64, 'Successful login attempt', 'alumno'),
(65, 'Successful login attempt', 'alumno'),
(66, 'Successful login attempt', 'alumno'),
(67, 'Successful login attempt', 'dani'),
(68, 'Successful login attempt', 'admin'),
(69, 'Successful login attempt', 'admin'),
(70, 'Successful login attempt', 'admin'),
(71, 'Successful login attempt', 'alumno'),
(72, 'Successful login attempt', 'admin'),
(73, 'Successful login attempt', 'alumno'),
(74, 'Successful login attempt', 'admin'),
(75, 'Successful login attempt', 'admin'),
(76, 'Successful login attempt', 'alumno'),
(77, 'Successful login attempt', 'admin'),
(78, 'Successful login attempt', 'admin'),
(79, 'Successful login attempt', 'admin'),
(80, 'Successful login attempt', 'dani'),
(82, 'Successful login attempt', 'admin'),
(83, 'Successful login attempt', 'admin'),
(84, 'Successful login attempt', 'admin'),
(85, 'Successful login attempt', 'admin'),
(86, 'Successful login attempt', 'alumno'),
(87, 'Successful login attempt', 'alumno'),
(88, 'Successful login attempt', 'admin'),
(89, 'Successful login attempt', 'dani'),
(90, 'Successful login attempt', 'alumno'),
(91, 'Successful login attempt', 'dani'),
(92, 'Successful login attempt', 'alumno'),
(93, 'Successful login attempt', 'alumno'),
(94, 'Successful login attempt', 'admin'),
(95, 'Successful login attempt', 'alumno'),
(96, 'Successful login attempt', 'alumno'),
(97, 'Successful login attempt', 'alumno'),
(98, 'Successful login attempt', 'admin'),
(99, 'Successful login attempt', 'alumno'),
(100, 'Successful login attempt', 'admin'),
(101, 'Successful login attempt', 'alumno'),
(102, 'Successful login attempt', 'alumno'),
(103, 'Successful login attempt', 'admin'),
(104, 'Successful login attempt', 'admin'),
(105, 'Successful login attempt', 'alumno'),
(106, 'Successful login attempt', 'admin'),
(107, 'Successful login attempt', 'alumno'),
(108, 'Successful login attempt', 'admin'),
(109, 'File uploaded:prueba_upload_1765532115838.txt', 'admin'),
(110, 'File uploaded:Nueva carpeta', 'admin'),
(111, 'File downloaded:C:\\Users\\Wilku\\Desktop\\prueba_upload_1765532115838.txt', 'admin'),
(112, 'Successful login attempt', 'admin'),
(113, 'File uploaded:Base de datos SQL.txt', 'admin'),
(114, 'Successful login attempt', 'admin'),
(115, 'Successful login attempt', 'admin'),
(116, 'Successful login attempt', 'alumno'),
(117, 'Successful login attempt', 'admin'),
(118, 'File uploaded:GitHub Desktop.lnk', 'admin'),
(119, 'File uploaded:Actividad unidad 6.docx', 'admin'),
(120, 'File uploaded:01-MANEJO DE FICHEROS.pdf', 'admin'),
(121, 'Successful login attempt', 'admin'),
(122, 'Successful login attempt', 'admin'),
(123, 'Successful login attempt', 'admin'),
(124, 'Successful login attempt', 'admin'),
(125, 'File delete:fsdfsdfds', 'admin'),
(126, 'Successful login attempt', 'admin'),
(127, 'File delete:GitHub Desktop.lnk', 'admin'),
(128, 'Successful login attempt', 'admin'),
(129, 'Successful login attempt', 'alumno'),
(130, 'Successful login attempt', 'admin'),
(131, 'File delete:01-MANEJO DE FICHEROS.pdf', 'admin'),
(132, 'File delete:Actividad unidad 6.docx', 'admin'),
(133, 'File delete:Base de datos SQL.txt', 'admin'),
(134, 'File delete:prueba.sql', 'admin'),
(135, 'File delete:prueba_upload_1765532115838.txt', 'admin'),
(136, 'Successful login attempt', 'alumno'),
(137, 'Successful login attempt', 'admin'),
(138, 'Successful login attempt', 'admin'),
(139, 'Successful login attempt', 'admin'),
(140, 'Successful login attempt', 'admin'),
(141, 'Successful login attempt', 'alumno'),
(142, 'Test de login', 'admin'),
(144, 'Test de login', 'admin'),
(146, 'Test de login', 'admin'),
(148, 'Successful login attempt', 'admin'),
(149, 'Successful login attempt', 'admin'),
(150, 'Test de login', 'admin'),
(152, 'Successful login attempt', 'admin'),
(153, 'Test de login', 'admin'),
(155, 'Successful login attempt', 'admin'),
(156, 'File uploaded:.gitattributes', 'admin'),
(157, 'File delete:.gitattributes', 'admin'),
(158, 'Successful login attempt', 'admin'),
(159, 'Create folder:dsadsa', 'admin'),
(160, 'Successful login attempt', 'alumno'),
(161, 'Successful login attempt', 'admin'),
(162, 'File uploaded:Actividad unidad 6.docx', 'admin'),
(163, 'File delete:Actividad unidad 6.docx', 'admin'),
(164, 'Successful login attempt', 'admin'),
(165, 'Successful login attempt', 'admin'),
(166, 'File uploaded:Actividad unidad 5.docx', 'admin'),
(167, 'File uploaded:GitHub Desktop.lnk', 'admin'),
(168, 'File delete:Actividad unidad 5.docx', 'admin'),
(169, 'Successful login attempt', 'alumno'),
(170, 'Successful login attempt', 'alumno'),
(171, 'Successful login attempt', 'alumno'),
(172, 'Successful login attempt', 'admin'),
(173, 'Successful login attempt', 'alumno'),
(174, 'Successful login attempt', 'admin'),
(175, 'Successful login attempt', 'admin'),
(176, 'File uploaded:README.md', 'admin'),
(177, 'Successful login attempt', 'admin'),
(178, 'Successful login attempt', 'admin'),
(179, 'Successful login attempt', 'admin'),
(180, 'File uploaded:.gitignore', 'admin'),
(181, 'File uploaded:Actividad unidad 5.docx', 'admin'),
(182, 'Successful login attempt', 'admin'),
(183, 'File uploaded:IntelliJ IDEA 2025.2.1.lnk', 'admin'),
(184, 'File uploaded:Dart-cheat-sheet.pdf', 'admin'),
(185, 'Successful login attempt', 'alumno'),
(186, 'File uploaded:Base de datos SQL.txt', 'admin'),
(187, 'Successful login attempt', 'profesor'),
(188, 'Successful login attempt', 'admin'),
(189, 'Successful login attempt', 'admin'),
(190, 'File uploaded:Start FileZilla Server.lnk', 'admin'),
(191, 'Successful login attempt', 'admin'),
(192, 'Successful login attempt', 'admin'),
(193, 'Successful login attempt', 'admin'),
(194, 'File uploaded:Manual configuracion virtual box.pdf', 'admin'),
(195, 'File uploaded:.classpath', 'admin'),
(196, 'Test de login', 'admin'),
(198, 'Successful login attempt', 'admin'),
(199, 'Successful login attempt', 'admin'),
(200, 'File uploaded:README.md', 'admin'),
(201, 'File uploaded:Ionic.pdf', 'admin'),
(202, 'Successful login attempt', 'admin'),
(203, 'Successful login attempt', 'admin'),
(204, 'Successful login attempt', 'admin'),
(205, 'Successful login attempt', 'admin'),
(206, 'File delete:Start FileZilla Server.lnk', 'admin'),
(207, 'Successful login attempt', 'admin'),
(208, 'File delete:README.md', 'admin'),
(209, 'File uploaded:pom.xml', 'admin'),
(210, 'File uploaded:Base de datos SQL.txt', 'admin'),
(211, 'Successful login attempt', 'profesor'),
(212, 'File uploaded:Seminario Git.pdf', 'admin'),
(213, 'Successful login attempt', 'admin'),
(214, 'File uploaded:Nuevo documento de texto.txt', 'admin'),
(215, 'File uploaded:BlueStacks 5.lnk', 'admin'),
(216, 'Successful login attempt', 'admin'),
(217, 'New Folder: ddsadsa', 'admin'),
(218, 'New Folder: dasdsa', 'admin'),
(219, 'File renamed :ddsadsa for: carpeta', 'admin'),
(220, 'Successful login attempt', 'profesor'),
(221, 'Successful login attempt', 'admin'),
(222, 'Successful login attempt', 'admin'),
(223, 'Successful login attempt', 'profesor'),
(224, 'Successful login attempt', 'admin'),
(225, 'Successful login attempt', 'admin'),
(226, 'Successful login attempt', 'admin'),
(227, 'File uploaded:exa1prsp2526.pdf', 'admin'),
(228, 'Successful login attempt', 'admin'),
(229, 'Successful login attempt', 'admin'),
(230, 'Successful login attempt', 'admin'),
(231, 'File delete:Base de datos SQL.txt', 'admin'),
(232, 'File delete:Ionic.pdf', 'admin'),
(233, 'File delete:Seminario Git.pdf', 'admin'),
(234, 'File delete:pom.xml', 'admin'),
(235, 'File delete:Manual configuracion virtual box.pdf', 'admin'),
(236, 'File delete:Nuevo documento de texto.txt', 'admin'),
(237, 'File delete:exa1prsp2526.pdf', 'admin'),
(238, 'File delete:IntelliJ IDEA 2025.2.1.lnk', 'admin'),
(239, 'File delete:BlueStacks 5.lnk', 'admin'),
(240, 'File delete:.classpath', 'admin'),
(241, 'Successful login attempt', 'admin'),
(242, 'File uploaded:Apuntes.txt', 'admin'),
(243, 'File delete:Apuntes.txt', 'admin'),
(244, 'Successful login attempt', 'dani'),
(245, 'File uploaded:final 1ª eval PRSP 2425.pdf', 'dani'),
(246, 'New Folder: cosa', 'dani'),
(247, 'File uploaded:final 1ª eval PRSP 2425.pdf', 'dani'),
(248, 'File delete:final 1Âª eval PRSP 2425.pdf', 'dani'),
(249, 'File uploaded:Actividad unidad 6.docx', 'admin'),
(250, 'Successful login attempt', 'admin'),
(251, 'File delete:Dart-cheat-sheet.pdf', 'admin'),
(252, 'File uploaded:League of Legends.lnk', 'dani'),
(253, 'Successful login attempt', 'admin'),
(254, 'Successful login attempt', 'profesor'),
(255, 'Successful login attempt', 'admin'),
(256, 'Successful login attempt', 'admin'),
(257, 'Successful login attempt', 'profesor'),
(258, 'Login attempt failed', 'alumno'),
(259, 'Successful login attempt', 'profesor'),
(260, 'Successful login attempt', 'admin'),
(261, 'New Folder: reter', 'admin'),
(262, 'Successful login attempt', 'dani'),
(263, 'Successful login attempt', 'profesor'),
(264, 'Successful login attempt', 'dani'),
(265, 'Successful login attempt', 'dani'),
(266, 'Successful login attempt', 'admin'),
(267, 'Successful login attempt', 'admin'),
(268, 'New Folder: hola1', 'admin'),
(269, 'Successful login attempt', 'profesor'),
(270, 'File delete:League of Legends.lnk', 'admin'),
(271, 'Successful login attempt', 'admin'),
(272, 'New Folder: 1', 'admin'),
(273, 'New Folder: 22', 'admin'),
(274, 'Successful login attempt', 'admin'),
(275, 'Successful login attempt', 'profesor'),
(276, 'Successful login attempt', 'admin'),
(277, 'Successful login attempt', 'profesor'),
(278, 'Successful login attempt', 'profesor'),
(279, 'Successful login attempt', 'admin'),
(280, 'Successful login attempt', 'profesor'),
(281, 'Successful login attempt', 'admin'),
(282, 'Successful login attempt', 'admin'),
(283, 'New Folder: 1212', 'admin'),
(284, 'Successful login attempt', 'profesor'),
(285, 'Successful login attempt', 'profesor'),
(286, 'Successful login attempt', 'admin'),
(287, 'File delete:1212', 'admin'),
(288, 'New Folder: 33', 'admin'),
(289, 'Successful login attempt', 'profesor'),
(290, 'Successful login attempt', 'dani'),
(291, 'Successful login attempt', 'profesor'),
(292, 'Successful login attempt', 'admin'),
(293, 'File delete:1', 'admin'),
(294, 'File delete:22', 'admin'),
(295, 'File delete:.gitignore', 'admin'),
(296, 'File delete:Actividad unidad 5.docx', 'admin'),
(297, 'File delete:carpeta', 'admin'),
(298, 'File delete:Actividad unidad 6.docx', 'admin'),
(299, 'File delete:cosa', 'admin'),
(300, 'File delete:hola', 'admin'),
(301, 'File delete:hola1', 'admin'),
(302, 'File delete:reter', 'admin'),
(303, 'New Folder: carpeta una', 'admin'),
(304, 'File uploaded:01-MANEJO DE FICHEROS.pdf', 'admin'),
(305, 'File uploaded:1 - CREACIÓN  DE HILOS EN JAVA.pdf', 'admin'),
(306, 'File uploaded:IntelliJ IDEA 2025.2.1.lnk', 'admin'),
(307, 'File uploaded:final 1ª eval PRSP 2425.pdf', 'dani'),
(308, 'Successful login attempt', 'dani'),
(309, 'Successful login attempt', 'profesor'),
(310, 'Successful login attempt', 'admin'),
(311, 'Successful login attempt', 'admin'),
(312, 'Successful login attempt', 'dani'),
(313, 'Successful login attempt', 'admin'),
(314, 'Successful login attempt', 'dani'),
(315, 'Successful login attempt', 'admin'),
(316, 'Successful login attempt', 'admin'),
(317, 'Successful login attempt', 'alumno'),
(318, 'Successful login attempt', 'admin'),
(319, 'Successful login attempt', 'alumno'),
(320, 'Login attempt failed', 'admin'),
(321, 'Successful login attempt', 'admin'),
(322, 'Successful login attempt', 'alumno'),
(323, 'Successful login attempt', 'alumno'),
(324, 'Login attempt failed', 'admin'),
(325, 'Successful login attempt', 'admin'),
(326, 'Successful login attempt', 'alumno'),
(327, 'Successful login attempt', 'admin'),
(328, 'Successful login attempt', 'alumno'),
(329, 'Successful login attempt', 'admin'),
(330, 'Successful login attempt', 'admin'),
(331, 'Successful login attempt', 'admin'),
(332, 'Successful login attempt', 'admin'),
(333, 'Successful login attempt', 'alumno'),
(334, 'Successful login attempt', 'admin'),
(335, 'Successful login attempt', 'admin'),
(336, 'Successful login attempt', 'admin'),
(337, 'Successful login attempt', 'admin'),
(338, 'Successful login attempt', 'admin'),
(339, 'New Folder: 1', 'admin'),
(340, 'New Folder: 12', 'admin'),
(341, 'New Folder: pruebaCrear', 'admin'),
(342, 'Successful login attempt', 'admin'),
(343, 'File delete:1', 'admin'),
(344, 'Successful login attempt', 'admin'),
(345, 'Successful login attempt', 'admin'),
(346, 'File uploaded:25.03-2025.txt', 'admin'),
(347, 'File uploaded:25.03-2025.txt', 'admin'),
(348, 'File uploaded:24.03-2025.txt', 'admin'),
(349, 'Successful login attempt', 'alumno'),
(350, 'Successful login attempt', 'admin'),
(351, 'File delete:carpeta una', 'admin'),
(352, 'Successful login attempt', 'alumno'),
(353, 'Login attempt failed', 'admin'),
(354, 'Successful login attempt', 'admin'),
(355, 'Successful login attempt', 'admin'),
(356, 'Successful login attempt', 'alumno'),
(357, 'Successful login attempt', 'alumno'),
(358, 'Successful login attempt', 'alumno'),
(359, 'Successful login attempt', 'alumno'),
(360, 'Successful login attempt', 'alumno'),
(361, 'Successful login attempt', 'admin'),
(362, 'Successful login attempt', 'admin'),
(363, 'Successful login attempt', 'admin'),
(364, 'Successful login attempt', 'admin'),
(365, 'Successful login attempt', 'alumno'),
(366, 'Successful login attempt', 'admin'),
(367, 'Successful login attempt', 'alumno'),
(368, 'Successful login attempt', 'admin'),
(369, 'Successful login attempt', 'admin'),
(370, 'Successful login attempt', 'admin'),
(371, 'Successful login attempt', 'admin'),
(372, 'Login attempt failed', 'admin'),
(373, 'Successful login attempt', 'admin'),
(374, 'Successful login attempt', 'admin'),
(375, 'Successful login attempt', 'alumno'),
(376, 'Successful login attempt', 'admin'),
(377, 'New Folder: hola', 'admin'),
(378, 'Successful login attempt', 'admin'),
(379, 'New Folder: werrwe', 'admin'),
(380, 'New Folder: puta', 'admin'),
(381, 'Successful login attempt', 'profesor'),
(382, 'Successful login attempt', 'admin'),
(383, 'Successful login attempt', 'admin'),
(384, 'Successful login attempt', 'alumno'),
(385, 'Successful login attempt', 'alumno'),
(386, 'Successful login attempt', 'alumno'),
(387, 'Successful login attempt', 'alumno'),
(388, 'Successful login attempt', 'admin'),
(389, 'Successful login attempt', 'admin'),
(390, 'Successful login attempt', 'alumno'),
(391, 'File delete:IntelliJ IDEA 2025.2.1.lnk', 'alumno'),
(392, 'Successful login attempt', 'admin'),
(393, 'Successful login attempt', 'admin'),
(394, 'New Folder: fdsfdsfds', 'admin'),
(395, 'Successful login attempt', 'alumno'),
(396, 'Successful login attempt', 'dani'),
(397, 'Successful login attempt', 'admin'),
(398, 'Successful login attempt', 'admin'),
(399, 'Successful login attempt', 'admin'),
(400, 'Successful login attempt', 'admin'),
(401, 'Successful login attempt', 'admin'),
(402, 'Successful login attempt', 'admin'),
(403, 'Successful login attempt', 'admin'),
(404, 'Successful login attempt', 'admin'),
(405, 'Successful login attempt', 'admin'),
(406, 'Successful login attempt', 'admin'),
(407, 'Successful login attempt', 'admin'),
(408, 'Test de login', 'admin'),
(410, 'Successful login attempt', 'admin'),
(411, 'Successful login attempt', 'admin'),
(412, 'Successful login attempt', 'admin'),
(413, 'Successful login attempt', 'admin'),
(414, 'Successful login attempt', 'admin'),
(415, 'Successful login attempt', 'admin'),
(416, 'Successful login attempt', 'dani'),
(417, 'Successful login attempt', 'admin'),
(418, 'Successful login attempt', 'admin'),
(419, 'Successful login attempt', 'admin'),
(420, 'New Folder: pruebaTraduccion', 'admin'),
(421, 'File renamed :pruebaTraduccion for: traduccion', 'admin'),
(422, 'File uploaded:Ejercicio3.sql', 'admin'),
(423, 'File delete:traduccion', 'admin'),
(424, 'Successful login attempt', 'admin'),
(425, 'Successful login attempt', 'admin'),
(426, 'Successful login attempt', 'admin'),
(427, 'Successful login attempt', 'admin'),
(428, 'Successful login attempt', 'dani'),
(429, 'Successful login attempt', 'admin'),
(430, 'Successful login attempt', 'admin'),
(431, 'Successful login attempt', 'admin'),
(432, 'Successful login attempt', 'admin'),
(433, 'Successful login attempt', 'alumno'),
(434, 'Successful login attempt', 'admin'),
(435, 'Successful login attempt', 'admin'),
(436, 'Successful login attempt', 'dani'),
(437, 'Successful login attempt', 'admin'),
(438, 'Successful login attempt', 'dani'),
(439, 'Successful login attempt', 'admin'),
(441, 'Successful login attempt', 'alumno'),
(442, 'Successful login attempt', 'admin'),
(443, 'Successful login attempt', 'admin'),
(444, 'Successful login attempt', 'admin'),
(445, 'Successful login attempt', 'admin'),
(446, 'Successful login attempt', 'admin'),
(447, 'Successful login attempt', 'alumno'),
(448, 'Successful login attempt', 'admin'),
(449, 'Successful login attempt', 'admin'),
(450, 'Successful login attempt', 'admin'),
(451, 'Successful login attempt', 'admin'),
(452, 'Successful login attempt', 'admin'),
(453, 'Successful login attempt', 'ADMIN'),
(454, 'Successful login attempt', 'admin'),
(455, 'Successful login attempt', 'admin'),
(456, 'Successful login attempt', 'admin'),
(457, 'Successful login attempt', 'admin'),
(458, 'Successful login attempt', 'admin'),
(459, 'New Folder: _', 'admin'),
(460, 'Successful login attempt', 'admin'),
(461, 'Successful login attempt', 'admin'),
(462, 'Successful login attempt', 'alumno'),
(463, 'Successful login attempt', 'alumno'),
(464, 'Successful login attempt', 'admin'),
(465, 'Successful login attempt', 'alumno'),
(466, 'Successful login attempt', 'alumno'),
(467, 'Successful login attempt', 'admin'),
(468, 'Successful login attempt', 'alumno'),
(469, 'Successful login attempt', 'admin'),
(470, 'Successful login attempt', 'admin'),
(471, 'Successful login attempt', 'admin'),
(472, 'Successful login attempt', 'admin'),
(473, 'Successful login attempt', 'alumno'),
(474, 'Successful login attempt', 'alumno'),
(475, 'Successful login attempt', 'alumno'),
(476, 'Successful login attempt', 'alumno'),
(477, 'Successful login attempt', 'alumno'),
(478, 'Successful login attempt', 'admin'),
(479, 'Successful login attempt', 'alumno'),
(480, 'Successful login attempt', 'admin'),
(481, 'Successful login attempt', 'admin'),
(482, 'File downloaded:C:\\Users\\Wilku\\Desktop\\No esta vacio tranquilo.txt', 'admin'),
(483, 'Successful login attempt', 'admin'),
(484, 'Successful login attempt', 'admin'),
(485, 'File downloaded:C:\\Users\\diego\\Desktop\\No esta vacio tranquilo.txt', 'admin'),
(486, 'Successful login attempt', 'alumno'),
(487, 'Successful login attempt', 'admin'),
(488, 'Successful login attempt', 'alumno'),
(489, 'Successful login attempt', 'alumno'),
(490, 'Successful login attempt', 'alumno'),
(491, 'Login attempt failed', 'alumno'),
(492, 'Login attempt failed', 'alumno'),
(493, 'Successful login attempt', 'admin'),
(494, 'Login attempt failed', 'admin'),
(495, 'Successful login attempt', 'admin'),
(496, 'Login attempt failed', 'alumno'),
(497, 'Login attempt failed', 'alumno'),
(498, 'Successful login attempt', 'admin'),
(499, 'Login attempt failed', 'admin'),
(500, 'Successful login attempt', 'alumno'),
(501, 'Login attempt failed', 'admin'),
(502, 'Login attempt failed', 'admin'),
(503, 'Login attempt failed', 'admin'),
(504, 'Login attempt failed', 'admin'),
(505, 'Successful login attempt', 'alumno'),
(506, 'Login attempt failed', 'admin'),
(507, 'Login attempt failed', 'admin'),
(508, 'Login attempt failed', 'dani'),
(509, 'Successful login attempt', 'admin'),
(510, 'Login attempt failed', 'alumno'),
(511, 'Successful login attempt', 'alumno'),
(512, 'Successful login attempt', 'admin'),
(513, 'Successful login attempt', 'admin'),
(514, 'Successful login attempt', 'admin'),
(515, 'Successful login attempt', 'admin'),
(516, 'Successful login attempt', 'admin'),
(517, 'Successful login attempt', 'admin'),
(518, 'Successful login attempt', 'alumno'),
(519, 'Successful login attempt', 'admin'),
(520, 'Successful login attempt', 'alumno'),
(521, 'Test de login', 'admin'),
(523, 'Test de login', 'admin'),
(525, 'Successful login attempt', 'admin'),
(526, 'Login attempt failed', 'admin'),
(527, 'Successful login attempt', 'alumno'),
(528, 'Successful login attempt', 'admin'),
(529, 'Successful login attempt', 'admin'),
(530, 'Successful login attempt', 'admin'),
(531, 'Successful login attempt', 'alumno'),
(532, 'Successful login attempt', 'admin'),
(533, 'Successful login attempt', 'admin'),
(534, 'Successful login attempt', 'alumno'),
(535, 'Successful login attempt', 'admin'),
(536, 'File delete:_', 'alumno'),
(537, 'File delete:No esta vacio tranquilo.txt', 'admin'),
(538, 'New Folder: Carpeta sin titulo', 'admin'),
(539, 'Successful login attempt', 'admin'),
(540, 'Successful login attempt', 'admin'),
(541, 'Successful login attempt', 'admin'),
(542, 'Login attempt failed', 'admin'),
(543, 'Successful login attempt', 'alumno'),
(544, 'Successful login attempt', 'admin'),
(545, 'Successful login attempt', 'admin'),
(546, 'Successful login attempt', 'admin'),
(547, 'Successful login attempt', 'admin'),
(548, 'New Folder: hola', 'admin'),
(549, 'New Folder: oo', 'admin'),
(550, 'File delete:oo', 'admin'),
(551, 'File delete:hola', 'admin'),
(552, 'Login attempt failed', 'admin'),
(553, 'New Folder: hola', 'admin'),
(554, 'Login attempt failed', 'admin'),
(555, 'File delete:hola', 'admin'),
(556, 'Successful login attempt', 'admin'),
(557, 'Successful login attempt', 'admin'),
(558, 'Successful login attempt', 'admin'),
(559, 'Successful login attempt', 'admin'),
(560, 'New Folder: ter', 'admin'),
(561, 'File uploaded:IntelliJ IDEA 2025.2.1.lnk', 'admin'),
(562, 'Successful login attempt', 'alumno'),
(563, 'File delete:ter', 'admin'),
(564, 'Successful login attempt', 'admin'),
(565, 'New Folder: 23', 'admin'),
(566, 'Successful login attempt', 'admin'),
(567, 'File delete:23', 'admin'),
(568, 'Successful login attempt', 'admin'),
(569, 'Successful login attempt', 'admin'),
(570, 'Successful login attempt', 'alumno'),
(573, 'Successful login attempt', 'admin'),
(574, 'Login attempt failed', 'admin'),
(575, 'New Folder: gfd', 'admin'),
(576, 'Successful login attempt', 'alumno'),
(577, 'Successful login attempt', 'alumno'),
(578, 'Successful login attempt', 'alumno'),
(579, 'Successful login attempt', 'dani'),
(580, 'File delete:ter', 'dani'),
(581, 'File delete:ter', 'dani'),
(582, 'Successful login attempt', 'admin'),
(583, 'Successful login attempt', 'admin'),
(584, 'Successful login attempt', 'dani'),
(585, 'File delete:ter', 'dani'),
(586, 'New Folder: rwerew', 'dani'),
(587, 'File uploaded:IntelliJ IDEA 2025ee.2.1.lnk', 'dani'),
(588, 'Successful login attempt', 'admin'),
(589, 'Successful login attempt', 'alumno'),
(590, 'Successful login attempt', 'dani'),
(593, 'Successful login attempt', 'alumno'),
(594, 'Successful login attempt', 'admin'),
(595, 'Successful login attempt', 'alumno'),
(596, 'Successful login attempt', 'alumno'),
(597, 'Successful login attempt', 'alumno'),
(598, 'Successful login attempt', 'alumno'),
(599, 'Successful login attempt', 'alumno'),
(600, 'Successful login attempt', 'admin'),
(601, 'Successful login attempt', 'alumno'),
(602, 'Successful login attempt', 'alumno'),
(604, 'Successful login attempt', 'admin'),
(605, 'Successful login attempt', 'alumno'),
(606, 'Successful login attempt', 'alumno'),
(607, 'Successful login attempt', 'alumno'),
(608, 'Successful login attempt', 'alumno'),
(609, 'Successful login attempt', 'alumno'),
(610, 'Successful login attempt', 'alumno'),
(611, 'Successful login attempt', 'ADMIN'),
(612, 'Successful login attempt', 'alumno'),
(613, 'Successful login attempt', 'alumno'),
(614, 'Successful login attempt', 'admin'),
(615, 'File delete:23', 'admin'),
(616, 'File delete:Carpeta sin titulo', 'admin'),
(617, 'File delete:gfd', 'admin'),
(618, 'File delete:IntelliJ IDEA 2025ee.2.1.lnk', 'admin'),
(619, 'File delete:rwerew', 'admin'),
(620, 'File delete:ter', 'admin'),
(621, 'Successful login attempt', 'dani'),
(622, 'New Folder: hola', 'dani'),
(623, 'Successful login attempt', 'admin'),
(624, 'Successful login attempt', 'prueba'),
(625, 'File delete:hola', 'prueba'),
(626, 'Successful login attempt', 'admin'),
(627, 'Successful login attempt', 'alumno'),
(628, 'Successful login attempt', 'alumno'),
(629, 'Successful login attempt', 'admin'),
(630, 'Successful login attempt', 'admin'),
(631, 'Successful login attempt', 'alumno'),
(632, 'New Folder: rew', 'admin'),
(633, 'Successful login attempt', 'alumno'),
(634, 'Successful login attempt', 'dani'),
(635, 'New Folder: hola dani', 'dani'),
(636, 'Successful login attempt', 'prueba'),
(637, 'New Folder: hola prueba', 'prueba'),
(638, 'Successful login attempt', 'alumno'),
(639, 'Successful login attempt', 'prueba'),
(640, 'New Folder: holaprueba1', 'prueba'),
(641, 'Successful login attempt', 'admin'),
(642, 'File uploaded:Actividad unidad 5.docx', 'prueba'),
(643, 'Successful login attempt', 'alumno'),
(644, 'Successful login attempt', 'admin'),
(645, 'Successful login attempt', 'alumno'),
(646, 'Successful login attempt', 'alumno'),
(647, 'Successful login attempt', 'alumno'),
(648, 'Login attempt failed', 'prueba'),
(649, 'Successful login attempt', 'alumno'),
(650, 'Successful login attempt', 'admin'),
(651, 'Successful login attempt', 'alumno'),
(652, 'Successful login attempt', 'alumno'),
(653, 'Successful login attempt', 'admin'),
(654, 'Successful login attempt', 'admin'),
(655, 'Login attempt failed', 'prueba'),
(656, 'Successful login attempt', 'alumno'),
(657, 'Successful login attempt', 'prueba'),
(658, 'New Folder: hola', 'prueba'),
(659, 'Successful login attempt', 'alumno'),
(660, 'Successful login attempt', 'alumno'),
(661, 'Successful login attempt', 'alumno'),
(662, 'Successful login attempt', 'alumno'),
(663, 'Successful login attempt', 'prueba'),
(664, 'New Folder: holaotravez', 'prueba'),
(665, 'File delete:holaotravez', 'prueba'),
(666, 'Successful login attempt', 'alumno'),
(667, 'Successful login attempt', 'alumno'),
(668, 'Successful login attempt', 'alumno'),
(669, 'Successful login attempt', 'alumno'),
(670, 'Successful login attempt', 'alumno'),
(671, 'Successful login attempt', 'alumno'),
(672, 'Successful login attempt', 'dani'),
(673, 'Successful login attempt', 'dani'),
(674, 'Successful login attempt', 'alumno'),
(675, 'Successful login attempt', 'alumno'),
(676, 'Successful login attempt', 'admin'),
(677, 'Successful login attempt', 'alumno'),
(678, 'Successful login attempt', 'alumno'),
(679, 'Successful login attempt', 'alumno'),
(680, 'Successful login attempt', 'admin'),
(681, 'Successful login attempt', 'prueba'),
(682, 'New Folder: hola', 'prueba'),
(683, 'Successful login attempt', 'dani'),
(684, 'Successful login attempt', 'admin'),
(685, 'Successful login attempt', 'dani'),
(686, 'Successful login attempt', 'dani'),
(687, 'Successful login attempt', 'admin'),
(688, 'Successful login attempt', 'admin'),
(689, 'Successful login attempt', 'admin'),
(690, 'Successful login attempt', 'admin'),
(691, 'Successful login attempt', 'alumno'),
(692, 'Successful login attempt', 'admin'),
(693, 'Successful login attempt', 'alumno'),
(694, 'Successful login attempt', 'admin'),
(695, 'Successful login attempt', 'alumno'),
(696, 'Successful login attempt', 'alumno'),
(697, 'Successful login attempt', 'alumno'),
(698, 'Successful login attempt', 'alumno'),
(699, 'Successful login attempt', 'admin'),
(700, 'Successful login attempt', 'alumno'),
(701, 'Successful login attempt', 'admin'),
(702, 'Successful login attempt', 'alumno'),
(703, 'Successful login attempt', 'admin'),
(704, 'Successful login attempt', 'alumno'),
(705, 'Successful login attempt', 'admin'),
(706, 'Successful login attempt', 'admin'),
(707, 'Successful login attempt', 'alumno'),
(708, 'Successful login attempt', 'alumno'),
(709, 'Successful login attempt', 'admin'),
(710, 'Successful login attempt', 'alumno'),
(711, 'Successful login attempt', 'admin'),
(712, 'Successful login attempt', 'alumno'),
(713, 'Successful login attempt', 'prueba'),
(714, 'Successful login attempt', 'prueba'),
(715, 'Successful login attempt', 'admin'),
(716, 'Successful login attempt', 'prueba'),
(717, 'Successful login attempt', 'admin'),
(718, 'Successful login attempt', 'admin'),
(719, 'Test de login', 'admin'),
(721, 'Test de login', 'admin'),
(723, 'Successful login attempt', 'admin'),
(724, 'Successful login attempt', 'admin'),
(725, 'Successful login attempt', 'admin'),
(726, 'Successful login attempt', 'admin'),
(727, 'Successful login attempt', 'admin'),
(728, 'Successful login attempt', 'admin'),
(729, 'Successful login attempt', 'admin'),
(730, 'Successful login attempt', 'admin'),
(731, 'Successful login attempt', 'admin'),
(732, 'Successful login attempt', 'admin'),
(733, 'Successful login attempt', 'admin'),
(734, 'Successful login attempt', 'admin'),
(735, 'Successful login attempt', 'admin'),
(736, 'Successful login attempt', 'admin'),
(737, 'Successful login attempt', 'admin'),
(738, 'Successful login attempt', 'alumno'),
(739, 'Successful login attempt', 'alumno'),
(740, 'Successful login attempt', 'admin'),
(741, 'Successful login attempt', 'admin'),
(742, 'Successful login attempt', 'admin'),
(743, 'Successful login attempt', 'admin'),
(744, 'Successful login attempt', 'admin'),
(745, 'Successful login attempt', 'admin'),
(746, 'Successful login attempt', 'alumno'),
(747, 'Successful login attempt', 'alumno'),
(748, 'Successful login attempt', 'alumno'),
(749, 'Successful login attempt', 'admin');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `roles`
--

CREATE TABLE `roles` (
  `id_roles` int(11) NOT NULL,
  `permiso` varchar(100) NOT NULL,
  `nombre` varchar(50) NOT NULL,
  `can_download` tinyint(1) DEFAULT 1 COMMENT 'Permite descargar archivos de otros usuarios',
  `can_modify` tinyint(1) DEFAULT 0 COMMENT 'Permite modificar/renombrar archivos de otros usuarios',
  `can_delete` tinyint(1) DEFAULT 0 COMMENT 'Permite eliminar archivos de otros usuarios'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `roles`
--

INSERT INTO `roles` (`id_roles`, `permiso`, `nombre`, `can_download`, `can_modify`, `can_delete`) VALUES
(1, 'admin', 'admin', 1, 1, 1),
(2, 'usuario', 'user', 1, 0, 0);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `traducciones`
--

CREATE TABLE `traducciones` (
  `id` int(11) NOT NULL,
  `espanol` text NOT NULL,
  `ingles` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `traducciones`
--

INSERT INTO `traducciones` (`id`, `espanol`, `ingles`) VALUES
(52, 'Gestor de Correos', 'Email Manager'),
(53, 'Nuevo Mensaje', 'New Message'),
(54, 'Bandeja de Entrada', 'Inbox'),
(55, 'Para:', 'To:'),
(56, 'Asunto:', 'Subject:'),
(57, 'Adjuntar Archivo', 'Attach File'),
(58, 'Limpiar', 'Clear'),
(59, 'Enviar Correo', 'Send Email'),
(60, 'No hay archivos adjuntos', 'No files attached'),
(61, 'Estado', 'Status'),
(62, 'Remitente', 'Sender'),
(63, 'Asunto', 'Subject'),
(64, 'Actualizar', 'Refresh'),
(65, 'Marcar Leído/No Leído', 'Mark Read/Unread'),
(66, 'Descargar .eml', 'Download .eml'),
(67, 'Eliminar Correo', 'Delete Email'),
(68, 'Selecciona un correo para leer...', 'Select an email to read...'),
(69, 'Regresar', 'Return'),
(70, 'Vista de Base de Datos', 'Database View'),
(71, 'Tablas', 'Tables'),
(72, 'Registros', 'Records'),
(73, 'Editar / Crear Registro', 'Edit / Create Record'),
(74, 'Cancelar', 'Cancel'),
(75, 'Guardar Nuevo', 'Save New'),
(76, 'Volver', 'Back'),
(77, 'Actualizar', 'Update'),
(78, 'Crear Nuevo Registro', 'Create New Record'),
(79, 'Editando Registro', 'Editing Record'),
(80, 'Editar', 'Edit'),
(81, 'Eliminar', 'Delete'),
(82, 'Acciones', 'Actions'),
(83, 'Gestor FTP', 'FTP Manager'),
(84, 'Regresar', 'Return'),
(85, 'Nueva Carpeta', 'New Folder'),
(86, 'Filtrar:', 'Filter:'),
(87, 'Nombre', 'Name'),
(88, 'Tamaño', 'Size'),
(89, 'Fecha', 'Date'),
(90, 'Acciones', 'Actions'),
(91, 'Menú Principal - Metro Málaga', 'Main Menu - Metro Málaga'),
(92, 'CRUD', 'CRUD'),
(93, 'FTP', 'FTP'),
(94, 'SMTP', 'SMTP'),
(95, 'Salir', 'Exit'),
(96, '¿Desea salir de la aplicación?', 'Do you want to exit the application?'),
(97, 'Sí, Salir', 'Yes, Exit'),
(98, 'No, Volver', 'No, Go Back'),
(99, 'Esto es un archivo, no una carpeta. Usa \'Descargar\' para obtenerlo.', 'This is a file, not a folder. Use \'Download\' to get it.'),
(100, 'Información', 'Information'),
(101, 'Error', 'Error'),
(102, 'Advertencia', 'Warning'),
(103, 'Confirmación', 'Confirmation'),
(104, 'Entrada', 'Input'),
(105, 'Guardar ', 'Save '),
(106, ' como...', ' as...'),
(107, 'Archivo descargado exitosamente en:\n', 'File successfully downloaded to:\n'),
(108, 'Error: No se puede descargar la carpeta', 'Error: The folder cannot be downloaded'),
(109, 'Error de Descarga', 'Download Error'),
(110, 'Error de E/S durante la descarga: ', 'I/O error during download: '),
(111, '¿Estás seguro de que quieres eliminar ', 'Are you sure you want to delete '),
(112, '?', '?'),
(113, 'Confirmar Eliminación', 'Confirm Deletion'),
(114, 'Archivo eliminado exitosamente.', 'File successfully deleted.'),
(115, 'No se puede eliminar la carpeta', 'Cannot delete the folder'),
(116, 'Error de Borrado', 'Deletion Error'),
(117, 'Error de E/S durante el borrado: ', 'I/O error during deletion: '),
(118, 'Nuevo nombre para ', 'New name for '),
(119, ':', ':'),
(120, 'Archivo renombrado exitosamente.', 'File renamed successfully.'),
(121, 'Fallo al renombrar el archivo.', 'Failed to rename the file.'),
(122, 'Error de Renombrado', 'Rename Error'),
(123, 'Error de E/S durante el renombrado: ', 'I/O error during rename: '),
(124, 'Seleccionar archivo para subir', 'Select file to upload'),
(125, 'Archivo subido exitosamente: ', 'File uploaded successfully: '),
(126, 'Fallo al subir el archivo.', 'File upload failed.'),
(127, 'Error de Subida', 'Upload Error'),
(128, 'Error de E/S durante la subida: ', 'I/O error during upload: '),
(129, 'Ya estás en el directorio raíz o el cambio falló.', 'You are already in the root directory or the change failed.'),
(130, 'Error de Navegación', 'Navigation Error'),
(131, 'Error al intentar retroceder: ', 'Error trying to go back: '),
(132, 'Nombre de la nueva carpeta:', 'Name of the new folder:'),
(133, 'Crear Carpeta', 'Create Folder'),
(134, 'La carpeta ya existe.', 'The folder already exists.'),
(135, 'No se pudo crear la carpeta.', 'The folder could not be created.'),
(136, 'El nombre de usuario y la contraseña no pueden estar vacíos.', 'The username and password cannot be empty.'),
(137, 'Error de Validación', 'Validation Error'),
(138, 'El usuario solo puede contener letras (a-z, A-Z) y números (0-9).', 'The user can only contain letters (a-z, A-Z) and numbers (0-9).'),
(139, 'Error de Formato', 'Format Error'),
(140, 'La contraseña debe tener al menos 8 caracteres y contener solo letras y números.', 'The password must be at least 8 characters long and contain only letters and numbers.'),
(141, 'Bienvenido, ', 'Welcome, '),
(142, '. Acceso concedido.', '. Access granted.'),
(143, 'Inicio de sesión exitoso', 'Login successful'),
(144, 'Error al cargar los datos del usuario. Por favor, inténtalo de nuevo.', 'Error loading user data. Please try again.'),
(145, 'Error Interno', 'Internal Error'),
(146, 'Nombre de usuario o contraseña incorrectos. Por favor, inténtalo de nuevo.', 'Incorrect username or password. Please try again.'),
(147, 'Error de Autenticación', 'Authentication Error'),
(148, 'SIN ARCHIVOS', 'NO FILES'),
(149, 'Destinatario necesario.', 'Recipient needed.'),
(150, 'Error: El email ', 'Error: The email '),
(151, ' no está en la lista blanca o no es un usuario.', ' is not in the whitelist or is not a user.'),
(152, 'Email bloqueado', 'Email blocked'),
(153, 'LEÍDO', 'READ'),
(154, 'NO LEÍDO', 'UNREAD'),
(155, '¿Eliminar?', 'Delete?'),
(156, 'CONFIRMAR', 'CONFIRM'),
(157, 'DE: ', 'FROM: '),
(158, 'ASUNTO: ', 'SUBJECT: '),
(159, '\n\n=== ARCHIVOS ADJUNTOS ===\n', '\n\n=== ATTACHMENTS ===\n'),
(160, '[Haz clic para cargar contenido...]', '[Click to load content...]'),
(161, 'Descargando contenido del mensaje...\nPor favor, espera.', 'Downloading message content...\nPlease wait.'),
(162, 'Eliminado.', 'Deleted.'),
(163, 'Guardado: ', 'Saved: '),
(164, 'Error: ', 'Error: '),
(165, 'ENVIANDO...', 'SENDING...'),
(166, 'Email enviado.', 'Email sent.'),
(167, 'Comprobando nuevos emails...', 'Checking new emails...'),
(168, 'Bandeja actualizada. ', 'Inbox updated. '),
(169, ' mensajes.', ' messages.'),
(170, 'Error al actualizar la bandeja.', 'Error updating inbox.'),
(171, 'Error conectando a BD: ', 'Error connecting to DB: '),
(172, 'Error: No se pudo obtener el nombre del catálogo (Base de Datos).', 'Error: Could not get catalog name (Database).'),
(173, 'Error de Configuración', 'Configuration Error'),
(174, 'Error al cargar la lista de tablas: ', 'Error loading table list: '),
(175, 'Error de Base de Datos', 'Database Error'),
(176, 'Error cargando tabla: ', 'Error loading table: '),
(177, 'No tienes permisos para modificar esta tabla.', 'You do not have permission to modify this table.'),
(178, 'Acceso Denegado', 'Access Denied'),
(179, 'Operación realizada con éxito', 'Operation completed successfully'),
(180, 'Error SQL: ', 'SQL Error: '),
(181, 'No tienes permisos para eliminar de esta tabla.', 'You do not have permission to delete from this table.'),
(182, '¿Eliminar registro con ID ', 'Delete record with ID '),
(183, '?', '?'),
(184, 'Error de Conexión/SQL. Por favor, contacta con soporte. Detalle: ', 'Connection/SQL Error. Please contact support. Detail: '),
(185, 'Error del Sistema', 'System Error'),
(186, 'Error al recuperar datos del usuario: ', 'Error retrieving user data: '),
(187, 'Error SQL', 'SQL Error'),
(188, 'Error al verificar lista blanca: ', 'Error verifying whitelist: '),
(189, 'Login Fallido para el usuario: ', 'Login Failed for user: '),
(190, '. Verifica la contraseña en ConnecionFTP.java y los privilegios del usuario en FileZilla Server.', '. Check password in ConnecionFTP.java and user privileges on FileZilla Server.'),
(191, 'No se pudo conectar al Servidor FTP en ', 'Could not connect to the FTP Server at '),
(192, '. El servidor puede estar fuera de línea o el firewall está bloqueando el acceso. ', '. The server may be offline or the firewall is blocking access. '),
(193, 'Error FTP', 'FTP Error'),
(194, 'Error al desconectar FTP: ', 'Error while disconnecting FTP: '),
(195, 'ERROR CRÍTICO DE BASE DE DATOS: No se pudo establecer conexión. Detalle: ', 'CRITICAL DATABASE ERROR: Could not establish connection. Detail: '),
(196, 'Error de Conexión SQL', 'SQL Connection Error'),
(197, 'Carpeta:', 'Folder:'),
(198, 'Iniciar sesión - Centimetro Málaga', 'Login - Centimetro Malaga');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `trenes`
--

CREATE TABLE `trenes` (
  `id_tren` int(11) NOT NULL,
  `nombre` varchar(100) NOT NULL,
  `tipo` varchar(50) DEFAULT NULL,
  `fk_id_linea` int(11) DEFAULT NULL,
  `fk_id_cochera` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuarios`
--

CREATE TABLE `usuarios` (
  `username` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  `correo_electronico` varchar(100) NOT NULL,
  `fk_id_rol` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `usuarios`
--

INSERT INTO `usuarios` (`username`, `password`, `correo_electronico`, `fk_id_rol`) VALUES
('admin', '$2a$10$fZ9EbDLt0psw8TE3vbIDEenIb/VPBnaHmQMnbRrMivLxfcqQXKkzG', 'p97834480@gmail.com', 1),
('alumno', '$2a$10$/4hQQe75oEW07SBF5.C1T.LGCa3XH/nUSkE.IRTp8w0hWRSja5wKm', 'p97834480@gmail.com', 1),
('dani', '$2a$10$3jOmiDKJ9Qr/euBnzSl0.OZw531dvn1dGpMRK8DjiKqVepYDx9JBm', 'dani@gmail.com', 2),
('profesor', '$2a$10$801ARxRNs/N16r3GzQ1iCeJZ7Kusx0.2eVepHPYyM3YcBDYUiz2Qa', 'proyectoprofesor8@gmail.com', 1),
('prueba', '$2a$10$JkW23cw46kjDlgpow3bUHefXdkiHxW.DlP/MptBysHolSqfWei.X2', 'prueba@gmail.com', 2);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `viajes`
--

CREATE TABLE `viajes` (
  `id_viaje` int(11) NOT NULL,
  `nombre` varchar(100) NOT NULL,
  `fk_id_tren` int(11) DEFAULT NULL,
  `fk_id_estacion` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `accesos_estaciones`
--
ALTER TABLE `accesos_estaciones`
  ADD PRIMARY KEY (`id_acceso`),
  ADD KEY `fk_acceso_estacion` (`fk_id_estacion`);

--
-- Indices de la tabla `cocheras`
--
ALTER TABLE `cocheras`
  ADD PRIMARY KEY (`id_cochera`);

--
-- Indices de la tabla `estaciones`
--
ALTER TABLE `estaciones`
  ADD PRIMARY KEY (`id_estacion`);

--
-- Indices de la tabla `ftp_file_ownership`
--
ALTER TABLE `ftp_file_ownership`
  ADD PRIMARY KEY (`filename`),
  ADD KEY `idx_uploaded_by` (`uploaded_by`),
  ADD KEY `idx_upload_date` (`upload_date`);

--
-- Indices de la tabla `lineas`
--
ALTER TABLE `lineas`
  ADD PRIMARY KEY (`id_linea`);

--
-- Indices de la tabla `lineas_estaciones`
--
ALTER TABLE `lineas_estaciones`
  ADD PRIMARY KEY (`fk_id_linea`,`fk_id_estacion`),
  ADD KEY `fk_le_estacion` (`fk_id_estacion`);

--
-- Indices de la tabla `lista_blanca`
--
ALTER TABLE `lista_blanca`
  ADD PRIMARY KEY (`correo_electronico`);

--
-- Indices de la tabla `logs`
--
ALTER TABLE `logs`
  ADD PRIMARY KEY (`id_log`),
  ADD KEY `fk_log_usuario` (`username`);

--
-- Indices de la tabla `roles`
--
ALTER TABLE `roles`
  ADD PRIMARY KEY (`id_roles`);

--
-- Indices de la tabla `traducciones`
--
ALTER TABLE `traducciones`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `trenes`
--
ALTER TABLE `trenes`
  ADD PRIMARY KEY (`id_tren`),
  ADD KEY `fk_tren_linea` (`fk_id_linea`),
  ADD KEY `fk_tren_cochera` (`fk_id_cochera`);

--
-- Indices de la tabla `usuarios`
--
ALTER TABLE `usuarios`
  ADD PRIMARY KEY (`username`),
  ADD KEY `fk_usuario_rol` (`fk_id_rol`);

--
-- Indices de la tabla `viajes`
--
ALTER TABLE `viajes`
  ADD PRIMARY KEY (`id_viaje`),
  ADD KEY `fk_viaje_tren` (`fk_id_tren`),
  ADD KEY `fk_viaje_estacion` (`fk_id_estacion`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `accesos_estaciones`
--
ALTER TABLE `accesos_estaciones`
  MODIFY `id_acceso` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `cocheras`
--
ALTER TABLE `cocheras`
  MODIFY `id_cochera` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT de la tabla `estaciones`
--
ALTER TABLE `estaciones`
  MODIFY `id_estacion` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `lineas`
--
ALTER TABLE `lineas`
  MODIFY `id_linea` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT de la tabla `logs`
--
ALTER TABLE `logs`
  MODIFY `id_log` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=750;

--
-- AUTO_INCREMENT de la tabla `roles`
--
ALTER TABLE `roles`
  MODIFY `id_roles` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT de la tabla `traducciones`
--
ALTER TABLE `traducciones`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=199;

--
-- AUTO_INCREMENT de la tabla `trenes`
--
ALTER TABLE `trenes`
  MODIFY `id_tren` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `viajes`
--
ALTER TABLE `viajes`
  MODIFY `id_viaje` int(11) NOT NULL AUTO_INCREMENT;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `accesos_estaciones`
--
ALTER TABLE `accesos_estaciones`
  ADD CONSTRAINT `fk_acceso_estacion` FOREIGN KEY (`fk_id_estacion`) REFERENCES `estaciones` (`id_estacion`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Filtros para la tabla `lineas_estaciones`
--
ALTER TABLE `lineas_estaciones`
  ADD CONSTRAINT `fk_le_estacion` FOREIGN KEY (`fk_id_estacion`) REFERENCES `estaciones` (`id_estacion`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_le_linea` FOREIGN KEY (`fk_id_linea`) REFERENCES `lineas` (`id_linea`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Filtros para la tabla `logs`
--
ALTER TABLE `logs`
  ADD CONSTRAINT `fk_log_usuario` FOREIGN KEY (`username`) REFERENCES `usuarios` (`username`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Filtros para la tabla `trenes`
--
ALTER TABLE `trenes`
  ADD CONSTRAINT `fk_tren_cochera` FOREIGN KEY (`fk_id_cochera`) REFERENCES `cocheras` (`id_cochera`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_tren_linea` FOREIGN KEY (`fk_id_linea`) REFERENCES `lineas` (`id_linea`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Filtros para la tabla `usuarios`
--
ALTER TABLE `usuarios`
  ADD CONSTRAINT `fk_usuario_rol` FOREIGN KEY (`fk_id_rol`) REFERENCES `roles` (`id_roles`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Filtros para la tabla `viajes`
--
ALTER TABLE `viajes`
  ADD CONSTRAINT `fk_viaje_estacion` FOREIGN KEY (`fk_id_estacion`) REFERENCES `estaciones` (`id_estacion`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_viaje_tren` FOREIGN KEY (`fk_id_tren`) REFERENCES `trenes` (`id_tren`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
