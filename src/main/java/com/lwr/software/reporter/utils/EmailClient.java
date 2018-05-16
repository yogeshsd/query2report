/* 
	Query2Report Copyright (C) 2018  Yogesh Deshpande
	
	This file is part of Query2Report.
	
	Query2Report is free software: you can redistribute it and/or modify
	it under the terms of the GNU General Public License as published by
	the Free Software Foundation, either version 3 of the License, or
	(at your option) any later version.
	
	Query2Report is distributed in the hope that it will be useful,
	but WITHOUT ANY WARRANTY; without even the implied warranty of
	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	GNU General Public License for more details.
	
	You should have received a copy of the GNU General Public License
	along with Query2Report.  If not, see <http://www.gnu.org/licenses/>.
*/

package com.lwr.software.reporter.utils;

import java.io.BufferedReader;

import java.io.File;
import java.io.FileReader;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;


public class EmailClient {
	
	private static Logger logger = LogManager.getLogger(EmailClient.class);
	private String hostName;
	
	private int port;
	
	private String userName;
	
	private String password;

	private boolean authRequired;
	
	public EmailClient(String hostName,int port) {
		this.hostName=hostName;
		this.port=port;
	}
	
	public EmailClient(String hostName,int port,String userName,String password) {
		this.hostName=hostName;
		this.port=port;
		this.userName=userName;
		this.password=password;
		this.authRequired=true;
	}

	public void sendEmail(String toEmail, String fromEmail,String subjectText, String messageText,String fileName) throws Exception {
		String[] tos = null;
		if(toEmail.contains(";")){
			tos = toEmail.split(";");
		}else{
			tos = toEmail.split(",");
		}
		Properties properties = System.getProperties();
		properties.put("mail.smtp.starttls.enable", "false");
		properties.put("mail.smtp.host", hostName);
		properties.put("mail.smtp.port", port);
		
		Session session = null;
		if (this.authRequired) {
			properties.put("mail.smtp.auth", "true");
			session = Session.getDefaultInstance(properties, new Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(userName, password);
				}
			});
		} else
			session = Session.getDefaultInstance(properties);
		
		MimeMessage message = new MimeMessage(session);
		message.setFrom(new InternetAddress(fromEmail));
		for (String to : tos)
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
		message.setSubject(subjectText);
		
		Multipart multipart = new MimeMultipart();
		BodyPart messageBodyPart = new MimeBodyPart();
		if(fileName != null){
			FileReader reader = new FileReader(fileName);
			BufferedReader bufReader = new BufferedReader(reader);
			StringBuffer messageBody = new StringBuffer();
			while(true){
				String line = bufReader.readLine();
				if(line == null)
					break;
				messageBody.append(line);
			}
			messageBodyPart.setContent(messageBody.toString(), "text/html");
		}else{
			messageBodyPart.setContent("Review SHR Status.", "text/html");
		}
		multipart.addBodyPart(messageBodyPart);
		
		
		DataSource source = new FileDataSource(fileName);
		String shortFileName = new File(fileName).getName();
		MimeBodyPart fileAttachementPart = new MimeBodyPart();
		fileAttachementPart.setDataHandler(new DataHandler(source));
		fileAttachementPart.setFileName(shortFileName);
		multipart.addBodyPart(fileAttachementPart);

		message.setContent((Multipart) multipart);
		Transport.send(message, message.getAllRecipients());
		Thread.sleep(5000);
	}
}