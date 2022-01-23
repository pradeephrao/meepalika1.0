package com.meepalika.dto;

import java.util.Set;

public class EmailContentDto {
	
	private String to;
	private String cc;
	private String bcc;
	private String subject;
	private String body;
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	
	public void setTo(Set<String> to) {
		this.to = EmailContentDto.convertSetToString(to);
	}
	public String getCc() {
		return cc;
	}
	public void setCc(String cc) {
		this.cc = cc;
	}
	
	public void setCc(Set<String> cc) {
		this.cc = EmailContentDto.convertSetToString(cc);
	}
	public String getBcc() {
		return bcc;
	}
	public void setBcc(String bcc) {
		this.bcc = bcc;
	}
	public void setBcc(Set<String> bcc) {
		this.bcc = EmailContentDto.convertSetToString(bcc);
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	
	public static String convertSetToString(Set<String> data) {
		return data != null && !data.isEmpty() ? String.join(",", data) : "";
	}

}
