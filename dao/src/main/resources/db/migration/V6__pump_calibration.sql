CREATE TABLE pump_calibration (
  deviceId Integer NOT NULL PRIMARY KEY REFERENCES device(deviceId),
  type VARCHAR(124) NOT NULL,
  volume VARCHAR(124) NOT NULL
);
