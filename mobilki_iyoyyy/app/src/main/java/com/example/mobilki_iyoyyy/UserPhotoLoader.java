package com.example.mobilki_iyoyyy;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import androidx.annotation.NonNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserPhotoLoader {
    public static void loadInto(ImageView imageView, Context context) {
        // 1) bitmap в кэше
        if (UserCache.profilePhoto != null) {
            imageView.setImageBitmap(UserCache.profilePhoto);
            return;
        }

        // 2) URL через Glide
        if (UserCache.userPhotoUrl != null && !UserCache.userPhotoUrl.trim().isEmpty()) {
            Glide.with(context)
                    .load(UserCache.userPhotoUrl)
                    .placeholder(R.drawable.kryg_zenhina)
                    .error(R.drawable.kryg_zenhina)
                    .circleCrop()
                    .into(imageView);
            return;
        }

        // 3) сервер (base64)
        RetrofitClient.getApiService().getUserPhoto()
                .enqueue(new Callback<UserPhotoResponse>() {
                    @Override
                    public void onResponse(Call<UserPhotoResponse> call, Response<UserPhotoResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            try {
                                String base64 = response.body().photo;
                                if (base64 != null && !base64.isEmpty()) {
                                    byte[] bytes = Base64.decode(base64, Base64.DEFAULT);
                                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                    Glide.with(imageView.getContext())
                                            .load(bitmap)
                                            .circleCrop()
                                            .into(imageView);

                                    UserCache.profilePhoto = bitmap;
                                } else {
                                    imageView.setImageResource(R.drawable.kryg_zenhina);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                imageView.setImageResource(R.drawable.kryg_zenhina);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<UserPhotoResponse> call, Throwable t) {
                        imageView.setImageResource(R.drawable.kryg_zenhina);
                    }
                });
    }
}
