package com.kevin_mic.aqua.dbi;

import com.kevin_mic.aqua.entity.Pin;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapperFactory;
import org.skife.jdbi.v2.tweak.BeanMapperFactory;

import java.util.List;

@RegisterMapperFactory(BeanMapperFactory.class)
public abstract class PinDbi {
    @SqlQuery("select * from " + Pin.TABLE_NAME + " where pinSupplierId = :supplierId")
    public abstract List<Pin> getPinsForSupplier(@Bind("supplierId") int supplierId);

    @SqlQuery("select * from " + Pin.TABLE_NAME + " where pinSupplierId = :supplierId for update")
    public abstract List<Pin> getPins_forUpdate(@Bind("supplierId") int supplierId);

    @SqlUpdate("insert into " + Pin.TABLE_NAME + " (pinNumber, pinSupplierId) values (:pinNumber, :pinSupplierId)")
    @GetGeneratedKeys
    public abstract int insert(@BindBean Pin pin);

    public void addDeviceOwnershipWithCheck(@Bind("pinId") int pinId, @Bind("deviceId") int deviceId) {
        Pin pin = getPin_ForUpdate(pinId);
        if (pin != null && pin.getOwnedByDeviceId() != null) {
            throw new RuntimeException("Pin Already In Use");
        }
        addDeviceOwnership(pinId, deviceId);
    }

    @SqlQuery("select * from " + Pin.TABLE_NAME + " where pinId = :pinId for update")
    public abstract Pin getPin_ForUpdate(@Bind("pinId") int pinId);

    @SqlUpdate("update " + Pin.TABLE_NAME + " set ownedByDeviceId = :deviceId where pinId = :pinId")
    abstract void addDeviceOwnership(@Bind("pinId") int pinId, @Bind("deviceId") int deviceId);

    @SqlUpdate("update " + Pin.TABLE_NAME + " set ownedByDeviceId = null where pinId = :pinId and ownedByDeviceId = :deviceId")
    public abstract void removeDeviceOwnership(@Bind("pinId") int pinId, @Bind("deviceId") int deviceId);

    @SqlUpdate("delete from " + Pin.TABLE_NAME + " where pinSupplierId = :supplierId")
    public abstract void deleteBySupplier(@Bind("supplierId") int supplierId);
}
