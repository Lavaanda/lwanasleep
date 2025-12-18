package com.example.mobilki_iyoyyy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

public class PracticeStudyActivity extends BaseActivity {
    ImageView imageView8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practike_study);

        int[] emptyTextIds = new int[] {};

        int[] imageIds = new int[] {};

        applyThemeGlobal(R.id.main, emptyTextIds, imageIds);

        ImageView imageView40 = findViewById(R.id.imageView40);
        imageView40.setOnClickListener(v ->
                startActivity(new Intent(this, ActivityTheory.class))
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

        // Введение
        findViewById(R.id.imageView15).setOnClickListener(v ->
                openRazdel(1)
        );

        // Операторы
        findViewById(R.id.imageView21).setOnClickListener(v ->
                openRazdel(2)
        );

        // Типы данных
        findViewById(R.id.imageView25).setOnClickListener(v ->
                openRazdel(3)
        );

        // Запросы
        findViewById(R.id.imageView35).setOnClickListener(v ->
                openRazdel(4)
        );

        // Переходы на разные активности
        ImageView imageView12 = findViewById(R.id.imageView12);
        imageView12.setOnClickListener(v ->
                startActivity(new Intent(PracticeStudyActivity.this, MainActivityPalka.class))
        );

        ImageView imageView210 = findViewById(R.id.imageView210);
        imageView210.setOnClickListener(v ->
                startActivity(new Intent(PracticeStudyActivity.this, RatingActivity.class))
        );

        ImageView imageView23 = findViewById(R.id.imageView23);
        imageView23.setOnClickListener(v ->
                startActivity(new Intent(PracticeStudyActivity.this, FORYM_TOPICS.class))
        );

        ImageView imageView22 = findViewById(R.id.imageView22);
        imageView22.setOnClickListener(v ->
                startActivity(new Intent(PracticeStudyActivity.this, MainActivity.class))
        );
        ImageView imageView8 = findViewById(R.id.imageView8);
        loadUserPhoto(findViewById(R.id.imageView8));
        imageView8.setOnClickListener(v ->
                startActivity(new Intent(PracticeStudyActivity.this, MainActivity_profile.class))
        );
    }

    private void openRazdel(int razdelId) {
        Intent intent = new Intent(PracticeStudyActivity.this, MainTaskActivity.class);
        intent.putExtra("RAZDEL_ID", razdelId);
        startActivity(intent);
    }
}
