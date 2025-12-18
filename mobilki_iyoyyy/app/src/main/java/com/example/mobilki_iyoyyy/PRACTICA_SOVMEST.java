package com.example.mobilki_iyoyyy;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PRACTICA_SOVMEST extends BaseActivity {

    TextView textL2;
    ImageView imageViewDelete, imageView8; // ← imageView7222

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sivmest);

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

        textL2 = findViewById(R.id.textL2);
        imageViewDelete = findViewById(R.id.imageView7222);

        textL2.setOnClickListener(v -> {
            Intent intent = new Intent(PRACTICA_SOVMEST.this, SovmestDobavitActivity.class);
            startActivity(intent);
        });

        imageViewDelete.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Удаление сокомандника")
                    .setMessage("Отправить запрос на удаление сокомандника?")
                    .setPositiveButton("Отправить", (d, w) -> sendDeleteRequest())
                    .setNegativeButton("Отмена", null)
                    .show();
        });

        ImageView imageView5 = findViewById(R.id.imageView71);
        imageView5.setOnClickListener(v -> {
            Intent intent = new Intent(PRACTICA_SOVMEST.this, MyCommandActivity.class);
            startActivity(intent);
        });

        // Переходы на разные активности
        ImageView imageView12 = findViewById(R.id.imageView12);
        imageView12.setOnClickListener(v ->
                startActivity(new Intent(PRACTICA_SOVMEST.this, MainActivityPalka.class))
        );

        ImageView imageView210 = findViewById(R.id.imageView210);
        imageView210.setOnClickListener(v ->
                startActivity(new Intent(PRACTICA_SOVMEST.this, RatingActivity.class))
        );

        ImageView imageView23 = findViewById(R.id.imageView23);
        imageView23.setOnClickListener(v ->
                startActivity(new Intent(PRACTICA_SOVMEST.this, FORYM_TOPICS.class))
        );

        ImageView imageView22 = findViewById(R.id.imageView22);
        imageView22.setOnClickListener(v ->
                startActivity(new Intent(PRACTICA_SOVMEST.this, MainActivity.class))
        );
        ImageView imageView8 = findViewById(R.id.imageView8);
        loadUserPhoto(findViewById(R.id.imageView8));
        imageView8.setOnClickListener(v ->
                startActivity(new Intent(PRACTICA_SOVMEST.this, MainActivity_profile.class))
        );
    }

    private void sendDeleteRequest() {
        ApiService api = RetrofitClient.getApiService();

        Map<String, Object> body = new HashMap<>();
        body.put("project_id", 1); // как ты сказала — у тебя всегда 1

        api.requestDeleteTeammate(body).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(PRACTICA_SOVMEST.this, "Запрос отправлен", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(PRACTICA_SOVMEST.this, "Ошибка при отправке", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(PRACTICA_SOVMEST.this, "Сеть недоступна", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
