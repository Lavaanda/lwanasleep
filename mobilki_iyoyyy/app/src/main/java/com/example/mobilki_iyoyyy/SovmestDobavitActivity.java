package com.example.mobilki_iyoyyy;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SovmestDobavitActivity extends BaseActivity {

    EditText editTextId;
    ImageView btnAdd, imageView8;

    RecyclerView friendsRecycler;
    FriendAdapter adapter;

    ApiService api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sovmest_dobavit);

        int[] emptyTextIds = new int[] {};

        int[] imageIds = new int[] {};

        applyThemeGlobal(R.id.main, emptyTextIds, imageIds);


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

        editTextId = findViewById(R.id.editTextMessage);
        btnAdd = findViewById(R.id.btnAddFriendToProject);
        friendsRecycler = findViewById(R.id.friendsRecycler);

        api = RetrofitClient.getApiService();

        // Настройка RecyclerView
        friendsRecycler.setLayoutManager(new LinearLayoutManager(this));
        adapter = new FriendAdapter();
        friendsRecycler.setAdapter(adapter);

        // Загружаем друзей
        loadFriends();

        // Показ списка при клике на EditText
        editTextId.setOnClickListener(v -> friendsRecycler.setVisibility(View.VISIBLE));

//        // Запуск сервиса для проверки приглашений
//        btnAdd.setOnClickListener(v -> {
//            Intent intent = new Intent(this, InviteCheckerService.class);
//            startService(intent);
//        });

        // Добавление друга в проект
        btnAdd.setOnClickListener(v -> addFriendToProject());

        // Клик по другу → подставляем ID + скрываем список
        adapter.setOnFriendClickListener(friend -> {
            editTextId.setText(String.valueOf(friend.getId()));
            friendsRecycler.setVisibility(View.GONE);
        });

        // Переходы на разные активности
        ImageView imageView12 = findViewById(R.id.imageView12);
        imageView12.setOnClickListener(v ->
                startActivity(new Intent(SovmestDobavitActivity.this, MainActivityPalka.class))
        );

        ImageView imageView210 = findViewById(R.id.imageView210);
        imageView210.setOnClickListener(v ->
                startActivity(new Intent(SovmestDobavitActivity.this, RatingActivity.class))
        );

        ImageView imageView23 = findViewById(R.id.imageView23);
        imageView23.setOnClickListener(v ->
                startActivity(new Intent(SovmestDobavitActivity.this, FORYM_TOPICS.class))
        );

        ImageView imageView22 = findViewById(R.id.imageView22);
        imageView22.setOnClickListener(v ->
                startActivity(new Intent(SovmestDobavitActivity.this, MainActivity.class))
        );

        ImageView imageView8 = findViewById(R.id.imageView8);
        loadUserPhoto(findViewById(R.id.imageView8));
        imageView8.setOnClickListener(v ->
                startActivity(new Intent(SovmestDobavitActivity.this, MainActivity_profile.class))
        );
    }

    private void loadFriends() {
        api.getFriends().enqueue(new Callback<List<Friend>>() {
            @Override
            public void onResponse(Call<List<Friend>> call, Response<List<Friend>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    adapter.setFriends(response.body());
                } else {
                    Toast.makeText(SovmestDobavitActivity.this, "Не удалось загрузить друзей", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Friend>> call, Throwable t) {
                Toast.makeText(SovmestDobavitActivity.this, "Ошибка соединения: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addFriendToProject() {
        String idText = editTextId.getText().toString().trim();

        if (idText.isEmpty()) {
            Toast.makeText(this, "Введите ID друга", Toast.LENGTH_SHORT).show();
            return;
        }

        int friendId;
        try {
            friendId = Integer.parseInt(idText);
        } catch (Exception e) {
            Toast.makeText(this, "ID должен быть числом", Toast.LENGTH_SHORT).show();
            return;
        }

        int projectId = 1; // фиксированный id проекта

        Map<String, String> form = new HashMap<>();
        form.put("friend_id", String.valueOf(friendId));
        form.put("project_id", String.valueOf(projectId));

        api.addFriendToProject(form).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(SovmestDobavitActivity.this, "Друг успешно добавлен в проект", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SovmestDobavitActivity.this, "Ошибка: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(SovmestDobavitActivity.this, "Ошибка соединения: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUserRole(String role) {
        Map<String, String> data = new HashMap<>();
        data.put("role", role);

        api.updateUserRole(data).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(SovmestDobavitActivity.this, "Роль обновлена", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SovmestDobavitActivity.this, "Ошибка сервера: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(SovmestDobavitActivity.this, "Ошибка соединения: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // --- Пример использования уведомлений с твоими иконками
    private void showNotificationExample(InviteModel invite) {

        Intent acceptIntent = new Intent(this, InviteActionReceiver.class);
        acceptIntent.putExtra("invite_id", invite.getId());
        acceptIntent.putExtra("action", "accept");

        PendingIntent acceptPending = PendingIntent.getBroadcast(
                this,
                invite.getId(),
                acceptIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        Intent declineIntent = new Intent(this, InviteActionReceiver.class);
        declineIntent.putExtra("invite_id", invite.getId());
        declineIntent.putExtra("action", "decline");

        PendingIntent declinePending = PendingIntent.getBroadcast(
                this,
                invite.getId() + 1000,
                declineIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        NotificationUtils.showInviteNotification(
                this,
                invite,
                acceptPending,
                declinePending
        );
    }

}
