CREATE TABLE pin (
  pinId INTEGER NOT NULL DEFAULT NEXTVAL('id_seq') PRIMARY KEY,
  pinNumber INTEGER NOT NULL,
  pinSupplierId INTEGER NOT NULL REFERENCES pin_supplier(pinSupplierId),
  ownedByDeviceId INTEGER REFERENCES device(deviceId)
);
