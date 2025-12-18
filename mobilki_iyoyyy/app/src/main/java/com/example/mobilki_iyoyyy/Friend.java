package com.example.mobilki_iyoyyy;

import com.google.gson.annotations.SerializedName;

public class Friend {

    @SerializedName(value = "id", alternate = {"id_user"})
    private int id_user;

    @SerializedName("login")
    private String login;

    @SerializedName("status_user")
    private String status_user;

    @SerializedName("photo_url")
    private String photoUrl;

    @SerializedName("photo")
    private String photoBase64;

    @SerializedName("is_friend")
    private boolean is_friend;



    public String getPhotoBase64() {
        return photoBase64;
    }


    public boolean isFriend() {
        return is_friend;
    }

    public void setIsFriend(boolean is_friend) {
        this.is_friend = is_friend;
    }




    public int getId() {
        return id_user;
    }

    public String getLogin() {
        return login != null ? login : "";
    }

    public String getStatus() {
        return status_user != null ? status_user : "";
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public Friend(int id_user, String login, String status_user, String photo) {
        this.id_user = id_user;
        this.login = login;
        this.status_user = status_user;
        this.photoUrl = photo;
    }
}
