package com.example.mobilki_iyoyyy;

public class SecretLoginRequest {
    private String login;
    private String secret_kod;

    public SecretLoginRequest(String login, String secret_kod) {
        this.login = login;
        this.secret_kod = secret_kod;
    }
}