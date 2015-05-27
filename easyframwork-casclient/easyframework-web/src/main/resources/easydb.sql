-- MySQL dump 10.13  Distrib 5.6.24, for osx10.8 (x86_64)
--
-- Host: localhost    Database: easydb
-- ------------------------------------------------------
-- Server version	5.6.24

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `sec_authority`
--

DROP TABLE IF EXISTS `sec_authority`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sec_authority` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `description` varchar(500) DEFAULT NULL,
  `name` varchar(200) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sec_authority`
--


--
-- Table structure for table `sec_authority_resource`
--

DROP TABLE IF EXISTS `sec_authority_resource`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sec_authority_resource` (
  `authority_id` bigint(20) NOT NULL,
  `resource_id` bigint(20) NOT NULL,
  KEY `FK_1o8lpl2ci8k3xig94wd0184fe` (`resource_id`),
  KEY `FK_39o5av1nds57taf0769ikdj3m` (`authority_id`),
  CONSTRAINT `FK_1o8lpl2ci8k3xig94wd0184fe` FOREIGN KEY (`resource_id`) REFERENCES `sec_resource` (`id`),
  CONSTRAINT `FK_39o5av1nds57taf0769ikdj3m` FOREIGN KEY (`authority_id`) REFERENCES `sec_authority` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sec_authority_resource`
--


--
-- Table structure for table `sec_menu`
--

DROP TABLE IF EXISTS `sec_menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sec_menu` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `description` varchar(500) DEFAULT NULL,
  `name` varchar(200) NOT NULL,
  `ordernum` int(11) DEFAULT NULL,
  `parent_menu` bigint(20) DEFAULT NULL,
  `resource` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_kms68ce8puu3w1qisfdeasxgx` (`parent_menu`),
  KEY `FK_gr5jfqa6h2ivb5ic7b1a29pvx` (`resource`),
  CONSTRAINT `FK_gr5jfqa6h2ivb5ic7b1a29pvx` FOREIGN KEY (`resource`) REFERENCES `sec_resource` (`id`),
  CONSTRAINT `FK_kms68ce8puu3w1qisfdeasxgx` FOREIGN KEY (`parent_menu`) REFERENCES `sec_menu` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sec_menu`
--

LOCK TABLES `sec_menu` WRITE;
/*!40000 ALTER TABLE `sec_menu` DISABLE KEYS */;
INSERT INTO `sec_menu` VALUES (1,'','系统管理',1,NULL,NULL),(2,'','用户管理',1,1,1),(3,'','部门管理',2,1,2),(4,'','角色管理',3,1,3),(5,'','权限管理',4,1,4),(6,'','资源管理',5,1,5),(7,'','菜单管理',6,1,6);
/*!40000 ALTER TABLE `sec_menu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sec_org`
--

DROP TABLE IF EXISTS `sec_org`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sec_org` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `active` varchar(255) DEFAULT NULL,
  `description` varchar(500) DEFAULT NULL,
  `name` varchar(200) NOT NULL,
  `type` varchar(200) DEFAULT NULL,
  `parent_org` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_guer5lm3n74mk4n6wajvm5q7j` (`parent_org`),
  CONSTRAINT `FK_guer5lm3n74mk4n6wajvm5q7j` FOREIGN KEY (`parent_org`) REFERENCES `sec_org` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sec_org`
--


--
-- Table structure for table `sec_resource`
--

DROP TABLE IF EXISTS `sec_resource`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sec_resource` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(200) NOT NULL,
  `source` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sec_resource`
--

LOCK TABLES `sec_resource` WRITE;
/*!40000 ALTER TABLE `sec_resource` DISABLE KEYS */;
INSERT INTO `sec_resource` VALUES (1,'用户管理','/security/user'),(2,'部门管理','/security/org'),(3,'角色管理','/security/role'),(4,'权限管理','/security/authority'),(5,'资源管理','/security/resource'),(6,'菜单管理','/security/menu');
/*!40000 ALTER TABLE `sec_resource` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sec_role`
--

DROP TABLE IF EXISTS `sec_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sec_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `description` varchar(500) DEFAULT NULL,
  `name` varchar(200) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sec_role`
--


--
-- Table structure for table `sec_role_authority`
--

DROP TABLE IF EXISTS `sec_role_authority`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sec_role_authority` (
  `role_id` bigint(20) NOT NULL,
  `authority_id` bigint(20) NOT NULL,
  KEY `FK_v5f4gpvs8jkf21ny8m9ht7xt` (`authority_id`),
  KEY `FK_kmrlo1d9at9fgm3bqt8jsjk05` (`role_id`),
  CONSTRAINT `FK_kmrlo1d9at9fgm3bqt8jsjk05` FOREIGN KEY (`role_id`) REFERENCES `sec_role` (`id`),
  CONSTRAINT `FK_v5f4gpvs8jkf21ny8m9ht7xt` FOREIGN KEY (`authority_id`) REFERENCES `sec_authority` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sec_role_authority`
--


--
-- Table structure for table `sec_user`
--

DROP TABLE IF EXISTS `sec_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sec_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `enabled` bit(1) DEFAULT NULL,
  `fullname` varchar(100) DEFAULT NULL,
  `password` varchar(50) DEFAULT NULL,
  `salt` varchar(255) DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  `username` varchar(50) NOT NULL,
  `org` bigint(20) DEFAULT NULL,
  `reserved` bit(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_m0lkuyoy79rh5jk3v34uts1ma` (`org`),
  CONSTRAINT `FK_m0lkuyoy79rh5jk3v34uts1ma` FOREIGN KEY (`org`) REFERENCES `sec_org` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sec_user`
--

LOCK TABLES `sec_user` WRITE;
/*!40000 ALTER TABLE `sec_user` DISABLE KEYS */;
INSERT INTO `sec_user` VALUES (1,true,'系统管理员','78198272543e94e5f2e27536c5cecaa320a96356','d5ef5f9438a8c096',1,'admin',NULL,true);
/*!40000 ALTER TABLE `sec_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sec_user_authority`
--

DROP TABLE IF EXISTS `sec_user_authority`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sec_user_authority` (
  `user_id` bigint(20) NOT NULL,
  `authority_id` bigint(20) NOT NULL,
  KEY `FK_ps6wmew0jifq0kyxqnritl78l` (`authority_id`),
  KEY `FK_3l39xq2dypbh9dhbkes1ua0e9` (`user_id`),
  CONSTRAINT `FK_3l39xq2dypbh9dhbkes1ua0e9` FOREIGN KEY (`user_id`) REFERENCES `sec_user` (`id`),
  CONSTRAINT `FK_ps6wmew0jifq0kyxqnritl78l` FOREIGN KEY (`authority_id`) REFERENCES `sec_authority` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sec_user_authority`
--


--
-- Table structure for table `sec_user_role`
--

DROP TABLE IF EXISTS `sec_user_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sec_user_role` (
  `user_id` bigint(20) NOT NULL,
  `role_id` bigint(20) NOT NULL,
  KEY `FK_gbjxy45gdoyjjskfv4lx10ef7` (`role_id`),
  KEY `FK_ltjxt102lg9nu9ep8jn7iybmu` (`user_id`),
  CONSTRAINT `FK_gbjxy45gdoyjjskfv4lx10ef7` FOREIGN KEY (`role_id`) REFERENCES `sec_role` (`id`),
  CONSTRAINT `FK_ltjxt102lg9nu9ep8jn7iybmu` FOREIGN KEY (`user_id`) REFERENCES `sec_user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sec_user_role`
--


/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2015-05-25 15:33:43
