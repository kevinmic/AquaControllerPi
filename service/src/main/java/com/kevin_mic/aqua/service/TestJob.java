package com.kevin_mic.aqua.service;

import com.kevin_mic.aqua.dbi.TestDbi;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import javax.inject.Inject;

public class TestJob implements Job {
    TestDbi testDbi;

    @Inject
    public TestJob(TestDbi testDbi) {
        this.testDbi = testDbi;
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        System.out.println("CHECKING TESTS - " + testDbi.getTests().size());
    }
}
