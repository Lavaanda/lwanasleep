package com.example.mobilki_iyoyyy;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

public class ActivityTheory extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theory);


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



        ImageView imageView8 = findViewById(R.id.imageView8);
        loadUserPhoto(findViewById(R.id.imageView8));
        imageView8.setOnClickListener(v ->
                startActivity(new Intent(ActivityTheory.this, MainActivity_profile.class))
        );

        int[] emptyTextIds = new int[] {};

        int[] imageIds = new int[] {};

        applyThemeGlobal(R.id.main, emptyTextIds, imageIds);

        ImageView img1 = findViewById(R.id.imageView15);
        ImageView img2 = findViewById(R.id.imageView21);
        ImageView img3 = findViewById(R.id.imageView25);
        ImageView img4 = findViewById(R.id.imageView35);

        img1.setOnClickListener(this);
        img2.setOnClickListener(this);
        img3.setOnClickListener(this);
        img4.setOnClickListener(this);

        // Переходы на активности
        ImageView imageView12 = findViewById(R.id.imageView12);
        imageView12.setOnClickListener(v ->
                startActivity(new Intent(ActivityTheory.this, MainActivityPalka.class))
        );

        ImageView imageView210 = findViewById(R.id.imageView210);
        imageView210.setOnClickListener(v ->
                startActivity(new Intent(ActivityTheory.this, RatingActivity.class))
        );

        ImageView imageView23 = findViewById(R.id.imageView23);
        imageView23.setOnClickListener(v ->
                startActivity(new Intent(ActivityTheory.this, FORYM_TOPICS.class))
        );

        ImageView imageView22 = findViewById(R.id.imageView22);
        imageView22.setOnClickListener(v ->
                startActivity(new Intent(ActivityTheory.this, MainActivity.class))
        );

        ImageView imageView11 = findViewById(R.id.imageView40);
        imageView11.setOnClickListener(v ->
                startActivity(new Intent(ActivityTheory.this, PracticeStudyActivity.class))
        );


    }

    @Override
    public void onClick(View v) {
        int theoryId = -1;

        if (v.getId() == R.id.imageView15) {
            theoryId = 1;
        } else if (v.getId() == R.id.imageView21) {
            theoryId = 2;
        } else if (v.getId() == R.id.imageView25) {
            theoryId = 3;
        } else if (v.getId() == R.id.imageView35) {
            theoryId = 4;
        }

        Log.d("ActivityTheory", "Clicked view id=" + v.getId() + ", theoryId=" + theoryId);

        if (theoryId != -1) {
            Intent intent = new Intent(this, ActivityTheorySTUDY.class);
            intent.putExtra("theory_id", theoryId);
            Log.d("ActivityTheory", "Starting ActivityTheorySTUDY with theoryId=" + theoryId);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Не удалось определить id теории", Toast.LENGTH_SHORT).show();
        }
    }
}