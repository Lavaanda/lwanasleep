package com.example.mobilki_iyoyyy;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import java.util.HashMap;
import java.util.Map;

public class BaseActivity extends AppCompatActivity {

    protected void loadUserPhoto(ImageView imageView) {
        UserPhotoLoader.loadInto(imageView, this);
    }

    // тёмная тема (как у тебя)
    private static final int DARK_BACKGROUND = 0xFF121212;
    private static final int DARK_TEXT_COLOR = 0xFFFFFFFF;
    private static final int DARK_IMAGE_COLOR = 0xFFA698FF;

    // Храним ОРИГИНАЛЬНЫЕ цвета из XML
    private final Map<Integer, Integer> originalTextColors = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void applyThemeGlobal(
            int rootLayoutId,
            int[] textViewIds,
            int[] imageViewIds
    ) {
        SharedPreferences prefs = getSharedPreferences("settings", MODE_PRIVATE);
        boolean darkEnabled = prefs.getBoolean("dark_theme", false);

        ConstraintLayout root = findViewById(rootLayoutId);
        if (root != null) {
            int lightBackground = ContextCompat.getColor(this, R.color.fiol);
            root.setBackgroundColor(darkEnabled ? DARK_BACKGROUND : lightBackground);
        }

        // ❗ важно: чистим кэш, иначе телефон "залипает"
        originalTextColors.clear();

        // TextView
        if (textViewIds != null) {
            for (int id : textViewIds) {
                TextView tv = findViewById(id);
                if (tv != null) {

                    // берём цвет ТОЛЬКО из XML
                    int xmlColor = tv.getTextColors().getDefaultColor();
                    originalTextColors.put(id, xmlColor);

                    tv.setTextColor(
                            darkEnabled
                                    ? DARK_TEXT_COLOR
                                    : xmlColor
                    );
                }
            }
        }

        // ImageView
        if (imageViewIds != null) {
            for (int id : imageViewIds) {
                ImageView iv = findViewById(id);
                if (iv != null) {
                    if (darkEnabled) {
                        iv.setColorFilter(DARK_IMAGE_COLOR);
                    } else {
                        iv.clearColorFilter();
                    }
                }
            }
        }
    }
}
