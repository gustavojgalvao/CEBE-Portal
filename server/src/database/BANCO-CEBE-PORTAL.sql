-- MySQL dump 10.13  Distrib 9.6.0, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: cebe
-- ------------------------------------------------------
-- Server version	9.6.0

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
SET @MYSQLDUMP_TEMP_LOG_BIN = @@SESSION.SQL_LOG_BIN;
SET @@SESSION.SQL_LOG_BIN= 0;

--
-- GTID state at the beginning of the backup 
--

SET @@GLOBAL.GTID_PURGED=/*!80000 '+'*/ 'eac28807-1c98-11f1-bd39-a8a159e9f924:1-259';

--
-- Table structure for table `aluno`
--

DROP TABLE IF EXISTS `aluno`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `aluno` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `nome` varchar(250) NOT NULL,
  `TELEFONE` varchar(15) NOT NULL,
  `CPF` varchar(11) NOT NULL,
  `EMAIL` varchar(255) NOT NULL,
  `Data_Nascimento` date NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `ID` (`ID`),
  UNIQUE KEY `TELEFONE` (`TELEFONE`),
  UNIQUE KEY `CPF` (`CPF`),
  UNIQUE KEY `EMAIL` (`EMAIL`),
  UNIQUE KEY `nome` (`nome`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `aluno`
--

/*!40000 ALTER TABLE `aluno` DISABLE KEYS */;
INSERT INTO `aluno` VALUES (1,'João Pedro Almeida','(71) 98888-1111','11122233344','joao@email.com','2002-05-15'),(2,'Marina Silva','(71) 98888-2222','22233344455','marina@email.com','1999-10-20'),(3,'Tiago Santos','(71) 98888-3333','33344455566','tiago@email.com','2001-02-10');
/*!40000 ALTER TABLE `aluno` ENABLE KEYS */;

--
-- Table structure for table `atendimento`
--

DROP TABLE IF EXISTS `atendimento`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `atendimento` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `ID_ALUNO` int NOT NULL,
  `STATUS_ATENDIMENTO` enum('Finalizado','Pendente','Em andamento') NOT NULL,
  `MENSAGEM` varchar(500) NOT NULL,
  `DATA_HORA` datetime NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `ID` (`ID`),
  KEY `FK_ALUNO_ATENDIMENTO` (`ID_ALUNO`),
  CONSTRAINT `FK_ALUNO_ATENDIMENTO` FOREIGN KEY (`ID_ALUNO`) REFERENCES `aluno` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `atendimento`
--

/*!40000 ALTER TABLE `atendimento` DISABLE KEYS */;
/*!40000 ALTER TABLE `atendimento` ENABLE KEYS */;

--
-- Table structure for table `cursos`
--

DROP TABLE IF EXISTS `cursos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cursos` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `nome` varchar(250) NOT NULL,
  `CARGA_HORARIA` int NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `ID` (`ID`),
  UNIQUE KEY `nome` (`nome`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cursos`
--

/*!40000 ALTER TABLE `cursos` DISABLE KEYS */;
INSERT INTO `cursos` VALUES (1,'Lógica de Programação',40),(2,'Banco de Dados Relacional',60),(3,'Desenvolvimento de Jogos',80);
/*!40000 ALTER TABLE `cursos` ENABLE KEYS */;

--
-- Table structure for table `horario_turma`
--

DROP TABLE IF EXISTS `horario_turma`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `horario_turma` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `ID_TURMA` int NOT NULL,
  `DIA_SEMANA` enum('Segunda-feira','Terça-feira','Quarta-feira','Quinta-feira','Sexta-feira') NOT NULL,
  `HORA_INICIO` time NOT NULL,
  `HORA_FIM` time NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_TURMA_HORARIO` (`ID_TURMA`),
  CONSTRAINT `FK_TURMA_HORARIO` FOREIGN KEY (`ID_TURMA`) REFERENCES `turma` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `horario_turma`
--

/*!40000 ALTER TABLE `horario_turma` DISABLE KEYS */;
INSERT INTO `horario_turma` VALUES (1,4,'Segunda-feira','07:30:00','09:10:00'),(2,5,'Terça-feira','09:20:00','11:00:00'),(3,6,'Quinta-feira','18:00:00','20:00:00');
/*!40000 ALTER TABLE `horario_turma` ENABLE KEYS */;

--
-- Table structure for table `matricula`
--

DROP TABLE IF EXISTS `matricula`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `matricula` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `ID_ALUNO` int NOT NULL,
  `ID_TURMA` int NOT NULL,
  `DATA_INSCRICAO` date NOT NULL,
  `STATUS_PAGAMENTO` enum('pago','Pendente','Vencido') NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `ID` (`ID`),
  KEY `FK_ALUNO_MATRICULA` (`ID_ALUNO`),
  KEY `FK_TURMA_MATRICULA` (`ID_TURMA`),
  CONSTRAINT `FK_ALUNO_MATRICULA` FOREIGN KEY (`ID_ALUNO`) REFERENCES `aluno` (`ID`),
  CONSTRAINT `FK_TURMA_MATRICULA` FOREIGN KEY (`ID_TURMA`) REFERENCES `turma` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `matricula`
--

/*!40000 ALTER TABLE `matricula` DISABLE KEYS */;
INSERT INTO `matricula` VALUES (1,1,4,'2026-06-11','pago'),(2,2,5,'2026-06-11','Pendente'),(3,3,6,'2026-06-11','pago');
/*!40000 ALTER TABLE `matricula` ENABLE KEYS */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_unicode_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'IGNORE_SPACE,ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `tg_validar_e_incrementar_vagas` BEFORE INSERT ON `matricula` FOR EACH ROW BEGIN
    DECLARE v_lotacao INT;
    DECLARE v_ocupadas INT;

    -- 1. Busca a lotação máxima e as vagas ocupadas atuais da turma selecionada
    SELECT LOTACAO_MAXIMA, VAGAS_OCUPADAS 
    INTO v_lotacao, v_ocupadas
    FROM turma
    WHERE ID = NEW.ID_TURMA;

    -- 2. Se as vagas ocupadas já atingiram ou passaram o limite, barra a inserção
    IF v_ocupadas >= v_lotacao THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Operação cancelada: Esta turma já atingiu a lotação máxima!';
    ELSE
        -- 3. Caso contrário, atualiza a tabela turma incrementando as vagas ocupadas
        UPDATE turma 
        SET VAGAS_OCUPADAS = VAGAS_OCUPADAS + 1 
        WHERE ID = NEW.ID_TURMA;
    END IF;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_unicode_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'IGNORE_SPACE,ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `tg_decrementar_vagas_canceladas` AFTER DELETE ON `matricula` FOR EACH ROW BEGIN
    -- Quando uma matrícula cai, diminui 1 vaga ocupada da respectiva turma
    UPDATE turma 
    SET VAGAS_OCUPADAS = VAGAS_OCUPADAS - 1 
    WHERE ID = OLD.ID_TURMA;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `professor`
--

DROP TABLE IF EXISTS `professor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `professor` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `NOME` varchar(100) NOT NULL,
  `EMAIL` varchar(255) NOT NULL,
  `ESPECIALIZACAO` varchar(25) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `ID` (`ID`),
  UNIQUE KEY `EMAIL` (`EMAIL`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `professor`
--

/*!40000 ALTER TABLE `professor` DISABLE KEYS */;
INSERT INTO `professor` VALUES (1,'Leonardo','leonardo@cebe.com','Engenharia de Software'),(2,'Camila Oliveira','camila@cebe.com','Banco de Dados'),(3,'Rafael Souza','rafael@cebe.com','Game Design');
/*!40000 ALTER TABLE `professor` ENABLE KEYS */;

--
-- Table structure for table `turma`
--

DROP TABLE IF EXISTS `turma`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `turma` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `ID_CURSOS` int NOT NULL,
  `ID_PROFESSOR` int NOT NULL,
  `turno` enum('Matutino','Vespertino','Noturno') NOT NULL,
  `LOTACAO_MAXIMA` int NOT NULL,
  `VAGAS_OCUPADAS` int NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `ID` (`ID`),
  KEY `FK_CURSOS_TURMA` (`ID_CURSOS`),
  KEY `FK_PROFESSOR_TURMA` (`ID_PROFESSOR`),
  CONSTRAINT `FK_CURSOS_TURMA` FOREIGN KEY (`ID_CURSOS`) REFERENCES `cursos` (`ID`),
  CONSTRAINT `FK_PROFESSOR_TURMA` FOREIGN KEY (`ID_PROFESSOR`) REFERENCES `professor` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `turma`
--

/*!40000 ALTER TABLE `turma` DISABLE KEYS */;
INSERT INTO `turma` VALUES (4,1,1,'Matutino',30,1),(5,2,2,'Vespertino',25,1),(6,3,3,'Noturno',40,1);
/*!40000 ALTER TABLE `turma` ENABLE KEYS */;

--
-- Dumping routines for database 'cebe'
--
SET @@SESSION.SQL_LOG_BIN = @MYSQLDUMP_TEMP_LOG_BIN;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-06-11  9:14:27
