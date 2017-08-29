package org.awesky.common.other;

import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.Message.RecipientType;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

public class EmailHelper {

	/**
	 * 
	 * @param recvEmail 收件人可多个,用","隔开
	 * @param title  标题
	 * @param text   邮件内容
	 */
	public static void sendTextEmail(String recvEmail, String title, String text) {
		try { 
			Properties props = new Properties();
			props.setProperty("mail.transport.protocol", "smtp");
			props.setProperty("mail.host", "smtp.qq.com");
			props.setProperty("mail.smtp.auth", "true");
			props.put("mail.smtp.ssl.enable", "true");
			props.put("mail.smtp.socketFactory.port",  "994");
			Session session = Session.getInstance(props, new Authenticator() {
				@Override
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication("463112653", "manllfvunnfwbjhh");
				}
			});
			session.setDebug(true);
			Message msg = new MimeMessage(session);
			msg.setSubject(title);
			msg.setText(text);
			msg.setFrom(new InternetAddress("463112653@qq.com"));
			msg.setRecipients(RecipientType.TO, InternetAddress.parse(recvEmail));
			msg.setRecipients(RecipientType.CC, InternetAddress.parse(recvEmail));
			Transport.send(msg);
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	public static void sendTextEmail(String recvEmail) {
		try { 
			Properties props = new Properties();
			props.setProperty("mail.transport.protocol", "smtp");
			props.setProperty("mail.host", "smtp.qq.com");
			props.setProperty("mail.smtp.auth", "true");
			props.put("mail.smtp.ssl.enable", "true");
			props.put("mail.smtp.socketFactory.port",  "994");
			Session session = Session.getInstance(props, new Authenticator() {
				@Override
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication("463112653", "manllfvunnfwbjhh");
				}
			});
			session.setDebug(true);
			Message msg = new MimeMessage(session);
			msg.setSubject("Hello Vme");
			//整个邮件的MultiPart(不能直接加入内容，需要在bodyPart中加入)
			Multipart emailPart = new MimeMultipart();
			MimeBodyPart attr1 = new MimeBodyPart();
			attr1.setDataHandler(new DataHandler(new FileDataSource("E:/workspaces/Archon/src/main/webapp/uploadfile/head_img/2601169057.png")));
			attr1.setFileName("tip.pic");
			
			MimeBodyPart attr2 = new MimeBodyPart();
			attr2.setDataHandler(new DataHandler(new FileDataSource("E:/workspaces/Archon/src/main/webapp/uploadfile/head_img/1724836491.png")));
			attr2.setFileName(MimeUtility.encodeText("哦图像"));
			
			MimeBodyPart content = new MimeBodyPart();
			MimeMultipart contentPart = new MimeMultipart();
			
			MimeBodyPart imgPart = new MimeBodyPart();
			imgPart.setDataHandler(new DataHandler(new FileDataSource("E:/workspaces/Archon/src/main/webapp/uploadfile/head_img/1724836491.png")));
			imgPart.setContentID("pic");
			
			MimeBodyPart htmlPart = new MimeBodyPart();
			htmlPart.setContent("<h1><a href='www.baidu.com'>百度一下</a><img src='cid:pic'/></h1>", "text/html;charset=utf-8");
			
			contentPart.addBodyPart(imgPart);
			contentPart.addBodyPart(htmlPart);
			content.setContent(contentPart);
			
			emailPart.addBodyPart(attr1);
			emailPart.addBodyPart(attr2);
			emailPart.addBodyPart(content);
			msg.setContent(emailPart);
			
			msg.setFrom(new InternetAddress("463112653@qq.com"));
			msg.setRecipients(RecipientType.TO, InternetAddress.parse("2601169057@qq.com,13687018680@163.com"));
			msg.setRecipients(RecipientType.CC, InternetAddress.parse("2601169057@qq.com,1724836491@qq.com"));
			Transport.send(msg);
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	
}
