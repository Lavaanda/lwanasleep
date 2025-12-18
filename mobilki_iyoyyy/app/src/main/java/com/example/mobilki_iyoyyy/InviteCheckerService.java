package com.example.mobilki_iyoyyy;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

import androidx.annotation.Nullable;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InviteCheckerService extends Service {

    private Handler handler = new Handler();
    private Runnable runnable;
    private ApiService api;
    private Set<Integer> shownInvites = new HashSet<>();

    @Override
    public void onCreate() {
        super.onCreate();
        api = RetrofitClient.getApiService();
        createNotificationChannel();

        runnable = new Runnable() {
            @Override
            public void run() {
                checkInvites();
                handler.postDelayed(this, 50000);
            }
        };
        handler.post(runnable);
    }

    private void checkInvites() {
        api.getInvites().enqueue(new Callback<List<InviteModel>>() {
            @Override
            public void onResponse(Call<List<InviteModel>> call,
                                   Response<List<InviteModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    for (InviteModel invite : response.body()) {
                        int id = invite.getId();

                        if (!shownInvites.contains(id)) {

                            if ("Ожидает".equals(invite.status)) {
                                showInviteNotification(
                                        getApplicationContext(), invite
                                );

                            } else if ("Ожидание удаления".equals(invite.status)) {

                                Intent acceptIntent =
                                        new Intent(InviteCheckerService.this,
                                                InviteActionReceiver.class);
                                acceptIntent.putExtra("invite_id", invite.getId());
                                acceptIntent.putExtra("action", "accept");

                                PendingIntent acceptPendingIntent =
                                        PendingIntent.getBroadcast(
                                                InviteCheckerService.this,
                                                invite.getId(),
                                                acceptIntent,
                                                PendingIntent.FLAG_UPDATE_CURRENT
                                                        | PendingIntent.FLAG_IMMUTABLE
                                        );

                                Intent declineIntent =
                                        new Intent(InviteCheckerService.this,
                                                InviteActionReceiver.class);
                                declineIntent.putExtra("invite_id", invite.getId());
                                declineIntent.putExtra("action", "decline");

                                PendingIntent declinePendingIntent =
                                        PendingIntent.getBroadcast(
                                                InviteCheckerService.this,
                                                invite.getId() + 1000,
                                                declineIntent,
                                                PendingIntent.FLAG_UPDATE_CURRENT
                                                        | PendingIntent.FLAG_IMMUTABLE
                                        );

                                NotificationUtils.showUserRemovedNotification(
                                        getApplicationContext(),
                                        invite,
                                        acceptPendingIntent,
                                        declinePendingIntent
                                );
                            }

                            shownInvites.add(id);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<InviteModel>> call, Throwable t) {}
        });
    }

    private void showInviteNotification(Context context, InviteModel invite) {
        Intent acceptIntent = new Intent(context, InviteActionReceiver.class);
        acceptIntent.putExtra("invite_id", invite.getId());
        acceptIntent.putExtra("action", "accept");

        PendingIntent acceptPendingIntent =
                PendingIntent.getBroadcast(
                        context,
                        invite.getId(),
                        acceptIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                                | PendingIntent.FLAG_IMMUTABLE
                );

        Intent declineIntent = new Intent(context, InviteActionReceiver.class);
        declineIntent.putExtra("invite_id", invite.getId());
        declineIntent.putExtra("action", "decline");

        PendingIntent declinePendingIntent =
                PendingIntent.getBroadcast(
                        context,
                        invite.getId() + 1000,
                        declineIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                                | PendingIntent.FLAG_IMMUTABLE
                );

        NotificationUtils.showInviteNotification(
                context,
                invite,
                acceptPendingIntent,
                declinePendingIntent
        );
    }

    private void createNotificationChannel() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            android.app.NotificationChannel channel =
                    new android.app.NotificationChannel(
                            NotificationUtils.CHANNEL_ID,
                            "Приглашения в проект",
                            android.app.NotificationManager.IMPORTANCE_HIGH
                    );

            channel.setDescription("Уведомления о приглашениях и действиях");
            channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);

            android.app.NotificationManager manager =
                    getSystemService(android.app.NotificationManager.class);

            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        handler.removeCallbacks(runnable);
        super.onDestroy();
    }
}
