package com.Instagram_clone.Instagram_clone.Entity;

import java.time.Instant;

public class post_DTP {
 
	private String username;
	private String caption;
	private Instant timestamp;
	
	public post_DTP() {
		super();
	}
	public post_DTP(String username, String caption, Instant timestamp) {
		super();
		this.username = username;
		this.caption = caption;
		this.timestamp = timestamp;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getCaption() {
		return caption;
	}
	public void setCaption(String caption) {
		this.caption = caption;
	}
	public Instant getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Instant timestamp) {
		this.timestamp = timestamp;
	}

}
