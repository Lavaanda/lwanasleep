package com.example.mobilki_iyoyyy;

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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SovmestZadanie extends BaseActivity {

    TextView vivodZadania;
    ImageView imageView8;
    int projectId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout. activity_sovmest_zadanie);

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

        vivodZadania = findViewById(R.id.vivodZadania);

        // Получаем project_id из Intent
        projectId = getIntent().getIntExtra("project_id", -1);

        if (projectId == -1) {
            Toast.makeText(this, "Ошибка: project_id не передан", Toast.LENGTH_LONG).show();
            return;
        }

        // Переходы на разные активности
        ImageView imageView12 = findViewById(R.id.imageView12);
        imageView12.setOnClickListener(v ->
                startActivity(new Intent(SovmestZadanie.this, MainActivityPalka.class))
        );

        ImageView imageView210 = findViewById(R.id.imageView210);
        imageView210.setOnClickListener(v ->
                startActivity(new Intent(SovmestZadanie.this, RatingActivity.class))
        );

        ImageView imageView23 = findViewById(R.id.imageView23);
        imageView23.setOnClickListener(v ->
                startActivity(new Intent(SovmestZadanie.this, FORYM_TOPICS.class))
        );

        ImageView imageView22 = findViewById(R.id.imageView22);
        imageView22.setOnClickListener(v ->
                startActivity(new Intent(SovmestZadanie.this, MainActivity.class))
        );

        loadProjectContent();

        ImageView imageView8 = findViewById(R.id.imageView8);
        loadUserPhoto(findViewById(R.id.imageView8));
        imageView8.setOnClickListener(v ->
                startActivity(new Intent(SovmestZadanie.this, MainActivity_profile.class))
        );
    }

    private void loadProjectContent() {
        ApiService api = RetrofitClient.getApiService();

        api.getProjectContent(projectId).enqueue(new Callback<ProjectContentResponse>() {
            @Override
            public void onResponse(Call<ProjectContentResponse> call, Response<ProjectContentResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    vivodZadania.setText(response.body().content);
                } else {
                    vivodZadania.setText("Нет данных по проекту");
                }
            }

            @Override
            public void onFailure(Call<ProjectContentResponse> call, Throwable t) {
                Toast.makeText(SovmestZadanie.this, "Ошибка: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
