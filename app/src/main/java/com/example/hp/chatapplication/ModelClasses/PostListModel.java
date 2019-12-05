package com.example.hp.chatapplication.ModelClasses;

public class PostListModel {
    private String post_id;


    private String you_liked;
    private String you_unliked;
    private String post_title;
    private String content;
    private String postedby;
    private String postedby_name;
    private String post_likes;
    private String posted_date;
    private String post_profileimg;


    public PostListModel(String post_id, String post_title, String content, String postedby, String postedby_name, String post_likes, String you_unliked, String posted_date, String post_profileimg, String you_liked) {
        this.post_id = post_id;
        this.post_title = post_title;
        this.content = content;
        this.postedby = postedby;
        this.postedby_name = postedby_name;
        this.post_likes = post_likes;
        this.you_unliked = you_unliked;
        this.posted_date = posted_date;
        this.post_profileimg = post_profileimg;
        this.you_liked = you_liked;
    }

    public String getYou_unliked() {
        return you_unliked;
    }

    public void setYou_unliked(String you_unliked) {
        this.you_unliked = you_unliked;
    }

    public String getYou_liked() {
        return you_liked;
    }

    public void setYou_liked(String you_liked) {
        this.you_liked = you_liked;
    }

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    public String getPost_title() {
        return post_title;
    }

    public void setPost_title(String post_title) {
        this.post_title = post_title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPostedby() {
        return postedby;
    }

    public void setPostedby(String postedby) {
        this.postedby = postedby;
    }

    public String getPostedby_name() {
        return postedby_name;
    }

    public void setPostedby_name(String postedby_name) {
        this.postedby_name = postedby_name;
    }

    public String getPost_likes() {
        return post_likes;
    }

    public void setPost_likes(String post_likes) {
        this.post_likes = post_likes;
    }

    public String getPosted_date() {
        return posted_date;
    }

    public void setPosted_date(String posted_date) {
        this.posted_date = posted_date;
    }

    public String getPost_profileimg() {
        return post_profileimg;
    }

    public void setPost_profileimg(String post_profileimg) {
        this.post_profileimg = post_profileimg;
    }
}
