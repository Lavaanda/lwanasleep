package com.example.mobilki_iyoyyy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
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

public class MyCommandActivity extends BaseActivity {

    Spinner spinnerRoles;
    int projectId = 1;
    private boolean isSpinnerInitialized = false;

    TextView textLogin22;
    TextView textLogin222;
    ImageView imageView8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_command);

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

        spinnerRoles = findViewById(R.id.spinnerRoles);
        textLogin22 = findViewById(R.id.textLogin22);
        textLogin222 = findViewById(R.id.textLogin222);

        setupRoleSpinner();
        loadTeamMembers();

        ImageView imageView3 = findViewById(R.id.imageView744);
        imageView3.setOnClickListener(v -> {
            Intent intent = new Intent(MyCommandActivity.this, SovmestZadanie.class);
            intent.putExtra("project_id", 1);
            startActivity(intent);
        });

        // Переходы на разные активности
        ImageView imageView12 = findViewById(R.id.imageView12);
        imageView12.setOnClickListener(v ->
                startActivity(new Intent(MyCommandActivity.this, MainActivityPalka.class))
        );

        ImageView imageView210 = findViewById(R.id.imageView210);
        imageView210.setOnClickListener(v ->
                startActivity(new Intent(MyCommandActivity.this, RatingActivity.class))
        );

        ImageView imageView23 = findViewById(R.id.imageView23);
        imageView23.setOnClickListener(v ->
                startActivity(new Intent(MyCommandActivity.this, FORYM_TOPICS.class))
        );

        ImageView imageView22 = findViewById(R.id.imageView22);
        imageView22.setOnClickListener(v ->
                startActivity(new Intent(MyCommandActivity.this, MainActivity.class))
        );
        ImageView imageView8 = findViewById(R.id.imageView8);
        loadUserPhoto(findViewById(R.id.imageView8));
        imageView8.setOnClickListener(v ->
                startActivity(new Intent(MyCommandActivity.this, MainActivity_profile.class))
        );
    }

    private void setupRoleSpinner() {
        Role[] roles = new Role[]{
                new Role("Администратор бд", ""),
                new Role("Дата инженер", "")
        };

        RoleAdapter adapter = new RoleAdapter(this, roles);
        spinnerRoles.setAdapter(adapter);

        spinnerRoles.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!isSpinnerInitialized) {
                    isSpinnerInitialized = true;
                    return;
                }
                Role selectedRole = (Role) parent.getItemAtPosition(position);
                updateUserRole(selectedRole.getName());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
    }

    private void loadTeamMembers() {
        RetrofitClient.getApiService().getTeamMembers()
                .enqueue(new Callback<TeamResponse>() {
                    @Override
                    public void onResponse(Call<TeamResponse> call, Response<TeamResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            TeamResponse team = response.body();

                            // текущий пользователь
                            if (team.getCurrentUser() != null)
                                textLogin22.setText(team.getCurrentUser().getLogin());
                            else
                                textLogin22.setText("Нет");

                            // напарник
                            if (team.getTeammate() != null)
                                textLogin222.setText(team.getTeammate().getLogin());
                            else
                                textLogin222.setText("Нет");
                        } else {
                            textLogin22.setText("Ошибка загрузки");
                            textLogin222.setText("Ошибка загрузки");
                        }
                    }

                    @Override
                    public void onFailure(Call<TeamResponse> call, Throwable t) {
                        textLogin22.setText("Ошибка сети");
                        textLogin222.setText("Ошибка сети");
                    }
                });
    }


    private void updateUserRole(String role) {
        RequestUserProject request = new RequestUserProject(projectId, role);

        RetrofitClient.getApiService().updateUserProject(request)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) { }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) { }
                });
    }
}
