package com.kevin_mic.aqua.service;

import com.kevin_mic.aqua.dbi.TestDbi;
import com.kevin_mic.aqua.model.TestObj;

import javax.inject.Inject;
import java.util.List;

public class TestService {
    TestDbi testDbi;

    @Inject
    public TestService(TestDbi testDbi) {
        this.testDbi = testDbi;
    }

    public List<TestObj> findTests() {
        return testDbi.getTests();
    }

    public void insert(TestObj obj) {
        testDbi.insert(obj);
    }
}
