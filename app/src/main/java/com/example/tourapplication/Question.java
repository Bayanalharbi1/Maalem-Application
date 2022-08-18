package com.example.tourapplication;

public class Question {
    String sender , question , answer ,user_image ,qid ;

    public Question() {
    }

    public Question(String sender, String question, String answer , String user_image , String qid) {
        this.sender = sender;
        this.question = question;
        this.answer = answer;
        this.user_image = user_image;
        this.qid = qid;
    }

    public String getQid() {
        return qid;
    }

    public String getUser_image() {
        return user_image;
    }

    public void setUser_image(String user_image) {
        this.user_image = user_image;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
