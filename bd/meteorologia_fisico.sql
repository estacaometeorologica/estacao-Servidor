-- Geração de Modelo físico
-- Sql ANSI 2003 - brModelo.



CREATE TABLE Estacao (
idEstacao int(11) PRIMARY KEY NOT NULL AUTO_INCREMENT,
apiKey char(20),
nome varchar(100) NOT NULL,
idUsuario int(11) NOT NULL
);

CREATE TABLE Leitor (
idLeitor int(11) PRIMARY KEY NOT NULL AUTO_INCREMENT,
ativo boolean NOT NULL,
idEstacao int(11) NOT NULL,
idSensor int(11) NOT NULL,
FOREIGN KEY(idEstacao) REFERENCES Estacao (idEstacao)
);

CREATE TABLE Sensor (
idSensor int(11) PRIMARY KEY NOT NULL AUTO_INCREMENT,
grandeza varchar(30) NOT NULL,
unidade varchar(30) NOT NULL
);

CREATE TABLE Leitura (
idLeitura int(11) PRIMARY KEY NOT NULL AUTO_INCREMENT,
valor varchar(10) NOT NULL,
data datetime NOT NULL,
idLeitor int(11) NOT NULL,
FOREIGN KEY(idLeitor) REFERENCES Leitor (idLeitor)
);

CREATE TABLE LOG_Leitor (
idLog int(11) PRIMARY KEY NOT NULL AUTO_INCREMENT,
idLeitor int(11) NOT NULL,
ativo boolean NOT NULL,
idEstacao int(11) NOT NULL,
idSensor int(11) NOT NULL,
operacao varchar(255) NOT NULL,
data datetime NOT NULL,
idUsuario int(11) NOT NULL
);

CREATE TABLE Usuario (
idUsuario int(11) PRIMARY KEY NOT NULL AUTO_INCREMENT,
apiKey char(20),
login varchar(255) NOT NULL,
senha varchar(255) NOT NULL,
nome varchar(100) NOT NULL,
sobrenome varchar(100) NOT NULL,
cpf bigint(11) NOT NULL,
telefone bigint(14),
email varchar(255) NOT NULL,
endereco varchar(255),
instituicao varchar(255),
admin boolean NOT NULL,
ativo boolean NOT NULL
);

CREATE TABLE LOG_Usuario (
idLog int(11) PRIMARY KEY NOT NULL AUTO_INCREMENT,
apiKey char(20),
login varchar(255) NOT NULL,
senha varchar(255) NOT NULL,
nome varchar(100) NOT NULL,
sobrenome varchar(100) NOT NULL,
cpf bigint(11) NOT NULL,
email varchar(255) NOT NULL,
telefone bigint(14),
instituicao varchar(255),
endereco varchar(255),
admin boolean NOT NULL,
ativo boolean NOT NULL,
data datetime NOT NULL,
operacao varchar(255) NOT NULL,
idUsuario int(11) NOT NULL,
FOREIGN KEY(idUsuario) REFERENCES Usuario (idUsuario)
);

CREATE TABLE LOG_Sensor (
idLog int(11) PRIMARY KEY NOT NULL AUTO_INCREMENT,
idSensor int(11) NOT NULL,
grandeza varchar(30) NOT NULL,
unidade varchar(30) NOT NULL,
operacao varchar(255) NOT NULL,
data datetime NOT NULL,
idUsuario int(11) NOT NULL,
FOREIGN KEY(idUsuario) REFERENCES Usuario (idUsuario)
);

CREATE TABLE LOG_Leitura (
idLog int(11) PRIMARY KEY NOT NULL AUTO_INCREMENT,
idLeitura int(11) NOT NULL,
operacao varchar(255) NOT NULL,
data datetime NOT NULL,
idUsuario int(11) NOT NULL,
FOREIGN KEY(idUsuario) REFERENCES Usuario (idUsuario)
);

CREATE TABLE LOG_Estacao (
idLog int(11) PRIMARY KEY NOT NULL AUTO_INCREMENT,
idEstacao int(11) NOT NULL,
apiKey char(20) NOT NULL,
nome varchar(100) NOT NULL,
operacao varchar(255) NOT NULL,
data datetime NOT NULL,
idUsuario int(11) NOT NULL,
FOREIGN KEY(idUsuario) REFERENCES Usuario (idUsuario)
);

ALTER TABLE Estacao ADD FOREIGN KEY(idUsuario) REFERENCES Usuario (idUsuario);
ALTER TABLE Leitor ADD FOREIGN KEY(idSensor) REFERENCES Sensor (idSensor);
ALTER TABLE LOG_Leitor ADD FOREIGN KEY(idUsuario) REFERENCES Usuario (idUsuario);
