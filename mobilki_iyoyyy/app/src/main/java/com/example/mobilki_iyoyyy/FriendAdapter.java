package com.example.mobilki_iyoyyy;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;
import java.util.List;
import android.util.LruCache;
import android.graphics.drawable.Drawable;
import androidx.annotation.Nullable;




public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.FriendViewHolder> {

    private final List<Friend> friends = new ArrayList<>();
    private OnFriendClickListener listener;

    // LruCache для кеширования Bitmaps
    private final LruCache<Integer, Bitmap> bitmapCache = new LruCache<>(10 * 1024 * 1024); // 10MB

    public interface OnFriendClickListener {
        void onFriendClick(Friend friend);
    }

    public void setOnFriendClickListener(OnFriendClickListener listener) {
        this.listener = listener;
    }

    public void setFriends(List<Friend> newFriends) {
        friends.clear();
        friends.addAll(newFriends);
        notifyDataSetChanged();
    }

    public Bitmap getFriendBitmap(int friendId) {
        return bitmapCache.get(friendId);
    }

    @NonNull
    @Override
    public FriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_item, parent, false);
        return new FriendViewHolder(view);
    }

    public void updateFriendPhoto(int userId, Bitmap bitmap) {
        bitmapCache.put(userId, bitmap);
        notifyDataSetChanged();
    }




    @Override
    public void onBindViewHolder(@NonNull FriendViewHolder holder, int position) {
        Friend friend = friends.get(position);

        holder.name.setText(friend.getLogin());
        holder.status.setText(friend.getStatus());

        Log.e("FRIEND_PHOTO",
                "id=" + friend.getId()
                        + " url=" + friend.getPhotoUrl()
                        + " base64=" + (friend.getPhotoBase64() != null));


        // 1️⃣ GOOGLE / URL
        if (friend.getPhotoUrl() != null && !friend.getPhotoUrl().isEmpty()) {

            Glide.with(holder.itemView)
                    .load(friend.getPhotoUrl())
                    .placeholder(R.drawable.friend_cat)
                    .error(R.drawable.friend_cat)
                    .circleCrop()
                    .into(holder.photo);

        }
        // 2️⃣ BASE64
        else if (friend.getPhotoBase64() != null && !friend.getPhotoBase64().isEmpty()) {
            try {
                byte[] decoded = Base64.decode(friend.getPhotoBase64(), Base64.DEFAULT);
                Bitmap bmp = BitmapFactory.decodeByteArray(decoded, 0, decoded.length);
                if (bmp != null) {
                    holder.photo.setImageBitmap(bmp);
                } else {
                    holder.photo.setImageResource(R.drawable.friend_cat);
                }
            } catch (Exception e) {
                holder.photo.setImageResource(R.drawable.friend_cat);
            }
        }
        // 3️⃣ FALLBACK
        else {
            holder.photo.setImageResource(R.drawable.friend_cat);
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onFriendClick(friend);
        });

        holder.fon.setOnClickListener(v -> {
            if (listener != null) listener.onFriendClick(friend);
        });
    }






    @Override
    public int getItemCount() {
        return friends.size();
    }

    static class FriendViewHolder extends RecyclerView.ViewHolder {
        ShapeableImageView photo;
        ImageView fon;
        TextView name, status;

        public FriendViewHolder(@NonNull View itemView) {
            super(itemView);
            photo = itemView.findViewById(R.id.imageView43);
            fon = itemView.findViewById(R.id.imageView37);
            name = itemView.findViewById(R.id.textView);
            status = itemView.findViewById(R.id.textView20);
        }
    }
}
