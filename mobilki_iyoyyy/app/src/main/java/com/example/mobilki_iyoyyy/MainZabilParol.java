package com.example.mobilki_iyoyyy;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class MainZabilParol extends BaseActivity {

    EditText loginEdit;
    EditText oldPassEdit;
    EditText newPassEdit;
    ImageView changeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zabil_parol2);

        int[] emptyTextIds = new int[] {R.id.textViewHeader};

        int[] imageIds = new int[] {};

        applyThemeGlobal(R.id.main, emptyTextIds, imageIds);


        loginEdit = findViewById(R.id.textViewVxodLineEdit);
        oldPassEdit = findViewById(R.id.textViewParolLineEdit);
        newPassEdit = findViewById(R.id.textViewVxodLineEdit228);
        changeButton = findViewById(R.id.imageViewVxodButton);

        changeButton.setOnClickListener(v -> {
            String login = loginEdit.getText().toString();
            String oldP = oldPassEdit.getText().toString();
            String newP = newPassEdit.getText().toString();

            if (login.isEmpty() || oldP.isEmpty() || newP.isEmpty()) {
                Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show();
                return;
            }

            if (oldP.equals(newP)) {
                Toast.makeText(this, "Новый пароль не должен совпадать со старым", Toast.LENGTH_SHORT).show();
                return;
            }

            changePassword(login, oldP, newP);
        });
        ImageView backToLogin = findViewById(R.id.imageViewPerexod);

        backToLogin.setOnClickListener(v -> {
            Intent intent = new Intent(MainZabilParol.this, Main_Nastroiki.class);
            startActivity(intent);
            finish();
        });
    }

    private void changePassword(String login, String oldPass, String newPass) {

        RequestQueue queue = Volley.newRequestQueue(this);

        JSONObject body = new JSONObject();
        try {
            body.put("login", login);
            body.put("old_password", oldPass);
            body.put("new_password", newPass);
        } catch (Exception e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                "http://10.0.2.2:5000/change_password", // добавил порт Flask по умолчанию
                body,
                response -> {
                    Toast.makeText(this, "Пароль изменён!", Toast.LENGTH_SHORT).show();
                    finish();
                },
                error -> {
                    if (error.networkResponse != null && error.networkResponse.data != null) {
                        // получаем тело ответа от сервера
                        String bodyStr = new String(error.networkResponse.data);
                        Toast.makeText(this, "Ошибка сервера: " + bodyStr, Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(this, "Ошибка: " + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
        );

        queue.add(request);
    }
}
