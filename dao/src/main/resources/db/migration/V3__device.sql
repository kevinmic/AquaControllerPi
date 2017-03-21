CREATE TABLE device (
  deviceId Integer NOT NULL DEFAULT NEXTVAL('id_seq') PRIMARY KEY ,
  pinId VARCHAR(124),
  type VARCHAR(124)  NOT NULL,
  name VARCHAR(124)  NOT NULL,
  hardwareId VARCHAR(32)
);

insert into device (deviceId, type, name) values (2, 'I2C_BUS', 'I2C Bus');
insert into device (deviceId, type, name) values (3, 'SHIFT_REGISTER_BUS', 'ShiftRegister BUS');
