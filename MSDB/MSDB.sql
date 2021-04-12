CREATE DATABASE  IF NOT EXISTS `COMP4342_MSDB` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `COMP4342_MSDB`;
-- MySQL dump 10.13  Distrib 8.0.21, for macos10.15 (x86_64)
--
-- Host: localhost    Database: COMP4342_MSDB
-- ------------------------------------------------------
-- Server version	8.0.23

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `order`
--

DROP TABLE IF EXISTS `order`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `order` (
  `orderID` int NOT NULL AUTO_INCREMENT,
  `confirmationCode` varchar(45) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `email` varchar(45) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `address` varchar(200) COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`orderID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order`
--

LOCK TABLES `order` WRITE;
/*!40000 ALTER TABLE `order` DISABLE KEYS */;
/*!40000 ALTER TABLE `order` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product`
--

DROP TABLE IF EXISTS `product`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product` (
  `productID` int NOT NULL,
  `type` varchar(45) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `brand` varchar(45) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `productName` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `productDescription` varchar(300) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `price` int DEFAULT NULL,
  `quantity` int DEFAULT NULL,
  `promotion` tinyint DEFAULT NULL,
  PRIMARY KEY (`productID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product`
--

LOCK TABLES `product` WRITE;
/*!40000 ALTER TABLE `product` DISABLE KEYS */;
INSERT INTO `product` VALUES (1,'CPU','AMD','AMD Ryzen Threadripper PRO 3995WX','Clock Rate: 2700MHz, Socket: TR4, External 4200MHz, Cache 256MB',47000,100,1),(2,'CPU','Intel','Intel Core i9-10900K','Clock Rate: 3700MHz, Socket: LGA 1200, External 5300MHz, Cache 20MB',3420,100,1),(3,'Motherboard','ASUS','ASUS PRIME B560M-K','Chips: Intel B560, Socket: LGA 1200, Measuremen ts: Micro-ATX, Supported Memory: DDR4',830,200,1),(4,'Display Card','MSI','MSI Radeon RX 6700 XT 12G','Chip: RT6700 XT, Memory: 12GB GDDR6, Input/Output: DisplayPort x 3 (v1.4) and HDMI x1, Clock Frequency(RAM/GPU):Boost: Up to 2581MHZ',5490,30,0),(5,'Chassis','GIGABYTE AORUS','GIGABYTE AORUS C700 GLASS','Vertical/Hoizontal: H, Motherboard Compatibility: Mini ITX / Micro ATX / ATX / E-ATX, No. of 3.5 Inch Disk Slot: 4, No. of expansion slot: 8+2, Weight: 19.2kg',2750,200,0),(6,'Internal Optical Drives','ASUS','BW-16D1HT Pro','DVD-R/RW: 16x/12x, DVD-R(RL): 12x, DVD+R/RW: 16x/12x, DVD+R(DL): 12x, CD-R/RW: 48x/40x, BD-R/RE: 12x/8x',750,30,0),(7,'Internal HDD','Western Digital','Western Digital 4TB HDD Red Plus WD40EFZX','Capacity: 4TB, Size: 3.5 inch, RPM: 5400rpm, Buffer Memory: 128MB, Display: SATA',717,50,0),(8,'SSD','Samsung','Samsung SSD 870 EVO SATA III 2.5\" 2TB (MZ-77E2T0BW)','Read: 560MB/s, Write: 530MB/s, Capacity: 2000GB, Size: 2.5 inch, Display: SATA',1799,50,0),(9,'Power Supply','Antec','Antec 750W NeoECO 80 Plus Platinum NE750','Power: 750W, 80 Plus Efficiency levels: 80 Plus Platinum, Modular: Full modular',989,60,0),(10,'RAM','Kingston','Kingston Value Ram 2666MHz 8GB','Capacity: 8GB, Type of RAM: DDR4, Speed: 2666MHz, DIMM/SODIMM: DIMM',330,2000,0),(11,'RAID Card','ASUS','ASUS HYPER M.2 X16 CARD','Display: PCI-E 16x',520,59,0),(12,'Sound Card','Creative','Creative Sound Blaster Z PCle Sound Card','Display: PCle, Sound Channel: 5.1',719,30,0);
/*!40000 ALTER TABLE `product` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;


--
-- Table structure for table `orderDetail`
--

DROP TABLE IF EXISTS `orderDetail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `orderDetail` (
  `orderID` int NOT NULL,
  `productID` int NOT NULL,
  `quantity` int DEFAULT NULL,
  PRIMARY KEY (`orderID`,`productID`),
  KEY `fk_productID_idx` (`productID`),
  CONSTRAINT `fk_orderID` FOREIGN KEY (`orderID`) REFERENCES `order` (`orderID`),
  CONSTRAINT `fk_productID` FOREIGN KEY (`productID`) REFERENCES `product` (`productID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orderDetail`
--

LOCK TABLES `orderDetail` WRITE;
/*!40000 ALTER TABLE `orderDetail` DISABLE KEYS */;
/*!40000 ALTER TABLE `orderDetail` ENABLE KEYS */;
UNLOCK TABLES;


-- Dump completed on 2021-04-12 15:37:23
