CREATE SEQUENCE id_seq START 101;

CREATE TABLE pin_supplier (
  pinSupplierId INTEGER NOT NULL PRIMARY KEY,
  supplierType VARCHAR(124) NOT NULL,
  name VARCHAR(124) NOT NULL,
  voltage VARCHAR(124) NULL,
  hardwareId VARCHAR(124) UNIQUE NOT NULL
);

insert into pin_supplier
(pinSupplierId, supplierType, name, voltage, hardwareId)
VALUES
  (1, 'RASBERRY_PI', 'PI', null, 'PI');