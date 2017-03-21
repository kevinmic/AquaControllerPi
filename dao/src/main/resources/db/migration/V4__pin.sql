CREATE TABLE pin (
  pinId VARCHAR(32)  NOT NULL PRIMARY KEY,
  pinSupplierId INTEGER NOT NULL REFERENCES pin_supplier(pinSupplierId),
  ownedByDeviceId INTEGER REFERENCES device(deviceId)
);

ALTER TABLE device ADD CONSTRAINT device_pinId_fk FOREIGN KEY (pinId) REFERENCES pin(pinId);

insert into pin (pinid, pinSupplierId, ownedByDeviceId) values ('PI_0', 1, 3);
insert into pin (pinid, pinSupplierId, ownedByDeviceId) values ('PI_1', 1, 3);
insert into pin (pinid, pinSupplierId, ownedByDeviceId) values ('PI_2', 1, 3);
insert into pin (pinid, pinSupplierId, ownedByDeviceId) values ('PI_3', 1, 3);
insert into pin (pinid, pinSupplierId, ownedByDeviceId) values ('PI_4', 1, 3);
insert into pin (pinid, pinSupplierId) values ('PI_5', 1);
insert into pin (pinid, pinSupplierId) values ('PI_6', 1);
insert into pin (pinid, pinSupplierId) values ('PI_7', 1);
insert into pin (pinid, pinSupplierId) values ('PI_8', 1);
insert into pin (pinid, pinSupplierId, ownedByDeviceId) values ('PI_9', 1, 2);
insert into pin (pinid, pinSupplierId, ownedByDeviceId) values ('PI_10', 1, 2);
insert into pin (pinid, pinSupplierId) values ('PI_11', 1);
insert into pin (pinid, pinSupplierId) values ('PI_12', 1);
insert into pin (pinid, pinSupplierId) values ('PI_13', 1);
insert into pin (pinid, pinSupplierId) values ('PI_14', 1);
insert into pin (pinid, pinSupplierId) values ('PI_15', 1);
insert into pin (pinid, pinSupplierId) values ('PI_16', 1);
insert into pin (pinid, pinSupplierId) values ('PI_17', 1);
insert into pin (pinid, pinSupplierId) values ('PI_18', 1);
insert into pin (pinid, pinSupplierId) values ('PI_19', 1);
insert into pin (pinid, pinSupplierId) values ('PI_20', 1);
insert into pin (pinid, pinSupplierId) values ('PI_21', 1);
insert into pin (pinid, pinSupplierId) values ('PI_22', 1);
insert into pin (pinid, pinSupplierId) values ('PI_23', 1);
insert into pin (pinid, pinSupplierId) values ('PI_24', 1);
insert into pin (pinid, pinSupplierId) values ('PI_25', 1);
insert into pin (pinid, pinSupplierId) values ('PI_26', 1);
insert into pin (pinid, pinSupplierId) values ('PI_27', 1);
insert into pin (pinid, pinSupplierId) values ('PI_28', 1);
insert into pin (pinid, pinSupplierId) values ('PI_29', 1);
