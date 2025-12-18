package com.example.mobilki_iyoyyy;

public class RequestUserProject {
    private int project_id;
    private String role;

    public RequestUserProject(int project_id, String role) {
        this.project_id = project_id;
        this.role = role;
    }

    public int getProject_id() {
        return project_id;
    }

    public String getRole() {
        return role;
    }
}
