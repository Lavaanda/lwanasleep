package com.example.mobilki_iyoyyy;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ForymTopicsAdapter extends RecyclerView.Adapter<ForymTopicsAdapter.TopicViewHolder> {

    private List<ForumTopic> topics = new ArrayList<>();
    private OnTopicClickListener listener;

    public interface OnTopicClickListener {
        void onClick(ForumTopic topic);
    }

    public ForymTopicsAdapter(List<ForumTopic> topics, OnTopicClickListener listener) {
        this.topics = topics;
        this.listener = listener;
    }

    public void setData(List<ForumTopic> newTopics) {
        topics.clear();
        topics.addAll(newTopics);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TopicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_item2, parent, false);
        return new TopicViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TopicViewHolder holder, int position) {
        ForumTopic topic = topics.get(position);
        holder.textView.setText(topic.topic);

        if (listener != null) {
            holder.itemView.setOnClickListener(v -> listener.onClick(topic));
        }
    }

    @Override
    public int getItemCount() {
        return topics.size();
    }

    static class TopicViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        ImageView imageView;

        TopicViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textTemaForym);
            imageView = itemView.findViewById(R.id.imageView55);
        }
    }
}
