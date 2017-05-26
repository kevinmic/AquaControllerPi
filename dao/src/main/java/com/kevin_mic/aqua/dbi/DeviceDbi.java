package com.kevin_mic.aqua.dbi;

import com.kevin_mic.aqua.model.dbobj.Device;
import com.kevin_mic.aqua.model.dbobj.DevicePin;
import com.kevin_mic.aqua.model.dbobj.Pin;
import com.kevin_mic.aqua.model.dbobj.PinSupplier;
import com.kevin_mic.aqua.model.joins.DevicePinSupplierJoin;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapperFactory;
import org.skife.jdbi.v2.tweak.BeanMapperFactory;

import java.util.List;

@RegisterMapperFactory(BeanMapperFactory.class)
public interface DeviceDbi {
    @SqlUpdate("insert into " + Device.TABLE_NAME + " (type, name, hardwareId, defaultOn) values (:type, :name, :hardwareId, :defaultOn) ")
    @GetGeneratedKeys
    int insert(@BindBean Device device);

    @SqlUpdate("update " + Device.TABLE_NAME + " " +
            "   set name = :name, " +
            "       defaultOn = :defaultOn, " +
            "       hardwareId = :hardwareId " +
            " where deviceId = :deviceId ")
    void update(@BindBean Device device);

    @SqlQuery("select * from " + Device.TABLE_NAME + " where deviceId = :deviceId")
    Device getDevice(@Bind("deviceId") int deviceId);

    @SqlQuery("select * from " + Device.TABLE_NAME + " where deviceId = :deviceId for update")
    Device getDevice_forUpdate(@Bind("deviceId") int deviceId);

    @SqlUpdate("delete from " + Device.TABLE_NAME + " where deviceId = :deviceId ")
    void delete(@Bind("deviceId") int deviceId);

    @SqlQuery("select * from " + Device.TABLE_NAME)
    List<Device> getAllDevices();

    @SqlQuery("select * from " + DevicePin.TABLE_NAME + " where deviceId = :deviceId")
    List<DevicePin> getPins(@Bind("deviceId") int deviceId);

    @SqlUpdate("insert into " + DevicePin.TABLE_NAME + " (pinid, deviceId, pinType) values (:pinId, :deviceId, :pinType)")
    void insertDevicePin(@BindBean DevicePin pinDevice);

    @SqlUpdate("delete from " + DevicePin.TABLE_NAME + " where deviceId = :deviceId")
    void removeAllPins(@Bind("deviceId") int deviceId);

    @SqlQuery("select d.defaultOn as deviceDefaultOn, dp.deviceId, dp.pinType, p.pinId, p.pinNumber, p.pinSupplierId, ps.type as pinSupplierType, ps.subType as pinSupplierSubType, ps.hardwareId as pinSupplierHardwareId" +
            " from " + DevicePin.TABLE_NAME + " dp " +
            " INNER JOIN " + Device.TABLE_NAME + " d ON dp.deviceId = d.deviceId " +
            " INNER JOIN " + Pin.TABLE_NAME + " p ON dp.pinId = p.pinId " +
            " INNER JOIN " + PinSupplier.TABLE_NAME + " ps ON p.pinSupplierId = ps.pinSupplierId " +
            " where dp.deviceId = :deviceId")
    List<DevicePinSupplierJoin> getPinsForDevice(@Bind("deviceId") Integer deviceId);
}
