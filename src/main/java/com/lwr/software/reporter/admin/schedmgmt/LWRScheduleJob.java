package com.lwr.software.reporter.admin.schedmgmt;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.StatefulJob;

import com.lwr.software.reporter.DashboardConstants;
import com.lwr.software.reporter.DashboardConstants.Destination;
import com.lwr.software.reporter.DashboardConstants.Status;
import com.lwr.software.reporter.reportmgmt.Report;
import com.lwr.software.reporter.reportmgmt.ReportManager;
import com.lwr.software.reporter.utils.EmailClient;
	
public class LWRScheduleJob implements StatefulJob{

	private Schedule schedule;
	
	private static Logger logger = LogManager.getLogger(LWRScheduleJob.class);
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		schedule = (Schedule)context.getMergedJobDataMap().get(DashboardConstants.LWR_SCHEDULE_JOB);
		String userName = context.getJobDetail().getKey().getGroup();
		
		Status status = run(userName);
		
		ScheduleRunAudit audit = new ScheduleRunAudit();
		audit.setScheduleName(this.schedule.getScheduleName());
		audit.setReportName(this.schedule.getReportName());
		audit.setStatus(status);
		audit.setUserName(userName);
		audit.setNextRunTime(context.getNextFireTime().getTime());
		audit.setLastRunTime(context.getFireTime().getTime());
		ScheduleManager.getScheduleManager().updateScheduleRunAudit(audit);
		
	}
	
	private Status run(String userName) {
		Status status = Status.RUNNING;
		String reportName = schedule.getReportName();
		String timeStamp ="";
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy-HH-mm");
		timeStamp = format.format(new Date(System.currentTimeMillis()));
		try {
			Report report = ReportManager.getReportManager().getReport(reportName, userName);
//			IReportRenderer renderer = ReportRendererFactory.getReportRenderer(schedule.getFormat(), report, DashboardConstants.HTML_JFREE);
//			
//			if(schedule.getDestination().equals(Destination.PersonalFolders)){
//				File dir = new File(DashboardConstants.PRIVATE_REPORT_DIR+File.separatorChar+userName+File.separatorChar+"schedule"+File.separatorChar);
//				dir.mkdirs();
//				
//				File tempFile = new File(dir.getAbsolutePath()+File.separatorChar+schedule.getScheduleName()+"-"+reportName+"-"+timeStamp+".html");
//				FileOutputStream stream = new FileOutputStream(tempFile);
//				renderer.render(stream);
//				stream.flush();
//				stream.close();
//				
//			}else if(schedule.getDestination().equals(Destination.EMAIL)){
//				String receiverEmails = schedule.getReceiverEmail();
//				if(receiverEmails == null || receiverEmails.isEmpty()){
//					throw new RuntimeException("Receiver Email(s) cannot be null");
//				}
//				
//				String senderEmail = schedule.getSenderEmail();
//				if(senderEmail == null || senderEmail.isEmpty()){
//					throw new RuntimeException("Sender Email cannot be null");
//				}
//				
//				String hostName = schedule.getSmtpHost();
//				if(hostName == null || hostName.isEmpty()){
//					throw new RuntimeException("Sender Email cannot be null");
//				}
//				
//				String port = schedule.getSmtpPort();
//				if(port == null || port.isEmpty()){
//					port = "25";
//				}
//
//				File dir = new File(DashboardConstants.TEMP_PATH);
//				File tempFile = new File(dir.getAbsolutePath()+File.separatorChar+schedule.getScheduleName()+"-"+reportName+"-"+timeStamp+".html");
//				FileOutputStream stream = new FileOutputStream(tempFile);
//				renderer.render(stream);
//				stream.flush();
//				stream.close();
//				
//				String subject = "Scheduled Report "+schedule.getScheduleName()+" - "+timeStamp;
//				String body = "Scheduled Report "+schedule.getScheduleName()+" - "+timeStamp;
//
//				EmailClient client = new EmailClient(hostName, Integer.parseInt(port));
//				client.sendEmail(senderEmail, receiverEmails, subject, body, tempFile.getAbsolutePath());
//				tempFile.delete();
//			}
			status=Status.SUCCESS;
		} catch (Throwable e) {
			status=Status.ERROR;
			e.printStackTrace();
			logger.error("Unable to execute scheduled job "+schedule.getScheduleName()+" at "+timeStamp,e);
		}
		return status;
	}
}
