package com.example.mobilki_iyoyyy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Main_Nastroiki extends AppCompatActivity {

    ImageView imageViewSmenaParol;
    TextView textViewVxod3;

    ImageView imageVixodIsPril;
    TextView textViewVxod1;

    TextView textViewVxod2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nasrtoiki);

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

        ImageView imageView210 = findViewById(R.id.imageView210);
        imageView210.setOnClickListener(v ->
                startActivity(new Intent(Main_Nastroiki.this, RatingActivity.class))
        );

        ImageView imageView23 = findViewById(R.id.imageView23);
        imageView23.setOnClickListener(v ->
                startActivity(new Intent(Main_Nastroiki.this, FORYM_TOPICS.class))
        );

        ImageView imageView22 = findViewById(R.id.imageView22);
        imageView22.setOnClickListener(v ->
                startActivity(new Intent(Main_Nastroiki.this, MainActivity.class))
        );

        imageViewSmenaParol = findViewById(R.id.imageViewSmenaParol);
        textViewVxod3 = findViewById(R.id.textViewVxod3);

        imageVixodIsPril = findViewById(R.id.imageVixodIsPril);
        textViewVxod1 = findViewById(R.id.textViewVxod1);

        textViewVxod2 = findViewById(R.id.textViewVxod2);

        // ===== Переход "Забыл пароль" (НЕ ТРОГАЕМ) =====
        View.OnClickListener openForgotPassword = v -> {
            Intent intent = new Intent(Main_Nastroiki.this, MainZabilParol.class);
            startActivity(intent);
        };
        imageViewSmenaParol.setOnClickListener(openForgotPassword);
        textViewVxod3.setOnClickListener(openForgotPassword);

        // ===== Выход из аккаунта (ЛОГИКА ТА ЖЕ) =====
        View.OnClickListener logoutClickListener = v -> handleLogoutRequest();
        imageVixodIsPril.setOnClickListener(logoutClickListener);
        textViewVxod1.setOnClickListener(logoutClickListener);

        // ===== Переход на экран Секретного кода (НЕ ТРОГАЕМ) =====
        textViewVxod2.setOnClickListener(v -> {
            Intent intent = new Intent(Main_Nastroiki.this, MainSecretKode.class);
            startActivity(intent);
        });
    }

    // ==========================================================
    // МЕТОД 1: Обработка запроса Logout (НЕ МЕНЯЕМ)
    // ==========================================================
    private void handleLogoutRequest() {
        Toast.makeText(this, "Выход из аккаунта...", Toast.LENGTH_SHORT).show();

        RetrofitClient.getApiService().logout()
                .enqueue(new Callback<LogoutResponse>() {

                    @Override
                    public void onResponse(Call<LogoutResponse> call,
                                           Response<LogoutResponse> response) {
                        performLocalLogout();
                    }

                    @Override
                    public void onFailure(Call<LogoutResponse> call, Throwable t) {
                        Toast.makeText(
                                Main_Nastroiki.this,
                                "Ошибка сети. Выход локально.",
                                Toast.LENGTH_LONG
                        ).show();
                        performLocalLogout();
                    }
                });
    }

    // ==========================================================
    // МЕТОД 2: Очистка локальных данных (ФИКС ФОТО)
    // ==========================================================
    private void performLocalLogout() {

        // 🔥 ГЛАВНЫЙ ФИКС — чистим фото
        UserCache.profilePhoto = null;

        // остальное — как у тебя и было
        if (UserCache.userId != null) {
            UserCache.userId = null;
        }
        if (UserCache.currentLogin != null) {
            UserCache.currentLogin = null;
        }

        Intent intent = new Intent(Main_Nastroiki.this, MainActivity2.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
