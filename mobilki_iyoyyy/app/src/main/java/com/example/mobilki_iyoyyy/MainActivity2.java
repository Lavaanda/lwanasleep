package com.example.mobilki_iyoyyy;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import java.util.HashMap;
import java.util.Map;


public class MainActivity2 extends BaseActivity {

    EditText loginEditText, passwordEditText;
    ImageView loginButton, goToRegisterButton;

    private ImageView imageGoogle;
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in);

//        int[] emptyTextIds = new int[] {R.id.textViewHeader};
//
//        int[] imageIds = new int[] {
//                R.id.imageViewFon};
//
//        applyThemeGlobal(R.id.main, emptyTextIds, imageIds);


//        applyThemeGlobalFull(R.id.main,
//                R.id.textViewHeader,
//                R.id.imageViewFon);


        imageGoogle = findViewById(R.id.imageGoogle);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("608034227462-qr12iiormi4ttehg1dkvmbsanc568qs6.apps.googleusercontent.com")
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        imageGoogle.setOnClickListener(v -> {
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);
        });

        loginEditText = findViewById(R.id.textViewVxodLineEdit);
        passwordEditText = findViewById(R.id.textViewParolLineEdit);
        loginButton = findViewById(R.id.imageViewVxodButton);
        TextView goToRegisterButton = findViewById(R.id.butReg);

        // Вход по логину и паролю
        loginButton.setOnClickListener(v -> {
            String login = loginEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            if (login.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Заполните все поля!", Toast.LENGTH_SHORT).show();
                return;
            }

            RetrofitClient.getApiService().login(new RequestLogin(login, password))
                    .enqueue(new Callback<ResponseUser>() {
                        @Override
                        public void onResponse(Call<ResponseUser> call, Response<ResponseUser> response) {
                            if (response.isSuccessful() && response.body() != null && response.body().user_id != 0) {
                                // Сохраняем сессионные данные
                                UserCache.userId = String.valueOf(response.body().user_id);
                                UserCache.currentLogin = response.body().login;

                                Toast.makeText(MainActivity2.this, "Авторизация успешна!", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(MainActivity2.this, MainActivity.class));
                                finish();
                            } else {
                                Toast.makeText(MainActivity2.this, "Неверный логин или пароль", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseUser> call, Throwable t) {
                            Toast.makeText(MainActivity2.this, "Ошибка сети: " + t.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        });

        // Переход на регистрацию
        goToRegisterButton.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity2.this, MainActivity3.class));
            finish();
        });

        // Вход в секретный раздел
        TextView textViewVxod = findViewById(R.id.textViewVxodв);
        textViewVxod.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity2.this, MainSecretKode_VXOD.class);
            startActivity(intent);
        });

        // Вход как гость
        TextView ghostButton = findViewById(R.id.ghostUser);

        ghostButton.setOnClickListener(v -> {
            RetrofitClient.getApiService().loginGuest()
                    .enqueue(new Callback<GuestResponse>() {
                        @Override
                        public void onResponse(Call<GuestResponse> call, Response<GuestResponse> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                UserCache.userId = String.valueOf(response.body().user_id);
                                UserCache.currentLogin = response.body().login;

                                Toast.makeText(MainActivity2.this, "Вход как гость!", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(MainActivity2.this, MainActivity.class));
                                finish();
                            } else {
                                Toast.makeText(MainActivity2.this, "Ошибка входа как гость", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<GuestResponse> call, Throwable t) {
                            Toast.makeText(MainActivity2.this, "Ошибка сети: " + t.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });

        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode != RC_SIGN_IN) return;

        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
        try {
            GoogleSignInAccount account = task.getResult(ApiException.class);
            if (account == null) {
                Log.w("MainActivity2", "GoogleSignInAccount is null");
                Toast.makeText(this, "Не удалось получить аккаунт Google", Toast.LENGTH_SHORT).show();
                return;
            }

            String idToken = account.getIdToken();
            if (idToken == null || idToken.isEmpty()) {
                Log.w("MainActivity2", "idToken is null/empty");
                Toast.makeText(this, "Не получили idToken от Google", Toast.LENGTH_SHORT).show();
                return;
            }

            RetrofitClient.getApiService().loginGoogle(new GoogleLoginRequest(idToken))
                    .enqueue(new Callback<ResponseGoogleLogin>() {
                        @Override
                        public void onResponse(Call<ResponseGoogleLogin> call, Response<ResponseGoogleLogin> response) {
                            try {
                                if (!response.isSuccessful()) {
                                    Log.e("MainActivity2", "loginGoogle not successful. code=" + response.code());
                                    Toast.makeText(MainActivity2.this, "Ошибка входа через Google: код " + response.code(), Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                ResponseGoogleLogin body = response.body();
                                if (body == null || body.user_id == 0) {
                                    Log.e("MainActivity2", "loginGoogle response body null or user_id==0");
                                    Toast.makeText(MainActivity2.this, "Ошибка входа через Google (пустой ответ)", Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                // 🔥 Google URL + данные
                                UserCache.userId = String.valueOf(body.user_id);
                                UserCache.currentLogin = body.login;
                                if (body.photo_url != null && !body.photo_url.trim().isEmpty()) {
                                    UserCache.userPhotoUrl = body.photo_url;
                                }

                                Log.i("MainActivity2", "loginGoogle success. userId=" + UserCache.userId + " photo=" + UserCache.userPhotoUrl);

                                Toast.makeText(MainActivity2.this, "Вход через Google успешен!", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(MainActivity2.this, MainActivity.class));
                                finish();

                            } catch (Exception e) {
                                Log.e("MainActivity2", "Exception in loginGoogle onResponse", e);
                                Toast.makeText(MainActivity2.this, "Внутренняя ошибка при входе", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseGoogleLogin> call, Throwable t) {
                            Log.e("MainActivity2", "loginGoogle onFailure", t);
                            Toast.makeText(MainActivity2.this, "Ошибка сети при входе через Google: " + t.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });

        } catch (ApiException e) {
            Log.e("MainActivity2", "Google Sign-In failed", e);
            Toast.makeText(this, "Ошибка Google Sign-In: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Log.e("MainActivity2", "Unexpected exception in onActivityResult", e);
            Toast.makeText(this, "Неожиданная ошибка", Toast.LENGTH_LONG).show();
        }
    }





    // Helper: безопасная загрузка изображения через Glide (в try/catch)
    private void safeLoadImage(ImageView iv, String url) {
        try {
            if (iv == null || url == null || url.trim().isEmpty()) return;
            Glide.with(this)
                    .load(url)
                    .placeholder(R.drawable.friend_cat)
                    .error(R.drawable.friend_cat) // на случай ошибки загрузки
                    .into(iv);
        } catch (Exception e) {
            Log.e("MainActivity2", "safeLoadImage failed", e);
        }
    }

    private void sendPhotoToServer(String userId, String photoUrl) {
        try {
            if (userId == null || userId.trim().isEmpty() || photoUrl == null || photoUrl.trim().isEmpty()) {
                Log.w("MainActivity2", "sendPhotoToServer skipped: bad params userId=" + userId + " photoUrl=" + photoUrl);
                return;
            }

            Map<String, String> body = new HashMap<>();
            body.put("user_id", userId);
            body.put("photo_url", photoUrl);

            RetrofitClient.getApiService().saveUserPhoto(body)
                    .enqueue(new retrofit2.Callback<Response<Void>>() {
                        @Override
                        public void onResponse(Call<Response<Void>> call, Response<Response<Void>> response) {
                            try {
                                if (response.isSuccessful()) {
                                    Log.i("MainActivity2", "saveUserPhoto success");
                                    // не обязательно Toast здесь — чтобы не спамить пользователя
                                } else {
                                    Log.w("MainActivity2", "saveUserPhoto not successful, code=" + response.code());
                                }
                            } catch (Exception e) {
                                Log.e("MainActivity2", "Exception in saveUserPhoto onResponse", e);
                            }
                        }

                        @Override
                        public void onFailure(Call<Response<Void>> call, Throwable t) {
                            Log.e("MainActivity2", "saveUserPhoto onFailure", t);
                        }
                    });
        } catch (Exception e) {
            Log.e("MainActivity2", "sendPhotoToServer threw", e);
        }
    }


    // GoogleLoginRequest.java
    public static class GoogleLoginRequest {
        private String id_token;

        public GoogleLoginRequest(String id_token) {
            this.id_token = id_token;
        }

        public String getId_token() {
            return id_token;
        }

        public void setId_token(String id_token) {
            this.id_token = id_token;
        }
    }
}

