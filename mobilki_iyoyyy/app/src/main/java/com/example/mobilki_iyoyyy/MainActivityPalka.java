package com.example.mobilki_iyoyyy;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import com.bumptech.glide.Glide;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivityPalka extends AppCompatActivity {

    private ImageView profileImage;
    private boolean isLaunching = false; // флаг для предотвращения повторного запуска MainActivity_friend

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_palka);


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


        profileImage = findViewById(R.id.imageViewProfile);

        loadUserPhoto();

        // ===== Загружаем фото пользователя =====
        if (UserCache.profilePhoto != null) {
            profileImage.setImageBitmap(UserCache.profilePhoto);
        }

        // Всегда делаем запрос к серверу для обновления фото
        RetrofitClient.getApiService().getUserPhoto().enqueue(new Callback<UserPhotoResponse>() {
            @Override
            public void onResponse(Call<UserPhotoResponse> call, Response<UserPhotoResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String base64 = response.body().photo;
                    if (base64 != null && !base64.isEmpty()) {
                        byte[] decodedBytes = Base64.decode(base64, Base64.DEFAULT);
                        Bitmap bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
                        profileImage.setImageBitmap(bitmap);
                        UserCache.profilePhoto = bitmap; // сохраняем в кэш
                    }
//                } else {
//                    Toast.makeText(MainActivityPalka.this, "Фото отсутствует", Toast.LENGTH_SHORT).show();
//                }
                }
            }

            @Override
            public void onFailure(Call<UserPhotoResponse> call, Throwable t) {
                // если фотка из кеша, то ничего не показываем
                if (profileImage.getDrawable() == null) {
                    Toast.makeText(MainActivityPalka.this,
                            "Ошибка сети: фото не удалось обновить",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });


        // ===== Переходы по кнопкам =====

        // Профиль
        TextView textViewVxod3 = findViewById(R.id.textViewVxod3);
        textViewVxod3.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivityPalka.this, MainActivity_profile.class);
            startActivity(intent);
        });

        TextView theme = findViewById(R.id.theme);
        theme.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivityPalka.this, MainTHEME.class);
            startActivity(intent);
        });


        ImageView imageView210 = findViewById(R.id.imageView210);
        imageView210.setOnClickListener(v ->
                startActivity(new Intent(MainActivityPalka.this, RatingActivity.class))
        );

        ImageView imageView23 = findViewById(R.id.imageView23);
        imageView23.setOnClickListener(v ->
                startActivity(new Intent(MainActivityPalka.this, FORYM_TOPICS.class))
        );

        ImageView imageView22 = findViewById(R.id.imageView22);
        imageView22.setOnClickListener(v ->
                startActivity(new Intent(MainActivityPalka.this, MainActivity.class))
        );

        // Друг (с флагом isLaunching)
        TextView textViewVxod2 = findViewById(R.id.textViewVxod2);
        textViewVxod2.setOnClickListener(v -> {
            if (isLaunching) return; // предотвращаем повторный старт
            isLaunching = true;

            Intent intent = new Intent(MainActivityPalka.this, MainActivity_friend.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

            // Сбрасываем флаг через 500мс
            v.postDelayed(() -> isLaunching = false, 500);
        });

        // Настройки
        TextView textViewVxod1 = findViewById(R.id.textViewVxod1);
        textViewVxod1.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivityPalka.this, Main_Nastroiki.class);
            startActivity(intent);
        });
    }

    private void loadUserPhoto() {
        // 1) bitmap в кэше
        if (UserCache.profilePhoto != null) {
            profileImage.setImageBitmap(UserCache.profilePhoto);
        }
        // 2) URL от Google
        else if (UserCache.userPhotoUrl != null && !UserCache.userPhotoUrl.trim().isEmpty()) {
            Glide.with(this)
                    .load(UserCache.userPhotoUrl)
                    .placeholder(R.drawable.friend_cat)  // или kryg_zenhina, как хочешь
                    .error(R.drawable.friend_cat)
                    .into(profileImage);
        }
        // 3) сервер (base64)
        else {
            RetrofitClient.getApiService().getUserPhoto().enqueue(new Callback<UserPhotoResponse>() {
                @Override
                public void onResponse(Call<UserPhotoResponse> call, Response<UserPhotoResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        String base64 = response.body().photo;
                        if (base64 != null && !base64.isEmpty()) {
                            try {
                                byte[] decodedBytes = Base64.decode(base64, Base64.DEFAULT);
                                Bitmap bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
                                profileImage.setImageBitmap(bitmap);
                                UserCache.profilePhoto = bitmap;
                            } catch (Exception e) {
                                e.printStackTrace();
                                profileImage.setImageResource(R.drawable.friend_cat);
                            }
                        } else {
                            profileImage.setImageResource(R.drawable.friend_cat);
                        }
                    } else {
                        profileImage.setImageResource(R.drawable.friend_cat);
                    }
                }

                @Override
                public void onFailure(Call<UserPhotoResponse> call, Throwable t) {
                    if (profileImage.getDrawable() == null) {
                        Toast.makeText(MainActivityPalka.this,
                                "Ошибка сети: фото не удалось обновить",
                                Toast.LENGTH_SHORT).show();
                    }
                    profileImage.setImageResource(R.drawable.friend_cat);
                }
            });
        }
    }
}
