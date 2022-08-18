package com.example.tourapplication.influencer;

import com.google.firebase.firestore.ServerTimestamp;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;

public class Posts {
    String  postName   ,postDesc ,imageposturl , influencerName ,influncerimg , location , channel ,email , postId ;
    @ServerTimestamp
    Date timestamp ;






    public Posts(String postName,String postDesc ,String imageposturl , String influencerName , String influncerimg , String location , String channel , String email  ,  String postId , Date timestamp  ) {
        this.postName = postName;
        this.postDesc = postDesc;
        this.imageposturl = imageposturl;
        this.influencerName = influencerName;
        this.influncerimg = influncerimg;
        this.location=location;
        this.channel=channel;
        this.email=email;
        this.postId=postId;
        this.timestamp=timestamp;

    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getPostId() {
        return postId;
    }

    public String getEmail() {
        return email;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Posts() {

    }

    public String getInfluencerName() {
        return influencerName;
    }

    public void setInfluencerName(String influencerName) {
        this.influencerName = influencerName;
    }

    public String getInfluncerimg() {
        return influncerimg;
    }

    public void setInfluncerimg(String influncerimg) {
        this.influncerimg = influncerimg;
    }

    public String getImageposturl() {
        return imageposturl;
    }

    public void setImageposturl(String imageposturl) {
        this.imageposturl = imageposturl;
    }

    public String getPostName() {
        return postName;
    }

    public void setPostName(String postName) {
        this.postName = postName;
    }

    public String getPostDesc() {
        return postDesc;
    }

    public void setPostDesc(String postDesc) {
        this.postDesc = postDesc;
    }

}
