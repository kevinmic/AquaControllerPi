package com.kevin_mic.aqua.service.action.schedulevalidators;

import com.google.common.collect.Lists;
import com.kevin_mic.aqua.model.schedule.HourMinute;
import com.kevin_mic.aqua.model.schedule.OnOffSchedule;
import com.kevin_mic.aqua.model.schedule.OnOffTime;
import com.kevin_mic.aqua.model.types.DayOfWeek;
import com.kevin_mic.aqua.service.AquaException;
import com.kevin_mic.aqua.service.ErrorType;
import com.kevin_mic.aqua.service.jobs.OnOffJob;
import org.apache.commons.collections.CollectionUtils;
import org.quartz.JobDetail;
import org.quartz.Trigger;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.kevin_mic.aqua.service.action.schedulevalidators.ScheduleServiceFactory.getActionGroupName;
import static org.quartz.CronScheduleBuilder.atHourAndMinuteOnGivenDaysOfWeek;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

public class OnOffScheduleService implements ScheduleServiceInterface<OnOffSchedule> {
    private static final int MAX_ON_OFF = 6;

    @Override
    public void validate(String fieldName, OnOffSchedule schedule) {
        validateDays(fieldName, schedule.getDays());
        validateOnOff(fieldName, schedule);
        validateOnOffOrder(fieldName, schedule);
    }

    @Override
    public List<ScheduleJob> getJobs(int actionId, OnOffSchedule onOffSchedule) {
        List<ScheduleJob> jobs = new ArrayList<>();

        Integer[] days = onOffSchedule.getDays()
                .stream()
                .map(DayOfWeek::getCronDays)
                .flatMap(s -> s.stream())
                .collect(Collectors.toList())
                .toArray(new Integer[] {});


        jobs.add(loadImmediateJob(actionId, onOffSchedule));
        jobs.add(loadScheduleJob(actionId, true, days, onOffSchedule.getOnOffTimes()));
        jobs.add(loadScheduleJob(actionId, false, days, onOffSchedule.getOnOffTimes()));

        return jobs;
    }

    private ScheduleJob loadImmediateJob(int actionId, OnOffSchedule onOffSchedule) {
        ScheduleJob scheduleJob = new ScheduleJob();

        LocalDate now = LocalDate.now();

        scheduleJob.setJobDetail(createJob(actionId, onOffSchedule.isOnNow(), "_IMMEDIATE"));
        scheduleJob.setTriggers(Lists.newArrayList(loadImmediateTrigger(actionId)));

        return scheduleJob;
    }

    Trigger loadImmediateTrigger(int actionId) {
        return newTrigger()
                .withIdentity("IMMEDIATE", getActionGroupName(actionId))
                .startNow()
                .build();
    }

    private ScheduleJob loadScheduleJob(int actionId, boolean on, Integer[] cronDays, List<OnOffTime> onOffTimes) {

        ScheduleJob scheduleJob = new ScheduleJob();
        scheduleJob.setJobDetail(createJob(actionId, on));

        List<Trigger> triggers = new ArrayList<>();
        scheduleJob.setTriggers(triggers);

        int[] counter = {0};
        onOffTimes.stream().map(t -> on?t.getOn():t.getOff()).forEach(hm -> {
            counter[0]++;
            triggers.add(createTrigger(actionId, on, counter[0], hm, cronDays));
        });

        return scheduleJob;
    }

    private Trigger createTrigger(int actionId, boolean on, int counter, HourMinute hm, Integer[] cronDays) {
        return newTrigger()
                .withIdentity(getOnOffName(on)+ "_" + counter, getActionGroupName(actionId))
                .withSchedule(
                        atHourAndMinuteOnGivenDaysOfWeek(hm.getHour(), hm.getMinute(), cronDays)
                ).build();
    }

    JobDetail createJob(int actionId, boolean on) {
        return createJob(actionId, on, "");
    }

