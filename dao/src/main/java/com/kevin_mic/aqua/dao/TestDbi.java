package com.kevin_mic.aqua.dao;

import com.kevin_mic.aqua.model.TestObj;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapperFactory;
import org.skife.jdbi.v2.tweak.BeanMapperFactory;

import java.util.List;

@RegisterMapperFactory(BeanMapperFactory.class)
public interface TestDbi {
    @SqlQuery("select id, name from test")
    List<TestObj> getTests();

    @SqlUpdate("insert into test (id, name) values (:id, :name)")
    void insert(@BindBean TestObj obj);
}
