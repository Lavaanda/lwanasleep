package com.example.mobilki_iyoyyy;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import com.google.android.material.switchmaterial.SwitchMaterial;

public class MainTHEME extends BaseActivity {
    ImageView imageView8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);

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

        SharedPreferences prefs = getSharedPreferences("settings", MODE_PRIVATE);

        SwitchMaterial switchDarkTheme = findViewById(R.id.switchDarkTheme);

        // применяем тему при старте
        applyThemeGlobal(R.id.main, null, null);

        boolean darkEnabled = prefs.getBoolean("dark_theme", false);

        switchDarkTheme.setOnCheckedChangeListener(null);
        switchDarkTheme.setChecked(darkEnabled);

        switchDarkTheme.setOnCheckedChangeListener((buttonView, isChecked) -> {
            prefs.edit().putBoolean("dark_theme", isChecked).apply();


            recreate();

            Toast.makeText(
                    this,
                    "Тема изменена",
                    Toast.LENGTH_SHORT
            ).show();
        });

        findViewById(R.id.imageView12).setOnClickListener(v ->
                startActivity(new Intent(this, MainActivityPalka.class)));

        findViewById(R.id.imageView210).setOnClickListener(v ->
                startActivity(new Intent(this, RatingActivity.class)));

        findViewById(R.id.imageView23).setOnClickListener(v ->
                startActivity(new Intent(this, FORYM_TOPICS.class)));

        findViewById(R.id.imageView22).setOnClickListener(v ->
                startActivity(new Intent(this, MainActivity.class)));

        ImageView imageView8 = findViewById(R.id.imageView8);
        loadUserPhoto(findViewById(R.id.imageView8));
        imageView8.setOnClickListener(v ->
                startActivity(new Intent(MainTHEME.this, MainActivity_profile.class))
        );
    }
}
