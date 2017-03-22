package com.kevin_mic.aqua.service;

import com.kevin_mic.aqua.dao.PinSupplierDao;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import javax.inject.Inject;

public class TestJob implements Job {
    private final PinSupplierDao pinSupplierDao;

    @Inject
    public TestJob(PinSupplierDao pinSupplierDao) {
        this.pinSupplierDao = pinSupplierDao;
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        System.out.println("CHECKING suppliers - " + pinSupplierDao.getPinSuppliers().size());
    }
}
