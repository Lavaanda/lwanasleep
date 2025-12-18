package com.example.mobilki_iyoyyy;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

    private List<ChatMessage> messages;
    private int topicId;

    public ChatAdapter(List<ChatMessage> messages, int topicId) {
        this.messages = messages;
        this.topicId = topicId;
    }

    public void setMessages(List<ChatMessage> messages) {
        this.messages = messages;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_item3, parent, false); // твоя разметка сообщения
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        ChatMessage msg = messages.get(position);

        holder.textLoginUser.setText(msg.getLogin());
        holder.textMessage.setText(msg.getContent());


        // Переход в CommentsActivity
        holder.commentsButton.setOnClickListener(v -> {
            Intent intent = new Intent(holder.itemView.getContext(), CommentsActivity.class);
            intent.putExtra("login", msg.getLogin());
            intent.putExtra("text", msg.getContent());
            intent.putExtra("id", msg.getId()); // id сообщения

            intent.putExtra("topic_id", topicId);

            holder.itemView.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    static class ChatViewHolder extends RecyclerView.ViewHolder {
        TextView textLoginUser, textMessage;
        ImageView commentsButton;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            textLoginUser = itemView.findViewById(R.id.textLoginUser);
            textMessage = itemView.findViewById(R.id.textMessage);
            commentsButton = itemView.findViewById(R.id.comments);
        }
    }
}
