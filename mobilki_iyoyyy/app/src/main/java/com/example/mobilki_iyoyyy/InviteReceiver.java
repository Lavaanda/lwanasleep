package com.example.mobilki_iyoyyy;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class InviteReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        int inviteId = intent.getIntExtra("invite_id", 0);
        String action = intent.getStringExtra("action");

        if ("accept".equals(action)) {
            Toast.makeText(context, "Принял приглашение: " + inviteId, Toast.LENGTH_SHORT).show();
            // здесь можешь вызвать метод API для принятия приглашения
        } else if ("decline".equals(action)) {
            Toast.makeText(context, "Отклонил приглашение: " + inviteId, Toast.LENGTH_SHORT).show();
            // здесь можешь вызвать метод API для отклонения приглашения
        }
    }
}
