USE crs_db;

CREATE TABLE users(
	UsersId INT NOT NULL AUTO_INCREMENT Primary Key,
    Username VARCHAR(16) NOT NULL,
    Password VARCHAR(16) NOT NULL,
    Fullname VARCHAR(30) NOT NULL,
    Email VARCHAR(30) NOT NULL
);