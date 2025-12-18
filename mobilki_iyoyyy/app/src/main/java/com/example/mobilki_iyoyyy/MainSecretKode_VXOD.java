package com.example.mobilki_iyoyyy;


import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainSecretKode_VXOD extends BaseActivity {

    EditText editTextLogin;
    EditText editTextSecretKode;
    ImageView buttonEnter, imageViewPerexod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secret_kode_vxod);

        int[] emptyTextIds = new int[] {R.id.textViewHeader};

        int[] imageIds = new int[] {};

        applyThemeGlobal(R.id.main, emptyTextIds, imageIds);

        editTextLogin = findViewById(R.id.textViewLogin);
        editTextSecretKode = findViewById(R.id.textViewSecretKode);
        buttonEnter = findViewById(R.id.imageViewVxodButton);

        imageViewPerexod = findViewById(R.id.imageViewPerexod);

        imageViewPerexod.setOnClickListener(v -> {
            Intent intent = new Intent(MainSecretKode_VXOD.this, MainActivity2.class);
            startActivity(intent);
            finish(); // закрыть текущую Activity
        });

        // Кнопка входа по секретному коду
        buttonEnter.setOnClickListener(v -> {
            String login = editTextLogin.getText().toString().trim();
            String secret = editTextSecretKode.getText().toString().trim();

            if (login.isEmpty()) {
                Toast.makeText(this, "Введите логин!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (secret.isEmpty()) {
                Toast.makeText(this, "Введите секретный код!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Запрос на вход по секретному коду
            RetrofitClient.getApiService()
                    .loginBySecretKode(new SecretLoginRequest(login, secret))
                    .enqueue(new Callback<UserInfoResponse>() {
                        @Override
                        public void onResponse(Call<UserInfoResponse> call, Response<UserInfoResponse> response) {
                            if (response.isSuccessful() && response.body() != null) {

                                Toast.makeText(MainSecretKode_VXOD.this, "Вход выполнен!", Toast.LENGTH_SHORT).show();

                                // Переход в главное меню
                                Intent intent = new Intent(MainSecretKode_VXOD.this, MainActivity.class);
                                startActivity(intent);
                                finish();

                            } else {
                                Toast.makeText(MainSecretKode_VXOD.this, "Неверный логин или секретный код!", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<UserInfoResponse> call, Throwable t) {
                            Toast.makeText(MainSecretKode_VXOD.this, "Ошибка сети: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }
}
