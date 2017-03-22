CREATE TABLE pin (
  pinId INTEGER NOT NULL DEFAULT NEXTVAL('id_seq') NOT NULL PRIMARY KEY,
  pinNumber INTEGER NOT NULL,
  pinSupplierId INTEGER NOT NULL REFERENCES pin_supplier(pinSupplierId),
  ownedByDeviceId INTEGER REFERENCES device(deviceId)
);

ALTER TABLE device ADD CONSTRAINT device_pinId_fk FOREIGN KEY (pinId) REFERENCES pin(pinId);

insert into pin (pinid, pinNumber, pinSupplierId, ownedByDeviceId) values (30, 0, 1, 3);
insert into pin (pinid, pinNumber, pinSupplierId, ownedByDeviceId) values (31, 1, 1, 3);
insert into pin (pinid, pinNumber, pinSupplierId, ownedByDeviceId) values (32, 2, 1, 3);
insert into pin (pinid, pinNumber, pinSupplierId, ownedByDeviceId) values (33, 3, 1, 3);
insert into pin (pinid, pinNumber, pinSupplierId, ownedByDeviceId) values (34, 4, 1, 3);
insert into pin (pinid, pinNumber, pinSupplierId) values (35, 5, 1);
insert into pin (pinid, pinNumber, pinSupplierId) values (36, 6, 1);
insert into pin (pinid, pinNumber, pinSupplierId) values (37, 7, 1);
insert into pin (pinid, pinNumber, pinSupplierId) values (38, 8, 1);
insert into pin (pinid, pinNumber, pinSupplierId, ownedByDeviceId) values (39, 9, 1, 2);
insert into pin (pinid, pinNumber, pinSupplierId, ownedByDeviceId) values (40, 10, 1, 2);
insert into pin (pinid, pinNumber, pinSupplierId) values (41, 11, 1);
insert into pin (pinid, pinNumber, pinSupplierId) values (42, 12, 1);
insert into pin (pinid, pinNumber, pinSupplierId) values (43, 13, 1);
insert into pin (pinid, pinNumber, pinSupplierId) values (44, 14, 1);
insert into pin (pinid, pinNumber, pinSupplierId) values (45, 15, 1);
insert into pin (pinid, pinNumber, pinSupplierId) values (46, 16, 1);
insert into pin (pinid, pinNumber, pinSupplierId) values (47, 17, 1);
insert into pin (pinid, pinNumber, pinSupplierId) values (48, 18, 1);
insert into pin (pinid, pinNumber, pinSupplierId) values (49, 19, 1);
insert into pin (pinid, pinNumber, pinSupplierId) values (50, 20, 1);
insert into pin (pinid, pinNumber, pinSupplierId) values (51, 21, 1);
insert into pin (pinid, pinNumber, pinSupplierId) values (52, 22, 1);
insert into pin (pinid, pinNumber, pinSupplierId) values (53, 23, 1);
insert into pin (pinid, pinNumber, pinSupplierId) values (54, 24, 1);
insert into pin (pinid, pinNumber, pinSupplierId) values (55, 25, 1);
insert into pin (pinid, pinNumber, pinSupplierId) values (56, 26, 1);
insert into pin (pinid, pinNumber, pinSupplierId) values (57, 27, 1);
insert into pin (pinid, pinNumber, pinSupplierId) values (58, 28, 1);
insert into pin (pinid, pinNumber, pinSupplierId) values (59, 29, 1);
