package com.example.tourapplication;

public class Comment {
    String commentusername , commentText , commentProfilePic ;

    public Comment(String commentusername, String commentText, String commentProfilePic) {
        this.commentusername = commentusername;
        this.commentText = commentText;
        this.commentProfilePic = commentProfilePic;
    }

    public Comment() {
    }

    public String getCommentusername() {
        return commentusername;
    }

    public String getCommentText() {
        return commentText;
    }

    public String getCommentProfilePic() {
        return commentProfilePic;
    }
}
