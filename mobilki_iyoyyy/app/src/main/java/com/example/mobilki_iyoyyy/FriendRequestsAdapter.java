package com.example.mobilki_iyoyyy;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FriendRequestsAdapter
        extends RecyclerView.Adapter<FriendRequestsAdapter.ViewHolder> {

    private final List<Friend> friends;
    private final ApiService apiService;

    public FriendRequestsAdapter(List<Friend> friends) {
        this.friends = friends;
        this.apiService = RetrofitClient.getApiService();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_friend_request, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull ViewHolder holder,
            int position
    ) {
        Friend friend = friends.get(position);

        holder.txtLogin.setText(friend.getLogin());
        holder.txtId.setText("ID: " + friend.getId());

        // фото
        if (!friend.getPhotoUrl().isEmpty()) {
            Glide.with(holder.itemView.getContext())
                    .load(friend.getPhotoUrl())
                    .placeholder(R.drawable.friend_cat)
                    .error(R.drawable.friend_cat)
                    .into(holder.imgAvatar);
        } else {
            holder.imgAvatar.setImageResource(R.drawable.friend_cat);
        }

        // ✅ принять
        holder.btnAccept.setOnClickListener(v ->
                apiService.acceptFriend(friend.getId())
                        .enqueue(new ActionCallback(position))
        );

        // ❌ отклонить
        holder.btnDecline.setOnClickListener(v ->
                apiService.declineFriend(friend.getId())
                        .enqueue(new ActionCallback(position))
        );
    }

    @Override
    public int getItemCount() {
        return friends.size();
    }

    class ActionCallback implements Callback<ResponseBody> {
        private final int position;

        ActionCallback(int position) {
            this.position = position;
        }

        @Override
        public void onResponse(
                Call<ResponseBody> call,
                Response<ResponseBody> response
        ) {
            if (response.isSuccessful()) {
                friends.remove(position);
                notifyItemRemoved(position);
            }
        }

        @Override
        public void onFailure(Call<ResponseBody> call, Throwable t) {
            // можно лог
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imgAvatar, btnAccept, btnDecline;
        TextView txtLogin, txtId;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgAvatar = itemView.findViewById(R.id.imgAvatar);
            txtLogin = itemView.findViewById(R.id.txtLogin);
            txtId = itemView.findViewById(R.id.txtId);
            btnAccept = itemView.findViewById(R.id.btnAccept);
            btnDecline = itemView.findViewById(R.id.btnDecline);
        }
    }
}
