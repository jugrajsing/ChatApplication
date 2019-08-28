package com.example.hp.chatapplication.ModelClasses;

public class Social_user_name {
    private String image_user_list_profile;
    private String text_user_list_nickname;

    public Social_user_name(String image_user_list_profile, String text_user_list_nickname) {
        this.image_user_list_profile = image_user_list_profile;
        this.text_user_list_nickname = text_user_list_nickname;
    }

    public String getImage_user_list_profile() {
        return image_user_list_profile;
    }

    public void setImage_user_list_profile(String image_user_list_profile) {
        this.image_user_list_profile = image_user_list_profile;
    }

    public String getText_user_list_nickname() {
        return text_user_list_nickname;
    }

    public void setText_user_list_nickname(String text_user_list_nickname) {
        this.text_user_list_nickname = text_user_list_nickname;
    }
}
