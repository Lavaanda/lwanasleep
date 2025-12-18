package com.example.mobilki_iyoyyy;

public class CommentModel {
    private String login;
    private String content;
    private String time;

    public CommentModel(String login, String content, String time){
        this.login = login;
        this.content = content;
        this.time = time;
    }

    public String getLogin() { return login; }
    public String getText() { return content; }  // getText() теперь возвращает content
    public String getTime() { return time; }
}
