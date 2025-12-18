package com.example.mobilki_iyoyyy;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MessageHeaderAdapter
        extends RecyclerView.Adapter<MessageHeaderAdapter.MessageViewHolder> {

    private final ChatMessage message;
    private final int topicId;

    public MessageHeaderAdapter(ChatMessage message, int topicId) {
        this.message = message;
        this.topicId = topicId;
    }

    @Override
    public int getItemCount() {
        return 1; // ВСЕГДА ОДИН ЭЛЕМЕНТ
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_item3, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull MessageViewHolder holder,
            int position
    ) {
        holder.textLoginUser.setText(message.getLogin());
        holder.textMessage.setText(message.getContent());

        // ЕСЛИ МЫ В CommentsActivity — УБИРАЕМ КНОПКУ
        if (holder.itemView.getContext() instanceof CommentsActivity) {

            holder.commentsButton.setOnClickListener(null);
            holder.commentsButton.setClickable(false);
            holder.commentsButton.setEnabled(false);
            holder.commentsButton.setVisibility(View.GONE);

        } else {
            // СТАРАЯ ЛОГИКА — БЕЗ ИЗМЕНЕНИЙ
            holder.commentsButton.setVisibility(View.VISIBLE);
            holder.commentsButton.setOnClickListener(v -> {
                Intent intent = new Intent(
                        holder.itemView.getContext(),
                        CommentsActivity.class
                );
                intent.putExtra("login", message.getLogin());
                intent.putExtra("text", message.getContent());
                intent.putExtra("id", message.getId());
                intent.putExtra("topic_id", topicId);
                holder.itemView.getContext().startActivity(intent);
            });
        }
    }

    static class MessageViewHolder extends RecyclerView.ViewHolder {

        TextView textLoginUser, textMessage;
        ImageView commentsButton;

        MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            textLoginUser = itemView.findViewById(R.id.textLoginUser);
            textMessage = itemView.findViewById(R.id.textMessage);
            commentsButton = itemView.findViewById(R.id.comments);
        }
    }
}
