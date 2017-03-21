CREATE TABLE listener (
  listenerId Integer NOT NULL PRIMARY KEY,
  deviceId Integer NOT NULL REFERENCES device(deviceId),
  type VARCHAR(124) NOT NULL,
  name VARCHAR(124) NOT NULL,
  listenerDataJson text NOT NULL
);
