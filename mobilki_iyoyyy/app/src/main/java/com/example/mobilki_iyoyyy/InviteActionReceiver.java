package com.example.mobilki_iyoyyy;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InviteActionReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        int inviteId = intent.getIntExtra("invite_id", -1);
        String action = intent.getStringExtra("action");

        Log.d("InviteReceiver", "Отправка invite_id=" + inviteId + ", action=" + action);

        if (inviteId == -1 || action == null) {
            Log.e("InviteReceiver", "Неверные данные приглашения: inviteId=" + inviteId + ", action=" + action);
            Toast.makeText(context, "Неверные данные приглашения", Toast.LENGTH_SHORT).show();
            return;
        }

        ApiService api = RetrofitClient.getApiService();

        Map<String, Object> data = new HashMap<>();
        data.put("invite_id", inviteId); // совпадает с тем, что сервер ожидает
        data.put("answer", action.equals("accept") ? "yes" : "no");

        Log.d("InviteReceiver", "Отправка данных: " + data.toString());

        api.inviteAnswer(data).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d("InviteReceiver", "code=" + response.code());
                try {
                    if (response.errorBody() != null) {
                        Log.d("InviteReceiver", "errorBody: " + response.errorBody().string());
                    }
                } catch (Exception e) { e.printStackTrace(); }

                if (response.isSuccessful()) {
                    Toast.makeText(context,
                            action.equals("accept") ? "Приглашение принято" : "Приглашение отклонено",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Ошибка сервера: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("InviteReceiver", "Ошибка сети", t);
                Toast.makeText(context, "Ошибка сети: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
