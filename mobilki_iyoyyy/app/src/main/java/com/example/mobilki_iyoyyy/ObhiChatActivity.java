package com.example.mobilki_iyoyyy;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ObhiChatActivity extends BaseActivity {

    private RecyclerView recyclerView;
    private ChatAdapter adapter;
    private int topicId;
    ImageView imageView8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_obhi_chat);

        int[] emptyTextIds = new int[] {};

        int[] imageIds = new int[] {};

        applyThemeGlobal(R.id.main, emptyTextIds, imageIds);


        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        WindowInsetsControllerCompat controller =
                new WindowInsetsControllerCompat(getWindow(), getWindow().getDecorView());
        controller.hide(WindowInsetsCompat.Type.systemBars());
        controller.setSystemBarsBehavior(WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        topicId = getIntent().getIntExtra("topic_id", -1);
        adapter = new ChatAdapter(new ArrayList<ChatMessage>(), topicId);

        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Переходы на разные активности
        ImageView imageView12 = findViewById(R.id.imageView12);
        imageView12.setOnClickListener(v ->
                startActivity(new Intent(ObhiChatActivity.this, MainActivityPalka.class))
        );

        ImageView imageView210 = findViewById(R.id.imageView210);
        imageView210.setOnClickListener(v ->
                startActivity(new Intent(ObhiChatActivity.this, RatingActivity.class))
        );

        ImageView imageView23 = findViewById(R.id.imageView23);
        imageView23.setOnClickListener(v ->
                startActivity(new Intent(ObhiChatActivity.this, FORYM_TOPICS.class))
        );

        ImageView imageView22 = findViewById(R.id.imageView22);
        imageView22.setOnClickListener(v ->
                startActivity(new Intent(ObhiChatActivity.this, MainActivity.class))
        );

        loadMessages();
        ImageView imageView8 = findViewById(R.id.imageView8);
        loadUserPhoto(findViewById(R.id.imageView8));
        imageView8.setOnClickListener(v ->
                startActivity(new Intent(ObhiChatActivity.this, MainActivity_profile.class))
        );
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadMessages();
    }

    private void loadMessages() {
        RetrofitClient.getApiService().getForumMessages(topicId)
                .enqueue(new Callback<List<ChatMessage>>() {
                    @Override
                    public void onResponse(Call<List<ChatMessage>> call, Response<List<ChatMessage>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            adapter.setMessages(response.body()); // 🔹 обновляем RecyclerView
                        } else {
                            Toast.makeText(ObhiChatActivity.this, "Ошибка загрузки", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<ChatMessage>> call, Throwable t) {
                        Toast.makeText(ObhiChatActivity.this, "Ошибка соединения", Toast.LENGTH_SHORT).show();
                    }
                });

        ImageView plus = findViewById(R.id.imageView57);
        plus.setOnClickListener(v -> {
            //DobavlenieActivity с topic_id
            Intent intent = new Intent(ObhiChatActivity.this, DobavlenieActivity.class);
            intent.putExtra("topic_id", topicId);
            startActivity(intent);
        });
    }
}
