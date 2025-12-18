package com.example.mobilki_iyoyyy;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity_friend extends BaseActivity {

    private RecyclerView recyclerView;
    private FriendAdapter adapter;
    private final List<Friend> allFriends = new ArrayList<>();      // весь список с сервера
    private final List<Friend> displayedFriends = new ArrayList<>(); // текущий отображаемый список
    private EditText editTextSearch;
    private ImageView imageView8, imagePalka;

    private boolean isLoadedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);

        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new FriendAdapter();
        recyclerView.setAdapter(adapter);

        editTextSearch = findViewById(R.id.editTextMessage);
        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) { filterFriends(s.toString()); }
            @Override public void afterTextChanged(Editable s) {}
        });

        ImageView sortAlphabetically = findViewById(R.id.imageView41);
        ImageView sortReset = findViewById(R.id.imageView42);

        // Сортировка по алфавиту по логину (текущий отображаемый список)
        sortAlphabetically.setOnClickListener(v -> {
            List<Friend> sorted = new ArrayList<>(displayedFriends);
            Collections.sort(sorted, (f1, f2) -> f1.getLogin().compareToIgnoreCase(f2.getLogin()));
            adapter.setFriends(sorted);
        });

        imagePalka = findViewById(R.id.imageView12);


        // Сброс к исходному порядку (текущий фильтр остаётся)
        sortReset.setOnClickListener(v -> {
            adapter.setFriends(new ArrayList<>(displayedFriends));
        });
        adapter.setOnFriendClickListener(friend -> {
            Intent intent = new Intent(MainActivity_friend.this, MainActivity_profile.class);
            intent.putExtra("friend_id", friend.getId());
            startActivity(intent);
        });

        imagePalka.setOnClickListener(v ->
                startActivity(new Intent(this, MainActivityPalka.class)));
        findViewById(R.id.imageView210).setOnClickListener(v ->
                startActivity(new Intent(this, RatingActivity.class)));
        findViewById(R.id.imageView23).setOnClickListener(v ->
                startActivity(new Intent(this, FORYM_TOPICS.class)));
        findViewById(R.id.imageView22).setOnClickListener(v ->
                startActivity(new Intent(this, MainActivity.class)));

        ImageView imageView8 = findViewById(R.id.imageView8);

        UserPhotoLoader.loadInto(imageView8, this);

        imageView8.setOnClickListener(v ->
                startActivity(new Intent(MainActivity_friend.this, MainActivity_profile.class))
        );
        loadFriendsFromServer();
    }

    private void loadFriendsFromServer() {
        if (isLoadedOnce) return;
        isLoadedOnce = true;

        RetrofitClient.getApiService().getFriends().enqueue(new Callback<List<Friend>>() {
            @Override
            public void onResponse(Call<List<Friend>> call, Response<List<Friend>> response) {
                if (!response.isSuccessful() || response.body() == null) return;

                allFriends.clear();
                allFriends.addAll(response.body());

                // изначально отображаем весь список
                displayedFriends.clear();
                displayedFriends.addAll(allFriends);

                adapter.setFriends(new ArrayList<>(displayedFriends));
            }

            @Override
            public void onFailure(Call<List<Friend>> call, Throwable t) {
                isLoadedOnce = false;
            }
        });
    }

    private void filterFriends(String query) {
        query = query.trim();
        displayedFriends.clear();

        if (query.isEmpty()) {
            displayedFriends.addAll(allFriends);
        } else if (query.matches("\\d+")) { // поиск по ID на сервере
            RetrofitClient.getApiService()
                    .searchUsers(query)
                    .enqueue(new Callback<List<Friend>>() {
                        @Override
                        public void onResponse(Call<List<Friend>> call, Response<List<Friend>> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                int myId = Integer.parseInt(
                                        getSharedPreferences("user", MODE_PRIVATE)
                                                .getString("id_user", "-1")
                                );

                                for (Friend f : response.body()) {
                                    if (f.getId() != myId) {
                                        displayedFriends.add(f);
                                    }
                                }

                                adapter.setFriends(new ArrayList<>(displayedFriends));
                            }
                        }

                        @Override
                        public void onFailure(Call<List<Friend>> call, Throwable t) {}
                    });
            return;
        } else { // поиск по логину среди друзей
            for (Friend f : allFriends) {
                if (f.getLogin().toLowerCase().contains(query.toLowerCase())) {
                    displayedFriends.add(f);
                }
            }
        }

        adapter.setFriends(new ArrayList<>(displayedFriends));
    }


    private void setupSystemUi() {
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
    }

//    private void loadFriendsFromServer() {
//        if (isLoadedOnce) return;
//        isLoadedOnce = true;
//
//        RetrofitClient.getApiService().getFriends().enqueue(new Callback<List<Friend>>() {
//            @Override
//            public void onResponse(Call<List<Friend>> call, Response<List<Friend>> response) {
//                if (!response.isSuccessful() || response.body() == null) return;
//                allFriends.clear();
//                allFriends.addAll(response.body());
//                adapter.setFriends(new ArrayList<>(allFriends));
//            }
//            @Override
//            public void onFailure(Call<List<Friend>> call, Throwable t) { isLoadedOnce = false; }
//        });
//    }
//
//    private void filterFriends(String query) {
//        query = query.trim();
//
//        if (query.isEmpty()) {
//            adapter.setFriends(new ArrayList<>(allFriends));
//            return;
//        }
//
//        // если цифры — ищем на сервере
//        if (query.matches("\\d+")) {
//            RetrofitClient.getApiService()
//                    .searchUsers(query)
//                    .enqueue(new Callback<List<Friend>>() {
//                        @Override
//                        public void onResponse(Call<List<Friend>> call, Response<List<Friend>> response) {
//                            if (response.isSuccessful() && response.body() != null) {
//
//                                // мой айди, выкидываем меня из поиска
//                                int myId = Integer.parseInt(
//                                        getSharedPreferences("user", MODE_PRIVATE)
//                                                .getString("id_user", "-1")
//                                );
//
//                                List<Friend> filtered = new ArrayList<>();
//                                for (Friend f : response.body()) {
//                                    if (f.getId() != myId) {
//                                        filtered.add(f);
//                                    }
//                                }
//
//                                adapter.setFriends(filtered);
//                            }
//                        }
//
//                        @Override
//                        public void onFailure(Call<List<Friend>> call, Throwable t) {
//                            Log.e("SEARCH", "Ошибка поиска", t);
//                        }
//                    });
//            return;
//        }
//
//
//        // если буквы (с цифрами) — по друзьям
//        List<Friend> filtered = new ArrayList<>();
//        for (Friend f : allFriends) {
//            if (f.getLogin().toLowerCase().contains(query.toLowerCase())) {
//                filtered.add(f);
//            }
//        }
//        adapter.setFriends(filtered);
//    }


    private void setupNavigation() {
        findViewById(R.id.imageView12).setOnClickListener(v -> startActivitySingleTop(MainActivityPalka.class));
        findViewById(R.id.imageView210).setOnClickListener(v -> startActivitySingleTop(RatingActivity.class));
        findViewById(R.id.imageView23).setOnClickListener(v -> startActivitySingleTop(FORYM_TOPICS.class));
        findViewById(R.id.imageView22).setOnClickListener(v -> startActivitySingleTop(MainActivity.class));
    }

    private void startActivitySingleTop(Class<?> cls) {
        Intent intent = new Intent(this, cls);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }
}
