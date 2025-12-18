package com.example.mobilki_iyoyyy;


public class ChatMessage {
    private int id_forym_message;
    private int id_user2;
    private int id_forym2;
    private String content;
    private String login;

    public ChatMessage(int id_forym_message, int id_user2, int id_forym2, String content, String login) {
        this.id_forym_message = id_forym_message;
        this.id_user2 = id_user2;
        this.id_forym2 = id_forym2;
        this.content = content;
        this.login = login;
    }

    public int getId() { return id_forym_message; }
    public int getIdUser() { return id_user2; }
    public int getIdTopic() { return id_forym2; }
    public String getContent() { return content; }
    public String getLogin() { return login; }
}
