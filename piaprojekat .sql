-- phpMyAdmin SQL Dump
-- version 4.0.4
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Sep 21, 2014 at 03:39 PM
-- Server version: 5.6.12-log
-- PHP Version: 5.4.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `piaprojekat`
--
CREATE DATABASE IF NOT EXISTS `piaprojekat` DEFAULT CHARACTER SET utf8 COLLATE utf8_croatian_ci;
USE `piaprojekat`;

-- --------------------------------------------------------

--
-- Table structure for table `cashiers`
--

CREATE TABLE IF NOT EXISTS `cashiers` (
  `userid` int(11) NOT NULL,
  `locationid` int(11) NOT NULL,
  UNIQUE KEY `userid` (`userid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_croatian_ci;

--
-- Dumping data for table `cashiers`
--

INSERT INTO `cashiers` (`userid`, `locationid`) VALUES
(2, 1);

-- --------------------------------------------------------

--
-- Table structure for table `categories`
--

CREATE TABLE IF NOT EXISTS `categories` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `eventid` int(11) NOT NULL,
  `name` varchar(20) COLLATE utf8_croatian_ci NOT NULL,
  `size` int(11) NOT NULL,
  `price` int(11) NOT NULL,
  `sold` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_croatian_ci AUTO_INCREMENT=6 ;

--
-- Dumping data for table `categories`
--

INSERT INTO `categories` (`id`, `eventid`, `name`, `size`, `price`, `sold`) VALUES
(1, 18, 'Kat1', 3000, 700, 0),
(2, 18, 'Kat2', 5000, 400, 0),
(3, 19, 'Kat1', 10000, 300, 0),
(4, 20, 'Kat1', 5000, 400, 0),
(5, 21, 'Kat1', 2000, 500, 0);

-- --------------------------------------------------------

--
-- Table structure for table `commentsgrades`
--

CREATE TABLE IF NOT EXISTS `commentsgrades` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userid` int(11) NOT NULL,
  `eventname` varchar(20) COLLATE utf8_croatian_ci NOT NULL,
  `comment` varchar(150) COLLATE utf8_croatian_ci NOT NULL,
  `grade` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_croatian_ci AUTO_INCREMENT=4 ;

--
-- Dumping data for table `commentsgrades`
--

INSERT INTO `commentsgrades` (`id`, `userid`, `eventname`, `comment`, `grade`) VALUES
(1, 6, 'Cirkus Fantasticus ', 'Bilo je lepo!', 8),
(2, 6, 'Cirkus Fantasticus ', 'O ho ho', 6),
(3, 6, 'Sasha lepotica ', 'Da da', 10);

-- --------------------------------------------------------

--
-- Table structure for table `events`
--

CREATE TABLE IF NOT EXISTS `events` (
  `id` int(12) NOT NULL AUTO_INCREMENT,
  `title` varchar(20) COLLATE utf8_croatian_ci NOT NULL,
  `locationid` int(12) NOT NULL,
  `date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `canceled` tinyint(1) NOT NULL,
  `soldtickets` int(11) NOT NULL,
  `resenable` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `maxres` int(11) NOT NULL,
  `description` varchar(200) COLLATE utf8_croatian_ci NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_croatian_ci AUTO_INCREMENT=22 ;

--
-- Dumping data for table `events`
--

INSERT INTO `events` (`id`, `title`, `locationid`, `date`, `canceled`, `soldtickets`, `resenable`, `maxres`, `description`) VALUES
(1, 'Cirkus Fantasticus', 1, '2014-09-20 00:06:34', 0, 4, '0000-00-00 00:00:00', 0, ''),
(2, 'Koncert Partibrejker', 2, '2014-09-20 00:06:34', 0, 4, '0000-00-00 00:00:00', 0, ''),
(3, 'ETFovci', 1, '2014-09-20 00:06:34', 0, 4, '0000-00-00 00:00:00', 0, ''),
(4, 'ETFovci', 1, '2014-09-25 09:06:00', 0, 4, '2014-09-22 09:06:00', 20, ''),
(5, 'Sasha lepotica', 1, '2014-09-20 00:06:34', 0, 4, '0000-00-00 00:00:00', 0, ''),
(6, 'Dogadjaj1', 1, '2014-09-20 00:29:52', 0, 45, '0000-00-00 00:00:00', 0, ''),
(9, 'Dogadjaj2', 1, '2014-10-08 09:00:00', 0, 0, '2014-10-05 09:00:00', 15, ''),
(12, 'Dogadjaj3', 1, '2014-09-24 11:00:00', 0, 0, '2014-09-20 23:00:00', 15, ''),
(13, 'Dogadjaj4', 1, '2014-09-29 13:20:00', 0, 0, '2014-09-26 01:20:00', 15, ''),
(14, 'Dogadjaj5', 1, '2014-09-25 22:00:00', 0, 0, '2014-09-22 22:00:00', 20, 'Mnogo je lep dogadjaj!'),
(15, 'Dogadjaj6', 1, '2014-09-20 17:12:45', 0, 8, '2014-09-24 00:00:00', 20, 'Sasa je lepa!'),
(16, 'Izlozba', 1, '2014-09-25 22:00:00', 0, 0, '2014-09-22 22:00:00', 15, 'Fina izlozbica.'),
(17, 'Dogadjaj10', 1, '2014-08-01 08:00:00', 0, 0, '2014-07-29 08:00:00', 10, 'Dogadjaj10'),
(18, 'Dogadjaj11', 1, '2014-09-21 14:33:07', 0, 5, '2014-08-02 07:30:00', 10, 'Dogadjaj11'),
(19, 'Dogadjaj12', 1, '2014-09-21 14:44:30', 0, 5, '2014-08-02 04:00:00', 10, 'Dogadjaj12'),
(20, 'Dogadjaj13', 1, '2014-09-21 15:29:43', 0, 5, '2014-08-01 22:00:00', 10, 'Dogadjaj13'),
(21, 'Dogadjaj14', 1, '2014-09-08 09:00:00', 0, 0, '2014-09-05 09:00:00', 10, 'Dogadjaj14');

-- --------------------------------------------------------

--
-- Table structure for table `files`
--

CREATE TABLE IF NOT EXISTS `files` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `eventid` int(11) NOT NULL,
  `filename` varchar(25) COLLATE utf8_croatian_ci NOT NULL,
  `filepath` varchar(100) COLLATE utf8_croatian_ci NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_croatian_ci AUTO_INCREMENT=8 ;

--
-- Dumping data for table `files`
--

INSERT INTO `files` (`id`, `eventid`, `filename`, `filepath`) VALUES
(1, 16, 'Chrysanthemum.jpg', '76756Chrysanthemum.jpg'),
(2, 16, 'Desert.jpg', '28499Desert.jpg'),
(3, 16, 'Hydrangeas.jpg', '37271Hydrangeas.jpg'),
(4, 16, 'Penguins.jpg', '63913Penguins.jpg'),
(5, 17, 'Lighthouse.jpg', '78710Lighthouse.jpg'),
(6, 18, 'Penguins.jpg', '86702Penguins.jpg'),
(7, 19, 'Penguins.jpg', '33008Penguins.jpg');

-- --------------------------------------------------------

--
-- Table structure for table `locations`
--

CREATE TABLE IF NOT EXISTS `locations` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(30) COLLATE utf8_croatian_ci NOT NULL,
  `address` varchar(40) COLLATE utf8_croatian_ci NOT NULL,
  `capacity` int(12) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_croatian_ci AUTO_INCREMENT=4 ;

--
-- Dumping data for table `locations`
--

INSERT INTO `locations` (`id`, `title`, `address`, `capacity`) VALUES
(1, 'Beogradska Arena', 'Bulevar Arsenija Carnojevica 58', 20000),
(2, 'Dom Omladine', 'Makedonska 22', 2000),
(3, 'Pionir', 'Carlija Caplina 39', 8000);

-- --------------------------------------------------------

--
-- Table structure for table `messages`
--

CREATE TABLE IF NOT EXISTS `messages` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user` varchar(20) COLLATE utf8_croatian_ci NOT NULL,
  `text` varchar(600) COLLATE utf8_croatian_ci NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_croatian_ci AUTO_INCREMENT=47 ;

--
-- Dumping data for table `messages`
--

INSERT INTO `messages` (`id`, `user`, `text`) VALUES
(2, 'qwe', 'Rezervisaliste 5 ulaznica za dogadjaj: Sasha lepotica koji se odrzava 2014-09-18 u Beogradska Arena. \nRezervacija Vam vazi do Sat Sep 20 15:32:05 CEST 2014. \nHvala na poverenju! \nBelgradeEvent Tim.'),
(5, 'maverik', 'Uspesno ste kupili 10 ulaznica za dogadjaj: Cirkus Fantasticus koji se odrzava 2014-09-10 u Beogradska Arena. \nHvala na poverenju! \nBelgradeEvent Tim.'),
(6, 'maverik', 'Rezervisaliste 7 ulaznica za dogadjaj: Sasha lepotica koji se odrzava 2014-09-18 u Beogradska Arena. \nRezervacija Vam vazi do Sat Sep 20 21:37:06 CEST 2014. \nHvala na poverenju! \nBelgradeEvent Tim.'),
(7, 'maverik', 'Rezervisaliste 7 ulaznica za dogadjaj: Sasha lepotica koji se odrzava 2014-09-18 u Beogradska Arena. \nRezervacija Vam vazi do Sat Sep 20 21:37:06 CEST 2014. \nHvala na poverenju! \nBelgradeEvent Tim.'),
(9, 'maverik', 'Uspesno ste kupili 7 ulaznica za dogadjaj: Sasha lepotica koji se odrzava 2014-09-18 u Beogradska Arena. \nHvala na poverenju! \nBelgradeEvent Tim.'),
(10, 'qwe', 'Uspesno ste kupili 5 ulaznica za dogadjaj: Sasha lepotica koji se odrzava 2014-09-18 u Beogradska Arena. \nHvala na poverenju! \nBelgradeEvent Tim.'),
(12, 'maverik', 'Uspesno ste kupili 10 ulaznica za dogadjaj: Cirkus Fantasticus koji se odrzava 2014-09-10 u Beogradska Arena. \nHvala na poverenju! \nBelgradeEvent Tim.'),
(13, 'maverik', 'Uspesno ste kupili 7 ulaznica za dogadjaj: Sasha lepotica koji se odrzava 2014-09-18 u Beogradska Arena. \nHvala na poverenju! \nBelgradeEvent Tim.'),
(14, 'maverik', 'Uspesno ste kupili 7 ulaznica za dogadjaj: Sasha lepotica koji se odrzava 2014-09-18 u Beogradska Arena. \nHvala na poverenju! \nBelgradeEvent Tim.'),
(17, 'maverik', 'Rezervisaliste 1 ulaznica za dogadjaj: Cirkus Fantasticus koji se odrzava 2014-09-10 u Beogradska Arena. \nRezervacija Vam vazi do Sat Sep 20 22:39:24 CEST 2014. \nHvala na poverenju! \nBelgradeEvent Tim.'),
(18, 'maverik', 'Uspesno ste kupili 1 ulaznica za dogadjaj: Cirkus Fantasticus koji se odrzava 2014-09-10 u Beogradska Arena. \nHvala na poverenju! \nBelgradeEvent Tim.'),
(19, 'maverik', 'Rezervisaliste 7 ulaznica za dogadjaj: Cirkus Fantasticus koji se odrzava 2014-09-10 u Beogradska Arena. \nRezervacija Vam vazi do Sat Sep 20 23:21:37 CEST 2014. \nHvala na poverenju! \nBelgradeEvent Tim.'),
(21, 'maverik', 'Rezervisaliste 1 ulaznica za dogadjaj: ETFovci koji se odrzava 2014-09-18 u Beogradska Arena. \nRezervacija Vam vazi do Sun Sep 21 13:37:36 CEST 2014. \nHvala na poverenju! \nBelgradeEvent Tim.'),
(23, 'maverik', 'Rezervisaliste 1 ulaznica za dogadjaj: Sasha lepotica koji se odrzava 2014-09-18 u Beogradska Arena. \nRezervacija Vam vazi do Sun Sep 21 16:41:20 CEST 2014. \nHvala na poverenju! \nBelgradeEvent Tim.'),
(25, 'maverik', 'Rezervisaliste 1 ulaznica za dogadjaj: Koncert Partibrejker koji se odrzava 2014-09-05 u Dom Omladine. \nRezervacija Vam vazi do Sun Sep 21 16:41:20 CEST 2014. \nHvala na poverenju! \nBelgradeEvent Tim.'),
(27, 'maverik', 'Rezervisaliste 1 ulaznica za dogadjaj: Sasha lepotica koji se odrzava 2014-09-18 u Beogradska Arena. \nRezervacija Vam vazi do Sun Sep 21 16:43:57 CEST 2014. \nHvala na poverenju! \nBelgradeEvent Tim.'),
(29, 'maverik', 'Rezervisaliste 1 ulaznica za dogadjaj: Koncert Partibrejker koji se odrzava 2014-09-05 u Dom Omladine. \nRezervacija Vam vazi do Sun Sep 21 16:43:57 CEST 2014. \nHvala na poverenju! \nBelgradeEvent Tim.'),
(31, 'maverik', 'Rezervisaliste 4 ulaznica za dogadjaj: Dogadjaj1 koji se odrzava 2014-09-23 06:32:00.0 u Beogradska Arena. \nRezervacija Vam vazi do Mon Sep 22 02:06:10 CEST 2014. \nHvala na poverenju! \nBelgradeEvent Tim.'),
(32, 'maverik', 'Rezervisaliste 1 ulaznica za dogadjaj: Dogadjaj1 koji se odrzava 2014-09-20 02:06:34.0 u Beogradska Arena. \nRezervacija Vam vazi do Mon Sep 22 02:25:02 CEST 2014. \nHvala na poverenju! \nBelgradeEvent Tim.'),
(33, 'maverik', 'Rezervisaliste 1 ulaznica za dogadjaj: Dogadjaj1 koji se odrzava 2014-09-20 02:06:34.0 u Beogradska Arena. \nRezervacija Vam vazi do Mon Sep 22 02:26:38 CEST 2014. \nHvala na poverenju! \nBelgradeEvent Tim.'),
(34, 'maverik', 'Rezervisaliste 1 ulaznica za dogadjaj: Dogadjaj1 koji se odrzava 2014-09-20 02:06:34.0 u Beogradska Arena. \nRezervacija Vam vazi do Mon Sep 22 02:28:03 CEST 2014. \nHvala na poverenju! \nBelgradeEvent Tim.'),
(35, 'maverik', 'Rezervisaliste 5 ulaznica za dogadjaj: Dogadjaj1 koji se odrzava 2014-09-20 02:06:34.0 u Beogradska Arena. \nRezervacija Vam vazi do Mon Sep 22 02:28:03 CEST 2014. \nHvala na poverenju! \nBelgradeEvent Tim.'),
(36, 'maverik', 'Rezervisaliste 7 ulaznica za dogadjaj: Dogadjaj3 koji se odrzava 2014-09-26 00:00:00.0 u Beogradska Arena. I to: Kat2 - 4 karte po ceni od 400 din. \nRezervacija Vam vazi do Mon Sep 22 06:54:39 CEST 2014. \nHvala na poverenju! \nBelgradeEvent Tim.'),
(37, 'maverik', 'Rezervisaliste 6 ulaznica za dogadjaj: Dogadjaj3 koji se odrzava 2014-09-20 06:55:20.0 u Beogradska Arena. I to: Kat1 - 4 karte po ceni od 700 din.Kat2 - 2 karte po ceni od 400 din. \nRezervacija Vam vazi do Mon Sep 22 07:20:37 CEST 2014. \nHvala na poverenju! \nBelgradeEvent Tim.'),
(38, 'maverik', 'Rezervisaliste 4 ulaznica za dogadjaj: Dogadjaj4 koji se odrzava 2014-09-29 15:20:00.0 u Beogradska Arena. I to: Kat1 - 3 karte po ceni od 2500 din.Kat2 - 1 karte po ceni od 1000 din. \nRezervacija Vam vazi do Mon Sep 22 16:55:04 CEST 2014. \nHvala na poverenju! \nBelgradeEvent Tim.'),
(39, 'maverik', 'Rezervisaliste 7 ulaznica za dogadjaj: Dogadjaj4 koji se odrzava 2014-09-29 15:20:00.0 u Beogradska Arena. I to: Kat1 - 2 karte po ceni od 2500 din.Kat2 - 5 karte po ceni od 1000 din. \nRezervacija Vam vazi do Mon Sep 22 16:59:20 CEST 2014. \nHvala na poverenju! \nBelgradeEvent Tim.'),
(40, 'maverik', 'Rezervisaliste 3 ulaznica za dogadjaj: Dogadjaj4 koji se odrzava 2014-09-29 15:20:00.0 u Beogradska Arena. I to: Kat1 - 1 karte po ceni od 2500 din.Kat2 - 2 karte po ceni od 1000 din. \nRezervacija Vam vazi do Mon Sep 22 17:13:39 CEST 2014. \nHvala na poverenju! \nBelgradeEvent Tim.'),
(41, 'maverik', 'Rezervisaliste 6 ulaznica za dogadjaj: Dogadjaj5 koji se odrzava 2014-09-26 00:00:00.0 u Beogradska Arena. I to: Kat1 - 2 karte po ceni od 4000 din.Kat2 - 0 karte po ceni od 3000 din.Kat3 - 1 karte po ceni od 2000 din.Kat4 - 3 karte po ceni od 1000 din. \nRezervacija Vam vazi do Mon Sep 22 18:52:53 CEST 2014. \nHvala na poverenju! \nBelgradeEvent Tim.'),
(42, 'maverik', 'Rezervisaliste 8 ulaznica za dogadjaj: Dogadjaj6 koji se odrzava 2014-09-27 14:00:00.0 u Beogradska Arena. I to: Kat1 - 2 karte po ceni od 1000 din.Kat2 - 5 karte po ceni od 2000 din.Kat3 - 1 karte po ceni od 1000 din. \nRezervacija Vam vazi do Mon Sep 22 19:12:20 CEST 2014. \nHvala na poverenju! \nBelgradeEvent Tim.'),
(43, 'maverik', 'Rezervisaliste 5 ulaznica za dogadjaj: Dogadjaj11 koji se odrzava 2014-08-05 09:30:00.0 u Beogradska Arena. I to: Kat1 - 5 karte po ceni od 700 din.Kat2 - 0 karte po ceni od 400 din. \nRezervacija Vam vazi do Tue Sep 23 16:32:45 CEST 2014. \nHvala na poverenju! \nBelgradeEvent Tim.'),
(44, 'maverik', 'Rezervisaliste 5 ulaznica za dogadjaj: Dogadjaj12 koji se odrzava 2014-08-05 06:00:00.0 u Beogradska Arena. I to: Kat1 - 5 karte po ceni od 300 din. \nRezervacija Vam vazi do Tue Sep 23 16:43:54 CEST 2014. \nHvala na poverenju! \nBelgradeEvent Tim.'),
(45, 'maverik', 'Rezervisaliste 5 ulaznica za dogadjaj: Dogadjaj13 koji se odrzava 2014-08-05 00:00:00.0 u Beogradska Arena. I to: Kat1 - 5 karte po ceni od 400 din. \nRezervacija Vam vazi do Tue Sep 23 17:28:26 CEST 2014. \nHvala na poverenju! \nBelgradeEvent Tim.'),
(46, 'maverik', 'Rezervisaliste 5 ulaznica za dogadjaj: Dogadjaj14 koji se odrzava 2014-09-08 11:00:00.0 u Beogradska Arena. I to: Kat1 - 5 karte po ceni od 500 din. \nRezervacija Vam vazi do Tue Sep 23 17:36:22 CEST 2014. \nHvala na poverenju! \nBelgradeEvent Tim.');

-- --------------------------------------------------------

--
-- Table structure for table `reservations`
--

CREATE TABLE IF NOT EXISTS `reservations` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user` varchar(20) COLLATE utf8_croatian_ci NOT NULL,
  `tickets` int(11) NOT NULL,
  `eventid` int(11) NOT NULL,
  `locationid` int(11) NOT NULL,
  `expirationdate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `realized` tinyint(1) NOT NULL,
  `adminapproval` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_croatian_ci AUTO_INCREMENT=55 ;

--
-- Dumping data for table `reservations`
--

INSERT INTO `reservations` (`id`, `user`, `tickets`, `eventid`, `locationid`, `expirationdate`, `realized`, `adminapproval`) VALUES
(1, 'maverik', 5, 4, 1, '2014-09-18 22:00:00', 1, 0),
(4, 'maverik', 10, 1, 1, '2014-09-18 22:00:00', 1, 0),
(5, 'maverik', 10, 1, 1, '2014-09-18 22:00:00', 1, 0),
(6, 'maverik', 10, 1, 1, '2014-09-18 22:00:00', 1, 0),
(8, 'maverik', 3, 1, 1, '2014-09-18 22:00:00', 1, 0),
(17, 'maverik', 3, 5, 1, '2014-09-19 22:00:00', 1, 0),
(18, 'qwe', 5, 5, 1, '2014-09-19 22:00:00', 1, 0),
(19, 'maverik', 7, 5, 1, '2014-09-19 22:00:00', 1, 0),
(20, 'maverik', 7, 5, 1, '2014-09-19 22:00:00', 1, 0),
(21, 'maverik', 7, 5, 1, '2014-09-19 22:00:00', 1, 0),
(24, 'maverik', 1, 1, 1, '2014-09-19 22:00:00', 1, 0),
(29, 'maverik', 1, 5, 1, '2014-09-18 22:00:00', 1, 1),
(30, 'maverik', 1, 4, 1, '2014-09-18 22:00:00', 1, 1),
(31, 'maverik', 1, 2, 2, '2014-09-18 22:00:00', 1, 1),
(32, 'maverik', 1, 1, 1, '2014-09-18 22:00:00', 1, 1),
(33, 'maverik', 1, 5, 1, '2014-09-18 22:00:00', 1, 1),
(34, 'maverik', 1, 4, 1, '2014-09-18 22:00:00', 1, 1),
(35, 'maverik', 1, 2, 2, '2014-09-18 22:00:00', 1, 1),
(36, 'maverik', 1, 1, 1, '2014-09-18 22:00:00', 1, 1),
(49, 'maverik', 6, 14, 1, '2014-09-20 16:56:16', 0, 0),
(50, 'maverik', 8, 15, 1, '2014-09-20 17:14:46', 0, 0),
(52, 'maverik', 5, 19, 1, '2014-09-21 14:46:30', 0, 0),
(53, 'maverik', 5, 20, 1, '2014-09-21 15:31:43', 0, 0),
(54, 'maverik', 5, 21, 1, '2014-09-21 15:38:39', 0, 0);

-- --------------------------------------------------------

--
-- Table structure for table `sellinfo`
--

CREATE TABLE IF NOT EXISTS `sellinfo` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `resid` int(11) NOT NULL,
  `eventid` int(11) NOT NULL,
  `catid` int(11) NOT NULL,
  `amount` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_croatian_ci AUTO_INCREMENT=7 ;

--
-- Dumping data for table `sellinfo`
--

INSERT INTO `sellinfo` (`id`, `resid`, `eventid`, `catid`, `amount`) VALUES
(3, 52, 19, 3, 5),
(4, 53, 20, 4, 5),
(5, 54, 21, 5, 5),
(6, 54, 21, 5, 5);

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE IF NOT EXISTS `users` (
  `id` int(32) NOT NULL AUTO_INCREMENT,
  `username` varchar(20) COLLATE utf8_croatian_ci NOT NULL,
  `password` varchar(20) COLLATE utf8_croatian_ci NOT NULL,
  `password2` varchar(20) COLLATE utf8_croatian_ci NOT NULL,
  `firstname` varchar(20) COLLATE utf8_croatian_ci NOT NULL,
  `lastname` varchar(20) COLLATE utf8_croatian_ci NOT NULL,
  `email` varchar(40) COLLATE utf8_croatian_ci NOT NULL,
  `telephone` varchar(20) COLLATE utf8_croatian_ci NOT NULL,
  `address` varchar(40) COLLATE utf8_croatian_ci NOT NULL,
  `city` varchar(15) COLLATE utf8_croatian_ci NOT NULL,
  `type` int(11) NOT NULL,
  `status` tinyint(1) NOT NULL,
  `blocked` tinyint(1) NOT NULL,
  `lastlogin` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `registered` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_croatian_ci AUTO_INCREMENT=10 ;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `username`, `password`, `password2`, `firstname`, `lastname`, `email`, `telephone`, `address`, `city`, `type`, `status`, `blocked`, `lastlogin`, `registered`) VALUES
(1, 'admin', 'admin', 'admin', 'Dragan', 'Veljovic', 'dragan.d.veljovic@gmail.com', '+381642910617', 'Gavrila Principa 38', 'Kragujevac', 2, 1, 0, '2014-09-21 11:48:58', '2014-08-31 22:00:00'),
(2, 'sasha', 'toshiba', 'toshiba', 'Aleksandra', 'Music', 'ma@yahoo.com', '+38165651652', 'Nova 23', 'Liliput', 1, 1, 0, '2014-09-21 15:35:51', '2014-08-31 22:00:00'),
(3, 'stu', 'sifra', 'sifra', 'Stevan', 'Milivojevic', 'stu@gmail.com', '+381647894561', 'Savska 35', 'Beograd', 0, 1, 0, '2014-09-02 22:00:00', '2014-09-02 22:00:00'),
(4, 'mali', 'pass123', 'pass123', 'Sinisa', 'Mali', 'mali@bg.rs', '+38111745852', 'Takovska 67', 'Beograd', 1, 1, 0, '2014-09-02 22:00:00', '2014-09-02 22:00:00'),
(5, 'bas', 'sifra', 'sifra', 'Bart', 'Bassileus', 'bas@gmail.com', '+9875465458', 'Oakwood Street 23', 'Boston', 0, 1, 0, '2014-09-03 22:00:00', '2014-09-03 22:00:00'),
(6, 'maverik', 'maverik5', 'maverik5', 'Tom', 'Kruz', 'maverik@gmail.com', '+381642581479', 'Zmaj Jovina 7', 'Beograd', 0, 1, 0, '2014-09-21 15:36:25', '2014-09-14 22:00:00'),
(9, 'qwe', 'qwe', 'qwe', 'qwe', 'qwe', 'qwe@qwe.com', '784556', 'qwe', 'qwe', 0, 1, 0, '2014-09-20 00:02:27', '2014-09-17 22:00:00');

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
