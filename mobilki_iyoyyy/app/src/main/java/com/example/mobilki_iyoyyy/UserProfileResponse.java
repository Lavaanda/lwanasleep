package com.example.mobilki_iyoyyy;

import java.util.List;

public class UserProfileResponse {

    private int id_user;
    private String login;
    private String description;
    private String photo;  // Base64
    private List<Friend> friends;  // <- список друзей с их логинами, фото и статусом

    public int getId_user() { return id_user; }
    public String getLogin() { return login; }
    public String getDescription() { return description; }
    public String getPhoto() { return photo; }

    public List<Friend> getFriends() { return friends; } // <- теперь этот метод существует
}
