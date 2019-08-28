package com.example.hp.chatapplication.ModelClasses;

public class FriendListModel {
    String name;
    String image;
    String gender;
    String secret_id;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    String user_id;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public FriendListModel(String name, String image, String gender, String secret_id ,  String user_id) {
        this.name = name;
        this.image = image;
        this.gender = gender;
        this.secret_id = secret_id;
        this.user_id = user_id;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getSecret_id() {
        return secret_id;
    }

    public void setSecret_id(String secret_id) {
        this.secret_id = secret_id;
    }






}
