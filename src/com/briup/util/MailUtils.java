package com.briup.util;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;

public class MailUtils {

	public static void sendMail(String email, String emailMsg)
			throws AddressException, MessagingException {
		// 1.创建一个程序与邮件服务器会话对象 Session

		Properties props = new Properties();
		//邮件发送协议 
		props.setProperty("mail.transport.protocol", "SMTP");
		//SMTP邮件服务器 
//		props.setProperty("mail.host", "smtp.exmail.qq.com");
		props.setProperty("mail.host", "smtp.126.com");
		//端口号
		//props.setProperty("mail.smtp.port", "465");
		//是否要求身份认证 
		props.setProperty("mail.smtp.auth", "true");// 指定验证为true

		// 创建验证器
		Authenticator auth = new Authenticator() {
			public PasswordAuthentication getPasswordAuthentication() {
				//akoxaijbfjovbbbj-->授权码
//				return new PasswordAuthentication("yg0329@foxmail.com", "akoxaijbfjovbbbj");
				return new PasswordAuthentication("mr_wangxs@126.com", "YY950329");
			}
		};

		Session session = Session.getInstance(props, auth);

		// 2.创建一个Message，它相当于是邮件内容
		Message message = new MimeMessage(session);

//		message.setFrom(new InternetAddress("yg0329@foxmail.com")); // 设置发送者
		message.setFrom(new InternetAddress("mr_wangxs@126.com")); // 设置发送者
		
		message.setRecipient(RecipientType.TO, new InternetAddress(email)); // 设置发送方式与接收者

		message.setSubject("用户激活");
		// message.setText("这是一封激活邮件，请<a href='#'>点击</a>");

		message.setContent(emailMsg, "text/html;charset=utf-8");

		// 3.创建 Transport用于将邮件发送

		Transport.send(message);
	}
}