    private JobDetail createJob(int actionId, boolean on, String extraNameInfo) {
        return newJob(OnOffJob.class)
                .withIdentity(getOnOffName(on) + extraNameInfo, getActionGroupName(actionId))
                .usingJobData(OnOffJob.ACTION_ID, actionId)
                .usingJobData(OnOffJob.ON, on)
                .build();
    }

    public static String getOnOffName(boolean on) {
        return on ? "on" : "off";
    }

    void validateOnOff(String fieldName, OnOffSchedule schedule) {
        if (CollectionUtils.isEmpty(schedule.getOnOffTimes())) {
            throw new AquaException(ErrorType.ScheduleOnOffTimesRequired, fieldName);
        }
        if (schedule.getOnOffTimes().size() > MAX_ON_OFF) {
            // I assume that if you have more than MAX_ON_OFF then you are abusing this feature
            // and probably should be using an interval schedule
            throw new AquaException(ErrorType.ScheduleOnOffTimesMaximumExceeded, fieldName);
        }
        if (schedule.getOnOffTimes().stream().filter(onOff -> !onOff.isValid()).findAny().isPresent()) {
            throw new AquaException(ErrorType.ScheduleOnOffInvalid, fieldName);
        }

        schedule.getOnOffTimes().forEach(onOff -> {
            validateHourMinute(fieldName, onOff.getOn());
            validateHourMinute(fieldName, onOff.getOff());
        });
    }

    void validateHourMinute(String fieldName, HourMinute hourMinute) {
        if (hourMinute.getHour() < 0 || hourMinute.getHour() > 23) {
            throw new AquaException(ErrorType.InvalidHour, fieldName);
        }
        if (hourMinute.getMinute() < 0 || hourMinute.getMinute() > 59) {
            throw new AquaException(ErrorType.InvalidMinute, fieldName);
        }
    }

    void validateOnOffOrder(String fieldName, OnOffSchedule schedule) {
        // Sort by On time
        Collections.sort(schedule.getOnOffTimes());

        // Assumptions
        // 1. All ON OFF times will be in order
        // 2a. ON will always be before OFF
        // 2b. EXCEPT - we allow the last OFF to cross to be before ON (so we can cross the 24 hour barrier)

        HourMinute firstOn = null;
        HourMinute lastTime = null;
        for (int i = 0; i < schedule.getOnOffTimes().size(); i++) {
            OnOffTime onOffTime = schedule.getOnOffTimes().get(i);
            HourMinute on = onOffTime.getOn();
            HourMinute off = onOffTime.getOff();

            if (firstOn == null) {
                firstOn = on;
            }

            if (lastTime != null) {
                if (on.equals(lastTime)) {
                    throw new AquaException(ErrorType.ScheduleOnOff_On_SameAsOff, fieldName);
                }
                if (on.isBefore(lastTime)) {
                    throw new AquaException(ErrorType.ScheduleOnOff_On_ConflictsWithPreviousOff, fieldName);
                }
            }
            lastTime = on;

            if (off.equals(lastTime)) {
                throw new AquaException(ErrorType.ScheduleOnOffInvalid, fieldName);
            }
            else if (off.isBefore(lastTime)) {
                if (firstOn.isBefore(off)) {
                    throw new AquaException(ErrorType.ScheduleOnOff_Off_ConflictsWithFirstOn, fieldName);
                }
                if (i != schedule.getOnOffTimes().size() - 1) {
                    throw new AquaException(ErrorType.ScheduleOnOff_Off_OnlyTheLastOffCanBeBeforeOn, fieldName);
                }
            }
            lastTime = off;
        }
    }

    void validateDays(String fieldName, Set<DayOfWeek> days) {
        if (CollectionUtils.isEmpty(days)) {
            throw new AquaException(ErrorType.ScheduleDaysRequired, fieldName);
        }
        else if (days.contains(DayOfWeek.ALL_DAYS) && days.size() > 1) {
            throw new AquaException(ErrorType.ScheduleDays_AllDaysCannotBeWithWeekDays, fieldName);
        }
    }
}

