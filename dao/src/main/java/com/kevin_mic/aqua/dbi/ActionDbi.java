package com.kevin_mic.aqua.dbi;

import com.kevin_mic.aqua.model.dbobj.ActionDB;
import com.kevin_mic.aqua.model.dbobj.ActionDevice;
import com.kevin_mic.aqua.model.types.ScheduleType;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapperFactory;
import org.skife.jdbi.v2.tweak.BeanMapperFactory;

import java.util.List;

@RegisterMapperFactory(BeanMapperFactory.class)
public interface ActionDbi {
    @SqlUpdate("insert into " + ActionDB.TABLE_NAME + " (actionId, name, actionType, actionJson, scheduleType) values (:actionId, :name, :actionType, :actionJson, :scheduleType)")
    void insertAction(@BindBean ActionDB action);

    @SqlUpdate("update " + ActionDB.TABLE_NAME + " set name = :name, actionJson = :actionJson, scheduleType = :scheduleType where actionId = :actionId")
    void updateAction(@BindBean ActionDB action);

    @SqlQuery("select * from action")
    List<ActionDB> getActions();

    @SqlQuery("select * from " + ActionDB.TABLE_NAME + " where actionId = :actionId")
    ActionDB getAction(@Bind("actionId") int action);

    @SqlQuery("select nextval('id_seq')")
    int getNextId();

    @SqlUpdate("delete from " + ActionDB.TABLE_NAME + " where actionId = :actionId")
    void delete(@Bind("actionId") int actionId);

    @SqlQuery("select * from " + ActionDevice.TABLE_NAME + " where actionId = :actionId")
    List<ActionDevice> findAllDevices(@Bind("actionId") int actionId);

    @SqlQuery("select * from " + ActionDevice.TABLE_NAME + " where deviceId = :deviceId")
    ActionDevice getActionForDeviceId(@Bind("deviceId") int deviceId);

    @SqlUpdate("insert into " + ActionDevice.TABLE_NAME + " (actionid, deviceId) values (:actionId, :deviceId)")
    void insertActionDevice(@BindBean ActionDevice actionDevice);

    @SqlUpdate("delete from " + ActionDevice.TABLE_NAME + " where actionId = :actionId")
    void deleteActionDevicesForActionId(@Bind("actionId") int actionId);

    @SqlQuery("select * from " + ActionDB.TABLE_NAME + " where scheduleType = :type")
    List<ActionDB> findActionsByScheduleType(@Bind("type") ScheduleType type);
}
