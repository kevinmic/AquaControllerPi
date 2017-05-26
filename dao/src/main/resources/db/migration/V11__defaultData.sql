insert into pin_supplier (pinSupplierId, type, subtype, name, hardwareId) VALUES (1, 'RASBERRY_PI', 'PI', 'PI', 'PI');

insert into device (deviceId, type, name) values (2, 'I2C_BUS', 'I2C Bus');
insert into device (deviceId, type, name) values (3, 'SHIFT_REGISTER_BUS', 'ShiftRegister BUS');
insert into device (deviceId, type, name) values (4, 'THERMOMETER_BUS', 'Thermometer BUS');

insert into pin (pinid, pinNumber, pinSupplierId, ownedByDeviceId) values (30, 0, 1, 3);
insert into pin (pinid, pinNumber, pinSupplierId, ownedByDeviceId) values (31, 1, 1, 3);
insert into pin (pinid, pinNumber, pinSupplierId, ownedByDeviceId) values (32, 2, 1, 3);
insert into pin (pinid, pinNumber, pinSupplierId, ownedByDeviceId) values (33, 3, 1, 3);
insert into pin (pinid, pinNumber, pinSupplierId, ownedByDeviceId) values (34, 4, 1, 3);
insert into pin (pinid, pinNumber, pinSupplierId) values (35, 5, 1);
insert into pin (pinid, pinNumber, pinSupplierId) values (36, 6, 1);
insert into pin (pinid, pinNumber, pinSupplierId, ownedByDeviceId) values (37, 7, 1, 4);
insert into pin (pinid, pinNumber, pinSupplierId, ownedByDeviceId) values (38, 8, 1, 2);
insert into pin (pinid, pinNumber, pinSupplierId, ownedByDeviceId) values (39, 9, 1, 2);
insert into pin (pinid, pinNumber, pinSupplierId) values (40, 10, 1);
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

insert into device_pin (pinId, deviceId, pinType) values (30, 3, 'SN74HC595_OutputEnabled');
insert into device_pin (pinId, deviceId, pinType) values (31, 3, 'SN74HC595_SER');
insert into device_pin (pinId, deviceId, pinType) values (32, 3, 'SN74HC595_LClock');
insert into device_pin (pinId, deviceId, pinType) values (33, 3, 'SN74HC595_Clock');
insert into device_pin (pinId, deviceId, pinType) values (34, 3, 'SN74HC595_Reset');

insert into device_pin (pinId, deviceId, pinType) values (38, 2, 'I2C_SDA1');
insert into device_pin (pinId, deviceId, pinType) values (39, 2, 'I2C_SLC1');

insert into device_pin (pinId, deviceId, pinType) values (37, 4, 'I2C_THERMOMETER');
