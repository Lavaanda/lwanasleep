package com.example.mobilki_iyoyyy;

import android.graphics.Bitmap;

public class FriendAdapterStaticCache {
    private static FriendAdapter adapter;

    public static void setAdapter(FriendAdapter a) {
        adapter = a;
    }

    public static void updateMyAvatar(int userId, Bitmap bmp) {
        if (adapter != null) {
            adapter.updateFriendPhoto(userId, bmp);
        }
    }
}
