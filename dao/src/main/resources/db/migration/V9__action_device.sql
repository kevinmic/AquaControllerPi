CREATE TABLE action_device (
  actionId Integer NOT NULL REFERENCES action(actionId),
  deviceId Integer UNIQUE NOT NULL REFERENCES device(deviceId),
  PRIMARY KEY (actionId, deviceId)
);
