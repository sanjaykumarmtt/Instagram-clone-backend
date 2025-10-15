package com.Instagram_clone.Instagram_clone.Entity;
import java.time.Instant;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

@Entity
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id; 

    // One Post has One User who created it
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "dbId") // Link to the User entity's primary key
    private User user;

    private String image;
    private String caption;
    private Integer likes;
    private Instant timestamp; // Using Instant to handle the "2025-02-11T14:30:00Z" ISO format

    // One Post has Many Comments
    // mappedBy points to the 'post' field in the Comment entity
    
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Comment> comments;

    // Getters and Setters (omitted for brevity)

    // Default Constructor (required by JPA)
    public Post() {}

    // Constructor with fields (optional, but helpful)
    public Post(int id, User user, String image, String caption, Integer likes, Instant timestamp) {
        this.id = id;
        this.user = user;
        this.image = image;
        this.caption = caption;
        this.likes = likes;
        this.timestamp = timestamp;
    }

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	public Integer getLikes() {
		return likes;
	}

	public void setLikes(Integer likes) {
		this.likes = likes;
	}

	public Instant getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Instant timestamp) {
		this.timestamp = timestamp;
	}

	public List<Comment> getComments() {
		return comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}
    
}
	
	
	
	
//	"posts": [
//	          {
//	            "id": 1,
//	            "user": {
//	              "id": 101,
//	              "username": "john_doe",
//	              "profile_pic": "src/assets/images2.jpg"
//	            },
//	            "image": "src/assets/images1.jpeg",
//	            "caption": "Beautiful sunset 🌅 #Nature",
//	            "likes": 120,
//	            "comments": [
//	              {
//	                "user": "alice_wonder",
//	                "comment": "Wow! Amazing shot! 📸"
//	              },
//	              {
//	                "user": "mark_92",
//	                "comment": "Where is this place?"
//	              }
//	            ],
//	            "timestamp": "2025-02-11T14:30:00Z"
//	          },
//}
//}