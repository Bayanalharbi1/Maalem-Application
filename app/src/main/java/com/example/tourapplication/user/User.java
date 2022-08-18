package com.example.tourapplication.user;
public class User {


    public String  mUserName;
    public String  mImageUrl;

    public String  email, password  , channelName , location  ;
    public int type , follower;
    public boolean followedUser ;


    public User() {

    }

    public String getChannelName() {
        return channelName;
    }

    public boolean isFollowedUser() {
        return followedUser;
    }



    public User(String email, boolean followedUser) {
        this.email = email;
        this.followedUser = followedUser;
    }

    public User(String userName, String password, String email , String imageUrl, int type ) {

        mUserName = userName;
        this.password = password;
        this.email = email;
        this.type = type;
        mImageUrl = imageUrl;



    }
    public User(String userName, String password, String email ,String imageUrl,int type ,String channelName , String location , int follower ) {

        this.mUserName = userName;
        this.password = password;
        this.email = email;
        this.type = type;
        mImageUrl = imageUrl;
        this.channelName = channelName;
        this.location = location;
        this.follower = follower;



    }

    public int getFollower() {
        return follower;
    }

    public String getLocation() {
        return location;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public String getEmail() {
        return email;
    }



    public String getPassword() {
        return password;
    }



    public String getUserName() {
        return mUserName;
    }


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
