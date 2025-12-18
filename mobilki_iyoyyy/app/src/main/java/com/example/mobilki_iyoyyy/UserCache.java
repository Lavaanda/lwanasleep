package com.example.mobilki_iyoyyy;
import android.graphics.Bitmap;
public class UserCache {
    public static Bitmap profilePhoto = null;

    public static String currentLogin; // текущий логин пользователя
    public static String userId;       // айди текущего пользователя
    public static String userPhotoUrl = null;

    public static boolean hasNoProfilePhoto() {
        return userPhotoUrl == null || userPhotoUrl.isEmpty();
    }
}





