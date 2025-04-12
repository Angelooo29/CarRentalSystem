USE crs_db;

CREATE TABLE transaction (
    TransactionId INT AUTO_INCREMENT PRIMARY KEY,
    ClientId INT NOT NULL,
    CarId INT NOT NULL,
    RentStartDate DATE NOT NULL,
    RentEndDate DATE NOT NULL,
    PickOffLocation VARCHAR(255),
    PaymentMethod VARCHAR(20),
    Price DECIMAL(10, 2), -- Changed Price to DECIMAL for better numeric precision
    TransactionDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (ClientId) REFERENCES users_client(ClientId),
    FOREIGN KEY (CarId) REFERENCES cars(CarId)
);
