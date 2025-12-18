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

public class MainSecretKode extends BaseActivity {

    EditText editTextLogin;
    EditText editTextSecretKode;
    ImageView imageViewPerexod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secret_kode);

        int[] emptyTextIds = new int[] {};

        int[] imageIds = new int[] {};

        applyThemeGlobal(R.id.main, emptyTextIds, imageIds);

        editTextLogin = findViewById(R.id.textViewLogin);
        editTextSecretKode = findViewById(R.id.textViewSecretKode);
        imageViewPerexod = findViewById(R.id.imageViewPerexod);

        // ===== Кнопка "назад в настройки" =====
        imageViewPerexod.setOnClickListener(v -> {
            Intent intent = new Intent(MainSecretKode.this, Main_Nastroiki.class);
            startActivity(intent);
            finish(); // чтобы закрыть текущую Activity
        });

        // ===== Сохраняем секретный код =====
        ImageView saveButton = findViewById(R.id.imageViewVxodButton);
        saveButton.setOnClickListener(v -> {
            String loginInput = editTextLogin.getText().toString().trim();
            String secretKodeInput = editTextSecretKode.getText().toString().trim();

            if (loginInput.isEmpty()) {
                Toast.makeText(MainSecretKode.this, "Введите логин!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (secretKodeInput.isEmpty()) {
                Toast.makeText(MainSecretKode.this, "Введите секретный код!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Получаем текущий логин с сервера по userId/сессии
            RetrofitClient.getApiService().getUserInfoRow() // <-- новое имя метода
                    .enqueue(new Callback<UserInfoResponse>() {
                        @Override
                        public void onResponse(Call<UserInfoResponse> call, Response<UserInfoResponse> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                String currentLogin = response.body().getLogin();

                                // Чувствительное к регистру сравнение
                                if (!loginInput.equals(currentLogin)) {
                                    Toast.makeText(MainSecretKode.this, "Логин не совпадает с текущим пользователем!", Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                // Всё ок — отправляем секретный код на сервер
                                RetrofitClient.getApiService().setSecretKode(secretKodeInput)
                                        .enqueue(new Callback<Void>() {
                                            @Override
                                            public void onResponse(Call<Void> call, Response<Void> response) {
                                                if (response.isSuccessful()) {
                                                    Toast.makeText(MainSecretKode.this, "Секретный код добавлен!", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Toast.makeText(MainSecretKode.this, "Ошибка при добавлении кода!", Toast.LENGTH_SHORT).show();
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<Void> call, Throwable t) {
                                                Toast.makeText(MainSecretKode.this, "Ошибка сети: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });

                            } else {
                                Toast.makeText(MainSecretKode.this, "Не удалось получить данные пользователя", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<UserInfoResponse> call, Throwable t) {
                            Toast.makeText(MainSecretKode.this, "Ошибка сети: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }
}
