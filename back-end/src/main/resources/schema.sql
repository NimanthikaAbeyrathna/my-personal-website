CREATE TABLE IF NOT EXISTS message
(
    id INT PRIMARY KEY AUTO_INCREMENT,
    date VARCHAR(200) NOT NULL,
    name VARCHAR(200) NOT NULL,
    email VARCHAR(300) NOT NULL ,
    message VARCHAR(1000) NOT NULL

);

DROP TABLE IF EXISTS message;