package com.example.mobilki_iyoyyy;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.MessageViewHolder> {

    private Context context;
    private List<MessageModel> messages;

    public MessagesAdapter(Context context, List<MessageModel> messages) {
        this.context = context;
        this.messages = messages;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_item3, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        MessageModel msg = messages.get(position);

        holder.textLoginUser.setText(msg.getLogin());
        holder.textMessage.setText(msg.getText());

        // НАЖАТИЕ НА КНОПКУ COMMENTS
        holder.commentsButton.setOnClickListener(v -> {
            Intent intent = new Intent(context, CommentsActivity.class);

            // передаем данные выбранного сообщения
            intent.putExtra("login", msg.getLogin());
            intent.putExtra("text", msg.getText());
            intent.putExtra("id", msg.getId());

            context.startActivity(intent);
        });
    }


    @Override
    public int getItemCount() {
        return messages.size();
    }

    static class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView textLoginUser, textMessage;
        ImageView commentsButton; // <--- кнопка COMMENTS

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            textLoginUser = itemView.findViewById(R.id.textLoginUser);
            textMessage = itemView.findViewById(R.id.textMessage);
            commentsButton = itemView.findViewById(R.id.comments);
        }
    }

}
