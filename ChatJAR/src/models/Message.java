package models;

import java.util.Date;

public class Message {

	
	private String from;
	private String to;
	private Date date;
	private String subject;
	private String content;

	public Message() {
	}

	public Message(String from, String to, Date date, String subject, String content) {
		super();
		this.from = from;
		this.to = to;
		this.date = date;
		this.subject = subject;
		this.content = content;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	@Override
	public String toString() {
		return "Message [from=" + from + ", to=" + to + ", date=" + date + ", subject=" + subject + ", content="
				+ content + "]";
	}


}
