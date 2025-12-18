package com.example.mobilki_iyoyyy;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FriendRequestsActivity extends BaseActivity {

    private RecyclerView recyclerView;
    private final List<Friend> requests = new ArrayList<>();
    ImageView imageView8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.request);

        recyclerView = findViewById(R.id.recyclerRequests);
        if (recyclerView == null) return;

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

        recyclerView.setLayoutManager(new LinearLayoutManager(this)); // ← ВОТ ЭТОГО НЕ ХВАТАЛО

        findViewById(R.id.imageView12).setOnClickListener(v ->
                startActivity(new Intent(this, MainActivityPalka.class)));

        findViewById(R.id.imageView210).setOnClickListener(v ->
                startActivity(new Intent(this, RatingActivity.class)));

        findViewById(R.id.imageView23).setOnClickListener(v ->
                startActivity(new Intent(this, FORYM_TOPICS.class)));

        findViewById(R.id.imageView22).setOnClickListener(v ->
                startActivity(new Intent(this, MainActivity.class)));

        loadRequests();
        ImageView imageView8 = findViewById(R.id.imageView8);
        loadUserPhoto(findViewById(R.id.imageView8));
        imageView8.setOnClickListener(v ->
                startActivity(new Intent(FriendRequestsActivity.this, MainActivity_profile.class))
        );
    }


    private void loadRequests() {
        RetrofitClient.getApiService()
                .getFriendRequests()
                .enqueue(new Callback<List<Friend>>() {

                    @Override
                    public void onResponse(
                            Call<List<Friend>> call,
                            Response<List<Friend>> response
                    ) {
                        if (response.body() == null) return;

                        requests.clear();
                        requests.addAll(response.body());

                        recyclerView.setAdapter(
                                new FriendRequestsAdapter(requests)
                        );
                    }

                    @Override
                    public void onFailure(Call<List<Friend>> call, Throwable t) {
                        // можно Toast
                    }
                });
    }
}
