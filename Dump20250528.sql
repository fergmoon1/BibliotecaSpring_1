-- MySQL dump 10.13  Distrib 8.0.42, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: biblioteca_1
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
-- Table structure for table `dvd`
--

DROP TABLE IF EXISTS `dvd`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `dvd` (
  `id` bigint NOT NULL,
  `genero` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `director` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `duracion` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `dvd_ibfk_1` FOREIGN KEY (`id`) REFERENCES `elemento_biblioteca` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dvd`
--

LOCK TABLES `dvd` WRITE;
/*!40000 ALTER TABLE `dvd` DISABLE KEYS */;
INSERT INTO `dvd` VALUES (3,'Ciencia Ficción-Editado','Christopher Nolan',148),(6,'Ficcion','Cristopher Nolan',148),(7,'Anima Drama Histórico','Isao takahata  -Editado',89);
/*!40000 ALTER TABLE `dvd` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `elemento_biblioteca`
--

DROP TABLE IF EXISTS `elemento_biblioteca`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `elemento_biblioteca` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `titulo` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `tipo` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `fecha_publicacion` date DEFAULT NULL,
  `descripcion` text COLLATE utf8mb4_unicode_ci,
  `ano_publicacion` int NOT NULL,
  `autor` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `elemento_biblioteca`
--

LOCK TABLES `elemento_biblioteca` WRITE;
/*!40000 ALTER TABLE `elemento_biblioteca` DISABLE KEYS */;
INSERT INTO `elemento_biblioteca` VALUES (1,'El Quijote de la Mancha','LIBRO','1605-01-02','Una de las obras más importantes de la literatura española.',0,NULL),(2,'National Geographic','REVISTA','2024-02-02','Edición especial sobre el cambio climático.',0,NULL),(3,'Inception','DVD','1995-02-01','Película de ciencia ficción dirigida por Christopher Nolan.',0,NULL),(4,'Cien Años de Soledad','LIBRO','1967-02-02',NULL,0,NULL),(5,'ADSO MAgazine- Editado','LIBRO','2024-10-10',NULL,0,NULL),(6,'Interstellar','LIBRO','2014-05-11',NULL,0,NULL),(7,'La Tumba de las Luciérnagas','LIBRO','2014-01-01',NULL,0,NULL);
/*!40000 ALTER TABLE `elemento_biblioteca` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `libro`
--

DROP TABLE IF EXISTS `libro`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `libro` (
  `id` bigint NOT NULL,
  `autor` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `editorial` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `isbn` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `numero_paginas` int NOT NULL,
  `genero` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `libro_ibfk_1` FOREIGN KEY (`id`) REFERENCES `elemento_biblioteca` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `libro`
--

LOCK TABLES `libro` WRITE;
/*!40000 ALTER TABLE `libro` DISABLE KEYS */;
INSERT INTO `libro` VALUES (1,'Miguel de Cervantes','Editorial EDAF','978-84-414-0625-0',650,NULL),(4,'Gabriel García Márquez',NULL,'123456789',250,NULL);
/*!40000 ALTER TABLE `libro` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `revista`
--

DROP TABLE IF EXISTS `revista`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `revista` (
  `id` bigint NOT NULL,
  `categoria` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `editorial` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `numero` int NOT NULL,
  `numero_edicion` int NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `revista_ibfk_1` FOREIGN KEY (`id`) REFERENCES `elemento_biblioteca` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `revista`
--

LOCK TABLES `revista` WRITE;
/*!40000 ALTER TABLE `revista` DISABLE KEYS */;
INSERT INTO `revista` VALUES (2,'Ciencia','National Geographic',1,0),(5,'tecnología','SENA',5,0);
/*!40000 ALTER TABLE `revista` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-05-28 15:39:39
