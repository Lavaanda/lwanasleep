package com.example.mobilki_iyoyyy;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FORYM_TOPICS extends BaseActivity {

    private RecyclerView recyclerView;
    private ForymTopicsAdapter adapter;
    ImageView imageView8;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tema_forym); // layout с RecyclerView


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

        recyclerView = findViewById(R.id.recyclerViewForumTopics);

        adapter = new ForymTopicsAdapter(new ArrayList<>(), topic -> {
            Intent intent = new Intent(FORYM_TOPICS.this, ObhiChatActivity.class);

            // Передаём айди и название темы
            intent.putExtra("topic_id", topic.id_forym);
            intent.putExtra("topic_name", topic.topic);

            startActivity(intent);
        });

        ImageView imageView8 = findViewById(R.id.imageView8);
        loadUserPhoto(findViewById(R.id.imageView8));
        imageView8.setOnClickListener(v ->
                startActivity(new Intent(FORYM_TOPICS.this, MainActivity_profile.class))
        );

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Переходы на разные активности
        ImageView imageView12 = findViewById(R.id.imageView12);
        imageView12.setOnClickListener(v ->
                startActivity(new Intent(FORYM_TOPICS.this, MainActivityPalka.class))
        );

        ImageView imageView210 = findViewById(R.id.imageView210);
        imageView210.setOnClickListener(v ->
                startActivity(new Intent(FORYM_TOPICS.this, RatingActivity.class))
        );

        ImageView imageView23 = findViewById(R.id.imageView23);
        imageView23.setOnClickListener(v ->
                startActivity(new Intent(FORYM_TOPICS.this, FORYM_TOPICS.class))
        );

        ImageView imageView22 = findViewById(R.id.imageView22);
        imageView22.setOnClickListener(v ->
                startActivity(new Intent(FORYM_TOPICS.this, MainActivity.class))
        );

        loadTopics(); // загружаем темы с сервера

        int[] emptyTextIds = new int[] {};

        int[] imageIds = new int[] {
        };

        applyThemeGlobal(R.id.main, emptyTextIds, imageIds);
    }

    private void loadTopics() {
        // используем твой RetrofitClient с cookie jar
        RetrofitClient.getApiService().getForumTopics().enqueue(new Callback<ForumTopicsResponse>() {
            @Override
            public void onResponse(Call<ForumTopicsResponse> call, Response<ForumTopicsResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    adapter.setData(response.body().topics); // обновляем адаптер
                } else {
                    Toast.makeText(FORYM_TOPICS.this, "Ошибка загрузки тем", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ForumTopicsResponse> call, Throwable t) {
                Toast.makeText(FORYM_TOPICS.this, "Не удалось подключиться к серверу", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
