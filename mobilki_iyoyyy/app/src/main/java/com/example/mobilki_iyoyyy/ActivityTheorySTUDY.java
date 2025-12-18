package com.example.mobilki_iyoyyy;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityTheorySTUDY extends BaseActivity {

    private TextView textTitle, textContent;
    private ApiService api;
    private int theoryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theory_study);

        textTitle = findViewById(R.id.textTitle);
        textContent = findViewById(R.id.textContent);

        loadUserPhoto(findViewById(R.id.imageView8));

        ImageView imageView8 = findViewById(R.id.imageView8);
        loadUserPhoto(findViewById(R.id.imageView8));
        imageView8.setOnClickListener(v ->
                startActivity(new Intent(ActivityTheorySTUDY.this, MainActivity_profile.class))
        );


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

        api = RetrofitClient.getApiService();

        // Получаем theoryId из Intent
        theoryId = getIntent().getIntExtra("theory_id", -1);
        if (theoryId == -1) {
            textTitle.setText("Ошибка");
            textContent.setText("ID теории не передан");
            Log.e("ActivityTheorySTUDY", "theory_id is -1");
            return;
        }
        Log.d("ActivityTheorySTUDY", "Received theoryId=" + theoryId);

        int[] emptyTextIds = new int[] {};

        int[] imageIds = new int[] {
        };

        applyThemeGlobal(R.id.main, emptyTextIds, imageIds);

        // Переходы на разные активности
        ImageView imageView12 = findViewById(R.id.imageView12);
        imageView12.setOnClickListener(v ->
                startActivity(new Intent(ActivityTheorySTUDY.this, MainActivityPalka.class))
        );

        ImageView imageView210 = findViewById(R.id.imageView210);
        imageView210.setOnClickListener(v ->
                startActivity(new Intent(ActivityTheorySTUDY.this, RatingActivity.class))
        );

        ImageView imageView23 = findViewById(R.id.imageView23);
        imageView23.setOnClickListener(v ->
                startActivity(new Intent(ActivityTheorySTUDY.this, FORYM_TOPICS.class))
        );

        ImageView imageView22 = findViewById(R.id.imageView22);
        imageView22.setOnClickListener(v ->
                startActivity(new Intent(ActivityTheorySTUDY.this, MainActivity.class))
        );


        // Загружаем теорию сразу, используя пользователя айди
        loadTheory(theoryId);


    }

    private void loadTheory(int id) {
        api.getTheory(id).enqueue(new Callback<Theory>() {
            @Override
            public void onResponse(Call<Theory> call, Response<Theory> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Theory theory = response.body();
                    textTitle.setText(theory.getTitle());
                    textContent.setText(theory.getContent());
                    Log.d("ActivityTheorySTUDY", "Loaded theory id=" + id);
                } else {
                    textTitle.setText("Ошибка");
                    textContent.setText("Теория не найдена или нет доступа");
                    Log.e("ActivityTheorySTUDY", "Failed to load theory, code=" + response.code());
                }
            }

            @Override
            public void onFailure(Call<Theory> call, Throwable t) {
                textTitle.setText("Ошибка");
                textContent.setText("Не удалось загрузить теорию: " + t.getMessage());
                Log.e("ActivityTheorySTUDY", "Load theory failure", t);
            }
        });
    }
}
