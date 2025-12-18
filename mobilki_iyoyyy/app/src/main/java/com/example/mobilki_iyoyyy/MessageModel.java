package com.example.mobilki_iyoyyy;

public class MessageModel {
    private int id;
    private String login;
    private String text;

    public MessageModel(int id, String login, String text) {
        this.id = id;
        this.login = login;
        this.text = text;
    }

    public int getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public String getText() {
        return text;
    }
}
