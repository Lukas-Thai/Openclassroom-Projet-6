-- Supprimer les tables dans l'ordre inverse des dépendances
DROP TABLE IF EXISTS Commentaire;
DROP TABLE IF EXISTS Article;
DROP TABLE IF EXISTS Theme;
DROP TABLE IF EXISTS User;
DROP TABLE IF EXISTS Abonnement;
-- Création des tables

CREATE TABLE User (
    id_user INT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    username VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT NULL
);

CREATE TABLE Theme (
    id_theme INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    description TEXT
);

CREATE TABLE Article (
    id_article INT AUTO_INCREMENT PRIMARY KEY,
    id_theme INT NOT NULL,
    id_user INT NOT NULL,
    titre VARCHAR(255) NOT NULL,
    contenu TEXT NOT NULL,
    date_creation DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_theme) REFERENCES Theme(id_theme) ON DELETE CASCADE,
    FOREIGN KEY (id_user) REFERENCES User(id_user) ON DELETE CASCADE
);

CREATE TABLE Commentaire (
    id_commentaire INT AUTO_INCREMENT PRIMARY KEY,
    id_article INT NOT NULL,
    id_user INT NOT NULL,
    contenu TEXT NOT NULL,
    date_commentaire DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_article) REFERENCES Article(id_article) ON DELETE CASCADE,
    FOREIGN KEY (id_user) REFERENCES User(id_user) ON DELETE CASCADE
);

CREATE TABLE Abonnement (
    id_abonnement INT AUTO_INCREMENT PRIMARY KEY,
    id_user INT NOT NULL,
    id_theme INT NOT NULL,
    UNIQUE (id_user, id_theme),
    FOREIGN KEY (id_user) REFERENCES User(id_user) ON DELETE CASCADE,
    FOREIGN KEY (id_theme) REFERENCES Theme(id_theme) ON DELETE CASCADE
);