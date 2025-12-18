package com.example.mobilki_iyoyyy;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InviteDecisionActivity extends AppCompatActivity {

    ApiService api;
    String inviteId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_decision);

        api = RetrofitClient.getApiService();

        inviteId = getIntent().getStringExtra("invite_id");

        Button btnYes = findViewById(R.id.btnYes);
        Button btnNo = findViewById(R.id.btnNo);

        btnYes.setOnClickListener(v -> sendAnswer("yes"));
        btnNo.setOnClickListener(v -> sendAnswer("no"));
    }

    private void sendAnswer(String answer) {
        Map<String, String> body = new HashMap<>();
        body.put("invite_id", inviteId);
        body.put("answer", answer);

        api.sendInviteAnswer(body).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Toast.makeText(InviteDecisionActivity.this,
                        response.isSuccessful() ? "Отправлено" : "Ошибка",
                        Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(InviteDecisionActivity.this,
                        "Ошибка сети", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}
