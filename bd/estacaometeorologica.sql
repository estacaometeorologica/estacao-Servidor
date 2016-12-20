-- phpMyAdmin SQL Dump
-- version 4.5.1
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Generation Time: 19-Jun-2016 às 01:48
-- Versão do servidor: 10.1.10-MariaDB
-- PHP Version: 7.0.4

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `estacaometeorologica`
--

-- --------------------------------------------------------

--
-- Estrutura da tabela `estacao`
--

CREATE TABLE `estacao` (
  `idEstacao` int(11) NOT NULL,
  `apiKey` char(20) DEFAULT NULL,
  `nome` varchar(100) NOT NULL,
  `idUsuario` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Estrutura da tabela `leitor`
--

CREATE TABLE `leitor` (
  `idLeitor` int(11) NOT NULL,
  `ativo` tinyint(1) NOT NULL,
  `idEstacao` int(11) NOT NULL,
  `idSensor` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Estrutura da tabela `leitura`
--

CREATE TABLE `leitura` (
  `idLeitura` int(11) NOT NULL,
  `valor` varchar(10) NOT NULL,
  `data` datetime NOT NULL,
  `idLeitor` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Estrutura da tabela `log_estacao`
--

CREATE TABLE `log_estacao` (
  `idLog` int(11) NOT NULL,
  `idEstacao` int(11) NOT NULL,
  `apiKey` char(20) NOT NULL,
  `nome` varchar(100) NOT NULL,
  `operacao` varchar(255) NOT NULL,
  `data` datetime NOT NULL,
  `idUsuario` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Estrutura da tabela `log_leitor`
--

CREATE TABLE `log_leitor` (
  `idLog` int(11) NOT NULL,
  `idLeitor` int(11) NOT NULL,
  `ativo` tinyint(1) NOT NULL,
  `idEstacao` int(11) NOT NULL,
  `idSensor` int(11) NOT NULL,
  `operacao` varchar(255) NOT NULL,
  `data` datetime NOT NULL,
  `idUsuario` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Estrutura da tabela `log_leitura`
--

CREATE TABLE `log_leitura` (
  `idLog` int(11) NOT NULL,
  `idLeitura` int(11) NOT NULL,
  `operacao` varchar(255) NOT NULL,
  `data` datetime NOT NULL,
  `idUsuario` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Estrutura da tabela `log_sensor`
--

CREATE TABLE `log_sensor` (
  `idLog` int(11) NOT NULL,
  `idSensor` int(11) NOT NULL,
  `grandeza` varchar(30) NOT NULL,
  `unidade` varchar(30) NOT NULL,
  `operacao` varchar(255) NOT NULL,
  `data` datetime NOT NULL,
  `idUsuario` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Estrutura da tabela `log_usuario`
--

CREATE TABLE `log_usuario` (
  `idLog` int(11) NOT NULL,
  `apiKey` char(20) DEFAULT NULL,
  `login` varchar(255) NOT NULL,
  `senha` varchar(255) NOT NULL,
  `nome` varchar(100) NOT NULL,
  `sobrenome` varchar(100) NOT NULL,
  `cpf` bigint(11) NOT NULL,
  `email` varchar(255) NOT NULL,
  `telefone` bigint(14) DEFAULT NULL,
  `instituicao` varchar(255) DEFAULT NULL,
  `endereco` varchar(255) DEFAULT NULL,
  `admin` tinyint(1) NOT NULL,
  `ativo` tinyint(1) NOT NULL,
  `data` datetime NOT NULL,
  `operacao` varchar(255) NOT NULL,
  `idUsuario` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Estrutura da tabela `sensor`
--

CREATE TABLE `sensor` (
  `idSensor` int(11) NOT NULL,
  `grandeza` varchar(30) NOT NULL,
  `unidade` varchar(30) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Estrutura da tabela `usuario`
--

CREATE TABLE `usuario` (
  `idUsuario` int(11) NOT NULL,
  `apiKey` char(20) DEFAULT NULL,
  `login` varchar(255) NOT NULL,
  `senha` varchar(255) NOT NULL,
  `nome` varchar(100) NOT NULL,
  `sobrenome` varchar(100) NOT NULL,
  `cpf` bigint(11) NOT NULL,
  `telefone` bigint(14) DEFAULT NULL,
  `email` varchar(255) NOT NULL,
  `endereco` varchar(255) DEFAULT NULL,
  `instituicao` varchar(255) DEFAULT NULL,
  `admin` tinyint(1) NOT NULL,
  `ativo` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `estacao`
--
ALTER TABLE `estacao`
  ADD PRIMARY KEY (`idEstacao`),
  ADD KEY `idUsuario` (`idUsuario`);

--
-- Indexes for table `leitor`
--
ALTER TABLE `leitor`
  ADD PRIMARY KEY (`idLeitor`),
  ADD KEY `idEstacao` (`idEstacao`),
  ADD KEY `idSensor` (`idSensor`);

--
-- Indexes for table `leitura`
--
ALTER TABLE `leitura`
  ADD PRIMARY KEY (`idLeitura`),
  ADD KEY `idLeitor` (`idLeitor`);

--
-- Indexes for table `log_estacao`
--
ALTER TABLE `log_estacao`
  ADD PRIMARY KEY (`idLog`),
  ADD KEY `idUsuario` (`idUsuario`);

--
-- Indexes for table `log_leitor`
--
ALTER TABLE `log_leitor`
  ADD PRIMARY KEY (`idLog`),
  ADD KEY `idUsuario` (`idUsuario`);

--
-- Indexes for table `log_leitura`
--
ALTER TABLE `log_leitura`
  ADD PRIMARY KEY (`idLog`),
  ADD KEY `idUsuario` (`idUsuario`);

--
-- Indexes for table `log_sensor`
--
ALTER TABLE `log_sensor`
  ADD PRIMARY KEY (`idLog`),
  ADD KEY `idUsuario` (`idUsuario`);

--
-- Indexes for table `log_usuario`
--
ALTER TABLE `log_usuario`
  ADD PRIMARY KEY (`idLog`),
  ADD KEY `idUsuario` (`idUsuario`);

--
-- Indexes for table `sensor`
--
ALTER TABLE `sensor`
  ADD PRIMARY KEY (`idSensor`);

--
-- Indexes for table `usuario`
--
ALTER TABLE `usuario`
  ADD PRIMARY KEY (`idUsuario`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `estacao`
--
ALTER TABLE `estacao`
  MODIFY `idEstacao` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;
--
-- AUTO_INCREMENT for table `leitor`
--
ALTER TABLE `leitor`
  MODIFY `idLeitor` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;
--
-- AUTO_INCREMENT for table `leitura`
--
ALTER TABLE `leitura`
  MODIFY `idLeitura` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `log_estacao`
--
ALTER TABLE `log_estacao`
  MODIFY `idLog` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `log_leitor`
--
ALTER TABLE `log_leitor`
  MODIFY `idLog` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `log_leitura`
--
ALTER TABLE `log_leitura`
  MODIFY `idLog` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `log_sensor`
--
ALTER TABLE `log_sensor`
  MODIFY `idLog` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `log_usuario`
--
ALTER TABLE `log_usuario`
  MODIFY `idLog` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `sensor`
--
ALTER TABLE `sensor`
  MODIFY `idSensor` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;
--
-- AUTO_INCREMENT for table `usuario`
--
ALTER TABLE `usuario`
  MODIFY `idUsuario` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;
--
-- Constraints for dumped tables
--

--
-- Limitadores para a tabela `estacao`
--
ALTER TABLE `estacao`
  ADD CONSTRAINT `estacao_ibfk_1` FOREIGN KEY (`idUsuario`) REFERENCES `usuario` (`idUsuario`);

--
-- Limitadores para a tabela `leitor`
--
ALTER TABLE `leitor`
  ADD CONSTRAINT `leitor_ibfk_1` FOREIGN KEY (`idEstacao`) REFERENCES `estacao` (`idEstacao`),
  ADD CONSTRAINT `leitor_ibfk_2` FOREIGN KEY (`idSensor`) REFERENCES `sensor` (`idSensor`);

--
-- Limitadores para a tabela `leitura`
--
ALTER TABLE `leitura`
  ADD CONSTRAINT `leitura_ibfk_1` FOREIGN KEY (`idLeitor`) REFERENCES `leitor` (`idLeitor`);

--
-- Limitadores para a tabela `log_estacao`
--
ALTER TABLE `log_estacao`
  ADD CONSTRAINT `log_estacao_ibfk_1` FOREIGN KEY (`idUsuario`) REFERENCES `usuario` (`idUsuario`);

--
-- Limitadores para a tabela `log_leitor`
--
ALTER TABLE `log_leitor`
  ADD CONSTRAINT `log_leitor_ibfk_1` FOREIGN KEY (`idUsuario`) REFERENCES `usuario` (`idUsuario`);

--
-- Limitadores para a tabela `log_leitura`
--
ALTER TABLE `log_leitura`
  ADD CONSTRAINT `log_leitura_ibfk_1` FOREIGN KEY (`idUsuario`) REFERENCES `usuario` (`idUsuario`);

--
-- Limitadores para a tabela `log_sensor`
--
ALTER TABLE `log_sensor`
  ADD CONSTRAINT `log_sensor_ibfk_1` FOREIGN KEY (`idUsuario`) REFERENCES `usuario` (`idUsuario`);

--
-- Limitadores para a tabela `log_usuario`
--
ALTER TABLE `log_usuario`
  ADD CONSTRAINT `log_usuario_ibfk_1` FOREIGN KEY (`idUsuario`) REFERENCES `usuario` (`idUsuario`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
