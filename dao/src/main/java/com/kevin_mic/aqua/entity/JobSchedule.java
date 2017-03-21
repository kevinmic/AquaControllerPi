package com.kevin_mic.aqua.entity;

import com.kevin_mic.aqua.type.ScheduleType;

public class JobSchedule {
    public static final String TABLE_NAME = "job_schedule";

    private int scheduleId;
    private int jobId;
    private ScheduleType type;
    private String cronTimer;
    private String intervalTimer;
}
