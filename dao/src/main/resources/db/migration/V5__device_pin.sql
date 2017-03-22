CREATE TABLE device_pin (
  deviceId Integer NOT NULL REFERENCES device(deviceId),
  pinId INTEGER NOT NULL REFERENCES pin(pinId),
  pinType VARCHAR(128) NOT NULL,
  PRIMARY KEY (deviceId, pinId)
);

