package com.kevin_mic.aqua.dbi;

import com.kevin_mic.aqua.model.Device;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapperFactory;
import org.skife.jdbi.v2.tweak.BeanMapperFactory;

@RegisterMapperFactory(BeanMapperFactory.class)
public interface DeviceDbi {
    @SqlUpdate("insert into " + Device.TABLE_NAME + " (pinId, type, name, hardwareId) values (:pinId, :type, :name, :hardwareId) ")
    @GetGeneratedKeys
    int insert(@BindBean Device device);

    @SqlQuery("select * from " + Device.TABLE_NAME + " where deviceId = :deviceId")
    Device getDevice(@Bind("deviceId") int deviceId);

    @SqlQuery("select * from " + Device.TABLE_NAME + " where deviceId = :deviceId for update")
    Device getDevice_forUpdate(@Bind("deviceId") int deviceId);

    @SqlUpdate("delete from " + Device.TABLE_NAME + " where deviceId = :deviceId ")
    void delete(@Bind("deviceId") int deviceId);
}
