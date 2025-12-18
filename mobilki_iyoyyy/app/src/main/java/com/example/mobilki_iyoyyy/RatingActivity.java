package com.example.mobilki_iyoyyy;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RatingActivity extends BaseActivity {

    private static final String TAG = "RatingActivity";

    // ТОП-3
    private TextView textName1, textBall1;
    private TextView textName2, textBall2;
    private TextView textName3, textBall3;
    ImageView imageView8;

    // ТЫ
    private TextView textPlaceYou, textNameYou, textBallYou;

    private ApiService api;
    private int myUserId = -1;
    private String myLogin = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            setContentView(R.layout.acrivity_rate);

            Log.d(TAG, "setContentView выполнен");

            api = RetrofitClient.getApiService();

            initViews();

            loadCurrentUserAndThenRating();

        } catch (Exception e) {
            Log.e(TAG, "Ошибка в onCreate", e);
            Toast.makeText(this, "Ошибка в onCreate: " + e.getClass().getSimpleName() + " " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

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

        // Переходы на разные активности
        ImageView imageView12 = findViewById(R.id.imageView12);
        imageView12.setOnClickListener(v ->
                startActivity(new Intent(RatingActivity.this, MainActivityPalka.class))
        );

        ImageView imageView210 = findViewById(R.id.imageView210);
        imageView210.setOnClickListener(v ->
                startActivity(new Intent(RatingActivity.this, RatingActivity.class))
        );

        ImageView imageView23 = findViewById(R.id.imageView23);
        imageView23.setOnClickListener(v ->
                startActivity(new Intent(RatingActivity.this, FORYM_TOPICS.class))
        );

        ImageView imageView22 = findViewById(R.id.imageView22);
        imageView22.setOnClickListener(v ->
                startActivity(new Intent(RatingActivity.this, MainActivity.class))
        );
        ImageView imageView8 = findViewById(R.id.imageView8);
        loadUserPhoto(findViewById(R.id.imageView8));
        imageView8.setOnClickListener(v ->
                startActivity(new Intent(RatingActivity.this, MainActivity_profile.class))
        );
    }

    private void initViews() {
        textName1 = findViewById(R.id.textName1);
        textBall1 = findViewById(R.id.textBall1);
        textName2 = findViewById(R.id.textName2);
        textBall2 = findViewById(R.id.textBall2);
        textName3 = findViewById(R.id.textName3);
        textBall3 = findViewById(R.id.textBall3);
        textPlaceYou = findViewById(R.id.textPlace_you);
        textNameYou = findViewById(R.id.textName_you);
        textBallYou = findViewById(R.id.textBall_you);

        checkView(textName1, "textName1");
        checkView(textBall1, "textBall1");
        checkView(textName2, "textName2");
        checkView(textBall2, "textBall2");
        checkView(textName3, "textName3");
        checkView(textBall3, "textBall3");
        checkView(textPlaceYou, "textPlace_you");
        checkView(textNameYou, "textName_you");
        checkView(textBallYou, "textBall_you");
    }

    private void checkView(TextView view, String name) {
        if (view == null) {
            Log.e(TAG, "TextView не найден: " + name);
            Toast.makeText(this, "TextView не найден: " + name, Toast.LENGTH_SHORT).show();
        } else {
            Log.d(TAG, "TextView OK: " + name);
        }
    }

    private void loadCurrentUserAndThenRating() {
        if (api == null) {
            Log.e(TAG, "ApiService null!");
            return;
        }

        api.getUserInfoRow().enqueue(new Callback<UserInfoResponse>() {
            @Override
            public void onResponse(Call<UserInfoResponse> call, Response<UserInfoResponse> response) {
                try {
                    if (!response.isSuccessful() || response.body() == null) {
                        Toast.makeText(RatingActivity.this, "Не удалось получить профиль (session)", Toast.LENGTH_SHORT).show();
                        loadRatingAndFill(-1, null);
                        return;
                    }

                    myUserId = response.body().getId_user();
                    myLogin = response.body().getLogin();

                    Log.d(TAG, "session id=" + myUserId + " login=" + myLogin);

                    loadRatingAndFill(myUserId, myLogin);

                } catch (Exception e) {
                    Log.e(TAG, "Ошибка в onResponse getUserInfoRow", e);
                    Toast.makeText(RatingActivity.this, "Ошибка getUserInfoRow: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<UserInfoResponse> call, Throwable t) {
                Log.e(TAG, "Ошибка сети getUserInfoRow", t);
                Toast.makeText(RatingActivity.this, "Ошибка сети: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                loadRatingAndFill(-1, null);
            }
        });
    }

    private void loadRatingAndFill(int myId, String myLoginFallback) {
        if (api == null) return;

        api.getRating().enqueue(new Callback<RatingResponse>() {
            @Override
            public void onResponse(Call<RatingResponse> call, Response<RatingResponse> response) {
                try {
                    if (!response.isSuccessful() || response.body() == null) {
                        Toast.makeText(RatingActivity.this, "Ошибка загрузки рейтинга", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    RatingResponse resp = response.body();
                    List<RatingUser> list = resp.rating;

                    fillTop3(list);

                    if (resp.your_place != null && resp.your_place > 0) {
                        safeSetText(textPlaceYou, resp.your_place + " место");
                        int yourPts = resp.your_points != null ? resp.your_points : 0;
                        safeSetText(textBallYou, yourPts + " баллов");


                        String fromList = findLoginInList(list, myId);
                        if (fromList != null) safeSetText(textNameYou, fromList);
                        else if (myLoginFallback != null) safeSetText(textNameYou, myLoginFallback);
                        else safeSetText(textNameYou, "-");

                        return;
                    }

                    if (myId <= 0) {
                        safeSetText(textPlaceYou, "-");
                        safeSetText(textNameYou, myLoginFallback != null ? myLoginFallback : "-");
                        safeSetText(textBallYou, "-");
                        return;
                    }

                    boolean found = false;
                    if (list != null) {
                        for (int i = 0; i < list.size(); i++) {
                            RatingUser u = list.get(i);
                            if (u != null && u.id_user == myId) {
                                safeSetText(textPlaceYou, String.valueOf(i + 1));
                                safeSetText(textNameYou, u.login != null ? u.login : myLoginFallback);
                                safeSetText(textBallYou, String.valueOf(u.points));
                                found = true;
                                break;
                            }
                        }
                    }

                    if (!found) {
                        safeSetText(textPlaceYou, "-");
                        safeSetText(textNameYou, myLoginFallback != null ? myLoginFallback : "-");
                        safeSetText(textBallYou, "-");
                    }

                } catch (Exception e) {
                    Log.e(TAG, "Ошибка в onResponse getRating", e);
                    Toast.makeText(RatingActivity.this, "Ошибка getRating: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<RatingResponse> call, Throwable t) {
                Log.e(TAG, "Ошибка сети getRating", t);
                Toast.makeText(RatingActivity.this, "Ошибка сети: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fillTop3(List<RatingUser> list) {
        safeSetText(textName1, "-");
        safeSetText(textBall1, "-");
        safeSetText(textName2, "-");
        safeSetText(textBall2, "-");
        safeSetText(textName3, "-");
        safeSetText(textBall3, "-");

        if (list == null) return;

        if (list.size() > 0) {
            safeSetText(textName1, list.get(0).login);
            safeSetText(textBall1, list.get(0).points + " баллов");

        }
        if (list.size() > 1) {
            safeSetText(textName2, list.get(1).login);
            safeSetText(textBall2, list.get(1).points + " баллов");

        }
        if (list.size() > 2) {
            safeSetText(textName3, list.get(2).login);
            safeSetText(textBall3, list.get(2).points + " баллов");

        }
    }

    private String findLoginInList(List<RatingUser> list, int id) {
        if (list == null) return null;
        for (RatingUser u : list) {
            if (u != null && u.id_user == id) return u.login;
        }
        return null;
    }

    /** Безопасный setText, чтобы не падало на null TextView */
    private void safeSetText(TextView tv, String text) {
        try {
            if (tv != null) tv.setText(text != null ? text : "-");
            else Log.e(TAG, "safeSetText: TextView null, текст=" + text);
        } catch (Exception e) {
            Log.e(TAG, "safeSetText: ошибка при setText", e);
        }
    }
}
