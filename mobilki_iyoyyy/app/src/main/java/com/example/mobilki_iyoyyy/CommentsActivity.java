package com.example.mobilki_iyoyyy;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.recyclerview.widget.ConcatAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentsActivity extends BaseActivity {

    TextView textLoginUser, textMessage;
    RecyclerView recyclerView;
    CommentsAdapter adapter;
    EditText editTextComment;
    ImageView sendButton, imageView8;

    int messageId;
    int topicId;

    //ДОБАВЛЕНО
    List<CommentModel> commentsList = new ArrayList<>();
    ConcatAdapter concatAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

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

        applyThemeGlobal(R.id.main, new int[]{}, new int[]{});

        // используем ПОЛЕ, а не локальную переменную
        recyclerView = findViewById(R.id.recyclerview);

        editTextComment = findViewById(R.id.editTextMessage);
        sendButton = findViewById(R.id.imageViewVxodButton);

        // -------- данные --------
        String login = getIntent().getStringExtra("login");
        String text = getIntent().getStringExtra("text");
        messageId = getIntent().getIntExtra("id", -1);
        topicId = getIntent().getIntExtra("topic_id", -1);

        if (messageId == -1) {
            Toast.makeText(this, "Ошибка: messageId не получен!", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        ImageView imageView8 = findViewById(R.id.imageView8);
        loadUserPhoto(findViewById(R.id.imageView8));

        imageView8.setOnClickListener(v ->
                startActivity(new Intent(CommentsActivity.this, MainActivity_profile.class))
        );

        // -------- HEADER (ОДНО СООБЩЕНИЕ) --------
        ChatMessage message = new ChatMessage(
                messageId,
                0,
                topicId,
                text,
                login
        );

        MessageHeaderAdapter headerAdapter =
                new MessageHeaderAdapter(message, topicId);

        // -------- КОММЕНТАРИИ --------
        adapter = new CommentsAdapter(commentsList);

        concatAdapter = new ConcatAdapter(headerAdapter, adapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(concatAdapter);

        loadCommentsFromServer();

        // -------- ОТПРАВКА --------
        sendButton.setOnClickListener(v -> {
            String commentText = editTextComment.getText().toString().trim();
            if (commentText.isEmpty()) {
                Toast.makeText(this, "Введите текст комментария", Toast.LENGTH_SHORT).show();
                return;
            }
            sendCommentToServer(commentText);
        });

        ImageView invisibleButton = findViewById(R.id.invisibleButton);
        invisibleButton.setOnClickListener(v -> {
            if (topicId == -1) {
                Toast.makeText(this, "Невозможно открыть чат — нет topicId!", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intent = new Intent(this, ObhiChatActivity.class);
            intent.putExtra("topic_id", topicId);
            startActivity(intent);
        });

        ImageView imageView12 = findViewById(R.id.imageView12);
        imageView12.setOnClickListener(v ->
                startActivity(new Intent(this, MainActivityPalka.class)));

        ImageView imageView210 = findViewById(R.id.imageView210);
        imageView210.setOnClickListener(v ->
                startActivity(new Intent(this, RatingActivity.class)));

        ImageView imageView23 = findViewById(R.id.imageView23);
        imageView23.setOnClickListener(v ->
                startActivity(new Intent(this, FORYM_TOPICS.class)));

        ImageView imageView22 = findViewById(R.id.imageView22);
        imageView22.setOnClickListener(v ->
                startActivity(new Intent(this, MainActivity.class)));
    }

    // ================== ЛОГИКА ЗАГРУЗКИ ==================
    private void loadCommentsFromServer() {
        ApiService api = RetrofitClient.getApiService();

        api.getForumComments(messageId).enqueue(new Callback<List<CommentModel>>() {
            @Override
            public void onResponse(Call<List<CommentModel>> call,
                                   Response<List<CommentModel>> response) {
                if (response.isSuccessful() && response.body() != null) {

                    // ❗ ВМЕСТО setAdapter()
                    commentsList.clear();
                    commentsList.addAll(response.body());
                    adapter.notifyDataSetChanged();

                } else {
                    Toast.makeText(CommentsActivity.this,
                            "Нет комментариев",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<CommentModel>> call, Throwable t) {
                Toast.makeText(CommentsActivity.this,
                        "Ошибка загрузки комментариев",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    // ================== ТВОЯ ЛОГИКА ОТПРАВКИ ==================
    private void sendCommentToServer(String commentText) {
        ApiService api = RetrofitClient.getApiService();

        api.addForumComment(commentText, messageId)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call,
                                           Response<Void> response) {
                        if (response.isSuccessful()) {
                            editTextComment.setText("");
                            loadCommentsFromServer();
                        } else {
                            Toast.makeText(CommentsActivity.this,
                                    "Ошибка при добавлении",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(CommentsActivity.this,
                                "Не удалось подключиться к серверу",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
