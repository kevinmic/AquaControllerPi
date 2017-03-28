package com.kevin_mic.aqua.service.action.schedulevalidators;

import org.junit.Test;

import static org.junit.Assert.*;

public class AlwaysOnScheduleServiceTest {

    @Test
    public void test_validate() {
        new AlwaysOnScheduleService().validate(null, null);
    }

}