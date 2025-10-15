package com.Instagram_clone.Instagram_clone.Entity;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@Entity
public class User {

    @Id
    // Assuming 'id' is a primary key and auto-generated for the database, 
    // although the JSON suggests pre-defined IDs (101).
    // You might choose to remove @GeneratedValue if IDs come from an external system.
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int dbId; 

    private int id; // The user ID as present in your JSON (e.g., 101)
    private String username;
    private String profilePic;

    // Getters and Setters (omitted for brevity)
    
    // Default Constructor (required by JPA)
    public User() {}

    // Constructor with fields (optional, but helpful)
    public User(int id, String username, String profilePic) {
        this.id = id;
        this.username = username;
        this.profilePic = profilePic;
    }

	public int getDbId() {
		return dbId;
	}

	public void setDbId(int dbId) {
		this.dbId = dbId;
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

	public String getProfilePic() {
		return profilePic;
	}

	public void setProfilePic(String profilePic) {
		this.profilePic = profilePic;
	}
}