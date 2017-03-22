CREATE TABLE device (
  deviceId Integer NOT NULL DEFAULT NEXTVAL('id_seq') PRIMARY KEY ,
  type VARCHAR(124)  NOT NULL,
  name VARCHAR(124)  NOT NULL,
  hardwareId VARCHAR(32)
);

