package com.example.mobilki_iyoyyy;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import com.bumptech.glide.Glide;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity_profile extends BaseActivity {

    TextView textLogin, textDescription, textId, textView222;
    ImageView imageProfile, imagePalka, btnAddFriend, btnZaavka, btnUploadPhoto;

    int currentFriendId;

    private static final int PICK_IMAGE = 101;
    private Uri selectedImageUri;

    private boolean requestSent = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        WindowInsetsControllerCompat controller =
                new WindowInsetsControllerCompat(getWindow(), getWindow().getDecorView());
        controller.hide(WindowInsetsCompat.Type.systemBars());
        controller.setSystemBarsBehavior(
                WindowInsetsControllerCompat.BEHAVIOR_DEFAULT
        );
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_profile), (v, insets) -> {
            return insets;
        });

        ImageView imageViewExport = findViewById(R.id.imageViewExport);
        imageViewExport.setOnClickListener(v -> exportProfileToExcel());

        // views
        textLogin = findViewById(R.id.textView229);
        textDescription = findViewById(R.id.textView228);
        textId = findViewById(R.id.textView230);
        imageProfile = findViewById(R.id.imageView30);
        imagePalka = findViewById(R.id.imageView12);
        btnAddFriend = findViewById(R.id.textView222);
        btnZaavka = findViewById(R.id.butBut333);
        btnUploadPhoto = findViewById(R.id.butBut);

        btnAddFriend.setOnClickListener(v -> {
            if (currentFriendId != -1) {
                sendFriendRequest(currentFriendId);
            }
        });

        btnZaavka.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity_profile.this, FriendRequestsActivity.class);
            startActivity(intent);
        });

        applyThemeGlobal(R.id.main_profile, new int[]{}, new int[]{});

        currentFriendId = getIntent().getIntExtra("friend_id", -1);

        // ✅ ВОТ ЭТОГО У ТЕБЯ НЕ БЫЛО: восстановили requestSent при открытии профиля
        if (currentFriendId != -1) {
            requestSent = getSharedPreferences("friends", MODE_PRIVATE)
                    .getBoolean("request_sent_" + currentFriendId, false);
        }

        if (currentFriendId != -1) {
            loadFriendProfile(currentFriendId);

            // ЗАПРЕЩАЕМ действия в профиле друга
            btnUploadPhoto.setClickable(false);
            btnUploadPhoto.setEnabled(false);

            btnZaavka.setClickable(false);
            btnZaavka.setEnabled(false);
            imageViewExport.setClickable(false);
            imageViewExport.setEnabled(false);

            imageViewExport.setAlpha(0.4f);
            btnUploadPhoto.setAlpha(0.4f);
            btnZaavka.setAlpha(0.4f);

        } else {
            btnAddFriend.setVisibility(View.GONE);
            loadOwnProfile();
        }

        // (эта проверка у тебя была, но она была бесполезна до восстановления requestSent —
        // сейчас она работает, но вообще можно и без неё; я оставляю как у тебя)
        if (requestSent) {
            btnAddFriend.setVisibility(View.GONE);
            btnAddFriend.setEnabled(false);
        }

        // загрузка фото
        btnUploadPhoto.setOnClickListener(v -> openGallery());
        if (currentFriendId != -1) {
            // 🔧 FIX: мы в профиле ДРУГА — не даём UserPhotoLoader
            // использовать мусор из UserCache
            UserCache.profilePhoto = null;
        }
        // навигация
        imagePalka.setOnClickListener(v ->
                startActivity(new Intent(this, MainActivityPalka.class)));
        findViewById(R.id.imageView210).setOnClickListener(v ->
                startActivity(new Intent(this, RatingActivity.class)));
        findViewById(R.id.imageView23).setOnClickListener(v ->
                startActivity(new Intent(this, FORYM_TOPICS.class)));
        findViewById(R.id.imageView22).setOnClickListener(v ->
                startActivity(new Intent(this, MainActivity.class)));

        ImageView imageView8 = findViewById(R.id.imageView8);

        UserPhotoLoader.loadInto(imageView8, this);

        imageView8.setOnClickListener(v ->
                startActivity(new Intent(MainActivity_profile.this, MainActivity_profile.class))
        );
    }

    // ВЫБОР ФОТО
    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            imageProfile.setImageURI(selectedImageUri);
            uploadProfilePhoto(selectedImageUri);
        }
    }

    private void exportProfileToExcel() {
        try {
            // Данные из TextView
            String login = textLogin.getText().toString();
            String description = textDescription.getText().toString();
            String userId = textId.getText().toString();

            // Имя файла
            String fileName = "profile_user_" + userId + ".csv";

            android.content.ContentValues values = new android.content.ContentValues();
            values.put(android.provider.MediaStore.Files.FileColumns.DISPLAY_NAME, fileName);
            values.put(android.provider.MediaStore.Files.FileColumns.MIME_TYPE, "text/csv");
            values.put(android.provider.MediaStore.Files.FileColumns.RELATIVE_PATH, android.os.Environment.DIRECTORY_DOCUMENTS);

            Uri uri = getContentResolver().insert(
                    android.provider.MediaStore.Files.getContentUri("external"),
                    values
            );

            if (uri == null) {
                Toast.makeText(this, "Ошибка сохранения файла", Toast.LENGTH_SHORT).show();
                return;
            }

            String csv = "ID пользователя,Логин,Описание\n" + userId + "," + login + "," + description;

            OutputStream os = getContentResolver().openOutputStream(uri);
            os.write(csv.getBytes());
            os.close();

            Toast.makeText(this, "CSV сохранён в Документы: " + fileName, Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Ошибка экспорта: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    // Открыть файл
    private void openExcelFile(Uri uri) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Toast.makeText(this, "Нет приложения для открытия Excel. Файл сохранён в Документы.", Toast.LENGTH_LONG).show();
        }
    }

    // ЗАГРУЗКА ФОТО
    private void uploadProfilePhoto(Uri uri) {
        try {
            InputStream is = getContentResolver().openInputStream(uri);
            byte[] bytes = readBytes(is);

            RequestBody requestFile =
                    RequestBody.create(bytes, MediaType.parse("image/*"));

            MultipartBody.Part body =
                    MultipartBody.Part.createFormData(
                            "photo", "profile.png", requestFile
                    );

            RetrofitClient.getApiService()
                    .uploadUserPhoto(body)
                    .enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call,
                                               Response<ResponseBody> response) {
                            if (response.isSuccessful()) {

                                Toast.makeText(
                                        MainActivity_profile.this,
                                        "Фото сохранено",
                                        Toast.LENGTH_SHORT
                                ).show();

                                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                if (currentFriendId == -1) { // только МОЙ профиль
                                    UserCache.profilePhoto = bmp;
                                }


                                int myId = Integer.parseInt(textId.getText().toString());
                                FriendAdapterStaticCache.updateMyAvatar(myId, bmp);
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Toast.makeText(
                                    MainActivity_profile.this,
                                    "Сеть: " + t.getMessage(),
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                    });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private byte[] readBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int nRead;
        byte[] data = new byte[4096];
        while ((nRead = inputStream.read(data)) != -1) {
            buffer.write(data, 0, nRead);
        }
        return buffer.toByteArray();
    }

    // ПРОФИЛЬ ДРУГА
    private void loadFriendProfile(int friendId) {

        imageProfile.setImageResource(R.drawable.friend_cat);

        RetrofitClient.getApiService().getFriendProfile(friendId)
                .enqueue(new Callback<Friend>() {

                    @Override
                    public void onResponse(Call<Friend> call, Response<Friend> response) {
                        if (response.isSuccessful() && response.body() != null) {

                            Friend friend = response.body();

                            // базовые данные
                            textLogin.setText(friend.getLogin());
                            textDescription.setText(friend.getStatus());
                            textId.setText(String.valueOf(friend.getId()));

                            // ДОБАВИТЬ В ДРУЗЬЯ
                            // показываем ТОЛЬКО если НЕ друг и если заявка ещё не отправлялась
                            boolean alreadySent = getSharedPreferences("friends", MODE_PRIVATE)
                                    .getBoolean("request_sent_" + friend.getId(), false);

                            if (!friend.isFriend() && !alreadySent) {
                                btnAddFriend.setVisibility(View.VISIBLE);
                                btnAddFriend.setEnabled(true);
                            } else {
                                btnAddFriend.setVisibility(View.GONE);
                                btnAddFriend.setEnabled(false);
                            }

                            // ---------- ФОТО ----------
                            imageProfile.setImageResource(R.drawable.friend_cat);

                            String photoUrl = friend.getPhotoUrl();
                            String base64 = friend.getPhotoBase64();

                            if (photoUrl != null && !photoUrl.isEmpty()) {

                                Glide.with(MainActivity_profile.this)
                                        .load(photoUrl)
                                        .placeholder(R.drawable.friend_cat)
                                        .error(R.drawable.friend_cat)
                                        .circleCrop()
                                        .into(imageProfile);

                            } else if (base64 != null && !base64.isEmpty()) {

                                byte[] decoded = Base64.decode(base64, Base64.DEFAULT);
                                Bitmap bitmap = BitmapFactory.decodeByteArray(decoded, 0, decoded.length);
                                imageProfile.setImageBitmap(bitmap);

                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<Friend> call, Throwable t) {
                        Toast.makeText(
                                MainActivity_profile.this,
                                "Ошибка сети: " + t.getMessage(),
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                });
    }

    private void sendFriendRequest(int friendId) {
        RetrofitClient.getApiService()
                .requestFriend(friendId)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call,
                                           Response<ResponseBody> response) {

                        // ✅ УСПЕШНО ОТПРАВИЛИ (200) или ✅ УЖЕ БЫЛО (409)
                        if (response.code() == 200 || response.code() == 409) {
                            requestSent = true;

                            // ✅ ВОТ ЭТОГО У ТЕБЯ НЕ БЫЛО: запомнили навсегда для этого friendId
                            getSharedPreferences("friends", MODE_PRIVATE)
                                    .edit()
                                    .putBoolean("request_sent_" + friendId, true)
                                    .apply();

                            btnAddFriend.setVisibility(View.GONE);
                            btnAddFriend.setEnabled(false);
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        btnAddFriend.setEnabled(true);
                    }
                });
    }

    private void addFriend(int friendId) {
        RetrofitClient.getApiService()
                .addFriend(friendId)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call,
                                           Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(
                                    MainActivity_profile.this,
                                    "Пользователь добавлен в друзья",
                                    Toast.LENGTH_SHORT
                            ).show();
                            btnAddFriend.setVisibility(View.GONE);
                        } else {
                            btnAddFriend.setEnabled(true);
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        btnAddFriend.setEnabled(true);
                        Toast.makeText(
                                MainActivity_profile.this,
                                "Сеть: " + t.getMessage(),
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                });
    }

    // ===============================
    // 👤 МОЙ ПРОФИЛЬ
    // ===============================
    private void loadOwnProfile() {
        RetrofitClient.getApiService().getUserInfo()
                .enqueue(new Callback<UserProfileResponse>() {
                    @Override
                    public void onResponse(Call<UserProfileResponse> call,
                                           Response<UserProfileResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            UserProfileResponse user = response.body();
                            textLogin.setText(user.getLogin());
                            textDescription.setText(
                                    user.getDescription() != null ? user.getDescription() : ""
                            );
                            textId.setText(String.valueOf(user.getId_user()));
                        }
                    }

                    @Override
                    public void onFailure(Call<UserProfileResponse> call, Throwable t) {
                        Toast.makeText(
                                MainActivity_profile.this,
                                "Ошибка сети: " + t.getMessage(),
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                });

        imageProfile.setImageResource(R.drawable.friend_cat);

        if (UserCache.profilePhoto != null) {
            imageProfile.setImageBitmap(UserCache.profilePhoto);
        } else if (UserCache.userPhotoUrl != null && !UserCache.userPhotoUrl.trim().isEmpty()) {
            Glide.with(this)
                    .load(UserCache.userPhotoUrl)
                    .placeholder(R.drawable.friend_cat)
                    .error(R.drawable.friend_cat)
                    .into(imageProfile);
        } else {
            loadUserPhoto();
        }
    }

    private void loadUserPhoto() {
        RetrofitClient.getApiService().getUserPhoto()
                .enqueue(new Callback<UserPhotoResponse>() {
                    @Override
                    public void onResponse(Call<UserPhotoResponse> call,
                                           Response<UserPhotoResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            String base64 = response.body().photo;
                            if (base64 != null && !base64.isEmpty()) {
                                byte[] decoded = Base64.decode(base64, Base64.DEFAULT);
                                Bitmap bitmap = BitmapFactory.decodeByteArray(
                                        decoded, 0, decoded.length
                                );
                                imageProfile.setImageBitmap(bitmap);
                                imageProfile.setImageBitmap(bitmap);
                                if (currentFriendId == -1) {
                                    UserCache.profilePhoto = bitmap;
                                }

                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<UserPhotoResponse> call, Throwable t) {
                        Toast.makeText(
                                MainActivity_profile.this,
                                "Ошибка загрузки фото",
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                });
    }
}
