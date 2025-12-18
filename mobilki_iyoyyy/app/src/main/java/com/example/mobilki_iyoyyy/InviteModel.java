package com.example.mobilki_iyoyyy;

import com.google.gson.annotations.SerializedName;

public class InviteModel {
    // JSON от сервера содержит ключ invite_id (в SELECT ты писал AS invite_id)
    @SerializedName("invite_id") // ИЗМЕНЕНО: сопоставляем JSON -> java
    public int invite_id;

    @SerializedName("project_id")
    public int project_id;

    @SerializedName("from_user")
    public int from_user;

    @SerializedName("from_login")
    public String from_login;

    @SerializedName("status")
    public String status;

    // удобный геттер, чтобы остальной код не менялся
    public int getId() {
        return invite_id;
    }
}


