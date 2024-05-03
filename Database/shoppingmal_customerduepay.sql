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
-- Table structure for table `customerduepay`
--

DROP TABLE IF EXISTS `customerduepay`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `customerduepay` (
  `duePayId` int NOT NULL AUTO_INCREMENT,
  `customerDueDate` date NOT NULL,
  `customerName` varchar(30) DEFAULT NULL,
  `customerPhoneNumber` varchar(12) DEFAULT NULL,
  `customerEmail` varchar(30) DEFAULT NULL,
  `customerDueAddress` varchar(45) DEFAULT NULL,
  `customerDuePay` float DEFAULT NULL,
  PRIMARY KEY (`duePayId`)
) ENGINE=InnoDB AUTO_INCREMENT=36 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customerduepay`
--

LOCK TABLES `customerduepay` WRITE;
/*!40000 ALTER TABLE `customerduepay` DISABLE KEYS */;
INSERT INTO `customerduepay` VALUES (1,'2023-10-21','Alim','0216562 ','jfsdfsf@email.com ','gfdgg ',20),(2,'2023-10-21','Alim','0216562 ','jfsdfsf@email.com ','gfdgg ',20),(3,'2023-10-17','Alim','0216562 ','jfsdfsf@email.com ','gfdgg ',20),(4,'2023-10-17','Alim','0216562 ','jfsdfsf@email.com ','gfdgg ',20),(5,'2023-10-17','Alim','0216562 ','jfsdfsf@email.com ','gfdgg ',20),(6,'2023-10-17','Alim','0216562 ','jfsdfsf@email.com ','gfdgg ',20),(7,'2023-10-22','Alim','0216562 ','jfsdfsf@email.com ','gfdgg ',75),(8,'2023-10-22','Alim','0216562 ','jfsdfsf@email.com ','gfdgg ',75),(9,'2023-10-22','Faysal','0216562 ','jfsdfsf@email.com ','gfdgg ',75),(10,'2023-10-22','Faysal','012344233 ','dftgfdg ',';kl;klj; ',200),(11,'2023-10-22','Alim','012344233 ','dftgfdg ',';kl;klj; ',50),(12,'2023-10-22','Faysal','012344233 ','dftgfdg ',';kl;klj; ',50),(13,'2023-10-23','Alim','0216562 ','jfsdfsf@email.com ','gfdgg ',25),(14,'2023-10-23','Alim','0216562 ','jfsdfsf@email.com ','gfdgg ',25),(15,'2023-10-23','Alim','0216562 ','jfsdfsf@email.com ','gfdgg ',25),(16,'2023-10-23','Alim','0216562 ','jfsdfsf@email.com ','gfdgg ',25),(17,'2023-10-23','Alim','0216562 ','jfsdfsf@email.com ','gfdgg ',25),(18,'2023-10-23','Alim','0216562 ','jfsdfsf@email.com ','gfdgg ',25),(19,'2023-10-23','Alim','0216562 ','jfsdfsf@email.com ','gfdgg ',25),(20,'2023-10-23','Alim','0216562 ','jfsdfsf@email.com ','gfdgg ',25),(21,'2023-10-23','Alim','0216562 ','jfsdfsf@email.com ','gfdgg ',25),(22,'2023-10-23','Alim','0216562 ','jfsdfsf@email.com ','gfdgg ',25),(23,'2023-10-23','Limon','0154215421 ','limon@email.com ','Khatashur, Mohammadpur, Dhaka-1207 ',50),(24,'2023-10-23','Limon','0154215421 ','limon@email.com ','Khatashur, Mohammadpur, Dhaka-1207 ',50),(25,'2023-10-23','Limon','0154215421 ','limon@email.com ','Khatashur, Mohammadpur, Dhaka-1207 ',50),(26,'2023-10-23','Limon','0154215421 ','limon@email.com ','Khatashur, Mohammadpur, Dhaka-1207 ',50),(27,'2023-10-23','Alim','0216562 ','jfsdfsf@email.com ','gfdgg ',25),(28,'2023-10-23','Alim','0216562 ','jfsdfsf@email.com ','gfdgg ',25),(29,'2023-10-26','Faysal','012344233 ','dftgfdg ',';kl;klj; ',2100),(30,'2023-10-26','Limon','0154215421 ','limon@email.com ','Khatashur, Mohammadpur, Dhaka-1207 ',70),(31,'2023-10-26','Alim','0154215421 ','limon@email.com ','Khatashur, Mohammadpur, Dhaka-1207 ',70),(32,'2023-10-28','Alim','0216562 ','jfsdfsf@email.com ','gfdgg ',290),(33,'2023-10-29','Alim','0216562 ','jfsdfsf@email.com ','gfdgg ',0.199997),(34,'2023-10-29','Limon','0216562 ','jfsdfsf@email.com ','gfdgg ',90),(35,'2023-10-29','Faysal','012344233 ','dftgfdg ',';kl;klj; ',0.199997);
/*!40000 ALTER TABLE `customerduepay` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-10-30 10:51:19
