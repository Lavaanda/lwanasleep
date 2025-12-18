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

public class MainActivity3 extends BaseActivity {

    EditText loginEditText, passwordEditText;
    ImageView registerButton, goToLoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);
        int[] emptyTextIds = new int[] {R.id.textViewHeader};

        int[] imageIds = new int[] {
                R.id.imageViewFon};

        applyThemeGlobal(R.id.main, emptyTextIds, imageIds);

//        applyThemeGlobalFull(R.id.main,
//                R.id.textViewHeader,
//                R.id.imageViewFon);

        loginEditText = findViewById(R.id.textViewVxodLineEdit);
        passwordEditText = findViewById(R.id.textViewParolLineEdit);
        registerButton = findViewById(R.id.imageViewVxodButton);
        goToLoginButton = findViewById(R.id.imageViewPerexod);

        registerButton.setOnClickListener(v -> {
            String login = loginEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            if (login.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Заполните все поля!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!password.matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{6,}$")) {
                Toast.makeText(this,
                        "Пароль должен содержать английские буквы, цифры и быть не короче 6 символов",
                        Toast.LENGTH_LONG).show();
                return;
            }
            
            RetrofitClient.getApiService().register(new RequestRegister(login, password, login))
                    .enqueue(new Callback<ResponseUser>() {
                        @Override
                        public void onResponse(Call<ResponseUser> call, Response<ResponseUser> response) {
                            if (response.isSuccessful() && response.body() != null && response.body().user_id != 0) {
                                Toast.makeText(MainActivity3.this, "Регистрация успешна!", Toast.LENGTH_SHORT).show();
                                loginUser(login, password);
                            } else {
                                Toast.makeText(MainActivity3.this, "Ошибка регистрации", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseUser> call, Throwable t) {
                            Toast.makeText(MainActivity3.this, "Ошибка сети: " + t.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        });

        goToLoginButton.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity3.this, MainActivity2.class));
            finish();
        });
    }

    private void loginUser(String login, String password) {
        RetrofitClient.getApiService().login(new RequestLogin(login, password))
                .enqueue(new Callback<ResponseUser>() {
                    @Override
                    public void onResponse(Call<ResponseUser> call, Response<ResponseUser> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().user_id != 0) {
                            Toast.makeText(MainActivity3.this, "Добро пожаловать!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(MainActivity3.this, MainActivity.class));
                            finish();
                        } else {
                            Toast.makeText(MainActivity3.this, "Ошибка авторизации после регистрации", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseUser> call, Throwable t) {
                        Toast.makeText(MainActivity3.this, "Ошибка сети при входе: " + t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}
