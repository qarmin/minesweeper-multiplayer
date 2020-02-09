Instalacja MariaDB
Przyłączenie Mysql Connector


CREATE USER 'java'@'localhost' IDENTIFIED BY 'javapass';
GRANT ALL PRIVILEGES ON * . * TO 'java'@'localhost';
FLUSH PRIVILEGES;



