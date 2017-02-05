package com.lwr.software.reporter.admin.schedmgmt;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Set;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.GroupMatcher;

import com.lwr.software.reporter.DashboardConstants;
import com.lwr.software.reporter.DashboardConstants.Recurrence;

public class LWRSchedular {

	private static LWRSchedular _instance = new LWRSchedular();

	private Scheduler schedular;

	private static Logger logger = LogManager.getLogger(LWRSchedular.class);

	private LWRSchedular() {
		init();
	}

	public static LWRSchedular getInstance() {
		if (_instance == null) {
			synchronized (LWRSchedular.class) {
				if (_instance == null) {
					_instance = new LWRSchedular();
				}
			}
		}
		return _instance;
	}

	public void init() {
		try {
			StdSchedulerFactory schedularFactory = new StdSchedulerFactory();
			schedular = schedularFactory.getScheduler();
			schedular.start();
		} catch (SchedulerException e) {
			logger.error("Unable to initialize schedular", e);
		}
	}

	public void resumeSchedular() {
		try {
			this.schedular.resumeAll();
		} catch (SchedulerException e) {
			logger.error("Unable to resume schedular", e);
		}
	}

	public void pauseSchedular() {
		try {
			this.schedular.pauseAll();
		} catch (SchedulerException e) {
			logger.error("Unable to pause schedular", e);
		}
	}

	public void startSchedular() {
		try {
			this.schedular.start();
		} catch (SchedulerException e) {
			logger.error("Unable to start schedular", e);
		}
	}

	public void stopSchedular() {
		try {
			this.schedular.shutdown();
		} catch (SchedulerException e) {
			logger.error("Unable to stop schedular", e);
		}
	}

	public void addJob(Schedule schedule, String userName) throws SchedulerException {
		logger.info("Scheduling job " + schedule.getScheduleName());
		int interval = schedule.getInterval();
		Recurrence recurrance = schedule.getRecurrence();
		Date startDate = schedule.getStartDate();
		Calendar cal = new GregorianCalendar();
		cal.setTime(startDate);
		Trigger trigger = null;
		if (recurrance.equals(Recurrence.DAILY)) {
			trigger = TriggerBuilder.newTrigger().withIdentity(schedule.getScheduleName(), userName).withSchedule(
					SimpleScheduleBuilder.simpleSchedule().withIntervalInMinutes(interval * 24 * 60).repeatForever())
					.build();
		} else if (recurrance.equals(Recurrence.HOURLY)) {
			trigger = TriggerBuilder.newTrigger().withIdentity(schedule.getScheduleName(), userName)
					.withSchedule(
							SimpleScheduleBuilder.simpleSchedule().withIntervalInMinutes(interval * 60).repeatForever())
					.build();
		}
		JobDetail job = JobBuilder.newJob(LWRScheduleJob.class).withIdentity(schedule.getScheduleName(), userName)
				.build();
		job.getJobDataMap().put(DashboardConstants.LWR_SCHEDULE_JOB, schedule);
		this.schedular.scheduleJob(job, trigger);
	}

	public void deleteJob(Schedule schedule, String userName) throws SchedulerException {
		logger.info("Deleting job " + schedule.getScheduleName());
		JobKey key = new JobKey(schedule.getScheduleName(), userName);
		this.schedular.deleteJob(key);
	}

	public void updateJob(Schedule schedule, String userName) throws SchedulerException {
		logger.info("Updating job " + schedule.getScheduleName());
		int interval = schedule.getInterval();
		Recurrence recurrance = schedule.getRecurrence();
		Date startDate = schedule.getStartDate();
		JobKey key = new JobKey(schedule.getScheduleName(), userName);
		this.schedular.deleteJob(key);
		Calendar cal = new GregorianCalendar();
		cal.setTime(startDate);

		Trigger trigger = null;
		if (recurrance.equals(Recurrence.DAILY)) {
			trigger = TriggerBuilder.newTrigger().withIdentity(schedule.getScheduleName(), userName)
					.withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInMinutes(interval * 60 * 24).repeatForever())
					.startAt(startDate)
					.build();
		} else if (recurrance.equals(Recurrence.HOURLY)) {
			trigger = TriggerBuilder.newTrigger().withIdentity(schedule.getScheduleName(), userName)
					.withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInMinutes(interval * 60).repeatForever())
					.startAt(startDate)
					.build();
		}
		JobDetail job = JobBuilder.newJob(LWRScheduleJob.class).withIdentity(schedule.getScheduleName(), userName)
				.build();
		job.getJobDataMap().put(DashboardConstants.LWR_SCHEDULE_JOB, schedule);
		this.schedular.scheduleJob(job, trigger);
	}

}
