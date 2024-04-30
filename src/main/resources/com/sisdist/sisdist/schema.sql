CREATE DATABASE IF NOT EXISTS service;
USE service;
CREATE TABLE IF NOT EXISTS Cliente_Candidato (
                                                 ID INT AUTO_INCREMENT PRIMARY KEY,
                                                 Nome VARCHAR(255) NOT NULL,
                                                 Email VARCHAR(255) UNIQUE NOT NULL,
                                                 Senha VARCHAR(255) NOT NULL
);
CREATE TABLE IF NOT EXISTS Competencias_Experiencia (
                                                        ID INT AUTO_INCREMENT PRIMARY KEY,
                                                        ID_Candidato INT,
                                                        Competencia VARCHAR(255),
                                                        Experiencia VARCHAR(255),
                                                        FOREIGN KEY (ID_Candidato) REFERENCES Cliente_Candidato (ID)
);
CREATE TABLE IF NOT EXISTS Cliente_Empresa
(
    ID   INT AUTO_INCREMENT PRIMARY KEY,
    Nome VARCHAR(255) NOT NULL,
    Ramo VARCHAR(255),
    Descricao VARCHAR(255),
    Email VARCHAR(255) NOT NULL UNIQUE,
    Senha VARCHAR(255) NOT NULL
);
CREATE TABLE IF NOT EXISTS Vagas_Emprego(
                                            ID INT AUTO_INCREMENT PRIMARY KEY,
                                            ID_Empresa INT,
                                            Ramo VARCHAR(255),
                                            Faixa_Salarial VARCHAR(255),
                                            Competencias_Requeridas VARCHAR(255),
                                            Experiencia_requerida VARCHAR(255),
                                            Disponivel BOOL,
                                            Divulgavel BOOL,
                                            FOREIGN KEY (ID_Empresa) REFERENCES Cliente_Empresa(ID)
)
