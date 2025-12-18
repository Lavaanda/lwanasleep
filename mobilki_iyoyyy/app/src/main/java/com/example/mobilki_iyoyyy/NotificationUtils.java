package com.example.mobilki_iyoyyy;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.app.Notification;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

public class NotificationUtils {

    //ЕДИНЫЙ КАНАЛ
    public static final String CHANNEL_ID = "INVITE_CHANNEL_V2";

    public static void showInviteNotification(
            Context context,
            InviteModel invite,
            PendingIntent acceptIntent,
            PendingIntent declineIntent
    ) {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context, CHANNEL_ID)
                        .setSmallIcon(R.drawable.chatgpt_image_23___2025____20_07_18)
                        .setContentTitle("Новое приглашение")
                        .setContentText(invite.from_login + " пригласил вас")
                        .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                        .setAutoCancel(true)

                        // 🔥 ОБЯЗАТЕЛЬНО
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(invite.from_login + " пригласил вас в команду"))

                        // 🔥 ДАЖЕ ПУСТОЙ
                        .setContentIntent(
                                PendingIntent.getActivity(
                                        context,
                                        0,
                                        new Intent(),
                                        PendingIntent.FLAG_IMMUTABLE
                                )
                        )

                        .addAction(R.drawable.plus, "Принять", acceptIntent)
                        .addAction(R.drawable.minus, "Отклонить", declineIntent);

        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED) {

            NotificationManagerCompat
                    .from(context)
                    .notify(invite.getId(), builder.build());
        }
    }

    public static void showUserRemovedNotification(
            Context context,
            InviteModel invite,
            PendingIntent acceptIntent,
            PendingIntent declineIntent
    ) {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context, CHANNEL_ID)
                        .setSmallIcon(R.drawable.chatgpt_image_23___2025____20_07_18)
                        .setContentTitle("Подтверждение удаления")
                        .setContentText(invite.from_login + " предлагает удалить вас")
                        .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                        .setAutoCancel(true)

                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(invite.from_login + " предлагает удалить вас из команды"))

                        .setContentIntent(
                                PendingIntent.getActivity(
                                        context,
                                        0,
                                        new Intent(),
                                        PendingIntent.FLAG_IMMUTABLE
                                )
                        )

                        .addAction(R.drawable.plus, "Принять", acceptIntent)
                        .addAction(R.drawable.minus, "Отклонить", declineIntent);

        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED) {

            NotificationManagerCompat
                    .from(context)
                    .notify(invite.getId(), builder.build());
        }
    }
}
