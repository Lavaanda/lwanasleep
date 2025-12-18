// TeamResponse.java
package com.example.mobilki_iyoyyy;

import com.google.gson.annotations.SerializedName;

public class TeamResponse {

    @SerializedName("current_user")
    private Friend currentUser;

    @SerializedName("teammate")
    private Friend teammate;

    public Friend getCurrentUser() { return currentUser; }
    public Friend getTeammate() { return teammate; }
}
