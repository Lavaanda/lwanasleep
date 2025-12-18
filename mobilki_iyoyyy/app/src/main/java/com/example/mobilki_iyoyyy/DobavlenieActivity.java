package com.example.mobilki_iyoyyy;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DobavlenieActivity extends BaseActivity {
    ImageView imageView8;

    private EditText editTextMessage;
    private int topicId; // айди темы для добавления сообщения

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dobavlenie);

        editTextMessage = findViewById(R.id.editTextMessage);


        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        WindowInsetsControllerCompat controller =
                new WindowInsetsControllerCompat(getWindow(), getWindow().getDecorView());
        controller.hide(WindowInsetsCompat.Type.systemBars());
        controller.setSystemBarsBehavior(
                WindowInsetsControllerCompat.BEHAVIOR_DEFAULT
        );
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            return insets;
        });

        ImageView imageView8 = findViewById(R.id.imageView8);
        loadUserPhoto(findViewById(R.id.imageView8));
        imageView8.setOnClickListener(v ->
                startActivity(new Intent(DobavlenieActivity.this, MainActivity_profile.class))
        );

        int[] emptyTextIds = new int[] {};

        int[] imageIds = new int[] {
        };

        applyThemeGlobal(R.id.main, emptyTextIds, imageIds);

        // получаем topic_id
        topicId = getIntent().getIntExtra("topic_id", -1);

        ImageView sendBtn = findViewById(R.id.imageViewVxodButton);
        sendBtn.setOnClickListener(v -> sendMessage());

        // Переходы на разные активности
        ImageView imageView12 = findViewById(R.id.imageView12);
        imageView12.setOnClickListener(v ->
                startActivity(new Intent(DobavlenieActivity.this, MainActivityPalka.class))
        );

        ImageView imageView210 = findViewById(R.id.imageView210);
        imageView210.setOnClickListener(v ->
                startActivity(new Intent(DobavlenieActivity.this, RatingActivity.class))
        );

        ImageView imageView23 = findViewById(R.id.imageView23);
        imageView23.setOnClickListener(v ->
                startActivity(new Intent(DobavlenieActivity.this, FORYM_TOPICS.class))
        );

        ImageView imageView22 = findViewById(R.id.imageView22);
        imageView22.setOnClickListener(v ->
                startActivity(new Intent(DobavlenieActivity.this, MainActivity.class))
        );
    }

    private void sendMessage() {
        String content = editTextMessage.getText().toString().trim();

        if (content.isEmpty()) {
            Toast.makeText(this, "Введите сообщение", Toast.LENGTH_SHORT).show();
            return;
        }

        // 🔹 отправляем сообщение с topic_id на сервер
        Call<ResponseBody> call = RetrofitClient.getApiService().addForumMessage(content, topicId);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(DobavlenieActivity.this, "Сообщение отправлено!", Toast.LENGTH_SHORT).show();
                    finish(); // закрываем окно
                } else {
                    Toast.makeText(DobavlenieActivity.this, "Ошибка при отправке", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(DobavlenieActivity.this, "Сервер недоступен", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
