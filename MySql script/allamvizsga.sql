-- phpMyAdmin SQL Dump
-- version 3.2.0.1
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Jan 25, 2011 at 10:28 PM
-- Server version: 5.1.37
-- PHP Version: 5.3.0

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `allamvizsga`
--

-- --------------------------------------------------------

--
-- Table structure for table `base`
--

DROP TABLE IF EXISTS `base`;
CREATE TABLE IF NOT EXISTS `base` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `Name` varchar(30) NOT NULL,
  `Latitude` double NOT NULL,
  `Longitude` double NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `Name` (`Name`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=11 ;

--
-- Dumping data for table `base`
--

INSERT INTO `base` (`ID`, `Name`, `Latitude`, `Longitude`) VALUES
(10, 'Madaras', 47.681, 22.843),
(9, 'Batiz', 47.8346, 22.9388);

-- --------------------------------------------------------

--
-- Table structure for table `basemachines`
--

DROP TABLE IF EXISTS `basemachines`;
CREATE TABLE IF NOT EXISTS `basemachines` (
  `BaseID` int(11) NOT NULL,
  `MachineID` varchar(30) NOT NULL,
  PRIMARY KEY (`BaseID`,`MachineID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `basemachines`
--

INSERT INTO `basemachines` (`BaseID`, `MachineID`) VALUES
(9, 'A100'),
(9, 'A101'),
(9, 'A102'),
(9, 'A103'),
(9, 'B101'),
(9, 'B102'),
(9, 'B103'),
(10, 'A002'),
(10, 'A1');

-- --------------------------------------------------------

--
-- Table structure for table `baseparcel`
--

DROP TABLE IF EXISTS `baseparcel`;
CREATE TABLE IF NOT EXISTS `baseparcel` (
  `BaseID` int(11) NOT NULL,
  `ParcelID` int(11) NOT NULL,
  PRIMARY KEY (`BaseID`,`ParcelID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `baseparcel`
--

INSERT INTO `baseparcel` (`BaseID`, `ParcelID`) VALUES
(9, 1),
(9, 2),
(9, 3),
(9, 4),
(9, 6),
(9, 7),
(9, 14),
(10, 8),
(10, 9),
(10, 10),
(10, 11),
(10, 12);

-- --------------------------------------------------------

--
-- Table structure for table `basepath`
--

DROP TABLE IF EXISTS `basepath`;
CREATE TABLE IF NOT EXISTS `basepath` (
  `BaseID` int(11) NOT NULL,
  `PathID` int(11) NOT NULL,
  PRIMARY KEY (`BaseID`,`PathID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `basepath`
--

INSERT INTO `basepath` (`BaseID`, `PathID`) VALUES
(9, 1),
(9, 2),
(9, 3),
(9, 4),
(9, 5),
(9, 6),
(10, 8),
(10, 9),
(10, 10),
(10, 11),
(10, 12);

-- --------------------------------------------------------

--
-- Table structure for table `machines`
--

DROP TABLE IF EXISTS `machines`;
CREATE TABLE IF NOT EXISTS `machines` (
  `ID` varchar(30) NOT NULL,
  `Type` varchar(30) NOT NULL,
  `speed` int(11) NOT NULL,
  `workspeed` double NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `machines`
--

INSERT INTO `machines` (`ID`, `Type`, `speed`, `workspeed`) VALUES
('A101', 'Tractor', 50, 1.5),
('A100', 'Tractor', 60, 2),
('A102', 'Tractor', 55, 1.8),
('A103', 'Tractor', 32, 2.1),
('B103', 'Harvester', 15, 3),
('B102', 'Harvester', 15, 2.5),
('B101', 'Harvester', 60, 2.3),
('A1', 'Tractor', 15, 1.1),
('A002', 'Tractor', 20, 1.2);

-- --------------------------------------------------------

--
-- Table structure for table `machinetypeoperation`
--

DROP TABLE IF EXISTS `machinetypeoperation`;
CREATE TABLE IF NOT EXISTS `machinetypeoperation` (
  `OperationID` int(11) NOT NULL,
  `MachineType` varchar(30) NOT NULL,
  PRIMARY KEY (`OperationID`,`MachineType`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `machinetypeoperation`
--

INSERT INTO `machinetypeoperation` (`OperationID`, `MachineType`) VALUES
(3, 'Harvester'),
(5, 'Tractor');

-- --------------------------------------------------------

--
-- Table structure for table `operations`
--

DROP TABLE IF EXISTS `operations`;
CREATE TABLE IF NOT EXISTS `operations` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `Name` varchar(30) NOT NULL,
  `Price` int(11) NOT NULL,
  `Time` double NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `Name` (`Name`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=14 ;

--
-- Dumping data for table `operations`
--

INSERT INTO `operations` (`ID`, `Name`, `Price`, `Time`) VALUES
(3, 'Harvest', 150, 1),
(5, 'Plowing', 100, 1);

-- --------------------------------------------------------

--
-- Table structure for table `parcel`
--

DROP TABLE IF EXISTS `parcel`;
CREATE TABLE IF NOT EXISTS `parcel` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `Name` varchar(30) NOT NULL,
  `Area` double NOT NULL,
  `PlantID` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `Name` (`Name`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=14 ;

--
-- Dumping data for table `parcel`
--

INSERT INTO `parcel` (`ID`, `Name`, `Area`, `PlantID`) VALUES
(6, 'BotizB1', 1.24694643990491, 17),
(4, 'BotizA4', 5.32686480772218, 17),
(2, 'BotizA2', 3.08050799150989, 17),
(3, 'BotizA3', 3.60663673244302, 17),
(1, 'BotizA1', 1.78292585301944, 17),
(7, 'BotizB2', 1.35091992163519, 17),
(8, 'M1', 1.87268077755673, 17),
(9, 'M2', 2.36555953811548, 17),
(10, 'M3', 1.63193734893572, 17),
(11, 'M4', 0.774391953860468, 17),
(12, 'M5', 1.92480385300365, 17);

-- --------------------------------------------------------

--
-- Table structure for table `path`
--

DROP TABLE IF EXISTS `path`;
CREATE TABLE IF NOT EXISTS `path` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `parcelFrom` int(11) NOT NULL,
  `parcelTO` int(11) NOT NULL,
  `Length` double NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=14 ;

--
-- Dumping data for table `path`
--

INSERT INTO `path` (`ID`, `parcelFrom`, `parcelTO`, `Length`) VALUES
(4, 7, 2, 44.42145443329),
(5, 3, 7, 125.080851044083),
(3, 2, 6, 73.2261025881755),
(6, 4, 3, 160.254505633481),
(1, 1, 0, 784.259342099918),
(2, 6, 1, 79.3651606802569),
(8, 8, 0, 273.694245605536),
(9, 10, 8, 75.4553562691228),
(10, 11, 10, 92.9614066425722),
(11, 12, 11, 132.768224461091),
(12, 9, 8, 887.248124232061);

-- --------------------------------------------------------

--
-- Table structure for table `pathpoints`
--

DROP TABLE IF EXISTS `pathpoints`;
CREATE TABLE IF NOT EXISTS `pathpoints` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `PathID` int(11) NOT NULL,
  `Latitude` double NOT NULL,
  `Longitude` double NOT NULL,
  `Elevation` double NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=55 ;

--
-- Dumping data for table `pathpoints`
--

INSERT INTO `pathpoints` (`ID`, `PathID`, `Latitude`, `Longitude`, `Elevation`) VALUES
(47, 12, 47.674173, 22.847474, 116.619353573156),
(46, 12, 47.674249, 22.846762, 117.662518212978),
(45, 12, 47.674494, 22.843872, 114.398641838396),
(44, 12, 47.674786, 22.841256, 107.269419154994),
(43, 12, 47.676187, 22.84152, 94.2341658250079),
(42, 12, 47.678447, 22.842069, 117.59545320553),
(41, 12, 47.678447, 22.842069, 117.599413282743),
(40, 11, 47.679327, 22.838057, 109.132467322892),
(39, 11, 47.679098, 22.839791, 112.25491988146),
(38, 11, 47.679098, 22.839791, 112.254929748668),
(37, 10, 47.679103, 22.83987, 112.575922485535),
(36, 10, 47.678923, 22.841078, 116.577692124345),
(35, 10, 47.678923, 22.841078, 116.492436841098),
(34, 9, 47.67892, 22.841169, 116.861932611369),
(33, 9, 47.678749, 22.842141, 118.179159188198),
(32, 9, 47.678749, 22.842141, 118.05409423826),
(31, 8, 47.678585, 22.842115, 117.879206480004),
(13, 4, 47.842241, 22.939627, 111.753257550719),
(9, 2, 47.842162, 22.940603, 117.338173940201),
(8, 2, 47.84145, 22.940663, 114.122150156194),
(16, 5, 47.842467, 22.939068, 112.951878387126),
(15, 4, 47.842438, 22.939111, 112.496115163733),
(14, 4, 47.842241, 22.939627, 111.568187107389),
(12, 3, 47.842211, 22.939698, 111.550633833401),
(11, 3, 47.842063, 22.940278, 113.371375008299),
(10, 3, 47.842089, 22.94063, 116.953010943118),
(7, 2, 47.84145, 22.940663, 113.960245332339),
(19, 6, 47.843022, 22.937501, 115.99396638759),
(18, 5, 47.842987, 22.937587, 115.807081001542),
(17, 5, 47.842467, 22.939068, 112.809449504462),
(21, 6, 47.843654, 22.93558, 109.331456471291),
(20, 6, 47.843022, 22.937501, 115.918276267471),
(30, 8, 47.68103, 22.842538, 118.232420061327),
(29, 8, 47.68103, 22.842538, 118.22029329131),
(6, 1, 47.841439, 22.940672, 114.193398273211),
(5, 1, 47.840611, 22.940746, 109.31857483335),
(4, 1, 47.838469, 22.940841, 115.993969869209),
(3, 1, 47.836988, 22.940394, 119.525083222091),
(2, 1, 47.834581, 22.939103, 123.244682596698),
(1, 1, 47.834581, 22.939103, 123.707566736423);

-- --------------------------------------------------------

--
-- Table structure for table `plantoperation`
--

DROP TABLE IF EXISTS `plantoperation`;
CREATE TABLE IF NOT EXISTS `plantoperation` (
  `PlantID` int(11) NOT NULL,
  `OperationID` int(11) NOT NULL,
  PRIMARY KEY (`PlantID`,`OperationID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `plantoperation`
--

INSERT INTO `plantoperation` (`PlantID`, `OperationID`) VALUES
(17, 3),
(17, 5);

-- --------------------------------------------------------

--
-- Table structure for table `plants`
--

DROP TABLE IF EXISTS `plants`;
CREATE TABLE IF NOT EXISTS `plants` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `Type` varchar(30) NOT NULL,
  `Seed` int(11) NOT NULL,
  `Income` int(11) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `Type` (`Type`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=19 ;

--
-- Dumping data for table `plants`
--

INSERT INTO `plants` (`ID`, `Type`, `Seed`, `Income`) VALUES
(0, 'uncultivated', 0, 0),
(17, 'PioneerGrain', 100, 1500);

-- --------------------------------------------------------

--
-- Table structure for table `points`
--

DROP TABLE IF EXISTS `points`;
CREATE TABLE IF NOT EXISTS `points` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `ParcelID` int(11) NOT NULL,
  `Latitude` double NOT NULL,
  `Longitude` double NOT NULL,
  `Elevation` double NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=88 ;

--
-- Dumping data for table `points`
--

INSERT INTO `points` (`ID`, `ParcelID`, `Latitude`, `Longitude`, `Elevation`) VALUES
(41, 6, 47.842146, 22.940724, 118.442375285119),
(40, 6, 47.844304, 22.943002, 107.987700933966),
(39, 6, 47.844558, 22.942502, 108.935450553265),
(29, 4, 47.843873, 22.934767, 112.216187297412),
(28, 4, 47.843178, 22.936754, 113.967338193997),
(27, 4, 47.840537, 22.93519, 113.549934789801),
(26, 4, 47.841313, 22.933137, 113.907799030701),
(25, 4, 47.843873, 22.934767, 112.16714709978),
(24, 4, 47.843873, 22.934767, 112.216187297412),
(23, 3, 47.840083, 22.936754, 117.204211583689),
(22, 3, 47.84262, 22.938379, 112.029830322246),
(21, 3, 47.843107, 22.936991, 114.641357124748),
(20, 3, 47.840496, 22.935348, 114.175707017254),
(19, 3, 47.840083, 22.936754, 117.221411939859),
(18, 3, 47.840083, 22.936754, 117.204211583689),
(17, 2, 47.839736, 22.937701, 116.394644741939),
(16, 2, 47.839384, 22.938825, 109.508363206965),
(15, 2, 47.841364, 22.940371, 110.79993283863),
(14, 2, 47.841966, 22.940297, 112.517921685808),
(13, 2, 47.842255, 22.939399, 111.50720647251),
(12, 2, 47.839736, 22.937701, 116.366979031729),
(11, 2, 47.839736, 22.937701, 116.394644741939),
(10, 1, 47.841682, 22.940663, 114.91323150936),
(9, 1, 47.841147, 22.940715, 113.069110774189),
(8, 1, 47.84119, 22.940911, 116.844026616425),
(7, 1, 47.841272, 22.941129, 120.61313219043),
(6, 1, 47.841725, 22.941748, 126.227685283315),
(5, 1, 47.841825, 22.941572, 124.906938250776),
(4, 1, 47.843989, 22.943786, 108.006635441772),
(3, 1, 47.84416, 22.943218, 107.949311850821),
(2, 1, 47.841682, 22.940663, 115.031625933664),
(1, 1, 47.841682, 22.940663, 114.91323150936),
(55, 8, 47.678827, 22.842324, 118.792105527981),
(54, 8, 47.6783, 22.846144, 116.063113361271),
(53, 8, 47.67806, 22.846073, 116.806587029569),
(47, 7, 47.839902, 22.937197, 116.860245057498),
(46, 7, 47.842454, 22.93886, 111.718921207261),
(45, 7, 47.842255, 22.939399, 111.500167427064),
(44, 7, 47.839736, 22.937701, 116.365521899571),
(43, 7, 47.839902, 22.937197, 116.8451736904),
(42, 7, 47.839902, 22.937197, 116.860245057498),
(38, 6, 47.842811, 22.940655, 125.394044771149),
(37, 6, 47.842146, 22.940724, 118.826467389214),
(36, 6, 47.842146, 22.940724, 118.442375285119),
(52, 8, 47.677958, 22.846507, 117.435017452971),
(51, 8, 47.677705, 22.846406, 119.683869979094),
(50, 8, 47.678287, 22.842196, 117.210396117165),
(49, 8, 47.678827, 22.842324, 118.714582211685),
(48, 8, 47.678827, 22.842324, 118.792105527981),
(56, 9, 47.676951, 22.847882, 108.825354191012),
(57, 9, 47.676951, 22.847882, 108.748746080331),
(58, 9, 47.674166, 22.847253, 116.909032692414),
(59, 9, 47.674064, 22.848171, 120.207052740458),
(60, 9, 47.677361, 22.848938, 112.098942190595),
(61, 9, 47.677181, 22.848588, 111.340648852299),
(62, 9, 47.677, 22.848284, 109.419863544179),
(63, 9, 47.676951, 22.847882, 108.825354191012),
(64, 10, 47.676951, 22.840105, 111.087540580167),
(65, 10, 47.676951, 22.840105, 110.839385778923),
(66, 10, 47.676829, 22.841082, 105.382750630637),
(67, 10, 47.678824, 22.841465, 117.392161196774),
(68, 10, 47.678978, 22.840547, 115.067190949624),
(69, 10, 47.676951, 22.840105, 111.087540580167),
(70, 11, 47.679026, 22.84014, 113.361076321001),
(71, 11, 47.679026, 22.84014, 113.423623274992),
(72, 11, 47.679096, 22.839676, 111.822440872441),
(73, 11, 47.677068, 22.839257, 115.363282335077),
(74, 11, 47.677028, 22.839697, 113.284701102104),
(75, 11, 47.679026, 22.84014, 113.361076321001),
(76, 12, 47.679387, 22.837499, 112.236507516002),
(77, 12, 47.679387, 22.837499, 112.23651746905),
(78, 12, 47.677334, 22.836966, 135.716586308112),
(79, 12, 47.677131, 22.838115, 128.401643114492),
(80, 12, 47.679254, 22.838494, 108.357699990205),
(81, 12, 47.679387, 22.837499, 112.236507516002);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
