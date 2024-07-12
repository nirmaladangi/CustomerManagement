/*
 *   Copyright (c) 2024 
 *   All rights reserved.
 */



CREATE DATABASE company;

use company;

CREATE TABLE customer (
    CUSTID INT AUTO_INCREMENT PRIMARY KEY,
    CUSTNAME VARCHAR(255) NOT NULL,
    EMAIL VARCHAR(255) NOT NULL UNIQUE,
    MOBILE_NUMBER INT(10) NOT NULL,
    CITY VARCHAR(20) NOT NULL
);


CREATE TABLE purchaseorder (
    OID INT AUTO_INCREMENT PRIMARY KEY,
    PRODUCT_NAME VARCHAR(255) NOT NULL,
    QUANTITY INT NOT NULL,
    PRICING DECIMAL(10, 2) NOT NULL,
    MRP DECIMAL(10, 2) NOT NULL,
    CUSTID INT,
    FOREIGN KEY (CUSTID) REFERENCES customer(CUSTID) ON DELETE CASCADE ON UPDATE CASCADE
);


CREATE TABLE Shippingdetails (
    SHIPPIN_ID INT AUTO_INCREMENT PRIMARY KEY,
    ADDRESS VARCHAR(50)NOT NULL,
    CITY VARCHAR(20) NOT NULL,
    PINCODE VARCHAR(10) NOT NULL,
    OID INT,
    CUSTID INT,
    FOREIGN KEY (OID) REFERENCES purchaseorderrder(OID) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (CUSTID) REFERENCES customer(CUSTID) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE users (
    UID INT AUTO_INCREMENT PRIMARY KEY,
    USERNAME VARCHAR(50) NOT NULL,
    PASSWORD VARCHAR(50) NOT NULL,
    ADMIN tinyint(1) NOT NULL
);
