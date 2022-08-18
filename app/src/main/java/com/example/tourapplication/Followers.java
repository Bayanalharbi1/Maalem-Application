package com.example.tourapplication;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;


public class Followers {
    public String  email , follower_name , follower_image;
    public boolean followedUser ;

    @ServerTimestamp
    public Date timestamp ;

    public Followers(String email, boolean followedUser , String follower_name , String follower_image , Date timestamp ) {
        this.email = email;
        this.followedUser = followedUser;
        this.follower_name = follower_name;
        this.follower_image = follower_image;
        this.timestamp = timestamp;
    }

    public Followers(String email, boolean followedUser){
        this.email = email;
        this.followedUser = followedUser;
    }

    public Followers() {
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getFollower_name() {
        return follower_name;
    }

    public String getFollower_image() {
        return follower_image;
    }

    public String getEmail() {
        return email;
    }

    public boolean isFollowedUser() {
        return followedUser;
    }
}
