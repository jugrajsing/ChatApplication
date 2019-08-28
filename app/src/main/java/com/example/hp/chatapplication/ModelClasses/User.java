package com.example.hp.chatapplication.ModelClasses;

public class User {

    String user_id;
    String user_name;
    String user_email;
    String user_mobile_no;
    String user_password;
    String user_secret_id;
    String user_image;
    String resident;


    String connected;

    public User(String user_id, String user_name, String user_email, String user_mobile_no, String user_password, String user_secret_id, String user_image, String resident ,String connected) {
        this.user_id = user_id;
        this.user_name = user_name;
        this.user_email = user_email;
        this.user_mobile_no = user_mobile_no;
        this.user_password = user_password;
        this.user_secret_id = user_secret_id;
        this.user_image = user_image;
        this.resident = resident;
        this.connected = connected;

    }


    public String getConnected() {
        return connected;
    }

    public void setConnected(String connected) {
        this.connected = connected;
    }


    public String getResident() {
        return resident;
    }

    public void setResident(String resident) {
        this.resident = resident;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getUser_mobile_no() {
        return user_mobile_no;
    }

    public void setUser_mobile_no(String user_mobile_no) {
        this.user_mobile_no = user_mobile_no;
    }

    public String getUser_password() {
        return user_password;
    }

    public void setUser_password(String user_password) {
        this.user_password = user_password;
    }


    public String getUser_secret_id() {

        return user_secret_id;
    }

    public void setUser_secret_id(String user_secret_id) {
        this.user_secret_id = user_secret_id;
    }

    public String getUser_image() {
        return user_image;
    }

    public void setUser_image(String user_image) {
        this.user_image = user_image;
    }



}
