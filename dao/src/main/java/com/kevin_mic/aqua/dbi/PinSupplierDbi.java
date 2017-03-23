package com.kevin_mic.aqua.dbi;

import com.kevin_mic.aqua.model.Pin;
import com.kevin_mic.aqua.model.PinSupplier;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapperFactory;
import org.skife.jdbi.v2.tweak.BeanMapperFactory;

import java.util.List;

@RegisterMapperFactory(BeanMapperFactory.class)
public interface PinSupplierDbi {
    @SqlQuery("select * from " + PinSupplier.TABLE_NAME + " where pinSupplierId = :supplierId")
    PinSupplier getSupplier(@Bind("supplierId") int supplierId);

    @SqlQuery("select * from " + PinSupplier.TABLE_NAME)
    List<PinSupplier> getSuppliers();

    @SqlQuery("select * from " + PinSupplier.TABLE_NAME + " where hardwareId = :id")
    PinSupplier getSupplierByHardwareId(@Bind("id") String hardwareId);

    @SqlUpdate("insert into " + PinSupplier.TABLE_NAME +
            " (pinSupplierId, supplierType, name, voltage, hardwareId ) " +
            "values " +
            "(:pinSupplierId, :supplierType, :name, :voltage, :hardwareId)")
    void insert(@BindBean PinSupplier pin);

    @SqlUpdate("delete from " + PinSupplier.TABLE_NAME + " where pinSupplierId = :supplierId")
    void delete(@Bind("supplierId") int pinSupplierId);

    @SqlQuery("select nextval('id_seq')")
    int getNextId();

    @SqlQuery("select * from pin where pinId = :pinId")
    Pin getPin(@Bind("pinId") int pinId);
}
