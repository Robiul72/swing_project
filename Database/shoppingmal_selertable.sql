-- MySQL dump 10.13  Distrib 8.0.30, for Win64 (x86_64)
--
-- Host: localhost    Database: shoppingmal
-- ------------------------------------------------------
-- Server version	8.0.30

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
-- Table structure for table `selertable`
--

DROP TABLE IF EXISTS `selertable`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `selertable` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `Seler_Name` varchar(20) DEFAULT NULL,
  `Unit_Price` float DEFAULT NULL,
  `Quantity` float DEFAULT NULL,
  `Total_Price` float DEFAULT NULL,
  `Discount` float DEFAULT NULL,
  `Actual_Price` float DEFAULT NULL,
  `Cash_Receive` float DEFAULT NULL,
  `Due_Amount` float DEFAULT NULL,
  `Cash_Return` float DEFAULT NULL,
  `Date` date NOT NULL,
  `customerName` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `selertable`
--

LOCK TABLES `selertable` WRITE;
/*!40000 ALTER TABLE `selertable` DISABLE KEYS */;
INSERT INTO `selertable` VALUES (1,'Banana',50,50,2500,25,2475,2400,75,0,'2023-10-18','Alim'),(2,'Banana',50,50,2500,625,1875,2400,75,0,'2023-10-18','Alim'),(3,'Banana',50,50,2500,25,2475,2400,75,0,'2023-10-18','Faysal'),(4,'Banana',50,50,2500,625,1875,2400,75,0,'2023-10-18','Faysal'),(5,'Banana',50,50,2500,0,2500,2400,100,0,'2023-10-18','Faysal'),(6,'Banana',25,40,1000,0,1000,950,50,0,'2023-10-23','Limon'),(7,'Banana',20,5,100,0,100,100,0,0,'2023-10-23','Faysal'),(8,'Banana',6,6,36,0,36,36,0,0,'2023-10-27','Faysal'),(9,'Meat',550,22,12100,0,12100,10000,2100,0,'2023-10-23','Faysal'),(10,'Banana',20,20,400,0,400,350,50,0,'2023-10-26','Limon'),(11,'Meat',20,198,3960,0,3960,3600,360,0,'2023-10-14','Alim'),(12,'Banana',20,20,400,0,400,400,0,0,'2023-10-26','Faysal'),(13,'Banana',20,50,1000,100,900,900,0,0,'2023-10-26','Faysal'),(14,'Banana ( 109.0 )',20,9,180,1.8,178.2,178,0.199997,0,'2023-10-26','Faysal'),(15,'Banana',20,20,400,4,396,396,0,0,'2023-10-27','Alim'),(16,'Banana',20,20,400,0,400,310,90,0,'2023-10-26','Limon'),(17,'Banana',50,20,1000,10,990,900,90,0,'2023-10-18','Limon');
/*!40000 ALTER TABLE `selertable` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-10-30 10:51:20
