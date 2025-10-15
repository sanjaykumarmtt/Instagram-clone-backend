package com.Instagram_clone.Instagram_clone.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class story {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String username;
	private String fullname;
	private String story_image;
	
	
	public story() {
		super();
	}
	public story(int id, String username, String fullname, String story_image) {
		super();
		this.id = id;
		this.username = username;
		this.fullname = fullname;
		this.story_image = story_image;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getFullname() {
		return fullname;
	}
	public void setFullname(String fullname) {
		this.fullname = fullname;
	}
	public String getStory_image() {
		return story_image;
	}
	public void setStory_image(String story_image) {
		this.story_image = story_image;
	}
}
