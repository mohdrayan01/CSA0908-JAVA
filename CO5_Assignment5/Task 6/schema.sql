CREATE DATABASE IF NOT EXISTS transport;
USE transport;
CREATE TABLE IF NOT EXISTS bus_pass(
 pass_id VARCHAR(20) PRIMARY KEY,reg_no VARCHAR(20) NOT NULL,name VARCHAR(100) NOT NULL,
 route_no INT NOT NULL,boarding_stop VARCHAR(100),validity_date DATE,amount DECIMAL(10,2) NOT NULL
);