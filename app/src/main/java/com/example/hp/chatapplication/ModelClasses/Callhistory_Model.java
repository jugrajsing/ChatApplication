package com.example.hp.chatapplication.ModelClasses;

public class Callhistory_Model {
    private String callername;
    //  private int caller_profile;
    private String calling_time;
    private String calling_status_text;

    public Callhistory_Model() {
    }

    public Callhistory_Model(String callername, String calling_time, String calling_status_text) {
        this.callername = callername;
        // this.caller_profile = caller_profile;
        this.calling_time = calling_time;
        this.calling_status_text = calling_status_text;
    }

    public String getCallername() {
        return callername;
    }

    public void setCallername(String callername) {
        this.callername = callername;
    }

   /* public int getCaller_profile() {
        return caller_profile;
    }

    public void setCaller_profile(int caller_profile) {
        this.caller_profile = caller_profile;
    }*/

    public String getCalling_time() {
        return calling_time;
    }

    public void setCalling_time(String calling_time) {
        this.calling_time = calling_time;
    }

    public String getCalling_status_text() {
        return calling_status_text;
    }

    public void setCalling_status_text(String calling_status_text) {
        this.calling_status_text = calling_status_text;
    }
}
